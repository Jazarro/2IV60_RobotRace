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
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import robotrace.Lighting;
import robotrace.Material;

/**
 * Implementation of the terrain.
 *
 * @author Arjan Boschman
 */
public class Terrain {

    private static final int WATER_LEVEL = 0;

    private final Set<Tree> trees = new HashSet<>();
    private Body terrainBody;
    private Body waterBody;

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        final Foliage foliage = new Foliage();
        foliage.initialize(gl, bmInitialiser);
        final FractalTerrainGenerator heightMap = FractalTerrainGenerator.create();
        final TreeGenerator treeGenerator = new TreeGenerator(new Rectangle(-500, -500, 1000, 1000), heightMap, foliage);
        treeGenerator.addForbiddenArea(-40, -40, 80, 80);
        for (int i = 0; i < 1000; i++) {
            trees.add(treeGenerator.get());
        }
        this.terrainBody = new TerrainFactory(1000, 1000, 1F)
                .makeTerrain(bmInitialiser, heightMap);
        this.waterBody = new TerrainFactory(1000, 1000, 100)
                .makeTerrain(bmInitialiser, (x, y) -> WATER_LEVEL);
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, Lighting lighting) {
        gl.glPushMatrix();
        {
            gl.glTranslated(0, 0, -145);//Ideally make the tracks fit the terrain, rather than the other way around.
            lighting.setMaterial(gl, Material.DIRT);
            terrainBody.draw(gl, glut);
            lighting.setMaterial(gl, Material.WATER);
            waterBody.draw(gl, glut);
            lighting.setMaterial(gl, Material.GREEN);
            trees.stream().forEach((tree) -> tree.draw(gl, glut, lighting));
        }
        gl.glPopMatrix();
    }

}
