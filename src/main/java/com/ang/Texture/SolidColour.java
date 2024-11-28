package com.ang.Texture;

import com.ang.Util.Vec3;

public class SolidColour extends Texture {
    private Vec3 albedo;

    // define all components in vector
    public SolidColour(Vec3 albedo) {
        this.albedo = albedo;
    }

    // define all components individually
    public SolidColour(double r, double g, double b) {
        this(new Vec3(r, g, b));
    }

    // always returns same colour regardless of texture coords
    @Override
    public Vec3 value(double u, double v, Vec3 p) {
        return albedo;
    }
}
