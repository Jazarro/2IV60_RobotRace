package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Represents a Robot, to be implemented according to the Assignments.
 */
class Robot {

    /**
     * The position of the robot.
     */
    private Vector position = new Vector(0, 0, 0);
    /**
     * The direction in which the robot is running.
     */
    private Vector direction = new Vector(1, 0, 0);
    /**
     * The material from which this robot is built.
     */
    private final Material material;
    private final int robotType;
    private final Bender bender;

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, Bender bender) {
        this.robotType = 0;
        this.material = material;
        this.bender = bender;
    }

    public Robot(Material material, int robotType, Bender bender) {
        this.robotType = robotType;
        this.material = material;
        this.bender = bender;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Material getMaterial() {
        return material;
    }
    
    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        switch (robotType) {
            case 0:
            default:
                //Set benders color
                gl.glColor3f(0.6f, 0.6f, 0.6f);
                //Store the current matrix.
                gl.glPushMatrix();

                bender.draw(gl);

                //gl.glBegin(gl.GL_QUAD_STRIP);
                //gl.glEnd();
                //glut.glutSolidTeapot(0.5);
                //gl.glTranslatef(1,1,1);
                //gl.glRotatef(1,1,1);
                //gl.glScalef(1,1,1);
                //glut.glutSolidCone(axisThickness * 5, coneHeight, DEFAULT_SLICES, DEFAULT_STACKS);
                //glut.glutSolidCube(axisThickness);
                //Restore the original matrix.
                gl.glPopMatrix();

                break;
        }
    }

}
