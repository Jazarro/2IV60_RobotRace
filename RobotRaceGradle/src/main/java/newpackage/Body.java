/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package newpackage;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;

/**
 * A rigid body, consisting of a collection of one or more shapes. Because the
 * body is rigid, its shapes cannot be transformed relative to each other. Note
 * also that the shapes are rigid themselves. The vertices that the shapes
 * consist of cannot be transformed relative to each other.
 *
 * Important is that only one copy of each body needs to exist. Bodies have no
 * position, scale or orientation in the world, they can be reused to draw the
 * same body multiple times on the screen.
 *
 * @author Arjan Boschman
 */
public interface Body {

    /**
     * To be called in the draw loop. Uses the given instances of GL2 and GLUT
     * to draw the body.
     *
     * NB: There are plans to drop the dependency on GLUT entirely, beware this
     * second parameter will likely disappear in a later version.
     *
     * @param gl   The instance of GL2 responsible for drawing the body.
     * @param glut An instance of GLUT that can be optionally used to assist in
     *             drawing.
     */
    void draw(GL2 gl, GLUT glut);

}
