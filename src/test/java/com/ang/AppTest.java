package com.ang;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;

/**
 * Unit test for the program. 
 */
public class AppTest 
{
    private ConfigHandler c;

    /**
     * Handles results of test, returning a pass if the renders are successful.
     */
    @Test
    public void runTestSuite() {
        boolean pass = renderScenes(); 
        if (pass) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }

    /**
     * Tries to render each scene in series, then logs the results.
     * @return output of checkResults().
     */
    private boolean renderScenes() {
        int sleepTime = 200;
        try {
            testScene1();
            while (!Global.master.renderDone) {
                Thread.sleep(sleepTime);
            }

            testScene2();
            while (!Global.master.renderDone) {
                Thread.sleep(sleepTime);
            }

            testScene3();
            while (!Global.master.renderDone) {
                Thread.sleep(sleepTime);
            }

            testScene4();
            while (!Global.master.renderDone) {
                Thread.sleep(sleepTime);
            }

            testScene5();
            while (!Global.master.renderDone) {
                Thread.sleep(sleepTime);
            }

        } catch (InterruptedException e) {
            System.out.println("Render tests interrupted");
            e.printStackTrace();
        }
        
        return checkResults();
    }

    /**
     * Checks if the results of all test renders have been saved correctly.
     * @return {@code true} if all saved files are found in the correct folder
     *         else {@code false}.
     */
    private boolean checkResults() {
        String path = "/rendersTest";
        for (int i = 1; i < 6; i++) {
            if (!new File(path + "scene_" + i + "_test").isFile()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Initializes the first test scene. (Random balls).
     */
    private void testScene1() {
        c = new ConfigHandler();
        c.setScene(1);
        c.configCam(20, 3, 3);
        c.configThreads(2, 0, 6);
        c.configFile("rendersTest", "scene_1_test", true);
        c.callRender();
    }

    /**
     * Initializes the second test scene. (Glass knight).
     */
    private void testScene2() {
        c = new ConfigHandler();
        c.setScene(2);
        c.configCam(20, 3, 3);
        c.configThreads(2, 0, 6);
        c.configFile("rendersTest", "scene_2_test", true);
        c.callRender();
    }

    /**
     * Initializes the third test scene. (Globe).
     */
    private void testScene3() {
        c = new ConfigHandler();
        c.setScene(3);
        c.configCam(20, 3, 3);
        c.configThreads(2, 0, 6);
        c.configFile("rendersTest", "scene_3_test", true);
        c.callRender();
    }

    /**
     * Initializes the fourth test scene. (Emission).
     */
    private void testScene4() {
        c = new ConfigHandler();
        c.setScene(4);
        c.configCam(20, 3, 3);
        c.configThreads(2, 0, 6);
        c.configFile("rendersTest", "scene_4_test", true);
        c.callRender();
    }

    /**
     * Initializes the fifth test scene. (Cornell box).
     */
    private void testScene5() {
        c = new ConfigHandler();
        c.setScene(5);
        c.configCam(20, 3, 3);
        c.configThreads(2, 0, 6);
        c.configFile("rendersTest", "scene_5_test", true);
        c.callRender();
    }
}
