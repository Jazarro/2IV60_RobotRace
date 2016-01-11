/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import Texture.ImplementedTexture;
import bodies.assembly.StackAssembler;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.media.opengl.GL2;

/**
 * Can create an instance of {@link SimpleBody} that consists of a stack of
 * partial toruses and conical frustums.
 *
 * The initial shape will be built at the bottom, with progressive shapes built
 * back to back on top of each other, creating a tunnel-like shape.
 *
 * @author Arjan Boschman
 */
public class StackBuilder {

    private final BufferManager.Initialiser bmInitialiser;
    private final StackAssembler assembler;
    private int sliceCount = 3;
    private ImplementedTexture textureTop = null;
    private ImplementedTexture textureBottom = null;
    private ImplementedTexture textureSide = null;
    private final List<Shape> texturedShapes;

    /**
     * Construct a new SimpleBody.StackBuilder. This builder can be used to
     * create one stacked body.
     *
     * @param bmInitialiser The BodyManager where the actual data will be
     *                      stored. The BodyManager deals with OpenGL directly,
     *                      the bodies created by these builders do not.
     */
    public StackBuilder(BufferManager.Initialiser bmInitialiser) {
        this.texturedShapes = new ArrayList<>();
        this.bmInitialiser = bmInitialiser;
        this.assembler = new StackAssembler();
    }

    /**
     * During the {@link #build()} call, the Shapes that have textures in them
     * will be stored. This getter retrieves those shapes. You can use these
     * handles to for instance swap out textures.
     *
     * @return A list of all Shapes in the constructed SimpleBody that have an
     *         ImplementedTexture.
     */
    public List<Shape> getTexturedShapes() {
        return Collections.unmodifiableList(texturedShapes);
    }

    /**
     * Create a SimpleBody from the previously given definition.
     *
     * @return A new SimpleBody.
     */
    public SimpleBody build() {
        /**
         * Tell Assembler to compile all the surfaces and then retrieve the
         * necessary values.
         */
        assembler.compileSurfaceCompilation();
        //Buffer containing all Vertextdata off all previously added shapes.
        //The data in in the format: vertexX, vertexY, vertexZ, normalX, normalY, normalZ.
        final FloatBuffer dataBuffer = assembler.getDataBuffer();
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
        final SimpleBody simpleBody = new SimpleBody();
        for (int i = 0; i < indexBufferNames.length; i++) {
            final int shapeMode = surfaceTypeList.get(i) ? GL2.GL_POLYGON : GL2.GL_QUAD_STRIP;
            final ImplementedTexture texture = textureList.get(i);
            final Shape shape = new Shape(indexBufferNames[i], indicesBufferList.get(i).capacity(), shapeMode).setTexture(texture);
            if (texture != null) {
                texturedShapes.add(shape);
            }
            simpleBody.addShape(shape);
        }
        return simpleBody;
    }

    /**
     * Sets the slice count for all shapes. The slice count defines the number
     * of faces each element of the stacked body has. The default is 3.
     *
     * NB: For now, it is necessary to keep the slice count the same for all
     * shapes in a stacked body.
     *
     * @param sliceCount The new slice count.
     *
     * @return This StackBuilder.
     */
    public StackBuilder setSliceCount(int sliceCount) {
        this.sliceCount = sliceCount;
        return this;
    }

    /**
     * Set all the texture properties of the stack part.
     *
     * @param textureTop    The texture for the top of the stack part.
     * @param textureBottom The texture for the bottom of the stack part.
     * @param textureSide   The texture for the sides of the stack part.
     * @return
     */
    public StackBuilder setTextures(ImplementedTexture textureTop, ImplementedTexture textureBottom, ImplementedTexture textureSide) {
        this.textureTop = textureTop;
        this.textureBottom = textureBottom;
        this.textureSide = textureSide;
        return this;
    }

    /**
     * Add a conical frustum (cone with top cut off) to the assembly.
     *
     * Do this by adding a partial torus, because a conical frustum is a partial
     * torus with stackCount equal to one.
     *
     * @param radiusLow   The radius of the lower ring of the frustum.
     * @param radiusHigh  The radius of the higher ring of the frustum.
     * @param heightLow   The height of the lower ring of the frustum.
     * @param heightHigh  The height of the higher ring of the frustum.
     * @param closeBottom If the lower end should be closed off.
     * @param closeTop    If the upper end should be closed off.
     *
     * @return This StackBuilder.
     */
    public StackBuilder addConicalFrustum(float radiusLow, float radiusHigh, float heightLow, float heightHigh, boolean closeBottom, boolean closeTop) {
        assembler.addConicalFrustum(sliceCount, radiusLow, radiusHigh, heightLow, heightHigh, closeBottom, closeTop, textureTop, textureBottom, textureSide);
        return this;
    }

    /**
     * Add a partial torus (torus with only one quarter of it's cross-section)
     * to the assembly.
     *
     * The lower ring and its properties will be ignored and the upper ring from
     * the last command will be used if compatible.
     *
     * @param stackCount  The number of stacks (z axis) that the torus is
     *                    divided in, more slices equals a smoother surface.
     * @param radiusLow   The radius of the lower ring of the torus.
     * @param radiusHigh  The radius of the higher ring of the torus.
     * @param heightLow   The height of the lower ring of the torus.
     * @param heightHigh  The height of the higher ring of the torus.
     * @param closeBottom If the lower end should be closed off.
     * @param closeTop    If the upper end should be closed off.
     *
     * @return This StackBuilder.
     */
    public StackBuilder addPartialTorus(int stackCount, float radiusLow, float radiusHigh, float heightLow, float heightHigh, boolean closeBottom, boolean closeTop) {
        assembler.addPartialTorus(sliceCount, stackCount, radiusLow, radiusHigh, heightLow, heightHigh, closeBottom, closeTop, textureTop, textureBottom, textureSide);
        return this;
    }

    /**
     * Add a polygon to close off the stacked body.
     *
     * @param height       The height relative to the stacked body.
     * @param isFacingDown Announce whether this polygon is facing up or down.
     *                     True is down, false is up.
     * @return This StackBuilder.
     */
    public StackBuilder addPolygon(double height, boolean isFacingDown) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add a conical frustum (cone with top cut off) to the assembly.
     *
     * Do this by adding a partial torus, because a conical frustum is a partial
     * torus with stackCount equal to one.
     *
     * @param radiusLow  The radius of the lower ring of the frustum.
     * @param radiusHigh The radius of the higher ring of the frustum.
     * @param heightLow  The height of the lower ring of the frustum.
     * @param heightHigh The height of the higher ring of the frustum.
     *
     * @return This StackBuilder.
     */
    public StackBuilder addConicalFrustum(double radiusLow, double radiusHigh, double heightLow, double heightHigh) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add a partial torus (torus with only one quarter of it's cross-section)
     * to the assembly.
     *
     * The lower ring and its properties will be ignored and the upper ring from
     * the last command will be used if compatible.
     *
     * @param stackCount The number of stacks (z axis) that the torus is divided
     *                   in, more slices equals a smoother surface.
     * @param radiusLow  The radius of the lower ring of the torus.
     * @param radiusHigh The radius of the higher ring of the torus.
     * @param heightLow  The height of the lower ring of the torus.
     * @param heightHigh The height of the higher ring of the torus.
     *
     * @return This StackBuilder.
     */
    public StackBuilder addPartialTorus(int stackCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh) {
        throw new UnsupportedOperationException();
    }

}
