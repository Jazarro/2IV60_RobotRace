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
package utility;

import robotrace.GlobalState;

/**
 * Static utility class. Contains some utility methods that aid in using the
 * GlobalState class.
 *
 * @see GlobalState
 * @author Arjan Boschman
 */
@SuppressWarnings("FinalClass")
public final class GsUtils {

    /**
     * Convenience method that retrieves the azimuth angle from the given
     * GlobalState.
     *
     * Mainly meant for lazy developers who don't want to remember the
     * difference between phi and theta.
     *
     * @param globalState The GlobalState instance where the variable is stored.
     *
     * @return The theta angle known to the given globalState instance.
     */
    public static float getAzimuth(GlobalState globalState) {
        return globalState.theta;
    }

    /**
     * Convenience method that sets the given azimuth angle to the given
     * GlobalState.
     *
     * Mainly meant for lazy developers who don't want to remember the
     * difference between phi and theta.
     *
     * @param globalState The GlobalState instance where the variable is stored.
     * @param azimuth     The value to assign to the theta variable in the given
     *                    globalState instance.
     */
    public static void setAzimuth(GlobalState globalState, float azimuth) {
        globalState.theta = azimuth;
    }

    /**
     * Convenience method that retrieves the inclination angle from the given
     * GlobalState.
     *
     * @param globalState The GlobalState instance where the variable is stored.
     *
     * @return The phi angle known to the given globalState instance.
     */
    public static float getInclination(GlobalState globalState) {
        return globalState.phi;
    }

    /**
     * Convenience method that sets the given inclination angle to the given
     * GlobalState.
     *
     * Mainly meant for lazy developers who don't want to remember the
     * difference between phi and theta.
     *
     * @param globalState The GlobalState instance where the variable is stored.
     * @param inclination The value to assign to the phi variable in the given
     *                    globalState instance.
     */
    public static void setInclination(GlobalState globalState, float inclination) {
        globalState.phi = inclination;
    }

    /**
     * Empty, private constructor to make sure no instance of this final class
     * is ever created.
     */
    private GsUtils() {
    }

}
