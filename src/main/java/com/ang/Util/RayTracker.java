package com.ang.Util;

public class RayTracker {
    public Vector3 attenuation;
    public Ray scattered;

    public RayTracker(Vector3 attenuation, Ray scattered) {
        this.attenuation = attenuation;
        this.scattered = scattered;
    }

    public void set(Vector3 attenuation, Ray scattered) {
        this.attenuation = attenuation;
        this.scattered = scattered;
    }
}
