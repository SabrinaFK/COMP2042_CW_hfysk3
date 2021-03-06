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

import java.awt.*;

/**This class controls the player
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class Player {


    public static final Color BORDER_COLOR = new Color(11, 68, 110);
    public static final Color INNER_COLOR = new Color(52, 141, 197);

    /**
     * defines how much the player moves
     */
    private static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerFace;
    private Point ballPoint;
    private int moveAmount;
    /**
     * contains minimum player coordinates
     */
    private int min;
    /**
     * contains maximum player coordinates
     */
    private int max;


    /** Generates the player
     * @param ballPoint contains ball point on player when position is reset
     * @param width contains player rectangle width
     * @param height contains player rectangle height
     * @param container contains player rectangle
     */
    public Player(Point ballPoint,int width,int height,Rectangle container) {
        this.ballPoint = ballPoint;
        moveAmount = 0;
        playerFace = makeRectangle(width, height);
        min = container.x + (width / 2);
        max = min + container.width - width;

    }

    /** Generates player rectangle based on parameters
     * @param width contains player width
     * @param height contains player height
     * @return returns player rectangle
     */
    private Rectangle makeRectangle(int width,int height){
        Point p = new Point((int)(ballPoint.getX() - (width / 2)),(int)ballPoint.getY());
        return  new Rectangle(p,new Dimension(width,height));
    }

    /** Checks player impact with ball
     * @param b contains ball
     * @return returns true if ball impacts player
     */
    public boolean impact(Ball b){
        return playerFace.contains(b.getPosition()) && playerFace.contains(b.down) ;
    }

    /**
     * controls player movement
     */
    public void move(){
        double x = ballPoint.getX() + moveAmount;
        if(x < min || x > max)
            return;
        ballPoint.setLocation(x,ballPoint.getY());
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
    }

    /**
     * Moves player left
     */
    public void moveLeft(){
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /**
     * Moves player right
     */
    public void moveRight(){
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /**
     * stops player from moving
     */
    public void stop(){
        moveAmount = 0;
    }

    /** gets player shape
     * @return Returns player shape
     */
    public Shape getPlayerFace(){
        return  playerFace;
    }

    /** moves ball and player according to parameter
     * @param p contains a point
     */
    public void moveTo(Point p){
        ballPoint.setLocation(p);
        playerFace.setLocation(ballPoint.x - (int)playerFace.getWidth()/2,ballPoint.y);
    }
}
