/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import Texture.ImplementedTexture;
import javax.media.opengl.GL2;

/**
 * A single OpenGL drawable element. This is a rigid shape, optionally defined
 * as part of a more complex {@link Body}.
 *
 * The actual data and index buffers are kept by the OpenGL libraries, instances
 * of this class just maintain the minimum required information to draw the
 * corresponding elements.
 *
 * @author Arjan Boschman
 */
public class Shape implements Body {

    private final int indexBufferName;
    private final int indexbufferLength;
    private final int shapeMode;
    private ImplementedTexture texture = null;

    /**
     * @param indexBufferName   The name of the index buffer used by this shape.
     *                          The actual buffer is help by OpenGL, this name
     *                          is how OpenGL refers to it.
     * @param indexbufferLength The length of the index buffer. This is the
     *                          number of vertices in use by this shape.
     * @param shapeMode         The shape mode is a reference to a {@link GL2}
     *                          enum constant. It tells the OpenGL library how
     *                          to draw this shape. The OpenGL version 2
     *                          specification tells us the following constants
     *                          are accepted: GL_POINTS, GL_LINE_STRIP,
     *                          GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP,
     *                          GL_TRIANGLE_FAN, GL_TRIANGLES, GL_QUAD_STRIP,
     *                          GL_QUADS, and GL_POLYGON.
     */
    public Shape(int indexBufferName, int indexbufferLength, int shapeMode) {
        this.indexBufferName = indexBufferName;
        this.indexbufferLength = indexbufferLength;
        this.shapeMode = shapeMode;
    }

    public Shape setTexture(ImplementedTexture texture) {
        this.texture = texture;
        return this;
    }

    public ImplementedTexture getTexture() {
        return texture;
    }

    @Override
    public void draw(GL2 gl) {
        if (texture != null) {
            texture.drawStart(gl);
        }
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexBufferName);
        gl.glDrawElements(shapeMode, indexbufferLength, BufferManager.INDEX_BUFFER_TYPE, BufferManager.INDEX_BUFFER_OFFSET);
        if (texture != null) {
            texture.drawEnd(gl);
        }
    }

}
