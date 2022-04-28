import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UIImage extends UIObject {

	private BufferedImage image;
	private ClickListener clicker;
	
	public UIImage(float x, float y, int width, int height, BufferedImage image, ClickListener clicker) {
		super(x, y, width, height);
		this.image = image;
		this.clicker = clicker;
	}

	@Override
	public void tick() {}

	@Override
	public void render(Graphics g) {
		if(hovering)
            g.drawImage(image,
            (int) x - (ImageLoader.getScaledDimensionWidth(new Dimension( image.getWidth(), image.getHeight()), new Dimension(width, height))/2),
            (int) y - (ImageLoader.getScaledDimensionHeight(new Dimension( image.getWidth(), image.getHeight() ), new Dimension(width, height))/2), 
            ImageLoader.getScaledDimensionWidth(new Dimension( image.getWidth(), image.getHeight()), new Dimension(width, height)), 
            ImageLoader.getScaledDimensionHeight(new Dimension( image.getWidth(), image.getHeight() ), new Dimension(width, height)), null);
		else
            g.drawImage(image,
            (int) x - (ImageLoader.getScaledDimensionWidth(new Dimension( image.getWidth(), image.getHeight()), new Dimension(width, height))/2),
            (int) y - (ImageLoader.getScaledDimensionHeight(new Dimension( image.getWidth(), image.getHeight() ), new Dimension(width, height))/2), 
            ImageLoader.getScaledDimensionWidth(new Dimension( image.getWidth(), image.getHeight()), new Dimension(width, height)), 
            ImageLoader.getScaledDimensionHeight(new Dimension( image.getWidth(), image.getHeight() ), new Dimension(width, height)), null);
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

}