/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

import bodies.BodyManager;
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
     * @param gl            The instance of GL2 responsible for drawing the
     *                      body.
     * @param bmInitialiser The BodyManager.Initialiser that is used to gather
     *                      all the data before communicating it to OpenGL.
     */
    void initialize(GL2 gl, BodyManager.Initialiser bmInitialiser);

    /**
     * To be called in the draw loop. Uses the given instances of GL2 and GLUT
     * to draw the body.
     *
     * NB: There are plans to drop the dependency on GLUT entirely, beware this
     * second parameter will likely disappear in a later version.
     *
     * @param gl          The instance of GL2 responsible for drawing the body.
     * @param glut        An instance of GLUT that can be optionally used to
     *                    assist in drawing.
     * @param stickFigure Whether the GLRobotBody is in stick mode or not. When
     *                    in stick mode, the bodies are supposed to be drawn as
     *                    stick figures.
     */
    void draw(GL2 gl, GLUT glut, boolean stickFigure);

    /**
     * Sets the default stance or animation. This animation will be repeated
     * indefinitely. When {@link #runAnimation} or {@link #runAnimationOnce} get
     * called, after those animations finish running the robot body will return
     * to displaying the default stance.
     *
     * The default stance is always the IDLE stance, until this method gets
     * called.
     *
     * Note that robot bodies are free to provide their own implementations of
     * the animations, and are in fact not obliged to support any stances other
     * than the IDLE stance.
     *
     * @param stance The animation type to display by default. If this robot
     *               body doesn't support the given stance, it will default to
     *               the IDLE stance.
     */
    void setDefaultAnimation(Stance stance);

    /**
     * Interrupt the currently playing animation and run the given animation
     * type once. After the animation is done, return to the default stance.
     *
     * @param stance Specifies the animation to run once.
     */
    void runAnimationOnce(Stance stance);

    /**
     * Interrupt the currently playing animation and run the given animation
     * type a given number of times. After the animation is done, return to the default stance.
     *
     * @param stance  Specifies the animation to run.
     * @param repeats How often the animation should repeat. A value equal to or
     *                lower than zero is interpreted as indefinitely and will
     *                trigger a call to {@link #setDefaultAnimation}.
     */
    void runAnimation(Stance stance, int repeats);

}
