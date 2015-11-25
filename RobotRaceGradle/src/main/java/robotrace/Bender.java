package robotrace;

import com.jogamp.common.nio.Buffers;
import java.nio.*;
import javax.media.opengl.GL2;

class Bender{

    private static final int BND_MAIN_MANY = 50;
    private static final int BND_MAIN_MANYH = 50;
    private static final int BND_NUMCOORD = 3;
    private static final int BND_NUMEDGE = 5;

    private static final int BND_TOTALEDGE = BND_NUMEDGE + 4 * BND_MAIN_MANYH;

    private static final double BND_MAIN_RADII[] = {0.175d, 0.225d, 0.125d, 0.125d, 0.01d, 0.05d, 0.03d, 0.025d};
    private static final double BND_MAIN_HGTS[] = {0.6d, 1.05d, 1.15d, 1.475d, 1.775d, 1.475d + Math.sqrt(Math.pow(BND_MAIN_RADII[3], 2d) - Math.pow(BND_MAIN_RADII[5], 2d)), 1d, 1.775d + Math.sqrt(Math.pow(BND_MAIN_RADII[7], 2d) - Math.pow(BND_MAIN_RADII[4], 2d))};

    private final double[] BND_Main_vrtx;
    private final int[] BND_Shiny_inds;
    private final int[] BND_Torso_inds;
    private final int[] BND_Neck_inds;
    private final int[] BND_Headl_inds;
    private final int[] BND_Headh_inds;
    private final int[] BND_Antb_inds;
    private final int[] BND_Antm_inds;
    private final int[] BND_Antt_inds;

    private final int[] BND_Buffers;

    public Bender(){
        BND_Main_vrtx = new double[(BND_MAIN_MANY + 1) * (BND_TOTALEDGE + 1) * BND_NUMCOORD];
        BND_Shiny_inds = new int[(BND_MAIN_MANY + 1)];
        BND_Torso_inds = new int[(BND_MAIN_MANY + 1) * 2];
        BND_Neck_inds = new int[(BND_MAIN_MANY + 1) * 2];
        BND_Headl_inds = new int[(BND_MAIN_MANY + 1) * 2];
        BND_Headh_inds = new int[(BND_MAIN_MANY + 1) * 2 * BND_MAIN_MANYH];
        BND_Antb_inds = new int[(BND_MAIN_MANY + 1) * 2 * BND_MAIN_MANYH];
        BND_Antm_inds = new int[(BND_MAIN_MANY + 1) * 2];
        BND_Antt_inds = new int[(BND_MAIN_MANY + 1) * 4 * BND_MAIN_MANYH];
        BND_Buffers = new int[10];
    }

    public int getVerticesBuffer(){
        return BND_Buffers[0];
    }

    public int getShinyBuffer(){
        return BND_Buffers[1];
    }

    public int getTorsoBuffer(){
        return BND_Buffers[2];
    }

    public int getNeckBuffer(){
        return BND_Buffers[3];
    }

    public int getHeadlBuffer(){
        return BND_Buffers[4];
    }

    public int getHeadhBuffer(){
        return BND_Buffers[5];
    }

    public int getAntbBuffer(){
        return BND_Buffers[6];
    }

    public int getAntmBuffer(){
        return BND_Buffers[7];
    }

    public int getAnttBuffer(){
        return BND_Buffers[8];
    }

    public int getSliceCount(){
        return BND_MAIN_MANY + 1;
    }

    public int getStackCount(){
        return BND_MAIN_MANYH - 1;
    }

    public int getCoordCount(){
        return BND_NUMCOORD;
    }

    public void initialize(GL2 gl){
        for(int i = 0; i <= BND_MAIN_MANY; i++){
            for(int j = 0; j < BND_NUMEDGE; j++){
                System.arraycopy(calcCoord(i, BND_MAIN_RADII[j], BND_MAIN_HGTS[j]), 0, BND_Main_vrtx, (i * BND_TOTALEDGE + j) * BND_NUMCOORD, 3);
            }
            BND_Shiny_inds[i] = (i * BND_TOTALEDGE + 0);
            BND_Torso_inds[i * 2 + 0] = (i * BND_TOTALEDGE + 0);
            BND_Torso_inds[i * 2 + 1] = (i * BND_TOTALEDGE + 1);
            BND_Neck_inds[i * 2 + 0] = (i * BND_TOTALEDGE + 1);
            BND_Neck_inds[i * 2 + 1] = (i * BND_TOTALEDGE + 2);
            BND_Headl_inds[i * 2 + 0] = (i * BND_TOTALEDGE + 2);
            BND_Headl_inds[i * 2 + 1] = (i * BND_TOTALEDGE + 3);
            for(int k = 0; k < BND_MAIN_MANYH; k++){
                System.arraycopy(calcCoord(i,
                                           BND_MAIN_RADII[3] * Math.cos(Math.toRadians((k + 1) * 90 / BND_MAIN_MANYH)),
                                           BND_MAIN_HGTS[3] + (BND_MAIN_RADII[3] * Math.sin(Math.toRadians((k + 1) * 90 / BND_MAIN_MANYH)))),
                                 0, BND_Main_vrtx, ((i * BND_TOTALEDGE) + BND_NUMEDGE + k) * BND_NUMCOORD, 3);
                BND_Headh_inds[((i + (k * (BND_MAIN_MANY + 1))) * 2) + 0] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + k - 1);
                BND_Headh_inds[((i + (k * (BND_MAIN_MANY + 1))) * 2) + 1] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + k - 0);
            }
            BND_Headh_inds[(i * 2) + 0] = ((i * BND_TOTALEDGE) + BND_NUMEDGE - 2);
            for(int k = 0; k < BND_MAIN_MANYH; k++){
                System.arraycopy(calcCoord(i,
                                           BND_MAIN_RADII[6] + ((BND_MAIN_RADII[5] - BND_MAIN_RADII[6]) * Math.cos(Math.toRadians(k * 90 / BND_MAIN_MANYH))),
                                           BND_MAIN_HGTS[5] + ((BND_MAIN_RADII[5] - BND_MAIN_RADII[6]) * Math.sin(Math.toRadians(k * 90 / BND_MAIN_MANYH)))),
                                 0, BND_Main_vrtx, ((i * BND_TOTALEDGE) + BND_NUMEDGE + BND_MAIN_MANYH + k) * BND_NUMCOORD, 3);
                BND_Antb_inds[((i + (k * (BND_MAIN_MANY + 1))) * 2) + 0] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + BND_MAIN_MANYH + k + 0);
                BND_Antb_inds[((i + (k * (BND_MAIN_MANY + 1))) * 2) + 1] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + BND_MAIN_MANYH + k + 1);
            }
            BND_Antm_inds[i * 2 + 0] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + 2 * BND_MAIN_MANYH - 1);
            BND_Antm_inds[i * 2 + 1] = ((i * BND_TOTALEDGE) + 4);
            for(int k = 0; k < (2 * BND_MAIN_MANYH); k++){
                System.arraycopy(calcCoord(i,
                                           BND_MAIN_RADII[7] * Math.sin(Math.toRadians(k * 180 / (2 * (BND_MAIN_MANYH - 1)))),
                                           BND_MAIN_HGTS[7] + (BND_MAIN_RADII[7] * Math.cos(Math.toRadians(k * 180 / (2 * (BND_MAIN_MANYH - 1)) - 180)))),
                                 0, BND_Main_vrtx, ((i * BND_TOTALEDGE) + BND_NUMEDGE + 2 * BND_MAIN_MANYH + k) * BND_NUMCOORD, 3);
                BND_Antt_inds[((i + (k * (BND_MAIN_MANY + 1))) * 2) + 0] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + 2 * BND_MAIN_MANYH + k + 0);
                BND_Antt_inds[((i + (k * (BND_MAIN_MANY + 1))) * 2) + 1] = ((i * BND_TOTALEDGE) + BND_NUMEDGE + 2 * BND_MAIN_MANYH + k + 1);
            }
        }

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glGenBuffers(10, BND_Buffers, 0);

        DoubleBuffer BND_Main_vrtx_b = Buffers.newDirectDoubleBuffer(BND_Main_vrtx);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, BND_Buffers[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, BND_Main_vrtx.length * Double.BYTES, BND_Main_vrtx_b, GL2.GL_STATIC_DRAW);
        //BND_Main_vrtx_b = null;

        IntBuffer BND_Shiny_inds_b = Buffers.newDirectIntBuffer(BND_Shiny_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[1]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Shiny_inds.length * Integer.BYTES, BND_Shiny_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Shiny_inds_b = null;

        IntBuffer BND_Torso_inds_b = Buffers.newDirectIntBuffer(BND_Torso_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[2]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Torso_inds.length * Integer.BYTES, BND_Torso_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Torso_inds_b = null;

        IntBuffer BND_Neck_inds_b = Buffers.newDirectIntBuffer(BND_Neck_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[3]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Neck_inds.length * Integer.BYTES, BND_Neck_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Shiny_inds_b = null;

        IntBuffer BND_Headl_inds_b = Buffers.newDirectIntBuffer(BND_Headl_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[4]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Headl_inds.length * Integer.BYTES, BND_Headl_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Headl_inds_b = null;

        IntBuffer BND_Headh_inds_b = Buffers.newDirectIntBuffer(BND_Headh_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[5]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Headh_inds.length * Integer.BYTES, BND_Headh_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Headh_inds_b = null;

        IntBuffer BND_Antb_inds_b = Buffers.newDirectIntBuffer(BND_Antb_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[6]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Antb_inds.length * Integer.BYTES, BND_Antb_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Antb_inds_b = null;

        IntBuffer BND_Antm_inds_b = Buffers.newDirectIntBuffer(BND_Antm_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[7]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Antm_inds.length * Integer.BYTES, BND_Antm_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Antm_inds_b = null;

        IntBuffer BND_Antt_inds_b = Buffers.newDirectIntBuffer(BND_Antt_inds);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Buffers[8]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, BND_Antt_inds.length * Integer.BYTES, BND_Antt_inds_b, GL2.GL_STATIC_DRAW);
        //BND_Antt_inds_b = null;

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    private double[] calcCoord(int angleIndex, double radius, double height){
        double coord[] = new double[3];
        coord[0] = radius * Math.cos(Math.toRadians(angleIndex * 360 / BND_MAIN_MANY));
        coord[1] = radius * Math.sin(Math.toRadians(angleIndex * 360 / BND_MAIN_MANY));
        coord[2] = height;
        return coord;
    }
}
