package main;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by maxde on 2-4-2016.
 */
public class Enemy {

    private double health = 100;
    private int damage = 10;
    private double x;
    private double y;
    private int size = 30;
    private double speed = 2;
    private int score;
    private double xspeed;
    private double yspeed;
    private double richting;
    private double turn;
    private boolean move = true;
    private Timer hit;
    private Image sprite;
    private Shape rect;
    private Shape rect2;
    private AffineTransform transform;


    public Enemy(int x, int y, int level, ArrayList<Image> images){
        health = health + (health/10 * level);
        int enemy = (int)(Math.round(Math.random()*3));
        if(enemy==0){
            sprite = images.get(0);
            health = health*2;
            damage = damage - damage/4;
            speed = speed - speed/3;
            turn = 0.05;
            score = 3;
        }
        else if (enemy ==1){
            sprite = images.get(1);
            health = health + health/4;
            damage = damage - damage/2;
            speed = speed - speed/4;
            turn = 0.05;
            score = 2;
        }
        else if (enemy == 2){
            sprite = images.get(2);
            health = health - health/4;
            damage = damage + damage/2;
            speed = speed + speed/2;
            turn = 0.015;
            score = 2;
        }
        else if(enemy == 3){
            sprite = images.get(3);
            health = health - health/2;
            damage = damage + damage/4;
            speed = speed + speed/2;
            turn = 0.02;
            score = 1;
        }
        rect = new Rectangle2D.Double(0,0,sprite.getWidth(null),sprite.getHeight(null));
        rect2 = rect;
        this.x = x+sprite.getWidth(null)/2;
        this.y = y+sprite.getHeight(null)/2;
        richting = Math.random() * Math.PI*2;
        transform = new AffineTransform();
        transform.translate(x - sprite.getWidth(null)/2,y - sprite.getHeight(null)/2);
        transform.rotate(richting, sprite.getWidth(null)/2, sprite.getHeight(null)/2);
        hit = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e2) {
                    move = true;
                hit.stop();
            }
        });

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getTurn() {
        return turn;
    }

    public void setTurn(double turn) {
        this.turn = turn;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Timer getHit() {
        return hit;
    }

    public void setHit(Timer hit) {
        this.hit = hit;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public Shape getRect2() {
        return rect2;
    }

    public void setRect2(Shape rect2) {
        this.rect2 = rect2;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public Shape getRect() {
        return rect;
    }

    public void setRect(Shape rect) {
        this.rect = rect;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getXspeed() {
        return xspeed;
    }

    public void setXspeed(double xspeed) {
        this.xspeed = xspeed;
    }

    public double getYspeed() {
        return yspeed;
    }

    public void setYspeed(double yspeed) {
        this.yspeed = yspeed;
    }

    public double getRichting() {
        return richting;
    }

    public void setRichting(double richting) {
        this.richting = richting;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }
}
