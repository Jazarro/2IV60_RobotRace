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
    private Body branch;

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        this.branch = new StackBuilder(bmInitialiser)
                .setSliceCount(10)
                .addConicalFrustum(0.5, 0, 0, 1, false, false)
                .build();
        this.leaf = new TerrainFactory(1, 1, 1).makeTerrain(bmInitialiser, (x, y) -> 0);
    }

    public void drawLeaf(GL2 gl) {
        leaf.draw(gl);
    }

    public void drawBranch(GL2 gl) {
        branch.draw(gl);
    }

}
