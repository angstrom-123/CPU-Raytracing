package com.ang.Util;

import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// allows for loading and sampling of images
public class ImageHandler {
    private BufferedImage   image;
    private int             width;
    private int             height;

    // attempts to load an image
    public boolean loadImage(String path) {
        try {
            image = ImageIO.read(this.getClass().getResource(path));
        } catch (IOException e) {
            System.out.println("Could not load image from "+path);
            return false;
        }

        width = image.getWidth();
        height = image.getHeight();

        return true;
    }

    // samples pixel from image
    public Vec3 pixelData(int i, int j) {
        // out of bounds, magenta debug colour
        if (i >= width || j >= height) {
            return new Vec3(1.0, 0.0, 1.0);
        }

        int sample = image.getRGB(i, j);

        // isolates colour components from sample
        int r = (sample & 0x00ff0000) >> 16;
        int g = (sample & 0x0000ff00) >> 8;
        int b = (sample & 0x000000ff);

        // normalizes to 0-1 and converts to linear colour space
        // computation is done in linear space, images are loaded in gamma space
        double rLinear = Math.pow((double)r / 255.0, 2.0);
        double gLinear = Math.pow((double)g / 255.0, 2.0);
        double bLinear = Math.pow((double)b / 255.0, 2.0);

        return new Vec3(rLinear, gLinear, bLinear);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
