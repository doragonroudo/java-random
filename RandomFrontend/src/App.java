import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class App implements Runnable{

    public Thread thread;
    private boolean running = false;

    private int width = 576;
    private int height = 1024;

    private JFrame frame;
    private Canvas canvas;

    private BufferStrategy bs;
    private Graphics g;

    private MouseManager mouseManager;
    private UIManager uiManager;

    Background bg;

    Properties itemProps;

    // States
    // private State gameState;

    public App() {
        Assets.init();
        mouseManager = new MouseManager();
        uiManager = new UIManager();
        this.getMouseManager().setUIManager(uiManager);
        this.itemProps = getProperty();
    }

    private void init() {

        // Start Frame

        frame = new JFrame("Random Frontend");
        canvas = new Canvas();

        // Frame settings
        // frame.setSize(screenSize.width, screenSize.height);
        frame.setSize(width, height); // BG is 16:9 (1080x1920)
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        // f.setUndecorated(true); // Hide menu bar
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Canvas settings
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));

        frame.add(canvas);
        frame.pack();

        // End Frame

        frame.addMouseListener(mouseManager);
        frame.addMouseMotionListener(mouseManager);
        canvas.addMouseListener(mouseManager);
        canvas.addMouseMotionListener(mouseManager);

        Assets.init();

        // backgroundImage = ImageLoader.loadImage("/img/bg_sprite.jpg");
        // bgSpriteSheet = new SpriteSheet(backgroundImage);

        // Load from config file
        boolean enable[] = new boolean[6];
        int stock[] = new int[6];
        String name[] = new String[6];
        String img[] = new String[6];

        for (String key : itemProps.stringPropertyNames()) {
            String[] fields = key.split("[.]");
            int index = Integer.parseInt(fields[1]);
            String field = fields[2];

            if (field.equals("enable"))
                enable[index] = Boolean.parseBoolean(itemProps.getProperty(key));
            if (field.equals("stock"))
                stock[index] = Integer.parseInt(itemProps.getProperty(key));
            if (field.equals("name"))
                name[index] = itemProps.getProperty(key);
            if (field.equals("img"))
                img[index] = itemProps.getProperty(key);
            // System.out.println(key + ": " + prop.getProperty(key));
        }

        // Count Enabled Items
        int enabledItemCount = 0;
        for (int i = 0; i < 6; i++) {
            if (enable[i]) { // if item is enable 
                enabledItemCount++;
            }
        }

        // Setup layout for each 1-6 enabled 150x150 < img size
        int posArrayX[][] =  {
                            { (canvas.getWidth()/6)*2-75, (canvas.getWidth()/6)*4-75 },
                            { (canvas.getWidth()/6)*2-75, (canvas.getWidth()/6)*4-75, (canvas.getWidth()/6)*3-75 },
                            { (canvas.getWidth()/6)*1-75, (canvas.getWidth()/6)*5-75, (canvas.getWidth()/6)*2-75, (canvas.getWidth()/6)*4-75 },
                            { (canvas.getWidth()/6)*1-75, (canvas.getWidth()/6)*5-75, (canvas.getWidth()/6)*2-75, (canvas.getWidth()/6)*4-75, (canvas.getWidth()/6)*3-75 },
                            { (canvas.getWidth()/6)*1-75, (canvas.getWidth()/6)*5-75, (canvas.getWidth()/6)*2-75, (canvas.getWidth()/6)*4-75, (canvas.getWidth()/6)*2-75, (canvas.getWidth()/6)*4-75 }
                        };
        int posArrayY[][] =  {
                            { (canvas.getHeight()/12)*6-0, (canvas.getHeight()/12)*6-0 },
                            { (canvas.getHeight()/12)*5-0, (canvas.getHeight()/12)*5-0, (canvas.getHeight()/12)*7-0 },
                            { (canvas.getHeight()/12)*5-0, (canvas.getHeight()/12)*5-0, (canvas.getHeight()/12)*7-0, (canvas.getHeight()/12)*7-0 },
                            { (canvas.getHeight()/12)*6-0, (canvas.getHeight()/12)*6-0, (canvas.getHeight()/12)*8-0, (canvas.getHeight()/12)*8-0, (canvas.getHeight()/12)*4-0},
                            { (canvas.getHeight()/12)*6-0, (canvas.getHeight()/12)*6-0, (canvas.getHeight()/12)*8-0, (canvas.getHeight()/12)*8-0, (canvas.getHeight()/12)*4-0, (canvas.getHeight()/12)*4-0 }
                        };
        
        // Set layout
        int posX[] = {};
        int posY[] = {};
        if (enabledItemCount <= 2) {
            posX = posArrayX[0];
            posY = posArrayY[0];
        } else if (enabledItemCount <= 3) {
            posX = posArrayX[1];
            posY = posArrayY[1];
        } else if (enabledItemCount <= 4) {
            posX = posArrayX[2];
            posY = posArrayY[2];
        } else if (enabledItemCount <= 5) {
            posX = posArrayX[3];
            posY = posArrayY[3];
        } else if (enabledItemCount <= 6) {
            posX = posArrayX[4];
            posY = posArrayY[4];
        }

        // Assign layout
        int slotCount = 0;
        for (int i = 0; i < 6; i++) {
            if (enable[i]) { // if item is enable 
                uiManager.addObject(new UIImage(posX[slotCount], posY[slotCount], 150, 150, ImageLoader.loadImageFromExternalSource(img[i]), new ClickListener() {

                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub
                        
                    }
                    
                }));
                slotCount++;
            }
        }
        
        bg = new Background(0, 0, canvas.getWidth(), canvas.getHeight());
        uiManager.addObject(new UIImageButton((canvas.getWidth()/2) - (283/2), (canvas.getHeight() - 100 - 50), 283, 100, Assets.randBtn, new ClickListener(){

            @Override
            public void onClick() {
                // random logic
            }

        }));
    }

    private void tick() {
        uiManager.tick();
        bg.tick();
        // System.out.println(this.getMouseManager().getMouseX() + ", " + this.getMouseManager().getMouseY());
    }

    private void render() {
        bs = canvas.getBufferStrategy();

        if(bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();

        // Clear screen

        g.clearRect(0, 0, width, height);

        // Draw here

        // g.fillRect(0, 0, 100, 100);
        bg.render(g);
        uiManager.render(g);
        // End draw

        bs.show();
        g.dispose();
    }

    public void run() {
        init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while(running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if(delta >= 1) {
                tick();
                render();
                ticks++;
                delta--;
            }
            if(timer >= 1000000000) {
                // System.out.println("FPS: " + ticks);
                ticks = 0;
                timer = 0;
            }
        }

        stop();
    }

    public synchronized void start() {
        if(running)
         return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if(!running)
         return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public void setProperty (String key, String value) {
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

    public Properties getProperty () {
		// Write file
		File configFile = new File("app.config");
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			reader.close();
			return props;
		} catch (FileNotFoundException ex) {
				// file does not exist
		} catch (IOException ex) {
				// I/O error
		}
        return null;
	}
}
