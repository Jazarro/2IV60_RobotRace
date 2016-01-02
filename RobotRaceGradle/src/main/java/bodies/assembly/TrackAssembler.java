package bodies.assembly;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import robotrace.Vector;

public class TrackAssembler {


    private final SurfaceCompilation surfaceCompilation = new SurfaceCompilation();

    public void calculateTrack(List<Vertex> trackDescription, double laneWidth, int laneCount, double trackHeight) {
        final List<TrackSlice> slices = new ArrayList<>();
        for (Vertex vertex : trackDescription) {
            Vertex previous, next;
            if (trackDescription.indexOf(vertex) == 0) {
                previous = trackDescription.get(trackDescription.size() - 1);
                next = trackDescription.get(trackDescription.indexOf(vertex) + 1);
            } else if (trackDescription.indexOf(vertex) == (trackDescription.size() - 1)) {
                previous = trackDescription.get(trackDescription.indexOf(vertex) - 1);
                next = trackDescription.get(0);
            } else {
                previous = trackDescription.get(trackDescription.indexOf(vertex) - 1);
                next = trackDescription.get(trackDescription.indexOf(vertex) + 1);
            }
            slices.add(new TrackSlice(previous, vertex, next, laneWidth, laneCount, trackHeight));
        }
        slices.add(slices.get(0));
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
            final double halfTrackWidth = laneWidth * laneCount / 2d;
            final Vector original = current.getPositionV();
            final Vector lower = new Vector(original.x(), original.y(), original.z() - trackHeight);
            final Vector lonNormal1 = lower.subtract(current.getPositionV()).cross(next.getPositionV().subtract(current.getPositionV()));
            final Vector lonNormal2 = previous.getPositionV().subtract(current.getPositionV()).cross(lower.subtract(current.getPositionV()));
            final Vector outerNormal = lonNormal1.add(lonNormal2).normalized();
            final Vector innerNormal = outerNormal.scale(-1d).normalized();
            final Vector latNormal1 = outerNormal.cross(next.getPositionV().subtract(current.getPositionV()));
            final Vector latNormal2 = previous.getPositionV().subtract(current.getPositionV()).cross(outerNormal);
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

        public TrackSliceSide getTop() {
            return top;
        }

        public TrackSliceSide getBottom() {
            return bottom;
        }

        public TrackSliceSide getInner() {
            return inner;
        }

        public TrackSliceSide getOuter() {
            return outer;
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
