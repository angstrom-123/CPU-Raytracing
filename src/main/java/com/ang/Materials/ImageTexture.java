package com.ang.Materials;

import com.ang.Utils.ImageHandler;
import com.ang.Utils.Interval;
import com.ang.Utils.Vector3;

public class ImageTexture extends Texture {
    private ImageHandler image = new ImageHandler();
    private boolean noData = false;

    public ImageTexture(String path) {
        boolean didLoad = image.loadImage(path);
        if (!didLoad) {
            noData = true;
        }
    }

    @Override
    public Vector3 value(double u, double v, Vector3 p) {
        if (image.height() <= 0 || noData) {
            return new Vector3(1,0,1); // magenta debug colour
        }

        Interval limit = new Interval(0,1);
        u = limit.clamp(u);
        v = 1 - limit.clamp(v); // flip v to image coords

        int i = (int)(u * image.width());
        int j = (int)(v * image.height());
        
        return image.pixelData(i, j);
    }
}
