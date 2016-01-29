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

import java.util.HashSet;
import java.util.Set;
import javax.media.opengl.GL2;

/**
 * A simple Body consisting of an arbitrary number or shapes. Use the builders
 * given by this class to obtain instances of SimpleBody.
 *
 * @author Arjan Boschman
 */
public class SimpleBody implements Body {

    private final Set<Shape> shapes = new HashSet<>();

    /**
     * Adds a shape to this SimpleBody. During the SimpleBody's draw phase,
     * shapes belonging to a SimpleBody will all be drawn in no particular
     * order. Shapes may belong to several SimpleBodies.
     *
     * @param shape The shape to be added.
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void draw(GL2 gl) {
        shapes.stream().forEach((shape) -> shape.draw(gl));
    }

}
