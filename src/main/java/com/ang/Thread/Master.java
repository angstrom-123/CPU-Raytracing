package com.ang.Thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import com.ang.Camera;
import com.ang.Global;
import com.ang.Hittable.HittableList;

public class Master implements ThreadListener {
    private int                     activeThreads = 0;

    private BlockingQueue<Runnable> taskQueue;
    private ExecutorService         executorService;
    private Camera                  cam;
    private HittableList            world;
    private double                  startTime;
    private int                     tileX, tileY;
    private int                     imageWidth, imageHeight;
    private int                     usedTiles;
    private int[][]                 tileSizes;

    public Master(Camera cam, HittableList world, int tileX, int tileY) {
        this.cam    = cam;
        this.world  = world;
        this.tileX  = tileX;
        this.tileY  = tileY;

        imageWidth  = Global.imageWidth;
        imageHeight = Global.imageHeight;
    }

    // when thread sends notification that it is done, it is reassigned to a
    // new block or parked.
    @Override
    public void threadComplete() {
        // if all tiles are done and all threads are parked, shuts down 
        if (usedTiles == tileSizes.length) {
            activeThreads--;
            if (activeThreads == 0) {
                shutDown();
            }
        } else {
            int[] data = tileSizes[usedTiles];

            Worker worker = new Worker(data[0], data[1], data[2], data[3]);
            worker.setListener(this);
            worker.setData(world, cam);

            addTask(worker);

            usedTiles++;
        }
    }

    @Override
    public void forceStop() {
        executorService.shutdown();
    }

    public void addTask(Runnable task) {
        taskQueue.offer(task);
        executorService.execute(task);
    }

    public void shutDown() {
        cam.saveFile("");
        
        executorService.shutdown();

        double endTime = System.currentTimeMillis();
        System.out.println(((endTime- startTime) / 1000.0)+"s To render");
    }

    public void render(int threadCount) {
        taskQueue = new LinkedBlockingQueue<>();
        executorService = Executors.newFixedThreadPool(threadCount);

        // auto calculates tile sizes if not defined
        tileX = (tileX == 0) 
        ? imageWidth 
        : tileX;

        tileY = (tileY == 0) 
        ? ((int)Math.floor(imageHeight / threadCount))
        : (tileY);

        // init
        calculateTileSizes();
        usedTiles = 0;
        startTime = (double)System.currentTimeMillis();

        // creates new workers to render tiles equal to threadCount
        for (int i = 0; i < threadCount; i++) {
            int[] data = tileSizes[usedTiles];

            Worker worker = new Worker(data[0], data[1], data[2], data[3]);
            worker.setListener(this);
            worker.setData(world, cam);
            
            addTask(worker);
            activeThreads++;
            
            usedTiles++;
        }
    }

    // calculate dimensions of each tile based on ideal dimensions
    private void calculateTileSizes() {
        // calculates amount of full tiles along each axis and remainders
        int xCount = (int)Math.floor(imageWidth / tileX);
        int remX = imageWidth % tileX;

        int yCount = (int)Math.floor(imageHeight / tileY);
        int remY = imageHeight % tileY;

        tileSizes = new int[(xCount * yCount) + remX + remY][4];
    
        // calculates start and end coords of each block
        int index = 0;
        for (int x = 0; (remX == 0) ? (x < xCount) : (x <= xCount); x++) {
            for (int y = 0; (remY == 0) ? (y < yCount) : (y <= yCount); y++) {
                int startX = x * tileX;
                int startY = y * tileY;

                // remainders are added to the final tiles in each row / column
                int endX = (x < xCount) ? (startX + tileX) : imageWidth;
                int endY = (y < yCount) ? (startY + tileY) : imageHeight;

                tileSizes[index] = new int[]{startX, endX, startY, endY};
                index++;
            }
        }
    }
}