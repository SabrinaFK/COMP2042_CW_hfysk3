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
    //Load Audio
    private JukeBox JukeBox = new JukeBox();
    JukeBox.loadAudio("audio/bgm-start.mp3" "Music1");

    //Load Music
    SoundPlayer.load("/audio/bgm-start.mp3","music1");

    //Start Menu Text
    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.2";

    //Start Menu Button text
    private static final String START_TEXT = "Start";
    private static final String EXIT_TEXT = "Exit";
    private static final String TUTORIAL_TEXT = "Tutorial";

    //Start Menu Colors
    private static final Color BG_COLOR = new Color(181, 229, 255);
    private static final Color BORDER_COLOR = new Color(23, 66, 131);
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);
    private static final Color TEXT_COLOR = new Color(130, 157, 255);
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;

    //Start Menu Button Rectangles
    private Rectangle menuFace;
    private Rectangle startButton;
    private Rectangle exitButton;
    private Rectangle tutorialButton;

    //Tutorial Menu Button Text
    private static final String BACK = "Back";

    //Tutorial Menu Button Rectangles
    private Rectangle backButton;

    private boolean showTutorial;

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

    //Click confirmation
    private boolean backClicked;
    private boolean startClicked;
    private boolean exitClicked;
    private boolean tutorialClicked;

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
        tutorialButton = new Rectangle(btnDim);
        backButton = new Rectangle(btnDim);

        borderStroke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStroke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        //Setting Fonts
        greetingsFont = new Font("Noto Mono",Font.PLAIN,25);
        gameTitleFont = new Font("Noto Mono",Font.BOLD,40);
        creditsFont = new Font("Monospaced",Font.PLAIN,10);
        buttonFont = new Font("Monospaced",Font.PLAIN,startButton.height-2);
    }


    public void paint(Graphics g){
        drawMenu((Graphics2D)g);
    }


    public void drawMenu(Graphics2D g2d){
        if (showTutorial) {
            drawContainer(g2d);
            Font tmpFont = g2d.getFont();
            Color tmpColor = g2d.getColor();

            g2d.setFont(gameTitleFont);
            g2d.setColor(TEXT_COLOR);

            if (strLen == 0) {
                FontRenderContext frc = g2d.getFontRenderContext();
                strLen = gameTitleFont.getStringBounds(TUTORIAL_TEXT, frc).getBounds().width;
            }

            int x = (this.getWidth() - strLen) / 2;
            int y = this.getHeight() / 10 +20;

            g2d.drawString(TUTORIAL_TEXT, x, y);
            drawButton(g2d);

            g2d.setFont(tmpFont);
            g2d.setColor(tmpColor);
        }
        else
        {
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
    }

    private void drawContainer(Graphics2D g2d){

        if(showTutorial)
        {
            //adding background image
            g2d.fill(menuFace);
            Image picture = Toolkit.getDefaultToolkit().getImage("images/tutorial-bg.jpg");
            g2d.drawImage(picture, 0, 0, this);
        }
        else
        {
            //adding background image
            g2d.fill(menuFace);
            Image picture = Toolkit.getDefaultToolkit().getImage("images/start-bg.jpg");
            g2d.drawImage(picture, 0, 0, this);
        }
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
        Rectangle2D mTxtRect = buttonFont.getStringBounds(EXIT_TEXT,frc);
        Rectangle2D tTxtRect = buttonFont.getStringBounds(TUTORIAL_TEXT,frc);
        Rectangle2D bTxtRect = buttonFont.getStringBounds(BACK,frc);

        g2d.setFont(buttonFont);

        if(showTutorial) {
            int x = (menuFace.width - backButton.width) - 10;
            int y = (menuFace.height - backButton.height) -10;

            backButton.setLocation(x, y);

            x = (int) (backButton.getWidth() - txtRect.getWidth()) / 2;
            y = (int) (backButton.getHeight() - txtRect.getHeight()) / 2;

            x += backButton.x;
            y += backButton.y + (backButton.height * 0.9);

            if (backClicked) {
                Color tmp = g2d.getColor();
                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(backButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(BACK, x, y);
                g2d.setColor(tmp);
            } else {
                g2d.draw(backButton);
                g2d.drawString(BACK, x, y);
            }
        }
        else
        {
            int x = (menuFace.width - startButton.width) / 2;
            int y =(int) ((menuFace.height - startButton.height) * 0.6);

            startButton.setLocation(x,y);

            x = (int)(startButton.getWidth() - bTxtRect.getWidth()) / 2;
            y = (int)(startButton.getHeight() - bTxtRect.getHeight()) / 2;

            x += startButton.x;
            y += startButton.y + (startButton.height * 0.9);

            if(startClicked){
                Color tmp = g2d.getColor();
                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(startButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(START_TEXT,x,y);
                g2d.setColor(tmp);
            }
            else{
                g2d.draw(startButton);
                g2d.drawString(START_TEXT,x,y);
            }

            x = startButton.x;
            y = startButton.y + 40;

            tutorialButton.setLocation(x,y);

            x = (int)(tutorialButton.getWidth() - tTxtRect.getWidth()) / 2;
            y = (int)(tutorialButton.getHeight() - tTxtRect.getHeight()) / 2;

            x += tutorialButton.x;
            y += tutorialButton.y + (startButton.height * 0.9);

            if(tutorialClicked){
                Color tmp = g2d.getColor();

                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(tutorialButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(TUTORIAL_TEXT,x,y);
                g2d.setColor(tmp);

                showTutorial= !showTutorial;
            }
            else{
                g2d.draw(tutorialButton);
                g2d.drawString(TUTORIAL_TEXT,x,y);
            }

            x = tutorialButton.x;
            y = tutorialButton.y + 40;

            exitButton.setLocation(x,y);

            x = (int)(exitButton.getWidth() - mTxtRect.getWidth()) / 2;
            y = (int)(exitButton.getHeight() - mTxtRect.getHeight()) / 2;

            x += exitButton.x;
            y += exitButton.y + (startButton.height * 0.9);

            if(exitClicked){
                Color tmp = g2d.getColor();

                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(exitButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(EXIT_TEXT,x,y);
                g2d.setColor(tmp);
            }
            else{
                g2d.draw(exitButton);
                g2d.drawString(EXIT_TEXT,x,y);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
           owner.enableGameBoard();
        }
        else if(exitButton.contains(p)){
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        }
        else if(tutorialButton.contains(p)){
            tutorialClicked = true;
            showTutorial = true;
            repaint();
        }
        else if(backButton.contains(p)){
            backClicked = true;
            showTutorial = false;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            startClicked = true;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);

        }
        else if(exitButton.contains(p)){
            exitClicked = true;
            repaint(exitButton.x, exitButton.y, exitButton.width+1, exitButton.height+1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(startClicked ){
            startClicked = false;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
        }
        else if(exitClicked){
            exitClicked = false;
            repaint(exitButton.x, exitButton.y, exitButton.width+1, exitButton.height+1);
        }
        else if(tutorialClicked){
            tutorialClicked = false;
            repaint(tutorialButton.x, tutorialButton.y, tutorialButton.width+1, tutorialButton.height+1);
        }
        else if(backClicked){
            backClicked = false;
            repaint(backButton.x, backButton.y, backButton.width+1, backButton.height+1);
        }
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
        if(startButton.contains(p) || exitButton.contains(p)|| tutorialButton.contains(p)||backButton.contains(p))
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());

    }
}
