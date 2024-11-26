package com.ang.Render;

// import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.*;

import com.ang.Util.InputHandler;
import com.ang.Util.Vector3;

public class Renderer extends JFrame{
    int width;
    int height;

    private int[] pixels;
    private int index = 0;

    private BufferedImage img ;
    private ImagePanel imgPanel;
    private JFrame frame = new JFrame();

    private InputHandler handler;

    public Renderer(int w, int h) {
        width = w;
        height = h;
        pixels = new int[w * h];
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        imgPanel = new ImagePanel(img);

        initWindow();
    }

    public Renderer(int w, int h, InputHandler handler) {
        width = w;
        height = h;
        pixels = new int[w * h];
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        imgPanel = new ImagePanel(img);

        this.handler = handler;

        initWindow();
    }

    private void initWindow() {
        frame.add(imgPanel);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                frame.dispose();
            }
        });
        imgPanel.addKeyListener(handler);
        imgPanel.setFocusable(true);
        imgPanel.requestFocusInWindow();
    }

    public void clear() {
        frame.dispose();
    }

    public void writePixel(Vector3 unitColour) {
        double r = unitColour.x();
        double g = unitColour.y();
        double b = unitColour.z();

        // gamma correction
        r = linear2gamma(r);
        g = linear2gamma(g);
        b = linear2gamma(b);

        // 0-1 to 0-255
        int rComponent = (int)Math.round(r * 255); 
        int gComponent = (int)Math.round(g * 255); 
        int bCompoment = (int)Math.round(b * 255); 

        if (rComponent > 255) {
            rComponent = 255;
        }
        if (gComponent > 255) {
            gComponent = 255;
        }
        if (bCompoment > 255) {
            bCompoment = 255;
        }

        int col = (rComponent << 16) | (gComponent << 8) | bCompoment;
        pixels[index] = col;
        img.setRGB(index % width, (int)Math.floor(index / width) , col);
        index++;

        frame.repaint();
    }

    public void drawScreen() {
        frame.repaint();
        index = 0;
    }

    private double linear2gamma(double linearComponent) {
        if (linearComponent > 0) {
            return Math.sqrt(linearComponent);
        }
        return 0;
    }

    public void saveFile(String path) {
        File file = new File("renders/"+path+String.valueOf(Math.random()).substring(2)+".png");
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.out.println("exception in save file");
        }
        // frame.dispose();
    }
}