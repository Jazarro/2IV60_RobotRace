package utility;

public class Surface {

    private final int[] vertexIndices;
    private final int[] normalIndices;
    private final boolean polygon;

    public Surface(int[] vertexIndices, int[] normalIndices, boolean polygon) {
        this.vertexIndices = vertexIndices;
        this.normalIndices = normalIndices;
        this.polygon = polygon;
    }

    public int[] getVertexIndices() {
        return vertexIndices;
    }

    public int[] getNormalIndices() {
        return normalIndices;
    }

    public boolean isPolygon() {
        return polygon;
    }

    public int getVertexCount() {
        return vertexIndices.length / Assembler.NUMCOORD;
    }

}
