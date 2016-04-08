package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * Created by maxde on 1-4-2016.
 */
public class Player {
    private int maxhealth = 100;
    private double health = 0;
    private double shield = 0;
    private double firerate = 4;
    private double damage = 33;
    private double x;
    private double y;
    private double speed = 2.5;
    private int size = 20;
    private double richting;
    private int score = 0;
    private int level = 0;
    private boolean hit = false;
    private Timer hit2;
    private Ellipse2D.Double rect;
    private Shape rect2;
    private Image sprite;
    private AffineTransform transform;


    public Player(int x, int y){
        this.x = x;
        this.y = y;
        rect = new Ellipse2D.Double(x+6,y,size,size);
        rect2 = new Area(rect);
        richting = Math.random() * Math.PI*2;
        transform = new AffineTransform();
//        transform.translate(
//                x - sprite.getWidth(null)/2,
//                y - sprite.getHeight(null)/2);
//        transform.rotate(richting, sprite.getWidth(null)/2, sprite.getHeight(null)/2);
        hit2 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e2) {
                hit = false;
                hit2.stop();
            }
        });
    }

    public int getMaxhealth() {
        return maxhealth;
    }

    public void setMaxhealth(int maxhealth) {
        this.maxhealth = maxhealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getShield() {
        return shield;
    }

    public void setShield(double shield) {
        this.shield = shield;
    }

    public double getFirerate() {
        return firerate;
    }

    public void setFirerate(double firerate) {
        this.firerate = firerate;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getRichting() {
        return richting;
    }

    public void setRichting(double richting) {
        this.richting = richting;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Timer getHit2() {
        return hit2;
    }

    public void setHit2(Timer hit2) {
        this.hit2 = hit2;
    }

    public Ellipse2D.Double getRect() {
        return rect;
    }

    public void setRect(Ellipse2D.Double rect) {
        this.rect = rect;
    }

    public Shape getRect2() {
        return rect2;
    }

    public void setRect2(Shape rect2) {
        this.rect2 = rect2;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
}
