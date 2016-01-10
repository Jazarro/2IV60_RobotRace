/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

/**
 * Materials that can be used for the robots.
 *
 * Some of the values were retrieved from the lookup table provided at:
 * http://www.real3dtutorials.com/tut00008.php
 *
 * @author Arjan Boschman
 */
public enum Material {

    NONE(new float[]{0f, 0f, 0f, 0f},
            new float[]{0f, 0f, 0f, 0f},
            0f),
    GOLD(new float[]{0.24725f, 0.1995f, 0.0745f, 1.0f},
            new float[]{0.75164f, 0.60648f, 0.22648f, 1.0f},
            new float[]{0.628281f, 0.555802f, 0.555802f, 1.0f},
            51.2f),
    SILVER(new float[]{0.19225f, 0.19225f, 0.19225f, 1.0f},
            new float[]{0.50754f, 0.50754f, 0.50754f, 1.0f},
            new float[]{0.508273f, 0.508273f, 0.508273f, 1.0f},
            51.2f),
    WOOD(new float[]{0.66f, 0.41f, 0.2f, 1.0f},
            new float[]{0.33f, 0.205f, 0.05f, 1.0f},
            2f),
    PLASTIC_ORANGE(
            new float[]{0.8f, 0.4f, 0.0f, 1.0f},
            new float[]{0.50196078f, 0.50196078f, 0.50196078f, 1.0f},
            32f),
    PLASTIC_CYAN(
            new float[]{0.0f, 0.1f, 0.06f, 1.0f},
            new float[]{0.0f, 0.50980392f, 0.50980392f, 1.0f},
            new float[]{0.50196078f, 0.50196078f, 0.50196078f, 1.0f},
            32f),
    DIRT(new float[]{0.2f, 0.2f, 0.2f, 1.0f},
            new float[]{0.6f, 0.5f, 0.3f, 1.0f},
            2f),
    BOULDER(new float[]{0.4f, 0.4f, 0.3f, 1.0f},
            new float[]{0.4f, 0.4f, 0.3f, 1.0f},
            0f),
    WATER(new float[]{0.0f, 0.1f, 0.2f, 0.9f},
            new float[]{0.2f, 0.2f, 0.2f, 1f},
            1f),
    LEAF(new float[]{0.3f, 0.4f, 0.1f, 1.0f},
            new float[]{0.1f, 0.3f, 0f, 1.0f},
            20f),
    BARK(new float[]{0.66f, 0.41f, 0.2f, 1.0f},
            new float[]{0.33f, 0.205f, 0.05f, 1.0f},
            2f);

    /**
     * The ambient RGBA reflectance of the material.
     */
    final float[] ambient;

    /**
     * The diffuse RGBA reflectance of the material.
     */
    final float[] diffuse;

    /**
     * The specular RGBA reflectance of the material.
     */
    final float[] specular;

    /**
     * The specular exponent of the material.
     */
    final float shininess;

    /**
     * Constructs a new material using the given properties.
     *
     * @param ambientAndDiffuse The ambient and diffuse reflection of the
     *                          material. To set them separately, use the
     *                          constructor overload.
     * @param specular          The specular reflection of the material.
     * @param shininess         The shininess of the material.
     */
    private Material(float[] ambientAndDiffuse, float[] specular, float shininess) {
        this(ambientAndDiffuse, ambientAndDiffuse, specular, shininess);
    }

    /**
     * Constructs a new material using the given properties.
     *
     * @param ambient   The ambient reflection of the material.
     * @param diffuse   The diffuse reflection of the material.
     * @param specular  The specular reflection of the material.
     * @param shininess The shininess of the material.
     */
    private Material(float[] ambient, float[] diffuse, float[] specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

}
