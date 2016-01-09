/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies.assembly;

import static bodies.assembly.Vertex.*;
import com.jogamp.common.nio.Buffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robke Geenen
 */
final class SurfaceCompilation {

    /**
     * All IndexedVertex's of all Surfaces in this SurfaceCompilation.
     */
    private final List<IndexedVertex> vertices = new ArrayList<>();
    /**
     * All Surfaces in this SurfaceCompilation.
     */
    private final List<Surface> surfaces = new ArrayList<>();

    /**
     * Add a Surface to this SurfaceCompilation.
     *
     * Do this by adding the Surface to the list of Surfaces and by then adding
     * all IndexedVertex's of the Surface to the list of IndexedVertex's, but
     * only when that vertex is not shared.
     *
     * @param surface The Surface to be added to this SurfaceCompilation.
     *
     * @return A list of IndexedVertex's containing the IndexedVertex's that
     *         should not have been present in this SurfaceCompilation before
     *         adding the Surface.
     */
    public List<IndexedVertex> addSurface(Surface surface) {
        surfaces.add(surface);
        final List<IndexedVertex> surfaceVertices = surface.getVertices();
        final List<IndexedVertex> knownVertices = new ArrayList<>();
        for (IndexedVertex vertex : surfaceVertices) {
            if (!vertex.isShared()) {
                vertex.setIndex(vertices.size());
                surface.setVertex(vertex, surfaceVertices.indexOf(vertex));
                vertices.add(vertex);
            } else {
                knownVertices.add(vertex);
            }
        }
        return knownVertices;
    }

    /**
     * Get all IndexedVertex's of all Surfaces in this SurfaceCompilation.
     *
     * @return All IndexedVertex's of all Surfaces in this SurfaceCompilation.
     */
    public List<IndexedVertex> getVertices() {
        return vertices;
    }

    /**
     * Get all Surfaces in this SurfaceCompilation.
     *
     * @return All Surfaces in this SurfaceCompilation.
     */
    public List<Surface> getSurfaces() {
        return surfaces;
    }

    /**
     * Get a buffer with all vertices of the SurfaceCompilation.
     *
     * @return A buffer with all vertices of the SurfaceCompilation.
     */
    public FloatBuffer getDataBuffer() {
        final List<Float> dataList = new ArrayList<>();
        //Iterate over all vertices in the SurfaceCompilation.
        for (IndexedVertex vertex : vertices) {
            //And store all vertices' data in an ArrayList.
            dataList.add(vertex.getVertex().getPositionA()[IND_X]);
            dataList.add(vertex.getVertex().getPositionA()[IND_Y]);
            dataList.add(vertex.getVertex().getPositionA()[IND_Z]);
            dataList.add(vertex.getVertex().getNormalA()[IND_X]);
            dataList.add(vertex.getVertex().getNormalA()[IND_Y]);
            dataList.add(vertex.getVertex().getNormalA()[IND_Z]);
        }
        final float[] dataArray = new float[dataList.size()];
        //Convert the ArrayList into an array.
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = dataList.get(i);
        }
        //Deliver it in the form of a buffer.
        return Buffers.newDirectFloatBuffer(dataArray);
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
        for (Surface surface : surfaces) {
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
        for (Surface surface : surfaces) {
            //Add the type of the surface to the list.
            surfaceTypeList.add(surface.isPolygon());
        }
        return surfaceTypeList;
    }
}
