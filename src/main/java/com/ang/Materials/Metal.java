package com.ang.Materials;

import com.ang.Utils.HitRecord;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Vector3;

public class Metal extends Material {
    private Vector3 albedo;
    private double fuzziness;

    public Metal(Vector3 albedo, double fuzziness) {
        this.albedo = albedo;
        this.fuzziness = fuzziness;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        Vector3 scatterDirection = Vector3.reflect(rIn.direction(), rec.normal);
        scatterDirection = scatterDirection.unitVector().add(Vector3.randomUnitVector().multiply(fuzziness));
        
        rt.set(albedo, new Ray(rec.p, scatterDirection));
        return true;
    }
}
