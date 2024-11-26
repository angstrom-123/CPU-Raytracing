package com.ang;

import com.ang.AABB.BVHNode;
import com.ang.Camera.Camera;
import com.ang.Hittables.HittableList;
import com.ang.Hittables.Compound.Mesh;
import com.ang.Hittables.Compound.Quad;
import com.ang.Hittables.Primitive.Sphere;
import com.ang.Hittables.Primitive.Tri;
import com.ang.Material.*;
import com.ang.Texture.ImageTexture;
import com.ang.Texture.SpatialCheckerTexture;
import com.ang.Texture.Texture;
import com.ang.Util.Interval;
import com.ang.Util.OBJImporter;
import com.ang.Util.Vector3;

public class Main 
{
    public static void main( String[] args )
    {
        Camera cam = new Camera();

        cam.imageWidth = 400;
        cam.samplesPerPixel = 50;
        cam.maxBounces = 20;

        switch (3) {
            case 1:
                spheresScene(cam);
                break;
            case 2:
                knightScene(cam);
                break;
            case 3:
                globeScene(cam);
                break;
            case 4:
                emissionScene(cam);
                break;
            case 5:
                cornellBoxScene(cam);
                break;
        }
    }

    public static void spheresScene(Camera cam) {
        HittableList world = new HittableList(2000);

        cam.aspectRatio = 16.0 / 9.0;

        cam.fov = 20;
        cam.lookFrom = new Vector3(13, 2, 3);
        cam.lookAt = new Vector3(0, 0, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.background = new Vector3(0.7, 0.8, 1);

        cam.defocusAngle = 0.6;
        cam.focusDistance = 10;

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

        Material ground = new Lambertian(new Vector3(0.5, 0.5, 0.5));
        Material ball1 = new Dielectric(1.5);
        Material ball2 = new Lambertian(new Vector3(0.4, 0.2, 0.1));
        Material ball3 = new Metal(new Vector3(0.7, 0.6, 0.5), 0);

        world.add(new Sphere(new Vector3(0,-1000,0), 1000, ground));
        world.add(new Sphere(new Vector3(0,1,0), 1, ball1));
        world.add(new Sphere(new Vector3(-4,1,0), 1, ball2));
        world.add(new Sphere(new Vector3(4,1,0), 1, ball3));

        world.add(new BVHNode(world));

        Global.world = world;

        render(cam, world);
    }

    public static void knightScene(Camera cam) {
        HittableList world = new HittableList(2000);

        cam.aspectRatio = 16.0 / 9.0;

        cam.fov = 50;
        cam.lookFrom = new Vector3(1, 1, 3);
        cam.lookAt = new Vector3(0, 1, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.background = new Vector3(0.7, 0.8, 1);

        cam.defocusAngle = 1;
        cam.focusDistance = 3;

        Interval inter = new Interval(-1, 1);
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = Math.random();
                Vector3 centre = new Vector3(a + 0.9 * Math.random(), 0.2, b + 0.9 * Math.random());

                if (inter.surrounds(centre.x()) && inter.surrounds(centre.z())) {
                    continue;
                }
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

        Texture checker = new SpatialCheckerTexture(0.32, new Vector3(0.2, 0.3, 0.1), new Vector3(0.9, 0.9, 0.9));

        Material glass = new Dielectric(1.5);
        Material mirror = new Metal(new Vector3(0.7, 0.6, 0.5), 0.0);

        OBJImporter importer = new OBJImporter();
        Mesh knight = importer.loadOBJ(new Vector3(0,0,0), "/models/chess_knight.obj", glass);
        
        world.add(knight);
        world.add(new Sphere(new Vector3(3, 1, -3), 3.0, mirror));
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(checker)));
        
        world.add(new BVHNode(world));

        Global.world = world;

        render(cam, world);
    }

    public static void globeScene(Camera cam) {
        HittableList world = new HittableList(100);

        cam.aspectRatio = 16.0 / 9.0;

        cam.fov = 50;
        cam.lookFrom = new Vector3(0, 0, 10);
        cam.lookAt = new Vector3(0, 0, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.background = new Vector3(0.7, 0.8, 1);

        cam.defocusAngle = 0;

        Texture earthTexture = new ImageTexture("/textures/earth_map.jpg");
        Material earthSurface = new Lambertian(earthTexture);

        world.add(new Sphere(new Vector3(0,0,0), 3, earthSurface));

        world.add(new BVHNode(world));

        Global.world = world;

        render(cam, world);
    }

    public static void emissionScene(Camera cam) {
        HittableList world = new HittableList(2000);
        
        cam.aspectRatio = 16.0 / 9.0;

        cam.fov = 50;
        cam.lookFrom = new Vector3(1, 1, 3);
        cam.lookAt = new Vector3(0, 1, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.background = new Vector3(0,0,0);

        cam.defocusAngle = 0;

        Texture checker = new SpatialCheckerTexture(0.32, new Vector3(0.2, 0.3, 0.1), new Vector3(0.9, 0.9, 0.9));

        Material glass = new Dielectric(1.5);
        Material light = new Emissive(new Vector3(5,5,5));

        OBJImporter importer = new OBJImporter();
        Mesh knight = importer.loadOBJ(new Vector3(0,0,0), "/models/chess_knight.obj", glass);
        
        world.add(knight);
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, new Lambertian(checker)));
        world.add(new Sphere(new Vector3(-3,2,-3), 1, light));

        world.add(new BVHNode(world));

        Global.world = world;

        render(cam, world);
    }

    public static void cornellBoxScene(Camera cam) {
        HittableList world = new HittableList(2000);
        
        cam.aspectRatio = 1.0 / 1.0;

        cam.fov = 50;
        cam.lookFrom = new Vector3(0, 3, 7);
        cam.lookAt = new Vector3(0, 3, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.background = new Vector3(0,0,0);

        cam.defocusAngle = 0;

        Material white = new Lambertian(new Vector3(0.73, 0.73, 0.73));
        Material red = new Lambertian(new Vector3(0.65,0.05,0.05));
        Material green = new Lambertian(new Vector3(0.12, 0.45, 0.15));
        Material light = new Emissive(new Vector3( 15, 15, 15));
        
        world.add(new Quad(new Vector3(-3,0,0), new Vector3(-3,0,-6), new Vector3(-3,6,-6), new Vector3(-3,6,0), green));
        world.add(new Quad(new Vector3(3,6,-6), new Vector3(3,0,-6), new Vector3(3,0,0), new Vector3(3,6,0), red));
        world.add(new Quad(new Vector3(-3,6,0), new Vector3(-3,6,-6), new Vector3(3,6,-6), new Vector3(3,6,0), white));
        world.add(new Quad(new Vector3(-3,0,0), new Vector3(3,0,0), new Vector3(3,0,-6), new Vector3(-3,0,-6), white));
        world.add(new Quad(new Vector3(-3,0,-6), new Vector3(3,0,-6), new Vector3(3,6,-6), new Vector3(-3,6,-6), white));
        world.add(new Quad(new Vector3(-1,6,-2), new Vector3(1,6,-2), new Vector3(1,6,-4), new Vector3(-1,6,-4), light));

        Global.world = world;
        
        render(cam, world);
    }

    public static void render(Camera cam, HittableList world) {
        double startTime = (double) System.currentTimeMillis();
        cam.render(world);
        double endTime = (double) System.currentTimeMillis();
        
        System.out.println(((endTime - startTime) / 1000)+"s to render");
    }
}
