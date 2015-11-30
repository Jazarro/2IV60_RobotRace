package robotrace;

import javax.media.opengl.GL2;
import robotrace.bender.Bender;

/**
 *
 * @author Arjan Boschman
 */
public class RobotFactory {

    private final Bender bender;

    public RobotFactory() {
        bender = new Bender();
    }

    /**
     * Initialises the 3D bodies maintained by this factory. This prepares the
     * given GL2 instance for drawing the bodies.
     *
     * @param gl An instance of GL2, needed to do the proper initialisation.
     */
    public void initialize(GL2 gl) {
        bender.initialize(gl);
    }

    /**
     * Constructs a new instance of Robot using the given material.
     *
     * @param material The material this robot consists of, used for lighting
     *                 calculations.
     * @return A newly created instance of Robot.
     */
    public Robot makeRobot(Material material) {
        return new Robot(material, bender);
    }

    /**
     * Constructs a new instance of Robot using the given material.
     *
     * This method allows one to set the initial position and direction. Note
     * that this is for showcasing purposes only; normally those values are
     * overwritten every game tick. For regular use, consider using
     * {@link #makeRobot(robotrace.Material)}.
     *
     * @param material  The material this robot consists of, used for lighting
     *                  calculations.
     * @param position  The initial position of this robot relative to the
     *                  world.
     * @param direction The initial direction of this robot, IE which way it's
     *                  facing. This is relative to the global axis system.
     * @return A newly created instance of Robot.
     * @see #makeRobot(Material)
     */
    public Robot makeRobotAt(Material material, Vector position, Vector direction) {
        final Robot robot = new Robot(material, bender);
        robot.setPosition(position);
        robot.setDirection(direction);
        return robot;
    }

    /**
     * Constructs a new instance of Robot using the given material and robot
     * type.
     *
     * @param material  The material this robot consists of, used for lighting
     *                  calculations.
     * @param robotType The type of body this robot has. For now, this doesn't
     *                  actually do anything, the only defined robot type is
     *                  bender.
     * @deprecated Will likely change shape or disappear altogether in the
     * future.
     * @return A newly created instance of Robot.
     */
    @Deprecated
    public Robot makeRobot(Material material, int robotType) {
        return new Robot(material, robotType, bender);
    }

}
