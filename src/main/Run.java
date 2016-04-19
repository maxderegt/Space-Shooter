package main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by maxde on 18-3-2016.
 */
public class Run extends JFrame {

    private JTabbedPane tabbedPane;
    private Game game;

    public static void main(String[] args){
        new Run();
    }
    public Run(){
        super("Top Down Arena Shooter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setMinimumSize(new Dimension(1280, 720));
        setVisible(true);
    }

    public void initComponents(){
        game = new Game(1280,720);
        setContentPane(game);
    }
}
