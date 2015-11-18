package robotrace;

import com.jogamp.common.nio.Buffers;
import java.nio.*;
import javax.media.opengl.GL2;

class Bender{
    private static final int BND_MAIN_MANY = 50;
    private static final int BND_NUMCOORD = 3;
    private static final int BND_NUMEDGE = 4;
    //private static final int BND_NUMEDGE = 2;
    
    private static final double BND_MAIN_HGTS[] = {0.6f, 1.05f, 1.15f, 1.475f};
    //private static final double BND_MAIN_HGTS[] = {0.6f, 1.05f};
    private static final double BND_MAIN_RADII[] = {0.35f, 0.45f, 0.25f, 0.25f};
    //private static final double BND_MAIN_RADII[] = {1f, 1f};

    private final double[] BND_Main_vrtx;
    private final int[] BND_Shiny_inds;
    private final int[] BND_Torso_inds;
    private final int[] BND_Neck_inds;
    private final int[] BND_Headl_inds;

    private final int[] BND_Buffers;

    public Bender(){
        BND_Main_vrtx = new double[(BND_MAIN_MANY + 1) * BND_NUMEDGE * BND_NUMCOORD];
        BND_Shiny_inds = new int[(BND_MAIN_MANY + 1)];
        BND_Torso_inds = new int[(BND_MAIN_MANY + 1) * 2];
        BND_Neck_inds = new int[(BND_MAIN_MANY + 1) * 2];
        BND_Headl_inds = new int[(BND_MAIN_MANY + 1) * 2];
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
    
    public int getSliceCount(){
        return BND_MAIN_MANY;
    }
    
    public int getCoordCount(){
        return BND_NUMCOORD;
    }

    public void initialize(GL2 gl){
        for(int i = 0; i <= BND_MAIN_MANY; i++){
            for(int j = 0; j < BND_NUMEDGE; j++){
                BND_Main_vrtx[(i * BND_NUMEDGE + j) * BND_NUMCOORD + 0] = BND_MAIN_RADII[j] * Math.cos(Math.toRadians(i * 360 / BND_MAIN_MANY)) / 2;
                BND_Main_vrtx[(i * BND_NUMEDGE + j) * BND_NUMCOORD + 1] = BND_MAIN_RADII[j] * Math.sin(Math.toRadians(i * 360 / BND_MAIN_MANY)) / 2;
                BND_Main_vrtx[(i * BND_NUMEDGE + j) * BND_NUMCOORD + 2] = BND_MAIN_HGTS[j];
            }
            BND_Shiny_inds[i] = (i * BND_NUMEDGE + 0);
            BND_Torso_inds[i * 2 + 0] = (i * BND_NUMEDGE + 0);
            BND_Torso_inds[i * 2 + 1] = (i * BND_NUMEDGE + 1);
            BND_Neck_inds[i * 2 + 0] = (i * BND_NUMEDGE + 1);
            BND_Neck_inds[i * 2 + 1] = (i * BND_NUMEDGE + 2);
            BND_Headl_inds[i * 2 + 0] = (i * BND_NUMEDGE + 2);
            BND_Headl_inds[i * 2 + 1] = (i * BND_NUMEDGE + 3);
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
        
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }
}
