package robotrace;

/**
 Materials that can be used for the robots.
 */
public enum Material{

    /**
     Gold material properties.
     */
    GOLD(
            new float[]{0.8f, 0.8f, 0.4f, 1.0f},
            new float[]{0.8f, 0.8f, 0.4f, 1.0f},
            10.0f),
    /**
     Silver material properties.
     */
    SILVER(
            new float[]{0.8f, 0.8f, 0.8f, 1.0f},
            new float[]{0.0f, 0.0f, 0.0f, 1.0f},
            0f),
    /**
     Wood material properties.
     */
    WOOD(
            new float[]{0.8f, 0.8f, 0.8f, 1.0f},
            new float[]{0.8f, 0.8f, 0.0f, 1.0f},
            0f),
    /**
     Orange material properties.
     */
    ORANGE(
            new float[]{0.2f, 0.2f, 0.2f, 1.0f},
            new float[]{0.8f, 0.4f, 0.4f, 1.0f},
            0f),
    GROUND(new float[]{0.2f, 0.2f, 0.2f, 1.0f},
           new float[]{0.6f, 0.5f, 0.3f, 1.0f},
           0f),
    BOULDER(new float[]{0.4f, 0.4f, 0.3f, 1.0f},
            new float[]{0.4f, 0.4f, 0.3f, 1.0f},
            0f);

    /**
     The diffuse RGBA reflectance of the material.
     */
    final float[] diffuse;

    /**
     The specular RGBA reflectance of the material.
     */
    final float[] specular;

    /**
     The specular exponent of the material.
     */
    final float shininess;

    /**
     Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess){
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

}
