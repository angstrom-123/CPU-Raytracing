package com.ang.Util;

// holds information about ray colour and next bounce direction
public class RayTracker {
    public Vec3 attenuation;
    public Ray  scattered;

    public RayTracker() {
        this.attenuation = new Vec3(0.0, 0.0, 0.0);
        this.scattered = new Ray(
            new Vec3(0.0, 0.0, 0.0), 
            new Vec3(0.0, 0.0, 0.0));
    }

    public RayTracker(Vec3 attenuation, Ray scattered) {
        this.attenuation = attenuation;
        this.scattered = scattered;
    }

    public void set(Vec3 attenuation, Ray scattered) {
        this.attenuation = attenuation;
        this.scattered = scattered;
    }
}
