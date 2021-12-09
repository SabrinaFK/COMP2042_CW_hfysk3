package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/** This class controls the appearance and movement for the Ball
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */

abstract public class Ball {

    private Shape ballFace;
    /**
     * Contains the shape of the ball
     */
    private Point2D center;

    /**
     * contains the uppermost point of the ball
     */
    Point2D up;
    /**
     * contains the lowermost point of the ball
     */
    Point2D down;
    /**
     * contains the leftmost point of the ball
     */
    Point2D left;
    /**
     * contains the rightmost point of the ball
     */
    Point2D right;

    private Color border;
    private Color inner;

    /**
     * contains the speed of the ball's movement along the x-axis
     */
    private int speedX;
    /**
     * contains the speed of the ball's movement along the y-axis
     */
    private int speedY;

    /** This method controls the appearance and points of the ball in accordance to the parameters
     * @param center contains the center point of the ball
     * @param radiusA contains the horizontal radius points of the ball
     * @param radiusB contains the vertical radius points of the ball
     * @param inner contains the inner color of the ball
     * @param border contains the outline color of the ball
     */
    public Ball(Point2D center,int radiusA,int radiusB,Color inner,Color border){
        this.center = center;

        up = new Point2D.Double();
        down = new Point2D.Double();
        left = new Point2D.Double();
        right = new Point2D.Double();

        up.setLocation(center.getX(),center.getY()-(radiusB / 2));
        down.setLocation(center.getX(),center.getY()+(radiusB / 2));

        left.setLocation(center.getX()-(radiusA /2),center.getY());
        right.setLocation(center.getX()+(radiusA /2),center.getY());


        ballFace = makeBall(center,radiusA,radiusB);
        this.border = border;
        this.inner  = inner;
        speedX = 0;
        speedY = 0;
    }

    /** Draws the ball on the screen
     * @param center contains the center point of the ball
     * @param radiusA contains the horizontal radius points of the ball
     * @param radiusB contains the vertical radius points of the ball
     */
    protected abstract Shape makeBall(Point2D center,int radiusA,int radiusB);

    /** Moves the ball
     * Controls the movement of the ball
     */
    public void move(){
        RectangularShape tmp = (RectangularShape) ballFace;
        center.setLocation((center.getX() + speedX),(center.getY() + speedY));
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h);
        setPoints(w,h);


        ballFace = tmp;
    }

    /** Sets ball speed according to the parameters
     * @param x contains the speed of the ball set by setSpeed along the x-axis
     * @param y contains the speed of the ball set by setSpeed along the y-axis
     */
    public void setSpeed(int x,int y){
        speedX = x;
        speedY = y;
    }

    /** Sets ball speed along x-axis according to the parameter
     * @param s contains the speed of the ball set by setXSpeed along the x-axis
     */
    public void setXSpeed(int s){
        speedX = s;
    }

    /** Sets ball speed along y-axis according to the parameter
     * @param s contains the speed of the ball set by setYSpeed along the y-axis
     */
    public void setYSpeed(int s){
        speedY = s;
    }

    /**
     * Reverses the speed and direction of the ball along the x-axis
     */
    public void reverseX(){
        speedX *= -1;
    }

    /**
     * Reverses the speed and direction of the ball along the y-axis
     */
    public void reverseY(){
        speedY *= -1;
    }

    /** Gets the border color of the ball
     * @return Returns the border color of the ball
     */
    public Color getBorderColor(){
        return border;
    }

    /** Gets the inner color of the ball
     * @return Returns the inner color of the ball
     */
    public Color getInnerColor(){
        return inner;
    }

    /** Gets the coordinates of the ball based on its center point
     * @return Returns the coordinates of the ball based on its center point
     */
    public Point2D getPosition(){
        return center;
    }

    /** Gets the shape of the ball
     * @return Returns the shape of the ball
     */
    public Shape getBallFace(){
        return ballFace;
    }

    /** Moves ball location to the coordinates specified by the parameter
     * @param p contains the coordinate the ball is moved to by moveTo
     */
    public void moveTo(Point p){
        center.setLocation(p);

        RectangularShape tmp = (RectangularShape) ballFace;
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h);
        ballFace = tmp;
    }

    /** moves the uppermost, leftmost, rightmost, and lowermost points of the ball according to the parameters
     * @param width contains the width of the ball
     * @param height contains the height of the ball
     */
    private void setPoints(double width,double height){
        up.setLocation(center.getX(),center.getY()-(height / 2));
        down.setLocation(center.getX(),center.getY()+(height / 2));

        left.setLocation(center.getX()-(width / 2),center.getY());
        right.setLocation(center.getX()+(width / 2),center.getY());
    }

    /** gets the speed of the ball along the x-axis
     * @return Returns the ball along the x-axis
     */
    public int getSpeedX(){
        return speedX;
    }

    /** gets the speed of the ball along the y-axis
     * @return Returns the ball along the y-axis
     */
    public int getSpeedY(){
        return speedY;
    }


}
