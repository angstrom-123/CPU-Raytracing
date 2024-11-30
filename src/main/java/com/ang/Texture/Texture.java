package com.ang.Texture;

import com.ang.Util.Vec3;

/**
 * Base class for all textures.
 */
public class Texture {
    public Texture texture;

    /**
     * Base value function to be overriden by textures.
     * @param u first texture space coordinate of the point to sample.
     * @param v second texture space coordinate of the point to sample.
     * @param p position vector representing the point of intersection with the
     *          hittable that has this texture.
     * @return vector representing the rgb values of a magenta debug colour.
     */
    public Vec3 value(double u, double v, Vec3 p) {
        return new Vec3(1.0, 0.0, 1.0);
    }
}
