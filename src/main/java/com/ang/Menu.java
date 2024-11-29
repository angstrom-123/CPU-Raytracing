package com.ang;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

// thread count
// horizontal resolution
// scene selector
// bounce limit
// samples per pixel
// tile x, tile y
public class Menu extends JFrame {
    private Font font = new Font("dialog", Font.PLAIN, 11);

    private ConfigHandler   c;
    private JFrame          frame;
    private JButton         renderButton;
    private JLabel          l1, l2, l3, l4, l5, l6, l7;
    private JLabel          sceneLabel;
    private JSpinner        s1, s2, s3, s4, s5, s6, s7;
    
    private JLabel[]        labels;
    private JSpinner[]      spinners;

    public Menu(ConfigHandler c) {
        this.c = c;
    }

    private void initElements() {
        // scene selector
        sceneLabel = new JLabel("Random balls"); 
        l1 = new JLabel("Demo scene");  
        s1 = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        s1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                switch ((Integer)s1.getValue()) {
                    case 1:
                        sceneLabel.setText("Random balls");
                        break;
                    case 2:
                        sceneLabel.setText("Glass knight");
                        break;
                    case 3:
                        sceneLabel.setText("Globe");
                        break;
                    case 4:
                        sceneLabel.setText("Emission");
                        break;
                    case 5:
                        sceneLabel.setText("Cornell box");
                        break;
                    default:
                        break;
                }
            }
        });

        // samples per pixel
        l2 = new JLabel("Samples per pixel");
        s2 = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));

        // bounce limit
        l3 = new JLabel("Max ray bounces");
        s3 = new JSpinner(new SpinnerNumberModel(20, 1, 500, 5));

        // image width
        l4 = new JLabel("Horizontal resolution");
        s4 = new JSpinner(new SpinnerNumberModel(426, 1, 3840, 100));

        // thread count
        l5 = new JLabel("Number of threads");
        s5 = new JSpinner(new SpinnerNumberModel(3, 1, 50, 1));

        // tile width
        l6 = new JLabel("Tile width (0=auto)");
        s6 = new JSpinner(new SpinnerNumberModel(0, 0, 3840, 10));

        // tile height
        l7 = new JLabel("Tile height (0=auto)");
        s7 = new JSpinner(new SpinnerNumberModel(0, 0, 2160, 10));

        // init arrays
        labels = new JLabel[]{l1, l2, l3, l4, l5, l6, l7};
        spinners = new JSpinner[]{s1, s2, s3, s4, s5, s6, s7};

        // render button
        renderButton = new JButton("Render");
        renderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {                
                try {
                    for (JSpinner spinner : spinners) {
                        spinner.commitEdit();
                    }
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                int scene   = (Integer) s1.getValue();
                int samples = (Integer) s2.getValue();
                int bounces = (Integer) s3.getValue();
                int width   = (Integer) s4.getValue();
                int threads = (Integer) s5.getValue();
                int tileX   = (Integer) s6.getValue();
                int tileY   = (Integer) s7.getValue();

                c.setScene(scene);
                c.configCam(width, samples, bounces);
                c.configThreads(threads, tileX, tileY);
                c.callRender();
            }
        });

        // positioning
        int left    = 10;
        int yOffset = 35;
        int lWidth  = 140;
        int lHeight = 15;
        int sWidth  = 60;
        int sHeight = 20;

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(font);
            labels[i].setBounds(left, i * yOffset, lWidth, lHeight);

            spinners[i].setFont(font);
            spinners[i].setBounds(left, 15 + i * yOffset, sWidth, sHeight);
        }

        renderButton.setFont(font);
        renderButton.setBounds(10, 250, 180, 20);
        
        sceneLabel.setFont(font);
        sceneLabel.setBounds(70, 15, 70, 20);
    }

    public void showMenu() {
        initElements();
        // add to frame
        frame = new JFrame("Config");
        frame.getContentPane().setPreferredSize(new Dimension(200, 280));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                frame.dispose();
            }
        });

        for (int i = 0; i < labels.length; i++) {
            frame.add(labels[i]);
            frame.add(spinners[i]);
        }

        frame.add(renderButton);
        frame.add(sceneLabel);

        frame.revalidate();
        frame.repaint();
    }  
}
