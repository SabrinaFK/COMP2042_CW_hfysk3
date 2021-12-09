package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**This class controls the dimensions, and position of the brick
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
abstract public class Brick  {
    /**
     * Defines the crack depth
     */
    public static final int DEF_CRACK_DEPTH = 1;

    /**
     * Defines the number of steps taken when generating a crack
     */
    public static final int DEF_STEPS = 35;


    /**
     * Integer indicator for impact on upper side of the brick
     */
    public static final int UP_IMPACT = 100;
    /**
     * Integer indicator for impact on bottom side of the brick
     */
    public static final int DOWN_IMPACT = 200;
    /**
     * Integer indicator for impact on left side of the brick
     */
    public static final int LEFT_IMPACT = 300;
    /**
     * Integer indicator for impact on right side of the brick
     */
    public static final int RIGHT_IMPACT = 400;

    /**
     * This class generates a crack on the brick
     */
    public class Crack{

        /**
         * Determines the number of sections that shapes the crack
         */
        private static final int CRACK_SECTIONS = 3;
        /**
         * Determines the probability of skipping steps while generating the crack
         */
        private static final double JUMP_PROBABILITY = 0.7;


        /**
         * Integer indicator for the crack to start generating from the left side of the brick
         */
        public static final int LEFT = 10;
        /**
         * Integer indicator for the crack to start generating from the right side of the brick
         */
        public static final int RIGHT = 20;
        /**
         * Integer indicator for the crack to start generating from the top side of the brick
         */
        public static final int UP = 30;
        /**
         * Integer indicator for the crack to start generating from the bottom side of the brick
         */
        public static final int DOWN = 40;
        /**
         * Integer indicator for the crack to be generated vertically
         */
        public static final int VERTICAL = 100;
        /**
         * Integer indicator for the crack to be generated horizontally
         */
        public static final int HORIZONTAL = 200;


        /**
         * Represents the geometric path the crack takes
         */
        private GeneralPath crack;

        /**
         * Contains the value of the craks's depth
         */
        private int crackDepth;
        /**
         * Represents how many steps are taken to generate the crack's path
         */
        private int steps;


        /** Generates a new geometric path for the crack, sets crackDepth and steps in accordance to parameters
         * @param crackDepth Contains the value of the crak's depth
         * @param steps Represents how many steps are taken to generate the crack's path
         */
        public Crack(int crackDepth, int steps){

            crack = new GeneralPath();
            this.crackDepth = crackDepth;
            this.steps = steps;

        }


        /** Returns the geometric path taken by the crack
         * @return Returns the geometric path taken by the crack
         */
        public GeneralPath draw(){

            return crack;
        }

        /**
         * Resets the state of the crack
         */
        public void reset(){
            crack.reset();
        }

        /** Creates a visible crack on the brick based on the parameters
         * @param point Represents the starting point of the crack
         * @param direction Represents the direction the crack is generated
         */
        protected void makeCrack(Point2D point, int direction){
            Rectangle bounds = Brick.this.brickFace.getBounds();

            Point impact = new Point((int)point.getX(),(int)point.getY());
            Point start = new Point();
            Point end = new Point();


            switch(direction){
                case LEFT:
                    start.setLocation(bounds.x + bounds.width, bounds.y);
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                    Point tmp = makeRandomPoint(start,end,VERTICAL);
                    makeCrack(impact,tmp);

                    break;
                case RIGHT:
                    start.setLocation(bounds.getLocation());
                    end.setLocation(bounds.x, bounds.y + bounds.height);
                    tmp = makeRandomPoint(start,end,VERTICAL);
                    makeCrack(impact,tmp);

                    break;
                case UP:
                    start.setLocation(bounds.x, bounds.y + bounds.height);
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                    tmp = makeRandomPoint(start,end,HORIZONTAL);
                    makeCrack(impact,tmp);
                    break;
                case DOWN:
                    start.setLocation(bounds.getLocation());
                    end.setLocation(bounds.x + bounds.width, bounds.y);
                    tmp = makeRandomPoint(start,end,HORIZONTAL);
                    makeCrack(impact,tmp);

                    break;

            }
        }

        /** Creates a visible crack on the brick based on the parameters
         * @param start Represents the starting point of the crack
         * @param end Represents the ending point of the crack
         */
        protected void makeCrack(Point start, Point end){

            GeneralPath path = new GeneralPath();


            path.moveTo(start.x,start.y);

            double w = (end.x - start.x) / (double)steps;
            double h = (end.y - start.y) / (double)steps;

            int bound = crackDepth;
            int jump  = bound * 5;

            double x,y;

            for(int i = 1; i < steps;i++){

                x = (i * w) + start.x;
                y = (i * h) + start.y + randomInBounds(bound);

                if(inMiddle(i,CRACK_SECTIONS,steps))
                    y += jumps(jump,JUMP_PROBABILITY);

                path.lineTo(x,y);

            }

            path.lineTo(end.x,end.y);
            crack.append(path,true);
        }

        /** Generates a random integer based on input bounds
         * @param bound Represents the boundary for randomly generated integer
         * @return Returns the randomly generated integer
         */
        private int randomInBounds(int bound){
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound;
        }

        /** Checks if crack generation has reached the middle point or not
         * @param i contains which step the crack generation is at
         * @param steps contains the number of steps crack has to take
         * @param divisions contains how many segments the crack has to generate
         * @return Returns true if crack generation has reached the center of the brick
         */
        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions);
            int up = low * (divisions - 1);

            return  (i > low) && (i < up);
        }

        /** Generates the number of steps skipped when generating the crack
         * @param bound represents the bounds for random number generation
         * @param probability represents the probability of crack skipping steps
         * @return a random number within the bounds if the randomly generated number is greater than the provided probability
         */
        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability)
                return randomInBounds(bound);
            return  0;

        }

        /** Randomly generates a point for crack generation
         * @param from Represents the starting point of the crack
         * @param to Represents the ending point of the crack
         * @param direction Represents the direction the crack is being generated
         * @return Returns randomly generated point
         */
        private Point makeRandomPoint(Point from,Point to, int direction){

            Point out = new Point();
            int pos;

            switch(direction){
                case HORIZONTAL:
                    pos = rnd.nextInt(to.x - from.x) + from.x;
                    out.setLocation(pos,to.y);
                    break;
                case VERTICAL:
                    pos = rnd.nextInt(to.y - from.y) + from.y;
                    out.setLocation(to.x,pos);
                    break;
            }
            return out;
        }

    }

    /**
     * Represents the randomizer used in this class
     */
    private static Random rnd;

    /**
     * Contains the brick type in string format
     */
    private String name;
    /**
     * Contains the shape of the brick
     */
    Shape brickFace;

    /**
     * Contains the color of the brick's outline
     */
    private Color border;
    /**
     * Contains the inner color of the brick
     */
    private Color inner;

    /**
     * Contains the maximum number of times the brick can be hit before breaking
     */
    private int fullStrength;
    /**
     * Contains the number of times the brick can be hit before breaking
     */
    private int strength;

    /**
     * Indicates if brick is broken or not
     */
    private boolean broken;


    /** Generates a brick
     * @param name contains the name of the brick type
     * @param pos contains the point where the brick will be generated
     * @param size contains the dimensions of the generated brick
     * @param border contains the border color of the brick
     * @param inner contains the inner color of the brick
     * @param strength contains the number of times the brick needs to be hit before braking
     */
    public Brick(String name, Point pos,Dimension size,Color border,Color inner,int strength){
        rnd = new Random();
        broken = false;
        this.name = name;
        brickFace = makeBrickFace(pos,size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;

    }

    /** Generates the shape of the brick
     * @param pos contains the position of the brick
     * @param size contains the dimensions of the brick
     */
    protected abstract Shape makeBrickFace(Point pos,Dimension size);

    /** Detects if there is an impact on the brick and decreases brick strength if brick is not broken
     * @param point contains the point of impact
     * @param dir contains the integer representation of the direction of the impact
     * @return true if brick is not broken
     */
    public  boolean setImpact(Point2D point , int dir){
        if(broken)
            return false;
        impact();
        return  broken;
    }

    /** Gets the bricks shape
     * @return Returns the bricks shape
     */
    public abstract Shape getBrick();


    /** Gets the border color of the brick
     * @return Returns the border color of the brick
     */
    public Color getBorderColor(){
        return  border;
    }

    /** Gets the inner color of the brick
     * @return Returns the inner color of the brick
     */
    public Color getInnerColor(){
        return inner;
    }


    /** Finds the direction of the impact between wall and ball
     * @param b Represents the ball
     * @return Returns the integer value that represents the impacts direction
     */
    public final int findImpact(Ball b){
        if(broken)
            return 0;
        int out  = 0;
        if(brickFace.contains(b.right))
            out = LEFT_IMPACT;
        else if(brickFace.contains(b.left))
            out = RIGHT_IMPACT;
        else if(brickFace.contains(b.up))
            out = DOWN_IMPACT;
        else if(brickFace.contains(b.down))
            out = UP_IMPACT;
        return out;
    }

    /** Checks if the brick is broken
     * @return Returns true if brick is broken
     */
    public final boolean isBroken(){
        return broken;
    }

    /**
     * Restores the brick to it's original condition
     */
    public void repair() {
        broken = false;
        strength = fullStrength;
    }

    /**
     * Reduces brick strength if there is impact and sets broken to true if strength reaches 0
     */
    public void impact(){
        strength--;
        broken = (strength == 0);
    }



}





