package com.ang.Material;

import com.ang.Texture.SolidColour;
import com.ang.Texture.Texture;
import com.ang.Util.Vector3;

public class Emissive extends Material{
    private Texture tex;

    public Emissive(Texture tex) {
        this.tex = tex;
    }

    public Emissive(Vector3 emit) {
        this.tex = new SolidColour(emit);
    }

    @Override
    public Vector3 emitted(double u, double v, Vector3 p) {
        return tex.value(u, v, p);
    }
}
