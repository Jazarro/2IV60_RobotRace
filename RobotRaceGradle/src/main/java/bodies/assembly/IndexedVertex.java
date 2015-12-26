/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies.assembly;

/**
 *
 * @author Robke Geenen
 */
final class IndexedVertex {

    /**
     * The Vertex this IndexedVertex is based on.
     */
    private Vertex vertex;
    /**
     * If this IndexedVertex is shared between objects.
     */
    private boolean shared;
    /**
     * The index of this IndexedVertex in the vertex array.
     */
    private int index;

    /**
     * Constructor specifying the Vertex to be used to instantiate this
     * IndexedVertex.
     *
     * @param vertex The Vertex used to instantiate this IndexedVertex.
     * @param shared If this IndexedVertex is shared between objects.
     * @param index  The index of this IndexedVertex in the vertex array.
     */
    private IndexedVertex(Vertex vertex, boolean shared, int index) {
        this.vertex = vertex;
        this.shared = shared;
        this.index = index;
    }

    /**
     * Create a new IndexedVertex from a Vertex;
     *
     * @param vertex The Vertex used to instantiate the new IndexedVertex.
     *
     * @return The new IndexedVertex.
     */
    public static IndexedVertex makeIndexedVertex(Vertex vertex) {
        return makeIndexedVertex(vertex, false, 0);
    }

    /**
     * Create a new IndexedVertex from a Vertex;
     *
     * @param vertex The Vertex used to instantiate the new IndexedVertex.
     * @param shared If this IndexedVertex is shared between objects.
     * @param index  The index of this IndexedVertex in the vertex array.
     *
     * @return The new IndexedVertex.
     */
    public static IndexedVertex makeIndexedVertex(Vertex vertex, boolean shared, int index) {
        final Vertex clone = new Vertex(vertex.getPosition(), vertex.getNormal());
        return new IndexedVertex(clone, shared, index);
    }

    /**
     * Get the Vertex this IndexedVertex is based on.
     *
     * @return The Vertex this IndexedVertex is based on.
     */
    public Vertex getVertex() {
        return vertex;
    }

    /**
     * Set the new Vertex this IndexedVertex is based on.
     *
     * @param vertex The new Vertex this IndexedVertex is based on.
     */
    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    /**
     * Set if this IndexedVertex is shared between objects.
     *
     * @param shared If this IndexedVertex is shared between objects.
     */
    public final void setShared(boolean shared) {
        this.shared = shared;
    }

    /**
     * Get if this IndexedVertex is shared between objects.
     *
     * @return If this IndexedVertex is shared between objects.
     */
    public final boolean isShared() {
        return shared;
    }

    /**
     * Get the index of this IndexedVertex in the vertex array.
     *
     * @return The index of this IndexedVertex in the vertex array.
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Set the index of this IndexedVertex in the vertex array.
     *
     * @param index The index of this IndexedVertex in the vertex array.
     */
    public final void setIndex(int index) {
        this.index = index;
    }
}
