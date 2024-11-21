package com.ang;

import com.ang.Utils.CameraController;
import com.ang.Utils.HitRecord;
import com.ang.Utils.InputHandler;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Renderer;
import com.ang.Utils.Vector3;
import com.ang.World.HittableList;

public class Camera implements MainInterface{
    public double aspectRatio = 1.0;
    public int imageWidth = 100;
    public int samplesPerPixel = 10;
    public int maxBounces = 10;

    public double fov = 90;
    public Vector3 lookFrom = new Vector3(0,0,0);
    public Vector3 lookAt = new Vector3(0,0,-1);
    public Vector3 vUp = new Vector3(0,1,0);

    public double focusDistance = 10;
    public double defocusAngle = 0.0;

    private int imageHeight;
    private double pixelSamplesScale; // if 10 samples/pixel, contribute 10% each
    private Vector3 centre;
    private Vector3 pixel0Location;
    private Vector3 pixelDeltaU;
    private Vector3 pixelDeltaV;
    private Vector3 u,v,w; // relative camera frame basis vectors
    private Vector3 defocusDiskU;
    private Vector3 defocusDiskV;

    private Renderer renderer;
    private HittableList worldStore;
    private CameraController controller;
    private boolean initDone = false;

    private double moveStep = 1;
    private double turnStep = 4;

    public void render(HittableList world) {
        if (!initDone) {
            worldStore = world;
            init();
            initDone = true;
        } else {
            update();
        }

        for (int j = 0; j < imageHeight; j++) {
            for (int i = 0; i < imageWidth; i++) {
                // 
                // SHADING WITH NORMALS
                // 
                // Vector3 pixelCentre = pixel0Location.add(pixelDeltaU.multiply(i)).add(pixelDeltaV.multiply(j));
                // Vector3 rayDirection = pixelCentre.subtract(centre);
                // Ray r = new Ray(centre, rayDirection);
                // HitRecord rec = new HitRecord();
                // Vector3 pixelColour;
                // if (world.hit(r, new Interval(0.001, Global.infinity), rec)) {
                //     if (rec.frontFace) {
                //         pixelColour = rec.normal;
                //     } else {
                //         pixelColour = new Vector3(0,0,0);
                //     }
                // } else {
                //     Vector3 unitDirection = (r.direction()).unitVector();
                //     double a = 0.5 * (unitDirection.y() + 1.0);
                //     pixelColour = new Vector3(1, 1, 1).multiply(1 - a).add(new Vector3(0.5, 0.7, 1).multiply(a));
                // }
                // renderer.writePixel(pixelColour);

                //
                //   SHADING WITH RT
                //
                Vector3 pixelColour = new Vector3(0,0,0);
                for (int sample = 0; sample < samplesPerPixel; sample++) {
                    Ray r = getRay(i, j);
                    pixelColour.ADD(rayColour(r, maxBounces, world));
                }
                renderer.writePixel(pixelColour.multiply(pixelSamplesScale));
            }
        }

        renderer.drawScreen();
        // renderer.saveFile("");
    }

    private void init() {
        // image
        imageHeight = (int) (imageWidth / aspectRatio);
        if (imageHeight < 1) {
            imageHeight = 1;
        }

        pixelSamplesScale = 1.0 / samplesPerPixel;

        renderer = new Renderer(imageWidth, imageHeight, new InputHandler(this));

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

        // camera movement class
        controller = new CameraController(w, u, v, lookFrom, lookAt);

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

    private void update() {        
        centre = lookFrom;

        double theta = Global.deg2rad(fov);
        double h = Math.tan(theta / 2);
        double viewportHeight = 2 * h  * focusDistance;
        double viewportWidth = viewportHeight * ((double) imageWidth / (double) imageHeight);
       
        // calculate relative camera basis vectors
        w = (lookFrom.subtract(lookAt)).unitVector(); // opposite of view
        u = (Vector3.cross(vUp, w)).unitVector(); // right of view
        v = Vector3.cross(w, u); // up of view

        controller.set(w, u, v, lookFrom, lookAt);

        // vectors along and down viewport
        Vector3 viewportU = u.multiply(viewportWidth); // vector across horizontal viewport edge
        Vector3 viewportV = (v.negative()).multiply(viewportHeight); // vector down vertical viewport edge

        // vector between scan points
        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);

        // top left pixel
        Vector3 viewportTopLeft = centre.subtract((w.multiply(focusDistance)).add((viewportU.divide(2))).add((viewportV.divide(2))));
        pixel0Location = viewportTopLeft.add((pixelDeltaU.add(pixelDeltaV)).multiply(0.5));

        // calculate defocus disk basis vectors
        double defocusRadius = focusDistance * Math.tan(Global.deg2rad(defocusAngle / 2));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);
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
        if (depth <= 0) {
            return new Vector3(0,0,0); // return black at bounce limit            
        }

        HitRecord rec = new HitRecord();
        // ignore extremely close hits to avoid floating point errors
        if (world.hit(r, new Interval(0.001, Global.infinity), rec)) {
            RayTracker rt = new RayTracker(new Vector3(0,0,0), new Ray(new Vector3(0, 0, 0), new Vector3(0, 0, 0)));
            if (rec.mat.scatter(r, rec, rt)) {
                return rayColour(rt.scattered, depth-1, world).multiply(rt.attenuation);
            }
            return new Vector3(0,0,0);
        }

        // blend between white and blue skybox
        Vector3 unitDirection = (r.direction()).unitVector();
        double a = 0.5 * (unitDirection.y() + 1.0);
        return new Vector3(1, 1, 1).multiply(1 - a).add(new Vector3(0.5, 0.7, 1).multiply(a));
    }

    private Vector3 sampleDefocusDisk() {
        Vector3 p = Vector3.randomInUnitDisk();
        return centre.add(defocusDiskU.multiply(p.x())).add(defocusDiskV.multiply(p.y()));
    }

    public void moveForward() {
        Vector3[] nextVectors = controller.move(0, 0, -1, moveStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void moveLeft() {
        Vector3[] nextVectors = controller.move(-1, 0, 0, moveStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void moveBack() {
        Vector3[] nextVectors = controller.move(0, 0, 1, moveStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void moveRight() {
        Vector3[] nextVectors = controller.move(1, 0, 0, moveStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void turnUp() {
        Vector3[] nextVectors = controller.turn(u.x(), u.y(), u.z(), turnStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void turnLeft() {
        Vector3[] nextVectors = controller.turn(vUp.x(), vUp.y(), vUp.z(), turnStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void turnDown() {
        Vector3[] nextVectors = controller.turn(-u.x(), -u.y(), -u.z(), turnStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }

    public void turnRight() {
        Vector3[] nextVectors = controller.turn(-vUp.x(), -vUp.y(), -vUp.z(), turnStep);
        lookFrom = nextVectors[0];
        lookAt = nextVectors[1];
        render(worldStore);
    }
}
