/*
 * TU/e Eindhoven University of Technology
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * 
 * Copyright (C) 2015 Arjan Boschman, Robke Geenen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package terrain.trees;

import bodies.Body;
import bodies.BufferManager;
import bodies.SingletonDrawable;
import bodies.StackBuilder;
import javax.media.opengl.GL2;
import terrain.TerrainFactory;

/**
 * Manages tree-related bodies.
 *
 * @author Arjan Boschman
 * @see Body
 */
public class Foliage implements SingletonDrawable {

    private Body leaf;
    private Body ultraHighDetailBranch;
    private Body highDetailBranch;
    private Body midDetailBranch;
    private Body lowDetailBranch;

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        this.ultraHighDetailBranch = makeBranch(bmInitialiser, 10);
        this.highDetailBranch = makeBranch(bmInitialiser, 8);
        this.midDetailBranch = makeBranch(bmInitialiser, 4);
        this.lowDetailBranch = makeBranch(bmInitialiser, 3);
        this.leaf = new TerrainFactory(1f, 1f, 1f).makeTerrain(bmInitialiser, (x, y) -> 0f, null);
    }

    private Body makeBranch(BufferManager.Initialiser bmInitialiser, int sliceCount) {
        return new StackBuilder(bmInitialiser)
                .setSliceCount(sliceCount)
                .addConicalFrustum(0.5f, 0f, 0f, 1f, false, false)
                .build();
    }

    /**
     * Draws a leaf at the current transformation.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    public void drawLeaf(GL2 gl) {
        leaf.draw(gl);
    }

    /**
     * Draws a branch at the current transformation. The level of detail (IE the
     * number of faces) on the branch depends on the given detail level.
     *
     * @param gl                  The instance of GL2 responsible for drawing
     *                            the body.
     * @param requiredDetailLevel The required level of detail. This is an
     *                            arbitrary scale, a number between 0 (incl) and
     *                            3 (excl) is expected. Any other number will be
     *                            interpreted as 3 instead. The higher the
     *                            number, the higher the detail level.
     */
    public void drawBranch(GL2 gl, int requiredDetailLevel) {
        switch (requiredDetailLevel) {
            case 0:
                lowDetailBranch.draw(gl);
                break;
            case 1:
                midDetailBranch.draw(gl);
                break;
            case 2:
                highDetailBranch.draw(gl);
                break;
            case 3:
            default:
                ultraHighDetailBranch.draw(gl);
                break;
        }
    }

}
