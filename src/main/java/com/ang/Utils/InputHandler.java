package com.ang.Utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.ang.MainInterface;

public class InputHandler extends KeyAdapter
{
private MainInterface mainInterface;

public InputHandler(MainInterface mainInterface){
    this.mainInterface = mainInterface;
}

public void keyPressed(KeyEvent e){
    int keyCode = e.getKeyCode();
    switch (keyCode){
        case KeyEvent.VK_UP:
            mainInterface.turnUp();
            break;
        case KeyEvent.VK_DOWN:
            mainInterface.turnDown();
            break;
        case KeyEvent.VK_LEFT:
            mainInterface.turnLeft();
            break;
        case KeyEvent.VK_RIGHT:
            mainInterface.turnRight();
            break;
        case KeyEvent.VK_W:
            mainInterface.moveForward();
            break;
        case KeyEvent.VK_A:
            mainInterface.moveLeft();
            break;
        case KeyEvent.VK_S:
            mainInterface.moveBack();
            break;
        case KeyEvent.VK_D:
            mainInterface.moveRight();
    }
}
}