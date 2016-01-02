/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package racetrack;

import bodies.Body;
import bodies.BufferManager;
import bodies.SimpleBody;
import bodies.SingletonDrawable;
import bodies.TrackBuilder;
import bodies.assembly.Vertex;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Track;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import robotrace.Vector;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack implements SingletonDrawable {

    private static final double LANE_WIDTH = 1.22d;
    private static final int LANE_COUNT = 4;
    private static final double TRACK_HEIGHT = 2d;
    private static final int SLICE_COUNT = 50;

    private Body raceTrackBody;

    /**
     * Array with 3N control points, where N is the number of segments.
     */
    private Vector[] controlPoints = null;

    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }

    /**
     * Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
    }

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        List<Vertex> trackDescription = new ArrayList<>();
        for (double t = 0d; t < 1d; t += (1d / SLICE_COUNT)) {
            trackDescription.add(new Vertex(getPoint(t)));
        }
        raceTrackBody = new TrackBuilder(bmInitialiser)
                .setTrackProperties(LANE_WIDTH, LANE_COUNT, TRACK_HEIGHT)
                .build(trackDescription);
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        if (controlPoints == null) {
            raceTrackBody.draw(gl, glut);
        } else {
            // draw the spline track
        }
    }

    /**
     * Returns the center of a lane at 0 <= t < 1. Use this method to find the
     * position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t) {
        if (controlPoints == null) {
            return Vector.O; // <- code goes here
        } else {
            return Vector.O; // <- code goes here
        }
    }

    /**
     * Returns the tangent of a lane at 0 <= t < 1. Use this method to find the
     * orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t) {
        if (controlPoints == null) {
            return Vector.O; // <- code goes here
        } else {
            return Vector.O; // <- code goes here
        }
    }

    /**
     * Returns a point on the test track at 0 <= t < 1.
     */
    private Vector getPoint(double t) {
        return new Vector(10 * Math.cos(2d * Math.PI * t), 14 * Math.sin(2d * Math.PI * t), 20*Math.cos(2d * Math.PI * t));
    }

    /**
     * Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t) {
        return new Vector(10 * Math.sin(2d * Math.PI * t), 14 * Math.cos(2d * Math.PI * t), 1);
    }

    /**
     * Returns a point on a bezier segment with control points P0, P1, P2, P3 at
     * 0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
            Vector P2, Vector P3) {
        return Vector.O; // <- code goes here
    }

    /**
     * Returns a tangent on a bezier segment with control points P0, P1, P2, P3
     * at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
            Vector P2, Vector P3) {
        return Vector.O; // <- code goes here
    }
}
