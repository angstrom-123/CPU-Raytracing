package com.ang.Thread;

import com.ang.Hittable.HittableList;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;
import com.ang.Camera;
import com.ang.Global;

/**
 * Runnable worker to be passed to threads dispatched by the ExecutorService in
 * the thread master.
 */
public class Worker implements Runnable{
    private ThreadListener  listener; // interface with Master
    private int             startRow;
    private int             endRow;
    private int             startCol;
    private int             endCol;
    private HittableList    world;
    private Camera          cam;
    private boolean         exit;

    /**
     * Constructs a worker with the dimensions of the tile it should work on.
     * @param startCol index of the column where this worker's tile starts.
     * @param endCol index of the column where this worker's tile ends.
     * @param startRow index of the row where this worker's tile starts.
     * @param endRow index of the row where this worker's tile ends.
     */
    public Worker(int startCol, int endCol, int startRow, int endRow){        
        this.startRow   = startRow;
        this.endRow     = endRow;
        this.startCol   = startCol;
        this.endCol     = endCol;
        this.exit       = false;
    }

    /**
     * Assigns the Camera and HittableList to be used for the render outside
     * of the constructor for clarity.
     * @param world the HittableList to be used for the render.
     * @param cam the Camera to be used for the render.
     */
    public void setData(HittableList world, Camera cam) {
        this.world  = world;
        this.cam    = cam;
    }

    /**
     * Assigns the Interface to be used to notify the Master that the tile is 
     * complete.
     * @param listener Interface with Master.
     */
    public void setListener(ThreadListener listener) {
        this.listener = listener;
    }

    /**
     * Sets exit to true, interrupting the render loop.
     */
    public void doStop() {
        exit = true;
    }

    /**
     * Loops over each pixel in the tile to render and sends rays into the 
     * scene equal to samplesPerPixel. Each ray is then allowed to interact with
     * hittables in the scene until maxBounces is reached. Then the colour that
     * it has collected from each interaction is returned as the pixel colour.
     * Master is notified when tile is complete.
     */
    @Override
    public void run() {
        // loops over active rows, calculates output colour for each pixel
        for (int j = startRow; j < endRow; j++) {
            for (int i = startCol; i < endCol; i++) {
                if (!exit) {
                    Vec3 pixelCol = new Vec3(0,0,0);
                    for (int samp = 0; samp < cam.samplesPerPixel; samp++) {
                        Ray r = getRay(i, j); // sub pixel sampling
                        pixelCol = pixelCol
                                   .add(rayColour(r, cam.maxBounces, world));
                    }

                    // normalizes pixel colour.
                    Vec3 outCol = pixelCol
                                  .multiply(1d / (double)cam.samplesPerPixel);
                    cam.sendPixelToRenderer(outCol, i, j);
                }
            }
        }

        listener.threadComplete(this);
    }

    /**
     * Generates a random ray for a given pixel. Applies slight sub-pixel 
     * jittering to the ray direction. This acts as anti-aliasing. Also performs
     * additional jittering on ray origin and direction for depth of field.
     * @param x first screen space coordinate of pixel to be sampled for.
     * @param y second screen space coordinate of pixel to be sampled for.
     * @return new jittered Ray.
     */
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

    /**
     * Generates a random ray direction within a unit square centred around 0,0.
     * This simulates the extremely high resolution of the real world.
     * @return vector representing sampled ray direction.
     */
    private Vec3 sampleSquare() {
        // random vector in (-0.5, -0.5) to (0.5, 0.5) unit square
        return new Vec3(Math.random() - 0.5, Math.random() - 0.5, 0.0);
    }

    /**
     * Calculates the colour of the ray after its interactions with the scene
     * recursively.
     * @param r the ray to be cast into the scene.
     * @param depth the amount of remaining bounces that the ray can perform.
     * @param world the HittableList for the ray to interact with.
     * @return vector representing rgb colour of the ray after its bounces.
     */
    private Vec3 rayColour(Ray r, int depth, HittableList world) {
        if (depth <= 0) {
            return new Vec3(0.0, 0.0, 0.0);        
        }

        HitRecord rec = new HitRecord();

        if (!world.hit(r, new Interval(0.001, Global.infinity), rec)) {
            return cam.background;
        }

        RayTracker rt = new RayTracker();

        Vec3 colFromEmission = rec.mat.emitted(rec.u, rec.v, rec.p);
        
        if (!rec.mat.scatter(r, rec, rt)) { // if no scatter then it must emit
            return colFromEmission;
        }

        Vec3 colFromScatter = rayColour(rt.scattered, depth-1, world);
        colFromScatter = colFromScatter.multiply(rt.attenuation);

        return colFromEmission.add(colFromScatter);
    }

    /**
     * Generates a random ray direction within a disk of size specified by 
     * defocusDiskU and defocusDiskV to be used for depth of field. This 
     * simulates light hitting a camera sensor that isn't in focus.
     * @return vector representing sampled ray direction.
     */
    private Vec3 sampleDefocusDisk() {
        Vec3 p = Vec3.randomInUnitDisk();
        Vec3 xOffset = cam.defocusDiskU.multiply(p.x());
        Vec3 yOffset = cam.defocusDiskV.multiply(p.y());

        return cam.centre.add(xOffset).add(yOffset);
    }
}