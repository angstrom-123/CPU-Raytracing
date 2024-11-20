package com.ang;

import com.ang.Materials.*;
import com.ang.Utils.BVHNode;
import com.ang.Utils.Importer;
import com.ang.Utils.Vector3;
import com.ang.World.HittableList;
import com.ang.World.Sphere;
import com.ang.World.Tri;
import com.ang.World.Mesh;

public class Main 
{
    public static void main( String[] args )
    {
        // Camera
        Camera cam = new Camera();

        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.samplesPerPixel = 1;
        cam.maxBounces = 1;

        cam.fov = 20;
        cam.lookFrom = new Vector3(5, 2, 5);
        // cam.lookFrom = new Vector3(13, 2, 3);
        cam.lookAt = new Vector3(0, 1, 0);
        cam.vUp = new Vector3(0, 1, 0);

        // cam.defocusAngle = 0.6;
        cam.defocusAngle = 0;
        cam.focusDistance = 10.0;

        // world
        HittableList world = createWorld();

        double[] info = render(cam, world);

        System.out.println((info[0]/1000)+"s");
        System.out.println(info[1]+" bbox "+info[2]+" sphere");
        System.out.println("bbox count "+Global.count);
    }

    public static HittableList createWorld() {
        HittableList world = new HittableList(2000);

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

                    // world.add(new Sphere(centre, 0.2, sphereMaterial));
                }
            }
        }

        Material groundMaterial = new Lambertian(new Vector3(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, groundMaterial));
        
        // Material mat1 = new Dielectric(1.5);
        // world.add(new Sphere(new Vector3(0, 1, 0), 1.0, mat1));
        
        Material mat2 = new Lambertian(new Vector3(0.4, 0.2, 0.1));
        // world.add(new Sphere(new Vector3(-4, 1, 0), 1.0, mat2));

        Material mat3 = new Metal(new Vector3(0.7, 0.6, 0.5), 0.0);
        // world.add(new Sphere(new Vector3(7, 1, 0), 1.0, mat3));

        // world.add(new Sphere(new Vector3(10,0,-20), 10, mat2));
        // world.add(new Sphere(new Vector3(10,0,-15), 9, mat3));
        // world.add(new Sphere(new Vector3(10,0,-8), 8, mat1));

        Global.world = world;
        Importer importer = new Importer();

        // Mesh knight = importer.importOBJ("/chess_knight.obj", new Lambertian(new Vector3(1,0.1,0.1)));
        // world.add(knight);

        Mesh box = importer.importOBJ("/cube.obj", new Lambertian(new Vector3(1,0.1,0.1)));
        world.add(box);
        
        world.add(new BVHNode(world));

        return world;
    }

    public static double[] render(Camera cam, HittableList world) {
        double startTime = (double) System.currentTimeMillis();
        cam.render(world);
        double endTime = (double) System.currentTimeMillis();
        
        return new double[]{(endTime-startTime), Global.bBoxHits, Global.hits};
    }
}
