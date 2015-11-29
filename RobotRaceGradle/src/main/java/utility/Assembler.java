package utility;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arjan Boschman
 * @author Robke Geenen
 */
public class Assembler {

    public static final int NUMCOORD = 3;

    private final List<Ring> rings = new ArrayList<>();

    public void addConicalFrustum(int sliceCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh) {
        addPartialTorus(sliceCount, 1, radiusLow, radiusHigh, heightLow, heightHigh, closeLow, closeHigh);
    }

    public void addPartialTorus(int sliceCount, int stackCount, double radiusLow, double radiusHigh, double heightLow, double heightHigh, boolean closeLow, boolean closeHigh) {
        if (rings.isEmpty()) {//TODO: also if sliceCount does not cioncide
            rings.add(makeRing(radiusLow, heightLow, sliceCount, true, closeLow));
        }
        for (int i = 1; i < stackCount; i++) {
            final double radiusInt = radiusHigh + ((radiusLow - radiusHigh) * Math.cos(Math.toRadians((double) i * 90d / (double) stackCount)));
            final double heightInt = heightLow + ((heightHigh - heightLow) * Math.sin(Math.toRadians((double) i * 90d / (double) stackCount)));
            rings.add(makeRing(radiusInt, heightInt, sliceCount, false, false));
        }
        rings.add(makeRing(radiusHigh, heightHigh, sliceCount, true, closeHigh));
    }

    public SurfaceCompilation makeSurfaceCompilation() {
        int vertexSize = rings.stream().mapToInt((ring) -> ring.size()).sum();
        int normalSize = rings.stream().mapToInt((ring) -> ring.getNormalCoordsCount()).sum();
        int vertexDataPtr = 0;
        int normalDataPtr = 0;
        final double[] vertexData = new double[vertexSize];
        final double[] normalData = new double[normalSize];
        for (int i = 0; i < rings.size(); i++) {
            final Ring thisRing = rings.get(i);
            final Ring nextRing = (i + 1 == rings.size()) ? null : rings.get(i + 1);
            int counted = 1;
            counted += thisRing.isClosed() ? 1 : 0;
            counted += (thisRing.isSharp() && i != 0) ? 1 : 0;
            final int nextNormalDataPtr = normalDataPtr + counted * thisRing.size();
            calcNormals(normalData, thisRing, nextRing, i == 0, normalDataPtr, nextNormalDataPtr);
//            System.arraycopy(normals, 0, normalData, normalDataPtr, thisRing.getNormalCoordsCount());
            normalDataPtr = nextNormalDataPtr;
            System.arraycopy(thisRing.getVerticesCoords(), 0, vertexData, vertexDataPtr, thisRing.size());
            vertexDataPtr += thisRing.size();
        }

        final SurfaceCompilation surfaceCompilation = new SurfaceCompilation(vertexData, normalData);
        vertexDataPtr = 0;
        normalDataPtr = 0;
        for (int i = 0; i < rings.size(); i++) {
            if (rings.get(i).isClosed()) {
                final Surface surface = makeSurfacePolygon(rings.get(i), vertexDataPtr / NUMCOORD, normalDataPtr / NUMCOORD);
                surfaceCompilation.addSurface(surface);
            }
            if (i != 0) {
                final int prevVertexPtr = vertexDataPtr - rings.get(i - 1).size();
                final int prevNormalPtr = normalDataPtr - rings.get(i - 1).getNormalCoordsCount();
                final Surface surface = makeSurfaceQuadStrip(rings.get(i - 1), rings.get(i),
                        prevVertexPtr / NUMCOORD, vertexDataPtr / NUMCOORD, prevNormalPtr / NUMCOORD, normalDataPtr / NUMCOORD);
                surfaceCompilation.addSurface(surface);
            }
            vertexDataPtr += rings.get(i).size();
            normalDataPtr += rings.get(i).getNormalCoordsCount();
        }
        return surfaceCompilation;
    }

    private void calcNormals(double[] normals, Ring ring, Ring nextRing, boolean isFirstRing, int thisRingNormalPtr, int nextRingNormalPtr) {
        final int closedOffset = ring.isClosed() ? ring.getVerticesCoords().length : 0;
        final int sharpOffset = (ring.isSharp() && !isFirstRing) ? ring.getVerticesCoords().length : 0;

        for (int i = 0; i < ring.size() / NUMCOORD; i++) {
            if (ring.isClosed()) {
                normals[thisRingNormalPtr + i * NUMCOORD + 0] = 0d;
                normals[thisRingNormalPtr + i * NUMCOORD + 1] = 0d;
                normals[thisRingNormalPtr + i * NUMCOORD + 2] = -1d;//todo: check
            }
            final double[] faceNormal;
            if (nextRing == null) {
                continue;
            } else {
                faceNormal = calcNorm(i, ring.getRadius() - nextRing.getRadius(),
                        ring.getHeight() - nextRing.getHeight(), ring.size() / NUMCOORD);
                System.arraycopy(faceNormal, 0, normals, nextRingNormalPtr + (i * NUMCOORD), NUMCOORD);
            }
            if (ring.isSharp()) {
                System.arraycopy(faceNormal, 0, normals, thisRingNormalPtr + closedOffset + sharpOffset + (i * NUMCOORD), NUMCOORD);
            } else {
                final double[] avgNormal = {
                    (faceNormal[0] + normals[thisRingNormalPtr + closedOffset + (i * NUMCOORD) + 0]) / 2d,
                    (faceNormal[1] + normals[thisRingNormalPtr + closedOffset + (i * NUMCOORD) + 1]) / 2d,
                    (faceNormal[2] + normals[thisRingNormalPtr + closedOffset + (i * NUMCOORD) + 2]) / 2d};
                System.arraycopy(avgNormal, 0, normals, thisRingNormalPtr + closedOffset + (i * NUMCOORD), NUMCOORD);
            }
        }
    }

    private Surface makeSurfacePolygon(Ring ring, int vertexOffset, int normalOffset) {
        final int[] vertexIndices = new int[ring.size() / NUMCOORD];
        final int[] normalIndices = new int[ring.size() / NUMCOORD];
        for (int i = 0; i < vertexIndices.length; i++) {
            vertexIndices[i] = vertexOffset + i;
            normalIndices[i] = normalOffset + i;
        }
        return new Surface(vertexIndices, normalIndices, true);
    }

    private Surface makeSurfaceQuadStrip(Ring ring1, Ring ring2, int vertexOffset1, int vertexOffset2, int normalOffset1, int normalOffset2) {
        final int[] vertexIndices = new int[(ring1.size() + ring2.size()) / NUMCOORD];
        final int[] normalIndices = new int[(ring1.size() + ring2.size()) / NUMCOORD];
        for (int i = 0; i < (vertexIndices.length / 2); i++) {
            vertexIndices[(i * 2) + 0] = vertexOffset1 + i;
            vertexIndices[(i * 2) + 1] = vertexOffset2 + i;
            normalIndices[(i * 2) + 0] = normalOffset1 + i;
            normalIndices[(i * 2) + 1] = normalOffset2 + i;
        }
        return new Surface(vertexIndices, normalIndices, false);
    }

    private Ring makeRing(double radius, double height, int sliceCount, boolean isSharp, boolean isClosed) {
        final double[] vrtx = new double[(sliceCount + 1) * NUMCOORD];
        for (int i = 0; i < sliceCount + 1; i++) {
            System.arraycopy(calcVrtx(i, radius, height, sliceCount), 0, vrtx, i * NUMCOORD, NUMCOORD);
        }
        return new Ring(vrtx, isSharp, isClosed, radius, height);
    }

    public static double[] calcVrtx(int angleIndex, double radius, double height, int sliceCount) {
        final double sliceAngle = Math.toRadians((double) angleIndex * 360d / (double) sliceCount);
        final double[] vrtx = {
            radius * Math.cos(sliceAngle),
            radius * Math.sin(sliceAngle),
            height
        };
        return vrtx;
    }

    public static double[] calcNorm(int angleIndex, double deltaRadius, double deltaHeight, int sliceCount) {
        final double sliceAngle = Math.toRadians((double) angleIndex * 360d / (double) sliceCount);
        final double stackAngle = (Math.PI / 2d) - Math.atan(deltaRadius / deltaHeight);
        final double[] norm = {
            Math.cos(sliceAngle) * Math.sin(stackAngle),
            Math.sin(sliceAngle) * Math.sin(stackAngle),
            Math.cos(stackAngle)
        };
        return norm;
    }

    private static class Ring {

        private final double[] verticesCoords;
        private final boolean sharp;
        private final boolean closed;
        private final double radius;
        private final double height;

        public Ring(double[] verticesCoords, boolean sharp, boolean closed, double radius, double height) {
            this.verticesCoords = verticesCoords;
            this.sharp = sharp;
            this.closed = closed;
            this.radius = radius;
            this.height = height;
        }

        private double[] getVerticesCoords() {
            return verticesCoords;
        }

        private int size() {
            return verticesCoords.length;
        }

        private boolean isSharp() {
            return sharp;
        }

        private boolean isClosed() {
            return closed;
        }

        private int getNormalCoordsCount() {
            int counted = 1;
            counted += isClosed() ? 1 : 0;
            counted += isSharp() ? 1 : 0;
            return counted * verticesCoords.length;
        }

        public double getRadius() {
            return radius;
        }

        public double getHeight() {
            return height;
        }

    }

}
