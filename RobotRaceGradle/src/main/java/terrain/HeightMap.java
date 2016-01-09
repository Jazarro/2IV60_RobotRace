/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
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
