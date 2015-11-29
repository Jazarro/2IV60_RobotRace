package utility;

public class Surface{

    private final int[] indices;
    private final boolean polygon;

    public Surface(int[] indices, boolean polygon){
        this.indices = indices;
        this.polygon = polygon;
    }

    public int[] getIndices(){
        return indices;
    }

    public boolean isPolygon(){
        return polygon;
    }
}
