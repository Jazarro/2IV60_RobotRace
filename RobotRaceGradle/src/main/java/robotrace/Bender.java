package robotrace;

import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;

public class Bender {

    private static final int NUMCOORD = 3;

    private static final int BODY_SLICE_COUNT = 50;
    private static final int BODY_STACK_COUNT = 50;
    private static final int BODY_SIMPLE_EDGE_COUNT = 5;
    private static final int BODY_TOTAL_EDGE_COUNT = BODY_SIMPLE_EDGE_COUNT + 4 * BODY_STACK_COUNT;
    private static final int BODY_BUFF_COUNT = 8;
    private static final double BODY_RADII[] = {0.175d, 0.225d, 0.125d, 0.125d, 0.01d, 0.05d, 0.03d, 0.025d};
    private static final double BODY_HEIGHTS[] = {0.6d, 1.05d, 1.15d, 1.475d, 1.775d, 1.475d + Math.sqrt(Math.pow(BODY_RADII[3], 2d) - Math.pow(BODY_RADII[5], 2d)), 1d, 1.775d + Math.sqrt(Math.pow(BODY_RADII[7], 2d) - Math.pow(BODY_RADII[4], 2d))};
    private static final int BODY_BUFF_SIZE[] = {BODY_SLICE_COUNT + 1,
        (BODY_SLICE_COUNT + 1) * 2,
        (BODY_SLICE_COUNT + 1) * 2,
        (BODY_SLICE_COUNT + 1) * 2,
        (BODY_SLICE_COUNT + 1) * 2 * BODY_STACK_COUNT,
        (BODY_SLICE_COUNT + 1) * 2 * BODY_STACK_COUNT,
        (BODY_SLICE_COUNT + 1) * 2,
        (BODY_SLICE_COUNT + 1) * 4 * BODY_STACK_COUNT};
    private static final int BODY_BUFF_TYPE[] = {GL2.GL_POLYGON,
        GL2.GL_QUAD_STRIP,
        GL2.GL_QUAD_STRIP,
        GL2.GL_QUAD_STRIP,
        GL2.GL_QUAD_STRIP,
        GL2.GL_QUAD_STRIP,
        GL2.GL_QUAD_STRIP,
        GL2.GL_QUAD_STRIP};

    private final double[] bodyVrtx;
    private final int[][] bodyInds;
    private final int[] bodyBuff;

    public Bender() {
        bodyVrtx = new double[(BODY_SLICE_COUNT + 1) * (BODY_TOTAL_EDGE_COUNT + 1) * NUMCOORD];
        bodyInds = new int[BODY_BUFF_COUNT][];
        for (int i = 0; i < BODY_BUFF_COUNT; i++) {
            bodyInds[i] = new int[BODY_BUFF_SIZE[i]];
        }
        bodyBuff = new int[BODY_BUFF_COUNT + 1];
    }

    public void initialize(GL2 gl) {
        bodyInit(gl);
    }

    private void bodyInit(GL2 gl) {
        for (int i = 0; i <= BODY_SLICE_COUNT; i++) {
            for (int j = 0; j < BODY_SIMPLE_EDGE_COUNT; j++) {
                System.arraycopy(calcCoord(i, BODY_RADII[j], BODY_HEIGHTS[j]), 0, bodyVrtx, (i * BODY_TOTAL_EDGE_COUNT + j) * NUMCOORD, 3);
            }
            bodyInds[0][i] = (i * BODY_TOTAL_EDGE_COUNT + 0);
            bodyInds[1][i * 2 + 0] = (i * BODY_TOTAL_EDGE_COUNT + 0);
            bodyInds[1][i * 2 + 1] = (i * BODY_TOTAL_EDGE_COUNT + 1);
            bodyInds[2][i * 2 + 0] = (i * BODY_TOTAL_EDGE_COUNT + 1);
            bodyInds[2][i * 2 + 1] = (i * BODY_TOTAL_EDGE_COUNT + 2);
            bodyInds[3][i * 2 + 0] = (i * BODY_TOTAL_EDGE_COUNT + 2);
            bodyInds[3][i * 2 + 1] = (i * BODY_TOTAL_EDGE_COUNT + 3);
            for (int k = 0; k < BODY_STACK_COUNT; k++) {
                System.arraycopy(calcCoord(i,
                        BODY_RADII[3] * Math.cos(Math.toRadians((k + 1) * 90 / BODY_STACK_COUNT)),
                        BODY_HEIGHTS[3] + (BODY_RADII[3] * Math.sin(Math.toRadians((k + 1) * 90 / BODY_STACK_COUNT)))),
                        0, bodyVrtx, ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + k) * NUMCOORD, 3);
                bodyInds[4][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + k - 1);
                bodyInds[4][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + k - 0);
            }
            bodyInds[4][(i * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT - 2);
            for (int k = 0; k < BODY_STACK_COUNT; k++) {
                System.arraycopy(calcCoord(i,
                        BODY_RADII[6] + ((BODY_RADII[5] - BODY_RADII[6]) * Math.cos(Math.toRadians(k * 90 / BODY_STACK_COUNT))),
                        BODY_HEIGHTS[5] + ((BODY_RADII[5] - BODY_RADII[6]) * Math.sin(Math.toRadians(k * 90 / BODY_STACK_COUNT)))),
                        0, bodyVrtx, ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + BODY_STACK_COUNT + k) * NUMCOORD, 3);
                bodyInds[5][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + BODY_STACK_COUNT + k + 0);
                bodyInds[5][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + BODY_STACK_COUNT + k + 1);
            }
            bodyInds[6][i * 2 + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT - 1);
            bodyInds[6][i * 2 + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + 4);
            for (int k = 0; k < (2 * BODY_STACK_COUNT); k++) {
                System.arraycopy(calcCoord(i,
                        BODY_RADII[7] * Math.sin(Math.toRadians(k * 180 / (2 * (BODY_STACK_COUNT - 1)))),
                        BODY_HEIGHTS[7] + (BODY_RADII[7] * Math.cos(Math.toRadians(k * 180 / (2 * (BODY_STACK_COUNT - 1)) - 180)))),
                        0, bodyVrtx, ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT + k) * NUMCOORD, 3);
                bodyInds[7][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT + k + 0);
                bodyInds[7][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT + k + 1);
            }
            bodyInds[7][((i + (((2 * BODY_STACK_COUNT) - 1) * (BODY_SLICE_COUNT + 1))) * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT + ((2 * (BODY_STACK_COUNT - 1)) - 1) + 0);
            bodyInds[7][((i + (((2 * BODY_STACK_COUNT) - 1) * (BODY_SLICE_COUNT + 1))) * 2) + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT + ((2 * (BODY_STACK_COUNT - 1)) - 1) + 1);
        }

        gl.glGenBuffers(BODY_BUFF_COUNT + 1, bodyBuff, 0);

        DoubleBuffer bodyVrtxBuff = Buffers.newDirectDoubleBuffer(bodyVrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bodyBuff[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, bodyVrtx.length * Double.BYTES, bodyVrtxBuff, GL2.GL_STATIC_DRAW);

        for (int i = 0; i < BODY_BUFF_COUNT; i++) {
            IntBuffer bodyIndsBuff = Buffers.newDirectIntBuffer(bodyInds[i]);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyBuff[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyInds[i].length * Integer.BYTES, bodyIndsBuff, GL2.GL_STATIC_DRAW);
        }
    }

    private double[] calcCoord(int angleIndex, double radius, double height) {
        double coord[] = new double[3];
        coord[0] = radius * Math.cos(Math.toRadians(angleIndex * 360 / BODY_SLICE_COUNT));
        coord[1] = radius * Math.sin(Math.toRadians(angleIndex * 360 / BODY_SLICE_COUNT));
        coord[2] = height;
        return coord;
    }

    public void draw(GL2 gl) {
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bodyBuff[0]);
        gl.glVertexPointer(NUMCOORD, GL2.GL_DOUBLE, 0, 0);

        for (int i = 0; i < BODY_BUFF_COUNT; i++) {
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyBuff[i + 1]);
            gl.glDrawElements(BODY_BUFF_TYPE[i], bodyInds[i].length, GL2.GL_UNSIGNED_INT, 0);
        }

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

}
