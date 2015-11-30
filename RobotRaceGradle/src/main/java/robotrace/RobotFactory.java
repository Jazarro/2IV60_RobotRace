package robotrace;

import javax.media.opengl.GL2;
import robotrace.bender.Bender;
import robotrace.bender.GLRobotBody;

/**
 *
 * @author Arjan Boschman
 */
public class RobotFactory {

    private final GLRobotBody robotBody;

    public RobotFactory() {
        robotBody = new Bender();
    }

    /**
     * Initialises the 3D bodies maintained by this factory. This prepares the
     * given GL2 instance for drawing the bodies.
     *
     * @param gl An instance of GL2, needed to do the proper initialisation.
     */
    public void initialize(GL2 gl) {
        robotBody.initialize(gl);
    }

    /**
     * Constructs a new instance of Robot using the given material.
     *
     * @param material The material this robot consists of, used for lighting
     *                 calculations.
     * @return A newly created instance of Robot.
     */
    public Robot makeRobot(Material material) {
        return new Robot(material, robotBody);
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
        final Robot robot = new Robot(material, robotBody);
        robot.setPosition(position);
        robot.setDirection(direction);
        return robot;
    }

}
