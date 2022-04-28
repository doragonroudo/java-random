package dev.drg.random;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import dev.drg.random.Item;

public class Main {
    public static void main(String[] args) {
        // Screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // UI declaration
        JFrame frame = new JFrame("Random Backend");
        JPanel mainPanel = new JPanel();
        JPanel itemPanel = new JPanel();
        JPanel reportPanel = new JPanel();

        // Focusable
		mainPanel.setFocusable( true );
		itemPanel.setFocusable( true );
		reportPanel.setFocusable( true );

        // Main panel: Layout
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints cMainPanel = new GridBagConstraints();
        cMainPanel.fill = GridBagConstraints.BOTH;
        cMainPanel.weightx = 0.5;
        cMainPanel.weighty = 0.5;

        // Item panel; Layout
        itemPanel.setBorder(BorderFactory.createTitledBorder("Manage Item"));
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));

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

        boolean enable[] = new boolean[6];
        int stock[] = new int[6];
        String name[] = new String[6];
        String img[] = new String[6];

        for (String key : prop.stringPropertyNames()) {
            String[] fields = key.split("[.]");
            int index = Integer.parseInt(fields[1]);
            String field = fields[2];

            if (field.equals("enable"))
                enable[index] = Boolean.parseBoolean(prop.getProperty(key));
            if (field.equals("stock"))
                stock[index] = Integer.parseInt(prop.getProperty(key));
            if (field.equals("name"))
                name[index] = prop.getProperty(key);
            if (field.equals("img"))
                img[index] = prop.getProperty(key);
            // System.out.println(key + ": " + prop.getProperty(key));
        }
        // Items
        for (int i = 0; i < 6; i++) {
            if (name[i] != null) {
                System.out.println(i);
                itemPanel.add(new Item(i, name[i], stock[i], img[i], enable[i]));
            }
        }

        // Add Item panel to Main panel
        // itemPanel.setSize(50, 50);
        // itemPanel.setMaximumSize(new Dimension(100, screenSize.height));
        itemPanel.setPreferredSize(new Dimension(100, screenSize.height + 40));
        JScrollPane itemScrollPane = new JScrollPane(itemPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cMainPanel.gridx = 0;
        cMainPanel.gridy = 0;
        mainPanel.add(itemScrollPane, cMainPanel);

    
        String[] columnNames = {"Timestamp", "Item", "Stock Balance"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        try {
            String line;
            BufferedReader bufferreader = new BufferedReader(new FileReader("report.csv"));
            int lineCount = 0;
            while ((line = bufferreader.readLine()) != null) {     
                System.out.println(line);
                String[] row = line.split("[,]");
                if (lineCount > 0) {
                    model.addRow(row);
                }
                lineCount++;
            }
    
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JTable table = new JTable(model);
        JScrollPane reportScrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cMainPanel.gridx = 0;
        cMainPanel.gridy = 0;
        reportPanel.setLayout(new BorderLayout());
        reportPanel.add(reportScrollPane, BorderLayout.CENTER);

        // Report panel
        reportPanel.setBorder(BorderFactory.createTitledBorder("Report"));
        cMainPanel.gridx = 1;
        cMainPanel.gridy = 0;
        mainPanel.add(reportPanel, cMainPanel);

        // Main panel
        mainPanel.setBackground(Color.BLACK);
        frame.add(mainPanel);

        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }
        });

        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }
        });

        reportPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }
        });

        // Frame settings
        frame.setSize(screenSize.width, screenSize.height);
        // frame.setSize(640, 480);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        // f.setUndecorated(true); // Hide menu bar
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
