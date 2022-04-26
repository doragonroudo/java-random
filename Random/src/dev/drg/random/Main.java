package dev.drg.random;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.*;

import dev.drg.random.components.MyDraggableComponent;

public class Main {

  private static ArrayList<MyDraggableComponent> items = new ArrayList<>();

  public static void main(String[] args) {
    JFrame f = new JFrame("Swing Hello World");

    // by doing this, we prevent Swing from resizing
    // our nice component
    f.setLayout(null);

    // Read item from file
    Properties prop = new Properties();
    String fileName = "app.config";
    try (FileInputStream fis = new FileInputStream(fileName)) {
        prop.load(fis);
    } catch (FileNotFoundException ex) {
        // FileNotFoundException catch is optional and can be collapsed
        ex.printStackTrace();
    } catch (IOException ex) {
        // ex
        ex.printStackTrace();
    }

    int posX[] = new int[20];
    int posY[] = new int[20];
    int stock[] = new int[20];
    String name[] = new String[20];
    String img[] = new String[20];

    for (String key: prop.stringPropertyNames()) {
      String[] fields = key.split("[.]");
      int index = Integer.parseInt(fields[1]);
      String field = fields[2];
      
      if(field.equals("posX"))
        posX[index] = Integer.parseInt(prop.getProperty(key));
      if(field.equals("posY"))
        posY[index] = Integer.parseInt(prop.getProperty(key));
      if(field.equals("stock"))
        stock[index] = Integer.parseInt(prop.getProperty(key));
      if(field.equals("name"))
        name[index] = prop.getProperty(key);
      if(field.equals("img"))
        img[index] = prop.getProperty(key);
      // System.out.println(key + ": " + prop.getProperty(key));
    }
    // Items
    for (int i = 0; i < 20; i++) {
      if(name[i] != null) {
        System.out.println(i);
        f.add(new MyDraggableComponent(posX[i], posY[i], img[i], i, stock[i], name[i]));
      }
    }

    // items.add(new MyDraggableComponent(50, 50, "/images/lol.png", 0, 10, "iPhone"));
    // items.add(new MyDraggableComponent(50, 100, "/images/lol.png", 1, 20, "AirPods Pro"));
    // for (MyDraggableComponent item : items) {
    //   f.add(item);
    // }

    // Menu Bar
    //Where the GUI is created:
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;
    JCheckBoxMenuItem cbMenuItem;

    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    menu = new JMenu("Item");
    menu.setMnemonic(KeyEvent.VK_A);
    // menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
    menuBar.add(menu);

    //a group of JMenuItems
    menuItem = new JMenuItem("Add new item", KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    // menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    // menuItem.addItemListener(l);
    menu.add(menuItem);

    f.setJMenuBar(menuBar);
    f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    // f.setUndecorated(true);
    f.setSize(500, 500);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.setVisible(true);
  }

}