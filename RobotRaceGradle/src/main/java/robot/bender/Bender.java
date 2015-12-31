/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot.bender;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import robot.RobotBody;
import robot.Stance;
import robotrace.Vector;

/**
 * An instance of GLRobotBody, this class describes how to build and draw a
 * robot body resembling the character 'Bender' from the popular cartoon
 * Futurama.
 *
 * @author Robke Geenen
 * @author Arjan Boschman
 */
public class Bender implements RobotBody {

    /**
     * The orientation of the robot body as a unit vector. Use this together
     * with the robots dynamic orientation to get the angle and axis needed to
     * perform the OpenGL rotation.
     */
    public static final Vector LOCAL_ORIENTATION = new Vector(0, 1, 0);

    /**
     * The absolute distance on the x-axis between the central vertical axis and
     * the position of the legs.
     */
    private static final double LEG_OFFCENTER = 0.1d;
    /**
     * The height of the shoulder relative to the height of the torso.
     */
    private static final double SHOULDER_HEIGHT = 0.375d;
    /**
     * The absolute distance on the x-axis between the central vertical axis and
     * the position of the shoulders.
     */
    private static final double SHOULDER_OFFCENTER = 0.2d;
    
    private final double[] leftLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesBend = new double[Limb.RING_COUNT + 1];

    private final Leg rightLeg;
    private final Torso torso;
    @Deprecated//Handle now kept in leg.
    private final Limb limb;
    private Stance defaultStance = Stance.IDLE;
    
    public Bender(Torso torso, Limb limb) {
        this.rightLeg = new Leg(limb, 1f);
        this.torso = torso;
        this.limb = limb;

        setLeftLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{10d, 20d, 30d, 40d, 50d, 60d, 70d,80d});
        setRightLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{-20d, -20d, -20d, -20d, -20d, -20d, -20d, -20d});
        setLeftArmAngles(new double[]{135d, 135d, 135d, 135d, 135d, 135d, 135d, 135d}, new double[]{45d, 90d, 90d, 90d, 90d, 90d, 90d,90d});
        setRightArmAngles(new double[]{45d, 45d, 45d, 45d, 45d, 45d, 45d, 45d}, new double[]{45d, 90d, 90d, 90d, 90d, 90d, 90d, 90d});
    }

    @Override
    public void draw(GL2 gl, GLUT glut, boolean stickFigure, float tAnim) {
        gl.glPushMatrix();
        {
            /**
             * Calculate the height of the legs and translate it so that the
             * rest of the body, arms and head are always drawn on top of them.
             * Note that the legs are currently drawn below zero
             */
            final double legHeight = Math.max(limb.height(leftLegAnglesAxis, leftLegAnglesBend, Limb.LimbType.LEFT_LEG), limb.height(rightLegAnglesAxis, rightLegAnglesBend, Limb.LimbType.RIGHT_LEG));
            gl.glTranslated(0d, 0d, legHeight);

            //Draws the torso and head:
            gl.glPushMatrix();
            torso.draw(gl, glut, stickFigure);
            gl.glPopMatrix();

            //Draws the eyes:
            gl.glPushMatrix();
            gl.glTranslated(0.05d, -0.125d, 0.8d);
            glut.glutSolidSphere(0.025d, 50, 50);
            gl.glTranslated(-0.1d, 0d, 0d);
            glut.glutSolidSphere(0.025d, 50, 50);
            gl.glPopMatrix();

            //Draws the right leg and foot.
            gl.glPushMatrix();
            gl.glTranslated((stickFigure) ? (-STICK_THICKNESS / 2) : (-LEG_OFFCENTER), 0d, 0d);
            limb.draw(gl, glut, stickFigure, rightLegAnglesAxis, rightLegAnglesBend, Limb.LimbType.RIGHT_LEG);
            gl.glPopMatrix();

            //Draws the left leg and foot.
            gl.glPushMatrix();
            gl.glTranslated((stickFigure) ? (STICK_THICKNESS / 2) : (LEG_OFFCENTER), 0d, 0d);
            limb.draw(gl, glut, stickFigure, leftLegAnglesAxis, leftLegAnglesBend, Limb.LimbType.LEFT_LEG);
            gl.glPopMatrix();

            //Draws the right arm and hand.
            gl.glPushMatrix();
            gl.glTranslated((stickFigure) ? (-STICK_THICKNESS / 2) : (-SHOULDER_OFFCENTER), 0d, SHOULDER_HEIGHT);
            gl.glRotated(90d, 0d, 1d, 0d);
            limb.draw(gl, glut, stickFigure, rightArmAnglesAxis, rightArmAnglesBend, Limb.LimbType.RIGHT_ARM);
            gl.glPopMatrix();

            //Draws the left arm and hand.
            gl.glPushMatrix();
            gl.glTranslated((stickFigure) ? (STICK_THICKNESS / 2) : (SHOULDER_OFFCENTER), 0d, SHOULDER_HEIGHT);
            gl.glRotated(-90d, 0d, 1d, 0d);
            limb.draw(gl, glut, stickFigure, leftArmAnglesAxis, leftArmAnglesBend, Limb.LimbType.LEFT_ARM);
            gl.glPopMatrix();
        }
        gl.glPopMatrix();
    }

    @Deprecated
    public void setLeftLegAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.leftLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.leftLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    @Deprecated
    public void setRightLegAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.rightLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    @Deprecated
    public void setLeftArmAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.leftArmAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.leftArmAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    @Deprecated
    public void setRightArmAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.rightArmAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightArmAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    @Override
    public void setDefaultAnimation(Stance stance) {
        this.defaultStance = stance;
    }

    @Override
    public void playAnimationOnce(Stance stance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playAnimation(Stance stance, int repeats) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
