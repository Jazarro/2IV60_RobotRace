package utility;

import java.util.*;

final class Surface{

    private final List<IndexedVertex> vertices;
    private final boolean polygon;

    public Surface(List<IndexedVertex> vertices, boolean polygon){
        this.vertices = vertices;
        this.polygon = polygon;
    }

    public List<IndexedVertex> getVertices(){
        return vertices;
    }

    public void setVertex(IndexedVertex vertex, int index){
        this.vertices.set(index, vertex);
    }

    public boolean isPolygon(){
        return polygon;
    }

    public List<Integer> getIndices(){
        final List<Integer> indices = new ArrayList<>();
        for(IndexedVertex vertex : vertices){
            indices.add(vertex.getIndex());
        }
        return indices;
    }
}
