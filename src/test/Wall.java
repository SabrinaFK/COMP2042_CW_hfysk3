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
import java.awt.geom.Point2D;
import java.util.Random;


/** This class is used to control the level
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class Wall {
    //Loading Audio
        //Audio taken from https://freesound.org/people/jalastram/packs/21727/
        private AudioPlayer sfx1 = new AudioPlayer("audio/sfx-bounce.wav");

        //Audio taken from https://mixkit.co
        private AudioPlayer sfx2 = new AudioPlayer("audio/sfx-hit-brick.wav");
        private AudioPlayer sfx3 = new AudioPlayer("audio/sfx-ball-lost.wav");

    /**
     * Indicates how many levels exist
     */
    private static final int LEVELS_COUNT = 6;

    /**
     * Integer indicator for clay brick
     */
    private static final int CLAY = 1;
    /**
     * Integer indicator for steel brick
     */
    private static final int STEEL = 2;
    /**
     * Integer indicator for cement brick
     */
    private static final int CEMENT = 3;

    private Random rnd;
    private Rectangle area;

    Brick[] bricks;
    Ball ball;
    Player player;

    private Brick[][] levels;
    /**
     * Indicates which level is currently displayed
     */
    private int level;

    private Point startPoint;
    /**
     * Indicates how many bricks are visible on the screen
     */
    private int brickCount;
    /**
     * Indicates how many balls the player has left
     */
    private int ballCount;
    /**
     * Indicates the player's score
     */
    private int playerScore;
    /**
     * Indicates if the ball has gone off screen or not
     */
    private boolean ballLost;

    /**
     * Indicates if audio is muted or not
     */
    private boolean mute;

    /** Generates the level boundaries
     * @param drawArea contains the play area rectangle
     * @param brickCount contains the level brick count
     * @param lineCount contains number of brick lines in a level
     * @param brickDimensionRatio contains the brick dimensions
     * @param ballPos contains the balls coordinates
     */
    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){
        this.startPoint = new Point(ballPos);

        levels = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio);
        level = 0;

        ballCount = 3;
        ballLost = false;

        rnd = new Random();

        makeBall(ballPos);
        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);

        player = new Player((Point) ballPos.clone(),150,10, drawArea);

        area = drawArea;


    }

    /** Generates a level with only one type of brick
     * @param drawArea contains the level rectangle
     * @param brickCnt contains the brcik coount for the level
     * @param lineCnt contains the number of lines of bricks
     * @param brickSizeRatio contains the brick size ratio in comparison to the window
     * @param type contains brick type integer indicator
     * @return Returns brick[]
     */
    private Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */

        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,type);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,type);
        }
        return tmp;

    }

    /** Generates a level with two types of bricks
     * @param drawArea contains the level rectangle
     * @param brickCnt contains the brcik coount for the level
     * @param lineCnt contains the number of lines of bricks
     * @param brickSizeRatio contains the brick size ratio in comparison to the window
     * @param typeA contains 1st brick type integer indicator
     * @param typeB contains 2nd brick type integer indicator
     * @return Returns Brick[]
     */
    private Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        int centerLeft = brickOnLine / 2 - 1;
        int centerRight = brickOnLine / 2 + 1;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            int posX = i % brickOnLine;
            double x = posX * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ?  makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }
        return tmp;
    }

    /** Generates a ball
     * @param ballPos contains ball position coordinates
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos);
    }

    /** Decides what type of level is generated based on level
     * @param drawArea contains game window rectangle boundaries
     * @param brickCount contrains number of brick on a level
     * @param lineCount contains number of lines of bricks on a level
     * @param brickDimensionRatio contains brick dimensions
     * @return
     */
    private Brick[][] makeLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){
        Brick[][] tmp = new Brick[LEVELS_COUNT][];
        tmp[0] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CEMENT);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[4] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CEMENT,STEEL);
        tmp[5] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);

        return tmp;
    }

    /**
     * Moves player and ball
     */
    public void move(){
        player.move();
        ball.move();
    }

    /**
     * checks for impactes between ball and wall, ball and brick, and ball and boundaries. Deducts the score if ball hits boundaries
     */
    public void findImpacts(){
        if(player.impact(ball)){
            ball.reverseY();
            if (mute==false)
                sfx1.play();
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
            * because for every brick program checks for horizontal and vertical impacts
            */
            brickCount--;
        }
        else if(impactBorder()) {
            ball.reverseX();
            if (mute==false)
                sfx1.play();
        }
        else if(ball.getPosition().getY() < area.getY()){
            ball.reverseY();
            if (mute==false)
                sfx1.play();
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight()){
            ballCount--;
            playerScore-=100;
            ballLost = true;
            if (mute==false)
                sfx3.play();
        }
    }

    /** Checks for impacts between ball and bricks
     * @return Returns true if ball hits a brick in any direction
     */
    private boolean impactWall(){
        for(Brick b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY();
                    playerScore+=100;
                    if (mute==false)
                        sfx2.play();
                    return b.setImpact(ball.down, Brick.Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY();
                    playerScore+=100;
                    if (mute==false)
                        sfx2.play();
                    return b.setImpact(ball.up,Brick.Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    playerScore+=100;
                    if (mute==false)
                        sfx2.play();
                    return b.setImpact(ball.right,Brick.Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    playerScore+=100;
                    if (mute==false)
                        sfx2.play();
                    return b.setImpact(ball.left,Brick.Crack.LEFT);
            }
        }
        return false;
    }

    /** Checks if ball impacts the borders of the level
     * @return true if ball position is less than window x-coordinate or greater than window width
     */
    private boolean impactBorder(){
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    /** gets brickcount
     * @return brickCount
     */
    public int getBrickCount(){
        return brickCount;
    }

    /** gets ballCount
     * @return ballCount
     */
    public int getBallCount(){
        return ballCount;
    }

    /** gets playerScore
     * @return playerScore
     */
    public int getPlayerScore(){return playerScore;}

    /** sets playerScore to parameter value
     * @param num contains new playerScore
     */
    public void setPlayerScore(int num){playerScore=num;}

    /** sets mute to parameter value
     * @param mute contains new mute boolean
     */
    public void setMute(boolean mute){this.mute=mute;}

    /** Checks if ball is lost
     * @return if ball is lost then returns true
     */
    public boolean isBallLost(){
        return ballLost;
    }

    /**
     * Resets ball position
     */
    public void ballReset(){
        player.moveTo(startPoint);
        ball.moveTo(startPoint);

        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);
        ballLost = false;
    }

    /**
     * Resets level
     */
    public void wallReset(){
        for(Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    /** checks if player has run out of balls
     * @return if ballcount is 0 then return true
     */
    public boolean ballEnd(){return ballCount == 0;}

    /** checks if player has destroyed all the bricks
     * @return returns true if brickCount is 0
     */
    public boolean isDone(){
        return brickCount == 0;
    }

    /**
     * Resets the level
     */
    public void resetLevels (){
        bricks = levels[0];
        level=0;
        this.brickCount = bricks.length;
    }

    /**
     * Resets and starts on the next level
     */
    public void nextLevel(){
        bricks = levels[level++];
        this.brickCount = bricks.length;
    }

    /** checks if there are more levels to play through
     * @return returns false if level goes beyond 6
     */
    public boolean hasLevel(){
        return level < levels.length;
    }

    /** sets new ball speed along x-axis according to parameter
     * @param s contains speed value
     */
    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

    /** sets new ball speed along y-axis according to parameter
     * @param s contains speed value
     */
    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

    /**
     * Resets player ballcount
     */
    public void resetBallCount(){ballCount = 3;}


    /** Generates brick according to parameters
     * @param point contains brick point location
     * @param size contains brick dimensions
     * @param type contains brick type integer indicator
     * @return returns brick
     */
    private Brick makeBrick(Point point, Dimension size, int type){
        Brick out;
        switch(type){
            case CLAY:
                out = new ClayBrick(point,size);
                break;
            case STEEL:
                out = new SteelBrick(point,size);
                break;
            case CEMENT:
                out = new CementBrick(point, size);
                break;
            default:
                throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
        return  out;
    }

}
