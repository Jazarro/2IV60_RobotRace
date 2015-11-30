package utility;

import java.util.*;

final class SurfaceCompilation{

    private final List<IndexedVertex> vertices = new ArrayList<>();
    private final List<Surface> surfaces = new ArrayList<>();

    public List<IndexedVertex> addSurface(Surface surface){
        surfaces.add(surface);
        final List<IndexedVertex> surfaceVertices = surface.getVertices();
        final List<IndexedVertex> knownVertices = new ArrayList<>();
        for(IndexedVertex vertex : surfaceVertices){
            if(!vertex.isShared()){
                vertex.setIndex(vertices.size());
                surface.setVertex(vertex, surfaceVertices.indexOf(vertex));
                vertices.add(vertex);
            }
            else{
                knownVertices.add(vertex);
            }
        }
        return knownVertices;
    }

    public List<IndexedVertex> getVertices(){
        return vertices;
    }

    public List<Surface> getSurfaces(){
        return surfaces;
    }

}
