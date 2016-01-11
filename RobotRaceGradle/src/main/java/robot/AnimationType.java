/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

/**
 * Sums up the types of animations that robots can display. Each robot body
 * implements its own versions of these animations.
 *
 * Robot bodies don't necessarily need to provide implementations for all
 * animations. If a robot body is assigned a animation it doesn't support, it
 * will default to the IDLE animation instead. IDLE is therefore an exception to
 * the rule; all robot bodies all obliged to support it.
 *
 * @author Arjan Boschman
 */
public enum AnimationType {
    /**
     * The default animation type. Just standing around, doing nothing. All
     * robot bodies must implement at least this animation.
     */
    IDLE,
    /**
     * Running straight ahead.
     */
    RUNNING,
    /**
     * Jump from a running animation, for instance to get over a hurdle. This is
     * not to be used while standing idle. Also, for best effect run this
     * animation only once, rather than repeatedly.
     */
    JUMPING,
    /**
     * Used primarily for spectators, this specifies the cheering on of the
     * athletes on the track.
     */
    CHEERING,
    /**
     * Celebration can take many forms, for instance jumping or throwing one's
     * arms into the air. Primarily used by athletes and spectators upon winning
     * the game.
     */
    CELEBRATING,
    /**
     * Animation would be sad or dispirited, primarily used by athletes and
     * spectators upon losing the game.
     */
    DEJECTED;
}
