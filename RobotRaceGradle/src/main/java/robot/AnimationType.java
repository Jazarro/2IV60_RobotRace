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
