package com.ang.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.ang.Global;
import com.ang.Camera.Camera;
import com.ang.Hittable.HittableList;

public class Master implements ThreadListener {
    private BlockingQueue<Runnable> taskQueue;
    private ExecutorService executorService;

    private int activeThreads = 0;

    private int[][] tileSizes;
    private int completedTiles;

    private int imageWidth;
    private int imageHeight;

    private int idealTileX;
    private int idealTileY;

    private double startTime;

    private Camera cam;
    private HittableList world;

    public Master(Camera cam, HittableList world, int tileX, int tileY) {
        this.cam = cam;
        this.world = world;

        idealTileX = tileX;
        idealTileY = tileY;
    }

    @Override
    public void threadComplete() {
        if (completedTiles == tileSizes.length) {
            // shutDown();
            activeThreads--;
            if (activeThreads == 0) {
                shutDown();
            }
        } else {
            int[] data = tileSizes[completedTiles];

            Worker worker = new Worker(data[0], data[1], data[2], data[3]);
            worker.setListener(this);
            worker.setData(world, cam);

            addTask(worker);

            completedTiles++;
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
        System.out.println((((double)System.currentTimeMillis() - startTime) / 1000)+"s To render");
    }

    public void render(int threadCount) {
        taskQueue = new LinkedBlockingQueue<>();
        executorService = Executors.newFixedThreadPool(threadCount);

        imageWidth = Global.imageWidth;
        imageHeight = Global.imageHeight;

        if (idealTileX == 0) {
            idealTileX = imageWidth;
        }
        if (idealTileY == 0) {
            idealTileY = (int)Math.floor(imageHeight / threadCount);
        }

        tileSizes = prefetchTileDimensions();
        completedTiles = 0;
        startTime = (double)System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            int[] data = tileSizes[completedTiles];

            Worker worker = new Worker(data[0], data[1], data[2], data[3]);
            worker.setListener(this);
            worker.setData(world, cam);
            
            addTask(worker);
            activeThreads++;
            
            completedTiles++;
        }
    }

    private int[][] prefetchTileDimensions() {
        int a = (int)Math.floor(imageWidth / idealTileX);
        int ar = imageWidth % idealTileX;

        int b = (int)Math.floor(imageHeight / idealTileY);
        int br = imageHeight % idealTileY;

        int[][] output = new int[a*b+ar+br][4];
        int index = 0;
    
        for (int x = 0; (ar == 0) ? (x < a) : (x <= a); x++) {
            for (int y = 0; (br == 0) ? (y < b) : (y <= b); y++) {
                int startX = x * idealTileX;
                int startY = y * idealTileY;

                int endX = x < a ? startX + idealTileX : imageWidth;
                int endY = y < b ? startY + idealTileY : imageHeight;

                output[index] = new int[]{startX, endX, startY, endY};
                index++;
            }
        }

        return output;
    }
}
