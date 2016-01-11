/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import javax.media.opengl.GL2;

/**
 * A rigid body, consisting of a collection of one or more shapes. Because the
 * body is rigid, its shapes cannot be transformed relative to each other.
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
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    void draw(GL2 gl);

}
