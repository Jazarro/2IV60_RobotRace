package robotrace;

/**
 * Implementation of a camera with a position and orientation.
 */
class Camera {

    /**
     * The position of the camera.
     */
    public Vector eye = new Vector(3f, 6f, 5f);

    /**
     * The point to which the camera is looking.
     */
    public Vector center = Vector.O;

    /**
     * The up vector.
     */
    public Vector up = Vector.Z;

    private float fovAngle = 40f;

    public float getFovAngle() {
        return fovAngle;
    }

    public void setFovAngle(float fovAngle) {
        this.fovAngle = fovAngle;
    }

    /**
     * Convenience method that retrieves the azimuth angle from the given
     * GlobalState.
     *
     * @param gs
     * @return
     */
    //Get the azimuth angle from the global state.
    public float getAzimuth(GlobalState gs) {
        return gs.theta;
    }

    //Set the azimuth angle to the global state.
    public void setAzimuth(GlobalState gs, float azimuth) {
        gs.theta = azimuth;
    }

    //Get the inclination angle from the global state.
    public float getInclination(GlobalState gs) {
        return gs.phi;
    }

    //Set the inclination angle to the global state.
    public void setInclination(GlobalState gs, float inclination) {
        gs.phi = inclination;
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
        //Calculate the x coordinate of the eye point relative to the center point.
        eye.x = Math.cos(getAzimuth(gs)) * Math.cos(getInclination(gs)) * gs.vDist;
        //Calculate the y coordinate of the eye point relative to the center point.
        eye.y = Math.sin(getAzimuth(gs)) * Math.cos(getInclination(gs)) * gs.vDist;
        //Calculate the z coordinate of the eye point relative to the center point.
        eye.z = Math.sin(getInclination(gs)) * gs.vDist;
        //Add the (possible) relative offet of the center point to the newly calculated coordinates of the eye point.
        eye = eye.add(gs.cnt);
        //Calculate the needed field of view angle to make the displayed portion of the line throughthe center point exactly vDist long.
        fovAngle = 40;
        //fovAngle = (float) Math.toDegrees(2 * Math.atan(gs.vWidth / (2 * gs.vDist)));
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode. The camera
     * should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, Robot focus) {
        // code goes here ...
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode. The camera
     * should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, Robot focus) {
        // code goes here ...
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

}
