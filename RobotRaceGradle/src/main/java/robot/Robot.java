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
 *
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

    private double speed = 1d;
    /**
     * The material from which this robot is built.
     */
    private final Material material;
    /**
     * The object in charge of drawing the robot's actual physical body.
     */
    private final RobotBody robotBody;

    /**
     * Constructs a new instance of robot.
     *
     * @param material  The material that the robot is to be made of.
     * @param robotBody The aesthetics of the body used by this robot.
     */
    public Robot(Material material, RobotBody robotBody) {
        this.material = material;
        this.robotBody = robotBody;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
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
            final double rotationDotY = direction.dot(Vector.Y) / (direction.length() * Vector.Y.length());
            final double rotationDotX = direction.dot(Vector.X) / (direction.length() * Vector.X.length());
            final double rotationAngle = Math.toDegrees(Math.acos(rotationDotY));
            gl.glRotated((rotationDotX > 0d) ? (-rotationAngle) : (rotationAngle), Vector.Z.x(), Vector.Z.y(), Vector.Z.z());
            final double elevationDot = direction.dot(Vector.Z) / (direction.length() * Vector.Z.length());
            final double elevationAngle = Math.toDegrees(Math.asin(elevationDot));
            gl.glRotated(elevationAngle, Vector.X.x(), Vector.X.y(), Vector.X.z());
            robotBody.draw(gl, glut, stickFigure, tAnim);
        }
        gl.glPopMatrix();
    }
}
