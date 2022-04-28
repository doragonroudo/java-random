import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

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

    Background bg;

    // States
    // private State gameState;

    public App() {
        mouseManager = new MouseManager();
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

        bg = new Background(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    private void tick() {
        bg.tick();
        System.out.println(this.getMouseManager().getMouseX() + ", " + this.getMouseManager().getMouseY());
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
}
