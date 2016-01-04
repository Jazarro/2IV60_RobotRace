/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

import robot.Robot;
import javax.media.opengl.glu.GLU;

import static utility.GsUtils.getAzimuth;
import static utility.GsUtils.getInclination;

/**
 * Implementation of a camera with a position and orientation.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
class Camera {

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

    private static final Vector HELICOPTER_EYE = new Vector(0d, 0d, 10d);
    private static final Vector HELICOPTER_LOOKAT = new Vector(0d, 0d, 0d);

    private static final Vector MOTORCYCLE_EYE = new Vector(7d, 0d, 1d);
    private static final Vector MOTORCYCLE_LOOKAT = new Vector(0d, 0d, 1d);

    private static final Vector FIRSTPERSON_EYE = new Vector(0d, 0d, 2d);
    private static final Vector FIRSTPERSON_LOOKAT = new Vector(0d, 2d, 2d);

    /**
     * Sets the camera's position, focus point and up direction to the given GLU
     * instance.
     *
     * @param glu The GLU instance that will be adjusted.
     */
    public void setLookAt(GLU glu) {
        glu.gluLookAt(eye.x(), eye.y(), eye.z(),
                center.x(), center.y(), center.z(),
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
    public void update(GlobalState gs, Robot focus) {
        switch (gs.camMode) {

            // Helicopter mode
            case 1:
                setHelicopterMode(gs, focus);
                break;

            // Motor cycle mode    
            case 2:
                setMotorCycleMode(gs, focus);
                break;

            // First person mode    
            case 3:
                setFirstPersonMode(gs, focus);
                break;

            // Auto mode    
            case 4:
                setAutoMode(gs, focus);
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

        setFOVAndPlane(gs, center, eye);
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode. The camera
     * should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, Robot focus) {
        up = focus.getDirection();
        eye = focus.getPosition().add(HELICOPTER_EYE);
        center = focus.getPosition().add(HELICOPTER_LOOKAT);
        setFOVAndPlane(gs, center, eye);
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode. The camera
     * should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, Robot focus) {
        up = Vector.Z;
        eye = addRelative(focus.getPosition(), focus.getDirection(), MOTORCYCLE_EYE);
        center = focus.getPosition().add(MOTORCYCLE_LOOKAT);
        setFOVAndPlane(gs, center, eye);
    }

    /**
     * Computes eye, center, and up, based on the first person mode. The camera
     * should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        up = Vector.Z;
        eye = addRelative(focus.getPosition(), focus.getDirection(), FIRSTPERSON_EYE);
        //center = focus.getPosition().add(FIRSTPERSON_LOOKAT);
        center = addRelative(focus.getPosition(), focus.getDirection(), FIRSTPERSON_LOOKAT);
        setFOVAndPlane(gs, center, eye);
    }

    /**
     * Computes eye, center, and up, based on the auto mode. The above modes are
     * alternated.
     */
    private void setAutoMode(GlobalState gs, Robot focus) {
        // code goes here ...
    }

    private void setFOVAndPlane(GlobalState gs, Vector center, Vector eye) {
        //todo: figure out why calculation below is weird
        //fovAngle = Math.toDegrees(2 * Math.atan(gs.vDist / (2 * gs.vWidth)));
        fovAngle = (float) 40d;
        final double dist = center.subtract(eye).length();
        planeNear = 0.1f * (float) dist;
        planeFar = 10f * (float) dist;
    }

    private Vector addRelative(Vector position, Vector direction, Vector vector) {
        return position
                .add(direction.cross(Vector.Z).normalized().scale(vector.x()))
                .add(direction.normalized().scale(vector.y()))
                .add(Vector.Z.normalized().scale(vector.z()));
    }

}
