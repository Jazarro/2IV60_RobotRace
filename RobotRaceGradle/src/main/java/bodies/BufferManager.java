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
package bodies;

import bodies.assembly.Vertex;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;

/**
 * Manages a data buffer. This class acts as a link to OpenGL during the draw
 * phase. Use its Initialiser subclass during the initialisation phase.
 *
 * @author Arjan Boschman
 */
public class BufferManager {

    /**
     * The type format used for the index buffers. This integer refers to a
     * {@link GL2} enum value.
     */
    public static final int INDEX_BUFFER_TYPE = GL2.GL_UNSIGNED_INT;
    /**
     * The default buffer offset used for index buffers.
     */
    public static final int INDEX_BUFFER_OFFSET = 0;

    private int dataBufferName;

    public Initialiser makeInitialiser(GL2 gl) {
        return new Initialiser(gl);
    }

    /**
     * Must be called at the start of the draw phase, before any of the bodies
     * managed by this instance are drawn.
     *
     * @param gl The instance of GL2 responsible for drawing the bodies.
     * @see #endDraw Must be called in conjunction with this method.
     */
    public void startDraw(GL2 gl) {
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        //Use the name under which the data buffer was stored to bind it.
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, dataBufferName);
        final int stride = Vertex.NR_VERTEX_ELEMENTS * Vertex.COORD_COUNT * Float.BYTES;
        /**
         * Tell OpenGL what formats and what stride length to expect when
         * extracting vertex coordinate information from the data buffer.
         */
        final int positionPointerOffset = 0 * Vertex.COORD_COUNT * Float.BYTES;
        gl.glVertexPointer(Vertex.COORD_COUNT, GL2.GL_FLOAT, stride, positionPointerOffset);
        /**
         * Tell OpenGL what formats, stride length and offset to expect when
         * extracting normal information from the data buffer.
         */
        final int normalPointerOffset = 1 * Vertex.COORD_COUNT * Float.BYTES;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, normalPointerOffset);
        /**
         * Tell OpenGL what formats, stride length and offset to expect when
         * extracting texture information from the data buffer.
         */
        final int texturePointerOffset = 2 * Vertex.COORD_COUNT * Float.BYTES;
        gl.glTexCoordPointer(Vertex.COORD_COUNT, GL2.GL_FLOAT, stride, texturePointerOffset);
    }

    /**
     * Must be called at the end of the draw phase, after all the bodies managed
     * by this instance are drawn.
     *
     * @param gl The instance of GL2 responsible for drawing the bodies.
     * @see #startDraw Must be called in conjunction with this method.
     */
    public void endDraw(GL2 gl) {
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    @SuppressWarnings("PublicInnerClass")
    public class Initialiser {

        private final GL2 gl;
        /**
         * The data buffer; contains all vertices and normals given to this
         * object through {@link #addData}.
         */
        private FloatBuffer dataBuffer = FloatBuffer.allocate(0);

        private Initialiser(GL2 gl) {
            this.gl = gl;
        }

        /**
         * This method must be called at the end of the initialisation phase. It
         * prepares the enclosing BodyManager instance for the draw phase.
         *
         * It is very important that this method is called, for the BodyManager
         * to function. After calling this method, the Initialiser cannot be
         * used anymore.
         */
        public void finish() {
            //Obtain buffer name.
            final int[] singleBufferName = new int[1];
            gl.glGenBuffers(1, singleBufferName, 0);
            BufferManager.this.dataBufferName = singleBufferName[0];
            //Push the data buffer to OpenGL.
            gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, BufferManager.this.dataBufferName);
            gl.glBufferData(GL2.GL_ARRAY_BUFFER, dataBuffer.capacity() * Float.BYTES, dataBuffer, GL2.GL_STATIC_DRAW);
            //Erase buffer data so nothing will stay in memory or silently fail if people abuse this class.
            dataBuffer = null;
        }

        /**
         * Adds some vertices to the data buffer. Also adds an index buffer to
         * OpenGL.
         *
         * @param data        This data will be appended to the existing data
         *                    buffer that is backing this Initialiser. The data
         *                    doesn't get sent to OpenGL yet, that only happens
         *                    at the end of this objects lifespan, upon calling
         *                    {@link #finish()}.
         * @param indexBuffer This index buffer will be sent to OpenGl
         *                    immediately.
         * @return The index buffer name that was used to register the given
         *         indexBuffer with OpenGL.
         */
        public int addData(FloatBuffer data, IntBuffer indexBuffer) {
            final List<IntBuffer> list = new ArrayList<>();
            list.add(indexBuffer);
            return addData(data, list)[0];
        }

        /**
         * Adds some vertices to the data buffer. Also optionally adds one or
         * more index buffers to OpenGL.
         *
         * @param data         This data will be appended to the existing data
         *                     buffer that is backing this Initialiser. The data
         *                     doesn't get sent to OpenGL yet, that only happens
         *                     at the end of this objects lifespan, upon calling
         *                     {@link #finish()}.
         * @param indexBuffers These index buffers will be sent to OpenGl
         *                     immediately.
         * @return An array of index buffer names, parallel to the given List of
         *         index buffers. These are the names that each of the index
         *         buffers is registered under with OpenGL.
         */
        public int[] addData(FloatBuffer data, List<IntBuffer> indexBuffers) {
            //Append databuffer to the one already stored in here.
            final int coordLength = Vertex.NR_VERTEX_ELEMENTS * Vertex.COORD_COUNT;
            final int oldLength = appendToDataBuffer(data) / coordLength;
            //Increment each index by the previous length of the databuffer.
            incrementEachElement(indexBuffers, oldLength);
            //Retrieve index buffer names and push the buffers to OpenGL.
            final int[] bufferNames = pushIndexArraysToGL(indexBuffers);
            //Return index buffer names.
            return bufferNames;
        }

        /**
         * Append the given buffer to the data buffer stored as a class member.
         *
         * @param addition The buffer to append.
         * @return The length of the old buffer.
         */
        private int appendToDataBuffer(FloatBuffer addition) {
            final int oldLength = dataBuffer.capacity();
            final FloatBuffer newData = FloatBuffer.allocate(oldLength + addition.capacity());
            newData.put(dataBuffer);
            newData.position(oldLength);
            newData.put(addition);
            newData.position(0);
            dataBuffer = newData;
            return oldLength;
        }

        /**
         * Increment all the elements of all the buffers by the given amount.
         *
         * @param intBuffers  The buffers.
         * @param incrementBy The amount to increments all the elements by.
         */
        private void incrementEachElement(List<IntBuffer> intBuffers, int incrementBy) {
            intBuffers.stream().forEach((intBuffer) -> {
                for (int i = 0; i < intBuffer.capacity(); i++) {
                    intBuffer.put(i, intBuffer.get(i) + incrementBy);
                }
                intBuffer.position(0);
            });
        }

        /**
         * Let OpenGL generate some buffer names and register each index buffer
         * to each of the names.
         *
         * @param indexBuffers The index buffer that are to be registered with
         *                     OpenGL.
         * @return The buffer names. Each of these corresponds to one of the
         *         index buffers given as a parameter.
         */
        private int[] pushIndexArraysToGL(List<IntBuffer> indexBuffers) {
            final int[] bufferNames = new int[indexBuffers.size()];
            gl.glGenBuffers(bufferNames.length, bufferNames, 0);
            for (int i = 0; i < indexBuffers.size(); i++) {
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferNames[i]);
                gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
                        indexBuffers.get(i).capacity() * Integer.BYTES,
                        indexBuffers.get(i), GL2.GL_STATIC_DRAW);
            }
            return bufferNames;
        }
    }

}
