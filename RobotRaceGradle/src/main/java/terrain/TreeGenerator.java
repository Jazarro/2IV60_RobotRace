/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import robotrace.Vector;

/**
 *
 * @author Arjan Boschman
 */
public class TreeGenerator implements Supplier<Tree> {

    /**
     * Random seed that will be used every time to ensure that the trees don't
     * actually change from one run of the program to the next.
     */
    private static final long RAND_SEED = 95_873_649_098L;
    /**
     * Every tree demands a clearing, a circle with this radius around itself.
     * Within these bounds, other trees are prevented from growing.
     */
    private static final int TREE_CLEARING_RADIUS = 5;

    /**
     * The maximum elevation difference in meters that can occur in a 1 meter
     * radius around a spot in order for that spot to be considered level enough
     * to support tree growth.
     */
    private static final float MAX_DECLINATION = 0.5F;

    private final Random rand = new Random(RAND_SEED);
    private final Set<Rectangle> forbiddenAreas = new HashSet<>();
    private final Rectangle bounds;
    private final HeightMap heightMap;
    private final Foliage foliage;

    public TreeGenerator(Rectangle bounds, HeightMap heightMap, Foliage foliage) {
        this.bounds = bounds;
        this.heightMap = heightMap;
        this.foliage = foliage;
    }

    public void addForbiddenArea(double x, double y, double width, double height) {
        this.forbiddenAreas.add(new Rectangle(x, y, width, height));
    }

    @Override
    public Tree get() {
        while (true) {
            final Point2D coords = new Point2D(
                    rand.nextDouble() * bounds.getWidth() + bounds.getX(),
                    rand.nextDouble() * bounds.getHeight() + bounds.getY());
            if (checkForbidden(coords) || checkAreaIsTooSteep(coords) || checkAreaIsUnderWater(coords)) {
                continue;
            }
            addForbiddenArea(coords.getX() - TREE_CLEARING_RADIUS,
                    coords.getY() - TREE_CLEARING_RADIUS,
                    2 * TREE_CLEARING_RADIUS, 2 * TREE_CLEARING_RADIUS);
            return new Tree(foliage,
                    new Vector(coords.getX(), coords.getY(),
                            heightMap.heightAt(coords.getX(), coords.getY())));
        }
    }

    private boolean checkForbidden(final Point2D coords) {
        return forbiddenAreas.stream().anyMatch((rect) -> rect.contains(coords));
    }

    private boolean checkAreaIsTooSteep(final Point2D coords) {
        final float center = heightMap.heightAt(coords.getX(), coords.getY());
        final float north = heightMap.heightAt(coords.getX(), coords.getY() - 1);
        final float south = heightMap.heightAt(coords.getX(), coords.getY() + 1);
        final float west = heightMap.heightAt(coords.getX() - 1, coords.getY());
        final float east = heightMap.heightAt(coords.getX() + 1, coords.getY());
        return Math.abs(center - north) > MAX_DECLINATION
                || Math.abs(center - south) > MAX_DECLINATION
                || Math.abs(center - west) > MAX_DECLINATION
                || Math.abs(center - east) > MAX_DECLINATION;
    }

    private boolean checkAreaIsUnderWater(final Point2D coords) {
        return heightMap.heightAt(coords.getX(), coords.getY()) < 0;
    }

}
