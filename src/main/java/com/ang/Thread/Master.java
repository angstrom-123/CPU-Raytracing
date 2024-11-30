package com.ang.Thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import com.ang.Camera;
import com.ang.Global;
import com.ang.Hittable.HittableList;

/*
 * Thread master. Controls all threads, their workers, and their workload. Uses
 * an ExecutorService to handle the threads. Each worker is responsible for 
 * rendering a smaller "tile" (section of the whole image) represented by the 
 * screen-space coordinates of the start and end rows and columns.
 */
public class Master implements ThreadListener {
    public boolean                  renderDone = true;
    
    private int                     activeThreads = 0;

    private BlockingQueue<Runnable> taskQueue; // Queue of workers
    private ExecutorService         executorService;
    private Camera                  cam;
    private HittableList            world;
    private Worker[]                workers;
    private double                  startTime;
    private int                     tileX, tileY; // dimensions of tiles
    private int                     usedTiles; // num of finished + WIP tiles
    private int                     threadCount;
    private int[][]                 tileSizes; // array of tile dimensions
    private boolean                 save; // boolean, save image or not?
    private String                  path;
    private String                  fileName;

    /**
     * Constructs the master with the Camera and HittableList to use for render.
     * @param cam the Camera to be used for the render.
     * @param world the HittableList to be used for the render.
     */
    public Master(Camera cam, HittableList world) {
        this.cam   = cam;
        this.world = world;
    }

    /**
     * Assigns a new Camera and HittableList to the master (used when rendering
     * multiple images in one session).
     * @param cam the Camera to be used for the render.
     * @param world the HittableList to be used for the render.
     */
    public void set(Camera cam, HittableList world) {
        this.cam        = cam;
        this.world      = world;
        this.renderDone = false;
    }

    /**
     * Sets the path and filename where to save the render result.
     * @param path the path to which to save the image.
     * @param fileName the name to be assigned to the image upon saving.
     * @param save boolean, should save image or not?
     */
    public void setSavePath(String path, String fileName, boolean save) {
        this.path = path;
        this.fileName = fileName;
        this.save = save;
    }

    /**
     * Sets the dimensions of tiles to be rendered by each worker.
     * @param x the width of the tile to be rendered by each worker.
     * @param y the height of the tile to be rendered by each worker.
     */
    public void setTileSize(int x, int y) {
        tileX = x;
        tileY = y;
    }

    /**
     * Sets the amount of threads for the ExecutorService to dispatch to render.
     * For best results on weaker computers (eg. laptops) a low thread count 
     * should be used (1-3). On more powerful computers (especially PC's with
     * newer CPU's) a higher thread count can be used (3-10). Performance will
     * vary between systems.
     * @param threadCount the amount of threads to use for rendering.
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        this.workers = new Worker[threadCount];
    }

    /**
     * When a worker completes its tile it notifies the master. The worker is 
     * then parked and replaced by a new worker with a new tile. If all tiles 
     * are completed or in progress then the worker is parked and a shutdown of
     * remaining threads is queued.
     * @param w the worker that completed their worload.
     */
    @Override
    public void threadComplete(Worker w) {
        // if all tiles are done and all threads are parked, shuts down 
        if (usedTiles == tileSizes.length) {
            activeThreads--;

            int index = findWorker(w);
            if (index < threadCount) {
                workers[index] = null;
            }

            if (activeThreads == 0) {
                if (save) {
                    cam.saveFile(path, fileName);
                }
                shutDown();
            }
        } else {
            int[] data = tileSizes[usedTiles]; // tile dimensions

            Worker worker = new Worker(data[0], data[1], data[2], data[3]);
            worker.setListener(this);
            worker.setData(world, cam);

            addTask(worker);

            usedTiles++;
        }
    }

    /**
     * Interrupts all threads and workers and clears worker array. This causes
     * the ExecutorService to throw an exception. This has no known effect on 
     * the program and is currently unresolved.
     */
    @Override
    public void forceStop() {
        renderDone      = true;
        usedTiles       = 0;
        activeThreads   = 0;

        for (Worker w : workers) {
            if (w != null) {
                w.doStop();
                w = null;
            }
        }

        taskQueue.clear();
        executorService.shutdownNow();
    }

    /**
     * Dispatches another worker to work on the render. The worker is tracked
     * in the workers array.
     * @param task the worker to be dispatched.
     */
    private void addTask(Runnable task) {
        taskQueue.offer(task);
        executorService.execute(task);
       
        int index = findEmptyWorkerSlot();
        if (index < threadCount) {
            workers[index] = (Worker) task;
        }
    }
    
    /**
     * Searches for a particular worker in the workers array.
     * @param w the worker to search for.
     * @return the index in workers array of { @param w } if the worker is found
     * else returns threadCount. This is out of bounds of workers array and is 
     * used as an error code.
     */
    private int findWorker(Worker w) {
        for (int i = 0; i < workers.length; i++) {
            if (workers[i] == w) {
                return i;
            }
        }

        return threadCount;
    }

    /**
     * Searches for an unoccupied space in the workers array.
     * @return the index of the empty slot in the workers array if there is one
     * else returns threadCount. This is out of bounds of workers array and is 
     * used as an error code.
     */
    private int findEmptyWorkerSlot() {
        for (int i = 0; i < workers.length; i++) {
            if (workers[i] == null) {
                return i;
            }
        }
        return threadCount;
    }

    /**
     * Queues termination of all threads. Threads are parked after their workers
     * complete their workload. Also displays the time taken to render in the
     * terminal.
     */
    private void shutDown() {        
        renderDone = true;
        executorService.shutdown();

        double endTime = System.currentTimeMillis();
        System.out.println(((endTime- startTime) / 1000.0)+"s To render");
    }

    /**
     * Calculates the start and end rows and columns for each worker and uses 
     * ExecutorService to dispatch threads to the image.
     */
    public void render() {
        taskQueue = new LinkedBlockingQueue<>();
        executorService = Executors.newFixedThreadPool(threadCount);

        // if tile dimensions are set to 0 then they are assigned to maximum.
        tileX = (tileX == 0) 
        ? Global.imageWidth 
        : tileX;
        tileY = (tileY == 0) 
        ? ((int)Math.floor(Global.imageHeight / threadCount))
        : (tileY);

        // clamps tile sizes to image dimensions
        tileX = (tileX > Global.imageWidth)
        ? Global.imageWidth
        : tileX;

        tileY = (tileY > Global.imageHeight) 
        ? Global.imageHeight
        : tileY;

        calculateTileSizes();
        usedTiles = 0;
        startTime = (double)System.currentTimeMillis();

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

    /**
     * Calculates the start and end rows of each tile for workers to work on.
     * If the image width or height is not divisible by the tile size then new
     * tiles are created to account for the remainders.
     */
    private void calculateTileSizes() {
        int xNum = (int)Math.floor(Global.imageWidth / tileX);
        int xRem = (Global.imageWidth % tileX > 0) ? 1 : 0;

        int yNum = (int)Math.floor(Global.imageHeight / tileY);
        int yRem = (Global.imageHeight % tileY > 0) ? 1 : 0;

        tileSizes = new int[(xNum + xRem) * (yNum + yRem)][4];
    
        int index = 0;
        for (int x = 0; x < xNum + xRem; x++) {
            for (int y = 0; y < yNum + yRem; y++) {
                int startX = x * tileX;
                int startY = y * tileY;

                int endX = (x < xNum) ? (startX + tileX) : Global.imageWidth;
                int endY = (y < yNum) ? (startY + tileY) : Global.imageHeight;

                tileSizes[index] = new int[]{startX, endX, startY, endY};

                index++;
            }
        }
    }
}
