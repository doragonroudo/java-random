import java.awt.image.BufferedImage;

public class Assets {
	
	private static final int width = 1080, height = 1920;
	private static final int bwidth = 1613, bheight = 569;

	public static BufferedImage bgStatic;
    public static BufferedImage[] bg;
    public static BufferedImage[] randBtn; // 1613 × 569

	public static void init(){
		// SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/sheet.png"));
		
		// player = sheet.crop(0, 0, width, height);
		// dirt = sheet.crop(width, 0, width, height);
		// grass = sheet.crop(width * 2, 0, width, height);
		// stone = sheet.crop(width * 3, 0, width, height);
		// tree = sheet.crop(0, height, width, height);

        SpriteSheet bgSheet = new SpriteSheet(ImageLoader.loadImage("/img/bg_sprite.jpg"));
		SpriteSheet btnSheet = new SpriteSheet(ImageLoader.loadImage("/img/btn_sprite.png"));
		
        bgStatic = bgSheet.crop(0, 0, width, height);
        bg = new BufferedImage[31];
        for (int i = 0; i < 31; i++) {
            bg[i] = bgSheet.crop(i * width, 0, width, height);
        }
        randBtn = new BufferedImage[2];
        for (int i = 0; i < 2; i++) {
            randBtn[i] = btnSheet.crop(i * bwidth, 0, bwidth, bheight);
        }
	}
	
}