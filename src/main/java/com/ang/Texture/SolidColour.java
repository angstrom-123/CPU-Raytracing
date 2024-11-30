package com.ang.Texture;

import com.ang.Util.Vec3;

/**
 * Texture that samples a solid colour.
 */
public class SolidColour extends Texture {
    private Vec3 albedo;

    /**
     * Constructs the texture using a base colour.
     * @param albedo vector representing the base colour of the texture.
     */
    public SolidColour(Vec3 albedo) {
        this.albedo = albedo;
    }

    /**
     * Constructs the texture using a base colour, separate components.
     * @param r the red component of the base colour.
     * @param g the green component of the base colour.
     * @param b the blue component of the base colour.
     */
    public SolidColour(double r, double g, double b) {
        this(new Vec3(r, g, b));
    }

    /**
     * Solid colour so always has the same value regardless of u v coorinates.
     * @param u first texture space coordinate of the point to sample.
     * @param v second texture space coordinate of the point to sample.
     * @param p position vector representing the point of intersection with the
     *          hittable that has this texture.
     * @return the base colour of the texture.
     */
    @Override
    public Vec3 value(double u, double v, Vec3 p) {
        return albedo;
    }
}
