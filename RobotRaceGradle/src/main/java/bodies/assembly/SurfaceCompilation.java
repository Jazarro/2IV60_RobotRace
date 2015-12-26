/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies.assembly;

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
}
