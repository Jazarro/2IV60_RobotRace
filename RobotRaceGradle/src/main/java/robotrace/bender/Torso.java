/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace.bender;

import bodies.Body;
import bodies.BodyManager;
import bodies.SimpleBody;
import com.jogamp.opengl.util.gl2.*;
import javax.media.opengl.*;

/**
 * Convenience class used by {@link Bender} to draw the torso and head.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Torso {

    /**
     * Radius of the body at the hips.
     */
    private static final double RADIUS_SHINY = 0.175d;
    /**
     * Radius of the body at the chest.
     */
    private static final double RADIUS_TORSO = 0.225d;
    /**
     * Radius of the body at the start of the neck.
     */
    private static final double RADIUS_NECK = 0.125d;
    /**
     * Radius of the body at the end of the neck and the head.
     */
    private static final double RADIUS_HEAD = 0.125d;
    /**
     * Radius of the body at the antenna base.
     */
    private static final double RADIUS_ANTENNA_BOTTOM = 0.05d;
    /**
     * Radius of the body at the bottom of the antenna, just above the base.
     */
    private static final double RADIUS_ANTENNA_MIDDLE = 0.03d;
    /**
     * Radius of the body at the top of the antenna, just below the transmitter.
     */
    private static final double RADIUS_ANTENNA_TOP = 0.01d;
    /**
     * Radius of the body at the center of the transmitter ball.
     */
    private static final double RADIUS_ANTENNA_BALL_MIDDLE = 0.025d;
    /**
     * Radius of the body at the top of the transmitter.
     */
    private static final double RADIUS_ANTENNA_BALL_TOP = 0d;

    /**
     * Height of the body at the hips.
     */
    private static final double HEIGHT_SHINY = 0d;
    /**
     * Height of the body at the chest.
     */
    private static final double HEIGHT_TORSO = 0.45d;
    /**
     * Height of the body at the start of the head.
     */
    private static final double HEIGHT_NECK = 0.55d;
    /**
     * Height of the body at where the head starts to curve inwards.
     */
    private static final double HEIGHT_HEAD = 0.875d;
    /**
     * Height of the body at the base of the antenna.
     */
    private static final double HEIGHT_ANTENNA_BOTTOM = 1d;
    /**
     * Height of the body at the top of the antenna base.
     */
    private static final double HEIGHT_ANTENNA_MIDDLE = 1.025d;
    /**
     * Height of the body at the top of the antenna, just below the transmitter.
     */
    private static final double HEIGHT_ANTENNA_TOP = 1.175d;
    /**
     * Height of the body at the center of the transmitter.
     */
    private static final double HEIGHT_ANTENNA_BALL_MIDDLE = HEIGHT_ANTENNA_TOP + RADIUS_ANTENNA_BALL_MIDDLE; //1.1875d;
    /**
     * Height of the body at the top of the transmitter.
     */
    private static final double HEIGHT_ANTENNA_BALL_TOP = HEIGHT_ANTENNA_BALL_MIDDLE + RADIUS_ANTENNA_BALL_MIDDLE; //1.2d;

    /**
     * The number of edges to give the rings of the various shapes.
     */
    private static final int SLICE_COUNT = 50;
    /**
     * The number of rings to use when calculating a partial torus curve.
     */
    private static final int STACK_COUNT = 20;
    /*
     The width of the stick figure sticks.
     */
    private static final double STICK_THICK = 0.03d;

    private Body torsoBody;

    public void initialize(GL2 gl, BodyManager.Initialiser bmInitialiser) {
        torsoBody = new SimpleBody.StackBuilder(bmInitialiser)
                .setSliceCount(SLICE_COUNT)
                .addConicalFrustum(RADIUS_SHINY, RADIUS_TORSO, HEIGHT_SHINY, HEIGHT_TORSO, true, false)
                .addConicalFrustum(RADIUS_TORSO, RADIUS_NECK, HEIGHT_TORSO, HEIGHT_NECK, false, false)
                .addConicalFrustum(RADIUS_NECK, RADIUS_HEAD, HEIGHT_NECK, HEIGHT_HEAD, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_HEAD, RADIUS_ANTENNA_BOTTOM, HEIGHT_HEAD, HEIGHT_ANTENNA_BOTTOM, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_ANTENNA_BOTTOM, RADIUS_ANTENNA_MIDDLE, HEIGHT_ANTENNA_BOTTOM, HEIGHT_ANTENNA_MIDDLE, false, false)
                .addConicalFrustum(RADIUS_ANTENNA_MIDDLE, RADIUS_ANTENNA_TOP, HEIGHT_ANTENNA_MIDDLE, HEIGHT_ANTENNA_TOP, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_ANTENNA_TOP, RADIUS_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_ANTENNA_BALL_MIDDLE, RADIUS_ANTENNA_BALL_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_BALL_TOP, false, false)
                .build();
    }

    /**
     * To be called in the draw loop. Uses the given instance of GL2 to draw the
     * body.
     *
     * @param gl          The instance of GL2 responsible for drawing the body.
     * @param glut        An instance of GLUT to optionally aid in drawing the
     *                    robot body.
     * @param stickFigure If true, the robot must draw itself as a stick figure
     *                    rather than a solid body.
     */
    public void draw(GL2 gl, GLUT glut, boolean stickFigure) {
        if (stickFigure) {
            double bdyHeight = HEIGHT_ANTENNA_BOTTOM - HEIGHT_SHINY;
            gl.glPushMatrix();
            gl.glScaled(STICK_THICK, STICK_THICK, bdyHeight);
            gl.glTranslated(0d, 0d, bdyHeight / 2);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
        } else {
            torsoBody.draw(gl, glut);
        }
    }

}
