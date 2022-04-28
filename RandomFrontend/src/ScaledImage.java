import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

public class ScaledImage extends Entity{
    
    private Animation animBg;

    public ScaledImage(float x, float y, int width, int height, BufferedImage[] frames, int speed) {
        super(x, y, width, height);
        animBg = new Animation(speed, frames); // half a second
    }

    @Override
    public void tick() {
        animBg.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animBg.getCurrentFrame(),
        (int) x - (ImageLoader.getScaledDimensionWidth(new Dimension( animBg.getCurrentFrame().getWidth(), animBg.getCurrentFrame().getHeight()), new Dimension(width, height))/2),
        (int) y - (ImageLoader.getScaledDimensionHeight(new Dimension( animBg.getCurrentFrame().getWidth(), animBg.getCurrentFrame().getHeight() ), new Dimension(width, height))/2), 
        ImageLoader.getScaledDimensionWidth(new Dimension( animBg.getCurrentFrame().getWidth(), animBg.getCurrentFrame().getHeight()), new Dimension(width, height)), 
        ImageLoader.getScaledDimensionHeight(new Dimension( animBg.getCurrentFrame().getWidth(), animBg.getCurrentFrame().getHeight() ), new Dimension(width, height)), null);
    }

    public void setCurrentFrame(int index){
		animBg.setCurrentFrame(index);
	}
    
}
