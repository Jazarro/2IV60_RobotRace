/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import Texture.ImplementedTexture;
import bodies.assembly.TrackAssembler;
import bodies.assembly.Vertex;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.List;
import javax.media.opengl.GL2;

/**
 *
 * @author Robke Geenen
 */
public class TrackBuilder {

    private final BufferManager.Initialiser bmInitialiser;
    private final TrackAssembler assembler;
    private double laneWidth = 1.22d;
    private int laneCount = 4;
    private double trackHeight = 1d;
    private boolean closedTrack = true;
    private ImplementedTexture textureTop;
    private ImplementedTexture textureBottom;
    private ImplementedTexture textureSide;

    /**
     * The constructor.
     *
     * @param bmInitialiser The BufferManager initializer used to construct the
     *                      RaceTracks.
     */
    public TrackBuilder(BufferManager.Initialiser bmInitialiser) {
        this.bmInitialiser = bmInitialiser;
        this.assembler = new TrackAssembler();
    }

    public SimpleBody build(List<Vertex> trackDescription) {
        assembler.calculateTrack(trackDescription, laneWidth, laneCount, trackHeight, closedTrack, textureTop, textureBottom, textureSide);
        /**
         * Buffer containing all vertex data from all previously added shapes.
         * The data in in the format: vertexX, vertexY, vertexZ, normalX,
         * normalY, normalZ.
         */
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
        final List<ImplementedTexture> textureList = assembler.getTextureList();
        final int[] indexBufferNames = bmInitialiser.addData(dataBuffer, indicesBufferList);
        /**
         * Create the SimpleBody that represents this RaceTrack.
         */
        final SimpleBody simpleBody = new SimpleBody();
        for (int i = 0; i < indexBufferNames.length; i++) {
            final int shapeMode = surfaceTypeList.get(i) ? GL2.GL_POLYGON : GL2.GL_QUAD_STRIP;
            final ImplementedTexture texture = textureList.get(i);
            simpleBody.addShape(new Shape(indexBufferNames[i], indicesBufferList.get(i).capacity(), shapeMode).setTexture(texture));
        }
        return simpleBody;
    }

    /**
     * Set the properties of the RaceTrack.
     *
     * @param laneWidth   The width of one lane.
     * @param laneCount   The number of lanes on a track.
     * @param trackHeight The height of the RaceTrack.
     * @param closedTrack If the track is closed.
     * @return The updated TrackBuilder object.
     */
    public TrackBuilder setTrackProperties(double laneWidth, int laneCount, double trackHeight, boolean closedTrack) {
        this.laneWidth = laneWidth;
        this.laneCount = laneCount;
        this.trackHeight = trackHeight;
        this.closedTrack = closedTrack;
        return this;
    }

    /**
     * Set all the texture properties of the RaceTrack.
     *
     * @param textureTop    The texture for the top of the RaceTrack.
     * @param textureBottom The texture for the bottom of the RaceTrack.
     * @param textureSide   The texture for the sides of the RaceTrack.
     * @return
     */
    public TrackBuilder setTextures(ImplementedTexture textureTop, ImplementedTexture textureBottom, ImplementedTexture textureSide) {
        this.textureTop = textureTop;
        this.textureBottom = textureBottom;
        this.textureSide = textureSide;
        return this;
    }

}
