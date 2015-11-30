package robotrace.bender;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import robotrace.Vector;

/**
 * An instance of GLRobotBody, this class describes how to build and draw a
 * robot body resembling the character 'Bender' from the popular cartoon
 * Futurama.
 *
 * @author Robke Geenen
 * @author Arjan Boschman
 */
public class Bender implements GLRobotBody {

    /**
     * The orientation of the robot body as a unit vector. Use this together
     * with the robots dynamic orientation to get the angle and axis needed to
     * perform the OpenGL rotation.
     */
    public static final Vector ORIENTATION = new Vector(0, 1, 0);

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

    private final BenderBody body;
    private final Limb limb;

    private final double[] leftLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesBend = new double[Limb.RING_COUNT + 1];

    public Bender() {
        body = new BenderBody();
        limb = new Limb();
    }

    @Override
    public void initialize(GL2 gl) {
        body.initialize(gl);
        limb.initialize(gl);
        setLeftLegAngles(new double[]{45d, 45d, 45d, 45d, 45d, 45d, 45d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        setRightLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        setLeftArmAngles(new double[]{45d, 45d, 45d, 45d, 45d, 45d, 45d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        setRightArmAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
    }

    @Override
    public void draw(GL2 gl, GLUT glut) {
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
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
            body.draw(gl);
            gl.glPopMatrix();

            //Draws the eyes:
            gl.glPushMatrix();
            gl.glTranslated(0.05d, -0.125d, 0.8d);
            glut.glutSolidSphere(0.025d, 50, 50);
            gl.glTranslated(-0.1d, 0d, 0d);
            glut.glutSolidSphere(0.025d, 50, 50);
            gl.glPopMatrix();

            //Draws the left leg and foot.
            gl.glPushMatrix();
            gl.glTranslated(-LEG_OFFCENTER, 0d, 0d);
            limb.draw(gl, leftLegAnglesAxis, leftLegAnglesBend, Limb.LimbType.LEFT_LEG);
            gl.glPopMatrix();

            //Draws the right leg and foot.
            gl.glPushMatrix();
            gl.glTranslated(LEG_OFFCENTER, 0d, 0d);
            limb.draw(gl, rightLegAnglesAxis, rightLegAnglesBend,Limb.LimbType.RIGHT_LEG);
            gl.glPopMatrix();

            //Draws the left arm and hand.
            gl.glPushMatrix();
            gl.glTranslated(-SHOULDER_OFFCENTER, 0d, SHOULDER_HEIGHT);
            gl.glRotated(90d, 0d, 1d, 0d);
            limb.draw(gl, leftArmAnglesAxis, leftArmAnglesBend,Limb.LimbType.RIGHT_ARM);
            gl.glPopMatrix();

            //Draws the right arm and hand.
            gl.glPushMatrix();
            gl.glTranslated(SHOULDER_OFFCENTER, 0d, SHOULDER_HEIGHT);
            gl.glRotated(-90d, 0d, 1d, 0d);
            limb.draw(gl, rightArmAnglesAxis, rightArmAnglesBend, Limb.LimbType.LEFT_ARM);
            gl.glPopMatrix();
        }
        gl.glPopMatrix();
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    /**
     * 
     * @param anglesAxis
     * @param anglesBend 
     */
    public void setLeftLegAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.leftLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.leftLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    public void setRightLegAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.rightLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    public void setLeftArmAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.leftArmAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.leftArmAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    public void setRightArmAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.rightArmAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightArmAnglesBend, 0, Limb.RING_COUNT + 1);
    }

}
