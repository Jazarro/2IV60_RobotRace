/*
 * TU/e Eindhoven University of Technology
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * 
 * This code is based on 6 template classes, as well as the RobotRaceLibrary. 
 * Both were provided by the course tutor, currently prof.dr.ir. 
 * J.J. (Jack) van Wijk. (e-mail: j.j.v.wijk@tue.nl)
 * 
 * Copyright (C) 2015 Arjan Boschman, Robke Geenen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package robot.bender;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import robot.Animation;

/**
 *
 * @author Arjan Boschman
 */
public class Leg {

    private static final float HIP_PERIOD_OFFSET = 1f / 12f;
    public static final int NR_HIP_JOINTS = 2;
    public static final int NR_KNEE_JOINTS = 3;
    public static final int NR_ANKLE_JOINTS = 1;

    private final Limb limb;
    private final float animationPeriodOffset;

    public Leg(Limb limb, float animationPeriodOffset) {
        this.limb = limb;
        this.animationPeriodOffset = animationPeriodOffset;
    }

    public void draw(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        gl.glPushMatrix();
        drawHips(gl, glut, stickFigure, animation);
        drawKnees(gl, glut, stickFigure, animation);
        drawAnkles(gl, glut, stickFigure, animation);
        gl.glPopMatrix();
    }

    private void drawHips(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        for (int i = 0; i < NR_HIP_JOINTS; i++) {
            gl.glRotated(getPartialHipAngle(animation), -1d, 0d, 0d);
            limb.drawSegment(gl, glut, stickFigure);
            gl.glTranslated(0d, 0d, Limb.HEIGHT_OUTER_SEGMENT);
        }
    }

    private float getPartialHipAngle(Animation animation) {
        switch (animation.getCurrentAnimationType()) {
            case RUNNING:
                final float hipFraction = animation.getLinearInterpolation(animationPeriodOffset + HIP_PERIOD_OFFSET);
                final float hipJointAngle = 26f - 48f - 48f * (float) Math.sin(hipFraction * 2f * Math.PI);
                return hipJointAngle / NR_HIP_JOINTS;
            case IDLE:
            default:
                return 0F;
        }
    }

    private void drawKnees(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        for (int i = 0; i < NR_KNEE_JOINTS; i++) {
            gl.glRotated(getPartialKneeAngle(animation), -1d, 0d, 0d);
            limb.drawSegment(gl, glut, stickFigure);
            gl.glTranslated(0d, 0d, Limb.HEIGHT_OUTER_SEGMENT);
        }
    }

    private float getPartialKneeAngle(Animation animation) {
        switch (animation.getCurrentAnimationType()) {
            case RUNNING:
                final float kneeFraction = animation.getLinearInterpolation(animationPeriodOffset);
                final float fractionInRadians = (float) (kneeFraction * 2f * Math.PI);
                final float kneeJointAngle = 25f + 36.6f + 12.2f
                        * (float) ((Math.cos(fractionInRadians) * 4f + Math.cos(fractionInRadians * 1f) * 2f));
                return kneeJointAngle / NR_KNEE_JOINTS;
            case IDLE:
            default:
                return 0f;
        }
    }

    private void drawAnkles(GL2 gl, GLUT glut, boolean stickFigure, Animation animation) {
        for (int i = 0; i < NR_ANKLE_JOINTS + 1; i++) {
            gl.glRotated(getPartialAnkleAngle(animation), -1d, 0d, 0d);
            if (i == NR_ANKLE_JOINTS) {
                limb.drawFoot(gl, stickFigure);
            } else {
                limb.drawSegment(gl, glut, stickFigure);
                gl.glTranslated(0d, 0d, Limb.HEIGHT_OUTER_SEGMENT);
            }
        }
    }

    private float getPartialAnkleAngle(Animation animation) {
        switch (animation.getCurrentAnimationType()) {
            case IDLE:
            default:
                return 0f;
        }
    }

}
