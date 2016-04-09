package main;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by maxde on 8-4-2016.
 */
public class Explosion {

    private int spritenum = 0;

    private ArrayList<BufferedImage> sprites = new ArrayList<>();
    private BufferedImage img;
    private Graphics2D g2;
    private Timer boom;
    private int x;
    private int y;
    private Mixer mixer;
    private Clip clip;

    public Explosion(int x2, int y2, BufferedImage bigimg2){
        int rows = 3;
        int cols = 3;
        int width = 64;
        int height = 64;
        x = x2;
        y = y2;
        img = new BufferedImage(64,64,BufferedImage.TYPE_4BYTE_ABGR);
        g2 = img.createGraphics();
        BufferedImage bigImg = null;
        bigImg = bigimg2;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                sprites.add(bigImg.getSubimage(
                        j * width,
                        i * height,
                        width,
                        height
                ));
            }
        }
        Mixer.Info[] mixinfo = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixinfo[0]);
        DataLine.Info datainfo = new DataLine.Info(Clip.class, null);
        try{
            clip = (Clip)(mixer.getLine(datainfo));
        }
        catch (LineUnavailableException lue){lue.printStackTrace();}

        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Explosion.class.getResource("/resources/explosion.wav"));
            clip.open(audioInputStream);
        }
        catch (LineUnavailableException | UnsupportedAudioFileException | IOException lue){lue.printStackTrace();}
        //volume setter
        //FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        //gainControl.setValue(-10.0f);
        boom = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(spritenum<sprites.size()){
                    img = new BufferedImage(64,64,BufferedImage.TYPE_4BYTE_ABGR);
                    g2 = img.createGraphics();
                    g2.drawImage(sprites.get(spritenum),0,0,null);
                    spritenum++;
                }
                else {
                    boom.stop();
                }
            }
        });
        boom.start();
        clip.start();
    }

    public int getSpritenum() {
        return spritenum;
    }

    public void setSpritenum(int spritenum) {
        this.spritenum = spritenum;
    }

    public ArrayList<BufferedImage> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<BufferedImage> sprites) {
        this.sprites = sprites;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public Graphics2D getG2() {
        return g2;
    }

    public void setG2(Graphics2D g2) {
        this.g2 = g2;
    }

    public Timer getBoom() {
        return boom;
    }

    public void setBoom(Timer boom) {
        this.boom = boom;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
