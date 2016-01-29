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
package bodies;

import javax.media.opengl.GL2;

/**
 * A rigid body, consisting of a collection of one or more shapes. Because the
 * body is rigid, its shapes cannot be transformed relative to each other.
 *
 * Important is that only one copy of each body needs to exist. Bodies have no
 * position, scale or orientation in the world, they can be reused to draw the
 * same body multiple times on the screen.
 *
 * @author Arjan Boschman
 */
public interface Body {

    /**
     * To be called in the draw loop. Uses the given instances of GL2 and GLUT
     * to draw the body.
     *
     * NB: There are plans to drop the dependency on GLUT entirely, beware this
     * second parameter will likely disappear in a later version.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    void draw(GL2 gl);

}
