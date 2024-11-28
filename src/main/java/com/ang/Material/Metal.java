package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

public class Metal extends Material {
    private Vec3   albedo;
    private double fuzziness;

    public Metal(Vec3 albedo, double fuzziness) {
        this.albedo    = albedo;
        this.fuzziness = fuzziness;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        Vec3 direction = Vec3.reflect(rIn.direction(), rec.normal).unitVector();
        direction = direction.add(Vec3.randomUnitVector().multiply(fuzziness));
        
        // updates ray colour and scatter direction for next iteration
        rt.set(albedo, new Ray(rec.p, direction));
        return true;
    }
}
