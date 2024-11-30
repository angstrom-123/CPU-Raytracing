package com.ang;

import com.ang.Render.Renderer;
import com.ang.Util.Vec3;

/**
 * Camera contains information about the viewport, image and virtual camera to 
 * be used when rendering the scene.
 */
public class Camera {
    public double   aspectRatio     = 1.0; // width / height
    public int      imageWidth      = 100;
    public int      samplesPerPixel =  10; // num of rays to cast per pixel
    public int      maxBounces      =  10;
    public Vec3     background      = new Vec3(0.0, 0.0, 0.0); // bg col
    public double   fov             = 90;
    public Vec3     lookFrom        = new Vec3(0.0, 0.0,  0.0); // position
    public Vec3     lookAt          = new Vec3(0.0, 0.0, -1.0); // target
    public Vec3     vUp             = new Vec3(0.0, 1.0,  0.0); // up vector
    public double   focusDistance   = 10.0;
    public double   defocusAngle    =  0.0;

    public Vec3     centre; // position vector of centre of camera
    public Vec3     pixel0Location; // screen-space position vector of top left
    public Vec3     pixelDeltaU; // horizontal offset between pixels
    public Vec3     pixelDeltaV; // vertical offset between pixels
    public Vec3     defocusDiskU; // horizontal size of defocus disk
    public Vec3     defocusDiskV; // vertical size of defocus disk
    public Renderer renderer;

    private int     imageHeight;
    private Vec3    u, v, w; // camera basis vectors

    /**
     * Calculates all values required to render with this camera.
     */
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
        double theta = Global.degToRad(fov);
        double h = Math.tan(theta / 2.0);
        double viewportHeight = 2.0 * h  * focusDistance;
        double viewportWidth = viewportHeight 
                               * ((double) imageWidth / (double) imageHeight);
       
        // calculate relative camera basis vectors
        w = (lookFrom.subtract(lookAt)).unitVector();   // opposite of view
        u = Vec3.cross(vUp, w).unitVector();            // right of view
        v = Vec3.cross(  w, u);                         // up of view

        // vectors along viewport edges
        Vec3 viewportU = u.multiply(viewportWidth); 
        Vec3 viewportV = v.negative().multiply(viewportHeight);

        // delta vectors between pixels 
        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);

        // calculates location of top left pixel, used as a basis for all other
        // pixel locations
        Vec3 viewportOffset = (w.multiply(focusDistance))
                              .add(viewportU.divide(2.0))
                              .add(viewportV.divide(2.0));
        Vec3 viewportTopLeft = centre
                               .subtract(viewportOffset);
        pixel0Location = viewportTopLeft
                         .add((pixelDeltaU.add(pixelDeltaV))
                         .multiply(0.5));
    
        // calculate defocus disk basis vectors for depth of field
        double defocusRadius = focusDistance 
                               * Math.tan(Global.degToRad(defocusAngle / 2.0));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);
    }

    /**
     * Passes a pixel to the renderer to be drawn to the screen.
     * @param unitColour the vector holding rgb values for the pixel to be
     *                   written. Normalized in the range (0,1). In linear 
     *                   colour-space.
     * @param x first screen-space coordinate of the pixel to be written.
     * @param y second screen-space coordinate of the pixel to be written.
     */
    public void sendPixelToRenderer(Vec3 unitCol, int x, int y) {
        renderer.writePixel(unitCol, x, y);
    }

    /**
     * Sends a information about the file path to the renderer for saving image.
     * @param path the path to which to save the image.
     * @param name the name to be assigned to the image upon saving.
     */
    public void saveFile(String path, String name) {
        renderer.saveFile(path, name);
    }
}
