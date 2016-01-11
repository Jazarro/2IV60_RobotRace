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
class IndexedVertex {

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
    protected IndexedVertex(Vertex vertex, boolean shared, int index) {
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
    protected static IndexedVertex makeIndexedVertex(Vertex vertex) {
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
    protected static IndexedVertex makeIndexedVertex(Vertex vertex, boolean shared, int index) {
        return new IndexedVertex(vertex, shared, index);
    }

    /**
     * Get the Vertex this IndexedVertex is based on.
     *
     * @return The Vertex this IndexedVertex is based on.
     */
    protected Vertex getVertex() {
        return vertex;
    }

    /**
     * Set the new Vertex this IndexedVertex is based on.
     *
     * @param vertex The new Vertex this IndexedVertex is based on.
     */
    protected void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    /**
     * Set if this IndexedVertex is shared between objects.
     *
     * @param shared If this IndexedVertex is shared between objects.
     */
    protected void setShared(boolean shared) {
        this.shared = shared;
    }

    /**
     * Get if this IndexedVertex is shared between objects.
     *
     * @return If this IndexedVertex is shared between objects.
     */
    protected boolean isShared() {
        return shared;
    }

    /**
     * Get the index of this IndexedVertex in the vertex array.
     *
     * @return The index of this IndexedVertex in the vertex array.
     */
    protected int getIndex() {
        return index;
    }

    /**
     * Set the index of this IndexedVertex in the vertex array.
     *
     * @param index The index of this IndexedVertex in the vertex array.
     */
    protected void setIndex(int index) {
        this.index = index;
    }

}
