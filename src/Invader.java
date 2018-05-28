import java.awt.image.BufferedImage;

public class Invader {
    int x;
    int y;
    boolean alive = true;
    BufferedImage sprite;


    public Invader(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
}
