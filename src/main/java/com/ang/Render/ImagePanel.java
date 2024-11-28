package com.ang.Render;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ImagePanel extends JPanel 
{
    private BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
    }

    // overwriting paintComponent to write individual pixels
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}