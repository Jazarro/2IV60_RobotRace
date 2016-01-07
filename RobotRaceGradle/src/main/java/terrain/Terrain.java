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
 * @author Arjan Boschman
 */
public class Terrain {

    private Body terrainBody;

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        this.terrainBody = new TerrainFactory(100, 100, 1)
                .makeTerrain(bmInitialiser,
                        (float x, float y) -> {
                            return (float) (0.6 * Math.cos(0.3 * x + 0.2 * y) + 0.4 * Math.cos(x - 0.5 * y));
                        });
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, Lighting lighting) {
        lighting.setMaterial(gl, Material.DIRT);
        terrainBody.draw(gl, glut);
    }

}
