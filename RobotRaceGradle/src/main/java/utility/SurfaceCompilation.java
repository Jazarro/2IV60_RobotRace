package utility;

import java.util.*;

public class SurfaceCompilation{

    private final List<Double> vertices = new ArrayList<>();
    private final List<Double> normals = new ArrayList<>();
    private final List<Surface> surfaces = new ArrayList<>();

    public void addSurface(Surface surface){
        surfaces.add(surface);
        final double[] newVrtx = surface.getVertex();
        final double[] newNrml = surface.getNormal();
        final int newIndicesCount = (newVrtx.length - surface.getSharedVrtxNrml()) / Assembler.NUMCOORD;
        final int[] vrtxInd = new int[newIndicesCount];
        final int[] nrmlInd = new int[newIndicesCount];
        for(int i = newIndicesCount; i < newVrtx.length / Assembler.NUMCOORD; i++){
            vrtxInd[i] = vertices.size();
            nrmlInd[i] = normals.size();
            for(int j = 0; j < Assembler.NUMCOORD; j++){
                vertices.add(newVrtx[(i * Assembler.NUMCOORD) + j]);
                normals.add(newNrml[(i * Assembler.NUMCOORD) + j]);
            }
        }
        surface.setVertexIndices(vrtxInd);
        surface.setNormalIndices(nrmlInd);
    }

    public double[] getVertices(){
        double retVrtx[] = vertices.stream().mapToDouble(d->d).toArray();
        return retVrtx;
    }

    public double[] getNormals(){
        double retNrml[] = normals.stream().mapToDouble(d->d).toArray();
        return retNrml;
    }

    public List<Surface> getSurfaces(){
        return surfaces;
    }

    public int size(){
        return surfaces.size();
    }
}
