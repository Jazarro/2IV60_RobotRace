/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;

/**
 * Interface that abstracts a certain openGL 3D robot body.
 *
 * @author Arjan Boschman
 * @see Robot Can be used by the Robot class to draw its body and set its
 * animations.
 */
public interface RobotBody {

    /**
     * The width of the stick figure sticks.
     */
    public static final double STICK_THICKNESS = 0.03d;

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
     * indefinitely. When {@link #playAnimation} or {@link #playAnimationOnce}
     * get called, after those animations finish running the robot body will
     * return to displaying the default stance.
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
    void playAnimationOnce(Stance stance);

    /**
     * Interrupt the currently playing animation and run the given animation
     * type a given number of times. After the animation is done, return to the
     * default stance.
     *
     * @param stance  Specifies the animation to run.
     * @param repeats How often the animation should repeat. A value equal to or
     *                lower than zero is interpreted as indefinitely and will
     *                trigger a call to {@link #setDefaultAnimation}.
     */
    void playAnimation(Stance stance, int repeats);

}
