/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package racetrack;

import bodies.Body;
import bodies.BufferManager;
import bodies.SingletonDrawable;
import bodies.TrackBuilder;
import bodies.assembly.Vertex;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static racetrack.RaceTrackDefinition.RTD_TEST;
import static racetrack.RaceTrackDefinition.getClosedTrack;
import static racetrack.RaceTrackDefinition.getTrackPoint;
import robotrace.Vector;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack implements SingletonDrawable {

    public static final double LANE_WIDTH = 1.22d;
    public static final int LANE_COUNT = 4;
    public static final double TRACK_HEIGHT = 2d;
    public static final int SLICE_COUNT = 50;

    private Body raceTrackBody;
    private int trackType = RTD_TEST;

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

    public void setTrackType(int trackType) {
        this.trackType = trackType;
    }

    public int getTrackType() {
        return this.trackType;
    }

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        List<Vertex> trackDescription = new ArrayList<>();
        for (double t = 0d; t < 1d; t += (1d / SLICE_COUNT)) {
            trackDescription.add(new Vertex(getTrackPoint(t, this)));
        }
        raceTrackBody = new TrackBuilder(bmInitialiser)
                .setTrackProperties(LANE_WIDTH, LANE_COUNT, TRACK_HEIGHT, getClosedTrack(this))
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
}
