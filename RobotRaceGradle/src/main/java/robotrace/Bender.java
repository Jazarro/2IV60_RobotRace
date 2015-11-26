package robotrace;

import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;

public class Bender{

    private static final int NUMCOORD = 3;

    private static final double BODY_RADII[] = {0.175d, 0.225d, 0.125d, 0.125d, 0.01d, 0.05d, 0.03d, 0.025d};
    private static final double BODY_HEIGHTS[] = {0.6d, 1.05d, 1.15d, 1.475d, 1.775d, 1.475d + Math.sqrt(Math.pow(BODY_RADII[3], 2d) - Math.pow(BODY_RADII[5], 2d)), 1d, 1.775d + Math.sqrt(Math.pow(BODY_RADII[7], 2d) - Math.pow(BODY_RADII[4], 2d))};
    private static final int BODY_SLICE_COUNT = 50;
    private static final int BODY_STACK_COUNT = 50;
    private static final int BODY_SIMPLE_EDGE_COUNT = 5;
    private static final int BODY_TOTAL_EDGE_COUNT = BODY_SIMPLE_EDGE_COUNT + 4 * BODY_STACK_COUNT;
    private static final int BODY_BUFF_COUNT = 8;
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

    private static final double LIMB_RING_HEIGHT = 0.5d / 6d;
    private static final double LIMB_RING_RADIUS = 0.05d;
    private static final int LIMB_RING_COUNT = 6;
    private static final int LIMB_SLICE_COUNT = 50;
    private static final int LIMB_EDGE_COUNT = 2;
    private static final int LIMB_BUFF_COUNT = 1;
    private static final int LIMB_BUFF_SIZE[] = {(LIMB_SLICE_COUNT + 1) * 2};
    private static final int LIMB_BUFF_TYPE[] = {GL2.GL_QUAD_STRIP};

    private final double[] bodyVrtx;
    private final int[][] bodyInds;
    private final int[] bodyBuff;

    private final double[] limbVrtx;
    private final int[][] limbInds;
    private final int[] limbBuff;

    private double[][] legLeftAngle = {{45d, 45d, 0d, 0d, 0d, 0d, 0d},
                                       {10d, 10d, 0d, 0d, 0d, 0d, 0d}};
    private double[][] legRightAngle = {{45d, 45d, 0d, 0d, 0d, 0d, 0d},
                                        {10d, 10d, 0d, 0d, 0d, 0d, 0d}};
    private double[][] armLeftAngle = {{10d, 10d, 0d, 0d, 0d, 0d, 0d},
                                       {45d, 45d, 0d, 0d, 0d, 0d, 0d}};
    private double[][] armRightAngle = {{10d, 10d, 0d, 0d, 0d, 0d, 0d},
                                        {45d, 45d, 0d, 0d, 0d, 0d, 0d}};

    public Bender(){
        bodyVrtx = new double[(BODY_SLICE_COUNT + 1) * (BODY_TOTAL_EDGE_COUNT + 1) * NUMCOORD];
        bodyInds = new int[BODY_BUFF_COUNT][];
        for(int i = 0; i < BODY_BUFF_COUNT; i++){
            bodyInds[i] = new int[BODY_BUFF_SIZE[i]];
        }
        bodyBuff = new int[BODY_BUFF_COUNT + 1];

        limbVrtx = new double[(LIMB_SLICE_COUNT + 1) * (LIMB_EDGE_COUNT) * NUMCOORD];
        limbInds = new int[LIMB_BUFF_COUNT][];
        for(int i = 0; i < LIMB_BUFF_COUNT; i++){
            limbInds[i] = new int[LIMB_BUFF_SIZE[i]];
        }
        limbBuff = new int[LIMB_BUFF_COUNT + 1];
    }

    public void initialize(GL2 gl){
        bodyInit(gl);
        limbInit(gl);
    }

    private void bodyInit(GL2 gl){
        for(int i = 0; i <= BODY_SLICE_COUNT; i++){
            for(int j = 0; j < BODY_SIMPLE_EDGE_COUNT; j++){
                System.arraycopy(calcCoord(i, BODY_RADII[j], BODY_HEIGHTS[j], BODY_SLICE_COUNT), 0, bodyVrtx, (i * BODY_TOTAL_EDGE_COUNT + j) * NUMCOORD, NUMCOORD);
            }
            bodyInds[0][i] = (i * BODY_TOTAL_EDGE_COUNT + 0);
            bodyInds[1][i * 2 + 0] = (i * BODY_TOTAL_EDGE_COUNT + 0);
            bodyInds[1][i * 2 + 1] = (i * BODY_TOTAL_EDGE_COUNT + 1);
            bodyInds[2][i * 2 + 0] = (i * BODY_TOTAL_EDGE_COUNT + 1);
            bodyInds[2][i * 2 + 1] = (i * BODY_TOTAL_EDGE_COUNT + 2);
            bodyInds[3][i * 2 + 0] = (i * BODY_TOTAL_EDGE_COUNT + 2);
            bodyInds[3][i * 2 + 1] = (i * BODY_TOTAL_EDGE_COUNT + 3);
            for(int k = 0; k < BODY_STACK_COUNT; k++){
                System.arraycopy(calcCoord(i,
                                           BODY_RADII[3] * Math.cos(Math.toRadians((k + 1) * 90 / BODY_STACK_COUNT)),
                                           BODY_HEIGHTS[3] + (BODY_RADII[3] * Math.sin(Math.toRadians((k + 1) * 90 / BODY_STACK_COUNT))),
                                           BODY_SLICE_COUNT),
                                 0, bodyVrtx, ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + k) * NUMCOORD, NUMCOORD);
                bodyInds[4][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + k - 1);
                bodyInds[4][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + k - 0);
            }
            bodyInds[4][(i * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT - 2);
            for(int k = 0; k < BODY_STACK_COUNT; k++){
                System.arraycopy(calcCoord(i,
                                           BODY_RADII[6] + ((BODY_RADII[5] - BODY_RADII[6]) * Math.cos(Math.toRadians(k * 90 / BODY_STACK_COUNT))),
                                           BODY_HEIGHTS[5] + ((BODY_RADII[5] - BODY_RADII[6]) * Math.sin(Math.toRadians(k * 90 / BODY_STACK_COUNT))),
                                           BODY_SLICE_COUNT),
                                 0, bodyVrtx, ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + BODY_STACK_COUNT + k) * NUMCOORD, NUMCOORD);
                bodyInds[5][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + BODY_STACK_COUNT + k + 0);
                bodyInds[5][((i + (k * (BODY_SLICE_COUNT + 1))) * 2) + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + BODY_STACK_COUNT + k + 1);
            }
            bodyInds[6][i * 2 + 0] = ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT - 1);
            bodyInds[6][i * 2 + 1] = ((i * BODY_TOTAL_EDGE_COUNT) + 4);
            for(int k = 0; k < (2 * BODY_STACK_COUNT); k++){
                System.arraycopy(calcCoord(i,
                                           BODY_RADII[7] * Math.sin(Math.toRadians(k * 180 / (2 * (BODY_STACK_COUNT - 1)))),
                                           BODY_HEIGHTS[7] + (BODY_RADII[7] * Math.cos(Math.toRadians(k * 180 / (2 * (BODY_STACK_COUNT - 1)) - 180))),
                                           BODY_SLICE_COUNT),
                                 0, bodyVrtx, ((i * BODY_TOTAL_EDGE_COUNT) + BODY_SIMPLE_EDGE_COUNT + 2 * BODY_STACK_COUNT + k) * NUMCOORD, NUMCOORD);
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

        for(int i = 0; i < BODY_BUFF_COUNT; i++){
            IntBuffer bodyIndsBuff = Buffers.newDirectIntBuffer(bodyInds[i]);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyBuff[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyInds[i].length * Integer.BYTES, bodyIndsBuff, GL2.GL_STATIC_DRAW);
        }
    }

    private void limbInit(GL2 gl){
        for(int i = 0; i <= LIMB_SLICE_COUNT; i++){
            System.arraycopy(calcCoord(i, LIMB_RING_RADIUS, 0, LIMB_SLICE_COUNT), 0, limbVrtx, (i * LIMB_EDGE_COUNT + 0) * NUMCOORD, NUMCOORD);
            System.arraycopy(calcCoord(i, LIMB_RING_RADIUS, LIMB_RING_HEIGHT, LIMB_SLICE_COUNT), 0, limbVrtx, (i * LIMB_EDGE_COUNT + 1) * NUMCOORD, NUMCOORD);
            limbInds[0][i * 2 + 0] = (i * LIMB_EDGE_COUNT + 0);
            limbInds[0][i * 2 + 1] = (i * LIMB_EDGE_COUNT + 1);
        }

        gl.glGenBuffers(LIMB_BUFF_COUNT + 1, limbBuff, 0);

        DoubleBuffer limbVrtxBuff = Buffers.newDirectDoubleBuffer(limbVrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, limbBuff[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, limbVrtx.length * Double.BYTES, limbVrtxBuff, GL2.GL_STATIC_DRAW);

        for(int i = 0; i < LIMB_BUFF_COUNT; i++){
            IntBuffer limbIndsBuff = Buffers.newDirectIntBuffer(limbInds[i]);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, limbBuff[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, limbInds[i].length * Integer.BYTES, limbIndsBuff, GL2.GL_STATIC_DRAW);
        }
    }

    private double[] calcCoord(int angleIndex, double radius, double height, int sliceCount){
        double coord[] = new double[3];
        coord[0] = radius * Math.cos(Math.toRadians(angleIndex * 360 / sliceCount));
        coord[1] = radius * Math.sin(Math.toRadians(angleIndex * 360 / sliceCount));
        coord[2] = height;
        return coord;
    }

    public void draw(GL2 gl){
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glPushMatrix();

        drawBody(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.1d, 0d, 0.6d);
        drawLimb(gl, legLeftAngle);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.1d, 0d, 0.6d);
        drawLimb(gl, legRightAngle);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.2d, 0d, 0.975d);
        gl.glRotated(90d, 0d, 1d, 0d);
        drawLimb(gl, armLeftAngle);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.2d, 0d, 0.975d);
        gl.glRotated(-90d, 0d, 1d, 0d);
        drawLimb(gl, armRightAngle);
        

        gl.glPopMatrix();
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    private void drawBody(GL2 gl){
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bodyBuff[0]);
        gl.glVertexPointer(NUMCOORD, GL2.GL_DOUBLE, 0, 0);
        for(int i = 0; i < BODY_BUFF_COUNT; i++){
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyBuff[i + 1]);
            gl.glDrawElements(BODY_BUFF_TYPE[i], bodyInds[i].length, GL2.GL_UNSIGNED_INT, 0);
        }
    }

    private void drawLimb(GL2 gl, double[][] angles){
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, limbBuff[0]);
        gl.glVertexPointer(NUMCOORD, GL2.GL_DOUBLE, 0, 0);
        for(int i = 0; i < LIMB_RING_COUNT; i++){
            gl.glRotated(angles[0][i], 1d, 0d, -LIMB_RING_HEIGHT * i);
            gl.glRotated(angles[1][i], 0d, 1d, -LIMB_RING_HEIGHT * i);
            gl.glTranslated(0d, 0d, -LIMB_RING_HEIGHT);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, limbBuff[1]);
            gl.glDrawElements(LIMB_BUFF_TYPE[0], limbInds[0].length, GL2.GL_UNSIGNED_INT, 0);
        }
    }
}
