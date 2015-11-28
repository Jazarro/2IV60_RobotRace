package utility;

import java.util.ArrayList;
import java.util.List;
import robotrace.bender.Bender;

/**

 @author Arjan Boschman
 @author Robke Geenen
 */
public class Assembler{

    public static final int NUMCOORD = 3;

    private final List<Ring> rings = new ArrayList<>();

    public void addConicalFrustum(int sliceCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh){
        addPartialTorus(sliceCount, 1, radiusLow, radiusHigh, heightLow, heightHigh, closeLow, closeHigh);
    }

    public void addPartialTorus(int sliceCount, int stackCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh){
        if(rings.isEmpty()){//TODO: also if sliceCount does not cioncide
            rings.add(makeRing(radiusLow, heightLow, sliceCount, true, closeLow));
        }
        for(int i = 1; i < stackCount; i++){
            final double radiusInt = radiusHigh + (radiusLow - radiusHigh) * Math.cos(Math.toRadians((double)i * 90d / (double)stackCount));
            final double heightInt = heightLow + (heightHigh - heightLow) * Math.sin(Math.toRadians((double)i * 90d / (double)stackCount));
            rings.add(makeRing(radiusInt, heightInt, sliceCount, false, false));
        }
        rings.add(makeRing(radiusHigh, heightHigh, sliceCount, true, closeHigh));
    }

    public SurfaceCompilation makeSurfaceCompilation(){
        int dataCount = rings.stream().mapToInt((ring)->ring.size()).sum();
        int dataPtr = 0;
        final double[] data = new double[dataCount];
        for(Ring ring : rings){
            System.arraycopy(ring.getVertices(), 0, data, dataPtr, ring.size());
            dataPtr += ring.size();
        }
        final SurfaceCompilation surfaceCompilation = new SurfaceCompilation(data);
        dataPtr = 0;
        for(int i = 0; i < rings.size(); i++){
            if(rings.get(i).isClosed()){
                surfaceCompilation.addSurface(new Surface(calcIndicesPolygon(rings.get(i), dataPtr / 3), true));
            }
            if(i != 0){
                surfaceCompilation.addSurface(new Surface(calcIndicesQuadStrip(rings.get(i - 1), rings.get(i), (dataPtr - rings.get(i-1).size()) / 3, dataPtr / 3), false));
            }
            dataPtr += rings.get(i).size();
        }
        return surfaceCompilation;
    }

    private int[] calcIndicesPolygon(Ring ring, int offset){
        final int[] indices = new int[ring.size()];
        for(int i = 0; i < indices.length; i++){
            indices[i] = offset + i;
        }
        return indices;
    }

    private int[] calcIndicesQuadStrip(Ring ring1, Ring ring2, int offset1, int offset2){
        final int[] indices = new int[ring1.size() + ring2.size()];
        for(int i = 0; i < (indices.length / 2); i++){
            indices[i * 2 + 0] = offset1 + i;
            indices[i * 2 + 1] = offset2 + i;
        }
        return indices;
    }

    private Ring makeRing(double radius, double height, int sliceCount, boolean isSharp, boolean isClosed){
        final double[] vrtx = new double[(sliceCount + 1) * NUMCOORD];
        for(int i = 0; i < sliceCount + 1; i++){
            System.arraycopy(calcVrtx(i, radius, height, sliceCount), 0, vrtx, 0, NUMCOORD);
        }
        return new Ring(vrtx, isSharp, isClosed);
    }

    /*public static void makeConicalFrustum(double radiusLow, double radiusHigh, double heightLow, double heightHigh, int sliceCount){
        makePartialTorus(radiusLow, radiusHigh, heightLow, heightHigh, sliceCount, 1);
    }

    public static void makePartialTorus(double radiusLow, double radiusHigh, double heightLow, double heightHigh, int sliceCount, int stackCount){
        final int elementCount = (sliceCount + 1) * (stackCount + 1);
        final double[] arrVrtxNorm = new double[elementCount * NUMCOORD * 2];
        final int[] arrIndx = new int[elementCount];
        for(int j = 0; j < stackCount + 1; j++){
            for(int i = 0; i < sliceCount + 1; i++){

                //final double[] lowerCoords = calcVrtx(i, radiusLow, heightLow, sliceCount);
                final double[] coords = calcVrtx(i,
                                                 radiusHigh + (radiusLow - radiusHigh) * Math.cos(Math.toRadians(j * 90d / stackCount)),
                                                 heightLow + (heightHigh - heightLow) * Math.sin(Math.toRadians(j * 90d / stackCount)),
                                                 sliceCount);
                final double[] norms = calcNorm(i, (radiusHigh - radiusLow), (heightHigh - heightLow), sliceCount);
                final int indexInArray = ((j * (sliceCount + 1)) + i) * 2 * NUMCOORD;
                //System.arraycopy(lowerCoords, 0, arrVrtxNorm, indexInArray + (0 * NUMCOORD), NUMCOORD);
                System.arraycopy(coords, 0, arrVrtxNorm, indexInArray + (0 * NUMCOORD), NUMCOORD);
                System.arraycopy(norms, 0, arrVrtxNorm, indexInArray + (1 * NUMCOORD), NUMCOORD);
                arrIndx[indexInArray] =;
            }
        }
    }*/
    public static double[] calcVrtx(int angleIndex, double radius, double height, int sliceCount){
        final double sliceAngle = Math.toRadians((double)angleIndex * 360d / (double)sliceCount);
        final double[] vrtx = {
            radius * Math.cos(sliceAngle),
            radius * Math.sin(sliceAngle),
            height
        };
        return vrtx;
    }

    public static double[] calcNorm(int angleIndex, double deltaRadius, double deltaHeight, int sliceCount){
        final double sliceAngle = Math.toRadians((double)angleIndex * 360d / (double)sliceCount);
        final double stackAngle = (Math.PI / 2d) - Math.atan(deltaRadius / deltaHeight);
        final double[] norm = {
            Math.cos(sliceAngle) * Math.sin(stackAngle),
            Math.sin(sliceAngle) * Math.sin(stackAngle),
            Math.cos(stackAngle)
        };
        return norm;
    }

    public static class SurfaceCompilation{

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

    public static class Surface{

        private final int[] indices;
        private final boolean polygon;

        public Surface(int[] indices, boolean polygon){
            this.indices = indices;
            this.polygon = polygon;
        }

        public int[] getIndices(){
            return indices;
        }

        public boolean isPolygon(){
            return polygon;
        }
    }

    private static class Ring{

        private final double[] vertices;
        private final boolean sharp;
        private final boolean closed;

        private Ring(double[] vertices, boolean isSharp, boolean isClosed){
            this.vertices = vertices;
            this.sharp = isSharp;
            this.closed = isClosed;
        }

        private double[] getVertices(){
            return vertices;
        }

        private int size(){
            return vertices.length;
        }

        private boolean isSharp(){
            return sharp;
        }

        private boolean isClosed(){
            return closed;
        }

    }

}
