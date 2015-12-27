/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace.bender;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;

/**
 * Interface that abstracts a certain openGL 3D robot body. Two different robots
 * with different bodies could be used by the same Robot class through this
 * interface.
 *
 * Only one instance of any given GLRobotBody implementation needs to exist.
 * Multiple robots can use the same instance, that way the memory used by the
 * vertices isn't allocated multiple times for the same body. As such, one can
 * think of a GLRobotBody more as a blueprint than an actual body. It therefore
 * also doesn't have a position or orientation in the world.
 *
 * @author Arjan Boschman
 */
public interface GLRobotBody {

    /**
     * Loads or calculates all data needed to draw the body and prepares the
     * given instance of GL2 for the task.
     *
     * @param gl The instance of GL2 responsible for drawing the body.
     */
    void initialize(GL2 gl);

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
    void draw(GL2 gl, GLUT glut, boolean stickFigure);

    //TODO: Add run, wave etc methods.
}
