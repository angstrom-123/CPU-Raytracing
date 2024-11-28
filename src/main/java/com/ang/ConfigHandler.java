package com.ang;

import com.ang.AABB.BVHNode;
import com.ang.Hittable.HittableList;
import com.ang.Hittable.Compound.Cuboid;
import com.ang.Hittable.Compound.Mesh;
import com.ang.Hittable.Compound.Quad;
import com.ang.Hittable.Primitive.Sphere;
import com.ang.Material.Dielectric;
import com.ang.Material.Emissive;
import com.ang.Material.Lambertian;
import com.ang.Material.Material;
import com.ang.Material.Metal;
import com.ang.Texture.CheckerTexture;
import com.ang.Texture.ImageTexture;
import com.ang.Texture.Texture;
import com.ang.Thread.Master;
import com.ang.Util.Interval;
import com.ang.Util.OBJImporter;
import com.ang.Util.Vec3;

public class ConfigHandler {
    private Camera cam;
    private HittableList world;

    public ConfigHandler() {
        cam = new Camera();
        world = new HittableList(2000);
        Global.master = new Master(cam, world);
    }

    public void callRender() {
        if (Global.master.renderDone) {
            cam.init();
            Global.master.set(this.cam, this.world);
            Global.master.render();
        }
    }

    public void configThreads(int threadNum, int tileX, int tileY) {
        if (Global.master.renderDone) {
            Global.master.setThreadCount(threadNum);
            Global.master.setTileSize(tileX, tileY);
        }
    }

    public void configCam(int imageWidth, int samplesPerPixel, int maxBounces) {
        if (Global.master.renderDone) {
            cam.imageWidth      = imageWidth;
            cam.samplesPerPixel = samplesPerPixel;
            cam.maxBounces      = maxBounces;
        }
    }

    public void setScene(int sceneNum) {
        if (!Global.master.renderDone) {
            return;
        }

        switch (sceneNum) {
            case 1: // spheres
                scene1();
                break;
            case 2: // knight
                scene2();
                break;
            case 3: // globe
                scene3();
                break;
            case 4: // emission
                scene4();
                break;
            case 5: // cornell
                scene5();
                break;
            default:
                break;
        }
    }

    private void scene1() {
        cam.aspectRatio = 16.0 / 9.0;

        cam.fov      = 20.0;
        cam.lookFrom = new Vec3(13.0, 2.0, 3.0);
        cam.lookAt   = new Vec3( 0.0, 0.0, 0.0);
        cam.vUp      = new Vec3( 0.0, 1.0, 0.0);

        cam.background = new Vec3(0.7, 0.8, 1.0);

        cam.defocusAngle  =  0.6;
        cam.focusDistance = 10.0;

        // random spheres
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = Math.random();
                Vec3 centre = new Vec3(
                    a + 0.9 * Math.random(), 
                    0.2,
                    b + 0.9 * Math.random());

                if (centre.subtract(new Vec3(4.0, 0.2, 0.0)).length() > 0.9) {
                    Material sphereMaterial;

                    if (chooseMat < 0.8) {
                        Vec3 albedo = Vec3.random().multiply(Vec3.random());
                        sphereMaterial = new Lambertian(albedo);
                    } else if (chooseMat < 0.95) {
                        Vec3 albedo = Vec3.random(0.5, 1.0);
                        double fuzziness = Global.randomInRange(0.0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzziness);
                    } else {
                        sphereMaterial = new Dielectric(1.5);
                    }

                    world.add(new Sphere(centre, 0.2, sphereMaterial));   
                }
            }
        }

        Material ground = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        Material ball1  = new Dielectric(1.5);
        Material ball2  = new Lambertian(new Vec3(0.4, 0.2, 0.1));
        Material ball3  = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);

        world.add(new Sphere(new Vec3( 0.0, -1000.0, 0.0), 1000.0, ground));
        world.add(new Sphere(new Vec3( 0.0,     1.0, 0.0),    1.0, ball1));
        world.add(new Sphere(new Vec3(-4.0,     1.0, 0.0),    1.0, ball2));
        world.add(new Sphere(new Vec3( 4.0,     1.0, 0.0),    1.0, ball3));

        world.add(new BVHNode(world));
    }

    private void scene2() {
        world = new HittableList(2000);

        cam.aspectRatio = 16.0 / 9.0;

        cam.fov      = 50.0;
        cam.lookFrom = new Vec3(1.0, 1.0, 3.0);
        cam.lookAt   = new Vec3(0.0, 1.0, 0.0);
        cam.vUp      = new Vec3(0.0, 1.0, 0.0);

        cam.background = new Vec3(0.7, 0.8, 1.0);

        cam.defocusAngle  = 1.0;
        cam.focusDistance = 3.0;

        // random spheres excluding centre
        Interval inter = new Interval(-1.0, 1.0);
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = Math.random();
                Vec3 centre = new Vec3(
                    a + 0.9 * Math.random(),
                    0.2, 
                    b + 0.9 * Math.random());

                if ((inter.surrounds(centre.x())) 
                && (inter.surrounds(centre.z()))) {
                    continue;
                }
                if (centre.subtract(new Vec3(4.0, 0.2, 0.0)).length() > 0.9) {
                    Material sphereMaterial;

                    if (chooseMat < 0.8) {
                        Vec3 albedo = Vec3.random().multiply(Vec3.random());
                        sphereMaterial = new Lambertian(albedo);
                    } else if (chooseMat < 0.95) {
                        Vec3 albedo = Vec3.random(0.5, 1.0);
                        double fuzziness = Global.randomInRange(0.0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzziness);
                    } else {
                        sphereMaterial = new Dielectric(1.5);
                    }
                    world.add(new Sphere(centre, 0.2, sphereMaterial));   
                }
            }
        }

        Vec3 col1 = new Vec3(0.2, 0.3, 0.1);
        Vec3 col2 = new Vec3(0.9, 0.9, 0.9);
        Texture groundTex = new CheckerTexture(0.32, col1, col2);
        Material groundMat = new Lambertian(groundTex);

        Material glass = new Dielectric(1.5);
        Material mirror = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);

        OBJImporter importer = new OBJImporter();
        Mesh knight = importer.loadOBJ("/models/chess_knight.obj", glass);
        
        world.add(knight);
        world.add(new Sphere(new Vec3(3.0,     1.0, -3.0),    3.0, mirror));
        world.add(new Sphere(new Vec3(0.0, -1000.0,  0.0), 1000.0, groundMat));
        
        world.add(new BVHNode(world));
    }

    private void scene3() {
        world = new HittableList(100);

        cam.aspectRatio = 16.0 / 9.0;

        cam.fov      = 50.0;
        cam.lookFrom = new Vec3(0.0, 0.0, 10.0);
        cam.lookAt   = new Vec3(0.0, 0.0,  0.0);
        cam.vUp      = new Vec3(0.0, 1.0,  0.0);

        cam.background = new Vec3(0.7, 0.8, 1);

        cam.defocusAngle = 0.0;

        Texture earthTexture = new ImageTexture("/textures/earth_map.jpg");
        Material earthSurface = new Lambertian(earthTexture);

        world.add(new Sphere(new Vec3(0.0, 0.0, 0.0), 3.0, earthSurface));

        world.add(new BVHNode(world));
    }

    private void scene4() {
        world = new HittableList(2000);
        
        cam.aspectRatio = 16.0 / 9.0;

        cam.fov      = 50.0;
        cam.lookFrom = new Vec3(1.0, 1.0, 3.0);
        cam.lookAt   = new Vec3(0.0, 1.0, 0.0);
        cam.vUp      = new Vec3(0.0, 1.0, 0.0);

        cam.defocusAngle = 0.0;

        Vec3 col1 = new Vec3(0.2, 0.3, 0.1);
        Vec3 col2 = new Vec3(0.9, 0.9, 0.9);
        Texture groundTex = new CheckerTexture(0.32, col1, col2);
        Material groundMat = new Lambertian(groundTex);

        Material glass = new Dielectric(1.5);
        Material light = new Emissive(new Vec3(5.0, 5.0, 5.0));

        OBJImporter importer = new OBJImporter();
        Mesh knight = importer.loadOBJ("/models/chess_knight.obj", glass);
        
        world.add(knight);

        world.add(new Sphere(new Vec3( 0.0, -1000.0,  0.0), 1000.0, groundMat));
        world.add(new Sphere(new Vec3(-3.0,     2.0, -3.0),    1.0, light));
        world.add(new BVHNode(world));
    }

    private void scene5() {
        world = new HittableList(2000);
        
        cam.aspectRatio = 1.0 / 1.0;

        cam.fov      = 25.0;
        cam.lookFrom = new Vec3(0.0, 3.0, 14.0);
        cam.lookAt   = new Vec3(0.0, 3.0,  0.0);
        cam.vUp      = new Vec3(0.0, 1.0,  0.0);

        cam.defocusAngle = 0.0;

        Material white = new Lambertian(new Vec3(0.73, 0.73, 0.73));
        Material red   = new Lambertian(new Vec3(0.65, 0.05, 0.05));
        Material green = new Lambertian(new Vec3(0.12, 0.45, 0.15));

        Material light = new Emissive(new Vec3(15.0, 13.0, 12.0));
        
        // walls
        world.add(new Quad(
            new Vec3(-3.0, 0.0,  0.0), 
            new Vec3(-3.0, 0.0, -6.0), 
            new Vec3(-3.0, 6.0, -6.0), 
            new Vec3(-3.0, 6.0,  0.0), 
            red));
        world.add(new Quad(
            new Vec3(3.0, 6.0, -6.0), 
            new Vec3(3.0, 0.0, -6.0), 
            new Vec3(3.0, 0.0,  0.0), 
            new Vec3(3.0, 6.0,  0.0), 
            green));
        world.add(new Quad(
            new Vec3(-3.0, 6.0,  0.0), 
            new Vec3(-3.0, 6.0, -6.0), 
            new Vec3( 3.0, 6.0, -6.0), 
            new Vec3( 3.0, 6.0,  0.0), 
            white));
        world.add(new Quad(
            new Vec3(-3.0, 0.0,  0.0), 
            new Vec3( 3.0, 0.0,  0.0), 
            new Vec3( 3.0, 0.0, -6.0), 
            new Vec3(-3.0, 0.0, -6.0), 
            white));
        world.add(new Quad(
            new Vec3(-3.0, 0.0, -6.0), 
            new Vec3( 3.0, 0.0, -6.0), 
            new Vec3( 3.0, 6.0, -6.0), 
            new Vec3(-3.0, 6.0, -6.0), 
            white));
        world.add(new Quad(
            new Vec3(-3.0, 0.0, 0.0), 
            new Vec3(-3.0, 6.0, 0.0), 
            new Vec3( 3.0, 6.0, 0.0), 
            new Vec3( 3.0, 0.0, 0.0), 
            white));      

        // boxes
        world.add(new Cuboid(
            new Vec3(-1.7, 0.0, -3.5), 
            new Vec3( 2.0, 0.0, -0.5), 
            new Vec3( 0.0, 4.0,  0.0), 
            new Vec3(-0.5, 0.0, -2.0), 
            white));
        world.add(new Cuboid(
            new Vec3(0.0, 0.0, -1.5), 
            new Vec3(2.0, 0.0,  0.5), 
            new Vec3(0.0, 2.0,  0.0), 
            new Vec3(0.5, 0.0, -2.0), 
            white));

        // light
        world.add(new Quad(
            new Vec3(-1.0, 6.0, -2.0), 
            new Vec3( 1.0, 6.0, -2.0), 
            new Vec3( 1.0, 6.0, -4.0), 
            new Vec3(-1.0, 6.0, -4.0), 
            light));
    }
}