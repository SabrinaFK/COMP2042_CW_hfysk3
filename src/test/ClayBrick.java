package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;


/** This class controls the dimensions, and appearance of the Clay Brick
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class ClayBrick extends Brick {
    /**
     * Contains the name of the clay Brick type
     */
    private static final String NAME = "Clay Brick";
    /**
     * Defines the inner color of the clay brick
     */
    private static final Color DEF_INNER = new Color(203, 86, 65).darker();
    /**
     * Defines the outer color of the clay brick
     */
    private static final Color DEF_BORDER = new Color(162, 162, 162);
    /**
     * Defines the strength of the clay brick
     */
    private static final int CLAY_STRENGTH = 1;





    /** Generates clay brick based on the parameters
     * @param point contains the point where clay brick is going to be generated
     * @param size contains the dimensions of the clay brick to be generated
     */
    public ClayBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH);
    }

    /** Generate clay brick based on the parameters
     * @param pos  contains the position of the brick
     * @param size contains the dimensions of the brick
     * @return new clay brick
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /** gets brick shape
     * @return Returns brick shape
     */
    @Override
    public Shape getBrick() {
        return super.brickFace;
    }


}
