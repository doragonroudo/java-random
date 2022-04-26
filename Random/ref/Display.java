package dev.drg.random.display;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Display {

	private JFrame frame;
	
	private String title;
	private int width, height;
	
	public Display(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
		
		createDisplay();
	}
	
	private void createDisplay(){
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

        // Program icon on title bar
        ImageIcon icon = new ImageIcon("res/images/lol.png");
        frame.setIconImage(icon.getImage());

        JPanel panel = new JPanel();
        
        // Image
        BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(this.getClass().getResource("/images/lol.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));

		// Txt
		JLabel txt = new JLabel();
		txt.setText("text");

		// text input
		JTextField input = new JTextField();
		input.setPreferredSize( new Dimension( 200, 24 ) );
		// Adds

		panel.add(picLabel);
		panel.add(txt);
		panel.add(input);
		frame.add(panel);

		frame.setVisible(true);



        
	}
}
