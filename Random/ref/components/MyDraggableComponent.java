package dev.drg.random.components;

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.*;
import java.io.IOException;

public class MyDraggableComponent extends JPanel {

  private volatile int screenX = 0;
  private volatile int screenY = 0;
  private volatile int posX = 0;
  private volatile int posY = 0;
  private BufferedImage myPicture = null;
  private int stockLeft = 0;
	private int objectId = 0;
	private String itemName = "";

  public MyDraggableComponent(int x, int y, String image_path, int id, int stock, String name) {
    
		// Set properties
		posX = x;
		posY = y;
		objectId = id;
		stockLeft = stock;
		itemName = name;

    // Load Image
    loadImage(image_path);

		// Set UI value
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx  = 8;
		c.weighty  = 4;
		c.anchor = GridBagConstraints.CENTER;
		
    setBorder(new LineBorder(Color.BLUE, 3));
    // setBackground(Color.WHITE);
    setBounds(0, 0, myPicture.getWidth() + 50, myPicture.getHeight() + 100);
		setLocation(posX, posY);
    // setOpaque(false);

		// Add Image
		JLabel pictureLabel = new JLabel(new ImageIcon(myPicture));
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(pictureLabel, c);

		// Add Text `Name`
		JLabel textName = new JLabel();
		textName.setText("Name");
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(textName, c);

		// Add Input `Name`
		JTextField inputName = new JTextField();
		inputName.setPreferredSize( new Dimension( 200, 24 ) );
		inputName.setText(itemName);
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		add(inputName, c);

		// Add Text `Stock`
		JLabel textStock = new JLabel();
		textStock.setText("Stock");
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(textStock, c);

		// Add Input `Stock Value`
		JTextField inputStock = new JTextField();
		inputStock.setPreferredSize( new Dimension( 200, 24 ) );
		inputStock.setText(Integer.toString(stockLeft));
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		add(inputStock, c);

		// Edit BUtton
		JButton buttonEdit = new JButton();
		buttonEdit.setText("Edit");
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 3;
		add(buttonEdit, c);

		// Remove Button
		JButton buttonRemove = new JButton();
		buttonRemove.setText("Remove");
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		add(buttonRemove, c);
		

		// Mouse listener
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) { }

      @Override
      public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        posX = getX();
        posY = getY();
      }

      @Override
      public void mouseReleased(MouseEvent e) { }

      @Override
      public void mouseEntered(MouseEvent e) { }

      @Override
      public void mouseExited(MouseEvent e) { }

    });
    addMouseMotionListener(new MouseMotionListener() {

      @Override
      public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        setLocation(posX + deltaX, posY + deltaY);
      }

      @Override
      public void mouseMoved(MouseEvent e) { }

    });
  }
  public void loadImage(String image_path) {
    // Image
		try {
			myPicture = ImageIO.read(this.getClass().getResource("/images/lol.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}

