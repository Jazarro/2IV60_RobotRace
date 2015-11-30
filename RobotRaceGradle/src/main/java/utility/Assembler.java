package utility;

import com.jogamp.common.nio.*;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.nio.*;
import java.util.*;
import static utility.Vertex.COORD_COUNT;
import static utility.Vertex.IND_X;
import static utility.Vertex.IND_Y;
import static utility.Vertex.IND_Z;

/**

 @author Arjan Boschman
 @author Robke Geenen
 */
public final class Assembler{

    //public static final int NUMCOORD = 3;
    private final List<Ring> rings = new ArrayList<>();
    private SurfaceCompilation surfaceCompilation;

    public void addConicalFrustum(int sliceCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh){
        addPartialTorus(sliceCount, 1, radiusLow, radiusHigh, heightLow, heightHigh, closeLow, closeHigh);
    }

    public void addPartialTorus(int sliceCount, int stackCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh){
        if(rings.isEmpty()){//todo: also check if sliceCount does not coincide
            rings.add(makeRing(radiusLow, heightLow, sliceCount, true, closeLow));
        }
        for(int i = 1; i < stackCount; i++){//todo: check radii
            final double radiusInterpolated;
            final double heightInterpolated;
            //if(radiusHigh <= radiusLow){
                radiusInterpolated = radiusHigh + ((radiusLow - radiusHigh) * cos(toRadians((double)i * 90d / (double)stackCount)));
                heightInterpolated = heightLow + ((heightHigh - heightLow) * sin(toRadians((double)i * 90d / (double)stackCount)));
            /*}
            else{
                radiusInterpolated = radiusHigh + ((radiusLow - radiusHigh) * sin(toRadians(((double)i * 90d / (double)stackCount) - 90d)));
                heightInterpolated = heightLow + ((heightHigh - heightLow) * cos(toRadians(((double)i * 90d / (double)stackCount) + 90d)));
            }*/
            rings.add(makeRing(radiusInterpolated, heightInterpolated, sliceCount, false, false));
        }
        rings.add(makeRing(radiusHigh, heightHigh, sliceCount, true, closeHigh));
    }

    public void compileSurfaceCompilation(){
        final SurfaceCompilation newSurfaceCompilation = new SurfaceCompilation();
        List<IndexedVertex> knownVertices = new ArrayList<>();
        for(int i = 0; i < rings.size(); i++){
            if(rings.get(i).isClosed()){
                final Surface surface = makeSurfacePolygon(rings.get(i));
                newSurfaceCompilation.addSurface(surface);
            }
            if(i != 0){
                final Surface surface = makeSurfaceQuadStrip((i < 2) ? null : rings.get(i - 2), rings.get(i - 1), rings.get(i), ((i + 1) >= rings.size()) ? null : rings.get(i + 1), (i == 1) ? null : knownVertices);
                knownVertices = newSurfaceCompilation.addSurface(surface);
            }
        }
        this.surfaceCompilation = newSurfaceCompilation;
    }

    public DoubleBuffer getDataBuffer(){
        final List<Double> dataList = new ArrayList<>();
        for(IndexedVertex vertex : surfaceCompilation.getVertices()){
            dataList.add(vertex.getVertex().getPosition()[IND_X]);
            dataList.add(vertex.getVertex().getPosition()[IND_Y]);
            dataList.add(vertex.getVertex().getPosition()[IND_Z]);
            dataList.add(vertex.getVertex().getNormal()[IND_X]);
            dataList.add(vertex.getVertex().getNormal()[IND_Y]);
            dataList.add(vertex.getVertex().getNormal()[IND_Z]);
        }
        final double[] dataArray = new double[dataList.size()];
        for(int i = 0; i < dataArray.length; i++){
            dataArray[i] = dataList.get(i);
        }
        return Buffers.newDirectDoubleBuffer(dataArray);
    }

    public List<IntBuffer> getIndicesBuffers(){
        final List<IntBuffer> indicesBufferList = new ArrayList<>();
        for(Surface surface : surfaceCompilation.getSurfaces()){
            final List<Integer> indicesList = surface.getIndices();
            final int[] indicesArray = new int[indicesList.size()];
            for(int i = 0; i < indicesArray.length; i++){
                indicesArray[i] = indicesList.get(i);
            }
            indicesBufferList.add(Buffers.newDirectIntBuffer(indicesArray));
        }
        return indicesBufferList;
    }

    public List<Boolean> getSurfaceTypeList(){
        final List<Boolean> surfaceTypeList = new ArrayList<>();
        for(Surface surface : surfaceCompilation.getSurfaces()){
            surfaceTypeList.add(surface.isPolygon());
        }
        return surfaceTypeList;
    }

    private Surface makeSurfacePolygon(Ring ring){
        final List<Vertex> vertices = ring.getVertices();
        final List<IndexedVertex> indexedVertices = new ArrayList<>();
        for(Vertex vertex : vertices){
            vertex.setNormal(calculatePolygonNormal());
            indexedVertices.add(IndexedVertex.makeIndexedVertex(vertex));
        }
        return new Surface(indexedVertices, true);
    }

    private double[] calculatePolygonNormal(){
        final double[] normal = new double[COORD_COUNT];
        normal[IND_X] = 0d;
        normal[IND_Y] = 0d;
        normal[IND_Z] = -1d; //todo: check
        return normal;
    }

    private Surface makeSurfaceQuadStrip(Ring ring0, Ring ring1, Ring ring2, Ring ring3, List<IndexedVertex> knownVertices){
        final Iterator<Vertex> vertices1 = (ring1 == null) ? Collections.emptyIterator() : ring1.getVertices().iterator();
        final Iterator<Vertex> vertices2 = (ring2 == null) ? Collections.emptyIterator() : ring2.getVertices().iterator();
        final Iterator<IndexedVertex> sharedVertices = (knownVertices == null) ? Collections.emptyIterator() : knownVertices.iterator();
        final List<IndexedVertex> indexedVertices = new ArrayList<>();
        while(vertices1.hasNext() || vertices2.hasNext()){
            if(vertices1.hasNext()){
                final Vertex vertex = vertices1.next();
                final IndexedVertex newVertex;
                if(sharedVertices.hasNext()){
                    newVertex = sharedVertices.next();
                }
                else{
                    vertex.setNormal(calculateQuadStripNormal(ring1.getVertices().indexOf(vertex), ring0, ring1, ring2));
                    newVertex = IndexedVertex.makeIndexedVertex(vertex);
                }
                indexedVertices.add(newVertex);
            }
            if(vertices2.hasNext()){
                final Vertex vertex = vertices2.next();
                vertex.setNormal(calculateQuadStripNormal(ring2.getVertices().indexOf(vertex), ring1, ring2, ring3));
                indexedVertices.add(IndexedVertex.makeIndexedVertex(vertex));
            }
        }
        return new Surface(indexedVertices, false);
    }

    private double[] calculateQuadStripNormal(int index, Ring ring0, Ring ring1, Ring ring2){
        final double[] vertexNormal;
        if(ring1.isNormalCalculated() && false){
            vertexNormal = ring1.getVertices().get(index).getNormal();
        }
        else{
            if((ring2 == null) && (ring0 == null)){
                vertexNormal = new double[COORD_COUNT];
                vertexNormal[IND_X] = 0d;
                vertexNormal[IND_Y] = 0d;
                vertexNormal[IND_Z] = 0d;
            }
            else{
                if(ring2 == null){
                    vertexNormal = ring0.getVertices().get(index).getNormal();
                }
                else{
                    vertexNormal = calculateNormal(index, ring1.getRadius() - ring2.getRadius(), ring1.getHeight() - ring2.getHeight(), ring1.getVertices().size());
                }
            }
            final Vertex vertex = ring1.getVertices().get(index);
            vertex.setNormal(vertexNormal);
            ring1.setVertex(vertex, index);
            ring1.setNormalCalculated(true);
        }
        if((!ring1.isSharp()) && (ring0 != null)){
            for(int i = 0; i < COORD_COUNT; i++){
                vertexNormal[i] += ring0.getVertices().get(index).getNormal()[i];
                vertexNormal[i] *= 0.5d;
            }
        }
        return vertexNormal;
    }

    private Ring makeRing(double radius, double height, int sliceCount, boolean isSharp, boolean isClosed){
        final List<Vertex> vertices = new ArrayList<>();
        for(int i = 0; i < sliceCount + 1; i++){
            vertices.add(new Vertex(calculatePosition(i, radius, height, sliceCount)));
        }
        return new Ring(vertices, isSharp, isClosed, radius, height);
    }

    private static double[] calculatePosition(int angleIndex, double radius, double height, int sliceCount){
        final double sliceAngle = toRadians((double)angleIndex * 360d / (double)sliceCount);
        final double[] position = new double[COORD_COUNT];
        position[IND_X] = radius * cos(sliceAngle);
        position[IND_Y] = radius * sin(sliceAngle);
        position[IND_Z] = height;
        return position;
    }

    private static double[] calculateNormal(int angleIndex, double deltaRadius, double deltaHeight, int sliceCount){
        final double sliceAngle = toRadians((double)angleIndex * 360d / (double)sliceCount);
        final double stackAngle = (PI / 2d) - atan(deltaRadius / deltaHeight);
        final double[] normal = new double[COORD_COUNT];
        normal[IND_X] = cos(sliceAngle) * sin(stackAngle);
        normal[IND_Y] = sin(sliceAngle) * sin(stackAngle);
        normal[IND_Z] = cos(stackAngle);
        return normal;
    }

    private static final class Ring{

        private final List<Vertex> vertices;
        private final boolean sharp;
        private final boolean closed;
        private boolean normalCalculated;
        private final double radius;
        private final double height;

        private Ring(List<Vertex> vertices, boolean sharp, boolean closed, double radius, double height){
            this.vertices = vertices;
            this.sharp = sharp;
            this.closed = closed;
            this.normalCalculated = false;
            this.radius = radius;
            this.height = height;
        }

        private List<Vertex> getVertices(){
            return vertices;
        }

        private void setVertex(Vertex vertex, int index){
            this.vertices.set(index, vertex);
        }

        private boolean isSharp(){
            return sharp;
        }

        private boolean isClosed(){
            return closed;
        }

        private boolean isNormalCalculated(){
            return normalCalculated;
        }

        private void setNormalCalculated(boolean value){
            this.normalCalculated = value;
        }

        private double getRadius(){
            return radius;
        }

        private double getHeight(){
            return height;
        }

    }
}
