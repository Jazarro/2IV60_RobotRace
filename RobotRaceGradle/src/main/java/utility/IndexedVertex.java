package utility;

final class IndexedVertex{

    public static IndexedVertex makeIndexedVertex(Vertex vertex){
        return makeIndexedVertex(vertex, false, 0);
    }

    public static IndexedVertex makeIndexedVertex(Vertex vertex, boolean shared, int index){
        final Vertex clone = new Vertex(vertex.getPosition(), vertex.getNormal());
        return new IndexedVertex(clone, shared, index);
    }

    private Vertex vertex;
    private boolean shared;
    private int index;

    private IndexedVertex(Vertex vertex, boolean shared, int index){
        this.vertex = vertex;
    }

    public Vertex getVertex(){
        return vertex;
    }

    public void setVertex(Vertex vertex){
        this.vertex = vertex;
    }

    public final void setShared(boolean shared){
        this.shared = shared;
    }

    public final boolean isShared(){
        return shared;
    }

    public final int getIndex(){
        return index;
    }

    public final void setIndex(int index){
        this.index = index;
    }

}
