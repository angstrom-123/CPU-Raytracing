package com.ang;

public class Main 
{
    public static void main( String[] args )
    {
        Menu menu = new Menu(new ConfigHandler());
        menu.showMenu();
    }
}
