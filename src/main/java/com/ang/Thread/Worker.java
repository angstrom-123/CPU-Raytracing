package com.ang.Thread;

import com.ang.Camera.Camera;
import com.ang.Hittable.HittableList;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vector3;
import com.ang.Global;

public class Worker implements Runnable{
    private ThreadListener listener;

    private int startRow;
    private int endRow;
    private int startCol;
    private int endCol;

    private HittableList world;
    private Camera cam;

    public Worker(int startCol, int endCol, int startRow, int endRow){        
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public void setData(HittableList world, Camera cam) {
        this.world = world;
        this.cam = cam;
    }

    public void setListener(ThreadListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        for (int j = startRow; j < endRow; j++) {
            for (int i = startCol; i < endCol; i++) {
                Vector3 pixelColour = new Vector3(0,0,0);
                for (int sample = 0; sample < cam.samplesPerPixel; sample++) {
                    Ray r = getRay(i, j);
                    pixelColour.ADD(rayColour(r, cam.maxBounces, world));
                }
                cam.sendPixelToRenderer(pixelColour.multiply((double)1 / (double)cam.samplesPerPixel), i, j);
            }
        }
        listener.threadComplete();
    }

    private Ray getRay(int i, int j) {
        Vector3 offset = sampleSquare();
        Vector3 pixelSample = cam.pixel0Location.add(cam.pixelDeltaU.multiply(i + offset.x()).add(cam.pixelDeltaV.multiply(j + offset.y())));              
        
        Vector3 rayOrigin;
        if (cam.defocusAngle <= 0) {
            rayOrigin = cam.centre;
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
            return cam.background;
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
        return cam.centre.add(cam.defocusDiskU.multiply(p.x())).add(cam.defocusDiskV.multiply(p.y()));
    }
}