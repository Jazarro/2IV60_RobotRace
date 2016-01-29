/*
 * TU/e Eindhoven University of Technology
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
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

import Texture.ImplementedTexture;
import bodies.BufferManager;
import bodies.Shape;
import bodies.SimpleBody;
import bodies.SingletonDrawable;
import bodies.StackBuilder;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import robot.Animation;
import robot.RobotBody;

/**
 * Convenience class used by {@link Bender} to draw the torso and head.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Torso implements SingletonDrawable {

    /**
     * The rotation of the torso around the X axis during the running stance.
     */
    private static final int RUNNING_ANGLE = -5;

    /**
     * Radius of the body at the hips.
     */
    private static final float RADIUS_HIPS = 0.175f;
    /**
     * Radius of the body at the chest.
     */
    private static final float RADIUS_TORSO = 0.225f;
    /**
     * Radius of the body at the start of the neck.
     */
    private static final float RADIUS_NECK = 0.125f;
    /**
     * Radius of the body at the end of the neck and the head.
     */
    private static final float RADIUS_HEAD = 0.125f;
    /**
     * Radius of the body at the antenna base.
     */
    private static final float RADIUS_ANTENNA_BOTTOM = 0.05f;
    /**
     * Radius of the body at the bottom of the antenna, just above the base.
     */
    private static final float RADIUS_ANTENNA_MIDDLE = 0.03f;
    /**
     * Radius of the body at the top of the antenna, just below the transmitter.
     */
    private static final float RADIUS_ANTENNA_TOP = 0.01f;
    /**
     * Radius of the body at the center of the transmitter ball.
     */
    private static final float RADIUS_ANTENNA_BALL_MIDDLE = 0.025f;
    /**
     * Radius of the body at the top of the transmitter.
     */
    private static final float RADIUS_ANTENNA_BALL_TOP = 0f;

    /**
     * Height of the body at the hips.
     */
    private static final float HEIGHT_PELVIS = 0f;
    /**
     * Height of the body at the chest.
     */
    private static final float HEIGHT_TORSO = 0.45f;
    /**
     * Height of the body at the start of the head.
     */
    private static final float HEIGHT_NECK = 0.55f;
    /**
     * Height of the body at where the head starts to curve inwards.
     */
    private static final float HEIGHT_HEAD = 0.875f;
    /**
     * Height of the body at the base of the antenna.
     */
    private static final float HEIGHT_ANTENNA_BOTTOM = 1f;
    /**
     * Height of the body at the top of the antenna base.
     */
    private static final float HEIGHT_ANTENNA_MIDDLE = 1.025f;
    /**
     * Height of the body at the top of the antenna, just below the transmitter.
     */
    private static final float HEIGHT_ANTENNA_TOP = 1.175f;
    /**
     * Height of the body at the center of the transmitter.
     */
    private static final float HEIGHT_ANTENNA_BALL_MIDDLE = HEIGHT_ANTENNA_TOP + RADIUS_ANTENNA_BALL_MIDDLE;
    /**
     * Height of the body at the top of the transmitter.
     */
    public static final float HEIGHT_ANTENNA_BALL_TOP = HEIGHT_ANTENNA_BALL_MIDDLE + RADIUS_ANTENNA_BALL_MIDDLE;

    /**
     * The default number of edges to give the rings of the various shapes.
     */
    private static final int SLICE_COUNT = 50;
    /**
     * The default number of rings to use when calculating a partial torus
     * curve.
     */
    private static final int STACK_COUNT = 20;
    /**
     * The absolute distance on the x-axis between the central vertical axis and
     * the position of the legs.
     */
    public static final float LEG_OFFCENTER = 0.1f;
    /**
     * The height of the shoulder relative to the height of the torso.
     */
    public static final float SHOULDER_HEIGHT = 0.375f;
    /**
     * The absolute distance on the x-axis between the central vertical axis and
     * the position of the shoulders.
     */
    public static final float SHOULDER_OFFCENTER = 0.2f;

    /**
     * One or more textures that will be printed on the robots' backs. These
     * must correspond to their back numbers. At least one texture must be
     * given, or an exception will occur during initialisation. Additionally;
     * the textures must be given in order of back-number. The texture at index
     * zero will be used for the zero'th robot, etc.
     */
    private final List<ImplementedTexture> backTextures = new ArrayList<>();
    private SimpleBody torsoBody;
    private Shape back;

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        backTextures.add(new ImplementedTexture(gl, "number1.png", true, false));
        backTextures.add(new ImplementedTexture(gl, "number2.png", true, false));
        backTextures.add(new ImplementedTexture(gl, "number3.png", true, false));
        backTextures.add(new ImplementedTexture(gl, "number4.png", true, false));
        final StackBuilder builder = new StackBuilder(bmInitialiser)
                .setSliceCount(SLICE_COUNT)
                .setTextures(null, null, backTextures.get(0))
                .addConicalFrustum(RADIUS_HIPS, RADIUS_TORSO, HEIGHT_PELVIS, HEIGHT_TORSO, true, false)
                .setTextures(null, null, null)
                .addConicalFrustum(RADIUS_TORSO, RADIUS_NECK, HEIGHT_TORSO, HEIGHT_NECK, false, false)
                .addConicalFrustum(RADIUS_NECK, RADIUS_HEAD, HEIGHT_NECK, HEIGHT_HEAD, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_HEAD, RADIUS_ANTENNA_BOTTOM, HEIGHT_HEAD, HEIGHT_ANTENNA_BOTTOM, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_ANTENNA_BOTTOM, RADIUS_ANTENNA_MIDDLE, HEIGHT_ANTENNA_BOTTOM, HEIGHT_ANTENNA_MIDDLE, false, false)
                .addConicalFrustum(RADIUS_ANTENNA_MIDDLE, RADIUS_ANTENNA_TOP, HEIGHT_ANTENNA_MIDDLE, HEIGHT_ANTENNA_TOP, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_ANTENNA_TOP, RADIUS_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, false, false)
                .addPartialTorus(STACK_COUNT, RADIUS_ANTENNA_BALL_MIDDLE, RADIUS_ANTENNA_BALL_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_BALL_TOP, false, false);
        this.torsoBody = builder.build();
        this.back = builder.getTexturedShapes().get(0);
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
     * @param animation   An instance of animation, containing information about
     *                    at what point in the animation period the torso is at.
     * @param backNumber  The robot's runner number. These don't have to be
     *                    unique, though that'd certainly make more sense. This
     *                    number determines the texture on the robot body's
     *                    back. If no such texture exists, no texture will be
     *                    used for this robot.
     */
    public void draw(GL2 gl, GLUT glut, boolean stickFigure, Animation animation, int backNumber) {
        switch (animation.getCurrentAnimationType()) {
            case RUNNING:
                applyRunningTransformation(gl, animation);
                break;
            default:
                break;
        }
        if (stickFigure) {
            final double bodyHeight = HEIGHT_ANTENNA_BOTTOM - HEIGHT_PELVIS;
            drawStickFigureBody(gl, glut, bodyHeight);
            drawStickFigurePelvis(gl, glut, bodyHeight);
            drawStickFigureShoulders(gl, glut);
        } else {
            back.setTexture(getCurrentTexture(backNumber));
            torsoBody.draw(gl);
        }
        drawEyes(gl, glut);
    }

    /**
     * Gets the currently relevant back texture based on the back number. If the
     * texture for that number doesn't exist, this returns null.
     *
     * @param backNumber The back number for which to find a texture.
     * @return The texture that should be printed on this Torso's back.
     */
    private ImplementedTexture getCurrentTexture(int backNumber) {
        if (backNumber >= 0 && backNumber < backTextures.size()) {
            return backTextures.get(backNumber);
        } else {
            return null;
        }
    }

    private void applyRunningTransformation(GL2 gl, Animation animation) {
        final double fractionInRadians = animation.getLinearInterpolation() * 2f * Math.PI;
        final double bobbingUpAndDownHeight = 0.05f * Math.abs(Math.sin(fractionInRadians));
        gl.glTranslated(0d, 0d, bobbingUpAndDownHeight);
        gl.glRotated(RUNNING_ANGLE, 1d, 0d, 0d);
    }

    private void drawStickFigureBody(GL2 gl, GLUT glut, double bodyHeight) {
        gl.glPushMatrix();
        gl.glScaled(RobotBody.STICK_THICKNESS, RobotBody.STICK_THICKNESS, bodyHeight);
        gl.glTranslated(0d, 0d, bodyHeight / 2f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
    }

    private void drawStickFigurePelvis(GL2 gl, GLUT glut, double bodyHeight) {
        final double pelvisWidth = LEG_OFFCENTER * 2f;
        gl.glPushMatrix();
        gl.glScaled(pelvisWidth, RobotBody.STICK_THICKNESS, RobotBody.STICK_THICKNESS);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
    }

    private void drawStickFigureShoulders(GL2 gl, GLUT glut) {
        final double shoulderWidth = SHOULDER_OFFCENTER * 2f;
        gl.glPushMatrix();
        gl.glTranslated(0d, 0d, SHOULDER_HEIGHT);
        gl.glScaled(shoulderWidth, RobotBody.STICK_THICKNESS, RobotBody.STICK_THICKNESS);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();
    }

    private void drawEyes(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslated(0.05d, 0.125d, 0.8d);
        glut.glutSolidSphere(0.025d, 50, 50);
        gl.glTranslated(-0.1d, 0d, 0d);
        glut.glutSolidSphere(0.025d, 50, 50);
        gl.glPopMatrix();
    }

    /**
     * Adds a transformation to the given GL2 instance. It is assumed that the
     * current coordinate system is based at the torso anchor point. This method
     * will transform to the mounting point of the right leg.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    public void setRightLegMountPoint(GL2 gl) {
        gl.glTranslated(LEG_OFFCENTER, 0d, 0d);
        gl.glRotated(180d, 1d, 0d, 0d);
    }

    /**
     * Adds a transformation to the given GL2 instance. It is assumed that the
     * current coordinate system is based at the torso anchor point. This method
     * will transform to the mounting point of the left leg.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    public void setLeftLegMountPoint(GL2 gl) {
        gl.glTranslated(-LEG_OFFCENTER, 0d, 0d);
        gl.glRotated(180d, 1d, 0d, 0d);
    }

    /**
     * Adds a transformation to the given GL2 instance. It is assumed that the
     * current coordinate system is based at the torso anchor point. This method
     * will transform to the mounting point of the right arm.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    public void setRightArmMountPoint(GL2 gl) {
        gl.glTranslated(SHOULDER_OFFCENTER, 0d, SHOULDER_HEIGHT);
        gl.glRotated(90d, 0d, 1d, 0d);
    }

    /**
     * Adds a transformation to the given GL2 instance. It is assumed that the
     * current coordinate system is based at the torso anchor point. This method
     * will transform to the mounting point of the left arm.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    public void setLeftArmMountPoint(GL2 gl) {
        gl.glTranslated(-SHOULDER_OFFCENTER, 0d, SHOULDER_HEIGHT);
        gl.glRotated(-90d, 0d, 1d, 0d);
    }

}
