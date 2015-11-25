package robotrace;

import com.jogamp.common.nio.Buffers;
import java.nio.*;
import javax.media.opengl.GL2;

class Bender{

    private static final int NUMCOORD = 3;

    private static final int bodySliceCount = 50;
    private static final int bodyStackCount = 50;
    private static final int bodySimpleEdgeCount = 5;
    private static final int bodyTotalEdgeCount = bodySimpleEdgeCount + 4 * bodyStackCount;

    private static final double bodyRadii[] = {0.175d, 0.225d, 0.125d, 0.125d, 0.01d, 0.05d, 0.03d, 0.025d};
    private static final double bodyHgts[] = {0.6d, 1.05d, 1.15d, 1.475d, 1.775d, 1.475d + Math.sqrt(Math.pow(bodyRadii[3], 2d) - Math.pow(bodyRadii[5], 2d)), 1d, 1.775d + Math.sqrt(Math.pow(bodyRadii[7], 2d) - Math.pow(bodyRadii[4], 2d))};

    private final double[] bodyVrtx;
    private final int[][] bodyInds;
    private final int[] bodyBuff;
    private static final int bodyBuffCount = 8;
    private static final int bodyBuffSize[] = {bodySliceCount + 1,
                                               (bodySliceCount + 1) * 2,
                                               (bodySliceCount + 1) * 2,
                                               (bodySliceCount + 1) * 2,
                                               (bodySliceCount + 1) * 2 * bodyStackCount,
                                               (bodySliceCount + 1) * 2 * bodyStackCount,
                                               (bodySliceCount + 1) * 2,
                                               (bodySliceCount + 1) * 4 * bodyStackCount};
    private static final int bodyBuffType[] = {GL2.GL_POLYGON,
                                               GL2.GL_QUAD_STRIP,
                                               GL2.GL_QUAD_STRIP,
                                               GL2.GL_QUAD_STRIP,
                                               GL2.GL_QUAD_STRIP,
                                               GL2.GL_QUAD_STRIP,
                                               GL2.GL_QUAD_STRIP,
                                               GL2.GL_QUAD_STRIP};

    public Bender(){
        bodyVrtx = new double[(bodySliceCount + 1) * (bodyTotalEdgeCount + 1) * NUMCOORD];
        bodyInds = new int[bodyBuffCount][];
        for(int i = 0; i < bodyBuffCount; i++){
            bodyInds[i] = new int[bodyBuffSize[i]];
        }
        bodyBuff = new int[bodyBuffCount + 1];
    }

    public void initialize(GL2 gl){
        bodyInit(gl);
    }

    public void draw(GL2 gl){
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bodyBuff[0]);
        gl.glVertexPointer(NUMCOORD, GL2.GL_DOUBLE, 0, 0);

        for(int i = 0; i < bodyBuffCount; i++){
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyBuff[i + 1]);
            gl.glDrawElements(bodyBuffType[i], bodyInds[i].length, GL2.GL_UNSIGNED_INT, 0);
        }

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    private void bodyInit(GL2 gl){
        for(int i = 0; i <= bodySliceCount; i++){
            for(int j = 0; j < bodySimpleEdgeCount; j++){
                System.arraycopy(calcCoord(i, bodyRadii[j], bodyHgts[j]), 0, bodyVrtx, (i * bodyTotalEdgeCount + j) * NUMCOORD, 3);
            }
            bodyInds[0][i] = (i * bodyTotalEdgeCount + 0);
            bodyInds[1][i * 2 + 0] = (i * bodyTotalEdgeCount + 0);
            bodyInds[1][i * 2 + 1] = (i * bodyTotalEdgeCount + 1);
            bodyInds[2][i * 2 + 0] = (i * bodyTotalEdgeCount + 1);
            bodyInds[2][i * 2 + 1] = (i * bodyTotalEdgeCount + 2);
            bodyInds[3][i * 2 + 0] = (i * bodyTotalEdgeCount + 2);
            bodyInds[3][i * 2 + 1] = (i * bodyTotalEdgeCount + 3);
            for(int k = 0; k < bodyStackCount; k++){
                System.arraycopy(calcCoord(i,
                                           bodyRadii[3] * Math.cos(Math.toRadians((k + 1) * 90 / bodyStackCount)),
                                           bodyHgts[3] + (bodyRadii[3] * Math.sin(Math.toRadians((k + 1) * 90 / bodyStackCount)))),
                                 0, bodyVrtx, ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + k) * NUMCOORD, 3);
                bodyInds[4][((i + (k * (bodySliceCount + 1))) * 2) + 0] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + k - 1);
                bodyInds[4][((i + (k * (bodySliceCount + 1))) * 2) + 1] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + k - 0);
            }
            bodyInds[4][(i * 2) + 0] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount - 2);
            for(int k = 0; k < bodyStackCount; k++){
                System.arraycopy(calcCoord(i,
                                           bodyRadii[6] + ((bodyRadii[5] - bodyRadii[6]) * Math.cos(Math.toRadians(k * 90 / bodyStackCount))),
                                           bodyHgts[5] + ((bodyRadii[5] - bodyRadii[6]) * Math.sin(Math.toRadians(k * 90 / bodyStackCount)))),
                                 0, bodyVrtx, ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + bodyStackCount + k) * NUMCOORD, 3);
                bodyInds[5][((i + (k * (bodySliceCount + 1))) * 2) + 0] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + bodyStackCount + k + 0);
                bodyInds[5][((i + (k * (bodySliceCount + 1))) * 2) + 1] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + bodyStackCount + k + 1);
            }
            bodyInds[6][i * 2 + 0] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + 2 * bodyStackCount - 1);
            bodyInds[6][i * 2 + 1] = ((i * bodyTotalEdgeCount) + 4);
            for(int k = 0; k < (2 * bodyStackCount); k++){
                System.arraycopy(calcCoord(i,
                                           bodyRadii[7] * Math.sin(Math.toRadians(k * 180 / (2 * (bodyStackCount - 1)))),
                                           bodyHgts[7] + (bodyRadii[7] * Math.cos(Math.toRadians(k * 180 / (2 * (bodyStackCount - 1)) - 180)))),
                                 0, bodyVrtx, ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + 2 * bodyStackCount + k) * NUMCOORD, 3);
                bodyInds[7][((i + (k * (bodySliceCount + 1))) * 2) + 0] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + 2 * bodyStackCount + k + 0);
                bodyInds[7][((i + (k * (bodySliceCount + 1))) * 2) + 1] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + 2 * bodyStackCount + k + 1);
            }
            bodyInds[7][((i + (((2 * bodyStackCount) - 1) * (bodySliceCount + 1))) * 2) + 0] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + 2 * bodyStackCount + ((2 * (bodyStackCount - 1)) - 1) + 0);
            bodyInds[7][((i + (((2 * bodyStackCount) - 1) * (bodySliceCount + 1))) * 2) + 1] = ((i * bodyTotalEdgeCount) + bodySimpleEdgeCount + 2 * bodyStackCount + ((2 * (bodyStackCount - 1)) - 1) + 1);
        }

        gl.glGenBuffers(bodyBuffCount + 1, bodyBuff, 0);

        DoubleBuffer bodyVrtxBuff = Buffers.newDirectDoubleBuffer(bodyVrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bodyBuff[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, bodyVrtx.length * Double.BYTES, bodyVrtxBuff, GL2.GL_STATIC_DRAW);

        for(int i = 0; i < bodyBuffCount; i++){
            IntBuffer bodyIndsBuff = Buffers.newDirectIntBuffer(bodyInds[i]);
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyBuff[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, bodyInds[i].length * Integer.BYTES, bodyIndsBuff, GL2.GL_STATIC_DRAW);
        }
    }

    private double[] calcCoord(int angleIndex, double radius, double height){
        double coord[] = new double[3];
        coord[0] = radius * Math.cos(Math.toRadians(angleIndex * 360 / bodySliceCount));
        coord[1] = radius * Math.sin(Math.toRadians(angleIndex * 360 / bodySliceCount));
        coord[2] = height;
        return coord;
    }
}
