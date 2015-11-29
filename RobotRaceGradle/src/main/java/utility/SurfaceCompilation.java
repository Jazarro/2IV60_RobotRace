package utility;

import java.util.*;

public class SurfaceCompilation{

    private final double[] vertices;
    private final double[] normals;
    private final List<Surface> surfaces = new ArrayList<>();

    public SurfaceCompilation(double[] vertices, double[] normals){
        this.vertices = vertices;
        this.normals = normals;
    }

    public void addSurface(Surface surface){
        surfaces.add(surface);
    }

    public double[] getVertices(){
        return vertices;
    }

    public double[] getNormals(){
        return normals;
    }

    public List<Surface> getSurfaces(){
        return surfaces;
    }

    public int size(){
        return surfaces.size();
    }
}
