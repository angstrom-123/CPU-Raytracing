package com.ang.Utils;

import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageHandler {
    private BufferedImage image;

    private int width, height;

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

    public Vector3 pixelData(int u, int v) {
        if (u >= width || v >= height) {
            return new Vector3(1,0,1); // out of bounds
        }
        int sample = image.getRGB(u, v);

        int r = (sample & 0x00ff0000) >> 16;
        int g = (sample & 0x0000ff00) >> 8;
        int b = (sample & 0x000000ff);

        double rLinear = Math.pow((double)r / 255, 2);
        double gLinear = Math.pow((double)g / 255, 2);
        double bLinear = Math.pow((double)b / 255, 2);

        return new Vector3(rLinear, gLinear, bLinear);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }
}
