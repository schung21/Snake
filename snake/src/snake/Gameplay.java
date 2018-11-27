package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;


public class Gameplay extends JPanel implements Runnable, KeyListener {

    private boolean running;
    private Thread thread;
    private Body b;
    private ArrayList<Body> snake;
    private boolean right = true, left = false, up = false, down = false;
    private Apple apple;
    private ArrayList<Apple> apples;
    private Apple2 apple2;
    private ArrayList<Apple2> apple2s;
    private Random r;
    private int score = -1;
    private Trap trap;
    private ArrayList<Trap> traps;
    private enum  STATE {
        MENU,
        GAME,
    }
    private STATE state = STATE.MENU;
    private int xCoor = 20, yCoor = 30, size = 5;
    private int ticks = 0;

    public Gameplay() {

        setFocusable(true);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);

        snake = new ArrayList<Body>();
        apples = new ArrayList<Apple>();
        traps = new ArrayList<Trap>();
        apple2s = new ArrayList<Apple2>();

        r = new Random();

        start();

    }

    public void start() {

        if (state == STATE.GAME) {

            running = true;
            thread = new Thread(this);
            thread.start();

        }
    }

    public void stop() {

        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void tick() {

        if (snake.size() == 0) {
            b = new Body(xCoor, yCoor, 10);
            snake.add(b);
        }

        ticks++;
        if (ticks > 930000) {

            if (right) xCoor++;
            if (left) xCoor--;
            if (up) yCoor--;
            if (down) yCoor++;

            ticks = 0;

            b = new Body(xCoor, yCoor, 10);
            snake.add(b);


            if (snake.size() > size) {
                snake.remove(0);
            }

            for (int x = 0; x < apples.size(); x++) {
                if (xCoor == apples.get(x).getxCoor() && yCoor == apples.get(x).getyCoor()) {

                    size++;
                    apples.remove(x);
                    x++;
                }
            }

            for (int x = 0; x < apple2s.size(); x++) {
                if (xCoor == apple2s.get(x).getxCoor() && yCoor == apple2s.get(x).getyCoor()) {

                    size++;
                    apple2s.remove(x);
                    x++;
                }
            }
        }
    }

    public void paint(Graphics g) {

        g.setColor(Color.WHITE);
        g.drawRect(24, 56, 498, 398);

        {
            g.setColor(Color.black);
            g.fillRect(0, 0, 580, 515);

        }

        if (state == STATE.GAME) {

            g.setColor(Color.white);
            g.setFont((new Font("HOBO STD", Font.PLAIN, 14)));
            g.drawString("'A' to Dash", 238, 450);

            g.setColor(Color.white);
            g.setFont((new Font("HOBO STD", Font.PLAIN, 14)));
            g.drawString("Score : " + score, 450, 34);

            if (apples.size() == 0) {

                {
                    int xCoor = r.nextInt(45);
                    int yCoor = r.nextInt(40);

                    apple = new Apple(xCoor, yCoor, 10);
                    apples.add(apple);
                    score++;
                }

                {
                    int xCoor = r.nextInt(65);
                    int yCoor = r.nextInt(65);
                    trap = new Trap(xCoor, yCoor, 10);
                    traps.add(trap);
                }


            if (score > 30 && apple2s.size() == 0) {
                int xCoor = r.nextInt(45);
                int yCoor = r.nextInt(40);

                apple2 = new Apple2(xCoor, yCoor, 10);
                apple2s.add(apple2);
                score++;

            }

        }

        }

        if (state == STATE.MENU)
        {
            g.setColor(Color.white);
            g.setFont(new Font("HOBO STD", Font.BOLD, 40));
            g.drawString("SNAKE", 205, 210);

            g.setColor(Color.white);
            g.setFont(new Font("Times New Roman", Font.BOLD, 18));
            g.drawString("Press ENTER", 223, 245);

        }

        for (int x = 0; x < WIDTH / 10; x++) {
            g.drawLine(x * 10, 0, x * 10, HEIGHT);
        }

        for (int x = 0; x < HEIGHT / 10; x++) {
            g.drawLine(0, x * 10, HEIGHT, x * 10);
        }

        for (int x = 0; x < snake.size(); x++) {
            snake.get(x).draw(g);
        }
        for(int x = 0 ; x < apples.size(); x++){
            apples.get(x).draw(g);
        }

        for(int x = 0; x < traps.size(); x++){
            traps.get(x).draw(g);
        }
        for(int x = 0 ; x < apple2s.size(); x++) {
            apple2s.get(x).draw(g);
        }
        //trap collision
        for (int x = 0; x <traps.size(); x++) {
            if (xCoor == traps.get(x).getxCoor() && yCoor == traps.get(x).getyCoor()) {

                stop();

                g.setColor(Color.WHITE);
                g.setFont(new Font("Times New Roman", Font.BOLD, 50));
                g.drawString("Game Over", 150, 210);

                g.setFont(new Font("Times New Roman", Font.BOLD, 24));
                g.drawString("Space to RESTART", 175, 250);

                g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
                g.drawString("Your Score: " + score, 230, 280);
            }
        }

        //collision on snake body
        for(int x = 0; x < snake.size() ; x++) {
            if (xCoor == snake.get(x).getxCoor() && yCoor == snake.get(x).getyCoor()) {
                if (x != snake.size() - 1) {

                    stop();

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Times New Roman", Font.BOLD, 50));
                    g.drawString("Game Over", 150, 210);

                    g.setFont(new Font("Times New Roman", Font.BOLD, 24));
                    g.drawString("Space to RESTART", 175, 250);

                    g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
                    g.drawString("Your Score: " + score, 230, 280);
                }
            }
        }
        //collision on border
        if(xCoor < 0 || xCoor > 53 || yCoor < 0 || yCoor > 46) {

            stop();

            g.setColor(Color.WHITE);
            g.setFont(new Font("Times New Roman", Font.BOLD, 50));
            g.drawString("Game Over", 150, 210);

            g.setFont(new Font("Times New Roman", Font.BOLD, 24));
            g.drawString("Space to RESTART", 175, 250);

            g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            g.drawString("Your Score: " + score, 230, 280);
        }

        if (score == 50)
        {

            stop();

            g.setColor(Color.WHITE);
            g.setFont(new Font("Times New Roman", Font.BOLD, 20));
            g.drawString("You beat the game. Nice.", 170, 215);

        }

            }


    public void run() {

        while (running) {
            tick();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void reset()
    {
        snake.clear();
        apples.clear();
        traps.clear();
        apple2s.clear();
        xCoor = 20;
        yCoor = 20;
        size = 5;
        score = -1;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void startgame()
    {
        state = STATE.GAME;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void dash()

    {
        if (right) xCoor+= 3;
        if (left) xCoor-= 3;
        if (up) yCoor-= 3;
        if (down) yCoor+= 3;

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT && !left) {
            right = true;
            up = false;
            down = false;
        }

        if (key == KeyEvent.VK_LEFT && !right) {
            up = false;
            down = false;
            left = true;
        }

        if (key == KeyEvent.VK_UP && !down) {
            right = false;
            up = true;
            left = false;
        }

        if (key == KeyEvent.VK_DOWN && !up) {
            right = false;
            down = true;
            left = false;
        }

        if (key == KeyEvent.VK_SPACE)
        {
            reset();
        }

        if (key == KeyEvent.VK_ENTER)
        {
            startgame();
        }

        if (key == KeyEvent.VK_A)

        {
            dash();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }}