package racetrack;

import java.util.*;

class RaceTrackDistances {

    private final List<RaceTrackDistanceT> raceTrackDistances = new ArrayList<>();
    private double distance = 0d;

    protected double getDistance(double t) {
        int i = 0;
        while (t >= raceTrackDistances.get(i).getT()) {
            i++;
            if (i >= raceTrackDistances.size() - 1) {
                i = 1;
                t -= raceTrackDistances.get(raceTrackDistances.size() - 1).getT();
            }
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
        final double scale = (distance - raceTrackDistances.get(i - 1).getDistance()) / (raceTrackDistances.get(i).getDistance() - raceTrackDistances.get(i - 1).getDistance());
        return (1d - scale) * raceTrackDistances.get(i - 1).getT() + scale * raceTrackDistances.get(i).getT();
    }

    protected void addPair(double deltaDistance, double t) {
        distance += deltaDistance;
        raceTrackDistances.add(new RaceTrackDistanceT(distance, t));
    }

    private static final class RaceTrackDistanceT {

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
