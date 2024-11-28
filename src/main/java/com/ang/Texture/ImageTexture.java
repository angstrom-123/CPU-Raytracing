package com.ang.Texture;

import com.ang.Util.ImageHandler;
import com.ang.Util.Interval;
import com.ang.Util.Vec3;

public class ImageTexture extends Texture {
    private ImageHandler image = new ImageHandler();

    private boolean      noData;

    // attempts to load image texture from specified path
    public ImageTexture(String path) {
        boolean didLoad = image.loadImage(path);
        noData = !didLoad;
    }

    @Override
    public Vec3 value(double u, double v, Vec3 p) {
        // magenta debug colour
        if ((image.getHeight() <= 0) || (noData)) {
            return new Vec3(1.0, 0.0, 1.0);
        }

        Interval limit = new Interval(0.0, 1.0);
        
        // converting texture space coords to image space
        u = limit.clamp(u);
        v = 1 - limit.clamp(v);

        int i = (int)(u * image.getWidth());
        int j = (int)(v * image.getHeight());
        
        // samples image
        return image.pixelData(i, j);
    }
}
