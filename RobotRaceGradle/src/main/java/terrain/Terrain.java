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

    private static final int WATER_LEVEL = -20;
    private Body terrainBody;
    private Body waterBody;

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        this.terrainBody = new TerrainFactory(1000, 1000, 1F)
                .makeTerrain(bmInitialiser, FractalTerrainGenerator.create());
        this.waterBody = new TerrainFactory(1000, 1000, 1000)
                .makeTerrain(bmInitialiser, (x, y) -> WATER_LEVEL);
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, Lighting lighting) {
        gl.glPushMatrix();
        gl.glTranslated(0, 0, -125);
        lighting.setMaterial(gl, Material.DIRT);
        terrainBody.draw(gl, glut);
        lighting.setMaterial(gl, Material.WATER);
        waterBody.draw(gl, glut);
        gl.glPopMatrix();
    }

}
