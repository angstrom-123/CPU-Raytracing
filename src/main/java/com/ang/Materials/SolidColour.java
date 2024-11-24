package com.ang.Materials;

import com.ang.Utils.Vector3;

public class SolidColour extends Texture {
    private Vector3 albedo;

    public SolidColour(Vector3 albedo) {
        this.albedo = albedo;
    }

    public SolidColour(double r, double g, double b) {
        this(new Vector3(r, g, b));
    }

    @Override
    public Vector3 value(double u, double v, Vector3 p) {
        return albedo;
    }
}
