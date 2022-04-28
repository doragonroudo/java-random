import java.awt.Graphics;

public class Background extends Entity{
    
    private Animation animBg;

    public Background(float x, float y, int width, int height) {
        super(x, y, width, height);
        animBg = new Animation(135, Assets.bg); // half a second
    }

    @Override
    public void tick() {
        animBg.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animBg.getCurrentFrame(), 0, 0, width, height, null);
    }
    
}
