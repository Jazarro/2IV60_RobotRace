package robotrace;

import com.jogamp.opengl.util.gl2.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import robotrace.bender.*;

/**
 Represents a Robot, to be implemented according to the Assignments.
 */
class Robot{

    /**
     The position of the robot.
     */
    private Vector position = new Vector(0, 0, 0);
    /**
     The direction in which the robot is running.
     */
    private Vector direction = new Vector(0, 1, 0);
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

    public void setPosition(Vector position){
        this.position = position;
    }

    public void setDirection(Vector direction){
        /**
         TODO: Restore this setter to its proper functionality. Right now the
         lane tangents are not implemented yet (because it's part of the
         second assignment) and this would be set to the O vector.
         */
//        this.direction = direction;
        this.direction = Vector.X;
    }

    public Material getMaterial(){
        return material;
    }

    /**
     Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim){
        switch(robotType){
            case 0:
            default:
                gl.glPushMatrix();
                final Vector rotationAxis = calcRotationAxis();
                gl.glRotated(calcRotationAngle(), rotationAxis.x, rotationAxis.y, rotationAxis.z);
                bender.draw(gl);
                gl.glPopMatrix();
                break;
        }
    }

    private double calcRotationAngle(){
        double f = Math.toDegrees(Math.acos(direction.dot(Bender.ORIENTATION)));
        return f;
    }

    private Vector calcRotationAxis(){
        Vector c = direction.cross(Bender.ORIENTATION).normalized();
        return c;
    }

}
