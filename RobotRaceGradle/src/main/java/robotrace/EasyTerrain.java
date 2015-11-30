/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Temporary setup for Terrain, just puts down some rough cubes to test things
 * out. Later we'll probably want to use a voxel based approach and read it out
 * from Xml or something.
 *
 * @author Arjan Boschman
 */
public class EasyTerrain extends Terrain {

    /**
     * z-coordinate of the ground. Note that this is the center of the block,
     * the actual ground level one could step on is half a meter higher.
     */
    private static final double GROUND_LEVEL = -5;
    /**
     * The edge length of the ground.
     */
    private static final double GROUND_EDGE_LENGTH = 100;
    /**
     * The edge length of the square in which ground clutter is allowed to
     * appear.
     */
    private static final double GROUND_CLUTTER_RANGE = 100;
    /**
     * The number of objects that will appear on the ground as ground clutter.
     */
    private static final int AMOUNT_OF_GROUND_CLUTTER = 100;
    /**
     * A seed to use for producing random values. By making this a constant the
     * terrain won't change from one run to another.
     */
    private static final long RANDOM_SEED = 123456761L;

    private final List<Shape> shapes = new ArrayList<>();

    @Override
    public void initialize() {
        shapes.add(makeGround());
        final Random rand = new Random(RANDOM_SEED);
        for (int i = 0; i < AMOUNT_OF_GROUND_CLUTTER; i++) {
            shapes.add(makeRandomCube(rand));
        }
    }

    private Shape makeGround() {
        final Vector translation = new Vector(0, 0, GROUND_LEVEL);
        final Vector scaling = new Vector(GROUND_EDGE_LENGTH, GROUND_EDGE_LENGTH, 1);
        return new Shape(translation, Vector.O, 0, scaling, Material.DIRT);
    }

    private Shape makeRandomCube(Random rand) {
        final Vector translation = new Vector(getCoord(rand), getCoord(rand), GROUND_LEVEL);
        final Vector rotation = new Vector(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()).normalized();
        final double rotationAngle = rand.nextDouble() * 360;
        final Vector scaling = new Vector(rand.nextDouble() * 2, rand.nextDouble() * 2, rand.nextDouble() * 2);
        final Material material = Material.BOULDER;
        return new Shape(translation, rotation, rotationAngle, scaling, material);
    }

    /**
     * Calculate a random x or y coordinate that would fit on the ground.
     *
     * @param rand An instance of Random.
     * @return The random coordinate.
     */
    private double getCoord(Random rand) {
        return (rand.nextDouble() * GROUND_CLUTTER_RANGE) - (GROUND_CLUTTER_RANGE / 2);
    }

    @Override
    public void draw(GL2 gl, GLU glu, GLUT glut, Lighting lighting) {
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
