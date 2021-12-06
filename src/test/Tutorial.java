package test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class Tutorial extends JComponent implements MouseListener, MouseMotionListener{
    //Information Menu Button Text
    private static final String TITLE = "Tutorial";
    private static final String BACK_TEXT = "Back";
    private static final String NEXT_TEXT = "Next";

    //Information Menu Colors
    private static final Color BG_COLOR = new Color(181, 229, 255);
    private static final Color BORDER_COLOR = new Color(23, 66, 131);
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);
    private static final Color TEXT_COLOR = new Color(130, 157, 255);
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;

    //Information Menu Button Rectangles
    private Rectangle menuFace;
    private Rectangle backButton;
    private Rectangle nextButton;

    //Border sizes
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    //Border Strokes
    private BasicStroke borderStroke;
    private BasicStroke borderStroke_noDashes;

    //Fonts
    private Font TitleFont;
    private Font buttonFont;

    private GameFrame owner;

    private int strLen=0;

    //Mouse hover indicator
    private boolean backHover;
    private boolean nextHover;


    public Tutorial (GameFrame owner, Dimension area)
    {
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        menuFace = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 5, area.height / 12);
        backButton = new Rectangle(btnDim);
        nextButton = new Rectangle(btnDim);

        borderStroke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStroke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        //Setting fonts
        TitleFont = new Font("Noto Mono",Font.BOLD,40);
        buttonFont = new Font("Monospaced",Font.PLAIN,backButton.height-2);
    }
    public void paint(Graphics g){drawMenu((Graphics2D)g);}

    public void drawMenu(Graphics2D g2d)
    {
        drawContainer(g2d);
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();

        g2d.setFont(TitleFont);
        g2d.setColor(TEXT_COLOR);

        if (strLen == 0) {
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = TitleFont.getStringBounds(TITLE, frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10 +20;

        g2d.drawString(TITLE, x, y);
        drawButton(g2d);

        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    private void drawContainer(Graphics2D g2d){
        //adding background image
        g2d.fill(menuFace);
        Image picture = Toolkit.getDefaultToolkit().getImage("images/tutorial-bg.jpg");
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

    private void drawButton(Graphics2D g2d){
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle2D bTxtRect = buttonFont.getStringBounds(BACK_TEXT,frc);
        Rectangle2D nTxtRect = buttonFont.getStringBounds(NEXT_TEXT,frc);

        g2d.setFont(buttonFont);
        int x = 10;
        int y = (menuFace.height - backButton.height) -10;

        backButton.setLocation(x, y);

        x = (int) (backButton.getWidth() - bTxtRect.getWidth()) / 2;
        y = (int) (backButton.getHeight() - bTxtRect.getHeight()) / 2;

        x += backButton.x + 5;
        y += backButton.y + (backButton.height * 0.9);

        if (backHover) {
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(backButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(BACK_TEXT, x, y);
            g2d.setColor(tmp);
        }
        else {
            g2d.draw(backButton);
            g2d.drawString(BACK_TEXT, x, y);
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
        }
        else {
            g2d.draw(nextButton);
            g2d.drawString(NEXT_TEXT, x, y);
        }
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();

        if (nextButton.contains(p)) {owner.enableLeaderboard(true);}
        else if(backButton.contains(p)){owner.disableTutorial();}
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
        if(backButton.contains(p)||nextButton.contains(p)) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if(backButton.contains(p)){
                backHover = true;
                repaint();
            }
            else if(nextButton.contains(p)){
                nextHover = true;
                repaint();
            }
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
            backHover = false;
            nextHover = false;
            repaint();
        }
    }
}


