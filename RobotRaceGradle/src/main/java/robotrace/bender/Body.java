package robotrace.bender;

import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import utility.Assembler;

public class Body{

    private static final double RADII[] = {0.175d, 0.225d, 0.125d, 0.125d, 0.01d, 0.05d, 0.03d, 0.025d};
    //private static final double HEIGHTS[] = {0.6d, 1.05d, 1.15d, 1.475d, 1.775d, 1.475d + Math.sqrt(Math.pow(RADII[3], 2d) - Math.pow(RADII[5], 2d)), 1d, 1.775d + Math.sqrt(Math.pow(RADII[7], 2d) - Math.pow(RADII[4], 2d))};
    private static final double HEIGHTS[] = {0d, 0.45d, 0.55d, 0.875d, 1.175d, 0.875d + Math.sqrt(Math.pow(RADII[3], 2d) - Math.pow(RADII[5], 2d)), 1d, 1.175d + Math.sqrt(Math.pow(RADII[7], 2d) - Math.pow(RADII[4], 2d))};
    private static final int SLICE_COUNT = 50;
    private static final int STACK_COUNT = 50;
    private static final int SIMPLE_EDGE_COUNT = 5;
    private static final int TOTAL_EDGE_COUNT = SIMPLE_EDGE_COUNT + 4 * STACK_COUNT;
    private static final int BUFF_COUNT = 8;
    private static final int BUFF_SIZE[] = {SLICE_COUNT + 1,
                                            (SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 2 * STACK_COUNT,
                                            (SLICE_COUNT + 1) * 2 * STACK_COUNT,
                                            (SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 4 * STACK_COUNT};
    private static final int BUFF_TYPE[] = {GL2.GL_POLYGON,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP};

    /*    private final double[] vrtx;
    private final int[][] inds;*/
    private int[] glBufferNames;

    //private double[] testVrtx = new double[(SLICE_COUNT + 1) * (TOTAL_EDGE_COUNT + 1) * 3];
    private int[] testInds;
    Assembler.SurfaceCompilation surfaceCompilation;

    public Body(){
        /*        vrtx = new double[(SLICE_COUNT + 1) * (TOTAL_EDGE_COUNT + 1) * Bender.NUMCOORD];
        inds = new int[BUFF_COUNT][];
        for(int i = 0; i < BUFF_COUNT; i++){
            inds[i] = new int[BUFF_SIZE[i]];
        }*/
        //buff = new int[BUFF_COUNT + 1];
    }

    public void initialize(GL2 gl){
        final Assembler bodyAssembler = new Assembler();
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADII[0], RADII[1], HEIGHTS[0], HEIGHTS[1], true, false);
        surfaceCompilation = bodyAssembler.makeSurfaceCompilation();
        //final double[] testVrtx = Assembler.addPartialTorus(RADII[0], RADII[1], HEIGHTS[0], HEIGHTS[1], SLICE_COUNT, 1);
        //testVrtx = new double[tmpVrtx.length];
        //System.arraycopy(tmpVrtx, 0, testVrtx, 0, tmpVrtx.length);
        /*testInds[0] = SLICE_COUNT * 0 + 0;
        testInds[1] = SLICE_COUNT * 2 + 0;
        testInds[2] = SLICE_COUNT * 0 + 2;
        testInds[3] = SLICE_COUNT * 2 + 2;
        testInds[4] = SLICE_COUNT * 0 + 4;
        testInds[5] = SLICE_COUNT * 2 + 4;
        testInds[6] = SLICE_COUNT * 0 + 6;
        testInds[7] = SLICE_COUNT * 2 + 6;*/
        //testInds = new int[testVrtx.length/6];
        /*for(int i = 0; i < ((testVrtx.length) / 12); i++){
            testInds[i*2 + 0] = i;
           * testInds[i*2 + 1] = i+(SLICE_COUNT+1);
        }*/

 /*for(int i = 0; i <= SLICE_COUNT; i++){
            for(int j = 0; j < SIMPLE_EDGE_COUNT; j++){
                System.arraycopy(Bender.calcCoord(i, RADII[j], HEIGHTS[j], SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + j) * Bender.NUMCOORD, Bender.NUMCOORD);
            }
            inds[0][i] = (i * TOTAL_EDGE_COUNT + 0);
            inds[1][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 0);
            inds[1][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 1);
            inds[2][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 1);
            inds[2][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 2);
            inds[3][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 2);
            inds[3][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 3);
            for(int k = 0; k < STACK_COUNT; k++){
                System.arraycopy(Bender.calcCoord(i,
                                                  RADII[3] * Math.cos(Math.toRadians((k + 1) * 90 / STACK_COUNT)),
                                                  HEIGHTS[3] + (RADII[3] * Math.sin(Math.toRadians((k + 1) * 90 / STACK_COUNT))),
                                                  SLICE_COUNT),
                                 0, vrtx, ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k) * Bender.NUMCOORD, Bender.NUMCOORD);
                inds[4][((i + (k * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k - 1);
                inds[4][((i + (k * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k - 0);
            }
            inds[4][(i * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT - 2);
            for(int k = 0; k < STACK_COUNT; k++){
                System.arraycopy(Bender.calcCoord(i,
                                                  RADII[6] + ((RADII[5] - RADII[6]) * Math.cos(Math.toRadians(k * 90 / STACK_COUNT))),
                                                  HEIGHTS[5] + ((RADII[5] - RADII[6]) * Math.sin(Math.toRadians(k * 90 / STACK_COUNT))),
                                                  SLICE_COUNT),
                                 0, vrtx, ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + k) * Bender.NUMCOORD, Bender.NUMCOORD);
                inds[5][((i + (k * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + k + 0);
                inds[5][((i + (k * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + k + 1);
            }
            inds[6][i * 2 + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + 2 * STACK_COUNT - 1);
            inds[6][i * 2 + 1] = ((i * TOTAL_EDGE_COUNT) + 4);
            for(int k = 0; k < (2 * STACK_COUNT); k++){
                System.arraycopy(Bender.calcCoord(i,
                                                  RADII[7] * Math.sin(Math.toRadians(k * 180 / (2 * (STACK_COUNT - 1)))),
                                                  HEIGHTS[7] + (RADII[7] * Math.cos(Math.toRadians(k * 180 / (2 * (STACK_COUNT - 1)) - 180))),
                                                  SLICE_COUNT),
                                 0, vrtx, ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + 2 * STACK_COUNT + k) * Bender.NUMCOORD, Bender.NUMCOORD);
                inds[7][((i + (k * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + 2 * STACK_COUNT + k + 0);
                inds[7][((i + (k * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + 2 * STACK_COUNT + k + 1);
            }
            inds[7][((i + (((2 * STACK_COUNT) - 1) * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + 2 * STACK_COUNT + ((2 * (STACK_COUNT - 1)) - 1) + 0);
            inds[7][((i + (((2 * STACK_COUNT) - 1) * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + 2 * STACK_COUNT + ((2 * (STACK_COUNT - 1)) - 1) + 1);
        }

        gl.glGenBuffers(BUFF_COUNT + 1, buff, 0);

        DoubleBuffer bodyVrtxBuff = Buffers.newDirectDoubleBuffer(vrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buff[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vrtx.length * Double.BYTES, bodyVrtxBuff, GL2.GL_STATIC_DRAW);

        for(int i = 0; i < BUFF_COUNT; i++){
            IntBuffer bodyIndsBuff = Buffers.newDirectIntBuffer(inds[i]);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BUFF_SIZE[i] * Integer.BYTES, bodyIndsBuff, GL2.GL_STATIC_DRAW);
        }*/
 /*gl.glGenBuffers(2, buff, 0);
        DoubleBuffer bodyVrtxBuff = Buffers.newDirectDoubleBuffer(testVrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buff[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, testVrtx.length * Double.BYTES, bodyVrtxBuff, GL2.GL_STATIC_DRAW);

        IntBuffer bodyIndsBuff = Buffers.newDirectIntBuffer(testInds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[1]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, testInds.length * Integer.BYTES, bodyIndsBuff, GL2.GL_STATIC_DRAW);*/
        glBufferNames = new int[surfaceCompilation.size() + 1];
        gl.glGenBuffers(surfaceCompilation.size() + 1, glBufferNames, 0);

        DoubleBuffer vrtxBuff = Buffers.newDirectDoubleBuffer(surfaceCompilation.getVertexNormal());
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glBufferNames[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, surfaceCompilation.getVertexNormal().length * Double.BYTES, vrtxBuff, GL2.GL_STATIC_DRAW);

        for(int i = 0; i < surfaceCompilation.size(); i++){
            IntBuffer indsBuff = Buffers.newDirectIntBuffer(surfaceCompilation.getSurfaces().get(i).getIndices());
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glBufferNames[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, surfaceCompilation.getSurfaces().get(i).getIndices().length * Integer.BYTES, indsBuff, GL2.GL_STATIC_DRAW);
        }

    }

    public void draw(GL2 gl){
        gl.glPushMatrix();
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glBufferNames[0]);
        gl.glVertexPointer(Assembler.NUMCOORD, GL2.GL_DOUBLE, 1 * Assembler.NUMCOORD * Double.BYTES, 0);
        ///////////////gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Assembler.NUMCOORD * Double.BYTES, 1 * Assembler.NUMCOORD * Double.BYTES);
        for(int i = 0; i < surfaceCompilation.size(); i++){
            drawBuffer(gl, i);
        }
        /*for(int i = 0; i < BUFF_COUNT; i++){
            drawBuffer(gl, i);*/
 /*gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[i + 1]);
            gl.glDrawElements(BUFF_TYPE[i], BUFF_SIZE[i], GL2.GL_UNSIGNED_INT, 0);*/
        //}
        //drawBuffer(gl, 0);
        gl.glPopMatrix();
    }

    private void drawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glBufferNames[buffInd + 1]);
        gl.glDrawElements((surfaceCompilation.getSurfaces().get(buffInd).isPolygon() ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)),
                          surfaceCompilation.getSurfaces().get(buffInd).getIndices().length, GL2.GL_UNSIGNED_INT, 0);
    }
}
