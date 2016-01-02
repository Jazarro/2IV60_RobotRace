package bodies;

import bodies.assembly.TrackAssembler;
import bodies.assembly.Vertex;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.media.opengl.GL2;

public class TrackBuilder {

    private final BufferManager.Initialiser bmInitialiser;
    private final TrackAssembler assembler;
    private double laneWidth = 1.22d;
    private int laneCount = 4;
    private double trackHeight = 1d;

    public TrackBuilder(BufferManager.Initialiser bmInitialiser) {
        this.bmInitialiser = bmInitialiser;
        this.assembler = new TrackAssembler();
    }

    public SimpleBody build(List<Vertex> trackDescription) {
        assembler.calculateTrack(trackDescription, laneWidth, laneCount, trackHeight);
        //Buffer containing all Vertextdata off all previously added shapes. 
        //The data in in the format: vertexX, vertexY, vertexZ, normalX, normalY, normalZ.
        final DoubleBuffer dataBuffer = assembler.getDataBuffer();
        /**
         * List of index buffers. Each index buffer belongs to a shape and
         * consists of pointers to vertices in the data buffer.
         */
        final List<IntBuffer> indicesBufferList = assembler.getIndicesBuffers();
        /**
         * List of boolean flags. Runs parallel to the indicesBufferList and is
         * true if the shape is a polygon, false if it's a QuadStrip.
         */
        final List<Boolean> surfaceTypeList = assembler.getSurfaceTypeList();
        final int[] indexBufferNames = bmInitialiser.addData(dataBuffer, indicesBufferList);
        final SimpleBody simpleBody = new SimpleBody();
        for (int i = 0; i < indexBufferNames.length; i++) {
            final int shapeMode = surfaceTypeList.get(i) ? GL2.GL_POLYGON : GL2.GL_QUAD_STRIP;
            simpleBody.addShape(new Shape(indexBufferNames[i], indicesBufferList.get(i).capacity(), shapeMode));
        }
        return simpleBody;
    }

    public TrackBuilder setTrackProperties(double laneWidth, int laneCount, double trackHeight) {
        this.laneWidth = laneWidth;
        this.laneCount = laneCount;
        this.trackHeight = trackHeight;
        return this;
    }
}
