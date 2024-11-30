package com.ang.Texture;

import com.ang.Util.Vec3;

/**
 * Spatial checker texture. Colour of point is determined based on the world-
 * space coordinates of its position.
 */
public class CheckerTexture extends Texture{
    private Texture even;
    private Texture odd;
    private double  invScale;
    
    /**
     * Constructs the texture using solid colours.
     * @param scale size of the checkers.
     * @param col1 vector representing an rgb colour for the even squares.
     * @param col2 vector representing an rgb colour for the odd squares.
     */
    public CheckerTexture(double scale, Vec3 col1, Vec3 col2) {
        this(scale, new SolidColour(col1), new SolidColour(col2));
    }  

    /**
     * Constructs the texture using textures.
     * @param scale size of the checkers.
     * @param even the texture to use for even squares.
     * @param odd the texture to use for odd squares.
     */
    public CheckerTexture(double scale, Texture even, Texture odd) {
        this.invScale = 1.0 / scale;
        this.even = even;
        this.odd = odd;
    } 

    /**
     * Samples a point on one of the textures.
     * @param u first texture space coordinate of the point to sample.
     * @param v second texture space coordinate of the point to sample.
     * @param p position vector representing the point of intersection with the
     *          hittable that has this texture.
     * @return a vector representing rgb colour of the texture sampled at u, v.
     */
    @Override
    public Vec3 value(double u, double v, Vec3 p) {
        int x = (int)Math.floor(invScale * p.x());
        int y = (int)Math.floor(invScale * p.y());
        int z = (int)Math.floor(invScale * p.z());

        if ((x + y + z) % 2 == 0) {
            return even.value(u, v, p);
        }
        return odd.value(u, v, p);
    }
}
