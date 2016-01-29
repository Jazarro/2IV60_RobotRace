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
package racetrack;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Robke Geenen
 */
class RaceTrackDistances {

    private final List<RaceTrackDistanceT> raceTrackDistances;
    private double distance = 0d;

    RaceTrackDistances() {
        this.raceTrackDistances = new ArrayList<>();
    }

    protected double getDistance(double t) {
        int i = 0;
        while (t >= raceTrackDistances.get(i).getT()) {
            i++;
            if (i >= raceTrackDistances.size() - 1) {
                i = 1;
                t -= raceTrackDistances.get(raceTrackDistances.size() - 1).getT();
            }
        }
        if (i == 0) {
            return 0d;
        }
        final double scale = (t - raceTrackDistances.get(i - 1).getT()) / (raceTrackDistances.get(i).getT() - raceTrackDistances.get(i - 1).getT());
        return (1d - scale) * raceTrackDistances.get(i - 1).getDistance() + scale * raceTrackDistances.get(i).getDistance();
    }

    protected double getT(double distance) {
        int i = 0;
        while (distance >= raceTrackDistances.get(i).getDistance()) {
            i++;
            if (i >= raceTrackDistances.size() - 1) {
                i = 1;
                distance -= raceTrackDistances.get(raceTrackDistances.size() - 1).getDistance();
            }
        }
        if (i == 0) {
            return 0d;
        }
        final double scale = (distance - raceTrackDistances.get(i - 1).getDistance()) / (raceTrackDistances.get(i).getDistance() - raceTrackDistances.get(i - 1).getDistance());
        return (1d - scale) * raceTrackDistances.get(i - 1).getT() + scale * raceTrackDistances.get(i).getT();
    }

    protected void addPair(double deltaDistance, double t) {
        distance += deltaDistance;
        raceTrackDistances.add(new RaceTrackDistanceT(distance, t));
    }

    private static class RaceTrackDistanceT {

        private final double t, distance;

        private RaceTrackDistanceT(double distance, double t) {
            this.distance = distance;
            this.t = t;
        }

        private double getDistance() {
            return distance;
        }

        private double getT() {
            return t;
        }
    }
}
