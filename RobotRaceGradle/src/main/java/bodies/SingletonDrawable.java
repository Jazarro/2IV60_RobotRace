/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import javax.media.opengl.GL2;

/**
 * Interface that describes any object that has a compositional relationship
 * with {@link Body} elements.
 *
 * Since only one instance of each Body needs to exist, SingletonDrawables
 * should also exist only once. They must be stateless, ergo they maintain no
 * record of previous interactions.
 *
 * @author Arjan Boschman
 * @see Body
 */
public interface SingletonDrawable {

    /**
     * Loads or calculates all data needed to draw the body and prepares the
     * given instance of GL2 for the task.
     *
     * @param gl            The instance of GL2 responsible for drawing the
     *                      bodies.
     * @param bmInitialiser The BodyManager.Initialiser that is used to gather
     *                      all the data before communicating it to OpenGL.
     */
    void initialize(GL2 gl, BufferManager.Initialiser bmInitialiser);

}
