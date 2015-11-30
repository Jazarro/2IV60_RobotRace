package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import robotrace.bender.Bender;

/**
 * Represents a Robot, to be implemented according to the Assignments.
 */
public class Robot {

    /**
     * The position of the robot.
     */
    private Vector position = new Vector(0, 0, 0);
    /**
     * The direction in which the robot is running.
     */
    private Vector direction = new Vector(0, 1, 0);
    /**
     * The material from which this robot is built.
     */
    private final Material material;
    private final int robotType;
    private final Bender bender;

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, Bender bender) {
        this.robotType = 0;
        this.material = material;
        this.bender = bender;
    }

    public Robot(Material material, int robotType, Bender bender) {
        this.robotType = robotType;
        this.material = material;
        this.bender = bender;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Material getMaterial() {
        return material;
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        gl.glPushMatrix();
        gl.glTranslated(position.x(), position.y(), position.z());
        final Vector rotationAxis = calcRotationAxis();
        gl.glRotated(calcRotationAngle(), rotationAxis.x(), rotationAxis.y(), rotationAxis.z());
        bender.draw(gl);
        gl.glPopMatrix();
    }

    /**
     * Calculate the rotation angle for this robot at it's current orientation.
     *
     * @return The angle in degrees that the robot body must be rotated around
     *         its current rotation axis in order to match the current robot
     *         direction.
     * @see #calcRotationAxis()
     */
    private double calcRotationAngle() {
        return Math.toDegrees(Math.acos(direction.dot(Bender.ORIENTATION)));
    }

    /**
     * Calculate the rotational axis for this robot at it's current orientation.
     * This is the axis around which the robot must be rotated in order to get
     * from the robot body's default, static orientation to the robot's dynamic
     * orientation.
     *
     * @return A normalized vector representing a rotational axis.
     * @see #calcRotationAngle()
     */
    private Vector calcRotationAxis() {
        return direction.cross(Bender.ORIENTATION).normalized();
    }

}
