import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //storing the segments of the snakes body 
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener { //INHERITING: We are creatubg a new game called snake game that will take on the properties of Jade panel.
    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25; 


    //Snake 
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;

    // Random
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    //Add different game states in here for example public final int titlescreen = 0, and publi
    //c final int playState = 1 and then add if statements to 
    // only "draw" the stuff for the title screen, when a button is pressed we then change the gamestate to one


    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth; 
        this.boardHeight = boardHeight; 
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true); //we want our snake game to listen to our key presses therefore setfocusable to true 

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);

        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(70, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
        g.setColor(Color.white);
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // Vertical lines
        }
        for (int j = 0; j < boardHeight / tileSize; j++) {
            g.drawLine(0, j * tileSize, boardWidth, j * tileSize); // Horizontal lines
        }

        //Food 
        g.setColor(Color.red);
        //g.fillRect(food.x * tileSize, food.y * tileSize , tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize , tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //Snake Body 
        for (int i = 0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize); //adds a body part into where the food is 
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 20, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize); //600 / 25 = 24  0 --> 24 random position 
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        //Snake Head
        if (collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Each tile need to catch up to the one before it before the snakehead can move 
        //Snake Body (iterating backward)
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile previousSnakePart = snakeBody.get(i - 1);
                snakePart.x = previousSnakePart.x;
                snakePart.y = previousSnakePart.y;
            }
        }

        //snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over conditions 
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //collide with the snake head 
            if (collision(snakeHead, snakePart)){
                gameOver = true; 
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight) {
                gameOver = true;
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}



    @Override
    public void keyReleased(KeyEvent e) {}

}

