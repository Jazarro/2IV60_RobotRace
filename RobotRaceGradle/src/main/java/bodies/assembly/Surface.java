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
package bodies.assembly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Robke Geenen
 */
class Surface {

    /**
     * The IndexedVertex's defining this Surface.
     */
    private final List<IndexedVertex> vertices;
    /**
     * If this Surface is a polygon or a quad strip.
     */
    private final boolean polygon;

    /**
     * Constructor specifying the IndexedVertex's defining this Surface and if
     * this Surface is a polygon or a quad strip.
     *
     * @param vertices The IndexedVertex's defining this Surface.
     * @param polygon  If this Surface is a polygon or a quad strip.
     */
    Surface(List<IndexedVertex> vertices, boolean polygon) {
        this.vertices = vertices;
        this.polygon = polygon;
    }

    /**
     * Get the IndexedVertex's defining this Surface.
     *
     * @return The IndexedVertex's defining this Surface.
     */
    public List<IndexedVertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    /**
     * Set a new IndexedVertex at position index in the list of IndexedVertex's
     * defining this Surface.
     *
     * @param vertex The new IndexedVertex.
     * @param index  The position in the list of IndexedVertex's defining this
     *               Surface.
     */
    public void setVertex(IndexedVertex vertex, int index) {
        this.vertices.set(index, vertex);
    }

    /**
     * Get if this Surface is a polygon or a quad strip.
     *
     * @return If this Surface is a polygon or a quad strip.
     */
    public boolean isPolygon() {
        return polygon;
    }

    /**
     * Get the indices of all IndexedVertex's defining this Surface.
     *
     * Do this by iterating over all IndexedVertex's defining this Surface and
     * returning the resulting list.
     *
     * @return The indices of all IndexedVertex's defining this Surface.
     */
    public List<Integer> getIndices() {
        final List<Integer> indices = new ArrayList<>();
        for (IndexedVertex vertex : vertices) {
            indices.add(vertex.getIndex());
        }
        return indices;
    }
}
