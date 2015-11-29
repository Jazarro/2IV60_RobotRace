package utility;

public final class Vertex{

    public static final int COORD_COUNT = 3;

    private double positionX;
    private double positionY;
    private double positionZ;
    private double normalX;
    private double normalY;
    private double normalZ;

    public Vertex(){
        setPosition(0d, 0d, 0d);
        setNormal(0d, 0d, 0d);
    }

    public Vertex(double positionX, double positionY, double positionZ){
        setPosition(positionX, positionY, positionZ);
        setNormal(0d, 0d, 0d);
    }

    public Vertex(double positionX, double positionY, double positionZ, double normalX, double normalY, double normalZ){
        setPosition(positionX, positionY, positionZ);
        setNormal(normalX, normalY, normalZ);
    }

    public void setPosition(double positionX, double positionY, double positionZ){
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
    }

    public double[] getPosition(){
        return new double[]{positionX, positionY, positionZ};
    }

    public void setNormal(double normalX, double normalY, double normalZ){
        this.normalX = normalX;
        this.normalY = normalY;
        this.normalZ = normalZ;
    }

    public double[] getNormal(){
        return new double[]{normalX, normalY, normalZ};
    }
}
