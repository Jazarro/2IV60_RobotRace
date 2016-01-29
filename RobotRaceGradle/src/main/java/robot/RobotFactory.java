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
package robot;

import bodies.BufferManager;
import javax.media.opengl.GL2;
import robot.bender.Arm;
import robot.bender.Bender;
import robot.bender.Leg;
import robot.bender.Limb;
import robot.bender.Torso;
import robotrace.Material;
import robotrace.Vector;

/**
 * Can be used to obtain instances of robots. Is also responsible for creating
 * and maintaining instances of singleton robot parts.
 *
 * @author Arjan Boschman
 */
public class RobotFactory {

    private final robot.bender.Limb benderLimb;
    private final robot.bender.Torso benderTorso;

    public RobotFactory() {
        this.benderLimb = new Limb();
        this.benderTorso = new Torso();
    }

    /**
     * Initialises the 3D bodies maintained by this factory. This prepares the
     * given GL2 instance for drawing the bodies.
     *
     * @param gl            An instance of GL2, needed to do the proper
     *                      initialisation.
     * @param bmInitialiser The BodyManager.Initialiser that is used to gather
     *                      all the data before communicating it to OpenGL.
     */
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        benderLimb.initialize(gl, bmInitialiser);
        benderTorso.initialize(gl, bmInitialiser);
    }

    /**
     * Constructs a new instance of Robot using the given material.
     *
     * @param backNumber The robot's runner number. These don't have to be
     *                   unique, though that'd certainly make more sense. This
     *                   number determines the texture on the robot body's back.
     *                   If no such texture exists, no texture will be used for
     *                   this robot.
     * @param material   The material this robot consists of, used for lighting
     *                   calculations.
     *
     * @return A newly created instance of Robot.
     */
    public Robot makeBender(int backNumber, Material material) {
        return new Robot(material, makeBenderBody(backNumber));
    }

    /**
     * Constructs a new instance of Robot using the given material.
     *
     * This method allows one to set the initial position and direction. Note
     * that this is for showcasing purposes only; normally those values are
     * overwritten every game tick. For regular use, consider using
     * {@link #makeBender(robotrace.Material)}.
     *
     * @param backNumber The robot's runner number. These don't have to be
     *                   unique, though that'd certainly make more sense. This
     *                   number determines the texture on the robot body's back.
     *                   If no such texture exists, no texture will be used for
     *                   this robot.
     * @param material   The material this robot consists of, used for lighting
     *                   calculations.
     * @param position   The initial position of this robot relative to the
     *                   world.
     * @param direction  The initial direction of this robot, IE which way it's
     *                   facing. This is relative to the global axis system.
     *
     * @return A newly created instance of Robot.
     *
     * @see #makeBender(Material)
     */
    public Robot makeBenderAt(int backNumber, Material material, Vector position, Vector direction) {
        final Robot robot = new Robot(material, RobotFactory.this.makeBenderBody(backNumber));
        robot.setPosition(position);
        robot.setDirection(direction);
        return robot;
    }

    /**
     * Create a body for bender.
     *
     * @param backNumber The robot's runner number. These don't have to be
     *                   unique, though that'd certainly make more sense. This
     *                   number determines the texture on the robot body's back.
     *                   If no such texture exists, no texture will be used for
     *                   this robot.
     * @return The newly created body of bender.
     */
    private Bender makeBenderBody(int backNumber) {
        final Leg rightLeg = new Leg(benderLimb, 0f);
        final Leg leftLeg = new Leg(benderLimb, 0.5f);
        final Arm rightArm = new Arm(benderLimb, 0.5f, Vector.Y, Vector.Z.scale(-1d));
        final Arm leftArm = new Arm(benderLimb, 0f, Vector.Y.scale(-1d), Vector.Z);
        final Animation animation = new Animation();
        animation.addAnimationType(AnimationType.RUNNING, Bender.ANIM_RUNNING_CONSTANT / 1.5f);
        animation.setDefaultAnimation(AnimationType.RUNNING);
        return new Bender(animation, benderTorso, rightLeg, leftLeg, rightArm, leftArm, backNumber);
    }

}
