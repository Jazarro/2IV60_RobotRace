/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package racetrack;

import bodies.BufferManager;
import java.util.*;
import javax.media.opengl.GL2;
import static racetrack.RaceTrackDefinition.getMaxTypeID;

public class RaceTrackFactory {

    private final List<RaceTrack> raceTrackTypes = new ArrayList<>();

    public RaceTrackFactory() {
        for (int i = 0; i <= getMaxTypeID(); i++) {
            final RaceTrack newRaceTrack = new RaceTrack();
            newRaceTrack.setTrackType(i);
            raceTrackTypes.add(newRaceTrack);
        }
    }

    public void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser) {
        for (RaceTrack raceTrack : raceTrackTypes) {
            raceTrack.initialize(gl, bmInitialiser);
        }
    }

    public RaceTrack makeRaceTrack(int raceTrackType) {
        return raceTrackTypes.get(raceTrackType);
    }

}
