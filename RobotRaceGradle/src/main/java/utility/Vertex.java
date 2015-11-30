package utility;

public class Vertex{

    public static final int COORD_COUNT = 3;
    public static final int IND_X = 0;
    public static final int IND_Y = 1;
    public static final int IND_Z = 2;

    private double positionX;
    private double positionY;
    private double positionZ;
    private double normalX;
    private double normalY;
    private double normalZ;

    public Vertex(){
        this(0d, 0d, 0d);
    }

    public Vertex(double positionX, double positionY, double positionZ){
        this(positionX, positionY, positionZ, 0d, 0d, 0d);
    }

    public Vertex(double positionX, double positionY, double positionZ, double normalX, double normalY, double normalZ){
        setPosition(positionX, positionY, positionZ);
        setNormal(normalX, normalY, normalZ);
    }

    public Vertex(double[] position){
        this(position, new double[]{0d, 0d, 0d});
    }

    public Vertex(double[] position, double[] normal){
        this(position[IND_X], position[IND_Y], position[IND_Z], normal[IND_X], normal[IND_Y], normal[IND_Z]);
    }

    public final void setPosition(double positionX, double positionY, double positionZ){
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
    }

    public final void setPosition(double[] position){
        this.positionX = position[IND_X];
        this.positionY = position[IND_Y];
        this.positionZ = position[IND_Z];
    }

    public final double[] getPosition(){
        double[] position = new double[COORD_COUNT];
        position[IND_X] = positionX;
        position[IND_Y] = positionY;
        position[IND_Z] = positionZ;
        return position;
    }

    public final void setNormal(double normalX, double normalY, double normalZ){
        this.normalX = normalX;
        this.normalY = normalY;
        this.normalZ = normalZ;
    }

    public final void setNormal(double[] normal){
        this.normalX = normal[IND_X];
        this.normalY = normal[IND_Y];
        this.normalZ = normal[IND_Z];
    }

    public final double[] getNormal(){
        double[] normal = new double[COORD_COUNT];
        normal[IND_X] = normalX;
        normal[IND_Y] = normalY;
        normal[IND_Z] = normalZ;
        return normal;
    }
}
