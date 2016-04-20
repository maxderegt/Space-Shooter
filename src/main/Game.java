package main;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by maxde on 18-3-2016.
 */
public class Game extends JPanel implements MouseListener,KeyListener,MouseMotionListener{
    private Shop shop;
    private Player player;
    private final Set<Character> pressed = new HashSet<Character>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Shape> buttons = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private BufferedImage expl;
    private BufferedImage background;
    private BufferedImage bloodback;
    private BufferedImage shopi;
    private Image playerimg;
    private ArrayList<Image> images = new ArrayList<>();
    private Graphics2D back;
    private Timer bullet;
    private Timer step;
    private Timer refresh;
    private Mixer mixer;
    private Clip clip;
    private boolean bloodcreated = false;
    private boolean shopb;
    private boolean spawn;
    private boolean game = false;
    private int x = 0;
    private int y = 0;

    public Game(int width, int height){
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        loadResources();
        step = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worldstep();
            }
        });
        refresh = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        bullet = new Timer(1000/4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e2) {
                bullets.add(new Bullet((int)player.getX()+player.getwidth()/2,(int)player.getY()+player.getheigth()/2,x,y));
            }
        });
//      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//      gainControl.setValue(-10.0f);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();



        player = new Player(1000000, 1000000,playerimg);
        shop = new Shop();
        refresh.start();
        step.start();
        spawn = true;
        shopb = true;
    }

    public void loadResources(){

        //Sound device selecting
        Mixer.Info[] mixinfo = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixinfo[0]);
        DataLine.Info datainfo = new DataLine.Info(Clip.class, null);

        try{
            background = ImageIO.read(Game.class.getResourceAsStream("/resources/background.jpg"));
            Image big1 = ImageIO.read(Game.class.getResourceAsStream("/resources/Bigenemy2.png"));
            Image small1 = ImageIO.read(Game.class.getResourceAsStream("/resources/Smallenemy1.png"));
            Image big2 = ImageIO.read(Game.class.getResourceAsStream("/resources/Bigenemy1.png"));
            Image small2 = ImageIO.read(Game.class.getResourceAsStream("/resources/Smallenemy2.png"));
            expl = ImageIO.read(Game.class.getResourceAsStream("/resources/explode_1.png"));
            playerimg = ImageIO.read(Game.class.getResourceAsStream("/resources/player.png"));
            images.add(big1);images.add(small1);images.add(big2);images.add(small2);
            clip = (Clip)(mixer.getLine(datainfo));
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Bullet.class.getResource("/resources/starlight.wav"));
            clip.open(audioInputStream);
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {e.printStackTrace();}


    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        RenderingHints normalAA = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        RenderingHints imageAA = new RenderingHints(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHints(normalAA);
        g2.setRenderingHints(imageAA);
        BufferedImage img = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        if(!bloodcreated) {
            bloodback = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            back = bloodback.createGraphics();
            bloodcreated = true;
        }
        Graphics2D G2 = img.createGraphics();
        G2.setColor(new Color(1,0, 255));
        if(player.getHealth()>0) G2.drawImage(player.getSprite(),player.getTransform(),null);
        for(Bullet bullet : bullets){
            G2.setColor(new Color(255, 105,0));
            G2.fillOval((int)(bullet.getX()-2),(int)(bullet.getY()-2),4,4);
        }
        for(Enemy enemy : enemies){
            G2.drawImage(enemy.getSprite(),enemy.getTransform(),null);
        }
        for(Explosion explosion : explosions){
            G2.drawImage(explosion.getImg(),explosion.getX(),explosion.getY(),null);
        }

        g2.drawImage(background,0,0,null);
        g2.drawImage(img,0,0,null);
        g2.setColor(new Color(0,0,0, 132));
        g2.fillRect(0,0,getWidth(),30);
        g2.setColor(Color.white);
        Font font = new Font("Courier New", Font.BOLD, 12);
        g2.setFont(font);
        g2.drawString("Score : " + player.getScore(),10,20);
        g2.drawString("Level : " + player.getLevel(),getWidth()/2,20);
        g2.drawString("Health : " + (int)Math.round(player.getHealth()) + "/" + player.getMaxhealth(),getWidth()-150,20);
        generateShop();
        if(shopb) {
            g2.drawImage(shopi,0,0,null);
        }
    }

    public void worldstep(){
        if(game) {
            if(player.getHealth()>0) {
                if (pressed.size() > 0) {
                    for (char key : pressed) {
                        if (pressed.contains('w') || pressed.contains('s')) {
                            if (key == 'a') {
                                player.setX(player.getX() - player.getSpeed() * 0.7);
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        } else {
                            if (key == 'a') {
                                player.setX(player.getX() - player.getSpeed());
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        }
                        if (pressed.contains('w') || pressed.contains('s')) {
                            if (key == 'd') {
                                player.setX(player.getX() + player.getSpeed() * 0.7);
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        } else {
                            if (key == 'd') {
                                player.setX(player.getX() + player.getSpeed());
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        }
                        if (pressed.contains('a') || pressed.contains('d')) {
                            if (key == 's') {
                                player.setY(player.getY() + player.getSpeed() * 0.7);
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        } else {
                            if (key == 's') {
                                player.setY(player.getY() + player.getSpeed());
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        }
                        if (pressed.contains('a') || pressed.contains('d')) {
                            if (key == 'w') {
                                player.setY(player.getY() - player.getSpeed() * 0.7);
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        } else {
                            if (key == 'w') {
                                player.setY(player.getY() - player.getSpeed());
                                player.setRect(new Rectangle2D.Double(player.getX(), player.getY(), player.getwidth(), player.getheigth()));
                            }
                        }
                        if (player.getX() <= 5) {
                            player.setX(5);
                        }
                        if (player.getX() >= (getWidth() - 20)) {
                            player.setX(getWidth() - 20);
                        }
                        if (player.getY() <= 5) {
                            player.setY(5);
                        }
                        if (player.getY() >= (getHeight() - 20)) {
                            player.setY(getHeight() - 20);
                        }
                    }
                }

                if (bullets.size() > 0) {
                    for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
                        Bullet bullet = iterator.next();
                        bullet.setX((bullet.getX() + bullet.getXspeed()));
                        bullet.setY((bullet.getY() + bullet.getYspeed()));
                        if (bullet.getX() > getWidth() || bullet.getY() > getHeight() || bullet.getY() < 0 || bullet.getX() < 0) {
                            iterator.remove();
                        }
                    }

                }

                double dx = x - player.getX();
                double dy = y - player.getY();
                double newRichting = Math.atan2(dy, dx);
                player.setRichting(newRichting);
                player.setTransform(new AffineTransform());
                player.getTransform().translate(player.getX() - player.getSprite().getWidth(null) / 2,player.getY() - player.getSprite().getHeight(null) / 2);
                player.getTransform().rotate(player.getRichting(), player.getSprite().getWidth(null) / 2, player.getSprite().getHeight(null) / 2);

                player.setRect2(player.getTransform().createTransformedShape(player.getRect()));

                if (spawn) {
                    player.setLevel(player.getLevel() + 1);
                    int level = player.getLevel();
                    int i = 0;
                    while (i < level * 2) {
                        int richting = (int) (Math.round(Math.random() * 4));
                        switch (richting) {
                            case 1:
                                enemies.add(new Enemy((int) (Math.random() * getWidth()), -1 * (int) (Math.random() * 500),player.getLevel(),images));
                                break;

                            case 2:
                                enemies.add(new Enemy(getWidth() + (int) (Math.random() * 500), (int) (Math.random() * getHeight()),player.getLevel(),images));
                                break;

                            case 3:
                                enemies.add(new Enemy((int) (Math.random() * getWidth()), getHeight() + (int) (Math.random() * 500),player.getLevel(),images));
                                break;

                            case 4:
                                enemies.add(new Enemy(-1 * (int) (Math.random() * 500), (int) (Math.random() * getHeight()),player.getLevel(),images));
                                break;
                        }
                        i++;
                    }
                    spawn = false;
                }
            }
            for(Iterator<Explosion> iterator = explosions.iterator(); iterator.hasNext();){
                Explosion explosion = iterator.next();
                if(explosion.getSpritenum()==explosion.getSprites().size()){
                    iterator.remove();
                }
            }
            if(enemies.size()==0){
                spawn = true;

            }
            if(player.getHealth()<=0){
                shopb = true;
                player.setHealth(0);
                bullet.stop();

            }
            if (enemies.size() > 0) {
                for (Iterator<Enemy> iterator2 = enemies.iterator(); iterator2.hasNext(); ) {
                    Enemy enemy = iterator2.next();
                    boolean removed = false;
                    for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
                        Bullet bullet = iterator.next();
                        if (enemy.getRect2().contains(bullet.getX(), bullet.getY())) {
                            iterator.remove();
                            enemy.setHealth(enemy.getHealth() - bullet.getDamage());
                            if (enemy.getHealth() < 0) enemy.setHealth(0);
                            if (enemy.getHealth() == 0) {
                                if (!removed) {
                                    explosions.add(new Explosion((int)enemy.getRect2().getBounds().getX(),(int)enemy.getRect2().getBounds().getY(),expl));
                                    iterator2.remove();
                                    player.setScore(player.getScore() + enemy.getScore());
                                    removed = true;
                                }
                            }
                        }
                    }
                    if (enemy.getRect2().intersects(player.getRect().getBounds()) && !shopb) {
                        if (!player.isHit()) {
                            player.setHit(true);
                            player.getHit2().start();
                            player.setHealth(player.getHealth() - enemy.getDamage());
                        }
                        enemy.getHit().start();
                        enemy.setMove(false);
                    }
                        double dx = player.getX() - enemy.getX();
                        double dy = player.getY() - enemy.getY();
                        double newRichting = Math.atan2(dy, dx);

                        double richtingsVerschil = newRichting - enemy.getRichting();
                        while (richtingsVerschil > Math.PI)
                            richtingsVerschil -= 2 * Math.PI;
                        while (richtingsVerschil < -Math.PI)
                            richtingsVerschil += 2 * Math.PI;

                        if (richtingsVerschil < 0)
                            enemy.setRichting(enemy.getRichting() - enemy.getTurn());
                        if (richtingsVerschil > 0)
                            enemy.setRichting(enemy.getRichting() + enemy.getTurn());
                        enemy.setX(enemy.getX() + Math.cos(enemy.getRichting()) * enemy.getSpeed());
                        enemy.setY(enemy.getY() + Math.sin(enemy.getRichting()) * enemy.getSpeed());

                        enemy.setTransform(new AffineTransform());
                        enemy.getTransform().translate(
                                enemy.getX() - enemy.getSprite().getWidth(null) / 2,
                                enemy.getY() - enemy.getSprite().getHeight(null) / 2);
                        enemy.getTransform().rotate(enemy.getRichting(), enemy.getSprite().getWidth(null) / 2, enemy.getSprite().getHeight(null) / 2);

                        enemy.setRect2(enemy.getTransform().createTransformedShape(enemy.getRect()));

                }

            }
        }

    }

    public void generateShop(){
        shopi = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = shopi.createGraphics();
        Rectangle2D rect1 = new Rectangle2D.Double(getWidth()/4,getHeight()/4,getWidth()/2,getHeight()/2);
        Rectangle2D rect2 = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/14),getHeight()/4+(rect1.getHeight()/7),rect1.getWidth()-rect1.getWidth()/7,rect1.getHeight()-rect1.getHeight()/3.5);
        Rectangle2D rect3 = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/14),rect2.getY()+(rect2.getHeight()/14)*0 ,(rect2.getWidth()/28)*2+10,rect2.getHeight()/7);
        Rectangle2D rect4 = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/14),rect2.getY()+((rect2.getHeight()/14)*3),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect5 = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/14),rect2.getY()+((rect2.getHeight()/14)*6),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect6 = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/14),rect2.getY()+((rect2.getHeight()/14)*9),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect7 = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/14),rect2.getY()+((rect2.getHeight()/14)*12),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect8 = new Rectangle2D.Double(getWidth()/4+rect1.getWidth()/2,rect2.getY()+(rect2.getHeight()/14)*0 ,(rect2.getWidth()/28)*2+10,rect2.getHeight()/7);
        Rectangle2D rect9 = new Rectangle2D.Double(getWidth()/4+rect1.getWidth()/2,rect2.getY()+((rect2.getHeight()/14)*3),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect10 = new Rectangle2D.Double(getWidth()/4+rect1.getWidth()/2,rect2.getY()+((rect2.getHeight()/14)*6),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect11 = new Rectangle2D.Double(getWidth()/4+rect1.getWidth()/2,rect2.getY()+((rect2.getHeight()/14)*9),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D rect12 = new Rectangle2D.Double(getWidth()/4+rect1.getWidth()/2,rect2.getY()+((rect2.getHeight()/14)*12),(rect2.getWidth()/28)*2+10,(rect2.getHeight()/14)*2);
        Rectangle2D HPbut = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/32)*12,rect2.getY()+(rect2.getHeight()/14)*0,(rect2.getWidth()/28)*3,rect2.getHeight()/7);
        Rectangle2D SPbut = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/32)*12,rect2.getY()+(rect2.getHeight()/14)*3,(rect2.getWidth()/28)*3,rect2.getHeight()/7);
        Rectangle2D SHbut = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/32)*12,rect2.getY()+(rect2.getHeight()/14)*6,(rect2.getWidth()/28)*3,rect2.getHeight()/7);
        Rectangle2D FRbut = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/32)*12,rect2.getY()+(rect2.getHeight()/14)*9,(rect2.getWidth()/28)*3,rect2.getHeight()/7);
        Rectangle2D DMbut = new Rectangle2D.Double(getWidth()/4+(rect1.getWidth()/32)*12,rect2.getY()+(rect2.getHeight()/14)*12,(rect2.getWidth()/28)*3,rect2.getHeight()/7);
        Rectangle2D Cont = new Rectangle2D.Double(getWidth()/4+rect1.getWidth()/32*22,rect2.getY()+rect2.getHeight(),(rect2.getX()+rect2.getWidth())-(getWidth()/4+rect1.getWidth()/32*22),rect1.getHeight()/16*2);

        buttons.add(HPbut);buttons.add(SPbut);/*buttons.add(SHbut);*/buttons.add(FRbut);buttons.add(DMbut);buttons.add(Cont);

        g.setColor(new Color(0, 0, 255, 100));
        g.fill(rect1);
        g.setColor(new Color(103, 51,0,200));
        g.fill(rect2);
        g.setColor(new Color(255,0, 15));
        g.fill(rect3);        g.fill(rect4);        g.fill(rect5);        g.fill(rect6);
        g.fill(rect7);        g.fill(rect8);        g.fill(rect9);        g.fill(rect10);
        g.fill(rect11);       g.fill(rect12);       g.fill(HPbut);        g.fill(SPbut);
        g.fill(SHbut);        g.fill(FRbut);        g.fill(DMbut);        g.fill(Cont);
        g.setColor(Color.black);

        g.drawString("Continue",(int)(getWidth()/4+rect1.getWidth()/32*22),(int)((rect2.getY()+rect2.getHeight())+Cont.getHeight()/2));
        g.drawString("Health",(int)(getWidth()/4+rect1.getWidth()/14),(int)(rect2.getY()+((rect2.getHeight()/14)*0)+HPbut.getHeight()/1.8));
        g.drawString("Speed",(int)(getWidth()/4+rect1.getWidth()/14),(int)(rect2.getY()+((rect2.getHeight()/14)*3)+HPbut.getHeight()/1.8));
        //g.drawString("Shield",(int)(getWidth()/4+rect1.getWidth()/14),(int)(rect2.getY()+((rect2.getHeight()/14)*6)+HPbut.getHeight()/1.8));
        g.drawString("Fire rate",(int)(getWidth()/4+rect1.getWidth()/14),(int)(rect2.getY()+((rect2.getHeight()/14)*9)+HPbut.getHeight()/1.8));
        g.drawString("Damage",(int)(getWidth()/4+rect1.getWidth()/14),(int)(rect2.getY()+((rect2.getHeight()/14)*12)+HPbut.getHeight()/1.8));
        g.drawString(""+shop.getHpcost(),(int)(getWidth()/4+(rect1.getWidth()/32)*12),(int)(rect2.getY()+((rect2.getHeight()/14)*0)+HPbut.getHeight()/1.8));
        g.drawString(""+shop.getSpcost(),(int)(getWidth()/4+(rect1.getWidth()/32)*12),(int)(rect2.getY()+((rect2.getHeight()/14)*3)+SPbut.getHeight()/1.8));
        //g.drawString(""+shop.getShcost(),(int)(getWidth()/4+(rect1.getWidth()/32)*12),(int)(rect2.getY()+((rect2.getHeight()/14)*6)+SHbut.getHeight()/1.8));
        g.drawString(""+shop.getFrcost(),(int)(getWidth()/4+(rect1.getWidth()/32)*12),(int)(rect2.getY()+((rect2.getHeight()/14)*9)+FRbut.getHeight()/1.8));
        g.drawString(""+shop.getDmcost(),(int)(getWidth()/4+(rect1.getWidth()/32)*12),(int)(rect2.getY()+((rect2.getHeight()/14)*12)+DMbut.getHeight()/1.8));

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(player.getHealth()>0 && enemies.size()!=0) {
            bullet.start();

            bullets.add(new Bullet((int)player.getX(), (int)player.getY(), x, y));

        }
        if(player.getHealth()<=0){
            if(buttons.size()!=0){
                int i = 0 ;
                if(buttons.get(i).contains(x,y)){
                    if(player.getScore()>=shop.getHpcost()) {
                        player.setScore((player.getScore()-shop.getHpcost()));
                        player.setMaxhealth((int)Math.round(player.getMaxhealth() * shop.getHpincrement()));
                        shop.setHpcost((int)(shop.getHpcost() * shop.getHpincrement()));
                    }
                }
                i++;
                if(buttons.get(i).contains(x,y)){
                    if(player.getScore()>=shop.getSpcost()) {
                        player.setScore((player.getScore()-shop.getSpcost()));
                        player.setSpeed(player.getSpeed() * shop.getSpincrement());
                        shop.setSpcost((int)(shop.getSpcost() * shop.getSpincrement()));
                    }
                }
                i++;
//                if(buttons.get(i).contains(x,y)){
//                    if(player.getScore()>=shop.getShcost()) {
//                        player.setScore((player.getScore()-shop.getShcost()));
//                        player.setShield(player.getShield() + shop.getShincrement());
//                        shop.setShcost((int)(shop.getShcost() * shop.getShincrement()));
//                    }
//                }
//                i++;
                if(buttons.get(i).contains(x,y)){
                    if(player.getScore()>=shop.getFrcost()) {
                        player.setScore((player.getScore()-shop.getFrcost()));
                        player.setFirerate(player.getFirerate() * shop.getFrincrement());
                        bullet = new Timer((int)Math.round(1000 / player.getFirerate()), new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                bullets.add(new Bullet((int)player.getX()+player.getwidth()/2,(int)player.getY()+player.getheigth()/2,x,y));
                            }
                        });
                        shop.setFrcost((int)(shop.getFrcost() * shop.getFrincrement()));
                    }
                }
                i++;
                if(buttons.get(i).contains(x,y)){
                    if(player.getScore()>=shop.getDmcost()) {
                        player.setScore((player.getScore()-shop.getDmcost()));
                        player.setDamage(player.getDamage() * shop.getDmincrement());
                        shop.setDmcost((int)(shop.getDmcost() * shop.getDmincrement()));
                    }
                }
                i++;
                if(buttons.get(i).contains(x,y)){
                    player.setHealth(player.getMaxhealth());
                    player.setX(getWidth()/2);
                    player.setY(getHeight()/2);
                    player.setRect(new Rectangle2D.Double(player.getX(),player.getY(),player.getwidth(),player.getheigth()));
                    player.setLevel(0);
                    bloodcreated = false;
                    game = true;
                    spawn = true;
                    shopb = false;
                    enemies.clear();
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(player.getHealth()!=0) {
            bullet.stop();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed.add(e.getKeyChar());

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyChar());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
