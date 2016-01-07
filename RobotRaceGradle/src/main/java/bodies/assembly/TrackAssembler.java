package bodies.assembly;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import robotrace.Vector;

public class TrackAssembler {

    private final SurfaceCompilation surfaceCompilation = new SurfaceCompilation();

    public void calculateTrack(List<Vertex> trackDescription, double laneWidth, int laneCount, double trackHeight, boolean closedTrack) {
        final List<TrackSlice> slices = new ArrayList<>();
        for (Vertex vertex : trackDescription) {
            Vertex previous, next;
            if (trackDescription.indexOf(vertex) == 0) {
                next = trackDescription.get(trackDescription.indexOf(vertex) + 1);
                if (closedTrack) {
                    previous = trackDescription.get(trackDescription.size() - 1);
                } else {
                    previous = new Vertex(vertex.getPositionV().subtract(next.getPositionV().subtract(vertex.getPositionV())));
                }
            } else if (trackDescription.indexOf(vertex) == (trackDescription.size() - 1)) {
                previous = trackDescription.get(trackDescription.indexOf(vertex) - 1);
                if (closedTrack) {
                    next = trackDescription.get(0);
                } else {
                    next = new Vertex(vertex.getPositionV().add(vertex.getPositionV().subtract(previous.getPositionV())));
                }
            } else {
                previous = trackDescription.get(trackDescription.indexOf(vertex) - 1);
                next = trackDescription.get(trackDescription.indexOf(vertex) + 1);
            }
            slices.add(new TrackSlice(previous, vertex, next, laneWidth, laneCount, trackHeight));
        }
        if (closedTrack) {
            slices.add(slices.get(0));
        }
        final List<IndexedVertex> topVertices = new ArrayList<>();
        final List<IndexedVertex> bottomVertices = new ArrayList<>();
        final List<IndexedVertex> innerVertices = new ArrayList<>();
        final List<IndexedVertex> outerVertices = new ArrayList<>();
        for (TrackSlice slice : slices) {
            topVertices.add(IndexedVertex.makeIndexedVertex(slice.getTop().getVertex1()));
            topVertices.add(IndexedVertex.makeIndexedVertex(slice.getTop().getVertex2()));
            bottomVertices.add(IndexedVertex.makeIndexedVertex(slice.getBottom().getVertex1()));
            bottomVertices.add(IndexedVertex.makeIndexedVertex(slice.getBottom().getVertex2()));
            innerVertices.add(IndexedVertex.makeIndexedVertex(slice.getInner().getVertex1()));
            innerVertices.add(IndexedVertex.makeIndexedVertex(slice.getInner().getVertex2()));
            outerVertices.add(IndexedVertex.makeIndexedVertex(slice.getOuter().getVertex1()));
            outerVertices.add(IndexedVertex.makeIndexedVertex(slice.getOuter().getVertex2()));
        }
        surfaceCompilation.addSurface(new Surface(topVertices, false));
        surfaceCompilation.addSurface(new Surface(bottomVertices, false));
        surfaceCompilation.addSurface(new Surface(innerVertices, false));
        surfaceCompilation.addSurface(new Surface(outerVertices, false));
        if (!closedTrack) {
            surfaceCompilation.addSurface(slices.get(0).getEndPlate(true));
            surfaceCompilation.addSurface(slices.get(slices.size() - 1).getEndPlate(false));
        }
    }

    /**
     * Get a buffer with all vertices of the SurfaceCompilation.
     *
     * @return A buffer with all vertices of the SurfaceCompilation.
     */
    public DoubleBuffer getDataBuffer() {
        return surfaceCompilation.getDataBuffer();
    }

    /**
     * Get a list of buffers with the indices of all vertices of all surfaces.
     *
     * @return A list of buffers with the indices of all vertices of all
     *         surfaces.
     */
    public List<IntBuffer> getIndicesBuffers() {
        return surfaceCompilation.getIndicesBuffers();
    }

    /**
     * Get a list of surface types (polygon or quad strip).
     *
     * @return A list of surface types.
     */
    public List<Boolean> getSurfaceTypeList() {
        return surfaceCompilation.getSurfaceTypeList();
    }

    private static final class TrackSlice {

        private final TrackSliceSide top, bottom, inner, outer;

        private TrackSlice(Vertex previous, Vertex current, Vertex next, double laneWidth, int laneCount, double trackHeight) {
            final double halfTrackWidth = laneWidth * laneCount * 0.5d;
            final Vector original = current.getPositionV();
            final Vector lower = original.subtract(Vector.Z.normalized().scale(trackHeight));
            final Vector lonNormal1 = lower.subtract(original).cross(next.getPositionV().subtract(original));
            final Vector lonNormal2 = previous.getPositionV().subtract(original).cross(lower.subtract(original));
            final Vector outerNormal = lonNormal1.add(lonNormal2).normalized();
            final Vector innerNormal = outerNormal.scale(-1d).normalized();
            final Vector latNormal1 = outerNormal.cross(next.getPositionV().subtract(original));
            final Vector latNormal2 = previous.getPositionV().subtract(original).cross(outerNormal);
            final Vector topNormal = latNormal1.add(latNormal2).normalized();
            final Vector bottomNormal = topNormal.scale(-1d).normalized();
            top = new TrackSliceSide(
                    new Vertex(original.add(outerNormal.scale(halfTrackWidth)), topNormal),
                    new Vertex(original.add(innerNormal.scale(halfTrackWidth)), topNormal)
            );
            bottom = new TrackSliceSide(
                    new Vertex(lower.add(outerNormal.scale(halfTrackWidth)), bottomNormal),
                    new Vertex(lower.add(innerNormal.scale(halfTrackWidth)), bottomNormal)
            );
            inner = new TrackSliceSide(
                    new Vertex(original.add(innerNormal.scale(halfTrackWidth)), innerNormal),
                    new Vertex(lower.add(innerNormal.scale(halfTrackWidth)), innerNormal)
            );
            outer = new TrackSliceSide(
                    new Vertex(original.add(outerNormal.scale(halfTrackWidth)), outerNormal),
                    new Vertex(lower.add(outerNormal.scale(halfTrackWidth)), outerNormal)
            );
        }

        private TrackSliceSide getTop() {
            return top;
        }

        private TrackSliceSide getBottom() {
            return bottom;
        }

        private TrackSliceSide getInner() {
            return inner;
        }

        private TrackSliceSide getOuter() {
            return outer;
        }

        private Surface getEndPlate(boolean flipNormal) {
            final List<IndexedVertex> vertices = new ArrayList<>();
            vertices.add(IndexedVertex.makeIndexedVertex(Vertex.crossNormal(top.getVertex2(), inner.getVertex1(), flipNormal)));
            vertices.add(IndexedVertex.makeIndexedVertex(Vertex.crossNormal(outer.getVertex1(), top.getVertex1(), flipNormal)));
            vertices.add(IndexedVertex.makeIndexedVertex(Vertex.crossNormal(bottom.getVertex1(), outer.getVertex2(), flipNormal)));
            vertices.add(IndexedVertex.makeIndexedVertex(Vertex.crossNormal(inner.getVertex2(), bottom.getVertex2(), flipNormal)));
            return new Surface(vertices, true);
        }

        private static final class TrackSliceSide {

            private final Vertex vertex1, vertex2;

            private TrackSliceSide(Vertex vertex1, Vertex vertex2) {
                this.vertex1 = vertex1;
                this.vertex2 = vertex2;
            }

            private Vertex getVertex1() {
                return vertex1;
            }

            private Vertex getVertex2() {
                return vertex2;
            }

        }
    }
}
