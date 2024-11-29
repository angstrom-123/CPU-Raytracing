package com.ang;

import com.ang.Render.Renderer;
import com.ang.Util.Vec3;

public class Camera {
    public double       aspectRatio        = 1.0;
    public int          imageWidth         = 100;
    public int          samplesPerPixel    =  10;
    public int          maxBounces         =  10;
    public Vec3         background         = new Vec3(0.0, 0.0, 0.0);
    public double       fov                = 90;
    public Vec3         lookFrom           = new Vec3(0.0, 0.0,  0.0);
    public Vec3         lookAt             = new Vec3(0.0, 0.0, -1.0);
    public Vec3         vUp                = new Vec3(0.0, 1.0,  0.0);
    public double       focusDistance      = 10.0;
    public double       defocusAngle       =  0.0;

    public Vec3         centre;
    public Vec3         pixel0Location;
    public Vec3         pixelDeltaU;
    public Vec3         pixelDeltaV;
    public Vec3         defocusDiskU;
    public Vec3         defocusDiskV;

    private int         imageHeight;
    private Vec3        u, v, w;
    private Renderer    renderer;


    public void init() {
        // image
        imageHeight = (int) (imageWidth / aspectRatio);
        if (imageHeight < 1) {
            imageHeight = 1;
        }

        renderer = new Renderer(imageWidth, imageHeight);

        Global.imageWidth = imageWidth;
        Global.imageHeight = imageHeight;

        centre = lookFrom;

        // camera 
        double theta = Global.deg2rad(fov);
        double h = Math.tan(theta / 2.0);
        double viewportHeight = 2.0 * h  * focusDistance;
        double viewportWidth = viewportHeight 
                             * ((double) imageWidth / (double) imageHeight);
       
        // calculate relative camera basis vectors
        w = (lookFrom.subtract(lookAt)).unitVector(); // opposite of view
        u = Vec3.cross(vUp, w).unitVector(); // right of view
        v = Vec3.cross(  w, u); // up of view

        // vectors along viewport edges
        Vec3 viewportU = u.multiply(viewportWidth); 
        Vec3 viewportV = v.negative().multiply(viewportHeight);

        // delta vectors between pixels, delta ray direction per pixel
        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);

        // calculate location of top left pixel
        Vec3 viewportOffset = (w.multiply(focusDistance))
                            .add(viewportU.divide(2.0))
                            .add(viewportV.divide(2.0));
        Vec3 viewportTopLeft = centre
                             .subtract(viewportOffset);
        pixel0Location = viewportTopLeft
                       .add((pixelDeltaU.add(pixelDeltaV))
                       .multiply(0.5));
    
        // calculate defocus disk basis vectors
        double defocusRadius = focusDistance 
                             * Math.tan(Global.deg2rad(defocusAngle / 2.0));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void sendPixelToRenderer(Vec3 unitCol, int x, int y) {
        renderer.writePixel(unitCol, x, y);
    }

    public void saveFile(String path) {
        renderer.saveFile(path);
    }
}
