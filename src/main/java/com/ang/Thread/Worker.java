package com.ang.Thread;

import com.ang.Camera.Camera;
import com.ang.Hittable.HittableList;
import com.ang.Render.Renderer;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vector3;
import com.ang.Global;

public class Worker implements Runnable {
    private boolean isStop = false;
    private int startRow;
    private int endRow;
    private Renderer r;

    private Vector3 centre;
    private Vector3 pixel0Location;
    private Vector3 deltaU;
    private Vector3 deltaV;
    private Vector3 defocusU;
    private Vector3 defocusV;
    private double samplesPerPixel;
    private double defocusAngle;
    private int maxBounces;
    private Vector3 bg;

    public Worker(int startRow, int endRow, Renderer r, Camera cam){
        this.startRow = startRow;
        this.endRow = endRow;
        this.r = r;

        centre = cam.getCentre();
        pixel0Location = cam.getPixel0Pos();
        deltaU = cam.getDeltaU();
        deltaV = cam.getDeltaV();
        defocusU = cam.getDefocusU();
        defocusV = cam.getDefocusV();
        samplesPerPixel = cam.getSamplesPerPixel();
        defocusAngle = cam.getDefocusAngle();
        maxBounces = cam.getMaxBounces();
        bg = cam.getBackground();
    }

    public synchronized void doStop(){
        this.isStop = true;
    }

    private synchronized boolean keepRunning(){
        return this.isStop == false;
    }

    @Override
    public void run() {
        if (keepRunning()) {
            for (int j = startRow; j < endRow; j++) {
                for (int i = 0; i < r.getImageWidth(); i++) {
                    Vector3 pixelColour = new Vector3(0,0,0);
                    for (int sample = 0; sample < samplesPerPixel; sample++) {
                        Ray r = getRay(i, j);
                        pixelColour.ADD(rayColour(r, maxBounces, Global.world));
                    }
                    r.writePixel(pixelColour.multiply(1 / samplesPerPixel), i, j);
                }
            }
        }
    }

    private Ray getRay(int i, int j) {
        Vector3 offset = sampleSquare();
        Vector3 pixelSample = pixel0Location.add(deltaU.multiply(i + offset.x()).add(deltaV.multiply(j + offset.y())));              
        
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
            return bg;
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
        return centre.add(defocusU.multiply(p.x())).add(defocusV.multiply(p.y()));
    }
}