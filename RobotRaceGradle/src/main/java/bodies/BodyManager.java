/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.media.opengl.GL2;

/**
 *
 * @author Arjan Boschman
 */
public class BodyManager {

    /**
     * The type format used for the index buffers. This integer refers to a
     * {@link GL2} enum value.
     */
    public static final int INDEX_BUFFER_TYPE = GL2.GL_UNSIGNED_INT;
    /**
     * The default buffer offset used for index buffers.
     */
    public static final int INDEX_BUFFER_OFFSET = 0;

    /**
     *
     * @param data
     * @param indexBuffers
     * @return Index buffer names.
     */
    public int[] njkew(DoubleBuffer data, List<IntBuffer> indexBuffers) {
        //TODO: 
        //Append databuffer to the one already stored in here.
        //Increment each index by the previous length of the databuffer.
        //Retrieve index buffer names and push them to OpenGL.
        //Return index buffer names.
        return new int[]{};
    }

}
