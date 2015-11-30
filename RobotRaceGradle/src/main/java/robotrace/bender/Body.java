/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace.bender;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.media.opengl.GL2;
import robotrace.bender.bodyassembly.Assembler;
import robotrace.bender.bodyassembly.Vertex;

/**
 * Convenience class used by {@link Bender} to draw the torso and head.
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Body {

    /**
     * Radius of the body at the hips.
     */
    private static final double RADIUS_SHINY = 0.175d;
    /**
     * Radius of the body at the chest.
     */
    private static final double RADIUS_TORSO = 0.225d;
    /**
     * Radius of the body at the start of the neck.
     */
    private static final double RADIUS_NECK = 0.125d;
    /**
     * Radius of the body at the end of the neck and the head.
     */
    private static final double RADIUS_HEAD = 0.125d;
    /**
     * Radius of the body at the antenna base.
     */
    private static final double RADIUS_ANTENNA_BOTTOM = 0.05d;
    /**
     * Radius of the body at the bottom of the antenna, just above the base.
     */
    private static final double RADIUS_ANTENNA_MIDDLE = 0.03d;
    /**
     * Radius of the body at the top of the antenna, just below the transmitter.
     */
    private static final double RADIUS_ANTENNA_TOP = 0.01d;
    /**
     * Radius of the body at the center of the transmitter ball.
     */
    private static final double RADIUS_ANTENNA_BALL_MIDDLE = 0.025d;
    /**
     * Radius of the body at the top of the transmitter.
     */
    private static final double RADIUS_ANTENNA_BALL_TOP = 0d;

    /**
     * Height of the body at the hips.
     */
    private static final double HEIGHT_SHINY = 0d;
    /**
     * Height of the body at the chest.
     */
    private static final double HEIGHT_TORSO = 0.45d;
    /**
     * Height of the body at the start of the head.
     */
    private static final double HEIGHT_NECK = 0.55d;
    /**
     * Height of the body at where the head starts to curve inwards.
     */
    private static final double HEIGHT_HEAD = 0.875d;
    /**
     * Height of the body at the base of the antenna.
     */
    private static final double HEIGHT_ANTENNA_BOTTOM = 1d;
    /**
     * Height of the body at the top of the antenna base.
     */
    private static final double HEIGHT_ANTENNA_MIDDLE = 1.025d;
    /**
     * Height of the body at the top of the antenna, just below the transmitter.
     */
    private static final double HEIGHT_ANTENNA_TOP = 1.175d;
    /**
     * Height of the body at the center of the transmitter.
     */
    private static final double HEIGHT_ANTENNA_BALL_MIDDLE = HEIGHT_ANTENNA_TOP + RADIUS_ANTENNA_BALL_MIDDLE; //1.1875d;
    /**
     * Height of the body at the top of the transmitter.
     */
    private static final double HEIGHT_ANTENNA_BALL_TOP = HEIGHT_ANTENNA_BALL_MIDDLE + RADIUS_ANTENNA_BALL_MIDDLE; //1.2d;

    /**
     * The number of edges to give the rings of the various shapes.
     */
    private static final int SLICE_COUNT = 50;
    /**
     * The number of rings to use when calculating a partial torus curve.
     */
    private static final int STACK_COUNT = 20;

    private int glDataBufferName;
    private int[] glIndicesBufferNames;

    private List<IntBuffer> indicesBufferList;
    private List<Boolean> surfaceTypeList;

    public void initialize(GL2 gl) {
        /**
         * Make a new instance of Assembler and add the shapes to it one by one.
         */
        final Assembler bodyAssembler = new Assembler();
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_SHINY, RADIUS_TORSO, HEIGHT_SHINY, HEIGHT_TORSO, true, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_TORSO, RADIUS_NECK, HEIGHT_TORSO, HEIGHT_NECK, false, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_NECK, RADIUS_HEAD, HEIGHT_NECK, HEIGHT_HEAD, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_HEAD, RADIUS_ANTENNA_BOTTOM, HEIGHT_HEAD, HEIGHT_ANTENNA_BOTTOM, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_BOTTOM, RADIUS_ANTENNA_MIDDLE, HEIGHT_ANTENNA_BOTTOM, HEIGHT_ANTENNA_MIDDLE, false, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_ANTENNA_MIDDLE, RADIUS_ANTENNA_TOP, HEIGHT_ANTENNA_MIDDLE, HEIGHT_ANTENNA_TOP, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_TOP, RADIUS_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_BALL_MIDDLE, RADIUS_ANTENNA_BALL_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_BALL_TOP, false, false);

        /**
         * Tell Assembler to compile all the surfaces and then retrieve the
         * necessary values.
         */
        bodyAssembler.compileSurfaceCompilation();
        //Buffer containing all Vertextdata off all previously added shapes. 
        //The data in in the format: vertexX, vertexY, vertexZ, normalX, normalY, normalZ.
        final DoubleBuffer dataBuffer = bodyAssembler.getDataBuffer();
        /**
         * List of index buffers. Each index buffer belongs to a shape and
         * consists of pointers to vertices in the data buffer.
         */
        indicesBufferList = bodyAssembler.getIndicesBuffers();
        /**
         * List of boolean flags. Runs parallel to the indicesBufferList and is
         * true if the shape is a polygon, false if it's a QuadStrip.
         */
        surfaceTypeList = bodyAssembler.getSurfaceTypeList();

        /**
         * Let OpenGL generate some buffer names. Our data buffer and index
         * buffers will each be stored under such a name.
         */
        final int[] tempBufferNames = new int[indicesBufferList.size() + 1];
        gl.glGenBuffers(tempBufferNames.length, tempBufferNames, 0);
        this.glIndicesBufferNames = new int[indicesBufferList.size()];
        System.arraycopy(tempBufferNames, 0, glIndicesBufferNames, 0, glIndicesBufferNames.length);
        this.glDataBufferName = tempBufferNames[tempBufferNames.length - 1];

        //Push the data buffer to OpenGL.
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glDataBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, dataBuffer.capacity() * Double.BYTES, dataBuffer, GL2.GL_STATIC_DRAW);

        //Push each of the index buffers to OpenGL.
        for (IntBuffer buffer : indicesBufferList) {
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glIndicesBufferNames[indicesBufferList.indexOf(buffer)]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * Integer.BYTES, buffer, GL2.GL_STATIC_DRAW);
        }
    }

    /**
     * To be called in the draw loop. Uses the given instance of GL2 to draw the
     * body.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    public void draw(GL2 gl) {
        //Use the name under which the data buffer was stored to bind it.
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glDataBufferName);
        /**
         * Tell OpenGL what formats and what stride length to expect when
         * extracting vertex coordinate information from the data buffer.
         */
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, 0);
        /**
         * Tell OpenGL what formats, stride length and offset to expect when
         * extracting normal information from the data buffer.
         */
        gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, Vertex.COORD_COUNT * Double.BYTES);
        //Loop over all shapes and draw them one by one.
        for (int buffInd = 0; buffInd < indicesBufferList.size(); buffInd++) {
            //Use the appropriate index buffer name to bind the correct index buffer,
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glIndicesBufferNames[buffInd]);
            final int shapeType = (surfaceTypeList.get(buffInd) ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP));
            gl.glDrawElements(shapeType, indicesBufferList.get(buffInd).capacity(), GL2.GL_UNSIGNED_INT, 0);
        }
    }

}
