package robotrace.bender;

import com.jogamp.common.nio.*;
import static java.lang.Math.*;
import java.nio.*;
import javax.media.opengl.*;
import utility.*;

public class Body{

    private static final double RADII[] = {0.175d, 0.225d, 0.125d, 0.125d, 0d, 0.05d, 0.03d, 0.01d, 0.025d};
    private static final double HEIGHTS[] = {0d, 0.45d, 0.55d, 0.875d, 1d, 0.875d + sqrt(pow(RADII[3], 2d) - pow(RADII[5], 2d)), 1.175d, 1d, 1.175d + sqrt(pow(RADII[7], 2d) - pow(RADII[4], 2d))};

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
    private static final int STACK_COUNT = 50;

    private int[] glBufferNames;
    SurfaceCompilation surfaceCompilation;

    public Body(){
    }

    public void initialize(GL2 gl){
        final Assembler bodyAssembler = new Assembler();
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_SHINY, RADIUS_TORSO, HEIGHT_SHINY, HEIGHT_TORSO, true, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_TORSO, RADIUS_NECK, HEIGHT_TORSO, HEIGHT_NECK, false, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_NECK, RADIUS_HEAD, HEIGHT_NECK, HEIGHT_HEAD, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_HEAD, RADIUS_ANTENNA_BOTTOM, HEIGHT_HEAD, HEIGHT_ANTENNA_BOTTOM, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_BOTTOM, RADIUS_ANTENNA_MIDDLE, HEIGHT_ANTENNA_BOTTOM, HEIGHT_ANTENNA_MIDDLE, false, false);
        bodyAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_ANTENNA_MIDDLE, RADIUS_ANTENNA_TOP, HEIGHT_ANTENNA_MIDDLE, HEIGHT_ANTENNA_TOP, false, false);
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_TOP, RADIUS_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, false, false);
        //TODO: Fix ball on antenna
        bodyAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_ANTENNA_BALL_MIDDLE, RADIUS_ANTENNA_BALL_TOP, HEIGHT_ANTENNA_BALL_MIDDLE, HEIGHT_ANTENNA_BALL_TOP, false, false);

        surfaceCompilation = bodyAssembler.makeSurfaceCompilation();
        glBufferNames = new int[surfaceCompilation.size() + 1];
        gl.glGenBuffers(surfaceCompilation.size() + 1, glBufferNames, 0);

        DoubleBuffer vrtxBuff = Buffers.newDirectDoubleBuffer(surfaceCompilation.getVertices());
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glBufferNames[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, surfaceCompilation.getVertices().length * Double.BYTES, vrtxBuff, GL2.GL_STATIC_DRAW);

        for(int i = 0; i < surfaceCompilation.size(); i++){
            IntBuffer indsBuff = Buffers.newDirectIntBuffer(surfaceCompilation.getSurfaces().get(i).getVertexIndices());
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glBufferNames[i + 1]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, surfaceCompilation.getSurfaces().get(i).getVertexIndices().length * Integer.BYTES, indsBuff, GL2.GL_STATIC_DRAW);
        }

    }

    public void draw(GL2 gl){
        gl.glPushMatrix();
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glBufferNames[0]);
        gl.glVertexPointer(Assembler.NUMCOORD, GL2.GL_DOUBLE, 1 * Assembler.NUMCOORD * Double.BYTES, 0);
        //gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Assembler.NUMCOORD * Double.BYTES, 1 * Assembler.NUMCOORD * Double.BYTES);
        for(int i = 0; i < surfaceCompilation.size(); i++){
            drawBuffer(gl, i);
        }
        gl.glPopMatrix();
    }

    private void drawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glBufferNames[buffInd + 1]);
        gl.glDrawElements((surfaceCompilation.getSurfaces().get(buffInd).isPolygon() ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)),
                          surfaceCompilation.getSurfaces().get(buffInd).getVertexIndices().length, GL2.GL_UNSIGNED_INT, 0);
    }
}
