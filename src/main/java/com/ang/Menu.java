package com.ang;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Creates a menu for the user to alter render parameters.
 */
public class Menu extends JFrame {
    private Font font = new Font("dialog", Font.PLAIN, 11);

    private ConfigHandler   c;
    private JFrame          frame;
    private JButton         renderButton;
    private JLabel          sceneLabel;
    private JCheckBox       doSave;
    private JLabel          l1, l2, l3, l4, l5, l6, l7;
    private JLabel          filePath, fileName;
    private JTextField      t1, t2;
    private JSpinner        s1, s2, s3, s4, s5, s6, s7;
    
    private JLabel[]        labels;
    private JSpinner[]      spinners;

    /**
     * Constructs the menu.
     * @param c new ConfigHandler.
     */
    public Menu(ConfigHandler c) {
        this.c = c;
    }

    /**
     * Initializes all elements in the menu.
     */
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

        // per-pixel sample count selector
        l2 = new JLabel("Samples per pixel");
        s2 = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));

        // maximum ray bounce limit selector
        l3 = new JLabel("Max ray bounces");
        s3 = new JSpinner(new SpinnerNumberModel(20, 1, 500, 5));

        // width of output image selector
        l4 = new JLabel("Horizontal resolution");
        s4 = new JSpinner(new SpinnerNumberModel(426, 1, 3840, 100));

        // thread count selector
        l5 = new JLabel("Number of threads");
        s5 = new JSpinner(new SpinnerNumberModel(3, 1, 50, 1));

        // worker tile width selector
        l6 = new JLabel("Tile width (0=auto)");
        s6 = new JSpinner(new SpinnerNumberModel(0, 0, 3840, 10));

        // worker tile height selector
        l7 = new JLabel("Tile height (0=auto)");
        s7 = new JSpinner(new SpinnerNumberModel(0, 0, 2160, 10));

        // checkbox to save file
        doSave = new JCheckBox("Save file", true);

        // input field for file path
        filePath = new JLabel("File path");
        t1 = new JTextField(12);

        // input field for path name
        fileName = new JLabel("File name");
        t2 = new JTextField(12);

        labels = new JLabel[]{l1, l2, l3, l4, l5, l6, l7};
        spinners = new JSpinner[]{s1, s2, s3, s4, s5, s6, s7};

        // render button
        // When pressed, all settings are commited and passed to ConfigHandler.
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

                int scene       = (Integer) s1.getValue();
                int samples     = (Integer) s2.getValue();
                int bounces     = (Integer) s3.getValue();
                int width       = (Integer) s4.getValue();
                int threads     = (Integer) s5.getValue();
                int tileX       = (Integer) s6.getValue();
                int tileY       = (Integer) s7.getValue();
                boolean save    = doSave.isSelected();
                String path     = t1.getText();
                String name     = t2.getText();

                c.setScene(scene);
                c.configFile(path, name, save);
                c.configCam(width, samples, bounces);
                c.configThreads(threads, tileX, tileY);
                c.callRender();
            }
        });

        // positioning GUI, applying font
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

        filePath.setFont(font);
        filePath.setBounds(10, 270, 140, 15);

        fileName.setFont(font);
        fileName.setBounds(10, 305, 140, 15);

        doSave.setFont(font);
        doSave.setBounds(10, 250, 140, 20);

        t1.setFont(font);
        t1.setBounds(10, 285, 140, 20);

        t2.setFont(font);
        t2.setBounds(10, 320, 140, 20);

        renderButton.setFont(font);
        renderButton.setBounds(10, 355, 180, 20);
        
        sceneLabel.setFont(font);
        sceneLabel.setBounds(70, 15, 70, 20);
    }

    /**
     * Displays the menu.
     */
    public void showMenu() {
        initElements();

        frame = new JFrame("Config");
        frame.getContentPane().setPreferredSize(new Dimension(200, 400));
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

        frame.add(filePath);
        frame.add(fileName);
        frame.add(t1);
        frame.add(t2);
        frame.add(doSave);
        frame.add(renderButton);
        frame.add(sceneLabel);

        frame.revalidate();
        frame.repaint();
    }  
}
