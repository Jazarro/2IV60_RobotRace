/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies.assembly;

import com.jogamp.common.nio.Buffers;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static bodies.assembly.Vertex.COORD_COUNT;
import static bodies.assembly.Vertex.IND_X;
import static bodies.assembly.Vertex.IND_Y;
import static bodies.assembly.Vertex.IND_Z;

/**
 *
 * @author Robke Geenen
 * @author Arjan Boschman
 */
public class Assembler {

    /**
     * The collection of Rings that make up the assembly.
     */
    private final List<Ring> rings = new ArrayList<>();
    /**
     * The SurfaceCompilation in which the assembly is compiled by
     * compileSurfaceCompilation.
     */
    private SurfaceCompilation surfaceCompilation;

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
    public void addConicalFrustum(int sliceCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh) {
        addPartialTorus(sliceCount, 1, radiusLow, radiusHigh, heightLow, heightHigh, closeLow, closeHigh);
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
    public void addPartialTorus(int sliceCount, int stackCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh) {
        //Make a new ring if the previous one can not be reused.
        if (((rings.isEmpty()) || (sliceCount != rings.get(rings.size() - 1).getSliceCount())) /*&& rings.get(rings.size() - 1).getRadius() == radiusLow
           && rings.get(rings.size() - 1).getHeight() == heightLow*/) {
            rings.add(makeRing(radiusLow, heightLow, sliceCount, true, closeLow));
        }
        //Switch the radii and heights if the torus has a smaller upper radius.
        final double radiusFrom, radiusTo, heightFrom, heightTo;
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
            final double radiusInterpolated = radiusFrom + (radiusTo * cos(toRadians((double) i * 90d / (double) stackCount)));
            final double heightInterpolated = heightFrom + (heightTo * sin(toRadians((double) i * 90d / (double) stackCount)));
            rings.add(makeRing(radiusInterpolated, heightInterpolated, sliceCount, false, false));
        }
        //Add the last ring reperately, because it needs to be sharp and can be closed.
        rings.add(makeRing(radiusHigh, heightHigh, sliceCount, true, closeHigh));
    }

    /**
     * Compile all current objects in the assembly into an SurfaceCompilation.
     */
    public void compileSurfaceCompilation() {
        final SurfaceCompilation newSurfaceCompilation = new SurfaceCompilation();
        List<IndexedVertex> knownVertices = new ArrayList<>();
        //Iterate over the Rings to create polygon surfaces for closed rings and quad strip surfaces for two consecutive Rings.
        for (int i = 0; i < rings.size(); i++) {
            if (rings.get(i).isClosed()) {
                //If the ring represents a closed surface, create a polygon Surface to close the Ring.
                final Surface surface = makeSurfacePolygon(rings.get(i));
                newSurfaceCompilation.addSurface(surface);
            }
            if (i != 0) {
                //From the second Ring onwards create Surfaces between two consecutive Rings.
                final Surface surface = makeSurfaceQuadStrip((i < 2) ? null : rings.get(i - 2), rings.get(i - 1), rings.get(i), ((i + 1) >= rings.size()) ? null : rings.get(i + 1), (i == 1) ? null : knownVertices);
                knownVertices = newSurfaceCompilation.addSurface(surface);
            }
        }
        //Store the complete SurfaceCompilation globally so it's properties can be queried.
        this.surfaceCompilation = newSurfaceCompilation;
    }

    /**
     * Get a buffer with all vertices of the SurfaceCompilation.
     *
     * @return A buffer with all vertices of the SurfaceCompilation.
     */
    public DoubleBuffer getDataBuffer() {
        final List<Double> dataList = new ArrayList<>();
        //Iterate over all vertices in the SurfaceCompilation.
        for (IndexedVertex vertex : surfaceCompilation.getVertices()) {
            //And store all vertices' data in an ArrayList.
            dataList.add(vertex.getVertex().getPosition()[IND_X]);
            dataList.add(vertex.getVertex().getPosition()[IND_Y]);
            dataList.add(vertex.getVertex().getPosition()[IND_Z]);
            dataList.add(vertex.getVertex().getNormal()[IND_X]);
            dataList.add(vertex.getVertex().getNormal()[IND_Y]);
            dataList.add(vertex.getVertex().getNormal()[IND_Z]);
        }
        final double[] dataArray = new double[dataList.size()];
        //Convert the ArrayList into an array.
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = dataList.get(i);
        }
        //Deliver it in the form of a buffer.
        return Buffers.newDirectDoubleBuffer(dataArray);
    }

    /**
     * Get a list of buffers with the indices of all vertices of all surfaces.
     *
     * @return A list of buffers with the indices of all vertices of all
     *         surfaces.
     */
    public List<IntBuffer> getIndicesBuffers() {
        final List<IntBuffer> indicesBufferList = new ArrayList<>();
        //Iterate over the surfaces in the SurfaceCompilation.
        for (Surface surface : surfaceCompilation.getSurfaces()) {
            //Get the list of indices from the surface.
            final List<Integer> indicesList = surface.getIndices();
            final int[] indicesArray = new int[indicesList.size()];
            //And convert it from an ArrayList to an array.
            for (int i = 0; i < indicesArray.length; i++) {
                indicesArray[i] = indicesList.get(i);
            }
            //Add it to the buffer list in the form of a buffer.
            indicesBufferList.add(Buffers.newDirectIntBuffer(indicesArray));
        }
        return indicesBufferList;
    }

    /**
     * Get a list of surface types (polygon or quad strip).
     *
     * @return A list of surface types.
     */
    public List<Boolean> getSurfaceTypeList() {
        final List<Boolean> surfaceTypeList = new ArrayList<>();
        //Iterate over all surfaces in the SurfaceCompilation.
        for (Surface surface : surfaceCompilation.getSurfaces()) {
            //Add the type of the surface to the list.
            surfaceTypeList.add(surface.isPolygon());
        }
        return surfaceTypeList;
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
            vertex.setNormal(calculatePolygonNormal());
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
    private double[] calculatePolygonNormal() {
        //The normal of a horizontal polygon surface always is in the negative z direction.
        final double[] normal = new double[COORD_COUNT];
        normal[IND_X] = 0d;
        normal[IND_Y] = 0d;
        normal[IND_Z] = -1d; //todo: check if this normal is correct
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
                final IndexedVertex newVertex;
                //If a vertex can be shared, do so.
                if (sharedVertices.hasNext()) {
                    newVertex = sharedVertices.next();
                } //Else calculate the normal of the unknown vertex and use the vertex with it's normal.
                else {
                    vertex.setNormal(calculateQuadStripNormal(ring1.getVertices().indexOf(vertex), ring0, ring1, ring2));
                    newVertex = IndexedVertex.makeIndexedVertex(vertex);
                }
                //And store the vertex.
                indexedVertices.add(newVertex);
            }
            //Again, interleaving, it means first vertex 1, then vertex 2, so here is vertex 2.
            if (vertices2.hasNext() && (ring2 != null)) {
                final Vertex vertex = vertices2.next();
                //The second ring is not calculated (yet), and can therefore not (yet) be reused.
                //So calculate the normal.
                vertex.setNormal(calculateQuadStripNormal(ring2.getVertices().indexOf(vertex), ring1, ring2, ring3));
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
    private double[] calculateQuadStripNormal(int index, Ring ring0, Ring ring1, Ring ring2) {
        final double[] vertexNormal;
        //If the normal is already calculated, it does not need to be calculated again.
        if (ring1.isVertexNormalCalculated(index)) {
            vertexNormal = ring1.getVertices().get(index).getNormal();
        } else //If this is the only ring (it has no neighboring rings) then no surface is defined, and also no normal.
        {
            if ((ring2 == null) && (ring0 == null)) {
                vertexNormal = new double[COORD_COUNT];
                vertexNormal[IND_X] = 0d;
                vertexNormal[IND_Y] = 0d;
                vertexNormal[IND_Z] = 0d;
            } else //If this is the last ring, then the normal is equal for both the first and second ring of the surface.
            {
                if (ring2 == null) {
                    vertexNormal = ring0.getVertices().get(index).getNormal();
                } //Else the normal will be calculated.
                else {
                    vertexNormal = calculateNormal(index, ring1.getRadius() - ring2.getRadius(), ring1.getHeight() - ring2.getHeight(), ring1.getVertices().size());
                    final Vertex vertex = ring1.getVertices().get(index);
                    vertex.setNormal(vertexNormal);
                    //And updated in the ring.
                    ring1.setVertex(vertex, index);
                    ring1.setVertexNormalCalculated(index);
                }
            }
        }
        if ((!ring1.isSharp()) && (ring0 != null)) {
            //If the ring represents a smooth edge, the normal will be averaged with the normal below it.
            for (int i = 0; i < COORD_COUNT; i++) {
                //By averaging the array element-wise.
                vertexNormal[i] += ring0.getVertices().get(index).getNormal()[i];
                vertexNormal[i] *= 0.5d;
            }
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
    private Ring makeRing(double radius, double height, int sliceCount, boolean isSharp, boolean isClosed) {
        final List<Vertex> vertices = new ArrayList<>();
        //Iterate over all slices in the new Ring and calculate their position.
        for (int i = 0; i < sliceCount + 1; i++) {
            vertices.add(new Vertex(calculatePosition(i, radius, height, sliceCount)));
        }
        //And then return the new Ring with all vertices.
        return new Ring(vertices, isSharp, isClosed, radius, height, sliceCount);
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
    private static double[] calculatePosition(int angleIndex, double radius, double height, int sliceCount) {
        //Calculate the normal of the surface in circular coordinates.
        final double sliceAngle = toRadians((double) angleIndex * 360d / (double) sliceCount);
        //And store the results as x, y, z components in an array.
        final double[] position = new double[COORD_COUNT];
        position[IND_X] = radius * cos(sliceAngle);
        position[IND_Y] = radius * sin(sliceAngle);
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
    private static double[] calculateNormal(int angleIndex, double deltaRadius, double deltaHeight, int sliceCount) {
        //Calculate the normal of the surface in spherical coordinates.
        final double sliceAngle = toRadians((double) angleIndex * 360d / (double) sliceCount);
        final double stackAngle = (PI / 2d) - atan(deltaRadius / deltaHeight);
        //And store the results as x, y, z components in an array.
        final double[] normal = new double[COORD_COUNT];
        normal[IND_X] = cos(sliceAngle) * sin(stackAngle);
        normal[IND_Y] = sin(sliceAngle) * sin(stackAngle);
        normal[IND_Z] = cos(stackAngle);
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
        private final double radius;
        private final double height;
        private final double sliceCount;

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
        private Ring(List<Vertex> vertices, boolean sharp, boolean closed, double radius, double height, double sliceCount) {
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
        private double getRadius() {
            return radius;
        }

        /**
         * Get the original height that specified this Ring.
         *
         * @return The original height that specified this Ring.
         */
        private double getHeight() {
            return height;
        }

        /**
         * Get the original number of slices that specified this Ring.
         *
         * @return The original number of slices that specified this Ring.
         */
        public double getSliceCount() {
            return sliceCount;
        }

    }
}
