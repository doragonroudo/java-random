import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Background extends Entity{
    
    private Animation animBg;

    public Background(float x, float y, int width, int height, BufferedImage[] frames, int speed) {
        super(x, y, width, height);
        animBg = new Animation(speed, frames); // half a second
    }

    @Override
    public void tick() {
        animBg.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animBg.getCurrentFrame(), (int) x, (int) y, width, height, null);
    }

    public void setCurrentFrame(int index){
		animBg.setCurrentFrame(index);
	}
    
}
