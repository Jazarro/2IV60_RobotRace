/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import bodies.assembly.Assembler;
import com.jogamp.opengl.util.gl2.GLUT;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.List;
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

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void draw(GL2 gl, GLUT glut) {
        shapes.stream().forEach((shape) -> shape.draw(gl, glut));
    }


}
