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
