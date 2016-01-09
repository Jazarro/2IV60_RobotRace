/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import bodies.Body;
import bodies.BufferManager;
import bodies.SingletonDrawable;
import bodies.StackBuilder;
import javax.media.opengl.GL2;

/**
 *
 * @author Arjan Boschman
 */
public class Foliage implements SingletonDrawable {

    private Body leaf;
    private Body branch;

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        this.branch = new StackBuilder(bmInitialiser)
                .setSliceCount(20)
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
