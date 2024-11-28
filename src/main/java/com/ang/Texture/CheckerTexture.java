package com.ang.Texture;

import com.ang.Util.Vec3;

// checker texture based on world space coords of each point
public class CheckerTexture extends Texture{
    private Texture even;
    private Texture odd;
    private double  invScale;
    
    // define in terms of colour vectors
    public CheckerTexture(double scale, Vec3 col1, Vec3 col2) {
        this(scale, new SolidColour(col1), new SolidColour(col2));
    }  

    // define in terms of textures
    public CheckerTexture(double scale, Texture even, Texture odd) {
        this.invScale = 1.0 / scale;
        this.even = even;
        this.odd = odd;
    } 

    // outputs colour depending on world space coords of intersection point p
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
