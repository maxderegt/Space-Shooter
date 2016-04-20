package main;

import javax.sound.sampled.*;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Created by maxde on 1-4-2016.
 */
public class Bullet {
    private double x;
    private double y;
    private double speed = 6;
    private double xspeed;
    private double yspeed;
    private int damage = 34;
    private Mixer mixer;
    private Clip clip;

    public Bullet(int x, int y, int eindx, int eindy) {
        this.x = x;
        this.y = y;
        boolean xpositief;
        boolean ypositief;
        int totaal;
        int xafstand;
        int yafstand;
        if (x < eindx) {
            xafstand = x - eindx;
            xpositief = true;
        } else {
            xafstand = eindx - x;
            xpositief = false;
        }
        if (y < eindy) {
            yafstand = y - eindy;
            ypositief = true;
        } else {
            yafstand = eindy - y;
            ypositief = false;
        }
        totaal = xafstand + yafstand;
        xspeed = ((double) xafstand / (double) totaal) * speed;
        yspeed = ((double) yafstand / (double) totaal) * speed;
        if (!xpositief) xspeed = xspeed * -1;
        if (!ypositief) yspeed = yspeed * -1;

        Mixer.Info[] mixinfo = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixinfo[0]);
        DataLine.Info datainfo = new DataLine.Info(Clip.class, null);
        try {
            clip = (Clip) (mixer.getLine(datainfo));
        } catch (LineUnavailableException lue) {
            lue.printStackTrace();
        }

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Explosion.class.getResource("/resources/laser.wav"));
            clip.open(audioInputStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException lue) {
            lue.printStackTrace();
        }
        clip.loop(0);
        clip.start();


    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getXspeed() {
        return xspeed;
    }

    public double getYspeed() {
        return yspeed;
    }
}
