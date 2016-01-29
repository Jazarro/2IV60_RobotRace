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

import javax.media.opengl.GL2;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import javax.media.opengl.glu.GLU;
import robotrace.GlobalState;
import robotrace.Vector;

public class CameraMode {

    /**
     * The far plane must have a minimum distance, or else nothing will be
     * visible when zoomed all the way in.
     */
    private static final float MIN_FAR_PLANE_DIST = 500f;

    /**
     * The position of the camera.
     */
    private final Vector eye;
    /**
     * The point to which the camera is looking.
     */
    private final Vector center;
    /**
     * The up vector.
     */
    private final Vector up;

    private final float fovAngle;
    private final float planeNear;
    private final float planeFar;

    public CameraMode(GlobalState gs, Vector eye, Vector center, Vector up, float fovAngle) {
        this.eye = eye;
        this.center = center;
        this.up = up;
        final float dist = (float) center.subtract(eye).length();
        if (fovAngle < 1d) {
            this.fovAngle = (float) Math.toDegrees(Math.atan(dist / (2d * gs.vWidth)));
        } else {
            this.fovAngle = fovAngle;
        }
        final float planeNearNew = 0.1f * dist;
        final float planeFarNew = Math.max(MIN_FAR_PLANE_DIST, 10f * dist);
        if (planeNearNew <= 0f) {
            this.planeNear = 1f;
        } else {
            this.planeNear = planeNearNew;
        }
        if (planeFarNew <= 0f) {
            this.planeFar = 1f;
        } else {
            this.planeFar = planeFarNew;
        }
    }

    private CameraMode(Vector eye, Vector center, Vector up, float fovAngle, float planeNear, float planeFar) {
        this.eye = eye;
        this.center = center;
        this.up = up;
        this.fovAngle = fovAngle;
        this.planeNear = planeNear;
        this.planeFar = planeFar;
    }

    public static CameraMode interpolateMode(CameraMode modeFrom, CameraMode modeTo, double distance) {
        return new CameraMode(
                modeFrom.eye.scale(1d - distance).add(modeTo.eye.scale(distance)),
                modeFrom.center.scale(1d - distance).add(modeTo.center.scale(distance)),
                modeFrom.up.scale(1d - distance).add(modeTo.up.scale(distance)),
                (float) ((modeFrom.fovAngle * (1d - distance)) + (modeTo.fovAngle * (distance))),
                (float) ((modeFrom.planeNear * (1d - distance)) + (modeTo.planeNear * (distance))),
                (float) ((modeFrom.planeFar * (1d - distance)) + (modeTo.planeFar * (distance)))
        );
    }

    public void setView(GL2 gl, GLU glu, GlobalState gs) {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        //Load the identity matrix.
        gl.glLoadIdentity();
        glu.gluPerspective(fovAngle, (float) gs.w / gs.h, planeNear, planeFar);
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        //Load the identity matrix.
        gl.glLoadIdentity();
        glu.gluLookAt(eye.x(), eye.y(), eye.z(),
                center.x(), center.y(), center.z(),
                up.x(), up.y(), up.z());
    }

    public Vector getEye() {
        return eye;
    }

    public Vector getCenter() {
        return center;
    }

    public Vector getUp() {
        return up;
    }

}
