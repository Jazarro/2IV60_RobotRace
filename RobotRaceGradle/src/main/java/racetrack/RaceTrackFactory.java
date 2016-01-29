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
package racetrack;

import bodies.BufferManager;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;
import static racetrack.RaceTrackDefinition.getMaxTypeID;

/**
 * 
 * @author Robke Geenen
 */
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
