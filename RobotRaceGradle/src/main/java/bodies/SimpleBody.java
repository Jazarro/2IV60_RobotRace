/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import bodies.assembly.Assembler;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.HashSet;
import java.util.Set;
import javax.media.opengl.GL2;

/**
 * A simple Body consisting of an arbitrary number or shapes. Use the builders
 * given by this class to obtain instances of SimpleBody.
 *
 * @author Arjan Boschman
 */
public class SimpleBody implements Body {

    private final Set<Shape> shapes = new HashSet<>();

    @Override
    public void draw(GL2 gl, GLUT glut) {
        shapes.stream().forEach((shape) -> shape.draw(gl, glut));
    }

    //=============== Nested Classes: ==========================================
    /**
     * Can create an instance of {@link SimpleBody} that consists of a stack of
     * partial toruses and conical frustums.
     *
     * The initial shape will be built at the bottom, with progressive shapes
     * built back to back on top of each other, creating a tunnel-like shape.
     */
    @SuppressWarnings("PublicInnerClass")
    public static class StackBuilder {

        private final BodyManager bodyManager;
        private final Assembler assembler;
        private int sliceCount = 3;
        private boolean useRelativeHeights = false;

        /**
         * Construct a new SimpleBody.StackBuilder. This builder can be used to
         * create one stacked body.
         *
         * @param bodyManager The BodyManager where the actual data will be
         *                    stored. The BodyManager deals with OpenGL
         *                    directly, the bodies created by these builders do
         *                    not.
         */
        public StackBuilder(BodyManager bodyManager) {
            this.bodyManager = bodyManager;
            this.assembler = new Assembler();
        }

        /**
         * Create the SimpleBody previously defined by calling the other public
         * methods of this builder.
         *
         * @return
         */
        public SimpleBody build() {
            return null;//TODO:...
        }

        /**
         * Sets the slice count for all shapes. The slice count defines the
         * number of faces each element of the stacked body has. The default is
         * 3.
         *
         * NB: For now, it is necessary to keep the slice count the same for all
         * shapes in a stacked body.
         *
         * @param sliceCount The new slice count.
         *
         * @return This StackBuilder.
         */
        public StackBuilder setSliceCount(int sliceCount) {
            this.sliceCount = sliceCount;
            return this;
        }

        /**
         * Whether to interpret the given heights as relative or not. If set to
         * true, heights will be relative to the previously added segment.
         * Otherwise they'll be relative to the stacked body as a whole. The
         * default is false.
         *
         * @param useRelativeHeights True to use relative heights, false to
         *                           interpret them as absolute.
         *
         * @return This StackBuilder.
         */
        @Deprecated//Not sure if this is necessary.
        public StackBuilder useRelativeHeights(boolean useRelativeHeights) {
            this.useRelativeHeights = useRelativeHeights;
            return this;
        }

        /**
         * Add a polygon to close off the stacked body.
         *
         * @param height       The height relative to the stacked body.
         * @param isFacingDown Announce whether this polygon is facing up or
         *                     down. True is down, false is up.
         * @return This StackBuilder.
         */
        public StackBuilder addPolygon(double height, boolean isFacingDown) {
            return this;//TODO:...
        }

        /**
         * Add a conical frustum (cone with top cut off) to the assembly.
         *
         * Do this by adding a partial torus, because a conical frustum is a
         * partial torus with stackCount equal to one.
         *
         * @param radiusLow  The radius of the lower ring of the frustum.
         * @param radiusHigh The radius of the higher ring of the frustum.
         * @param heightLow  The height of the lower ring of the frustum.
         * @param heightHigh The height of the higher ring of the frustum.
         *
         * @return This StackBuilder.
         */
        public StackBuilder addConicalFrustum(double radiusLow, double radiusHigh,
                double heightLow, double heightHigh) {
            return addPartialTorus(1, radiusLow, radiusHigh, heightLow, heightHigh);
        }

        /**
         * Add a partial torus (torus with only one quarter of it's
         * cross-section) to the assembly.
         *
         * The lower ring and its properties will be ignored and the upper ring
         * from the last command will be used if compatible.
         *
         * @param stackCount The number of stacks (z axis) that the torus is
         *                   divided in, more slices equals a smoother surface.
         * @param radiusLow  The radius of the lower ring of the torus.
         * @param radiusHigh The radius of the higher ring of the torus.
         * @param heightLow  The height of the lower ring of the torus.
         * @param heightHigh The height of the higher ring of the torus.
         *
         * @return This StackBuilder.
         */
        public StackBuilder addPartialTorus(int stackCount, double radiusLow,
                double radiusHigh, double heightLow, double heightHigh) {
            return this; //TODO:...
        }

    }

}
