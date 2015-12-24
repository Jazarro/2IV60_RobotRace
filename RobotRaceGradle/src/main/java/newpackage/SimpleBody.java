/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package newpackage;

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

    /**
     * Can create an instance of {@link SimpleBody} that consists of a stack of
     * partial toruses and conical frustums.
     *
     * The initial shape will be built at the bottom, with progressive shapes
     * built back to back on top of each other, creating a tunnel-like shape.
     */
    @SuppressWarnings("PublicInnerClass")
    public static class StackBuilder {

        public SimpleBody build() {
            return null;
        }

    }

}
