package utility;

final class IndexedVertex{

    private Vertex vertex;
    private boolean shared;
    private int index;

    public IndexedVertex(Vertex vertex){
        this(vertex, false, 0);
    }

    public IndexedVertex(Vertex vertex, boolean shared, int index){
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
