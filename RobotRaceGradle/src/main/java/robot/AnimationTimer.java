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
public class AnimationTimer {

    /**
     * The length of the animations period in seconds. A negative value means
     * there is no animation and interpolation will always return zero.
     */
    private float periodLength;
    /**
     * The time that the current animation period started in seconds. A negative
     * value means the animation hasn't started yet and the next call to
     * {@link #updateTime()} will set the initial value to the current time.
     */
    private float zeroTime = -1f;
    /**
     * The total time elapsed since the start of the current animation period in
     * seconds. This is always a value between 0 and the periodLength.
     */
    private float totalTimeElapsed = 0f;

    /**
     * Resets the Animation's timing to the start of the Animation period.
     * Calling the {@link #getLinearInterpolation()} method directly after this
     * will return zero.
     *
     * @param periodLength The length of the animation period.
     */
    public void restart(float periodLength) {
        this.periodLength = periodLength;
        this.zeroTime = -1f;
        this.totalTimeElapsed = 0f;
    }

    /**
     * Updates the animation time.
     *
     * @param tAnim The global time in seconds. This increases indefinitely with
     *              time and never loops back to an earlier value. It is not
     *              necessarily linked to any actual point in time; it can only
     *              be used to keep track of the passing of time.
     * @return How many times the period looped around this update. Is usually
     *         zero, but is one if the animation has wrapped around. If a lot of
     *         time passed since the previous update, it might skip periods and
     *         the returned value will be even higher than one.
     */
    public int updateTime(float tAnim) {
        if (periodLength <= 0f) {
            return 0;
        }
        if (zeroTime <= 0f) {
            zeroTime = tAnim;
            totalTimeElapsed = 0f;
        } else {
            totalTimeElapsed = tAnim - zeroTime;
        }
        int periodsAdvanced = 0;
        while (totalTimeElapsed >= periodLength) {
            periodsAdvanced++;
            final float remainder = totalTimeElapsed - periodLength;
            zeroTime = tAnim - remainder;
            totalTimeElapsed = remainder;
        }
        return periodsAdvanced;
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
        return getLinearInterpolation(0f);
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
     *               sync by some constant amount. The offset must be a number
     *               between 0 and 1.
     * @return The interpolation value between 0 and 1.
     */
    public float getLinearInterpolation(float offset) {
        if (periodLength <= 0f) {
            return 0f;
        } else {
            return (totalTimeElapsed + offset * periodLength) % periodLength / periodLength;
        }
    }

}
