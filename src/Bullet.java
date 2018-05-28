import java.awt.image.BufferedImage;

public class Bullet {
    int x;
    int y;
    boolean onscreen = false;
    BufferedImage sprite;

    public Bullet(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
}


