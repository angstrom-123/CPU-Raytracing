package com.ang.Texture;

import com.ang.Util.Vector3;

public class SpatialCheckerTexture extends Texture{
    private double invScale;
    private Texture even;
    private Texture odd;

    public SpatialCheckerTexture(double scale, Texture even, Texture odd) {
        this.invScale = 1 / scale;
        this.even = even;
        this.odd = odd;
    }

    public SpatialCheckerTexture(double scale, Vector3 col1, Vector3 col2) {
        this.invScale = 1 / scale;
        this.even = new SolidColour(col1);
        this.odd = new SolidColour(col2);
    }   

    @Override
    public Vector3 value(double u, double v, Vector3 p) {
        int x = (int)Math.floor(invScale * p.x());
        int y = (int)Math.floor(invScale * p.y());
        int z = (int)Math.floor(invScale * p.z());

        if ((x+y+z) % 2 == 0) {
            return even.value(u, v, p);
        }
        return odd.value(u, v, p);
    }
}
