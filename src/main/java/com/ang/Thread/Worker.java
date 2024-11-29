package com.ang.Thread;

import com.ang.Hittable.HittableList;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;
import com.ang.Camera;
import com.ang.Global;

public class Worker implements Runnable{
    private ThreadListener  listener;
    private int             startRow;
    private int             endRow;
    private int             startCol;
    private int             endCol;
    private HittableList    world;
    private Camera          cam;
    private boolean         exit;

    public Worker(int startCol, int endCol, int startRow, int endRow){        
        this.startRow   = startRow;
        this.endRow     = endRow;
        this.startCol   = startCol;
        this.endCol     = endCol;
        this.exit       = false;
    }

    public void setData(HittableList world, Camera cam) {
        this.world  = world;
        this.cam    = cam;
    }

    public void setListener(ThreadListener listener) {
        this.listener = listener;
    }

    public void doStop() {
        exit = true;
    }

    // ray tracing logic
    @Override
    public void run() {
        // loops over active rows, calculates output colour for each pixel
        for (int j = startRow; j < endRow; j++) {
            for (int i = startCol; i < endCol; i++) {
                if (!exit) {
                    // pixel colour is defaulted as black (no light)
                    Vec3 pixelCol = new Vec3(0,0,0);
                    for (int samp = 0; samp < cam.samplesPerPixel; samp++) {
                        // pixel collects colour of each surface the ray hits
                        Ray r = getRay(i, j);
                        pixelCol = pixelCol
                                .add(rayColour(r, cam.maxBounces, world));
                    }

                    // sends normalized pixel colour to renderer to draw
                    Vec3 outCol = pixelCol
                                .multiply(1d / (double)cam.samplesPerPixel);
                    cam.sendPixelToRenderer(outCol, i, j);
                }
            }
        }

        // notifies master that job is finished
        listener.threadComplete(this);
    }

    // generates jittered rays for a given pixel
    private Ray getRay(int x, int y) {
        Vec3 offset = sampleSquare();

        Vec3 xOffset = cam.pixelDeltaU.multiply(x + offset.x());
        Vec3 yOffset = cam.pixelDeltaV.multiply(y + offset.y());

        Vec3 pixelSample = cam.pixel0Location.add(xOffset).add(yOffset);              
        
        Vec3 rayOrigin = (cam.defocusAngle <= 0) 
        ? cam.centre 
        : sampleDefocusDisk();

        Vec3 rayDirection = pixelSample.subtract(rayOrigin);
        
        return new Ray(rayOrigin, rayDirection);
    }

    // generates random offsets to use for same-pixel sampling
    private Vec3 sampleSquare() {
        // random vector in -.5, -.5 to .5,.5 unit square
        return new Vec3(Math.random() - 0.5, Math.random() - 0.5, 0.0);
    }

    // recursively calculates ray colour for depth bounces
    private Vec3 rayColour(Ray r, int depth, HittableList world) {
        // return black at bounce limit
        if (depth <= 0) {
            return new Vec3(0.0, 0.0, 0.0);        
        }

        HitRecord rec = new HitRecord();

        // return background if ray missed the world
        if (!world.hit(r, new Interval(0.001, Global.infinity), rec)) {
            return cam.background;
        }

        RayTracker rt = new RayTracker();

        // handles emission first
        Vec3 colFromEmission = rec.mat.emitted(rec.u, rec.v, rec.p);
        
        // if directly hit light source, immediately return emission colour
        if (!rec.mat.scatter(r, rec, rt)) {
            return colFromEmission;
        }

        // otherwise calculates ray colour 
        Vec3 colFromScatter = rayColour(rt.scattered, depth-1, world);
        colFromScatter = colFromScatter.multiply(rt.attenuation);

        return colFromEmission.add(colFromScatter);
    }

    // generates random points within disk for depth of field
    private Vec3 sampleDefocusDisk() {
        Vec3 p = Vec3.randomInUnitDisk();
        Vec3 xOffset = cam.defocusDiskU.multiply(p.x());
        Vec3 yOffset = cam.defocusDiskV.multiply(p.y());

        return cam.centre.add(xOffset).add(yOffset);
    }
}