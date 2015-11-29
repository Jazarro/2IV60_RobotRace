package robotrace.bender;

import com.jogamp.common.nio.*;
import java.nio.*;
import javax.media.opengl.*;
import utility.*;

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
    private static final double HAND_RADIUS = 0.06d;
    private static final double HAND_HEIGHT = 0.07d;
    private static final double FINGER_RADIUS = 0.0125d;
    private static final double FINGER_HEIGHT = 0.07d + FINGER_RADIUS;
    private static final int FINGER_COUNT = 3;
    private static final int SLICE_COUNT = 50;
    private static final int STACK_COUNT = 50;
    private static final int SIMPLE_EDGE_COUNT = 5;
    private static final int TOTAL_EDGE_COUNT = SIMPLE_EDGE_COUNT + 2 * STACK_COUNT;
    private static final int BUFF_COUNT = 7;
    private static final int BUFF_SIZE[] = {(SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 2,
                                            SLICE_COUNT + 1,
                                            (SLICE_COUNT + 1) * 2,
                                            (SLICE_COUNT + 1) * 2 * STACK_COUNT,
                                            (SLICE_COUNT + 1) * 2 * STACK_COUNT,
                                            SLICE_COUNT + 1};
    private static final int BUFF_TYPE[] = {GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_POLYGON,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_QUAD_STRIP,
                                            GL2.GL_POLYGON};

    private final double[] vrtx;
    private final int[][] inds;
    private final int[] buff;

    public Limb(){
        vrtx = new double[(SLICE_COUNT + 1) * (TOTAL_EDGE_COUNT) * Assembler.NUMCOORD];
        inds = new int[BUFF_COUNT][];
        for(int i = 0; i < BUFF_COUNT; i++){
            inds[i] = new int[BUFF_SIZE[i]];
        }
        buff = new int[BUFF_COUNT + 1];
    }

    public void initialize(GL2 gl){
        for(int i = 0; i <= SLICE_COUNT; i++){
            System.arraycopy(Assembler.calcVrtx(i, RING_RADIUS, 0, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 0) * Assembler.NUMCOORD, Assembler.NUMCOORD);
            System.arraycopy(Assembler.calcVrtx(i, RING_RADIUS, -RING_HEIGHT, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 1) * Assembler.NUMCOORD, Assembler.NUMCOORD);
            inds[0][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 0);
            inds[0][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 1);

            System.arraycopy(Assembler.calcVrtx(i, HAND_RADIUS, -HAND_HEIGHT, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 2) * Assembler.NUMCOORD, Assembler.NUMCOORD);
            inds[1][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 0);
            inds[1][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 2);
            inds[2][i] = (i * TOTAL_EDGE_COUNT + 2);
            System.arraycopy(Assembler.calcVrtx(i, FINGER_RADIUS, FINGER_RADIUS, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 3) * Assembler.NUMCOORD, Assembler.NUMCOORD);
            System.arraycopy(Assembler.calcVrtx(i, FINGER_RADIUS, -FINGER_HEIGHT + FINGER_RADIUS, SLICE_COUNT), 0, vrtx, (i * TOTAL_EDGE_COUNT + 4) * Assembler.NUMCOORD, Assembler.NUMCOORD);
            inds[3][i * 2 + 0] = (i * TOTAL_EDGE_COUNT + 3);
            inds[3][i * 2 + 1] = (i * TOTAL_EDGE_COUNT + 4);
            for(int k = 0; k < STACK_COUNT; k++){
                System.arraycopy(Assembler.calcVrtx(i,
                                                  FINGER_RADIUS * Math.cos(Math.toRadians(k * 90 / STACK_COUNT)),
                                                  FINGER_RADIUS * (1 - Math.sin(Math.toRadians(k * 90 / STACK_COUNT))),
                                                  SLICE_COUNT),
                                 0, vrtx, ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k) * Assembler.NUMCOORD, Assembler.NUMCOORD);
                inds[4][((i + (k * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k + 0);
                inds[4][((i + (k * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + k + 1);
            }
            inds[4][((i + ((STACK_COUNT - 1) * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + (STACK_COUNT - 1) + 0);

            for(int k = 0; k < STACK_COUNT; k++){
                System.arraycopy(Assembler.calcVrtx(i,
                                                  FOOT_RADIUS * Math.cos(Math.toRadians(k * 90 / STACK_COUNT)),
                                                  FOOT_RADIUS * Math.sin(Math.toRadians(k * 90 / STACK_COUNT)),
                                                  SLICE_COUNT),
                                 0, vrtx, ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + k) * Assembler.NUMCOORD, Assembler.NUMCOORD);
                inds[5][((i + (k * (SLICE_COUNT + 1))) * 2) + 0] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + k + 0);
                inds[5][((i + (k * (SLICE_COUNT + 1))) * 2) + 1] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + k + 1);
            }
            inds[6][i] = ((i * TOTAL_EDGE_COUNT) + SIMPLE_EDGE_COUNT + STACK_COUNT + 0);

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

    public void draw(GL2 gl, double[] anglesAxis, double[] anglesBend, int type, int side){
        double currAngleAxis;
        double currAngleBend;
        double newPos[] = {0d, 0d, 0d};
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buff[0]);
        gl.glVertexPointer(Assembler.NUMCOORD, GL2.GL_DOUBLE, 0, 0);
        for(int i = 0; i < RING_COUNT; i++){
            gl.glPushMatrix();
            currAngleAxis = (side == RIGHT) ? (-anglesAxis[i]) : (anglesAxis[i]);
            currAngleBend = anglesBend[i];
            gl.glTranslated(newPos[0], newPos[1], newPos[2]);
            gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
            newPos = nextPos(newPos, RING_HEIGHT, currAngleBend, currAngleAxis);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[1]);
            gl.glDrawElements(BUFF_TYPE[0], BUFF_SIZE[0], GL2.GL_UNSIGNED_INT, 0);
            gl.glPopMatrix();
        }
        gl.glPushMatrix();
        currAngleAxis = (side == RIGHT) ? (-anglesAxis[RING_COUNT]) : (anglesAxis[RING_COUNT]);
        currAngleBend = anglesBend[RING_COUNT];
        switch(type){
            case LEG:
                newPos = nextPos(newPos, FOOT_HEIGHT, currAngleBend, currAngleAxis);
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[6]);
                gl.glDrawElements(BUFF_TYPE[5], BUFF_SIZE[5], GL2.GL_UNSIGNED_INT, 0);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[7]);
                gl.glDrawElements(BUFF_TYPE[6], BUFF_SIZE[6], GL2.GL_UNSIGNED_INT, 0);
                break;
            case ARM:
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[2]);
                gl.glDrawElements(BUFF_TYPE[1], BUFF_SIZE[1], GL2.GL_UNSIGNED_INT, 0);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[3]);
                gl.glDrawElements(BUFF_TYPE[2], BUFF_SIZE[2], GL2.GL_UNSIGNED_INT, 0);

                gl.glTranslated(0d, 0d, -HAND_HEIGHT);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[4]);
                gl.glDrawElements(BUFF_TYPE[3], BUFF_SIZE[3], GL2.GL_UNSIGNED_INT, 0);
                gl.glTranslated(0d, 0d, -FINGER_HEIGHT);
                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[5]);
                gl.glDrawElements(BUFF_TYPE[4], BUFF_SIZE[4], GL2.GL_UNSIGNED_INT, 0);
                break;
        }
        gl.glPopMatrix();
    }

    public double height(double[] anglesAxis, double[] anglesBend, int type, int side){
        double currAngleAxis;
        double currAngleBend;
        double newPos[] = {0d, 0d, 0d};
        for(int i = 0; i < RING_COUNT; i++){
            currAngleAxis = (side == RIGHT) ? (-anglesAxis[i]) : (anglesAxis[i]);
            currAngleBend = anglesBend[i];
            newPos = nextPos(newPos, RING_HEIGHT, currAngleBend, currAngleAxis);
        }
        currAngleAxis = (side == RIGHT) ? (-anglesAxis[RING_COUNT]) : (anglesAxis[RING_COUNT]);
        currAngleBend = anglesBend[RING_COUNT];
        switch(type){
            case LEG:
                newPos = nextPos(newPos, FOOT_HEIGHT, currAngleBend, currAngleAxis);
                newPos[2] -= FOOT_RADIUS * Math.sin(Math.toRadians(currAngleBend));
                break;
            case ARM:
                //newPos[2] -= ?;
                break;
        }
        return Math.abs(newPos[2]);
    }

    private double[] nextPos(double[] currPos, double height, double currAngleBend, double currAngleAxis){
        currPos[0] -= height * Math.sin(Math.toRadians(currAngleBend)) * Math.sin(Math.toRadians(currAngleAxis));
        currPos[1] += height * Math.sin(Math.toRadians(currAngleBend)) * Math.cos(Math.toRadians(currAngleAxis));
        currPos[2] -= height * Math.cos(Math.toRadians(currAngleBend));
        return currPos;
    }

    private void drawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buff[buffInd + 1]);
        gl.glDrawElements(BUFF_TYPE[buffInd], BUFF_SIZE[buffInd], GL2.GL_UNSIGNED_INT, 0);
    }
}
