package com.ang.Material;

import com.ang.Texture.SolidColour;
import com.ang.Texture.Texture;
import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vector3;

public class Lambertian extends Material{
    private Texture tex;

    public Lambertian(Vector3 albedo) {
        tex = new SolidColour(albedo);
    }

    public Lambertian(Texture tex) {
        this.tex = tex;
    }
    boolean saved = false;

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        Vector3 scatterDirection = rec.normal.add(Vector3.randomUnitVector());
        
        // if scatter direction is oposite to normal, it becomes almost 0,0,0
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal;
        }

        rt.set(tex.value(rec.u, rec.v, rec.p), new Ray(rec.p, scatterDirection));

        return true;
    }
}
