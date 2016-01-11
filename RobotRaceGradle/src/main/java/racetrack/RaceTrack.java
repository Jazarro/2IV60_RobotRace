/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package racetrack;

import Texture.ImplementedTexture;
import bodies.Body;
import bodies.BufferManager;
import bodies.SingletonDrawable;
import bodies.TrackBuilder;
import bodies.assembly.Vertex;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import static racetrack.RaceTrackDefinition.*;
import robotrace.Lighting;
import robotrace.Material;
import robotrace.Vector;
import terrain.Terrain;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
public class RaceTrack implements SingletonDrawable {

    public static final float LANE_WIDTH = 1.22f;
    public static final int LANE_COUNT = 4;
    public static final float TRACK_HEIGHT = 2f;

    private Body raceTrackBody;
    private int trackType = RTD_TEST;
    private final RaceTrackDistances trackDistances = new RaceTrackDistances();
    private final List<RaceTrackDistances> laneDistances = new ArrayList<>();
    private ImplementedTexture textureTop;
    private ImplementedTexture textureBottom;
    private ImplementedTexture textureSide;

    public void setTrackType(int trackType) {
        this.trackType = trackType;
    }

    public int getTrackType() {
        return this.trackType;
    }

    @Override
    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        for (int i = 0; i < LANE_COUNT; i++) {
            laneDistances.add(new RaceTrackDistances());
        }
        final List<Vertex> trackDescription = new ArrayList<>();
        final double stepSize = 1d / getSliceCount();
        double tPrevious = 0d;
        for (double t = 0d; t < (1d + stepSize); t += stepSize) {
            if (t > 1d) {
                t = 1d;
            }
            trackDescription.add(new Vertex(getTrackPoint(t)));
            trackDistances.addPair(getTrackPoint(t).subtract(getTrackPoint(tPrevious)).length(), t);
            for (int i = 0; i < LANE_COUNT; i++) {
                laneDistances.get(i).addPair(getLanePoint(t, i).subtract(getLanePoint(tPrevious, i)).length(), t);
            }
            tPrevious = t;
        }
        textureTop = getTopTexture(trackType, gl);
        textureBottom = getBottomTexture(trackType, gl);
        textureSide = getSideTexture(trackType, gl);
        raceTrackBody = new TrackBuilder(bmInitialiser)
                .setTrackProperties(LANE_WIDTH, LANE_COUNT, TRACK_HEIGHT, getClosedTrack())
                .setTextures(textureTop, textureBottom, textureSide)
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

    public double getTrackDistance(double t) {
        return trackDistances.getDistance(t);
    }

    public double getTrackT(double distance) {
        return trackDistances.getT(distance);
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

    public double getLaneDistance(double t, int laneNumber) {
        return laneDistances.get(laneNumber).getDistance(t);
    }

    public double getLaneT(double distance, int laneNumber) {
        return laneDistances.get(laneNumber).getT(distance);
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, Lighting lighting) {
        lighting.setMaterial(gl, Material.NONE);
        lighting.setColor(gl, 1f, 1f, 1f, 1f);
        gl.glPushMatrix();
        raceTrackBody.draw(gl);
        gl.glPopMatrix();
    }

}
