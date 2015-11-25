package robotrace;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.gl2.GLUT;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 Represents a Robot, to be implemented according to the Assignments.
 */
class Robot{

    /**
     The position of the robot.
     */
    public Vector position = new Vector(0, 0, 0);

    /**
     The direction in which the robot is running.
     */
    public Vector direction = new Vector(1, 0, 0);

    /**
     The material from which this robot is built.
     */
    private final Material material;
    private final int robotType;

    private final Bender bender;

    /**
     Constructs the robot with initial parameters.
     */
    public Robot(Material material, Bender bender){
        this.robotType = 0;
        this.material = material;
        this.bender = bender;
    }

    public Robot(Material material, int robotType, Bender bender){
        this.robotType = robotType;
        this.material = material;
        this.bender = bender;
    }

    /**
     Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim){
        switch(robotType){
            case 0:
            default:
                //Set benders color
                gl.glColor3f(0.6f, 0.6f, 0.6f);
                //Store the current matrix.
                gl.glPushMatrix();

                gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bender.getVerticesBuffer());
                gl.glVertexPointer(bender.getCoordCount(), GL2.GL_DOUBLE, 0, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getShinyBuffer());
                gl.glDrawElements(GL2.GL_POLYGON, bender.getSliceCount(), GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getTorsoBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * 2, GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getNeckBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * 2, GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getHeadlBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * 2, GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getHeadhBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * (bender.getStackCount() + 1) * 2, GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getAntbBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * (bender.getStackCount()) * 2, GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getAntmBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * 2, GL2.GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bender.getAnttBuffer());
                gl.glDrawElements(GL2.GL_QUAD_STRIP, (bender.getSliceCount()) * (bender.getStackCount()) * 4, GL2.GL_UNSIGNED_INT, 0);

                gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

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
