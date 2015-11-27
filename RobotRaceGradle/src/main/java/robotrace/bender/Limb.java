package robotrace.bender;

import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;

public class Limb{

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int LEG = 0;
    public static final int ARM = 1;
    public static final int RING_COUNT = 6;

    private static final double RING_HEIGHT = 0.5d / 6d;
    private static final double RING_RADIUS = 0.04d;
    private static final double FOOT_HEIGHT = 0.1d;
    private static final double FOOT_RADIUS = Math.sqrt(Math.pow(FOOT_HEIGHT, 2d) + Math.pow(RING_RADIUS, 2d));
    private static final int SLICE_COUNT = 50;
    private static final int STACK_COUNT = 50;
    private static final int SIMPLE_EDGE_COUNT = 2;
    private static final int TOTAL_EDGE_COUNT = SIMPLE_EDGE_COUNT + STACK_COUNT;
    private static final int BUFF_COUNT = 3;
    private static final int BUFF_SIZE[] = {(SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 2 * STACK_COUNT,
                                            SLICE_COUNT + 1};
    private static final int BUFF_TYPE[] = {GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_POLYGON};

    private final double[] vrtx;
    private final int[][] inds;
    private final int[] buff;

    public Limb(){
        vrtx = new double[(SLICE_COUNT + 1) * (TOTAL_EDGE_COUNT) * Bender.NUMCOORD];
        inds = new int[BUFF_COUNT][];
        for(int i = 0; i < BUFF_COUNT; i++){
            inds[i] = new int[BUFF_SIZE[i]];
        }
        buff = new int[BUFF_COUNT + 1];
    }

    public void initialize(GL2 gl){
        for(int i = 0; i <= SLICE_COUNT; i++){
            System.arraycopy(Bender.calcCoord(i, RING_RADIUS, 0, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 0) * Bender.NUMCOORD, Bender.NUMCOORD);
            System.arraycopy(Bender.calcCoord(i, RING_RADIUS, RING_HEIGHT, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 1) * Bender.NUMCOORD, Bender.NUMCOORD);
            inds[0][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 0);
            inds[0][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 1);

            for(int k = 0; k < STACK_COUNT; k++){
                System.arraycopy(Bender.calcCoord(i,
                                                  FOOT_RADIUS * Math.cos(Math.toRadians(k * 90 / STACK_COUNT)),
                                                  FOOT_RADIUS * Math.sin(Math.toRadians(k * 90 / STACK_COUNT)),
                                                  SLICE_COUNT),
                                 0, vrtx, ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k) * Bender.NUMCOORD, Bender.NUMCOORD);
                inds[1][((i + (k * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k + 0);
                inds[1][((i + (k * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k + 1);
            }
            inds[2][i] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT);
        }

        gl.glGenBuffers(BUFF_COUNT + 1, buff, 0);

        DoubleBuffer limbVrtxBuff = Buffers.newDirectDoubleBuffer(vrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buff[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vrtx.length * Double.BYTES, limbVrtxBuff, GL2.GL_STATIC_DRAW);

        for(int i = 0; i < BUFF_COUNT; i++){
            IntBuffer limbIndsBuff = Buffers.newDirectIntBuffer(inds[i]);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BUFF_SIZE[i] * Integer.BYTES, limbIndsBuff, GL2.GL_STATIC_DRAW);
        }
    }

    public void draw(GL2 gl, double[] anglesInOut, double[] anglesFrontBack, int type, int side){
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buff[0]);
        gl.glVertexPointer(Bender.NUMCOORD, GL2.GL_DOUBLE, 0, 0);
        for(int i = 0; i < RING_COUNT; i++){
            gl.glRotated(((side == LEFT) ? (-anglesInOut[i]) : (anglesInOut[i])), 0d, 1d, -RING_HEIGHT * i);
            gl.glRotated(anglesFrontBack[i], 1d, 0d, -RING_HEIGHT * i);
            gl.glTranslated(0d, 0d, -RING_HEIGHT);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[1]);
            gl.glDrawElements(BUFF_TYPE[0], BUFF_SIZE[0], GL2.GL_UNSIGNED_INT, 0);
        }
        switch(type){
            case LEG:
                gl.glTranslated(0d, 0d, -FOOT_HEIGHT);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[2]);
                gl.glDrawElements(BUFF_TYPE[1], BUFF_SIZE[1], GL2.GL_UNSIGNED_INT, 0);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[3]);
                gl.glDrawElements(BUFF_TYPE[2], BUFF_SIZE[2], GL2.GL_UNSIGNED_INT, 0);
                break;
            case ARM:
                break;
        }
    }
}
