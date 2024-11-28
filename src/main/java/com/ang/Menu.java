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

    private ConfigHandler c;
    private JFrame        frame;
    private JButton       button;
    private JLabel        l1, l2, l3, l4, l5, l6, l7;
    private JLabel        sceneLabel;
    private JSpinner      s1, s2, s3, s4, s5, s6, s7;
    
    public Menu(ConfigHandler c) {
        this.c = c;
    }

    public void showMenu() {
        // scene selector
        l1 = new JLabel("Demo scene");   
        s1 = new JSpinner(new SpinnerNumberModel(1,1,5,1));
        sceneLabel = new JLabel("Random balls");
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
        s2 = new JSpinner(new SpinnerNumberModel(100,1,10000,10));

        // bounce limit
        l3 = new JLabel("Max ray bounces");
        s3 = new JSpinner(new SpinnerNumberModel(20,1,500,5));

        // image width
        l4 = new JLabel("Horizontal resolution");
        s4 = new JSpinner(new SpinnerNumberModel(426,1,3840,100));

        // thread count
        l5 = new JLabel("Number of threads");
        s5 = new JSpinner(new SpinnerNumberModel(3,1,50,1));

        // tile width
        l6 = new JLabel("Tile width (0=auto)");
        s6 = new JSpinner(new SpinnerNumberModel(0,0,3840,10));

        // tile height
        l7 = new JLabel("Tile height (0=auto)");
        s7 = new JSpinner(new SpinnerNumberModel(0,0,2160,10));

        // render
        button = new JButton("Render");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {                
                try {
                    s1.commitEdit();
                    s2.commitEdit();
                    s3.commitEdit();
                    s4.commitEdit();
                    s5.commitEdit();
                    s6.commitEdit();
                    s7.commitEdit();
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

                // frame.dispose();

                c.setScene(scene);
                c.configCam(width, samples, bounces);
                c.configThreads(threads, tileX, tileY);
                c.callRender();
            }
        });

        // set font
        l1.setFont(font);
        l2.setFont(font);
        l3.setFont(font);
        l4.setFont(font);
        l5.setFont(font);
        l6.setFont(font);
        l7.setFont(font);

        s1.setFont(font);
        s2.setFont(font);
        s3.setFont(font);
        s4.setFont(font);
        s5.setFont(font);
        s6.setFont(font);
        s7.setFont(font);

        button.setFont(font);
        sceneLabel.setFont(font);

        // positioning
        l1.setBounds(10,   0, 140, 15);
        l2.setBounds(10,  35, 140, 15);
        l3.setBounds(10,  70, 140, 15);
        l4.setBounds(10, 105, 140, 15);
        l5.setBounds(10, 140, 140, 15);
        l6.setBounds(10, 175, 140, 15);
        l7.setBounds(10, 210, 140, 15);

        s1.setBounds(10,  15, 60, 20);
        s2.setBounds(10,  50, 60, 20);
        s3.setBounds(10,  85, 60, 20);
        s4.setBounds(10, 120, 60, 20);
        s5.setBounds(10, 155, 60, 20);
        s6.setBounds(10, 190, 60, 20);
        s7.setBounds(10, 225, 60, 20);

        button.setBounds(10, 250, 180, 20);
        sceneLabel.setBounds(70, 15, 70, 20);

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

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(l4);
        frame.add(l5);
        frame.add(l6);
        frame.add(l7);

        frame.add(s1);
        frame.add(s2);
        frame.add(s3);
        frame.add(s4);
        frame.add(s5);
        frame.add(s6);
        frame.add(s7);

        frame.add(button);
        frame.add(sceneLabel);
    }  
}
