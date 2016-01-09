/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import java.util.Random;

/**
 * Uses the diamond-square algorithm to generate realistic looking terrain.
 *
 * @author Arjan Boschman
 */
public class FractalTerrainGenerator implements HeightMap {

    /**
     * Random seed that will be used every time to ensure that the terrain
     * doesn't actually change from one run of the program to the next. This
     * number has been more or less arbitrarily chosen.
     */
    private static final long RAND_SEED = 12_345_678_422L;

    public static FractalTerrainGenerator create() {
        final FractalTerrainGenerator instance = new FractalTerrainGenerator(10, 0.3F);
        instance.initialise();
        return instance;
    }
    private final Random rand = new Random(RAND_SEED);
    private final int globalSize;
    private final int max;
    private final float[][] map;
    private final float roughness;

    /**
     * Make a new instance of the FractalTerrainGenerator. Note that before
     * using this object as a HeightMap, it needs to be initialised first.
     *
     * @param detail    A measure of the size of the cube of terrain that will
     *                  be generated. Size is obtained as follows: size = (2 ^
     *                  detail) + 1.
     * @param roughness A value between 0 and 1, this is a measure of the
     *                  smoothness of the terrain. Values near zero yield smooth
     *                  terrain, values near 1 yield mountainous terrain.
     * @see #create() Consider using this static factory method instead. It
     * creates the generator using some default values and also initialises it.
     */
    public FractalTerrainGenerator(int detail, float roughness) {
        this.globalSize = (int) Math.pow(2, detail) + 1;
        this.max = globalSize - 1;
        this.map = new float[globalSize][globalSize];
        this.roughness = roughness;
    }

    /**
     * Must be called before this instance is used as a HeightMap. This method
     * will calculate the heights of all grid points in advance.
     */
    public void initialise() {
        final float initialValue = -50;
        map[0][0] = initialValue;
        map[max][0] = initialValue;
        map[0][max] = initialValue;
        map[max][max] = initialValue;
        divide(max);
    }

    private void divide(int size) {
        final int half = size / 2;
        final float scale = roughness * size;
        if (half < 1) {
            return;
        }
        for (int y = half; y < max; y += size) {
            for (int x = half; x < max; x += size) {
                performDiamondStep(x, y, half, rand.nextFloat() * scale * 2 - scale);
            }
        }
        for (int y = 0; y <= max; y += half) {//Be mindful of rounding errors.
            for (int x = (y + half) % size; x <= max; x += size) {
                performSquareStep(x, y, half, rand.nextFloat() * scale * 2 - scale);
            }
        }
        divide(half);
    }

    /**
     * The square step. For each diamond in the array, set the midpoint of that
     * diamond to be the average of the four corner points plus a random value.
     *
     * @param x      Position on x-axis in the map array.
     * @param y      Position on y-axis in the map array.
     * @param radius Half the distance between the diamonds's opposing edges.
     * @param offset The z-axis offset to add to the average value. This is what
     *               adds randomness to the generation.
     */
    private void performSquareStep(int x, int y, int radius, float offset) {
        final float average
                = (map[x][Math.floorMod(y - radius, globalSize)] //Top
                + map[Math.floorMod(x - radius, globalSize)][y] //Left
                + map[x][Math.floorMod(y + radius, globalSize)] //Bottom
                + map[Math.floorMod(x + radius, globalSize)][y]) / 4;  //Right
        map[x][y] = average + offset;
    }

    /**
     * The diamond step. For a certain square in the array, set the midpoint of
     * that square to be the average of the four corner points plus a random
     * value.
     *
     * @param x      Position on x-axis in the map array.
     * @param y      Position on y-axis in the map array.
     * @param radius Half the length of the square's edges.
     * @param offset The z-axis offset to add to the average value. This is what
     *               adds randomness to the generation.
     */
    private void performDiamondStep(int x, int y, int radius, float offset) {
        final float a1 = map[Math.floorMod(x - radius, globalSize)][Math.floorMod(y - radius, globalSize)]; //Top-Left
        final float a2 = map[Math.floorMod(x - radius, globalSize)][Math.floorMod(y + radius, globalSize)]; //Bottom-Left
        final float a3 = map[Math.floorMod(x + radius, globalSize)][Math.floorMod(y - radius, globalSize)]; //Top-Right
        final float a4 = map[Math.floorMod(x + radius, globalSize)][Math.floorMod(y + radius, globalSize)]; //Bottom-Right
        final float average
                = (map[Math.floorMod(x - radius, globalSize)][Math.floorMod(y - radius, globalSize)] //Top-Left
                + map[Math.floorMod(x - radius, globalSize)][Math.floorMod(y + radius, globalSize)] //Bottom-Left
                + map[Math.floorMod(x + radius, globalSize)][Math.floorMod(y - radius, globalSize)] //Top-Right
                + map[Math.floorMod(x + radius, globalSize)][Math.floorMod(y + radius, globalSize)]) / 4; //Bottom-Right
        map[x][y] = average + offset;
    }

    @Override
    public float heightAt(double x, double y) {
        final int gridX = (int) x + globalSize / 2;
        final int gridY = (int) y + globalSize / 2;
        return map[Math.floorMod(gridX, globalSize)][Math.floorMod(gridY, globalSize)];
    }

}
