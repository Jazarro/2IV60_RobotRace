/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies.assembly;

import robotrace.*;

/**

 @author Robke Geenen
 */
public class Vertex{

    /**
     The number of coordinates a Vertex consists of.
     */
    public static final int COORD_COUNT = 3;
    /**
     The indices in the position and normal arrays that signify the x, y, and
     z components.
     */
    public static final int IND_X = 0;
    public static final int IND_Y = 1;
    public static final int IND_Z = 2;

    /**
     The position of this Vertex in x, y and z components.
     */
    private double positionX;
    private double positionY;
    private double positionZ;
    /**
     The normal of this Vertex (mainly useful when using surfaces) in x, y and
     z components.
     */
    private double normalX;
    private double normalY;
    private double normalZ;

    /**
     Constructor.
     */
    public Vertex(){
        this(0d, 0d, 0d);
    }

    /**
     Constructor specifying the position of the Vertex.

     @param positionX Component of the position in the X axis.
     @param positionY Component of the position in the Y axis.
     @param positionZ Component of the position in the Z axis.
     */
    public Vertex(double positionX, double positionY, double positionZ){
        this(positionX, positionY, positionZ, 0d, 0d, 0d);
    }

    /**
     Constructor specifying the position and normal of the Vertex.

     @param positionX Component of the position in the X axis.
     @param positionY Component of the position in the Y axis.
     @param positionZ Component of the position in the Z axis.
     @param normalX   Component of the normal in the X axis.
     @param normalY   Component of the normal in the Y axis.
     @param normalZ   Component of the normal in the Z axis.
     */
    public Vertex(double positionX, double positionY, double positionZ, double normalX, double normalY, double normalZ){
        setPositionC(positionX, positionY, positionZ);
        setNormalC(normalX, normalY, normalZ);
    }

    /**
     Constructor specifying the position of the Vertex.

     @param position The position.
     */
    public Vertex(double[] position){
        this(position, new double[]{0d, 0d, 0d});
    }

    /**
     Constructor specifying the position and normal of the Vertex.

     @param position The position.
     @param normal   The normal.
     */
    public Vertex(double[] position, double[] normal){
        this(position[IND_X], position[IND_Y], position[IND_Z], normal[IND_X], normal[IND_Y], normal[IND_Z]);
    }

    /**
     Constructor specifying the position and normal of the Vertex.

     @param position The position.
     */
    public Vertex(Vector position){
        this(position.x(), position.y(), position.z());
    }

    /**
     Constructor specifying the position and normal of the Vertex.

     @param position The position.
     @param normal   The normal.
     */
    public Vertex(Vector position, Vector normal){
        this(position.x(), position.y(), position.z(), normal.x(), normal.y(), normal.z());
    }

    /**
     Set the position of this Vertex.

     @param positionX Component of the new position in the X axis.
     @param positionY Component of the new position in the Y axis.
     @param positionZ Component of the new position in the Z axis.
     */
    public final void setPositionC(double positionX, double positionY, double positionZ){
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
    }

    /**
     Set the position of this Vertex.

     @param position The new position.
     */
    public final void setPositionA(double[] position){
        this.setPositionC(position[IND_X], position[IND_Y], position[IND_Z]);
    }

    /**
     Set the position of this Vertex.

     @param position The new position.
     */
    public final void setPositionV(Vector position){
        this.setPositionC(position.x(), position.y(), position.z());
    }

    /**
     Get the position of this Vertex.

     @return The position of this Vertex.
     */
    public final double[] getPositionA(){
        double[] position = new double[COORD_COUNT];
        position[IND_X] = positionX;
        position[IND_Y] = positionY;
        position[IND_Z] = positionZ;
        return position;
    }

    /**
     Get the position of this Vertex.

     @return The position of this Vertex.
     */
    public final Vector getPositionV(){
        return new Vector(positionX, positionY, positionZ);
    }

    /**
     Set the normal of this Vertex.

     @param normalX Component of the new normal in the X axis.
     @param normalY Component of the new normal in the Y axis.
     @param normalZ Component of the new normal in the Z axis.
     */
    public final void setNormalC(double normalX, double normalY, double normalZ){
        this.normalX = normalX;
        this.normalY = normalY;
        this.normalZ = normalZ;
    }

    /**
     Set the normal of this Vertex.

     @param normal The new normal of this Vertex.
     */
    public final void setNormalA(double[] normal){
        this.setNormalC(normal[IND_X], normal[IND_Y], normal[IND_Z]);
    }

    /**
     Set the normal of this Vertex.

     @param normal The new normal of this Vertex.
     */
    public final void setNormalV(Vector normal){
        this.setNormalC(normal.x(), normal.y(), normal.z());
    }

    /**
     Get the normal of this Vertex.

     @return The normal of this Vertex.
     */
    public final double[] getNormalA(){
        double[] normal = new double[COORD_COUNT];
        normal[IND_X] = normalX;
        normal[IND_Y] = normalY;
        normal[IND_Z] = normalZ;
        return normal;
    }

    /**
     Get the normal of this Vertex.

     @return The normal of this Vertex.
     */
    public final Vector getNormalV(){
        return new Vector(normalX, normalY, normalZ);
    }
}
