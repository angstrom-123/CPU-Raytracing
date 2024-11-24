package com.ang.Utils;

import com.ang.Materials.Material;

public class HitRecord {
    public Vector3 p;
    public Vector3 normal;
    public Material mat;
    public double u;
    public double v;
    public double t;
    public boolean frontFace;

    public void setFaceNormal(Ray r, Vector3 outwardNormal) {
        if (Vector3.dot(r.direction(), outwardNormal) > 0.0) {
            normal = outwardNormal.negative();
            this.frontFace = false;
        } else {
            normal = outwardNormal;
            this.frontFace = true;
        }
    }

    public void setUV(double[] uv) {
        this.u = uv[0];
        this.v = uv[1];
    }

    public void set(HitRecord rec) {
        p = rec.p;
        normal = rec.normal;
        mat = rec.mat;
        u = rec.u;
        v = rec.v;
        t = rec.t;
        frontFace = rec.frontFace;
    }
}
