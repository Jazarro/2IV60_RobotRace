package utility;

public class Surface{

    private final double[] vertex;
    private int[] vertexIndices;
    private final double[] normal;
    private int[] normalIndices;
    private final int sharedVrtxNrml;
    private final boolean polygon;

    public Surface(double[] vertex, int[] sharedVertexIndices, double[] normal, int[] sharedNormalIndices, boolean polygon){
        this.vertex = vertex;
        this.normal = normal;
        if((sharedVertexIndices != null) && (sharedNormalIndices != null)){
            this.vertexIndices = sharedVertexIndices;
            this.normalIndices = sharedNormalIndices;
        }
        this.sharedVrtxNrml = sharedVertexIndices.length;
        this.polygon = polygon;
    }

    public void setVertexIndices(int[] vertexIndices){
        final int[] newVrtxIndices = new int[this.vertexIndices.length + vertexIndices.length];
        System.arraycopy(this.vertexIndices, 0, newVrtxIndices, 0, this.vertexIndices.length);
        System.arraycopy(vertexIndices, 0, newVrtxIndices, this.vertexIndices.length, vertexIndices.length);
        this.vertexIndices = newVrtxIndices;
    }

    public void setNormalIndices(int[] normalIndices){
        final int[] newNrmlIndices = new int[this.normalIndices.length + normalIndices.length];
        System.arraycopy(this.normalIndices, 0, newNrmlIndices, 0, this.normalIndices.length);
        System.arraycopy(normalIndices, 0, newNrmlIndices, this.normalIndices.length, normalIndices.length);
        this.normalIndices = newNrmlIndices;
    }

    public int[] getVertexIndices(){
        return vertexIndices;
    }

    public int[] getNormalIndices(){
        return normalIndices;
    }

    public double[] getVertex(){
        return vertex;
    }

    public double[] getNormal(){
        return normal;
    }

    public int getSharedVrtxNrml(){
        return sharedVrtxNrml;
    }

    public boolean isPolygon(){
        return polygon;
    }

    /*public int getVertexCount() {
        return vertexIndices.length / Assembler.NUMCOORD;
    }*/
}
