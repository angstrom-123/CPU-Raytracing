package com.ang.Utils;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 origin() {
        return origin;
    }

    public Vector3 direction() {
        return direction;
    }

    public Vector3 at(double t) {
        return origin.add(direction.multiply(t));
    }

    public void set(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
