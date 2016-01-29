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
        viewPrevious = generateCameraView(gs, robots);
        viewNext = generateCameraView(gs, robots);
    }

    /**
     * Updates the camera viewpoint and direction based on the selected camera
     * mode.
     *
     * @param gl
     * @param glu
     * @param gs
     * @param robots
     */
    public void update(GL2 gl, GLU glu, GlobalState gs, List<Robot> robots) {
        final double deltaTime = gs.tAnim - tPrevious;
        tPrevious = gs.tAnim;
        tAutoSwitch += deltaTime;
        if ((tAutoSwitch >= SWITCH_TIME) || (gs.camMode != camModePrevious)) {
            tAutoSwitch = 0d;
            camModePrevious = gs.camMode;
            viewPrevious = viewNext;
            viewNext = generateCameraView(gs, robots);
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

    private CameraView generateCameraView(GlobalState gs, List<Robot> robots) {
        return new CameraView(gs, robots, robots.get((int) Math.floor(Math.random() * robots.size())), ((int) Math.floor(Math.random() * 3d)) + 1);
    }

}
