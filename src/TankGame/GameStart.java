package TankGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.awt.Image;
import java.util.ArrayList;
import static javax.imageio.ImageIO.read;

/**
 *
 * @author Sam Zaffanella
 */
public class GameStart extends JPanel  {

    private static final int SCREEN_WIDTH = 1400;
    private static final int SCREEN_HEIGHT = 800;
    private BufferedImage world;
    private BufferedImage divider;
    private int counter = 0;
    private Graphics2D buffer;
    private Image miniMap;
    private JFrame jf;
    private Tank t1;
    private Tank t2;
    private WorldGUI BG;
    private BufferedImage backGround = null, wall = null, breakableWall = null, powerUp = null;
    private ArrayList<lazer> lazer = new ArrayList<>();
    private ArrayList<lazer> lazer2 = new ArrayList<>();
    private SoundControl sfx = new SoundControl();
    private boolean newWorld = false;

    public static void main(String[] args) {
        GameStart trex = new GameStart();
        SoundControl music = new SoundControl();
        trex.init();
        try {
            //music.MusicStart("Theme.wav");
            while (true) {
                trex.t1.update();
                trex.t2.update();
                trex.lazer = trex.t1.getBulletList();
                for (int i = 0; i < trex.lazer.size(); i++) {
                    trex.lazer.get(i).update();
                    if (trex.lazer.get(i).checkCollision()) {
                        trex.t1.removeBullet(i);
                    }
                }
                trex.lazer2 = trex.t2.getBulletList();
                for (int i = 0; i < trex.lazer2.size(); i++) {
                    trex.lazer2.get(i).update();
                    if (trex.lazer2.get(i).checkCollision()) {
                        trex.t2.removeBullet(i);
                    }
                }
                trex.t1.bulletHitsTank(trex.lazer2);
                trex.t2.bulletHitsTank(trex.lazer);
                if ((trex.t1.healthBar <= 0 && trex.t1.heartCount > 0) || (trex.t2.healthBar <= 0 && trex.t2.heartCount > 0)) {
                    trex.lazer.clear();
                    trex.lazer2.clear();
                    trex.t1.respawn();
                    trex.t2.respawn();
                    trex.newWorld = true;
                }
                /*if (trex.t1.hitbox.intersects(trex.t2.hitbox) || trex.t2.hitbox.intersects(trex.t1.hitbox)) { //NOT PERFECT
                    trex.t1.collisionHandle();
                    trex.t2.collisionHandle();
                }
                /*if (trex.t2.hitbox.intersects(trex.t1.hitbox)) { //TANKS GET STUCK TO EACH OTHER IF BOTH MOVE TORWARD EACHOTHER
                    trex.t2.collisionHandle();
                }*/
                trex.repaint();
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }

    }


    private void init() {
        this.jf = new JFrame("Tank Game!");
        this.world = new BufferedImage(GameStart.SCREEN_WIDTH, GameStart.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1img = null, t2img = null, Health1 = null, Health2 = null, Life1 = null, Life2 = null;
        try {
            System.out.println(System.getProperty("user.dir"));
            /*
             * note class loaders read files from the out folder (build folder in netbeans) and not the
             * current working directory.
             */
            File f = new File("Sources/SmallTankBlue.png");
            System.out.println(f.getAbsolutePath());
            t1img = read(f);
            t2img = read(new File("Sources/SmallTankRed.PNG"));
            backGround = read(new File("Sources/Green-Tile.gif"));
            wall = read(new File("Sources/Green-Wall.gif"));
            breakableWall = read(new File("Sources/Green-Wall-Breakable.gif"));
            powerUp = read(new File("Sources/Question-Mark.png"));
            Health1 = read(new File("Sources/SmallBlueHealth.png"));
            Health2 = read(new File("Sources/SmallRedHealth.png"));
            Life1 = read(new File("Sources/SmallBlueHeart.png"));
            Life2 = read(new File("Sources/SmallRedHeart.png"));
            divider = read(new File("Sources/divider.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        BG = new WorldGUI();
        BG.init(backGround, wall, breakableWall, powerUp);
        t1 = new Tank(105, 655, 0, 0, 270, t1img, Health1, Life1, BG);
        t2 = new Tank(1255, 105, 0, 0, 90, t2img, Health2, Life2, BG);


        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);


        this.jf.addKeyListener(tc1);
        this.jf.addKeyListener(tc2);

        this.jf.setSize(GameStart.SCREEN_WIDTH - 400, GameStart.SCREEN_HEIGHT + 30 - 100);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);


    }

    private int XCam(int x) {
        int screen_x = x - 250;

        if(screen_x < 0){
            screen_x = 0;
        }

        if(screen_x > 900){
            screen_x = 900;
        }
        return screen_x;
    }

    private int YCam(int y) {
        int screen_y = y - 250;

        if(screen_y < 0){
            screen_y = 0;
        }

        if(screen_y > 100){
            screen_y = 100;
        }
        return screen_y;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (newWorld) {
            this.BG.reset();
            newWorld = false;
        }
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);
        this.BG.draw(buffer);
        miniMap = world.getScaledInstance(140, 80, 2);
        for (int i = 0; i < this.lazer.size(); i++) {
            this.lazer.get(i).draw(buffer);
        }
        for (int i = 0; i < this.lazer2.size(); i++) {
            this.lazer2.get(i).draw(buffer);
        }
        this.t1.draw(buffer);
        this.t2.draw(buffer);


        BufferedImage lhs = world.getSubimage(XCam(t1.x), YCam(t1.y), 500, 700);
        BufferedImage rhs = world.getSubimage(XCam(t2.x), YCam(t2.y), 500, 700);
        g2.drawImage(lhs, 0, 0, this);
        g2.drawImage(rhs, 500, 0, this);
        g2.drawImage(divider,498, 80, null);

        g2.drawImage(miniMap, 430, 0, null);
        if (t1.gameOver() && t2.gameOver()) {
            if (counter == 0) {
                sfx.outPutAudio("You-Win.wav", 13.0f);
            }
            t1.R = 0;
            t2.R = 0;
            t1.ROTATIONSPEED = 0;
            t2.ROTATIONSPEED = 0;
            g2.setColor(Color.MAGENTA);
            g2.setFont(new Font("AR Delaney", Font.PLAIN, 72));
            if (counter < GameStart.SCREEN_HEIGHT/2) {
                counter+=2;
            }
            g2.drawString("DRAW", 600, counter);
        }
        else if(t1.gameOver() && !t2.gameOver()){
            if (counter == 0) {
                sfx.outPutAudio("You-Win.wav", 13.0f);
            }
            t1.R = 0;
            t1.ROTATIONSPEED = 0;
            t2.R = 1;
            g2.setColor(Color.RED);
            g2.setFont(new Font("AR Destine", Font.PLAIN, 72));
            if (counter < GameStart.SCREEN_HEIGHT/2 - 50) {
                counter+=3;
            }
            g2.drawString("RED WINS!", 350, counter);
        }
        else if(t2.gameOver() && !t1.gameOver()){

            if (counter == 0) {
                sfx.outPutAudio("You-Win.wav", 13.0f);
            }
            t1.R = 1;
            t2.R = 0;
            t2.ROTATIONSPEED = 0;
            g2.setColor(Color.BLUE);
            g2.setFont(new Font("Magneto", Font.PLAIN, 72));
            if (counter < GameStart.SCREEN_HEIGHT/2 - 50) {
                counter+=2;
            }
            g2.drawString("Blue Wins!", 300, counter);
        }

    }


}