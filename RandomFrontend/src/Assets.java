import java.awt.image.BufferedImage;

public class Assets {
	

	public static BufferedImage bgStatic;
    public static BufferedImage[] bg;
    public static BufferedImage[] randBtn; // 1613 × 569
    public static BufferedImage[] backBtn; // 1613 × 569
    public static BufferedImage[] flare;
    public static BufferedImage[] flareEndless;

	public static void init(){
		// SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/sheet.png"));
		
		// player = sheet.crop(0, 0, width, height);
		// dirt = sheet.crop(width, 0, width, height);
		// grass = sheet.crop(width * 2, 0, width, height);
		// stone = sheet.crop(width * 3, 0, width, height);
		// tree = sheet.crop(0, height, width, height);

        SpriteSheet bgSheet = new SpriteSheet(ImageLoader.loadImage("/img/bg_new_sprite.jpg"));
		SpriteSheet btnSheet = new SpriteSheet(ImageLoader.loadImage("/img/btn_sprite.png"));
		SpriteSheet flareSheet = new SpriteSheet(ImageLoader.loadImage("/img/flare_sprite_recolored.png"));
		SpriteSheet btnBackSheet = new SpriteSheet(ImageLoader.loadImage("/img/btn_back.png"));
		
        int width = 1080, height = 1920;
        int bwidth = 1613, bheight = 569;
        int fwidth = 192, fheight = 192;

        bgStatic = bgSheet.crop(0, 0, width, height);
        bg = new BufferedImage[21];
        for (int i = 0; i < 21; i++) {
            bg[i] = bgSheet.crop(i * width, 0, width, height);
        }
        randBtn = new BufferedImage[2];
        for (int i = 0; i < 2; i++) {
            randBtn[i] = btnSheet.crop(i * bwidth, 0, bwidth, bheight);
        }
        backBtn = new BufferedImage[2];
        for (int i = 0; i < 2; i++) {
            backBtn[i] = btnBackSheet.crop(0, 0, bwidth, bheight);
        }
        flare = new BufferedImage[20];
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                flare[x+(y*4)] = flareSheet.crop(x * fwidth, y * fheight, fwidth, fheight);
            }
        }
        flareEndless = new BufferedImage[8];
        for (int x = 0; x < 5; x++) {
            flareEndless[x] = flareSheet.crop(x * fwidth, 3 * fheight, fwidth, fheight);
        }
        flareEndless[5] = flareSheet.crop(3 * fwidth, 3 * fheight, fwidth, fheight);
        flareEndless[6] = flareSheet.crop(2 * fwidth, 3 * fheight, fwidth, fheight);
        flareEndless[7] = flareSheet.crop(1 * fwidth, 3 * fheight, fwidth, fheight);
        
    }
}