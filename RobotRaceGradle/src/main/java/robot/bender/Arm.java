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
import robotrace.Vector;

/**
 *
 * @author Arjan Boschman
 */
public class Arm {

    public static final int NR_SHOULDER_JOINTS = 2;
    public static final int NR_ELBOW_JOINTS = 3;
    public static final int NR_WRIST_JOINTS = 1;

    private final Limb limb;
    private final float animationPeriodOffset;
    private final Vector verticalTurningAxis;
    private final Vector horizontalTurningAxis;

    public Arm(Limb limb, float animationPeriodOffset, Vector verticalTurningAxis, Vector horizontalTurningAxis) {
        this.limb = limb;
        this.animationPeriodOffset = animationPeriodOffset;
        this.verticalTurningAxis = verticalTurningAxis;
        this.horizontalTurningAxis = horizontalTurningAxis;
    }

    public void draw(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        gl.glPushMatrix();
        drawShoulders(gl, glut, stickFigure, animation);
        drawElbows(gl, glut, stickFigure, animation);
        drawWrists(gl, glut, stickFigure, animation);
        gl.glPopMatrix();
    }

    private void drawShoulders(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        for (int i = 0; i < NR_SHOULDER_JOINTS; i++) {
            gl.glRotated(getPartialShoulderAngle(animation), horizontalTurningAxis.x(), horizontalTurningAxis.y(), horizontalTurningAxis.z());
            gl.glRotated(45, verticalTurningAxis.x(), verticalTurningAxis.y(), verticalTurningAxis.z());
            limb.drawSegment(gl, glut, stickFigure);
            gl.glTranslated(0, 0, Limb.HEIGHT_OUTER_SEGMENT);
        }
    }

    private float getPartialShoulderAngle(Animation animation) {
        switch (animation.getCurrentAnimationType()) {
            case RUNNING:
                final float fraction = animation.getLinearInterpolation(animationPeriodOffset);
                final float fractionInRadians = (float) (fraction * 2 * Math.PI);
                final float jointAngle = 28 + -60 * (float) Math.sin(fractionInRadians);
                return jointAngle / NR_SHOULDER_JOINTS;
            case IDLE:
            default:
                return 0F;
        }
    }

    private void drawElbows(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        for (int i = 0; i < NR_ELBOW_JOINTS; i++) {
            gl.glRotated(getPartialElbowAngle(animation), 1, 0, 0.7 * horizontalTurningAxis.z());
            limb.drawSegment(gl, glut, stickFigure);
            gl.glTranslated(0, 0, Limb.HEIGHT_OUTER_SEGMENT);
        }
    }

    private float getPartialElbowAngle(Animation animation) {
        switch (animation.getCurrentAnimationType()) {
            case RUNNING:
                final float fraction = animation.getLinearInterpolation(animationPeriodOffset);
                final float fractionInRadians = (float) (fraction * 2 * Math.PI);
                final float jointAngle = -90 + 20 * (float) Math.sin(fractionInRadians);
                return jointAngle / NR_SHOULDER_JOINTS;
            case IDLE:
            default:
                return 0F;
        }
    }

    private void drawWrists(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        for (int i = 0; i < NR_WRIST_JOINTS + 1; i++) {
            gl.glRotated(getPartialWristAngle(animation), 0.1, 0.5 * horizontalTurningAxis.z(), 0);
            if (i == NR_WRIST_JOINTS) {
                limb.drawHand(gl, glut, stickFigure);
            } else {
                limb.drawSegment(gl, glut, stickFigure);
                gl.glTranslated(0, 0, Limb.HEIGHT_OUTER_SEGMENT);
            }
        }
    }

    private float getPartialWristAngle(Animation animation) {
        switch (animation.getCurrentAnimationType()) {
            case RUNNING:
                final float fraction = animation.getLinearInterpolation(animationPeriodOffset);
                final float fractionInRadians = (float) (fraction * 2 * Math.PI);
                final float jointAngle = -30 + -35 * (float) Math.sin(fractionInRadians);
                return jointAngle / (NR_WRIST_JOINTS + 1);
            case IDLE:
            default:
                return 0F;
        }
    }

}
