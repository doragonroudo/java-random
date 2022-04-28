import java.awt.image.BufferedImage;

public class Assets {
	
	private static final int width = 1080, height = 1920;
	
	public static BufferedImage bgStatic;
    public static BufferedImage[] bg;

	public static void init(){
		// SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/sheet.png"));
		
		// player = sheet.crop(0, 0, width, height);
		// dirt = sheet.crop(width, 0, width, height);
		// grass = sheet.crop(width * 2, 0, width, height);
		// stone = sheet.crop(width * 3, 0, width, height);
		// tree = sheet.crop(0, height, width, height);

        SpriteSheet bgSheet = new SpriteSheet(ImageLoader.loadImage("/img/bg_sprite.jpg"));
		
        bgStatic = bgSheet.crop(0, 0, width, height);
        bg = new BufferedImage[31];
        for (int i = 0; i < 31; i++) {
            bg[i] = bgSheet.crop(i * width, 0, width, height);
        }
        // bg[0] = bgSheet.crop(0, 0, width, height);
        // bg[1] = bgSheet.crop(1 * width, 0, width, height);
	}
	
}