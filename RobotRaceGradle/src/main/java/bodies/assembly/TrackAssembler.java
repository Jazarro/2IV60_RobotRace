package bodies.assembly;

import Texture.ImplementedTexture;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import robotrace.Vector;

public class TrackAssembler {

    private static final float TEXTURE_SCALE = 100f;

    private final SurfaceCompilation surfaceCompilation = new SurfaceCompilation();
    private final List<ImplementedTexture> textureList = new ArrayList<>();

    /**
     * Calculate all parameters of the track.
     *
     * @param trackDescription The list of Vertices that define the centerline.
     * @param laneWidth        The width of one lane.
     * @param laneCount        The number of lanes per track.
     * @param trackHeight      The hight of the track.
     * @param closedTrack      If the track is closed.
     * @param textureTop       The texture for the top of the track.
     * @param textureBottom    The texture for the bottom of the track.
     * @param textureSide      The texture for the side of the track.
     */
    public void calculateTrack(List<Vertex> trackDescription, float laneWidth, int laneCount, float trackHeight, boolean closedTrack, ImplementedTexture textureTop, ImplementedTexture textureBottom, ImplementedTexture textureSide) {
        final List<TrackSlice> slices = new ArrayList<>();
        /**
         * Select appropriate previous and next vertices (interpolate if at end
         * of list and open track)
         */
        final float trackWidth = laneWidth * laneCount;
        final float widthDistance = TEXTURE_SCALE * trackWidth;
        final float heightDistance = TEXTURE_SCALE * trackHeight;
        TrackSlice previousSlice = null;
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
            final TrackSlice newSlice = new TrackSlice(previous, vertex, next, previousSlice, trackWidth, trackHeight, (textureTop.getStretchWidth()) ? (1f) : (widthDistance / textureTop.getImageWidth()), (textureBottom.getStretchWidth()) ? (1f) : (widthDistance / textureBottom.getImageWidth()), (textureSide.getStretchHeight()) ? (1f) : (heightDistance / textureSide.getImageHeight()), textureTop.getImageHeight(), textureBottom.getImageHeight(), textureSide.getImageWidth());
            slices.add(newSlice);
            previousSlice = newSlice;
        }
        /**
         * Close the track if necessary.
         */
        if (closedTrack) {
            final Vertex previous = trackDescription.get(trackDescription.size() - 1);
            final Vertex vertex = trackDescription.get(0);
            final Vertex next = trackDescription.get(1);
            slices.add(new TrackSlice(previous, vertex, next, previousSlice, trackWidth, trackHeight, (textureTop.getStretchWidth()) ? (1f) : (widthDistance / textureTop.getImageWidth()), (textureBottom.getStretchWidth()) ? (1f) : (widthDistance / textureBottom.getImageWidth()), (textureSide.getStretchHeight()) ? (1f) : (heightDistance / textureSide.getImageHeight()), textureTop.getImageHeight(), textureBottom.getImageHeight(), textureSide.getImageWidth()));
        }
        /**
         * And create a SurfaceCompilation of the track.
         */
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
        textureList.add(textureTop);
        surfaceCompilation.addSurface(new Surface(bottomVertices, false));
        textureList.add(textureBottom);
        surfaceCompilation.addSurface(new Surface(innerVertices, false));
        textureList.add(textureSide);
        surfaceCompilation.addSurface(new Surface(outerVertices, false));
        textureList.add(textureSide);
        /**
         * With end caps if it is an open track.
         */
        if (!closedTrack) {
            surfaceCompilation.addSurface(slices.get(0).getStartEndPlate(true, widthDistance / textureSide.getImageWidth(), (textureSide.getStretchHeight()) ? (1f) : (heightDistance / textureSide.getImageHeight())));
            textureList.add(textureSide);
            surfaceCompilation.addSurface(slices.get(slices.size() - 1).getStartEndPlate(false, widthDistance / textureSide.getImageWidth(), (textureSide.getStretchHeight()) ? (1f) : (heightDistance / textureSide.getImageHeight())));
            textureList.add(textureSide);
        }
    }

    /**
     * Get a buffer with all vertices of the SurfaceCompilation.
     *
     * @return A buffer with all vertices of the SurfaceCompilation.
     */
    public FloatBuffer getDataBuffer() {
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

    /**
     * Get a list of textures.
     *
     * @return A list of textures.
     */
    public List<ImplementedTexture> getTextureList() {
        return textureList;
    }

    private static final class TrackSlice {

        private final TrackSliceSide top, bottom, inner, outer;
        float outerTopDistance, outerBottomDistance, centerTopDistance, centerBottomDistance, innerTopDistance, innerBottomDistance;

        /**
         * Constructor.
         *
         * @param previous    The previous vertex.
         * @param current     The current vertex.
         * @param next        The next vertex.
         * @param laneWidth   The width of one lane on the track.
         * @param laneCount   The number of lanes per track.
         * @param trackHeight The height of the track.
         */
        private TrackSlice(Vertex previous, Vertex current, Vertex next, TrackSlice previousSlice, float trackWidth, float trackHeight, float widthDistanceTop, float widthDistanceBottom, float heightDistance, float textureTopHeight, float textureBottomHeight, float textureSideWidth) {
            /**
             * Calculate all four corners of the slice of the track. And
             * calculate two normals per corner.
             */
            final double halfTrackWidth = trackWidth * 0.5d;
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
            final Vector topInner = original.add(innerNormal.scale(halfTrackWidth));
            final Vector topOuter = original.add(outerNormal.scale(halfTrackWidth));
            final Vector bottomInner = lower.add(innerNormal.scale(halfTrackWidth));
            final Vector bottomOuter = lower.add(outerNormal.scale(halfTrackWidth));
            if (previousSlice != null) {
                final Vector previousTopCenter = previousSlice.getTop().getVertex1().getPositionV().add(previousSlice.getTop().getVertex2().getPositionV()).scale(0.5d);
                final Vector previousBottomCenter = previousSlice.getBottom().getVertex1().getPositionV().add(previousSlice.getBottom().getVertex2().getPositionV()).scale(0.5d);
                outerTopDistance = TEXTURE_SCALE * (float) topOuter.subtract(previousSlice.getTop().getVertex1().getPositionV()).length()
                        + previousSlice.getOuterTopDistance();
                outerBottomDistance = TEXTURE_SCALE * (float) bottomOuter.subtract(previousSlice.getBottom().getVertex1().getPositionV()).length()
                        + previousSlice.getOuterBottomDistance();
                centerTopDistance = TEXTURE_SCALE * (float) original.subtract(previousTopCenter).length()
                        + previousSlice.getCenterTopDistance();
                centerBottomDistance = TEXTURE_SCALE * (float) lower.subtract(previousBottomCenter).length()
                        + previousSlice.getCenterBottomDistance();
                innerTopDistance = TEXTURE_SCALE * (float) topInner.subtract(previousSlice.getTop().getVertex2().getPositionV()).length()
                        + previousSlice.getInnerTopDistance();
                innerBottomDistance = TEXTURE_SCALE * (float) bottomInner.subtract(previousSlice.getBottom().getVertex2().getPositionV()).length()
                        + previousSlice.getInnerBottomDistance();

            } else {
                outerTopDistance = 0f;
                outerBottomDistance = 0f;
                centerTopDistance = 0f;
                centerBottomDistance = 0f;
                innerTopDistance = 0f;
                innerBottomDistance = 0f;
            }
            /**
             * Store them, two corners per face.
             */
            top = new TrackSliceSide(
                    new Vertex(topOuter, topNormal, new Vector(widthDistanceTop, centerTopDistance / textureTopHeight, 0d)),
                    new Vertex(topInner, topNormal, new Vector(0d, centerTopDistance / textureTopHeight, 0d))
            );
            bottom = new TrackSliceSide(
                    new Vertex(bottomOuter, bottomNormal, new Vector(widthDistanceBottom, centerBottomDistance / textureBottomHeight, 0d)),
                    new Vertex(bottomInner, bottomNormal, new Vector(0d, centerBottomDistance / textureBottomHeight, 0d))
            );
            inner = new TrackSliceSide(
                    new Vertex(topInner, innerNormal, new Vector(innerTopDistance / textureSideWidth, heightDistance, 0d)),
                    new Vertex(bottomInner, innerNormal, new Vector(innerBottomDistance / textureSideWidth, 0d, 0d))
            );
            outer = new TrackSliceSide(
                    new Vertex(topOuter, outerNormal, new Vector(outerTopDistance / textureSideWidth, heightDistance, 0d)),
                    new Vertex(bottomOuter, outerNormal, new Vector(outerBottomDistance / textureSideWidth, 0d, 0d))
            );
        }

        /**
         * Get the top face.
         *
         * @return The top face.
         */
        private TrackSliceSide getTop() {
            return top;
        }

        /**
         * Get the bottom face.
         *
         * @return The bottom face.
         */
        private TrackSliceSide getBottom() {
            return bottom;
        }

        /**
         * Get the inner face.
         *
         * @return The inner face.
         */
        private TrackSliceSide getInner() {
            return inner;
        }

        /**
         * Get the outer face.
         *
         * @return The outer face.
         */
        private TrackSliceSide getOuter() {
            return outer;
        }

        /**
         * Get the outer top distance face.
         *
         * @return The outer top distance face.
         */
        public float getOuterTopDistance() {
            return outerTopDistance;
        }

        /**
         * Get the outer bottom distance face.
         *
         * @return The outer bottom distance face.
         */
        public float getOuterBottomDistance() {
            return outerBottomDistance;
        }

        /**
         * Get the center top distance face.
         *
         * @return The center top distance face.
         */
        public float getCenterTopDistance() {
            return centerTopDistance;
        }

        /**
         * Get the center bottom distance face.
         *
         * @return The center bottom distance face.
         */
        public float getCenterBottomDistance() {
            return centerBottomDistance;
        }

        /**
         * Get the inner top distance face.
         *
         * @return The inner top distance face.
         */
        public float getInnerTopDistance() {
            return innerTopDistance;
        }

        /**
         * Get the inner bottom distance face.
         *
         * @return The inner bottom distance face.
         */
        public float getInnerBottomDistance() {
            return innerBottomDistance;
        }

        /**
         * Calculate an end cap (if the track is open).
         *
         * @param flipNormal If the face normal of the end cap should be flipped
         *                   180 degrees.
         * @return The Surface of the end cap.
         */
        private Surface getStartEndPlate(boolean flipNormal, float widthDistance, float heightDistance) {
            final List<IndexedVertex> vertices = new ArrayList<>();
            final Vertex topInner = Vertex.crossNormal(top.getVertex2(), inner.getVertex1(), flipNormal);
            final Vertex topOuter = Vertex.crossNormal(outer.getVertex1(), top.getVertex1(), flipNormal);
            final Vertex bottomInner = Vertex.crossNormal(inner.getVertex2(), bottom.getVertex2(), flipNormal);
            final Vertex bottomOuter = Vertex.crossNormal(bottom.getVertex1(), outer.getVertex2(), flipNormal);
            topInner.setTextureC(0f, heightDistance, 0f);
            topOuter.setTextureC(widthDistance, heightDistance, 0f);
            bottomInner.setTextureC(0f, 0f, 0f);
            bottomOuter.setTextureC(widthDistance, 0f, 0f);
            vertices.add(IndexedVertex.makeIndexedVertex(topInner));
            vertices.add(IndexedVertex.makeIndexedVertex(topOuter));
            vertices.add(IndexedVertex.makeIndexedVertex(bottomOuter));
            vertices.add(IndexedVertex.makeIndexedVertex(bottomInner));
            return new Surface(vertices, true);
        }

        private static final class TrackSliceSide {

            private final Vertex vertex1, vertex2;

            /**
             * Constructor.
             *
             * @param vertex1 The first corner of the face.
             * @param vertex2 The second corner of the face.
             */
            private TrackSliceSide(Vertex vertex1, Vertex vertex2) {
                this.vertex1 = vertex1;
                this.vertex2 = vertex2;
            }

            /**
             * Get the first corner of the face.
             *
             * @return The first corner of the face.
             */
            private Vertex getVertex1() {
                return vertex1;
            }

            /**
             * Get the second corner of the face.
             *
             * @return The second corner of the face.
             */
            private Vertex getVertex2() {
                return vertex2;
            }

        }
    }
}
