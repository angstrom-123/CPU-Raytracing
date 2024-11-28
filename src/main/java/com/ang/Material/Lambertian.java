package com.ang.Material;

import com.ang.Texture.SolidColour;
import com.ang.Texture.Texture;
import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

public class Lambertian extends Material{
    private Texture tex;

    public Lambertian(Vec3 albedo) {
        tex = new SolidColour(albedo);
    }

    public Lambertian(Texture tex) {
        this.tex = tex;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        Vec3 direction = rec.normal.add(Vec3.randomUnitVector());
        
        // if scatter direction is opposite to normal, it becomes almost 0,0,0
        // this case is caught and replaced by normal
        if (direction.nearZero()) {
            direction = rec.normal;
        }

        // updates ray colour and scatter direction for next iteration
        rt.set(tex.value(rec.u, rec.v, rec.p), new Ray(rec.p, direction));
        return true;
    }
}
