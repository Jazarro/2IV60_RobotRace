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
package Camera;

import java.util.List;
import robot.Robot;
import robotrace.GlobalState;
import robotrace.Vector;
import static utility.GsUtils.getAzimuth;
import static utility.GsUtils.getInclination;

public class CameraView {

    private static final Vector HELICOPTER_EYE = new Vector(0d, 0d, 30d);
    private static final Vector HELICOPTER_LOOKAT = new Vector(0d, 0d, 0d);

    private static final Vector MOTORCYCLE_EYE = new Vector(5.5d, 0d, 1d);
    private static final Vector MOTORCYCLE_LOOKAT = new Vector(0d, 0d, 1d);

    private static final Vector FIRSTPERSON_EYE = new Vector(0d, 0d, 2d);
    private static final Vector FIRSTPERSON_LOOKAT = new Vector(0d, 0d, 1d);

    private List<Robot> robots;
    private final int robotFocus;
    private final int robotEye;
    private final int camMode;
    private CameraMode cameraMode;

    public CameraView(GlobalState gs, List<Robot> robots, Robot robotFocus, int camMode) {
        if (gs.camMode < 4) {
            this.camMode = gs.camMode;
        } else {
            this.camMode = camMode;
        }
        this.robots = robots;
        this.robotFocus = robots.indexOf(robotFocus);
        this.robotEye = getLastRobot(robots);
        update(gs, robots);
    }

    public CameraMode getCameraMode() {
        return cameraMode;
    }

    public final void update(GlobalState gs, List<Robot> robots) {
        this.robots = robots;
        switch (this.camMode) {
            case 1:
                setHelicopterMode(gs);
                break;
            case 2:
                setMotorCycleMode(gs);
                break;
            case 3:
                setFirstPersonMode(gs);
                break;
            case 0:
            case 4:
            default:
                setDefaultMode(gs);
                break;
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     *
     * @param gs
     */
    public void setDefaultMode(GlobalState gs) {
        //Calculate the x coordinate of the eye point relative to the center point.
        final double xEyeLocal = Math.cos(getAzimuth(gs)) * Math.cos(getInclination(gs)) * gs.vDist;
        //Calculate the y coordinate of the eye point relative to the center point.
        final double yEyeLocal = Math.sin(getAzimuth(gs)) * Math.cos(getInclination(gs)) * gs.vDist;
        //Calculate the z coordinate of the eye point relative to the center point.
        final double zEyeLocal = Math.sin(getInclination(gs)) * gs.vDist;
        //Create a new vector with the local eye coördinates, IE relative to the center.
        //Add the relative offet of the center point to the newly calculated coordinates of the eye point.
        final Vector eye = new Vector(xEyeLocal, yEyeLocal, zEyeLocal).add(gs.cnt);
        //Get the center point from the global state.
        final Vector center = gs.cnt;
        //Reset the up vector.
        final Vector up = Vector.Z;
        //Calculate the needed field of view angle to make the displayed portion 
        //of the line through the center point exactly vDist long.
        final float fovAngle = 0f;
        cameraMode = new CameraMode(gs, eye, center, up, fovAngle);
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode. The camera
     * should focus on the robot.
     *
     * @param gs
     */
    public void setHelicopterMode(GlobalState gs) {
        final Vector eye = robots.get(robotFocus).getPosition().add(HELICOPTER_EYE);
        final Vector center = robots.get(robotFocus).getPosition().add(HELICOPTER_LOOKAT);
        final Vector up = robots.get(robotFocus).getDirection();
        final float fovAngle = 40f;
        cameraMode = new CameraMode(gs, eye, center, up, fovAngle);
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode. The camera
     * should focus on the robot.
     *
     * @param gs
     */
    public void setMotorCycleMode(GlobalState gs) {
        final Vector eye = addRelative(robots.get(robotFocus).getPositionTrack(), robots.get(robotFocus).getDirectionTrack(), MOTORCYCLE_EYE);
        final Vector center = robots.get(robotFocus).getPosition().add(MOTORCYCLE_LOOKAT);
        final Vector up = Vector.Z;
        final float fovAngle = 40f;
        cameraMode = new CameraMode(gs, eye, center, up, fovAngle);
    }

    /**
     * Computes eye, center, and up, based on the first person mode. The camera
     * should view from the perspective of the robot.
     *
     * @param gs
     */
    public void setFirstPersonMode(GlobalState gs) {
        final Robot robot = robots.get(robotEye);
        Robot newFocus = robots.get(robotFocus);
        if (robots.get(robotFocus) == robot) {
            if (robot == robots.get(0)) {
                newFocus = robots.get(1);
            } else {
                newFocus = robots.get(0);
            }
        }
        final Vector eye = addRelative(robot.getPosition(), robot.getDirection(), FIRSTPERSON_EYE);
        final Vector center = addRelative(newFocus.getPosition(), newFocus.getDirection(), FIRSTPERSON_LOOKAT);
        final Vector up = Vector.Z;
        final float fovAngle = 40f;
        cameraMode = new CameraMode(gs, eye, center, up, fovAngle);
    }

    private Vector addRelative(Vector position, Vector direction, Vector vector) {
        final Vector normal = direction.cross(Vector.Z).normalized();
        return position
                .add(normal.scale(vector.x()))
                .add(direction.normalized().scale(vector.y()))
                .add(normal.cross(direction).normalized().scale(vector.z()));
    }

    private int getLastRobot(List<Robot> robots) {
        int index = 0;
        double distance = robots.get(index).getTrackT();
        for (int i = 0; i < robots.size(); i++) {
            final Robot robot = robots.get(i);
            if (distance > robot.getTrackT()) {
                index = i;
                distance = robot.getTrackT();
            }
        }
        return index;
    }

}
