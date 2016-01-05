/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
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
     * @param material The material this robot consists of, used for lighting
     *                 calculations.
     *
     * @return A newly created instance of Robot.
     */
    public Robot makeBender(Material material) {
        return new Robot(material, makeBenderBody());
    }

    /**
     * Constructs a new instance of Robot using the given material.
     *
     * This method allows one to set the initial position and direction. Note
     * that this is for showcasing purposes only; normally those values are
     * overwritten every game tick. For regular use, consider using
     * {@link #makeBender(robotrace.Material)}.
     *
     * @param material  The material this robot consists of, used for lighting
     *                  calculations.
     * @param position  The initial position of this robot relative to the
     *                  world.
     * @param direction The initial direction of this robot, IE which way it's
     *                  facing. This is relative to the global axis system.
     *
     * @return A newly created instance of Robot.
     *
     * @see #makeBender(Material)
     */
    public Robot makeBenderAt(Material material, Vector position, Vector direction) {
        final Robot robot = new Robot(material, RobotFactory.this.makeBenderBody());
        robot.setPosition(position);
        robot.setDirection(direction);
        return robot;
    }

    private Bender makeBenderBody() {
        final Leg rightLeg = new Leg(benderLimb, 0F);
        final Leg leftLeg = new Leg(benderLimb, 0.5F);
        final Arm rightArm = new Arm(benderLimb, 0.5F, Vector.Y, Vector.Z.scale(-1));
        final Arm leftArm = new Arm(benderLimb, 0F, Vector.Y.scale(-1), Vector.Z);
        final Animation animation = new Animation();
        animation.addAnimationType(AnimationType.RUNNING, 0.8F);
        return new Bender(animation, benderTorso, rightLeg, leftLeg, rightArm, leftArm);
    }

}
