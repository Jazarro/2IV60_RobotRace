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
import static racetrack.RaceTrackDefinition.*;
import robotrace.Vector;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack implements SingletonDrawable {

    public static final double LANE_WIDTH = 1.22d;
    public static final int LANE_COUNT = 4;
    public static final double TRACK_HEIGHT = 2d;

    private Body raceTrackBody;
    private int trackType = RTD_TEST;

    public RaceTrack() {
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
        for (double t = 0d; t <= 1d; t += (1d / getSliceCount())) {
            trackDescription.add(new Vertex(getTrackPoint(t)));
        }
        raceTrackBody = new TrackBuilder(bmInitialiser)
                .setTrackProperties(LANE_WIDTH, LANE_COUNT, TRACK_HEIGHT, getClosedTrack())
                .build(trackDescription);
    }

    public boolean getClosedTrack() {
        return RaceTrackDefinition.getClosedTrack(trackType);
    }

    public int getSliceCount() {
        return RaceTrackDefinition.getSliceCount(trackType);
    }

    public Vector getTrackPoint(double t) {
        return RaceTrackDefinition.getTrackPoint(trackType, t);
    }

    public Vector getTrackNormal(double t) {
        return RaceTrackDefinition.getTrackNormal(trackType, t);
    }

    public Vector getTrackTangent(double t) {
        return RaceTrackDefinition.getTrackTangent(trackType, t);
    }

    public double getTrackDistance(double t, double tPrevious) {
        return RaceTrackDefinition.getTrackDistance(trackType, t, tPrevious);
    }

    public Vector getLanePoint(double t, int laneNumber) {
        return RaceTrackDefinition.getLanePoint(trackType, laneNumber, t);
    }

    public Vector getLaneNormal(double t, int laneNumber) {
        return RaceTrackDefinition.getTrackNormal(trackType, t);
    }

    public Vector getLaneTangent(double t, int laneNumber) {
        return RaceTrackDefinition.getLaneTangent(trackType, laneNumber, t);
    }

    public double getLaneDistance(double t, double tPrevious, int laneNumber) {
        return RaceTrackDefinition.getLaneDistance(trackType, laneNumber, t, tPrevious);
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        raceTrackBody.draw(gl, glut);
    }
}
