/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies.assembly;

import Texture.ImplementedTexture;
import static bodies.assembly.Vertex.COORD_COUNT;
import static bodies.assembly.Vertex.IND_X;
import static bodies.assembly.Vertex.IND_Y;
import static bodies.assembly.Vertex.IND_Z;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import robotrace.Vector;

/**
 *
 * @author Robke Geenen
 * @author Arjan Boschman
 */
public class StackAssembler {

    /**
     * The collection of Rings that make up the assembly.
     */
    private final List<Ring> rings = new ArrayList<>();
    /**
     * The SurfaceCompilation in which the assembly is compiled by
     * compileSurfaceCompilation.
     */
    private final SurfaceCompilation surfaceCompilation = new SurfaceCompilation();
    private final List<ImplementedTexture> textureList = new ArrayList<>();
    private float stackHeight = 0;

    /**
     * Add a conical frustum (cone with top cut off) to the assembly.
     *
     * Do this by adding a partial torus, because a conical frustum is a partial
     * torus with stackCount equal to one.
     *
     * @param sliceCount The number of slices (xy plane) that the frustum is
     *                   divided in, more slices equals a smoother surface.
     * @param radiusLow  The radius of the lower ring of the frustum.
     * @param radiusHigh The radius of the higher ring of the frustum.
     * @param heightLow  The height of the lower ring of the frustum.
     * @param heightHigh The height of the higher ring of the frustum.
     * @param closeLow   If the lower ring should be a closed surface.
     * @param closeHigh  If the higher ring should be a closed surface.
     */
    public void addConicalFrustum(int sliceCount, float radiusLow, float radiusHigh, float heightLow, float heightHigh, boolean closeLow, boolean closeHigh, ImplementedTexture textureTop, ImplementedTexture textureBottom, ImplementedTexture textureSide) {
        addPartialTorus(sliceCount, 1, radiusLow, radiusHigh, heightLow, heightHigh, closeLow, closeHigh, textureTop, textureBottom, textureSide);
    }

    /**
     * Add a partial torus (torus with only one quarter of it's cross-section)
     * to the assembly.
     *
     * The lower ring and its properties will be ignored and the upper ring from
     * the last command will be used if compatible.
     *
     * @param sliceCount The number of slices (xy plane) that the torus is
     *                   divided in, more slices equals a smoother surface.
     * @param stackCount The number of stacks (z axis) that the torus is divided
     *                   in, more slices equals a smoother surface.
     * @param radiusLow  The radius of the lower ring of the torus.
     * @param radiusHigh The radius of the higher ring of the torus.
     * @param heightLow  The height of the lower ring of the torus.
     * @param heightHigh The height of the higher ring of the torus.
     * @param closeLow   If the lower ring should be a closed surface.
     * @param closeHigh  If the higher ring should be a closed surface.
     */
    public void addPartialTorus(int sliceCount, int stackCount, float radiusLow, float radiusHigh, float heightLow, float heightHigh, boolean closeLow, boolean closeHigh, ImplementedTexture textureTop, ImplementedTexture textureBottom, ImplementedTexture textureSide) {
        //Make a new ring if the previous one can not be reused.
        if (((rings.isEmpty()) || (sliceCount != rings.get(rings.size() - 1).getSliceCount())) /*&& rings.get(rings.size() - 1).getRadius() == radiusLow
           && rings.get(rings.size() - 1).getHeight() == heightLow*/) {
            rings.add(makeRing(radiusLow, heightLow, sliceCount, true, closeLow, stackHeight, textureBottom, textureSide));
        }
        //Switch the radii and heights if the torus has a smaller upper radius.
        final float radiusFrom, radiusTo, heightFrom, heightTo;
        if (radiusHigh > radiusLow) {
            radiusFrom = radiusLow;
            radiusTo = radiusHigh - radiusLow;
            heightFrom = heightHigh;
            heightTo = heightLow - heightHigh;
        } else {
            radiusFrom = radiusHigh;
            radiusTo = radiusLow - radiusHigh;
            heightFrom = heightLow;
            heightTo = heightHigh - heightLow;
        }
        //Interpolate the radii and heights stackCount times, as to generate enough rings to get a smooth surface.
        for (int i = 1; i < stackCount; i++) {
            final float radiusInterpolated = radiusFrom + (float) (radiusTo * cos(toRadians((double) i * 90f / stackCount)));
            final float heightInterpolated = heightFrom + (float) (heightTo * sin(toRadians((double) i * 90f / stackCount)));
            rings.add(makeRing(radiusInterpolated, heightInterpolated, sliceCount, false, false, stackCount + (i / stackCount), textureBottom, textureSide));
        }
        //Add the last ring reperately, because it needs to be sharp and can be closed.
        rings.add(makeRing(radiusHigh, heightHigh, sliceCount, true, closeHigh, stackHeight + 1f, textureTop, textureSide));
        stackHeight += 1f;
    }

    /**
     * Compile all current objects in the assembly into an SurfaceCompilation.
     */
    public void compileSurfaceCompilation() {
        List<IndexedVertex> knownVertices = new ArrayList<>();
        //Iterate over the Rings to create polygon surfaces for closed rings and quad strip surfaces for two consecutive Rings.
        for (int i = 0; i < rings.size(); i++) {
            if (rings.get(i).isClosed()) {
                //If the ring represents a closed surface, create a polygon Surface to close the Ring.
                final Surface surface = makeSurfacePolygon(rings.get(i));
                surfaceCompilation.addSurface(surface);
                textureList.add(rings.get(i).getTexturePolygon());
            }
            if (i != 0) {
                //From the second Ring onwards create Surfaces between two consecutive Rings.
                final Surface surface = makeSurfaceQuadStrip((i < 2) ? null : rings.get(i - 2), rings.get(i - 1), rings.get(i), ((i + 1) >= rings.size()) ? null : rings.get(i + 1), (i == 1) ? null : knownVertices);
                knownVertices = surfaceCompilation.addSurface(surface);
                textureList.add(rings.get(i).getTextureSide());
            }
        }
    }

    /**
     * Get a buffer with all vertices of the SurfaceCompilation.
     *
     * @return A buffer with all vertices of the SurfaceCompilation.
     */
    public FloatBuffer getDataBuffer() {
        return surfaceCompilation.getDataBuffer();
    }

    /**
     * Get a list of buffers with the indices of all vertices of all surfaces.
     *
     * @return A list of buffers with the indices of all vertices of all
     *         surfaces.
     */
    public List<IntBuffer> getIndicesBuffers() {
        return surfaceCompilation.getIndicesBuffers();
    }

    /**
     * Get a list of surface types (polygon or quad strip).
     *
     * @return A list of surface types.
     */
    public List<Boolean> getSurfaceTypeList() {
        return surfaceCompilation.getSurfaceTypeList();
    }

    /**
     * Get a list of textures.
     *
     * @return A list of textures.
     */
    public List<ImplementedTexture> getTextureList() {
        return textureList;
    }

    /**
     * Create a polygon surface from a Ring representing a closed surface.
     *
     * @param ring The Ring representing the closed surface.
     *
     * @return The calculated Surface.
     */
    private Surface makeSurfacePolygon(Ring ring) {
        final List<Vertex> vertices = ring.getVertices();
        final List<IndexedVertex> indexedVertices = new ArrayList<>();
        //Iterate over all vertices in the Ring.
        for (Vertex vertex : vertices) {
            //Calculate the normal of the polygon vertex.
            vertex.setNormalA(calculatePolygonNormal());
            final Vector textureCoord = vertex.getPositionV().normalized();
            vertex.setTextureC((float) textureCoord.x(), (float) textureCoord.y(), 0f);
            indexedVertices.add(IndexedVertex.makeIndexedVertex(vertex));
        }
        //Create a new surface from all calculated vertices.
        return new Surface(indexedVertices, true);
    }

    /**
     * Calculate the normal for a polygon surface.
     *
     * @return The normal of a polygon surface.
     */
    private float[] calculatePolygonNormal() {
        //The normal of a horizontal polygon surface always is in the negative z direction.
        final float[] normal = new float[COORD_COUNT];
        normal[IND_X] = 0;
        normal[IND_Y] = 0;
        normal[IND_Z] = -1; //todo: check if this normal is correct
        return normal;
    }

    /**
     * Create a new quad strip surface connecting two rings.
     *
     * @param ring0         The ring before the surface.
     * @param ring1         The first ring of the surface.
     * @param ring2         The second ring of the surface.
     * @param ring3         The ring after the surface.
     * @param knownVertices The list of vertices that can be reused from the
     *                      previous surface.
     *
     * @return The new Surface.
     */
    private Surface makeSurfaceQuadStrip(Ring ring0, Ring ring1, Ring ring2, Ring ring3, List<IndexedVertex> knownVertices) {
        final Iterator<Vertex> vertices1 = (ring1 == null) ? Collections.emptyIterator() : ring1.getVertices().iterator();
        final Iterator<Vertex> vertices2 = (ring2 == null) ? Collections.emptyIterator() : ring2.getVertices().iterator();
        final Iterator<IndexedVertex> sharedVertices = (knownVertices == null) ? Collections.emptyIterator() : knownVertices.iterator();
        final List<IndexedVertex> indexedVertices = new ArrayList<>();
        //Iterate over all vertices of both rings of the surface.
        while (vertices1.hasNext() || vertices2.hasNext()) {
            //And interleave them into indexedVertices, so that it becomes a proper quad strip.
            if (vertices1.hasNext() && (ring1 != null)) {
                final Vertex vertex = vertices1.next();
                final float textureX = (float) ring1.getVertices().indexOf(vertex) / (float) ring1.getVertices().size();
                final IndexedVertex newVertex;
                //If a vertex can be shared, do so.
                if (sharedVertices.hasNext()) {
                    newVertex = sharedVertices.next();
                } //Else calculate the normal of the unknown vertex and use the vertex with it's normal.
                else {
                    vertex.setNormalA(calculateQuadStripNormal(ring1.getVertices().indexOf(vertex), ring0, ring1, ring2));
                    vertex.setTextureC(textureX, ring1.getTextureSmear(), 0f);
                    newVertex = IndexedVertex.makeIndexedVertex(vertex);
                }
                //And store the vertex.
                indexedVertices.add(newVertex);
            }
            //Again, interleaving, it means first vertex 1, then vertex 2, so here is vertex 2.
            if (vertices2.hasNext() && (ring2 != null)) {
                final Vertex vertex = vertices2.next();
                final float textureX = (float) ring2.getVertices().indexOf(vertex) / (float) ring2.getVertices().size();
                //The second ring is not calculated (yet), and can therefore not (yet) be reused.
                //So calculate the normal.
                vertex.setNormalA(calculateQuadStripNormal(ring2.getVertices().indexOf(vertex), ring1, ring2, ring3));
                vertex.setTextureC(textureX, ring2.getTextureSmear(), 0f);
                //And store it.
                indexedVertices.add(IndexedVertex.makeIndexedVertex(vertex));
            }
        }
        //Return the new Surface created from all just calculated vertices with their normals.
        return new Surface(indexedVertices, false);
    }

    /**
     * Calculate the normals of a ring in a quad strip surface.
     *
     * @param index The index of the vertex currently being calculated.
     * @param ring0 The ring below the ring currently being calculated.
     * @param ring1 The ring currently being calculated.
     * @param ring2 The ring above the ring currently being calculated.
     *
     * @return The normal of the specified vertex.
     */
    private float[] calculateQuadStripNormal(int index, Ring ring0, Ring ring1, Ring ring2) {
        final float[] vertexNormal;
        //If the normal is already calculated, it does not need to be calculated again.
        if (ring1.isVertexNormalCalculated(index)) {
            vertexNormal = ring1.getVertices().get(index).getNormalA();
        } else //If this is the only ring (it has no neighboring rings) then no surface is defined, and also no normal.
         if ((ring2 == null) && (ring0 == null)) {
                vertexNormal = new float[COORD_COUNT];
                vertexNormal[IND_X] = 0;
                vertexNormal[IND_Y] = 0;
                vertexNormal[IND_Z] = 0;
            } else //If this is the last ring, then the normal is equal for both the first and second ring of the surface.
             if (ring2 == null) {
                    vertexNormal = ring0.getVertices().get(index).getNormalA();
                } //Else the normal will be calculated.
                else {
                    vertexNormal = calculateNormal(index, ring1.getRadius() - ring2.getRadius(), ring1.getHeight() - ring2.getHeight(), ring1.getVertices().size());
                    final Vertex vertex = ring1.getVertices().get(index);
                    vertex.setNormalA(vertexNormal);
                    //And updated in the ring.
                    ring1.setVertex(vertex, index);
                    ring1.setVertexNormalCalculated(index);
                }
        if ((!ring1.isSharp()) && (ring0 != null)) {
            //If the ring represents a smooth edge, the normal will be averaged with the normal below it.
            for (int i = 0; i < COORD_COUNT; i++) {
                //By averaging the array element-wise.
                vertexNormal[i] += ring0.getVertices().get(index).getNormalA()[i];
                vertexNormal[i] *= 0.5d;
            }
        }
        //Average the last vertex with the first one to get a smooth transition and use that normal for both the first and the last vertex.
        if ((index == (ring1.getVertices().size() - 1))) {
            final Vertex firstVertex = ring1.getVertices().get(0);
            for (int i = 0; i < COORD_COUNT; i++) {
                //This also happens by element-wise averaging.
                vertexNormal[i] += firstVertex.getNormalA()[i];
                vertexNormal[i] *= 0.5d;
            }
            firstVertex.setNormalA(vertexNormal);
            ring1.setVertex(firstVertex, 0);
        }
        //Lastly the normal is returned.
        return vertexNormal;
    }

    /**
     * Create a new Ring from the defining information.
     *
     * @param radius     The radius of the Ring.
     * @param height     The height of the Ring.
     * @param sliceCount The number of slices in the Ring.
     * @param isSharp    If the Ring represents a sharp edge.
     * @param isClosed   If the Ring represents a closed surface.
     *
     * @return The new Ring.
     */
    private Ring makeRing(float radius, float height, int sliceCount, boolean isSharp, boolean isClosed, float textureSmear, ImplementedTexture texturePolygon, ImplementedTexture textureSide) {
        final List<Vertex> vertices = new ArrayList<>();
        //Iterate over all slices in the new Ring and calculate their position.
        for (int i = 0; i < sliceCount + 1; i++) {
            vertices.add(new Vertex(calculatePosition(i, radius, height, sliceCount)));
        }
        //And then return the new Ring with all vertices.
        return new Ring(vertices, isSharp, isClosed, radius, height, sliceCount, textureSmear, texturePolygon, textureSide);
    }

    /**
     * Calculate the position of a vertex of a ring.
     *
     * @param angleIndex The number of the slice that has to be calculated.
     * @param radius     The radius of the ring.
     * @param height     The height of the ring.
     * @param sliceCount The total number of slices in the surface.
     *
     * @return The position.
     */
    private static float[] calculatePosition(int angleIndex, float radius, float height, int sliceCount) {
        //Calculate the normal of the surface in circular coordinates.
        final float sliceAngle = (float) toRadians((double) angleIndex * 360F / sliceCount);
        //And store the results as x, y, z components in an array.
        final float[] position = new float[COORD_COUNT];
        position[IND_X] = radius * (float) cos(sliceAngle);
        position[IND_Y] = radius * (float) sin(sliceAngle);
        position[IND_Z] = height;
        return position;
    }

    /**
     * Calculate the normal of a surface in a vertex.
     *
     * @param angleIndex  The number of the slice that has to be calculated.
     * @param deltaRadius The difference in radius between the bottom and the
     *                    top of the surface.
     * @param deltaHeight The difference in height between the bottom and the
     *                    top of the surface.
     * @param sliceCount  The total number of slices in the surface.
     *
     * @return The normal.
     */
    private static float[] calculateNormal(int angleIndex, float deltaRadius, float deltaHeight, int sliceCount) {
        //Calculate the normal of the surface in spherical coordinates.
        final float sliceAngle = (float) toRadians(angleIndex * 360F / sliceCount);
        final float stackAngle = (float) ((PI * 0.5d) - atan(deltaRadius / deltaHeight));
        //And store the results as x, y, z components in an array.
        final float[] normal = new float[COORD_COUNT];
        normal[IND_X] = (float) (cos(sliceAngle) * sin(stackAngle));
        normal[IND_Y] = (float) (sin(sliceAngle) * sin(stackAngle));
        normal[IND_Z] = (float) cos(stackAngle);
        return normal;
    }

    private static final class Ring {

        /**
         * The list of Vertex's defining this Ring.
         */
        private final List<Vertex> vertices;
        /**
         * If the edge represented by this Ring is sharp and/or represents a
         * closed surface.
         */
        private final boolean sharp;
        private final boolean closed;
        /**
         * If the vertices in this Ring have undergone normal calculation yet,
         * can not be reset.
         */
        private final boolean vertexNormalCalculated[];
        /**
         * The original properties that specified this Ring.
         */
        private final float radius;
        private final float height;
        private final float sliceCount;

        private final float textureSmear;
        private final ImplementedTexture texturePolygon;
        private final ImplementedTexture textureSide;

        /**
         * Constructor taking all properties of this Ring.
         *
         * @param vertices   The list of Vertex's defining this Ring.
         * @param sharp      If the edge represented by this Ring is sharp.
         * @param closed     If this Ring represents a closed surface.
         * @param radius     The original radius that specified this Ring.
         * @param height     The original height that specified this Ring.
         * @param sliceCount The original number of slices that specified this
         *                   Ring.
         */
        private Ring(List<Vertex> vertices, boolean sharp, boolean closed, float radius, float height, float sliceCount, float textureSmear, ImplementedTexture texturePolygon, ImplementedTexture textureSide) {
            this.vertices = vertices;
            this.sharp = sharp;
            this.closed = closed;
            this.vertexNormalCalculated = new boolean[vertices.size()];
            //Register that no normals are calculated in a new Ring.
            for (int i = 0; i < vertexNormalCalculated.length; i++) {
                vertexNormalCalculated[i] = false;
            }
            this.radius = radius;
            this.height = height;
            this.sliceCount = sliceCount;
            this.textureSmear = textureSmear;
            this.texturePolygon = texturePolygon;
            this.textureSide = textureSide;
        }

        /**
         * Get the list of Vertex's defining this Ring.
         *
         * @return The list of Vertex's defining this Ring.
         */
        private List<Vertex> getVertices() {
            return vertices;
        }

        /**
         * Set a new Vertex at position index in the list of Vertex's defining
         * this Ring.
         *
         * @param vertex The new Vertex.
         * @param index  The position in the list of Vertex's defining this
         *               Ring.
         */
        private void setVertex(Vertex vertex, int index) {
            this.vertices.set(index, vertex);
        }

        /**
         * Get if the edge represented by this Ring is sharp.
         *
         * @return If the edge represented by this Ring is sharp.
         */
        private boolean isSharp() {
            return sharp;
        }

        /**
         * Get if this Ring represents a closed surface.
         *
         * @return If this Ring represents a closed surface.
         */
        private boolean isClosed() {
            return closed;
        }

        /**
         * Get if the vertex in this Ring has undergone normal calculation yet.
         *
         * @param index The index of the vertex that has to be checked if it has
         *              undergone normal compilation yet.
         *
         * @return If the vertex in this Ring has undergone normal calculation
         *         yet.
         */
        private boolean isVertexNormalCalculated(int index) {
            return vertexNormalCalculated[index];
        }

        /**
         * Set that the vertex in this Ring has undergone normal calculation.
         *
         * @param index The index of the vertex that has undergone normal
         *              calculation.
         */
        private void setVertexNormalCalculated(int index) {
            this.vertexNormalCalculated[index] = true;
        }

        /**
         * Get the original radius that specified this Ring.
         *
         * @return The original radius that specified this Ring.
         */
        private float getRadius() {
            return radius;
        }

        /**
         * Get the original height that specified this Ring.
         *
         * @return The original height that specified this Ring.
         */
        private float getHeight() {
            return height;
        }

        /**
         * Get the original number of slices that specified this Ring.
         *
         * @return The original number of slices that specified this Ring.
         */
        public float getSliceCount() {
            return sliceCount;
        }

        public float getTextureSmear() {
            return textureSmear;
        }

        public ImplementedTexture getTexturePolygon() {
            return texturePolygon;
        }

        public ImplementedTexture getTextureSide() {
            return textureSide;
        }

    }
}
