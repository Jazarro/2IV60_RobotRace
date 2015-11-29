package utility;

import java.util.*;

public class SurfaceCompilation{

    private final double[] vertexNormal;
    private final List<Surface> surfaces = new ArrayList<>();

    public SurfaceCompilation(double[] vertexNormal){
        this.vertexNormal = vertexNormal;
    }

    public void addSurface(Surface surface){
        surfaces.add(surface);
    }

    public double[] getVertexNormal(){
        return vertexNormal;
    }

    public List<Surface> getSurfaces(){
        return surfaces;
    }

    public int size(){
        return surfaces.size();
    }
}
