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
package bodies;

import javax.media.opengl.GL2;

/**
 * Interface that describes any object that has a compositional relationship
 * with {@link Body} elements.
 *
 * Since only one instance of each Body needs to exist, SingletonDrawables
 * should also exist only once per purpose. They're not true singletons in the
 * strictest sense, since two instances can exist simultaneously; as long as
 * they both have a different purpose and don't maintain different instances of
 * the same Body.
 *
 * SingletonDrawables must be stateless, ergo they maintain no record of
 * previous interactions (during the draw phase).
 *
 * @author Arjan Boschman
 * @see Body
 */
public interface SingletonDrawable {

    /**
     * Loads or calculates all data needed to draw the body and prepares the
     * given instance of GL2 for the task.
     *
     * @param gl            The instance of GL2 responsible for drawing the
     *                      bodies.
     * @param bmInitialiser The BodyManager.Initialiser that is used to gather
     *                      all the data before communicating it to OpenGL.
     */
    void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser);

}
