package test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class GameOver extends JComponent implements MouseListener, MouseMotionListener {
    //GameOver Screen Text
    private String TITLE = "GameOver";
    private static final String PLAYER_SCORE_TEXT = "Player Score";
    private String PLAYER_SCORE;

    //GameOver Button Text
    private static final String RESTART_TEXT = "Restart";
    private static final String NEXT_TEXT = "Next";

    //GameOver Screen  Colors
    private static final Color BG_COLOR = new Color(181, 229, 255);
    private static final Color BORDER_COLOR = new Color(23, 66, 131);
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);
    private static final Color TEXT_COLOR = new Color(130, 157, 255);
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;

    //GameOver Screen  Button Rectangles
    private Rectangle menuFace;
    private Rectangle restartButton;
    private Rectangle nextButton;


    //Save Score? Screen Text
    private String MESSAGE;

    //Save Score? Button Text
    private static final String YES_TEXT = "Yes";
    private static final String NO_TEXT = "No";

    //Save Score Button Rectangles
    private Rectangle yesButton;
    private Rectangle noButton;

    //Border sizes
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    //Border Strokes
    private BasicStroke borderStroke;
    private BasicStroke borderStroke_noDashes;

    //Fonts
    private Font titleFont;
    private Font playerScoreFont;
    private Font playerScoreTextFont;
    private Font messageFont;
    private Font buttonFont;

    private GameFrame owner;

    private int strLen=0;

    private boolean showSaveScoreScreen;

    //Mouse hover indicator
    private boolean restartHover;
    private boolean nextHover;
    private boolean yesHover;
    private boolean noHover;

    public GameOver (GameFrame owner, Dimension area)
    {
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 4, area.height / 12);
        restartButton = new Rectangle(btnDim);
        nextButton = new Rectangle(btnDim);
        yesButton = new Rectangle(btnDim);
        noButton = new Rectangle(btnDim);

        borderStroke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStroke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        //Setting fonts
        titleFont = new Font("Noto Mono",Font.BOLD,40);
        buttonFont = new Font("Monospaced",Font.PLAIN, restartButton.height-2);
        playerScoreFont = new Font("Noto Mono",Font.BOLD,60);
        playerScoreTextFont = new Font("Noto Mono",Font.PLAIN,30);
        messageFont = new Font("Monospaced",Font.PLAIN, 30);
    }

    public void paint(Graphics g){drawMenu((Graphics2D)g);}

    public void drawMenu(Graphics2D g2d)
    {
        PLAYER_SCORE = String.format("%d", owner.getScore());
        drawContainer(g2d);
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();

        g2d.setFont(titleFont);
        g2d.setColor(TEXT_COLOR);

        if (strLen == 0) {
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = titleFont.getStringBounds(TITLE, frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10 +20;

        g2d.drawString(TITLE, x, y);
        drawButton(g2d);
        drawText(g2d);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    private void drawContainer(Graphics2D g2d){
        //adding background image
        g2d.fill(menuFace);
        Image picture = Toolkit.getDefaultToolkit().getImage("images/score-bg.jpg");
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

        if (!showSaveScoreScreen)
        {
            Rectangle2D playerScoreTextRect = playerScoreTextFont.getStringBounds(PLAYER_SCORE_TEXT, frc);
            Rectangle2D playerScoreRect = playerScoreFont.getStringBounds(PLAYER_SCORE, frc);

            int sX, sY;

            sX = (int) (menuFace.getWidth() - playerScoreTextRect.getWidth()) / 2;
            sY = (int) (menuFace.getHeight() / 3);

            g2d.setFont(playerScoreTextFont);
            g2d.drawString(PLAYER_SCORE_TEXT, sX, sY);

            sX = (int) (menuFace.getWidth() - playerScoreRect.getWidth()) / 2;
            sY += (int) playerScoreRect.getHeight() + 20;

            g2d.setFont(playerScoreFont);
            g2d.drawString(PLAYER_SCORE, sX, sY);
        }
        else
        {
            Rectangle2D messageRect = messageFont.getStringBounds(MESSAGE, frc);

            int sX, sY;

            sX = (int) (menuFace.getWidth() - messageRect.getWidth()) / 2;
            sY = (int) (menuFace.getHeight() / 3);

            g2d.setFont(messageFont);
            g2d.drawString(MESSAGE, sX, sY);
        }

    }

    private void drawButton(Graphics2D g2d){
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle2D bTxtRect = buttonFont.getStringBounds(RESTART_TEXT,frc);
        Rectangle2D nTxtRect = buttonFont.getStringBounds(NEXT_TEXT,frc);
        Rectangle2D yTxtRect = buttonFont.getStringBounds(YES_TEXT,frc);
        Rectangle2D noTxtRect = buttonFont.getStringBounds(NO_TEXT,frc);

        g2d.setFont(buttonFont);

        if (!showSaveScoreScreen)
        {
            int x = 10;
            int y = (menuFace.height - restartButton.height) - 10;

            restartButton.setLocation(x, y);

            x = (int) (restartButton.getWidth() - bTxtRect.getWidth()) / 2;
            y = (int) (restartButton.getHeight() - bTxtRect.getHeight()) / 2;

            x += restartButton.x + 2;
            y += restartButton.y + (restartButton.height * 0.9);

            if (restartHover) {
                Color tmp = g2d.getColor();
                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(restartButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(RESTART_TEXT, x, y);
                g2d.setColor(tmp);
            } else {
                g2d.draw(restartButton);
                g2d.drawString(RESTART_TEXT, x, y);
            }

            x = (menuFace.width - nextButton.width) - 10;
            y = (menuFace.height - nextButton.height) - 10;

            nextButton.setLocation(x, y);

            x = (int) (nextButton.getWidth() - nTxtRect.getWidth()) / 2;
            y = (int) (nextButton.getHeight() - nTxtRect.getHeight()) / 2;

            x += nextButton.x + 5;
            y += nextButton.y + (nextButton.height * 0.9);

            if (nextHover) {
                Color tmp = g2d.getColor();
                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(nextButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(NEXT_TEXT, x, y);
                g2d.setColor(tmp);
            } else {
                g2d.draw(nextButton);
                g2d.drawString(NEXT_TEXT, x, y);
            }
        }
        else
        {
            int x = (menuFace.width - yesButton.width)/4;
            int y = (menuFace.height - yesButton.height)/2;

            yesButton.setLocation(x, y);

            x = (int) (yesButton.width - yTxtRect.getWidth()) / 2;
            y = (int) (yesButton.height - yTxtRect.getHeight()) / 2;

            x += yesButton.x + 2;
            y += yesButton.y + (restartButton.height * 0.9);

            if (yesHover) {
                Color tmp = g2d.getColor();
                g2d.setColor(CLICKED_BUTTON_COLOR);
                g2d.draw(yesButton);
                g2d.setColor(CLICKED_TEXT);
                g2d.drawString(YES_TEXT, x, y);
                g2d.setColor(tmp);
            } else {
                g2d.draw(yesButton);
                g2d.drawString(YES_TEXT, x, y);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();

        if (nextButton.contains(p)) {
            if(showSaveScoreScreen) {
                owner.enableLeaderboard(false);
            }
            else {
                MESSAGE = "Would You Like to Save Your Score?";
                showSaveScoreScreen = true;
            }
            repaint();
        }
        else if(restartButton.contains(p)){
            owner.disableGameOver();
            owner.enableGameBoard();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(restartButton.contains(p)||nextButton.contains(p)||yesButton.contains(p)||noButton.contains(p)) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if(restartButton.contains(p)){
                restartHover = true;
                repaint();
            }
            else if(nextButton.contains(p)){
                nextHover = true;
                repaint();
            }
            else if(yesButton.contains(p)){
                yesHover = true;
                repaint();
            }
            else if(noButton.contains(p)){
                noHover = true;
                repaint();
            }
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
            restartHover = false;
            nextHover = false;
            repaint();
        }
    }
}
