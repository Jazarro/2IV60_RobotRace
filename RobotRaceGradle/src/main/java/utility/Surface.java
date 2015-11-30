package utility;

import java.util.*;

/**

 @author Robke Geenen
 */
final class Surface{

    /**
     The IndexedVertex's defining this Surface.
     */
    private final List<IndexedVertex> vertices;
    /**
     If this Surface is a polygon or a quad strip.
     */
    private final boolean polygon;

    /**
     Constructor specifying the IndexedVertex's defining this Surface and if
     this Surface is a polygon or a quad strip.

     @param vertices The IndexedVertex's defining this Surface.
     @param polygon  If this Surface is a polygon or a quad strip.
     */
    public Surface(List<IndexedVertex> vertices, boolean polygon){
        this.vertices = vertices;
        this.polygon = polygon;
    }

    /**
     Get the IndexedVertex's defining this Surface.

     @return The IndexedVertex's defining this Surface.
     */
    public List<IndexedVertex> getVertices(){
        return vertices;
    }

    /**
     Set a new IndexedVertex at position index in the list of IndexedVertex's
     defining this Surface.

     @param vertex The new IndexedVertex.
     @param index  The position in the list of IndexedVertex's defining this
                   Surface.
     */
    public void setVertex(IndexedVertex vertex, int index){
        this.vertices.set(index, vertex);
    }

    /**
     Get if this Surface is a polygon or a quad strip.

     @return If this Surface is a polygon or a quad strip.
     */
    public boolean isPolygon(){
        return polygon;
    }

    /**
     Get the indices of all IndexedVertex's defining this Surface.

     Do this by iterating over all IndexedVertex's defining this Surface and
     returning the resulting list.

     @return The indices of all IndexedVertex's defining this Surface.
     */
    public List<Integer> getIndices(){
        final List<Integer> indices = new ArrayList<>();
        for(IndexedVertex vertex : vertices){
            indices.add(vertex.getIndex());
        }
        return indices;
    }
}
