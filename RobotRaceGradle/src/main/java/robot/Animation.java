/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

/**
 * Very simple class that allows one to keep track of an Animation's timing.
 *
 * @author Arjan Boschman
 */
public class Animation {

    private final float periodLength;

    /**
     * The time that the current animation period started in seconds.
     */
    private float zeroTime = -1;
    /**
     * The total time elapsed since the start of the current animation period in
     * seconds. This is always a value between 0 and the periodLength.
     */
    private float totalTimeElapsed = 0;

    /**
     * Creates a new instance of Animation, which can be used to keep track of
     * timing and calculate interpolation values.
     *
     * @param periodLength The length of the animations period in seconds. Must
     *                     be positive and greater than zero.
     */
    public Animation(float periodLength) {
        if (periodLength <= 0) {
            throw new IllegalArgumentException("Tried to create an instance of Animation with a period <= 0.");
        }
        this.periodLength = periodLength;
    }

    /**
     * Resets the Animation's timing to the start of the Animation period.
     * Calling the {@link #getLinearInterpolation()} method directly after this
     * will return zero.
     *
     * @param tAnim The length of the animations period in seconds. Must be
     *              positive and greater than zero.
     */
    public void restart(float tAnim) {
        zeroTime = tAnim;
        totalTimeElapsed = 0;
    }

    /**
     * Updates the animation time.
     *
     * @param tAnim The global time in seconds. This increases indefinitely with
     *              time and never loops back to an earlier value. It is not
     *              necessarily linked to any actual point in time; it can only
     *              be used to keep track of the passing of time.
     */
    public void updateTime(float tAnim) {
        if (zeroTime == -1) {
            zeroTime = tAnim;
            totalTimeElapsed = 0;
        } else {
            totalTimeElapsed = tAnim - zeroTime;
        }
        while (totalTimeElapsed >= periodLength) {
            final float remainder = totalTimeElapsed - periodLength;
            zeroTime = tAnim - remainder;
            totalTimeElapsed = remainder;
        }
    }

    /**
     * Returns the linear interpolation value. This value is a measure of how
     * far along the animation is in its period.
     *
     * The interpolation value ranges between 0 and 1, where zero means the
     * animation is about the start and 1 means the animation has ended. In a
     * circular, looping animation (one that doesn't ping-pong) 0 is equivalent
     * to 1.
     *
     * @return The interpolation value between 0 and 1.
     */
    public float getLinearInterpolation() {
        return getLinearInterpolation(0F);
    }

    /**
     * Returns the linear interpolation value. This value is a measure of how
     * far along the animation is in its period.
     *
     * The interpolation value ranges between 0 and 1, where zero means the
     * animation is about the start and 1 means the animation has ended. In a
     * circular, looping animation (one that doesn't ping-pong) 0 is equivalent
     * to 1.
     *
     * @param offset The period offset to take into account. Useful for getting
     *               an interpolation fraction for applications that are out of
     *               sync by some constant amount. The offset must be a number between 0 and 1.
     * @return The interpolation value between 0 and 1.
     */
    public float getLinearInterpolation(float offset) {
        return (totalTimeElapsed + offset * periodLength) % periodLength / periodLength;
    }

}
