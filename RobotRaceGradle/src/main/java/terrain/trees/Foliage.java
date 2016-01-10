/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
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
        this.leaf = new TerrainFactory(1, 1, 1).makeTerrain(bmInitialiser, (x, y) -> 0);
    }

    private Body makeBranch(BufferManager.Initialiser bmInitialiser, int sliceCount) {
        return new StackBuilder(bmInitialiser)
                .setSliceCount(sliceCount)
                .addConicalFrustum(0.5F, 0, 0, 1, false, false)
                .build();
    }

    public void drawLeaf(GL2 gl) {
        leaf.draw(gl);
    }

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
