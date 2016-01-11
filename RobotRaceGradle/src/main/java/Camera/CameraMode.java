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
        this.planeNear = 0.1f * dist;
        this.planeFar = Math.max(MIN_FAR_PLANE_DIST, 10f * dist);
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
