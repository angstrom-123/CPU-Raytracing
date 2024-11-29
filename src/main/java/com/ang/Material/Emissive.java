package com.ang.Material;

import com.ang.Texture.SolidColour;
import com.ang.Texture.Texture;
import com.ang.Util.Vec3;

/*
 * Physical lights in the scene
 */
public class Emissive extends Material{
    private Texture tex;

    public Emissive(Texture tex) {
        this.tex = tex;
    }

    public Emissive(Vec3 emit) {
        this.tex = new SolidColour(emit);
    }

    // emissive overrides emissive property in material, no albedo
    @Override
    public Vec3 emitted(double u, double v, Vec3 p) {
        return tex.value(u, v, p);
    }
}
