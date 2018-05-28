import java.awt.image.BufferedImage;

public class Player {
    int x;
    int y;
    boolean alive = true;
    BufferedImage sprite;

    public Player(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
}
