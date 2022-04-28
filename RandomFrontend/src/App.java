import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
    private UIManager itemManager;
    private UIManager resultManager;

    private ArrayList<Integer> randomAble = new ArrayList<Integer>();
    boolean enable[] = new boolean[6];
    int stock[] = new int[6];
    String name[] = new String[6];
    String img[] = new String[6];

    Background bg;
    Background flare;
    Background flareEndless;
    double flareTimer = 0;
    boolean flareEndlessEnabled = false;
    boolean resultEnabled = false;

    // States
    // private State gameState;

    public App() {
        Assets.init();
        mouseManager = new MouseManager();
        uiManager = new UIManager();
        itemManager = new UIManager();
        this.getMouseManager().setUIManager(uiManager);
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

        // Setup UI

        setUpUI();
        
        bg = new Background(0, 0, canvas.getWidth(), canvas.getHeight(), Assets.bg, 135);
        flare = new Background((canvas.getWidth()/2)-(192*2), (canvas.getHeight()/2)-(192*2), 192*4, 192*4, Assets.flare, 135);
        flareEndless = new Background((canvas.getWidth()/2)-(192*2), (canvas.getHeight()/2)-(192*2), 192*4, 192*4, Assets.flareEndless, 135);

        uiManager.addObject(new UIImageButton((canvas.getWidth()/2) - (283/2), (canvas.getHeight() - 100 - 50), 283, 100, Assets.randBtn, new ClickListener(){

            @Override
            public synchronized void onClick() {
                if(flareTimer > 0)
                    return;
                if(flareTimer <= 0 && flareEndlessEnabled) {
                    flareEndlessEnabled = false;
                    System.out.println("flare" + flareEndlessEnabled);
                    flare.setCurrentFrame(0);
                    flareEndless.setCurrentFrame(0);
                    return;
                }
                // TODO: random logic
                int min = 0; // inclusive
                int max = getRandomAble().size()-1; // exclusive
                System.out.println("Random between:" + min + " and " + max);
                int result = ThreadLocalRandom.current().nextInt(min, max + 1);

                // printing initial value
                System.out.print("The initial values in `randomAble` are : ");
                for (Integer value : getRandomAble()) {
                    System.out.print(value);
                    System.out.print(" ");
                }

                System.out.println();
                System.out.println("Randomed index is: " + result
                 + " price is " + getRandomAble().get(result)
                 + " stock is " + stock[result]
                 + " stock will become " + Integer.toString(stock[result] - 1));
                
                Boolean res = setProperty("item."+Integer.toString(getRandomAble().get(result))+".stock", Integer.toString(stock[result] - 1));
                System.out.println("Update res: " + res);

                flareTimer = 20 * 5.00;
                
                getRandomAble().clear();
                itemManager.getObjects().clear();
            }

        }));
    }

    private void tick() {
        uiManager.tick();
        itemManager.tick();

        if(flareTimer > 0) {
            flare.tick();
            flareEndlessEnabled = true;
        }

        if(flareTimer <= 0 && flareEndlessEnabled) {
            flareEndless.tick();
        }
        
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

        if(itemManager.getObjects().size() == 0) {
            System.out.println("Setting up UI");
            setUpUI();
        }
        uiManager.render(g);
        itemManager.render(g);
        if(flareTimer > 0) {
            flare.render(g);
            flareTimer--;
        }
        if(flareTimer <= 0 && flareEndlessEnabled) {
            flareEndless.render(g);
        }
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

    public void setStock(int[] stock) {
        this.stock = stock;
    }
    public void setEnable(boolean[] enable) {
        this.enable= enable;
    }
    public void setName(String[] name) {
        this.name = name;
    }
    public void setImg(String[] img) {
        this.img = img;
    }
    public void setRandomAble(ArrayList<Integer> randomAble) {
        this.randomAble = randomAble;
    }

    public int[] getStock() {
        return this.stock;
    }

    public boolean[] getEnable() {
        return this.enable;
    }

    public String[] getName() {
        return this.name;
    }

    public String[] getImg() {
        return this.img;
    }

    public ArrayList<Integer> getRandomAble() {
        return this.randomAble;
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

    public synchronized MouseManager getMouseManager() {
        return mouseManager;
    }

    public synchronized boolean setProperty (String key, String value) {
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
            return true;
		} catch (FileNotFoundException ex) {
				// file does not exist
		} catch (IOException ex) {
				// I/O error
		}
        return false;
	}

    public synchronized Properties getProperty () {
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

    public synchronized void setUpUI() {

        // Load from config file
        Properties itemProps = getProperty();

        boolean enableT[] = new boolean[6];
        int stockT[] = new int[6];
        String nameT[] = new String[6];
        String imgT[] = new String[6];

        for (String key : itemProps.stringPropertyNames()) {
            String[] fields = key.split("[.]");
            int index = Integer.parseInt(fields[1]);
            String field = fields[2];

            if (field.equals("enable"))
                enableT[index] = Boolean.parseBoolean(itemProps.getProperty(key));
            if (field.equals("stock"))
                stockT[index] = Integer.parseInt(itemProps.getProperty(key));
            if (field.equals("name"))
                nameT[index] = itemProps.getProperty(key);
            if (field.equals("img"))
                imgT[index] = itemProps.getProperty(key);
            // System.out.println(key + ": " + prop.getProperty(key));
        }

        this.setEnable(enableT);
        this.setStock(stockT);
        this.setName(nameT);
        this.setImg(imgT);

        // Count Enabled Items
        int enabledItemCount = 0;
        ArrayList<Integer> randomAbleT = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            if (enable[i] && stock[i] > 0) { // if item is enable and have stock
                randomAbleT.add(i);
                enabledItemCount++;
            }
        }
        this.setRandomAble(randomAbleT);

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
            if (enable[i] && stock[i] > 0) { // if item is enable 
                itemManager.addObject(new UIImage(posX[slotCount], posY[slotCount], 150, 150, ImageLoader.loadImageFromExternalSource(img[i]), null));
                slotCount++;
            }
        }
    }
}
