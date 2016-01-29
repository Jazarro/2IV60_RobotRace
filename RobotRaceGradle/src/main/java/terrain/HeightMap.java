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
package terrain;

/**
 * Describes the height map of a certain terrain.
 *
 * @author Arjan Boschman
 */
public interface HeightMap {

    /**
     * Computes the elevation of the terrain at (x, y).
     *
     * @param x The x-coordinate in meters.
     * @param y The y-coordinate in meters.
     * @return The z-coordinate of the terrain at (x, y) in meters.
     */
    float heightAt(double x, double y);
}
