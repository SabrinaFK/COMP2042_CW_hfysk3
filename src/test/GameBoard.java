/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;

/**This class controls the game board
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {
    /**
     * Contains the text on Continue Button in Pause Menu
     */
    private static final String CONTINUE = "Continue";
    /**
     * Contains the text on Restart Button in Pause Menu
     */
    private static final String RESTART = "Restart";
    /**
     * Contains the text on Mute / Unmute Button in Pause Menu
     */
    private String MUTE = "Mute";
    /**
     * Contains the text on Home Button in Pause Menu
     */
    private static final String HOME = "Home";
    /**
     * Contains the text on Exit Button in Pause Menu
     */
    private static final String EXIT = "Exit";
    /**
     * Contains the title text in Pause Menu
     */
    private static final String PAUSE = "Pause Menu";

    /**
     * Contains Text size for Pause Menu
     */
    private static final int TEXT_SIZE = 40;
    /**
     * Contains Color for Pause Menu background
     */
    private static final Color MENU_COLOR = new Color(175, 228, 250);

    /**
     * Contains Game board width
     */
    private static final int DEF_WIDTH = 600;
    /**
     * Contains Game board heigh
     */
    private static final int DEF_HEIGHT = 450;

    private Timer gameTimer;

    private GameFrame owner;
    private Wall wall;

    /**
     * Contains the upper text in the center of the game baord
     */
    private String message1;
    /**
     * Contains the lower text in the center of the game baord
     */
    private String message2;

    /**
     * indicates if audio is muted/not
     */
    private boolean mute;
    /**
     * indicates if pause menu is visible / not
     */
    private boolean showPauseMenu;
    /**
     *indicates if lvl audio is playing / not
     */
    private boolean lvlAudioPlaying;

    private Font menuFont;

    //Pause Menu Buttons
    private Rectangle continueButtonRect;
    private Rectangle restartButtonRect;
    private Rectangle homeButtonRect;
    private Rectangle muteButtonRect;
    private Rectangle exitButtonRect;

    private int strLen;
    private int score;

    //Loading Audio
        //Audio taken from https://mixkit.co
        private AudioPlayer sfx = new AudioPlayer("audio/sfx-next-lvl.wav");
        //Audio taken from https://sourceforge.net/projects/tlk-brickbreaker/files/Brick%20Breaker/MP3%20Files/
        private AudioPlayer gameAudio = new AudioPlayer("audio/bgm-lvl.wav");

    private DebugConsole debugConsole;

    /** Generates the GameBoard
     * @param owner
     */
    public GameBoard(GameFrame owner){
        super();
        strLen = 0;
        showPauseMenu = false;
        this.owner = owner;

        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);

        this.initialize();
        message1 = "";
        message2 = "";
        wall = new Wall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));

        debugConsole = new DebugConsole(owner,wall,this);
        //init the first level
        wall.nextLevel();
        gameTimer = new Timer(10,e ->{
            wall.move();
            wall.findImpacts();
            message1 = String.format("Bricks: %d Balls: %d",wall.getBrickCount(),wall.getBallCount());
            message2 = String.format("Player Score: %d",wall.getPlayerScore());

            if(wall.isBallLost()){
                if(wall.ballEnd()){
                    wall.wallReset();
                    gameAudio.stop();
                    lvlAudioPlaying=false;
                    message1 = "";
                    message2 = "";
                    owner.setScore(wall.getPlayerScore());
                    owner.enableGameOver(false);
                    wall.setPlayerScore(0);
                }

                wall.ballReset();
                gameTimer.stop();
            }
            else if(wall.isDone()){
                if(wall.hasLevel()){
                    message1 = "Going to Next Level";
                    message2 = "";

                    sfx.play();
                    gameTimer.stop();
                    wall.ballReset();
                    wall.wallReset();
                    wall.nextLevel();
                }
                else{
                    wall.ballReset();
                    wall.wallReset();
                    wall.resetLevels();
                    gameAudio.stop();
                    lvlAudioPlaying=false;
                    owner.setScore(wall.getPlayerScore());
                    owner.enableGameOver(true);
                    message1 = "";
                    message2 = "";
                    gameTimer.stop();
                    wall.setPlayerScore(0);
                }
            }

            repaint();
        });

    }

    /**
     * Initializes gameboard
     */
    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /** Draws the gameboard
     * @param g contains the graphics controller
     */
    public void paint(Graphics g){
        if (!lvlAudioPlaying)
        {
            gameAudio.play();
            gameAudio.loop();
            lvlAudioPlaying=true;
        }
        if (mute){
            gameAudio.stop();
            muteAudio(true);
            MUTE = "Unmute";
        }
        else
        {
            muteAudio(false);
            MUTE = "Mute";
        }

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(new Color(143, 200, 243));
        g2d.drawString(message1,255,220);
        g2d.drawString(message2,255,235);

        drawBall(wall.ball,g2d);

        for(Brick b : wall.bricks)
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(wall.player,g2d);

        if(showPauseMenu)
            drawMenu(g2d);

        Toolkit.getDefaultToolkit().sync();
    }

    /** Sets gameboard background
     * @param g2d contains the geometry controller
     */
    private void clear(Graphics2D g2d){
        g2d.fillRect(0,0,getWidth(),getHeight());

        //level background image
        Image picture = Toolkit.getDefaultToolkit().getImage("images/level-bg.jpg");
        g2d.drawImage(picture, 0, 0, this);
    }

    /** Generates the level
     * @param brick generates the brick
     * @param g2d contains the geometry controller
     */
    private void drawBrick(Brick brick,Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }

    /** Generates the ball
     * @param ball contains the ball
     * @param g2d contains the geometry controller
     */
    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = ball.getBallFace();

        g2d.setColor(ball.getInnerColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderColor());
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /** Generates the player
     * @param p contains the player
     * @param g2d contains the geometry controller
     */
    private void drawPlayer(Player p,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(Player.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /** Draws the pause menu
     * @param g2d contains the geometry controller
     */
    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /** Generates the pause menu background
     * @param g2d contains the geometry controller
     */
    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /** generates the pause menu buttons and text
     * @param g2d contains the geometry controller
     */
    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();

        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE,x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }

        g2d.drawString(CONTINUE,x,y);

        y += 60;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        g2d.drawString(RESTART,x,y);

        y += 60;
        if(muteButtonRect == null){
            muteButtonRect = (Rectangle) continueButtonRect.clone();
            muteButtonRect.setLocation(x,y- muteButtonRect.height);
        }

        g2d.drawString(MUTE,x,y);

        y += 60;

        if(homeButtonRect == null){
            homeButtonRect = (Rectangle) continueButtonRect.clone();
            homeButtonRect.setLocation(x,y-homeButtonRect.height);
        }

        g2d.drawString(HOME,x,y);

        y += 60;

        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString(EXIT,x,y);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    /** mutes all audio players in gameboard
     * @param mute indicates if audio is muted or not
     */
    public void muteAudio (boolean mute){
        this.mute = mute;
        if(mute){
            wall.setMute(true);
            gameAudio.muteAudio= true;
            sfx.muteAudio= true;
        }
        else {
            wall.setMute(false);
            gameAudio.muteAudio= false;
            sfx.muteAudio= false;
        }
    }

    /** gets mute status
     * @return returns true if audio is muted
     */
    public boolean getMute (){return mute;}

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int a = keyEvent.getKeyCode();
        switch(a){
            //Move player left
            case KeyEvent.VK_A:
                wall.player.moveLeft();
                break;
            case KeyEvent.VK_LEFT:
                wall.player.moveLeft();
                break;

            //Move player right
            case KeyEvent.VK_D:
                wall.player.moveRight();
                break;
            case KeyEvent.VK_RIGHT:
                wall.player.moveRight();
                break;

            //pause & display menu
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu;
                repaint();
                gameTimer.stop();
                break;

            //Pause game
            case KeyEvent.VK_SPACE:
                if(gameTimer.isRunning())
                    gameTimer.stop();
                else
                    gameTimer.start();
                break;

            //Show Debug Panel
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true);
            default:
                wall.player.stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        wall.player.stop();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;

        if(continueButtonRect.contains(p)){
            showPauseMenu = false;
            repaint();
        }

        else if(restartButtonRect.contains(p)){
            message1 = "Please Wait";
            message2 = "Restarting Game...";
            wall.ballReset();
            wall.setPlayerScore(0);
            wall.wallReset();
            showPauseMenu = false;
            repaint();
        }
        else if (homeButtonRect.contains(p)){
            showPauseMenu = false;
            gameAudio.stop();
            wall.ballReset();
            wall.setPlayerScore(0);
            wall.wallReset();
            wall.resetLevels();
            lvlAudioPlaying=false;
            owner.disableGameBoard();
        }
        else if(muteButtonRect.contains(p)){
            mute=!mute;
            lvlAudioPlaying = !lvlAudioPlaying;
            gameAudio.muteAudio= !gameAudio.muteAudio;
            sfx.muteAudio= !sfx.muteAudio;
            repaint();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p) || muteButtonRect.contains(p)|| homeButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void onLostFocus(){
        gameTimer.stop();
        message1 = "Focus Lost";
        message2 = "";
        repaint();
    }

}
