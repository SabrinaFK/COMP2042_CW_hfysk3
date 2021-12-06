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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class HomeMenu extends JComponent implements MouseListener, MouseMotionListener {
    //Start Menu Text
    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.2";

    //Start Menu Button text
    private static final String START_TEXT = "Start";
    private static final String EXIT_TEXT = "Exit";
    private static final String INFO_TEXT = "Information";
    private String MUTE_TEXT = "Mute";

    //Start Menu Colors
    private static final Color BG_COLOR = new Color(181, 229, 255);
    private static final Color BORDER_COLOR = new Color(23, 66, 131);
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);
    private static final Color TEXT_COLOR = new Color(130, 157, 255);
    private static final Color BUTTON_COLOR = new Color(23,23,35,255);
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;

    //Start Menu Button Rectangles
    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle exitButton;
    private Rectangle muteButton;
    private Rectangle infoButton;

    private boolean mute;
    private boolean audioPlaying;

    //Border sizes
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    //Border Strokes
    private BasicStroke borderStroke;
    private BasicStroke borderStroke_noDashes;

    //Fonts
    private Font greetingsFont;                     //welcome to:
    private Font gameTitleFont;                     //Brick Breaker
    private Font creditsFont;                       //version 0.2
    private Font buttonFont;                        //Button text font

    private GameFrame owner;

    private int strLen=0;

    //Mouse Hover indicator
    private boolean startHover;
    private boolean muteHover;
    private boolean tutorialHover;
    private boolean exitHover;

    //loading audio
    //Audio taken from https://sourceforge.net/projects/tlk-brickbreaker/files/Brick%20Breaker/MP3%20Files/
    private AudioPlayer audio = new AudioPlayer("audio/bgm-start.wav");

    public HomeMenu(GameFrame owner,Dimension area){
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);

        //Setting Button Dimensions
        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        startButton = new Rectangle(btnDim);
        exitButton = new Rectangle(btnDim);
        infoButton = new Rectangle(btnDim);
        muteButton = new Rectangle(btnDim);


        borderStroke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStroke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        //Setting Fonts
        greetingsFont = new Font("Noto Mono",Font.PLAIN,25);
        gameTitleFont = new Font("Noto Mono",Font.BOLD,40);
        creditsFont = new Font("Monospaced",Font.PLAIN,10);
        buttonFont = new Font("Monospaced",Font.PLAIN,startButton.height-2);
    }

    public boolean getMute(){return mute;}
    public void setMute(boolean mute){this.mute=mute;}

    public void paint(Graphics g){
        if (!audioPlaying)
        {
            if (mute) {
                audio.stop();
                MUTE_TEXT = "Unmute";
            }
            else {
                audio.play();
                audio.loop();
                MUTE_TEXT = "Mute";
                audioPlaying =true;
            }
        }

        drawMenu((Graphics2D)g);
    }


    public void drawMenu(Graphics2D g2d){
        drawContainer(g2d);
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = menuFace.getX();
        double y = menuFace.getY();

        g2d.translate(x,y);
        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
        */

        //methods calls
        drawText(g2d);
        drawButton(g2d);
        //end of methods calls

        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);

    }

    private void drawContainer(Graphics2D g2d){

        //adding background image
        g2d.fill(menuFace);
        Image picture = Toolkit.getDefaultToolkit().getImage("images/start-bg.jpg");
        g2d.drawImage(picture, 0, 0, this);

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(borderStroke_noDashes);
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(borderStroke);
        g2d.setColor(BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(tmp);
    }

    private void drawText(Graphics2D g2d){
        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = greetingsFont.getStringBounds(GREETINGS,frc);
        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = creditsFont.getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(menuFace.getWidth() - greetingsRect.getWidth()) / 2;
        sY = (int)(menuFace.getHeight() / 4);

        g2d.setFont(greetingsFont);
        g2d.drawString(GREETINGS,sX,sY);

        sX = (int)(menuFace.getWidth() - gameTitleRect.getWidth()) / 2;
        sY += (int) gameTitleRect.getHeight() * 1.1;//add 10% of String height between the two strings

        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE,sX,sY);

        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2;
        sY += (int) creditsRect.getHeight() * 1.1;

        g2d.setFont(creditsFont);
        g2d.drawString(CREDITS,sX,sY);
    }

    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D eTxtRect = buttonFont.getStringBounds(EXIT_TEXT,frc);
        Rectangle2D muTxtRect = buttonFont.getStringBounds(MUTE_TEXT,frc);
        Rectangle2D tTxtRect = buttonFont.getStringBounds(INFO_TEXT,frc);

        g2d.setFont(buttonFont);

        int x = (menuFace.width - startButton.width) / 5;
        int y =(int) ((menuFace.height - startButton.height) * 0.7);

        startButton.setLocation(x,y);

        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);

        if(startHover){
            Color tmp = g2d.getColor();
            g2d.setColor(BUTTON_COLOR);
            g2d.fill(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.draw(startButton);
            g2d.drawString(START_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.setColor(BUTTON_COLOR);
            g2d.fill(startButton);
            g2d.setColor(TEXT_COLOR);
            g2d.draw(startButton);
            g2d.drawString(START_TEXT,x,y);
        }

        x = startButton.x;
        y = startButton.y + 40;

        infoButton.setLocation(x,y);

        x = (int)(infoButton.getWidth() - tTxtRect.getWidth()) / 2;
        y = (int)(infoButton.getHeight() - tTxtRect.getHeight()) / 2;

        x += infoButton.x;
        y += infoButton.y + (startButton.height * 0.9);

        if(tutorialHover){
            Color tmp = g2d.getColor();

            g2d.setColor(BUTTON_COLOR);
            g2d.fill(infoButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.draw(infoButton);
            g2d.drawString(INFO_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.setColor(BUTTON_COLOR);
            g2d.fill(infoButton);
            g2d.setColor(TEXT_COLOR);
            g2d.draw(infoButton);
            g2d.drawString(INFO_TEXT,x,y);
        }

        x = startButton.width + 100;
        y = startButton.y +40;

        exitButton.setLocation(x,y);

        x = (int)(exitButton.getWidth() - eTxtRect.getWidth()) / 2;
        y = (int)(exitButton.getHeight() - eTxtRect.getHeight()) / 2;

        x += exitButton.x;
        y += exitButton.y + (startButton.height * 0.9);

        if(exitHover){
            Color tmp = g2d.getColor();

             g2d.setColor(BUTTON_COLOR);
             g2d.fill(exitButton);
             g2d.setColor(CLICKED_TEXT);
             g2d.draw(exitButton);
             g2d.drawString(EXIT_TEXT,x,y);
             g2d.setColor(tmp);
        }
        else{
            g2d.setColor(BUTTON_COLOR);
            g2d.fill(exitButton);
            g2d.setColor(TEXT_COLOR);
            g2d.draw(exitButton);
            g2d.drawString(EXIT_TEXT,x,y);
        }

        x = startButton.width + 100;
        y = startButton.y;

        muteButton.setLocation(x,y);

        x = (int)(muteButton.getWidth() - muTxtRect.getWidth()) / 2;
        y = (int)(muteButton.getHeight() - muTxtRect.getHeight()) / 2;

        x += muteButton.x;
        y += muteButton.y + (startButton.height * 0.9);

        if(muteHover){
            Color tmp = g2d.getColor();
            g2d.setColor(BUTTON_COLOR);
            g2d.fill(muteButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.draw(muteButton);
            g2d.drawString(MUTE_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.setColor(BUTTON_COLOR);
            g2d.fill(muteButton);
            g2d.setColor(TEXT_COLOR);
            g2d.draw(muteButton);
            g2d.drawString(MUTE_TEXT,x,y);
        }

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
           owner.enableGameBoard();
           audio.stop();
           audioPlaying =false;
        }
        else if(exitButton.contains(p)){
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        }
        else if(infoButton.contains(p)){
            owner.enableTutorial();
            repaint();
        }
        else if(muteButton.contains(p)){
            mute = !mute;
            if (mute) {
                audio.stop();
                MUTE_TEXT = "Unmute";
            }
            else {
                audio.play();
                MUTE_TEXT = "Mute";
            }
            repaint();
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
        if(startButton.contains(p) || exitButton.contains(p)|| infoButton.contains(p)||muteButton.contains(p)) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if(startButton.contains(p)){
                startHover = true;
                repaint();
            }
            else if(exitButton.contains(p)){
                exitHover = true;
                repaint();
            }
            else if(infoButton.contains(p)){
                tutorialHover = true;
                repaint();
            }
            else if(muteButton.contains(p)){
                muteHover = true;
                repaint();
            }
        }
        else {
            this.setCursor(Cursor.getDefaultCursor());
            startHover = false;
            tutorialHover = false;
            exitHover = false;
            muteHover = false;
            repaint();
        }

    }
}
