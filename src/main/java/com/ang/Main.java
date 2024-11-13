package com.ang;

import com.ang.Materials.*;
import com.ang.Utils.Vector3;
import com.ang.World.HierarchyNode;
import com.ang.World.HittableList;
import com.ang.World.Sphere;

public class Main 
{
    public static void main( String[] args )
    {
        // Camera
        Camera cam = new Camera();

        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 20;
        cam.maxBounces = 50;

        cam.fov = 20;
        cam.lookFrom = new Vector3(13, 2, 3);
        cam.lookAt = new Vector3(0, 0, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.defocusAngle = 0.6;
        cam.focusDistance = 10.0;

        // world
        HittableList world = createWorld();
        Global.counter = 0;

        //render(cam, world);
        runTestSuite(cam, world);
    }

    public static HittableList createWorld() {
        HittableList world = new HittableList(1000);

        // random spheres
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = Math.random();
                Vector3 centre = new Vector3(a + 0.9 * Math.random(), 0.2, b + 0.9 * Math.random());

                if (centre.subtract(new Vector3(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;

                    if (chooseMat < 0.8) {
                        Vector3 albedo = Vector3.random().multiply(Vector3.random());
                        sphereMaterial = new Lambertian(albedo);
                    } else if (chooseMat < 0.95) {
                        Vector3 albedo = Vector3.random(0.5, 1);
                        double fuzziness = Global.randomInRange(0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzziness);
                    } else {
                        sphereMaterial = new Dielectric(1.5);
                    }

                    world.add(new Sphere(centre, 0.2, sphereMaterial));
                }
            }
        }

        // ground
        Material groundMaterial = new Lambertian(new Vector3(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, groundMaterial));
        
        // spheres
        Material mat1 = new Dielectric(1.5);
        world.add(new Sphere(new Vector3(0, 1, 0), 1.0, mat1));
        
        Material mat2 = new Lambertian(new Vector3(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vector3(-4, 1, 0), 1.0, mat2));

        Material mat3 = new Metal(new Vector3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vector3(4, 1, 0), 1.0, mat3));

        // AABB
        world.add(new HierarchyNode(world));

        return world;
    }

    public static double[] render(Camera cam, HittableList world) {
        double startTime = (double) System.currentTimeMillis();
        cam.render(world);
        double endTime = (double) System.currentTimeMillis();
        
        return new double[]{(endTime-startTime), Global.counter};
    }

    public static void runTestSuite(Camera cam, HittableList world) {
        int testNum = 20;
        double[][] results = new double[testNum][2];
        double avgTime = 0;
        double avgChecks = 0;

        for (int i = 0; i < testNum; i++) {
            results[i] = render(cam, world);
        }

        for (int i = 0; i < testNum; i++) {
            avgTime += results[i][0];
            avgChecks += results[i][1];
        }
        avgTime /= testNum;
        avgChecks /= testNum;

        System.out.println("average time "+avgTime+" seconds");
        System.out.println("average checks "+avgChecks+" collisions");
    }
}
