package com.ang.Thread;

import com.ang.Camera.Camera;

public class Master {
    private static Thread[] threads;
    private static Worker[] workers;

    public static void render(int threadCount, Camera cam) {
        threads = new Thread[threadCount];
        workers = new Worker[threadCount];

        cam.init();

        int height = cam.getRenderer().getImageHeight();

        int rowsPerThread = (int)Math.floor(height / threadCount);
        int remainder = height % threadCount;

        int currentRow = 0;

        for (int i = 0; i < threadCount; i++) {
            Worker worker;
            if (i != threadCount -1) {
                worker = new Worker(currentRow, currentRow + rowsPerThread, cam.getRenderer(), cam);
            } else {
                worker = new Worker(currentRow, currentRow + rowsPerThread + remainder, cam.getRenderer(), cam);
            }

            currentRow += rowsPerThread;

            Thread thread = new Thread(worker);
            threads[i] = thread;
            workers[i] = worker;
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        cam.getRenderer().saveFile("");
    }

    public static void terminate() {
        for (Worker worker : workers) {
            worker.doStop();
        }
    }
}
