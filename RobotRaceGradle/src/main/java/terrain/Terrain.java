/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import Texture.ImplementedTexture;
import bodies.Body;
import bodies.BufferManager;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.shape.Rectangle;
import static javax.media.opengl.GL.GL_CULL_FACE;
import javax.media.opengl.GL2;
import robotrace.Lighting;
import robotrace.Material;
import robotrace.Vector;
import terrain.trees.Foliage;
import terrain.trees.Tree;
import terrain.trees.TreeSupplier;

/**
 * Implementation of the terrain.
 *
 * @author Arjan Boschman
 */
public class Terrain {

    private static final Vector TERRAIN_LEVEL = new Vector(0, 0, -145);
    private static final float WATER_LEVEL = 0f;
    private static final String TERRAIN_TEXTURE_FILENAME = "terrain.png";

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
        final ImplementedTexture terrainTexture = new ImplementedTexture(gl, TERRAIN_TEXTURE_FILENAME, true, false);
        this.terrainBody = new TerrainFactory(1000, 1000, 1F)
                .makeTerrain(bmInitialiser, heightMap, terrainTexture);
        this.waterBody = new TerrainFactory(1000, 1000, 100)
                .makeTerrain(bmInitialiser, (x, y) -> WATER_LEVEL, null);
    }

    /**
     * Draws the terrain.
     *
     * @param gl       The instance of GL2 responsible for drawing the body.
     * @param glut     An instance of GLUT that can be optionally used to assist
     *                 in drawing.
     * @param camPos   The position of the camera in world coordinates.
     * @param lighting The Lighting instance responsible for calculating the
     *                 lighting in this scene. Can be used to set the colours of
     *                 bodies before drawing them.
     */
    public void draw(GL2 gl, GLUT glut, Vector camPos, Lighting lighting) {
        gl.glPushMatrix();
        {
            gl.glTranslated(TERRAIN_LEVEL.x(), TERRAIN_LEVEL.y(), TERRAIN_LEVEL.z());
            gl.glEnable(GL_CULL_FACE);
            lighting.setMaterial(gl, Material.NONE);
            lighting.setColor(gl, 1f, 1f, 1f, 1f);
            terrainBody.draw(gl);
            lighting.setMaterial(gl, Material.WATER);
            waterBody.draw(gl);
            gl.glDisable(GL_CULL_FACE);
            final Vector camPosRelativeToTerrain = camPos.subtract(TERRAIN_LEVEL);
            trees.stream().forEach((tree) -> tree.draw(gl, camPosRelativeToTerrain, lighting));
        }
        gl.glPopMatrix();
    }

}
