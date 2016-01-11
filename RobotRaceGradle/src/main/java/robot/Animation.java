/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Arjan Boschman
 */
public class Animation {

    private final Map<AnimationType, Float> supportedAnims = new HashMap<>();

    private final AnimationTimer animationTimer;
    private AnimationType defaultAnimationType = AnimationType.IDLE;
    private AnimationType currentAnimationType = AnimationType.IDLE;
    private int nrPeriodsLeft = 0;

    public Animation() {
        this.animationTimer = new AnimationTimer();
        this.supportedAnims.put(AnimationType.IDLE, -1f);
    }

    public void addAnimationType(AnimationType animationType, float periodLength) {
        this.supportedAnims.put(animationType, periodLength);
    }

    public void setDefaultAnimationType(AnimationType defaultAnimationType) {
        this.defaultAnimationType = defaultAnimationType;
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }

    public void update(float tAnim) {
        final int periodsAdvanced = animationTimer.updateTime(tAnim);
        if (currentAnimationType != defaultAnimationType) {
            nrPeriodsLeft -= periodsAdvanced;
            if (nrPeriodsLeft <= 0) {
                nrPeriodsLeft = 0;
                currentAnimationType = defaultAnimationType;
                animationTimer.restart(supportedAnims.get(defaultAnimationType));
            }
        }
    }

    public void setDefaultAnimation(AnimationType animationType) {
        if (supportedAnims.containsKey(animationType)) {
            if (currentAnimationType == defaultAnimationType) {
                currentAnimationType = animationType;
                animationTimer.restart(supportedAnims.get(animationType));
            }
            this.defaultAnimationType = animationType;
        }
    }

    public void playAnimationOnce(AnimationType animationType) {
        playAnimation(animationType, 1);
    }

    public void playAnimation(AnimationType animationType, int repeats) {
        if (supportedAnims.containsKey(animationType)) {
            this.nrPeriodsLeft = repeats;
            this.currentAnimationType = animationType;
            animationTimer.restart(supportedAnims.get(animationType));
        }
    }

    /**
     * Delegates to {@link AnimationTimer#getLinearInterpolation()}.
     *
     * @return
     */
    public float getLinearInterpolation() {
        return animationTimer.getLinearInterpolation();
    }

    /**
     * Delegates to {@link AnimationTimer#AnimationTimer(float)}.
     *
     * @param offset
     * @return
     */
    public float getLinearInterpolation(float offset) {
        return animationTimer.getLinearInterpolation(offset);
    }

}
