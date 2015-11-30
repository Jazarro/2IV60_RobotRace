/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package utility;

import robotrace.GlobalState;

/**
 Static utility class. Contains some utility methods that aid in using the
 GlobalState class.

 @see GlobalState
 @author Arjan Boschman
 */
public final class GsUtils{

    /**
     Convenience method that retrieves the azimuth angle from the given
     GlobalState.

     Mainly meant for lazy developers who don't want to remember the
     difference between phi and theta.

     @param globalState The GlobalState instance where the variable is stored.

     @return The theta angle known to the given globalState instance.
     */
    public static float getAzimuth(GlobalState globalState){
        return globalState.theta;
    }

    /**
     Convenience method that sets the given azimuth angle to the given
     GlobalState.

     Mainly meant for lazy developers who don't want to remember the
     difference between phi and theta.

     @param globalState The GlobalState instance where the variable is stored.
     @param azimuth     The value to assign to the theta variable in the given
                        globalState instance.
     */
    public static void setAzimuth(GlobalState globalState, float azimuth){
        globalState.theta = azimuth;
    }

    /**
     Convenience method that retrieves the inclination angle from the given
     GlobalState.

     @param globalState The GlobalState instance where the variable is stored.

     @return The phi angle known to the given globalState instance.
     */
    public static float getInclination(GlobalState globalState){
        return globalState.phi;
    }

    /**
     Convenience method that sets the given inclination angle to the given
     GlobalState.

     Mainly meant for lazy developers who don't want to remember the
     difference between phi and theta.

     @param globalState The GlobalState instance where the variable is stored.
     @param inclination The value to assign to the phi variable in the given
                        globalState instance.
     */
    public static void setInclination(GlobalState globalState, float inclination){
        globalState.phi = inclination;
    }

    /**
     Empty, private constructor to make sure no instance of this final class
     is ever created.
     */
    private GsUtils(){
    }

}
