import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.Dimension;
import java.awt.image.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
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
    Background flareSmallEndless;
    ScaledImage randomItems;
    double randomTimer = 0;
    double flareTimer = 0;
    double flareSmallTimer = 0;
    boolean flareEndlessEnabled = false;
    boolean flareEnabled = false;
    boolean flareSmallEnabled = false;
    boolean randomEnabled = false;
    int randomResultIndex = 0;

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
        playSound("bg_fmt.wav", true);
        frame = new JFrame("Random Frontend");
        canvas = new Canvas();

        // Frame settings
        // Screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        // frame.setSize(width, height); // BG is 16:9 (1080x1920)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        frame.setUndecorated(true); // Hide menu bar
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Canvas settings
        canvas.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        canvas.setMaximumSize(new Dimension(screenSize.width, screenSize.height));
        canvas.setMinimumSize(new Dimension(screenSize.width, screenSize.height));

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
        flare = new Background((canvas.getWidth()/6)*3-(192*2), (canvas.getHeight()/12)*7-(192*2), 192*4, 192*4, Assets.flare, 135);
        flareSmallEndless = new Background((canvas.getWidth()/6)*3-(192*1), (canvas.getHeight()/12)*7-(192*1), 192*2, 192*2, Assets.flare, 135);
        flareEndless = new Background((canvas.getWidth()/6)*3-(192*2), (canvas.getHeight()/12)*7-(192*2), 192*4, 192*4, Assets.flareEndless, 135);

        BufferedImage[] items = new BufferedImage[1];
        String path = getImg()[getRandomAble().get(0)];
        items[0] = (ImageLoader.loadImageFromExternalSource(path));
        randomItems = new ScaledImage((canvas.getWidth()/2), (canvas.getHeight()/2)+100, 200, 200, items, 135);

        uiManager.addObject(new UIImageButton((canvas.getWidth()/2) - (283/2), (canvas.getHeight() - 100 - 50), 283, 100, Assets.randBtn, new ClickListener(){

            @Override
            public synchronized void onClick() {

                if(flareSmallTimer > 0)
                    return;
                if(flareTimer <= 0 && flareEndlessEnabled) {
                    flareEndlessEnabled = false;
                    randomEnabled = false;
                    System.out.println("flare" + flareEndlessEnabled);
                    flare.setCurrentFrame(0);
                    flareEndless.setCurrentFrame(0);
                    flareSmallEndless.setCurrentFrame(0);

                    getRandomAble().clear();
                    itemManager.getObjects().clear();
                    return;
                }

                playSound("slotmachine_long_fmt.wav", false);

                BufferedImage[] items = new BufferedImage[getRandomAble().size()];
                int randItemCount = 0;
                for (Integer index : getRandomAble()) {
                    String path = getImg()[index];

                    items[randItemCount] = (ImageLoader.loadImageFromExternalSource(path));
                    randItemCount++;
                }
                randomItems = new ScaledImage((canvas.getWidth()/2), (canvas.getHeight()/2)+100, 200, 200, items, 135);

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
                 + " stock is " + getStock()[getRandomAble().get(result)]
                 + " stock will become " + Integer.toString(getStock()[getRandomAble().get(result)] - 1));
                
                randomResultIndex = result;
                randomEnabled = true;
                
                Boolean res = setProperty("item."+Integer.toString(getRandomAble().get(result))+".stock", Integer.toString(getStock()[getRandomAble().get(result)] - 1));
                System.out.println("Update res: " + res);

                // Save to csv
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                // System.out.println(timestamp);
                String dataToWrite = timestamp + "," + getName()[getRandomAble().get(result)] + "," + Integer.toString(getStock()[getRandomAble().get(result)] - 1);

                try {
                    // you want to output to file
                    BufferedWriter writer = new BufferedWriter(new FileWriter("report.csv", true));
                    // but let's print to console while debugging
                    // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
                    writer.write(dataToWrite);
                    writer.newLine();
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                flareSmallTimer = 20 * 8.00;
                randomTimer = 20 * 8.00;
                flareTimer = 20 * 5.00;
                flareSmallEnabled = true;
            }

        }));

        uiManager.addObject(new UIImageButton(0, 0, canvas.getWidth(), canvas.getHeight(), new BufferedImage[2], new ClickListener() {

            @Override
            public void onClick() {
                // TODO Auto-generated method stub
                if(flareSmallTimer > 0)
                    return;
                if(flareTimer <= 0 && flareEndlessEnabled) {
                    flareEndlessEnabled = false;
                    randomEnabled = false;
                    System.out.println("flare" + flareEndlessEnabled);
                    flare.setCurrentFrame(0);
                    flareEndless.setCurrentFrame(0);
                    flareSmallEndless.setCurrentFrame(0);

                    getRandomAble().clear();
                    itemManager.getObjects().clear();
                    return;
                }
            }
            
        }));
    }

    private void tick() {
        uiManager.tick();
        itemManager.tick();

        if(flareSmallTimer > 0) {
            flareSmallEndless.tick();
        }

        if(flareSmallTimer <= 0 && flareSmallEnabled) {
            flareSmallEnabled = false;
            flareEnabled = true;
        }

        if(flareTimer > 0 && flareEnabled) {
            flare.tick();
        }

        if(flareTimer <= 0 && flareEnabled) {
            flareEnabled = false;
            flareEndlessEnabled = true;
        }

        if(flareTimer <= 0 && flareEndlessEnabled) {
            flareEndless.tick();
        }

        if(randomTimer > 0 && randomEnabled) {
            randomItems.tick();
        } else {
            randomItems.setCurrentFrame(randomResultIndex);
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

        if(flareSmallTimer > 0 && flareSmallEnabled) {
            flareSmallEndless.render(g);
            flareSmallTimer--;
        }

        if(flareTimer > 0 && flareEnabled) {
            flare.render(g);
            flareTimer--;
        }
        
        if(flareTimer <= 0 && flareEndlessEnabled) {
            flareEndless.render(g);
        }

        if(randomTimer > 0) {
            randomTimer--;
        }

        if (randomEnabled) {
            randomItems.render(g);
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
            if (enableT[i] && stockT[i] > 0) { // if item is enable and have stock
                randomAbleT.add(i);
                enabledItemCount++;
            }
        }
        this.setRandomAble(randomAbleT);

        // Setup layout for each 1-6 enabled 150x150 < img size
        int posArrayX[][] =  {
                            { (canvas.getWidth()/6)*2-0, (canvas.getWidth()/6)*4-0 },
                            { (canvas.getWidth()/6)*2-0, (canvas.getWidth()/6)*4-0, (canvas.getWidth()/6)*3-0 },
                            { (canvas.getWidth()/6)*1-0, (canvas.getWidth()/6)*5-0, (canvas.getWidth()/6)*2-0, (canvas.getWidth()/6)*4-0 },
                            { (canvas.getWidth()/6)*1-0, (canvas.getWidth()/6)*5-0, (canvas.getWidth()/6)*2-0, (canvas.getWidth()/6)*4-0, (canvas.getWidth()/6)*3-0 },
                            { (canvas.getWidth()/6)*1-0, (canvas.getWidth()/6)*5-0, (canvas.getWidth()/6)*2-0, (canvas.getWidth()/6)*4-0, (canvas.getWidth()/6)*2-0, (canvas.getWidth()/6)*4-0 }
                        };
        int posArrayY[][] =  {
                            { (canvas.getHeight()/12)*6+75, (canvas.getHeight()/12)*6+75 },
                            { (canvas.getHeight()/12)*5+75, (canvas.getHeight()/12)*5+75, (canvas.getHeight()/12)*7+75 },
                            { (canvas.getHeight()/12)*5+75, (canvas.getHeight()/12)*5+75, (canvas.getHeight()/12)*7+75, (canvas.getHeight()/12)*7+75 },
                            { (canvas.getHeight()/12)*6+75, (canvas.getHeight()/12)*6+75, (canvas.getHeight()/12)*8+75, (canvas.getHeight()/12)*8+75, (canvas.getHeight()/12)*4+75},
                            { (canvas.getHeight()/12)*6+75, (canvas.getHeight()/12)*6+75, (canvas.getHeight()/12)*8+75, (canvas.getHeight()/12)*8+75, (canvas.getHeight()/12)*4+75, (canvas.getHeight()/12)*4+75 }
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
            if (enableT[i] && stockT[i] > 0) { // if item is enable 

                itemManager.addObject(new UIImage(posX[slotCount], posY[slotCount], 150, 150, ImageLoader.loadImageFromExternalSource(imgT[i]), null));
                slotCount++;
            }
        }
    }

    public static synchronized void playSound(final String url, boolean isLoop) {
        new Thread(new Runnable() {
        // The wrapper thread is unnecessary, unless it blocks on the
        // Clip finishing; see comments.
            public void run() {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                App.class.getResource("/wav/" + url));
                clip.open(inputStream);
                if(isLoop)
                    clip.loop(Clip.LOOP_CONTINUOUSLY); 
                else
                    clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            }
        }).start();
        }
}
