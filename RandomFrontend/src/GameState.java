import java.awt.Graphics;

public class GameState extends State{

    public GameState() {

    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        g.drawImage(Assets.bgStatic, 0, 0, 576, 1024, null);
    }
    
}
