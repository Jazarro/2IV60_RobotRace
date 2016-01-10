/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import bodies.Body;
import bodies.BufferManager;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.shape.Rectangle;
import static javax.media.opengl.GL.GL_CULL_FACE;
import javax.media.opengl.GL2;
import robotrace.Camera;
import robotrace.Lighting;
import robotrace.Material;
import terrain.trees.Foliage;
import terrain.trees.Tree;
import terrain.trees.TreeSupplier;

/**
 * Implementation of the terrain.
 *
 * @author Arjan Boschman
 */
public class Terrain {

    public static final float TERRAIN_LEVEL = 145;
    private static final int WATER_LEVEL = 0;

    private final Set<Tree> trees = new HashSet<>();
    private Body terrainBody;
    private Body waterBody;

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        final Foliage foliage = new Foliage();
        foliage.initialize(gl, bmInitialiser);
        final FractalTerrainGenerator heightMap = FractalTerrainGenerator.create();
        final TreeSupplier treeSupplierClose = new TreeSupplier(new Rectangle(-150, -150, 300, 300), heightMap, foliage);
        treeSupplierClose.addForbiddenArea(-25, -25, 50, 50);
        final TreeSupplier treeSupplierFar = new TreeSupplier(new Rectangle(-500, -500, 1000, 1000), heightMap, foliage);
        treeSupplierFar.addForbiddenArea(-50, -50, 100, 100);
        for (int i = 0; i < 15; i++) {
            trees.add(treeSupplierClose.get());
            trees.add(treeSupplierFar.get());
        }
        this.terrainBody = new TerrainFactory(1000, 1000, 1F)
                .makeTerrain(bmInitialiser, heightMap);
        this.waterBody = new TerrainFactory(1000, 1000, 100)
                .makeTerrain(bmInitialiser, (x, y) -> WATER_LEVEL);
    }

    /**
     * Draws the terrain.
     *
     * @param gl       The instance of GL2 responsible for drawing the body.
     * @param glut     An instance of GLUT that can be optionally used to assist
     *                 in drawing.
     * @param camera   The camera that is used to view the terrain.
     * @param lighting The Lighting instance responsible for calculating the
     *                 lighting in this scene. Can be used to set the colours of
     *                 bodies before drawing them.
     */
    public void draw(GL2 gl, GLUT glut, Camera camera, Lighting lighting) {
        gl.glPushMatrix();
        {
            gl.glEnable(GL_CULL_FACE);
            lighting.setMaterial(gl, Material.DIRT);
            terrainBody.draw(gl);
            lighting.setMaterial(gl, Material.WATER);
            waterBody.draw(gl);
            gl.glDisable(GL_CULL_FACE);
            trees.stream().forEach((tree) -> tree.draw(gl, camera, lighting));
        }
        gl.glPopMatrix();
    }

}
