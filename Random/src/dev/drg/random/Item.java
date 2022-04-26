package dev.drg.random;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class Item extends JPanel {
	private String name = "";
	private int stock = 0;
	private String imagePath = "";
	private boolean enable = false;
	private int id = 0;

	public Item(int id, String name, int stock, String imagePath, boolean enable) {

		// Set class variables
		this.name = name;
		this.stock = stock;
		this.imagePath = imagePath;
		this.enable = enable;
		this.id = id;

		// UI Setting
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		// this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getMinimumSize().height));
		this.setPreferredSize(new Dimension(450, 300));
		// this.setBorder(new LineBorder(Color.RED, 3));
		this.setFocusable( true );

		// UI init
		JLabel imgLabel = new JLabel();
		JButton browseBtn = new JButton();
		JLabel nameTxt = new JLabel();
		JLabel stockTxt = new JLabel();
		JTextField nameInput = new JTextField();
		JTextField stockInput = new JTextField();
		JCheckBox enableCheck = new JCheckBox();

		// Set layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints cPanel = new GridBagConstraints();
		cPanel.fill = GridBagConstraints.HORIZONTAL;
		cPanel.anchor = GridBagConstraints.CENTER;
		cPanel.weightx = 1.0;
		cPanel.weighty = 1.0;

		// Image label
		// imgLabel.setBorder(new LineBorder(Color.BLUE, 3));
		// imgLabel.setBackground(Color.BLUE);
		imgLabel.setMaximumSize(new Dimension(150, 150));
    imgLabel.setPreferredSize(new Dimension(150, 150));
		imgLabel.setHorizontalAlignment(JLabel.CENTER);
		cPanel.gridheight = 3;
		cPanel.gridwidth = 2;
		cPanel.gridx = 0;
		cPanel.gridy = 0;

		try {
			imgLabel.setIcon(new ImageIcon(new ImageIcon(this.imagePath).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
		} catch (Exception e) {
			System.out.println("DEBUG: can't load image, showing default one");
			imgLabel.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/images/lol.png")).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
		}
		
		this.add(imgLabel, cPanel);

		// Text: name
		nameTxt.setText("Name");
		// nameTxt.setBorder(new LineBorder(Color.BLUE, 3));
		nameTxt.setMaximumSize(new Dimension(50, 40));
    nameTxt.setPreferredSize(new Dimension(50, 40));
		cPanel.weightx = 0;
		cPanel.gridheight = 1;
		cPanel.gridwidth = 1;
		cPanel.gridx = 2;
		cPanel.gridy = 0;
		this.add(nameTxt, cPanel);

		// Input: name
		nameInput.setText(this.name);
		// nameInput.setBorder(new LineBorder(Color.BLUE, 3));
		nameInput.setMaximumSize(new Dimension(100, 40));
    nameInput.setPreferredSize(new Dimension(100, 40));
		cPanel.weightx = 1;
		cPanel.gridheight = 1;
		cPanel.gridwidth = 2;
		cPanel.gridx = 3;
		cPanel.gridy = 0;
		this.add(nameInput, cPanel);

		// Text: stock
		stockTxt.setText("Stock");
		// stockTxt.setBorder(new LineBorder(Color.BLUE, 3));
		stockTxt.setMaximumSize(new Dimension(50, 40));
    stockTxt.setPreferredSize(new Dimension(50, 40));
		cPanel.weightx = 0;
		cPanel.gridheight = 1;
		cPanel.gridwidth = 1;
		cPanel.gridx = 2;
		cPanel.gridy = 1;
		this.add(stockTxt, cPanel);

		// Input: stock
		stockInput.setText(Integer.toString(this.stock));
		// stockInput.setBorder(new LineBorder(Color.BLUE, 3));
		stockInput.setMaximumSize(new Dimension(100, 40));
    stockInput.setPreferredSize(new Dimension(100, 40));
		cPanel.weightx = 1;
		cPanel.gridheight = 1;
		cPanel.gridwidth = 2;
		cPanel.gridx = 3;
		cPanel.gridy = 1;
		this.add(stockInput, cPanel);

		// Enable
		enableCheck.setText("Enable");
		enableCheck.setSelected(this.enable);
		cPanel.gridheight = 1;
		cPanel.gridwidth = 3;
		cPanel.gridx = 2;
		cPanel.gridy = 2;
		this.add(enableCheck, cPanel);

		// Button
		browseBtn.setText("Browse");
		browseBtn.setMaximumSize(new Dimension(150, 40));
    browseBtn.setPreferredSize(new Dimension(150, 40));
		// browseBtn.setBorder(new LineBorder(Color.BLUE, 3));
		cPanel.fill = GridBagConstraints.NONE;
		cPanel.ipadx = 60;
		cPanel.weightx = 0;
		cPanel.gridheight = 1;
		cPanel.gridwidth = 2;
		cPanel.gridx = 0;
		cPanel.gridy = 3;
		this.add(browseBtn, cPanel);

		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.dir")));
				// filtering files
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "png");
				file.addChoosableFileFilter(filter);
				int res = file.showSaveDialog(null);
				// if the user clicks on save in Jfilechooser
				if (res == JFileChooser.APPROVE_OPTION) {
					File selFile = file.getSelectedFile();
					String path = selFile.getAbsolutePath();
					System.out.println("DEBUG: Loaded image path is:" + path);
					imgLabel.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
					setProperty("item."+Integer.toString(id)+"."+"img", path);
				}
			}
		});

		nameInput.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				System.out.println("User entered " + nameInput.getText());
				setProperty("item."+Integer.toString(id)+"."+"name", nameInput.getText());
			}
		});

		stockInput.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				System.out.println("User entered " + stockInput.getText());
				setProperty("item."+Integer.toString(id)+"."+"stock", stockInput.getText());
			}
		});

		enableCheck.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				System.out.println("User entered " + Boolean.toString(enableCheck.isSelected()));
				setProperty("item."+Integer.toString(id)+"."+"enable", Boolean.toString(enableCheck.isSelected()));
			}
		});

		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
					KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
			}
	});

	}

	void setProperty (String key, String value) {
		// Write file
		File configFile = new File("app.config");
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			reader.close();
			props.setProperty(key, value);
			FileWriter writer = new FileWriter(configFile);
			props.store(writer, "item settings");
			writer.close();
		} catch (FileNotFoundException ex) {
				// file does not exist
		} catch (IOException ex) {
				// I/O error
		}
	}
}
