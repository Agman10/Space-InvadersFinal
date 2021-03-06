import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Game extends JPanel {
    JFrame frame;
    static Invader[] invaders = new Invader[50];
    static final int WIDTH = 210;
    static final int HEIGHT = 230;
    static final int SCALE = 3;
    static BufferedImage invader;
    public static Set<Integer> keysDown = new HashSet<>();

    static Player player;

    static Bullet bullet;

    static int invaderSpacing = 16; //avståndet mellan invaders
    static int invadersPerRow = 10; //antallet iinvader pär rad
    static int invadersWidth = invaderSpacing * invadersPerRow;
    static boolean movingLeft = true;
    static int invadersX = (WIDTH / 2) - ((invadersPerRow * invaderSpacing) / 2);
    static int invadersY = 15;

    static int margin = 5;
    static int minX = margin;
    static int maxX = (WIDTH - margin);


    public static void main(String[] args) throws IOException {

        player = new Player(WIDTH / 2, 200, ImageIO.read(new File("textures/player.png"))); //spelarens texture
        bullet = new Bullet(0, 0, ImageIO.read(new File("textures/playerBullet.png"))); //skottets texture
        populateInvaders();
        Game game = new Game();
    }


    public static void populateInvaders() throws IOException {
        //int invaderSpacing = (invader.getWidth() + 4) * SCALE;
        for (int i = 0; i < invaders.length; i++) {



            int x = (i % invadersPerRow);
            int y = ((i - x) / invadersPerRow);


            //placera anorlunda invader sprites på olika platser
            BufferedImage sprite = ImageIO.read(new File("textures/invaderSprite1.png"));
            if(i > 29){
                sprite = ImageIO.read(new File("textures/invaderSprite5.png"));
            }
            if(i < 10){
                sprite = ImageIO.read(new File("textures/invaderSprite3.png"));
            }
            invaders[i] = new Invader((x * invaderSpacing), y * invaderSpacing, sprite);

            //System.out.println("New invaders: " + x * invaderSpacing + " - " + y * invaderSpacing);
        }
    }

    double invaderScale = .5;



    public Game() throws IOException {
        this.frame = new JFrame("space invaders");
        this.frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
        this.frame.add(this);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.decode("#111111"));
        //this.frame.setIconImage(img.getImage());
        this.addKeyListener(new Listener());
        this.setFocusable(true);
        this.frame.setVisible(true);
        //this.frame.setLayout(new BorderLayout());
        //this.frame.pack();
        // Import test

    }

    // här kollar spelet om en kollision har hänt.
    private void checkCollision() {
        if (!bullet.onscreen) return;
        for (int i = 0; i < invaders.length; i++) {
            if (invaders[i].alive) {

                //kolisionen
                if (invaders[i].x + invadersX < bullet.x + bullet.sprite.getWidth() && invaders[i].x + invadersX + invaders[i].sprite.getWidth() > bullet.x && invaders[i].y + invadersY < bullet.y + bullet.sprite.getHeight() && invaders[i].sprite.getHeight() + invaders[i].y + invadersY > bullet.y) {
                    // En kollision har hänt!
                    invaders[i].alive = false;
                    bullet.onscreen = false;
                }


            }
        }

    }


    int lastUpdate = 0;

    @Override
    public void paintComponent(Graphics g) {


        //logik


        checkCollision();
        //få spelaren att gå vänster eller höger genom att trycka på piltangenterna
        if (keysDown.contains(37)) {
            player.x--;
        }
        if (keysDown.contains(39)) {
            player.x++;
        }
        //om skottet är i skärmen så åker den uppåt.
        if (bullet.onscreen) {
            bullet.y--;
        }
        //om skottets y position är 0 så är den inte i skärmen
        if (bullet.y < 0) {
            bullet.onscreen = false;
        }


        if (g == null) {
            System.out.println("Null");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
            return;
        }



        /* Clear background */

        g.setColor(Color.decode("#111111"));
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
        lastUpdate++;
        //bestämmer hur snabbt invaderserna går åt sidan och ner
        if (lastUpdate > 3) {
            int xSpeed = 1;
            int ySpeed = 3;

            //om invaderserna åker till vänster så blir X kordinaten minus annars blir den plus
            lastUpdate = 0;
            if (movingLeft) {
                invadersX -= xSpeed;
            } else {
                invadersX += xSpeed;
            }
            //System.out.println(invadersX + " : " + (maxX));

            //om invaders X kordinat är mer än maxX vilket är så långt den kan vara på höger sida så åker dem åt vänster om går ner några steg
            if (invadersX > maxX - invadersWidth) {
                movingLeft = true;
                invadersY += ySpeed;
            }

            //om minX asså längst åt vänster är mer än Invaders X kordinat så går den inte år vänster och den går ner några steg.
            if (invadersX < minX) {
                movingLeft = false;
                invadersY += ySpeed;
            }

        }

        //invaders ritas
        for (int i = 0; i < invaders.length; i++) {
            if (invaders[i].alive) drawImage(invaders[i].sprite, invaders[i].x + invadersX, invaders[i].y + invadersY, g);
        }
        // rita ut skottet
        if (bullet.onscreen) {
            drawImage(bullet.sprite, bullet.x, bullet.y, g);
        }

        //rita ut spelaren
        drawImage(player.sprite, player.x, player.y, g);


        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        repaint();
    }

    public void drawImage(BufferedImage img, int x, int y, Graphics g) {
        try {
            g.drawImage(img, x * SCALE, y * SCALE, img.getWidth() * SCALE, img.getHeight() * SCALE, this);
        } catch (NullPointerException e) {
            System.out.println("Problem drawing image. " + e.getMessage());
        }

    }


    public class Listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!keysDown.contains(e.getKeyCode())) keysDown.add(e.getKeyCode());
            if (!bullet.onscreen && e.getKeyCode() == 32) {
                bullet.onscreen = true;
                bullet.x = player.x + 6;
                bullet.y = player.y;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(e.getKeyCode());
        }
    }
}