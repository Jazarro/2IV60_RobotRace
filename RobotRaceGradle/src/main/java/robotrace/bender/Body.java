package robotrace.bender;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.media.opengl.GL2;
import utility.Assembler;

public class Body {

    private static final double RADIUS_SHINY = 0.175d;
    private static final double RADIUS_TORSO = 0.225d;
    private static final double RADIUS_NECK = 0.125d;
    private static final double RADIUS_HEAD = 0.125d;
    private static final double RADIUS_ANTENNA_BOTTOM = 0.05d;
    private static final double RADIUS_ANTENNA_MIDDLE = 0.03d;
    private static final double RADIUS_ANTENNA_TOP = 0.01d;
    private static final double RADIUS_ANTENNA_BALL_MIDDLE = 0.025d;
    private static final double RADIUS_ANTENNA_BALL_TOP = 0d;

    private static final double HEIGHT_SHINY = 0d;
    private static final double HEIGHT_TORSO = 0.45d;
    private static final double HEIGHT_NECK = 0.55d;
    private static final double HEIGHT_HEAD = 0.875d;
    private static final double HEIGHT_ANTENNA_BOTTOM = 1d;
    private static final double HEIGHT_ANTENNA_MIDDLE = 1.025d;
    private static final double HEIGHT_ANTENNA_TOP = 1.175d;
    private static final double HEIGHT_ANTENNA_BALL_MIDDLE = 1.1875d;
    private static final double HEIGHT_ANTENNA_BALL_TOP = 1.2d;

    private static final int SLICE_COUNT = 50;
    private static final int STACK_COUNT = 20;

    private int glDataBufferName;
    private int[] glIndicesBufferNames;

    private DoubleBuffer dataBuffer;
    private List<IntBuffer> indicesBufferList;
    private List<Boolean> surfaceTypeList;

    public void initialize(GL2 gl) {
        final Assembler bodyAssembler = new Assembler();
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_SHINY, RADIUS_TORSO, HEIGHT_SHINY, HEIGHT_TORSO, true, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_TORSO, RADIUS_NECK, HEIGHT_TORSO, HEIGHT_NECK, false, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_NECK, RADIUS_HEAD, HEIGHT_NECK, HEIGHT_HEAD, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_HEAD, RADIUS_ANTENNA_BOTTOM, HEIGHT_HEAD, HEIGHT_ANTENNA_BOTTOM, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_BOTTOM, RADIUS_ANTENNA_MIDDLE, HEIGHT_ANTENNA_BOTTOM, HEIGHT_ANTENNA_MIDDLE, false, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_ANTENNA_MIDDLE, RADIUS_ANTENNA_TOP, HEIGHT_ANTENNA_MIDDLE, HEIGHT_ANTENNA_TOP, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_TOP, RADIUS_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_BALL_MIDDLE, RADIUS_ANTENNA_BALL_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_BALL_TOP, false, false);
        //TODO: Fix ball on antenna

        bodyAssembler.compileSurfaceCompilation();
        dataBuffer = bodyAssembler.getDataBuffer();
        indicesBufferList = bodyAssembler.getIndicesBuffers();
        surfaceTypeList = bodyAssembler.getSurfaceTypeList();

        final int[] tempBufferNames = new int[indicesBufferList.size() + 1];
        gl.glGenBuffers(tempBufferNames.length, tempBufferNames, 0);
        this.glIndicesBufferNames = new int[indicesBufferList.size()];
        System.arraycopy(tempBufferNames, 0, glIndicesBufferNames, 0, glIndicesBufferNames.length);
        this.glDataBufferName = tempBufferNames[tempBufferNames.length - 1];

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glDataBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, dataBuffer.capacity() * Double.BYTES, dataBuffer, GL2.GL_STATIC_DRAW);

        for (IntBuffer buffer : indicesBufferList) {
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glIndicesBufferNames[indicesBufferList.indexOf(buffer)]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * Integer.BYTES, buffer, GL2.GL_STATIC_DRAW);
        }
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glDataBufferName);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 3 * 2 * Double.BYTES, 0);//todo: COORD_COUNT
        gl.glNormalPointer(GL2.GL_DOUBLE, 3 * 2 * Double.BYTES, 3 * Double.BYTES);//todo: COORD_COUNT
        for (int i = 0; i < indicesBufferList.size(); i++) {
            drawBuffer(gl, i);
        }
        gl.glPopMatrix();
    }

    private void drawBuffer(GL2 gl, int buffInd) {
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glIndicesBufferNames[buffInd]);
        gl.glDrawElements((surfaceTypeList.get(buffInd) ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)), indicesBufferList.get(buffInd).capacity(), GL2.GL_UNSIGNED_INT, 0);
    }

}
