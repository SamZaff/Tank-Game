package TankGame;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static javax.imageio.ImageIO.read;

class lazer {

    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private WorldGUI BG;
    private BufferedImage img;
    private final int R = 7;
    Rectangle hitbox;

    lazer(int x, int y, int vx, int vy, int angle, WorldGUI BG){

        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.BG = BG;

        try {
            this.img = read(new File("Sources/lazer.png"));
        }catch (IOException io) {
            io.getMessage();
        }
        hitbox = new Rectangle(x, y, img.getWidth(), img.getHeight());

    }

    void update(){
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        hitbox.x = x;
        hitbox.y = y;
    }

    boolean checkCollision() {
        return BG.checkBulletCollision(hitbox);
    }

    void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }
}