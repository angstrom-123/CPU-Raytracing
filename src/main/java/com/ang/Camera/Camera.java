package com.ang.Camera;

import com.ang.Render.Renderer;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vector3;
import com.ang.Global;
import com.ang.Hittable.HittableList;

public class Camera {
    public double aspectRatio = 1.0;
    public int imageWidth = 100;
    public int samplesPerPixel = 10;
    public int maxBounces = 10;
    public Vector3 background;

    public double fov = 90;
    public Vector3 lookFrom = new Vector3(0,0,0);
    public Vector3 lookAt = new Vector3(0,0,-1);
    public Vector3 vUp = new Vector3(0,1,0);

    public double focusDistance = 10;
    public double defocusAngle = 0.0;

    private int imageHeight;
    private double pixelSamplesScale; // if 10 samples/pixel, contribute 10% each
    
    private Vector3 u,v,w; // relative camera frame basis vectors

    public Vector3 centre;
    public Vector3 pixel0Location;
    public Vector3 pixelDeltaU;
    public Vector3 pixelDeltaV;
    public Vector3 defocusDiskU;
    public Vector3 defocusDiskV;

    private Renderer renderer;

    // public void render(HittableList world) {
    //     for (int j = 0; j < imageHeight; j++) {
    //         for (int i = 0; i < imageWidth; i++) {
    //             Vector3 pixelColour = new Vector3(0,0,0);
    //             for (int sample = 0; sample < samplesPerPixel; sample++) {
    //                 Ray r = getRay(i, j);
    //                 pixelColour.ADD(rayColour(r, maxBounces, world));
    //             }
    //             renderer.writePixel(pixelColour.multiply(pixelSamplesScale));
    //         }
    //     }

    //     renderer.drawScreen();
    //     renderer.saveFile("");
    // }

    public void init() {
        // image
        imageHeight = (int) (imageWidth / aspectRatio);
        if (imageHeight < 1) {
            imageHeight = 1;
        }

        pixelSamplesScale = 1.0 / samplesPerPixel;

        renderer = new Renderer(imageWidth, imageHeight);

        Global.imageWidth = imageWidth;
        Global.imageHeight = imageHeight;

        centre = lookFrom;

        // camera 
        double theta = Global.deg2rad(fov);
        double h = Math.tan(theta / 2);
        double viewportHeight = 2 * h  * focusDistance;
        double viewportWidth = viewportHeight * ((double) imageWidth / (double) imageHeight);
       
        // calculate relative camera basis vectors
        w = (lookFrom.subtract(lookAt)).unitVector(); // opposite of view
        u = (Vector3.cross(vUp, w)).unitVector(); // right of view
        v = Vector3.cross(w, u); // up of view

        // vectors along viewport edges
        Vector3 viewportU = u.multiply(viewportWidth); // vector across horizontal viewport edge
        Vector3 viewportV = (v.negative()).multiply(viewportHeight); // vector down vertical viewport edge

        // delta vectors between pixels. this is the distance between scan points
        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);

        // location of top left pixel. this is the starting point for snanning
        Vector3 viewportTopLeft = centre.subtract((w.multiply(focusDistance)).add((viewportU.divide(2))).add((viewportV.divide(2))));
        pixel0Location = viewportTopLeft.add((pixelDeltaU.add(pixelDeltaV)).multiply(0.5));
    
        // calculate defocus disk basis vectors
        double defocusRadius = focusDistance * Math.tan(Global.deg2rad(defocusAngle / 2));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);

    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void sendPixelToRenderer(Vector3 unitCol, int x, int y) {
        // System.out.println(unitCol.x());
        renderer.writePixel(unitCol, x, y);
    }

    public void saveFile(String path) {
        renderer.saveFile(path);
    }

    private Ray getRay(int i, int j) {
        Vector3 offset = sampleSquare();
        Vector3 pixelSample = pixel0Location.add(pixelDeltaU.multiply(i + offset.x()).add(pixelDeltaV.multiply(j + offset.y())));              
        
        Vector3 rayOrigin;
        if (defocusAngle <= 0) {
            rayOrigin = centre;
        } else {
            rayOrigin = sampleDefocusDisk();
        }
        Vector3 rayDirection = pixelSample.subtract(rayOrigin);
        
        return new Ray(rayOrigin, rayDirection);
    }

    // generates random offsets to use for same-pixel sampling
    private Vector3 sampleSquare() {
        // random vector in -.5, -.5 to .5,.5 unit square
        return new Vector3(Math.random() - 0.5, Math.random() - 0.5, 0);
    }

    private Vector3 rayColour(Ray r, int depth, HittableList world) {
        // return black at bounce limit
        if (depth <= 0) {
            return new Vector3(0,0,0);        
        }

        HitRecord rec = new HitRecord();

        // return background if ray missed the world
        if (!world.hit(r, new Interval(0.001, Global.infinity), rec)) {
            return background;
        }

        RayTracker rt = new RayTracker(new Vector3(0,0,0), new Ray(new Vector3(0, 0, 0), new Vector3(0, 0, 0)));
        Vector3 colourFromEmission = rec.mat.emitted(rec.u, rec.v, rec.p);
        
        if (!rec.mat.scatter(r, rec, rt)) {
            return colourFromEmission;
        }

        Vector3 colourFromScatter = rayColour(rt.scattered, depth-1, world).multiply(rt.attenuation);

        return colourFromEmission.add(colourFromScatter);
    }

    private Vector3 sampleDefocusDisk() {
        Vector3 p = Vector3.randomInUnitDisk();
        return centre.add(defocusDiskU.multiply(p.x())).add(defocusDiskV.multiply(p.y()));
    }
}
