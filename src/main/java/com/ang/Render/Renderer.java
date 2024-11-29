package com.ang.Render;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;
import javax.imageio.*;

import com.ang.Global;
import com.ang.Util.Vec3;

/*
 * Renderer handles displaying and saving the raytracing result. Makes use of 
 * java swing for displaying render in window. File format for saved images is
 * png by default.
 */
public class Renderer extends JFrame{
    private JFrame          frame = new JFrame();

    private BufferedImage   img ;
    private ImagePanel      imgPanel;
    private int             width;
    private int             height;
    private boolean         initDone;

    public Renderer(int w, int h) {
        width       = w;
        height      = h;
        img         = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        imgPanel    = new ImagePanel(img);
        initDone    = false;
    }

    public int getImageWidth() {
        return width;
    }

    public int getImageHeight() {
        return height;
    }

    private void initWindow() {
        imgPanel.setPreferredSize(new Dimension(width, height));
        frame.getContentPane().add(imgPanel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        /* 
         * if the window is closed, the thread master is instructed to terminate
         * all current threads. The frame is then disposed of.
         */
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                Global.terminateThreads();
                frame.dispose();
            }
        });

        imgPanel.setFocusable(true);
        imgPanel.requestFocusInWindow();
    }

    /*
     * Writes an rgb colour value to the screen space coordinates (x, y). The 
     * unitColour passed in should be a colour vector (containing rgb for xyz).
     * unitColour should be normalized to the range (0,1) and be in linear 
     * colour space.
     */
    public void writePixel(Vec3 unitColour, int x, int y) {
        if (!initDone) {
            initWindow();
            initDone = true;
        }

        double r = unitColour.x();
        double g = unitColour.y();
        double b = unitColour.z();

        r = linearToGamma(r);
        g = linearToGamma(g);
        b = linearToGamma(b);

        int rComponent = (int)Math.round(r * 255); 
        int gComponent = (int)Math.round(g * 255); 
        int bComponent = (int)Math.round(b * 255); 

        rComponent = rComponent > 255 ? rComponent = 255 : rComponent;
        gComponent = gComponent > 255 ? gComponent = 255 : gComponent;
        bComponent = bComponent > 255 ? bComponent = 255 : bComponent;

        /*
         * BufferedImage accepts pixel colour as a single integer storing all
         * component colours. Each component is 2 bytes in the order alpha, red,
         * green, blue. col is the rgb value in this representation.
         */
        int col = (rComponent << 16) | (gComponent << 8) | bComponent;
        img.setRGB(x, y, col);

        /*
         * Redrawing the frame every time a pixel is drawn hurts the performance
         * of the program. I opt to do this anyway for aesthetic purposes.
         */
        frame.repaint();
    }

    public void drawScreen() {
        frame.repaint();
    }

    private double linearToGamma(double linearComponent) {
        if (linearComponent > 0) {
            return Math.sqrt(linearComponent);
        }
        return 0;
    }

    public void saveFile(String path, String name) {
        /*
         * If the path is undefined then the renders directory is searched for
         * in common locaitons. If it is not found, the file will be saved in 
         * the cwd.
         */
        if ((path == null) || (path.length() < 1)) {
            String[] prefixes = new String[]{
                "/",
                "./",
                "../",
                "../../",
                "/src/"
            };

            int i = 0;
            while (true) {
                if (i >= prefixes.length) {
                    path = "."; // cwd
                    break;
                }
                String prefix = prefixes[i];
                if (Files.isDirectory(Paths.get(prefix + "renders"))) {
                    path = prefix + "renders/";
                    break;
                }

                i++;
            }
        } else if (path.substring(path.length() - 1) != "/") {
            path = path + "/"; // last character of path must be a /
        }

        if ((name == null) || (name.length() < 1)) {
            name = String.valueOf(Math.random()).substring(2); // random name
        }

        try {
            ImageIO.write(img, "png", new File(path + name + ".png"));
        } catch (IOException e) {
            System.out.println("Exception in save file");
            e.printStackTrace();
        }  
    }
}