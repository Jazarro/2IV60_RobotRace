/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package racetrack;

import bodies.BufferManager;
import javax.media.opengl.GL2;

public class RaceTrackFactory {

    private final RaceTrack testRaceTrack = new RaceTrack();
    private final RaceTrack testElevatedRaceTrack = new RaceTrack();

    public RaceTrackFactory() {
    }

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        testRaceTrack.setTrackType(RaceTrackDefinition.RTD_TEST);
        testRaceTrack.initialize(gl, bmInitialiser);
        testElevatedRaceTrack.setTrackType(RaceTrackDefinition.RTD_TEST_ELEVATED);
        testElevatedRaceTrack.initialize(gl, bmInitialiser);
    }

    public RaceTrack makeTestRaceTrack() {
        return testRaceTrack;
    }

    public RaceTrack makeTestElevatedRaceTrack() {
        return testElevatedRaceTrack;
    }

}
