/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot.bender;

import bodies.Body;
import bodies.BodyManager;
import bodies.SimpleBody;
import com.jogamp.opengl.util.gl2.GLUT;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import javax.media.opengl.GL2;

/**
 * Convenience class used by {@link Bender} to draw the arms, legs, hands and
 * feet.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Limb {

    /**
     * The number of cylinders to use for each limb.
     */
    public static final int RING_COUNT = 6;
    /**
     * The number of fingers to use on each hand.
     */
    private static final int FINGER_COUNT = 3;
    private static final double FINGER_OFFCENTER = 0.03d;

    private static final double HEIGHT_RING = 0.5d / 6d;
    private static final double HEIGHT_FOOT = 0.1d;
    private static final double HEIGHT_HAND = 0.07d;
    private static final double HEIGHT_FINGER = 0.0625d;

    private static final double RADIUS_RING = 0.04d;
    private static final double RADIUS_FOOT = Math.sqrt(Math.pow(HEIGHT_FOOT, 2d) + Math.pow(RADIUS_RING, 2d));
    private static final double RADIUS_HAND = 0.06d;
    private static final double RADIUS_FINGER = HEIGHT_FINGER - 0.05d;

    /*
     The width of the stick figure sticks.
     */
    private static final double STICK_THICK = 0.03d;

    /**
     * The number of edges to give the rings of the various shapes.
     */
    private static final int SLICE_COUNT = 50;
    /**
     * The number of rings to use when calculating a partial torus curve.
     */
    private static final int STACK_COUNT = 20;

    private Body ringBody;
    private Body footBody;
    private Body handBody;
    private Body fingerBody;

    /**
     * NB: Due to lack of time there is significant code duplication here. This
     * will be cleaned up by the next submission. The pattern followed is
     * basically the same as in the {@link Body} class. Please refer to that
     * class for explanation.
     *
     * @param gl            The instance of GL2 responsible for drawing the
     *                      body.
     * @param bmInitialiser
     */
    public void initialize(GL2 gl, BodyManager.Initialiser bmInitialiser) {
        ringBody = new SimpleBody.StackBuilder(bmInitialiser)
                .setSliceCount(SLICE_COUNT)
                .addConicalFrustum(RADIUS_RING, RADIUS_RING, 0d, -HEIGHT_RING, true, true)
                .build();

        footBody = new SimpleBody.StackBuilder(bmInitialiser)
                .setSliceCount(SLICE_COUNT)
                .addPartialTorus(STACK_COUNT, RADIUS_FOOT, 0d, 0d, HEIGHT_FOOT, true, false)
                .build();

        handBody = new SimpleBody.StackBuilder(bmInitialiser)
                .setSliceCount(SLICE_COUNT)
                .addConicalFrustum(RADIUS_RING, RADIUS_HAND, 0d, -HEIGHT_HAND, true, true)
                .build();

        fingerBody = new SimpleBody.StackBuilder(bmInitialiser)
                .setSliceCount(SLICE_COUNT)
                .addConicalFrustum(RADIUS_FINGER, RADIUS_FINGER, RADIUS_FINGER, -HEIGHT_FINGER + RADIUS_FINGER, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_FINGER, 0d, -HEIGHT_FINGER + RADIUS_FINGER, -HEIGHT_FINGER, false, false)
                .build();
    }

    /**
     * To be called in the draw loop. Uses the given instance of GL2 to draw the
     * body.
     *
     * Drawing the shapes here works much the same as in {@link Body}, however
     * the limbs have six independently rotatable cylinders and a foot/hand at
     * the end. The rotations are given as arguments, the translations are
     * inferred from those.
     *
     * @param gl          The instance of GL2 responsible for drawing the body.
     * @param glut        An instance of GLUT to optionally aid in drawing the
     *                    robot body.
     * @param stickFigure If true, the robot must draw itself as a stick figure
     *                    rather than a solid body.
     * @param anglesAxis  Describes the vectors around which the limbs are
     *                    rotated. This array is in the form x2,y2,z2,x2,y2,z2
     *                    etc...
     * @param anglesBend  An array of angles, in degrees. Each angle is relative
     *                    to the bender body. The zeroth element is for the
     *                    cylinder at the shoulder, each consecutive element is
     *                    for the next cylinder, all the way down to the wrist.
     * @param limbType
     */
    public void draw(GL2 gl, GLUT glut, boolean stickFigure, double[] anglesAxis, double[] anglesBend, LimbType limbType) {
        if (stickFigure) {
            double lmbHeight = height(anglesAxis, anglesBend, limbType);
            lmbHeight = HEIGHT_RING * RING_COUNT;
            double currAngleAxis = 0d;
            double currAngleBend = 0d;
            for (int i = 0; i < anglesAxis.length; i++) {
                currAngleAxis += anglesAxis[i];
            }
            for (int i = 0; i < anglesBend.length; i++) {
                currAngleBend += anglesBend[i];
            }
            currAngleAxis /= anglesAxis.length;
            currAngleBend /= anglesBend.length;
            gl.glPushMatrix();
            {
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
                gl.glScaled(STICK_THICK, STICK_THICK, lmbHeight);
                gl.glTranslated(0d, 0d, -lmbHeight / 2);
                glut.glutSolidCube(1f);
            }
            gl.glPopMatrix();
        } else {
            double currAngleAxis;
            double currAngleBend;
            double newPos[] = {0d, 0d, 0d};
            for (int i = 0; i < RING_COUNT; i++) {
                gl.glPushMatrix();
                currAngleAxis = (limbType.isRight()) ? (-anglesAxis[i]) : (anglesAxis[i]);
                currAngleBend = anglesBend[i];
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
                newPos = nextPos(newPos, HEIGHT_RING, currAngleBend, currAngleAxis);
                ringBody.draw(gl, glut);
                gl.glPopMatrix();
            }
            gl.glPushMatrix();
            currAngleAxis = (limbType.isRight()) ? (-anglesAxis[RING_COUNT]) : (anglesAxis[RING_COUNT]);
            currAngleBend = anglesBend[RING_COUNT];
            if (limbType.isLeg()) {
                newPos = nextPos(newPos, HEIGHT_FOOT, currAngleBend, currAngleAxis);
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
                footBody.draw(gl, glut);
            } else {
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
                handBody.draw(gl, glut);

                gl.glTranslated(0d, 0d, -HEIGHT_HAND);
                for (int j = 0; j < FINGER_COUNT; j++) {
                    //todo: add FINGER_PHASE
                    //todo: add fingerAngle
                    gl.glPushMatrix();
                    gl.glTranslated(FINGER_OFFCENTER * cos(toRadians(j * 360 / FINGER_COUNT)), FINGER_OFFCENTER * sin(toRadians(j * 360 / FINGER_COUNT)), 0d);
                    fingerBody.draw(gl, glut);
                    gl.glPopMatrix();
                }
            }
            gl.glPopMatrix();
        }
    }

    /**
     * Used in order to be able to calculate the distance of the torso to the
     * ground. Returns the height of the 'highest' leg.
     *
     * @param anglesAxis Describes the vectors around which the limbs are
     *                   rotated. This array is in the form x2,y2,z2,x2,y2,z2
     *                   etc...
     * @param anglesBend An array of angles, in degrees. Each angle is relative
     *                   to the bender body. The zeroth element is for the
     *                   cylinder at the shoulder, each consecutive element is
     *                   for the next cylinder, all the way down to the wrist.
     * @param limbType   Only works for leg types.
     *
     * @return The distance of the torso to the ground, assuming that at least
     *         one leg touches the ground.
     */
    public double height(double[] anglesAxis, double[] anglesBend, LimbType limbType) {
        double currAngleAxis;
        double currAngleBend;
        double newPos[] = {0d, 0d, 0d};
        for (int i = 0; i < RING_COUNT; i++) {
            currAngleAxis = (limbType.isRight()) ? (-anglesAxis[i]) : (anglesAxis[i]);
            currAngleBend = anglesBend[i];
            newPos = nextPos(newPos, HEIGHT_RING, currAngleBend, currAngleAxis);
        }
        currAngleAxis = (limbType.isRight()) ? (-anglesAxis[RING_COUNT]) : (anglesAxis[RING_COUNT]);
        currAngleBend = anglesBend[RING_COUNT];
        if (limbType.isLeg()) {
            newPos = nextPos(newPos, HEIGHT_FOOT, currAngleBend, currAngleAxis);
            newPos[2] -= RADIUS_FOOT * Math.sin(Math.toRadians(currAngleBend));
        } else {//todo: arm does not return correct height
            //newPos[2] -= ?;
            newPos = nextPos(newPos, HEIGHT_FOOT, currAngleBend, currAngleAxis);
            newPos[2] -= RADIUS_FOOT * Math.sin(Math.toRadians(currAngleBend));
        }
        return Math.abs(newPos[2]);
    }

    //todo: fix relative angles?
    private double[] nextPos(double[] currPos, double height, double currAngleBend, double currAngleAxis) {
        currPos[0] -= height * Math.sin(Math.toRadians(currAngleBend)) * Math.sin(Math.toRadians(currAngleAxis));
        currPos[1] += height * Math.sin(Math.toRadians(currAngleBend)) * Math.cos(Math.toRadians(currAngleAxis));
        currPos[2] -= height * Math.cos(Math.toRadians(currAngleBend));
        return currPos;
    }

    /**
     * Convenience class used to announce what limb this is. Will probably be
     * replaced by a more elegant solution later.
     */
    @SuppressWarnings("PublicInnerClass")
    public static enum LimbType {

        RIGHT_ARM(true, true),
        LEFT_ARM(true, false),
        RIGHT_LEG(false, true),
        LEFT_LEG(false, false);

        private final boolean isArm;
        private final boolean isRight;

        private LimbType(boolean isArm, boolean isRight) {
            this.isArm = isArm;
            this.isRight = isRight;
        }

        public boolean isArm() {
            return isArm;
        }

        public boolean isLeg() {
            return !isArm;
        }

        public boolean isRight() {
            return isRight;
        }

        public boolean isLeft() {
            return !isRight;
        }

    }

}
