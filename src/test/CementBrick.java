package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


/** This class controls the dimensions, and appearance of the Cement Brick
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class CementBrick extends Brick {


    /**
     * Contains the name of the cement Brick type
     */
    private static final String NAME = "Cement Brick";
    /**
     * Defines the inner color of the cement brick
     */
    private static final Color DEF_INNER = new Color(147, 147, 147);
    /**
     * Defines the outer color of the cement brick
     */
    private static final Color DEF_BORDER = new Color(87, 87, 87);
    /**
     * Defines the strength of the cement brick
     */
    private static final int CEMENT_STRENGTH = 2;

    private Crack crack;

    /**
     * Represents the shape of the cement brick
     */
    private Shape brickFace;


    /** Generates cement brick based on the parameters
     * @param point contains the point where cement brick is going to be generated
     * @param size contains the dimensions of the cement brick to be generated
     */
    public CementBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH);
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS);
        brickFace = super.brickFace;
    }

    /** Generates cement brick based on the parameters
     * @param pos  contains the position of the brick
     * @param size contains the dimensions of the brick
     * @return new cement brick
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /** Detects the point of impact and generates a crack
     * @param point contains the point of impact
     * @param dir   contains the integer representation of the direction of the impact
     * @return
     */
    @Override
    public boolean setImpact(Point2D point, int dir) {
        if(super.isBroken())
            return false;
        super.impact();
        if(!super.isBroken()){
            crack.makeCrack(point,dir);
            updateBrick();
            return false;
        }
        return true;
    }


    /** gets brick shape
     * @return Returns brick shape
     */
    @Override
    public Shape getBrick() {
        return brickFace;
    }

    /**
     * updates brick appearance after crack is generated
     */
    private void updateBrick(){
        if(!super.isBroken()){
            GeneralPath gp = crack.draw();
            gp.append(super.brickFace,false);
            brickFace = gp;
        }
    }

    /**
     * restroes brick to original condition
     */
    public void repair(){
        super.repair();
        crack.reset();
        brickFace = super.brickFace;
    }
}
