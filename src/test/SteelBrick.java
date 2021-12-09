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

/** This class controls the dimensions, and appearance of the Steel Brick
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class SteelBrick extends Brick {
    /**
     * Contains the name of the cement Brick type
     */
    private static final String NAME = "Steel Brick";
    /**
     * Defines the inner color of the Steel brick
     */
    private static final Color DEF_INNER = new Color(113, 121, 126);
    /**
     * Defines the outer color of the Steel brick
     */
    private static final Color DEF_BORDER = new Color(55, 62, 66);
    /**
     * Defines the strength of the Steel brick
     */
    private static final int STEEL_STRENGTH = 1;
    /**
     * Defines the jump probability of the Steel brick
     */
    private static final double STEEL_PROBABILITY = 0.4;

    private Random rnd;
    private Shape brickFace;

    /** Generates Steel brick based on the parameters
     * @param point contains the point where Steel brick is going to be generated
     * @param size contains the dimensions of the Steel brick to be generated
     */
    public SteelBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH);
        rnd = new Random();
        brickFace = super.brickFace;
    }

    /** Generates Steel brick based on the parameters
     * @param pos  contains the position of the Steel brick
     * @param size contains the dimensions of the Steel brick
     * @return new Steel brick
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
        return brickFace;
    }

    public  boolean setImpact(Point2D point , int dir){
        if(super.isBroken())
            return false;
        impact();
        return  super.isBroken();
    }

    /**
     * Generates the number of steps skipped when generating the crack and reduces brick strength if there is impact and sets broken to true if strength reaches 0
     */
    public void impact(){
        if(rnd.nextDouble() < STEEL_PROBABILITY){
            super.impact();
        }
    }

}
