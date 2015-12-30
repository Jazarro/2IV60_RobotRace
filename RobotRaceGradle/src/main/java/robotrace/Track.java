package robotrace;

import bodies.assembly.Vertex;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Track {

    private static final double STEP_T = 0.01d;
    private static final double LANE_WIDTH = 1.22d;
    private static final int LANE_COUNT = 4;
    private static final double TRACK_HEIGHT = 1d;

    private DoubleBuffer data;
    private List<IntBuffer> indices;

    private void initialize() {
        final List<Vertex> vertices = new ArrayList<>();
        final List<TrackSlice> slices = new ArrayList<>();
        /*for (double t = 0; t < 1; t += STEP_T) {
            vertices.add(new Vertex(getPoint(t)));
        }*/
        //TODO: get track points
        for (Vertex vertex : vertices) {
            Vertex previous, next;
            if (vertices.indexOf(vertex) == 0) {
                previous = vertices.get(vertices.size() - 1);
                next = vertices.get(vertices.indexOf(vertex) + 1);
            } else if (vertices.indexOf(vertex) == (vertices.size() - 1)) {
                previous = vertices.get(vertices.indexOf(vertex) - 1);
                next = vertices.get(0);
            } else {
                previous = vertices.get(vertices.indexOf(vertex) - 1);
                next = vertices.get(vertices.indexOf(vertex) + 1);
            }
            slices.add(new TrackSlice(previous, vertex, next));
        }
        final int duplicatedVertices = 2;
        final int verticesPerSide = 2 * slices.size() + duplicatedVertices;
        final int top[] = new int[verticesPerSide];
        final int bottom[] = new int[verticesPerSide];
        final int inner[] = new int[verticesPerSide];
        final int outer[] = new int[verticesPerSide];
        for (int i = 0; i < verticesPerSide - duplicatedVertices; i++) {
            top[i] = slices.get(i).getTop().getVertex1().;
            bottom[i] = bottom.get(i);
            inner[i] = inner.get(i);
            outer[i] = outer.get(i);
        }
        //TODO: convert slices into surfaces
        //TODO: Convert top, bottom, inner, outer, vertices to IntBuffer, DoubleBuffer
    }

    private static final class TrackSlice {

        private final TrackSliceSide top, bottom, inner, outer;

        private TrackSlice(Vertex previous, Vertex current, Vertex next) {
            final double halfTrackWidth = LANE_WIDTH * LANE_COUNT / 2d;
            final Vector original = current.getPositionV();
            final Vector lower = new Vector(original.x(), original.y(), original.z() - TRACK_HEIGHT);
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
