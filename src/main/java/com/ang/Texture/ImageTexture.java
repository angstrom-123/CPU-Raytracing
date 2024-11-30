package com.ang.Texture;

import com.ang.Util.ImageHandler;
import com.ang.Util.Interval;
import com.ang.Util.Vec3;

/**
 * A texture that samples an image.
 */
public class ImageTexture extends Texture {
    private ImageHandler    image = new ImageHandler();

    private boolean         noData;

    /**
     * Constructs the ImageTexture by attempting to load an image to the
     * ImageHandler.
     * @param path the path to the image.
     */
    public ImageTexture(String path) {
        boolean didLoad = image.loadImage(path);
        noData = !didLoad;
    }

    /**
     * Samples a point on the loaded image.
     * @param u first texture space coordinate of the point to sample.
     * @param v second texture space coordinate of the point to sample.
     * @param p position vector representing the point of intersection with the
     *          hittable that has this texture.
     * @return a vector representing rgb colour of the texture sampled at u, v.
     */
    @Override
    public Vec3 value(double u, double v, Vec3 p) {
        // magenta debug colour
        if ((image.getHeight() <= 0) || (noData)) {
            return new Vec3(1.0, 0.0, 1.0);
        }

        Interval limit = new Interval(0.0, 1.0);
        
        // converting texture-space coords to image-space
        u = limit.clamp(u);
        v = 1 - limit.clamp(v);

        int i = (int)(u * image.getWidth());
        int j = (int)(v * image.getHeight());
        
        return image.pixelData(i, j);
    }
}
