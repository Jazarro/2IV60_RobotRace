/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot.bender;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import robot.Animation;
import static robot.bender.Limb.RING_COUNT;

/**
 *
 * @author Arjan Boschman
 */
public class Leg {

    @Deprecated//Just for testing.
    private static final double HEIGHT_OUTER_SEGMENT = 0.5d / 6d;

    private final Limb limb;
    private final float animationPeriodOffset;

    private final double[] leftLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesBend = new double[Limb.RING_COUNT + 1];

    public Leg(Limb limb, float animationPeriodOffset) {
        this.limb = limb;
        this.animationPeriodOffset = animationPeriodOffset;
    }

    public void draw(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        final float fraction = animation.getLinearInterpolation(animationPeriodOffset);
        
    }

    @Deprecated
    private void something(GL2 gl, GLUT glut, boolean stickFigure, float tAnim) {
        double currAngleAxis;
        double currAngleBend;
        double newPos[] = {0d, 0d, 0d};
        for (int i = 0; i < RING_COUNT; i++) {
            gl.glPushMatrix();
            currAngleAxis = (Limb.LimbType.LEFT_LEG.isRight()) ? (-leftLegAnglesAxis[i]) : (leftLegAnglesAxis[i]);
            currAngleBend = leftLegAnglesBend[i];
            gl.glTranslated(newPos[0], newPos[1], newPos[2]);
            gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
            newPos = nextPos(newPos, HEIGHT_OUTER_SEGMENT, currAngleBend, currAngleAxis);
            limb.drawSegment(gl, glut, stickFigure);
            gl.glPopMatrix();
        }
    }

    @Deprecated//Just for testing.
    private double[] nextPos(double[] currPos, double height, double currAngleBend, double currAngleAxis) {
        currPos[0] -= height * Math.sin(Math.toRadians(currAngleBend)) * Math.sin(Math.toRadians(currAngleAxis));
        currPos[1] += height * Math.sin(Math.toRadians(currAngleBend)) * Math.cos(Math.toRadians(currAngleAxis));
        currPos[2] -= height * Math.cos(Math.toRadians(currAngleBend));
        return currPos;
    }

    @Deprecated
    public void setRightLegAngles(double[] anglesAxis, double[] anglesBend) {
        System.arraycopy(anglesAxis, 0, this.rightLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

}
