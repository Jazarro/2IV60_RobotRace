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
import robot.AnimationType;
import robot.RobotBody;
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
     * Meters bridged per animation period in seconds. Can be used to calculate
     * the proper animation period for a given velocity in meters per second.
     */
    public static final float ANIM_RUNNING_CONSTANT = 1.3333F;

    private final Animation animation;
    private final Torso torso;
    private final Leg rightLeg;
    private final Leg leftLeg;
    private final Arm rightArm;
    private final Arm leftArm;
    private final int backNumber;

    public Bender(Animation animation, Torso torso, Leg rightLeg, Leg leftLeg, Arm rightArm, Arm leftArm, int backNumber) {
        this.animation = animation;
        this.torso = torso;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.backNumber = backNumber;
    }

    @Override
    public void draw(GL2 gl, GLUT glut, boolean stickFigure, float tAnim) {
        animation.update(tAnim);
        gl.glPushMatrix();
        {
            final double legHeight = Limb.HEIGHT_OUTER_SEGMENT * Limb.RING_COUNT + Limb.HEIGHT_FOOT;
            gl.glTranslated(0d, 0d, legHeight);

            torso.draw(gl, glut, stickFigure, animation, backNumber);

            gl.glPushMatrix();
            {
                torso.setRightLegMountPoint(gl);
                rightLeg.draw(gl, glut, stickFigure, animation);
            }
            gl.glPopMatrix();
            gl.glPushMatrix();
            {
                torso.setLeftLegMountPoint(gl);
                leftLeg.draw(gl, glut, stickFigure, animation);
            }
            gl.glPopMatrix();
            gl.glPushMatrix();
            {
                torso.setRightArmMountPoint(gl);
                rightArm.draw(gl, glut, stickFigure, animation);
            }
            gl.glPopMatrix();
            gl.glPushMatrix();
            {
                torso.setLeftArmMountPoint(gl);
                leftArm.draw(gl, glut, stickFigure, animation);
            }
            gl.glPopMatrix();
        }
        gl.glPopMatrix();
    }

    @Override
    public void setDefaultAnimation(AnimationType animationType) {
        animation.setDefaultAnimationType(animationType);
    }

    @Override
    public void playAnimationOnce(AnimationType animationType) {
        animation.playAnimationOnce(animationType);
    }

    @Override
    public void playAnimation(AnimationType animationType, int repeats) {
        animation.playAnimation(animationType, repeats);
    }

}
