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
    
    private static final Vector MOTORCYCLE_EYE = new Vector(5d, 0d, 1d);
    private static final Vector MOTORCYCLE_LOOKAT = new Vector(0d, 0d, 1d);

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

        fovAngle = (float) calcFOVAngle(gs);
        planeNear = 0.1f * gs.vDist;
        planeFar = 10f * gs.vDist;
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode. The camera
     * should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, Robot focus) {
        up = focus.getDirection();
        eye = addRelative(focus.getPosition(), HELICOPTER_EYE);
        center = focus.getPosition().add(HELICOPTER_LOOKAT);
        fovAngle = (float) calcFOVAngle(gs);
        final double dist = center.subtract(eye).length();
        planeNear = 0.1f * (float) dist;
        planeFar = 10f * (float) dist;
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode. The camera
     * should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, Robot focus) {
        up = Vector.Z;
        eye = addRelative(focus.getPosition(), MOTORCYCLE_EYE);
        eye = focus.getPosition()
                .add(Vector.Z.cross(focus.getPosition()).normalized().scale(MOTORCYCLE_EYE.x()))
                .add(focus.getPosition().normalized().scale(MOTORCYCLE_EYE.y()))
                .add(Vector.Z.normalized().scale(MOTORCYCLE_EYE.z()));
        center = focus.getPosition().add(MOTORCYCLE_LOOKAT);
        fovAngle = (float) calcFOVAngle(gs);
        final double dist = center.subtract(eye).length();
        planeNear = 0.1f * (float) dist;
        planeFar = 10f * (float) dist;
    }

    /**
     * Computes eye, center, and up, based on the first person mode. The camera
     * should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        // code goes here ...
    }

    /**
     * Computes eye, center, and up, based on the auto mode. The above modes are
     * alternated.
     */
    private void setAutoMode(GlobalState gs, Robot focus) {
        // code goes here ...
    }

    private double calcFOVAngle(GlobalState gs) {
        //todo: figure out why calculation below is weird
        //return Math.toDegrees(2 * Math.atan(gs.vDist / (2 * gs.vWidth)));
        return 40d;
    }
    
    private Vector addRelative(Vector vector1, Vector vector2){
        return vector1
                .add(Vector.Z.cross(vector1).normalized().scale(vector2.x()))
                .add(vector1.normalized().scale(vector2.y()))
                .add(Vector.Z.normalized().scale(vector2.z()));
    }

}
