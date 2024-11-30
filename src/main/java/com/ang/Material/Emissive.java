package com.ang.Material;

import com.ang.Texture.SolidColour;
import com.ang.Texture.Texture;
import com.ang.Util.Vec3;

/*
 * Material that emits light.
 */
public class Emissive extends Material{
    private Texture tex;

    /**
     * Constructs the material using a texture for the emission.
     * @param tex the texture to use for emission.
     */
    public Emissive(Texture tex) {
        this.tex = tex;
    }

    /**
     * Constructs the material using a solid colour.
     * @param emit the vector representing an rgb colour of the emission.
     */
    public Emissive(Vec3 emit) {
        this.tex = new SolidColour(emit);
    }

    /**
     * Determines the colour of emission at a given texture-space coordinate.
     * @param u first texture-space coordinate to be sampled.
     * @param v second texture-space coordinate to be sampled.
     * @param p position vector representing the point of intersection with the 
     *          hittable that has this material.
     * @return a vector representing rgb colour of the texture sampled at u v.
     */
    @Override
    public Vec3 emitted(double u, double v, Vec3 p) {
        return tex.value(u, v, p);
    }
}
