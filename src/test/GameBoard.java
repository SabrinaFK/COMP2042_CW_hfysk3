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


public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {
    //Text in Pause Menu
    private static final String CONTINUE = "Continue";      //Text on Continue Button
    private static final String RESTART = "Restart";        //Text on Restart Button
    private String MUTE = "Mute";                           //Text on Mute / Unmute Button
    private static final String HOME = "Home";              //Text on Home Button
    private static final String EXIT = "Exit";              //Exit Button
    private static final String PAUSE = "Pause Menu";

    //Format for Pause Menu
    private static final int TEXT_SIZE = 40;
    private static final Color MENU_COLOR = new Color(175, 228, 250);

    //Game frame size
    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private Timer gameTimer;

    private Wall wall;

    private String message1;                //Top Text on the center of level
    private String message2;                //Lower Text on the center of level

    private boolean mute;                   //indicates if audio is muted/not
    private boolean showPauseMenu;          //indicates if pause menu is visible / not
    private boolean lvlAudioPlaying;        //indicates if lvl audio is playing / not

    private Font menuFont;

    //Pause Menu Buttons
    private Rectangle continueButtonRect;
    private Rectangle restartButtonRect;
    private Rectangle homeButtonRect;
    private Rectangle muteButtonRect;
    private Rectangle exitButtonRect;

    private Rectangle scoreBoard;

    private int strLen;

    //Loading Audio
        //Audio taken from https://www.youtube.com/watch?v=j29TCZYJgWA - Game Over by MB Music
        private AudioPlayer gameOver = new AudioPlayer("audio/bgm-game-over.wav");
        //Audio taken from https://www.youtube.com/watch?v=br3OzOrARh4 - 8-bit Win by Heatley Bros
        private AudioPlayer win = new AudioPlayer("audio/bgm-win.wav");
        //Audio taken from https://mixkit.co
        private AudioPlayer sfx = new AudioPlayer("audio/sfx-next-lvl.wav");
        //Audio taken from https://sourceforge.net/projects/tlk-brickbreaker/files/Brick%20Breaker/MP3%20Files/
        private AudioPlayer gameAudio = new AudioPlayer("audio/bgm-lvl.wav");

    private DebugConsole debugConsole;
    public GameBoard(JFrame owner){
        super();
        strLen = 0;
        showPauseMenu = false;

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
                    gameOver.play();
                    gameOver.loop();
                    lvlAudioPlaying=false;
                    message1 = String.format("Game Over");
                    message2 = String.format("Player Score: %d",wall.getPlayerScore());
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

                    gameAudio.stop();
                    win.play();
                    win.loop();

                    message1 = "GREAT JOB!";
                    message2 = "ALL WALLS DESTROYED";
                    gameTimer.stop();
                }
            }

            repaint();
        });

    }

    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void paint(Graphics g){
        if (!lvlAudioPlaying)
        {
            gameOver.stop();
            gameAudio.play();
            gameAudio.loop();
            lvlAudioPlaying=true;
        }
        if (mute){
            gameAudio.stop();
            gameOver.stop();
            win.stop();
            lvlAudioPlaying=false;
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

    private void clear(Graphics2D g2d){
        g2d.fillRect(0,0,getWidth(),getHeight());

        //level background image
        Image picture = Toolkit.getDefaultToolkit().getImage("images/level-bg.jpg");
        g2d.drawImage(picture, 0, 0, this);
    }

    private void drawBrick(Brick brick,Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }

    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = ball.getBallFace();

        g2d.setColor(ball.getInnerColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderColor());
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    private void drawPlayer(Player p,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(Player.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

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
        }
        else if(muteButtonRect.contains(p)){
            mute=!mute;
            gameAudio.muteAudio= !gameAudio.muteAudio;
            gameOver.muteAudio= !gameOver.muteAudio;
            sfx.muteAudio= !sfx.muteAudio;
            win.muteAudio= !win.muteAudio;
            repaint();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }

    }
    public void muteAudio (boolean mute){
        if(mute){
            wall.setMute(true);
            gameAudio.muteAudio= true;
            gameOver.muteAudio= true;
            sfx.muteAudio= true;
            win.muteAudio= true;
        }
        else {
            wall.setMute(false);
            gameAudio.muteAudio= false;
            gameOver.muteAudio= false;
            sfx.muteAudio= false;
            win.muteAudio= false;
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
