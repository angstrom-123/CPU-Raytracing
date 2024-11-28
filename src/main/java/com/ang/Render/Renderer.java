package com.ang.Render;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.*;

import com.ang.Global;
import com.ang.Util.Vec3;

public class Renderer extends JFrame{
    private JFrame        frame = new JFrame();

    private BufferedImage img ;
    private ImagePanel    imgPanel;
    private int           width;
    private int           height;
    private boolean       initDone;

    public Renderer(int w, int h) {
        width    = w;
        height   = h;
        img      = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        imgPanel = new ImagePanel(img);
        initDone = false;
    }

    public int getImageWidth() {
        return width;
    }

    public int getImageHeight() {
        return height;
    }

    private void initWindow() {
        // ensures correct dimensions
        imgPanel.setPreferredSize(new Dimension(width, height));
        frame.getContentPane().add(imgPanel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // shuts down threads when window is closed
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                Global.terminateThreads();
                frame.dispose();
            }
        });

        imgPanel.setFocusable(true);
        imgPanel.requestFocusInWindow();
    }

    public void clear() {
        frame.dispose();
    }

    // unitColour is a 0-1 normalized vector in linear colour space
    public void writePixel(Vec3 unitColour, int x, int y) {
        if (!initDone) {
            initWindow();
            initDone = true;
        }

        double r = unitColour.x();
        double g = unitColour.y();
        double b = unitColour.z();

        // gamma correction
        r = linear2gamma(r);
        g = linear2gamma(g);
        b = linear2gamma(b);

        // multiply to be in range 0-255 for output
        int rComponent = (int)Math.round(r * 255); 
        int gComponent = (int)Math.round(g * 255); 
        int bComponent = (int)Math.round(b * 255); 

        // clamping values to 0-255
        rComponent = rComponent > 255 ? rComponent = 255 : rComponent;
        gComponent = gComponent > 255 ? gComponent = 255 : gComponent;
        bComponent = bComponent > 255 ? bComponent = 255 : bComponent;

        // consolidating rgb values to single integer, 1 byte per component
        // first byte is alpha, not set as this defaults to 255 (full opacity)
        int col = (rComponent << 16) | (gComponent << 8) | bComponent;
        img.setRGB(x, y, col);

        // redrawing only for visualization purposes, likely hurts performance
        frame.repaint();
    }

    public void drawScreen() {
        frame.repaint();
    }

    // gamma correction, square rooting component
    private double linear2gamma(double linearComponent) {
        if (linearComponent > 0) {
            return Math.sqrt(linearComponent);
        }
        return 0;
    }

    public void saveFile(String path) {
        String randomName = String.valueOf(Math.random()).substring(2);
        File file = new File("renders/" + path + randomName + ".png");
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.out.println("exception in save file");
        }
    }
}