/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import robot.bender.Bender;
import robotrace.Material;
import robotrace.Vector;

/**
 * Represents a Robot, to be implemented according to the Assignments.
 * @author Arjan Boschman
 */
public class Robot {

    /**
     * The position of the robot.
     */
    private Vector position = Vector.O;
    /**
     * The direction in which the robot is facing.
     */
    private Vector direction = Vector.Y;
    /**
     * The material from which this robot is built.
     */
    private final Material material;
    /**
     * The object in charge of drawing the robot's actual physical body.
     */
    private final GLRobotBody robotBody;

    /**
     * Constructs a new instance of robot.
     *
     * @param material  The material that the robot is to be made of.
     * @param robotBody The aesthetics of the body used by this robot.
     */
    public Robot(Material material, GLRobotBody robotBody) {
        this.material = material;
        this.robotBody = robotBody;
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
     * Draws this robot on the screen.
     *
     * @param gl          The instance of GL2 responsible for drawing the robot
     *                    on the screen.
     * @param glu         An instance of GLU to optionally aid in drawing the
     *                    robot body.
     * @param glut        An instance of GLUT to optionally aid in drawing the
     *                    robot body.
     * @param stickFigure If true, the robot must draw itself as a stick figure
     *                    rather than a solid body.
     * @param tAnim       Time since the start of the animation in seconds.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        gl.glPushMatrix();
        {
            gl.glTranslated(position.x(), position.y(), position.z());
            final Vector rotationAxis = calcRotationAxis();
            gl.glRotated(calcRotationAngle(), rotationAxis.x(), rotationAxis.y(), rotationAxis.z());
            robotBody.draw(gl, glut, stickFigure);
        }
        gl.glPopMatrix();
    }

    /**
     * Calculate the rotation angle for this robot at it's current orientation.
     *
     * @return The angle in degrees that the robot body must be rotated around
     *         its current rotation axis in order to match the current robot
     *         direction.
     *
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
     *
     * @see #calcRotationAngle()
     */
    private Vector calcRotationAxis() {
        return direction.cross(Bender.ORIENTATION).normalized();
    }

}
