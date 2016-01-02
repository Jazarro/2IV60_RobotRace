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

    private final racetrack.RaceTrack testRaceTrack;

    public RaceTrackFactory() {
        this.testRaceTrack = new RaceTrack();
    }

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        testRaceTrack.initialize(gl, bmInitialiser);
    }

    public RaceTrack makeTestRaceTrack() {
        return testRaceTrack;
    }

}
