package com.ang.Render;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * Container for BufferedImage that is used to display the render on the screen.
 */
public class ImagePanel extends JPanel 
{
    private BufferedImage image;

    /**
     * Constructs the panel with a BufferedImage.
     * @param image the BufferedImage to be used for rendering to.
     */
    public ImagePanel(BufferedImage image) {
        this.image = image;
    }

    /**
     * Allows for individual pixels to be written to the BufferedImage.
     * This method is never directly called in the code.
     * @param g built in Graphics parameter from superclass.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}