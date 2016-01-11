/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain.debug;

import bodies.BufferManager;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import robotrace.Lighting;
import robotrace.Material;
import robotrace.Vector;
import terrain.Terrain;

/**
 * Terrain made for measuring robot velocities. It's got some handy evenly
 * spaced stripes to help get a good idea of velocities.
 *
 * @author Arjan Boschman
 */
@Deprecated
public class TestingTerrain extends Terrain {

    /**
     * z-coordinate of the ground. Note that this is the center of the block,
     * the actual ground level one could step on is half a meter higher.
     */
    private static final double GROUND_LEVEL = -0.5;
    /**
     * The edge length of the ground.
     */
    private static final double GROUND_EDGE_LENGTH = 25;

    private final List<Shape> shapes = new ArrayList<>();

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        for (int i = -5; i < 1000; i++) {
            shapes.add(makeFloorShape(i));
            shapes.add(makeWallShape(i));
        }
    }

    private Shape makeFloorShape(int i) {
        final Vector translation = new Vector(i * 0.1F, 0, GROUND_LEVEL);
        final Vector scaling = new Vector(0.1F, GROUND_EDGE_LENGTH, 1);
        final Material material = i % 2 == 0 ? Material.BOULDER : Material.DIRT;
        return new Shape(translation, Vector.O, 0, scaling, material);
    }

    private Shape makeWallShape(int i) {
        final Vector translation = new Vector(i * 0.1F, 0.5F, 1);
        final Vector scaling = new Vector(0.1F, 1, 2);
        final Material material = i % 2 != 0 ? Material.BOULDER : Material.DIRT;
        return new Shape(translation, Vector.O, 0, scaling, material);
    }

    @Override
    public void draw(GL2 gl, GLUT glut, Vector camPos, Lighting lighting) {
        shapes.stream().forEach((shape) -> {
            gl.glPushMatrix();
            gl.glTranslated(shape.translation.x(), shape.translation.y(), shape.translation.z());
            gl.glRotated(shape.rotationAngle, shape.rotation.x(), shape.rotation.y(), shape.rotation.z());
            gl.glScaled(shape.scaling.x(), shape.scaling.y(), shape.scaling.z());
            lighting.setMaterial(gl, shape.material);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
        });
    }

    private class Shape {

        private final Vector translation;
        private final Vector rotation;
        private final double rotationAngle;
        private final Vector scaling;
        private final Material material;

        private Shape(Vector translation, Vector rotation, double rotationAngle, Vector scaling, Material material) {
            this.translation = translation;
            this.rotation = rotation;
            this.rotationAngle = rotationAngle;
            this.scaling = scaling;
            this.material = material;
        }

    }

}
