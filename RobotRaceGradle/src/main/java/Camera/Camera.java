/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package Camera;

import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import robot.Robot;
import robotrace.GlobalState;
import robotrace.Vector;

/**
 * Implementation of a camera with a position and orientation.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Camera {

    private CameraView viewPrevious;
    private CameraView viewNext;
    private CameraMode modeCurrent;

    private static final double SWITCH_TIME = 10d;
    private static final double SWITCH_TRANSIT_TIME = 4d;

    private double tPrevious = 0d;
    private double tAutoSwitch = 0d;
    private int camModePrevious = 0;

    public void initialize(GlobalState gs, List<Robot> robots) {
        viewPrevious = new CameraView(gs, robots, robots.get((int) Math.floor(Math.random() * robots.size())), (int) Math.floor(Math.random() * 3));
        viewNext = new CameraView(gs, robots, robots.get((int) Math.floor(Math.random() * robots.size())), (int) Math.floor(Math.random() * 3));
    }

    /**
     * Updates the camera viewpoint and direction based on the selected camera
     * mode.
     */
    public void update(GL2 gl, GLU glu, GlobalState gs, List<Robot> robots) {
        final double deltaTime = gs.tAnim - tPrevious;
        tPrevious = gs.tAnim;
        tAutoSwitch += deltaTime;
        if ((tAutoSwitch >= SWITCH_TIME) || (gs.camMode != camModePrevious)) {
            tAutoSwitch = 0d;
            camModePrevious = gs.camMode;
            viewPrevious = viewNext;
            viewNext = new CameraView(gs, robots, robots.get((int) Math.floor(Math.random() * robots.size())), ((int) Math.floor(Math.random() * 3d)) + 1);
        }
        viewPrevious.update(gs, robots);
        viewNext.update(gs, robots);
        if (tAutoSwitch <= SWITCH_TRANSIT_TIME) {
            final double transit = tAutoSwitch / SWITCH_TRANSIT_TIME;
            modeCurrent = CameraMode.interpolateMode(viewPrevious.getCameraMode(), viewNext.getCameraMode(), transit);
        } else {
            modeCurrent = viewNext.getCameraMode();
        }
        modeCurrent.setView(gl, glu, gs);
    }

    public Vector getCamPos() {
        return modeCurrent.getEye();
    }

}
