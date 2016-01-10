/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

import java.util.List;
import javax.media.opengl.glu.GLU;
import robot.Robot;
import terrain.Terrain;
import static utility.GsUtils.getAzimuth;
import static utility.GsUtils.getInclination;

/**
 * Implementation of a camera with a position and orientation.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Camera {

    /**
     * The position of the camera.
     */
    private Vector eye = new Vector(3f, 6f, 5f);
    /**
     * The point to which the camera is looking.
     */
    private Vector center = Vector.O;
    /**
     * The up vector.
     */
    private Vector up = Vector.Z;

    private float fovAngle;
    private float planeNear;
    private float planeFar;

    private static final Vector HELICOPTER_EYE = new Vector(0d, 0d, 30d);
    private static final Vector HELICOPTER_LOOKAT = new Vector(0d, 0d, 0d);

    private static final Vector MOTORCYCLE_EYE = new Vector(7d, 0d, 1d);
    private static final Vector MOTORCYCLE_LOOKAT = new Vector(0d, 0d, 1d);

    private static final Vector FIRSTPERSON_EYE = new Vector(0d, 0d, 2d);
    private static final Vector FIRSTPERSON_LOOKAT = new Vector(0d, 0d, 1d);

    private static final double SWITCH_TIME = 10d;

    private double tPrevious = 0d;
    private double tAutoSwitch = 0d;
    private double tHelicopterSwitch = 0d;
    private double tMotorCycleSwitch = 0d;
    private double tFirstPersonSwitch = 0d;
    private Robot focusAuto;
    private Robot focusHelicopter;
    private Robot focusMotorCycle;
    private Robot focusFirstPerson;
    private int modeAuto = 0;

    public Vector getEye() {
        return eye.add(new Vector(0, 0, Terrain.TERRAIN_LEVEL));
    }

    public Vector getCenter() {
        return center.add(new Vector(0, 0, Terrain.TERRAIN_LEVEL));
    }

    public void initialize(List<Robot> robots) {
        focusAuto = robots.get(0);
        focusHelicopter = robots.get(0);
        focusMotorCycle = robots.get(0);
        focusFirstPerson = robots.get(0);
    }

    /**
     * Sets the camera's position, focus point and up direction to the given GLU
     * instance.
     *
     * @param glu The GLU instance that will be adjusted.
     */
    public void setLookAt(GLU glu) {
        glu.gluLookAt(getEye().x(), getEye().y(), getEye().z(),
                getCenter().x(), getCenter().y(), getCenter().z(),
                up.x(), up.y(), up.z());
    }

    public void setPerspective(GLU glu, GlobalState gs) {
        // Set the perspective.
        glu.gluPerspective(fovAngle, (float) gs.w / gs.h, planeNear, planeFar);
    }

    /**
     * Updates the camera viewpoint and direction based on the selected camera
     * mode.
     */
    public void update(GlobalState gs, List<Robot> robots) {
        final double deltaTime = gs.tAnim - tPrevious;
        tPrevious = gs.tAnim;
        tHelicopterSwitch += deltaTime;
        if (tHelicopterSwitch >= SWITCH_TIME) {
            tHelicopterSwitch = 0d;
            focusHelicopter = getRandomRobot(robots);
        }
        tMotorCycleSwitch += deltaTime;
        if (tMotorCycleSwitch >= SWITCH_TIME) {
            tMotorCycleSwitch = 0d;
            focusMotorCycle = getRandomRobot(robots);
        }
        tFirstPersonSwitch += deltaTime;
        if (tFirstPersonSwitch >= SWITCH_TIME) {
            tFirstPersonSwitch = 0d;
            focusFirstPerson = getRandomRobot(robots);
        }
        switch (gs.camMode) {

            // Helicopter mode
            case 1:
                setHelicopterMode(gs, focusHelicopter, robots);
                break;

            // Motor cycle mode    
            case 2:
                setMotorCycleMode(gs, focusMotorCycle, robots);
                break;

            // First person mode    
            case 3:
                setFirstPersonMode(gs, focusFirstPerson, robots);
                break;

            // Auto mode    
            case 4:
                setAutoMode(gs, robots, deltaTime);
                break;

            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        //Get the center point from the global state.
        center = gs.cnt;
        //Reset the up vector.
        up = Vector.Z;
        //Calculate the x coordinate of the eye point relative to the center point.
        final double xEyeLocal = Math.cos(getAzimuth(gs)) * Math.cos(getInclination(gs)) * gs.vDist;
        //Calculate the y coordinate of the eye point relative to the center point.
        final double yEyeLocal = Math.sin(getAzimuth(gs)) * Math.cos(getInclination(gs)) * gs.vDist;
        //Calculate the z coordinate of the eye point relative to the center point.
        final double zEyeLocal = Math.sin(getInclination(gs)) * gs.vDist;
        //Create a new vector with the local eye coördinates, IE relative to the center.
        eye = new Vector(xEyeLocal, yEyeLocal, zEyeLocal);
        //Add the relative offet of the center point to the newly calculated coordinates of the eye point.
        eye = eye.add(gs.cnt);
        //Calculate the needed field of view angle to make the displayed portion 
        //of the line through the center point exactly vDist long.
        setFOVAndPlane(gs, center, eye, 40d);//TODO: Fix FOVAngle
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode. The camera
     * should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, Robot focus, List<Robot> robots) {
        up = focus.getDirection();
        eye = focus.getPosition().add(HELICOPTER_EYE);
        center = focus.getPosition().add(HELICOPTER_LOOKAT);
        setFOVAndPlane(gs, center, eye, 40d);
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode. The camera
     * should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, Robot focus, List<Robot> robots) {
        up = Vector.Z;
        eye = addRelative(focus.getPosition(), focus.getDirection(), MOTORCYCLE_EYE);
        center = focus.getPosition().add(MOTORCYCLE_LOOKAT);
        setFOVAndPlane(gs, center, eye, 40d);
    }

    /**
     * Computes eye, center, and up, based on the first person mode. The camera
     * should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus, List<Robot> robots) {
        final Robot robot = getLastRobot(robots);
        if (focus == robot) {
            if (robot == robots.get(0)) {
                focus = robots.get(1);
            } else {
                focus = robots.get(0);
            }
        }
        up = Vector.Z;
        eye = addRelative(robot.getPosition(), robot.getDirection(), FIRSTPERSON_EYE);
        center = addRelative(focus.getPosition(), focus.getDirection(), FIRSTPERSON_LOOKAT);
        setFOVAndPlane(gs, center, eye, 40d);
    }

    /**
     * Computes eye, center, and up, based on the auto mode. The above modes are
     * alternated.
     */
    private void setAutoMode(GlobalState gs, List<Robot> robots, double deltaTime) {
        tAutoSwitch += deltaTime;
        if (tAutoSwitch >= SWITCH_TIME) {
            tAutoSwitch = 0d;
            focusAuto = getRandomRobot(robots);
            modeAuto = (int) Math.floor(Math.random() * 3);
        }
        switch (modeAuto) {
            case 1:
                setHelicopterMode(gs, focusAuto, robots);
                break;
            case 2:
                setMotorCycleMode(gs, focusAuto, robots);
                break;
            case 3:
            default:
                setFirstPersonMode(gs, focusAuto, robots);
                break;
        }
    }

    private void setFOVAndPlane(GlobalState gs, Vector center, Vector eye, double fovAngle) {
        //todo: figure out why calculation below is weird
        if (fovAngle < 1d) {
            this.fovAngle = (float) Math.toDegrees(2d * Math.atan(gs.vDist / (2d * gs.vWidth)));
        } else {
            this.fovAngle = (float) fovAngle;
        }
        final double dist = center.subtract(eye).length();
        planeNear = 0.1f * (float) dist;
        planeFar = 10f * (float) dist;
    }

    private Vector addRelative(Vector position, Vector direction, Vector vector) {
        final Vector normal = direction.cross(Vector.Z).normalized();
        return position
                .add(normal.scale(vector.x()))
                .add(direction.normalized().scale(vector.y()))
                .add(normal.cross(direction).normalized().scale(vector.z()));
    }

    private Robot getLastRobot(List<Robot> robots) {
        int index = 0;
        double distance = robots.get(index).getTrackT();
        for (int i = 0; i < robots.size(); i++) {
            final Robot robot = robots.get(i);
            if (distance > robot.getTrackT()) {
                index = i;
                distance = robot.getTrackT();
            }
        }
        return robots.get(index);
    }

    private Robot getRandomRobot(List<Robot> robots) {
        return robots.get((int) Math.floor(Math.random() * robots.size()));
    }

}
