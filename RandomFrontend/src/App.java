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

    private BufferedImage backgroundImage;

    private void init() {

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

        backgroundImage = ImageLoader.loadImage("/img/bg/1.jpg");

    }

    private void tick() {

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
        g.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight(), null);

        // End draw

        bs.show();
        g.dispose();
    }

    public void run() {
        init();

        while(running) {
            tick();
            render();
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
}
