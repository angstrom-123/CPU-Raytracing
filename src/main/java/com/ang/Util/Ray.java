package com.ang.Util;

public class Ray {
    private Vec3 origin;
    private Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vec3 origin() {
        return origin;
    }

    public Vec3 direction() {
        return direction;
    }

    // gets position of ray at a given length along it
    public Vec3 at(double t) {
        return origin.add(direction.multiply(t));
    }

    public void set(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
