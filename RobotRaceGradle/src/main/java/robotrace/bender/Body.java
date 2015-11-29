package robotrace.bender;

import com.jogamp.common.nio.Buffers;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import utility.Assembler;
import utility.Surface;
import utility.SurfaceCompilation;

public class Body{

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

    private static final int SLICE_COUNT = 4;
    private static final int STACK_COUNT = 4;

    private int[] glIndexBufferNames;
    private int glVertexBufferName;
    private int glNormalBufferName;
    private SurfaceCompilation surfaceCompilation;

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
        final int[] tempBufferNames = new int[surfaceCompilation.size() + 2];
        gl.glGenBuffers(tempBufferNames.length, tempBufferNames, 0);
        this.glIndexBufferNames = new int[tempBufferNames.length - 2];
        System.arraycopy(tempBufferNames, 2, glIndexBufferNames, 0, glIndexBufferNames.length);
        this.glVertexBufferName = tempBufferNames[0];
        this.glNormalBufferName = tempBufferNames[1];
        
        final double all[] = new double[surfaceCompilation.getVertices().length + surfaceCompilation.getNormals().length];
        System.arraycopy(surfaceCompilation.getVertices(), 0, all, 0, surfaceCompilation.getVertices().length);
        System.arraycopy(surfaceCompilation.getNormals(), 0, all, surfaceCompilation.getVertices().length, surfaceCompilation.getNormals().length);

        //final DoubleBuffer vertexBuff = Buffers.newDirectDoubleBuffer(surfaceCompilation.getVertices());
        final DoubleBuffer vertexBuff = Buffers.newDirectDoubleBuffer(all);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glVertexBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, surfaceCompilation.getVertices().length * Double.BYTES, vertexBuff, GL2.GL_STATIC_DRAW);

        final DoubleBuffer normalBuff = Buffers.newDirectDoubleBuffer(surfaceCompilation.getNormals());
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glNormalBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, surfaceCompilation.getNormals().length * Double.BYTES, normalBuff, GL2.GL_STATIC_DRAW);

        for(int surfaceIndex = 0; surfaceIndex < surfaceCompilation.size(); surfaceIndex++){
            final Surface surface = surfaceCompilation.getSurfaces().get(surfaceIndex);
            final int[] combinedIndices = new int[surface.getVertexIndices().length * 2];
            for(int vertexIndex = 0; vertexIndex < surface.getVertexIndices().length; vertexIndex++){
                combinedIndices[vertexIndex * 2 + 0] = surface.getVertexIndices()[vertexIndex];
                combinedIndices[vertexIndex * 2 + 1] = surface.getNormalIndices()[vertexIndex];
            }
            final IntBuffer indexBuff = Buffers.newDirectIntBuffer(combinedIndices);
            //final IntBuffer indexBuff = Buffers.newDirectIntBuffer(surface.getVertexIndices());
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glIndexBufferNames[surfaceIndex]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, combinedIndices.length * Integer.BYTES, indexBuff, GL2.GL_STATIC_DRAW);
        }

    }

    public void draw(GL2 gl){
        gl.glPushMatrix();
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glVertexBufferName);
        //gl.glVertexPointer(Assembler.NUMCOORD, GL2.GL_DOUBLE, 0, 0);
        gl.glVertexPointer(Assembler.NUMCOORD, GL2.GL_DOUBLE, 0, 0);
        //gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, glNormalBufferName);
        gl.glNormalPointer(GL2.GL_DOUBLE, 0, 0);
        for(int i = 0; i < surfaceCompilation.size(); i++){
            drawBuffer(gl, i);
        }
        gl.glPopMatrix();
    }

    private void drawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, glIndexBufferNames[buffInd]);
        gl.glDrawElements((surfaceCompilation.getSurfaces().get(buffInd).isPolygon() ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)),
                          surfaceCompilation.getSurfaces().get(buffInd).getVertexIndices().length, GL2.GL_UNSIGNED_INT, 0);
    }

}
