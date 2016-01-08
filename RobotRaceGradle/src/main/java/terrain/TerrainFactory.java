/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import bodies.Body;
import bodies.BufferManager;
import bodies.Shape;
import bodies.assembly.Vertex;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import robotrace.Vector;

/**
 * Generic factory capable of generating a terrain {@link Body} from a
 * {@link HeightMap} blueprint.
 *
 * This solution is loosely based on a 2011 blog post by Jeremiah van Oosten:
 * http://www.3dgep.com/multi-textured-terrain-in-opengl/#The_Terrain::GenerateVertexBuffers_Method
 *
 * @author Arjan Boschman
 */
public class TerrainFactory {

    private final float widthInMeters;
    private final float heightInMeters;
    private final float blockScale;
    private final int widthInVertices;
    private final int heightInVertices;

    /**
     * Make a new instance of TerrainFactory.
     *
     * @param widthInMeters  The width (size on x-axis) of the terrain to render
     *                       in meters.
     * @param heightInMeters The height (size of y-axis) of the terrain to
     *                       render in meters.
     * @param blockScale     The block scale in meters per vertex. Make smaller
     *                       to get smoother edges; make bigger to save on
     *                       performance.
     */
    public TerrainFactory(float widthInMeters, float heightInMeters, float blockScale) {
        this.heightInMeters = heightInMeters;
        this.widthInMeters = widthInMeters;
        this.blockScale = blockScale;
        this.widthInVertices = (int) (widthInMeters / blockScale);
        this.heightInVertices = (int) (heightInMeters / blockScale);
    }

    //TODO: docs.
    public Body makeTerrain(BufferManager.Initialiser bmInitialiser, HeightMap heightMap) {
        final IntBuffer indexBuffer = generateIndexBuffer();
        final Vector[] points = generatePoints(heightMap);
        final Vector[] normals = generateNormals(points, indexBuffer);
        final DoubleBuffer dataBuffer = generateVertices(points, normals);
        final int indexBufferName = bmInitialiser.addData(dataBuffer, indexBuffer);
        return new Shape(indexBufferName, indexBuffer.capacity(), GL2.GL_TRIANGLES);
    }

    private DoubleBuffer generateVertices(Vector[] points, Vector[] normals) {
        final int nrVertices = widthInVertices * heightInVertices;
        final DoubleBuffer buffer = DoubleBuffer.allocate(Vertex.COORD_COUNT * Vertex.NR_VERTEX_ELEMENTS * nrVertices);
        for (int i = 0; i < nrVertices; i++) {
            buffer.put(new double[]{points[i].x(), points[i].y(), points[i].z()});
            buffer.put(new double[]{normals[i].x(), normals[i].y(), normals[i].z()});
        }
        buffer.position(0);
        return buffer;
    }

    //TODO: doc.
    private IntBuffer generateIndexBuffer() {
        final IntBuffer buffer = IntBuffer.allocate(getNrTriangles() * Vertex.COORD_COUNT);
        for (int x = 0; x < widthInVertices - 1; x++) {
            for (int y = 0; y < heightInVertices - 1; y++) {//Be mindful of possible error here.
                final int vertexIndex = (y * widthInVertices) + x;
                // Top triangle (T0)
                buffer.put(vertexIndex);                                        //V0
                buffer.put(vertexIndex + widthInVertices + 1);                  //V3
                buffer.put(vertexIndex + 1);                                    //V1
                // Bottom triangle (T1)
                buffer.put(vertexIndex);                                        //V0
                buffer.put(vertexIndex + widthInVertices);                      //V2
                buffer.put(vertexIndex + widthInVertices + 1);                  //V3
            }
        }
        buffer.position(0);
        return buffer;
    }

    //TODO: doc.
    private Vector[] generatePoints(HeightMap heightMap) {
        final int nrVertices = widthInVertices * heightInVertices;
        final Vector[] points = new Vector[nrVertices];
        for (int x = 0; x < widthInVertices; x++) {
            for (int y = 0; y < heightInVertices; y++) {
                final int vertexIndex = (y * widthInVertices) + x;
                final float xInMeters = x * blockScale - widthInMeters * 0.5f;
                final float yInMeters = y * blockScale - heightInMeters * 0.5f;
                final float zInMeters = heightMap.heightAt(xInMeters, yInMeters);
                points[vertexIndex] = new Vector(xInMeters, yInMeters, zInMeters);
            }
        }
        return points;
    }

    //TODO: doc.
    private Vector[] generateNormals(Vector[] points, IntBuffer indexBuffer) {
        final int nrVertices = widthInVertices * heightInVertices;
        final Vector[] normals = new Vector[nrVertices];
        //Loop through the triangles, calculate face normals and add them to the corresponding vertices.
        for (int i = 0; i < indexBuffer.capacity(); i += 3) {
            final Vector point0 = points[indexBuffer.get(i + 0)];
            final Vector point1 = points[indexBuffer.get(i + 1)];
            final Vector point2 = points[indexBuffer.get(i + 2)];
            final Vector faceNormal = point2.subtract(point0).cross(point1.subtract(point0)).normalized();
            addVectorToVertex(faceNormal, normals, indexBuffer.get(i + 0));
            addVectorToVertex(faceNormal, normals, indexBuffer.get(i + 1));
            addVectorToVertex(faceNormal, normals, indexBuffer.get(i + 2));
        }
        //Normalise all the vertex normals.
        for (int i = 0; i < normals.length; i++) {
            normals[i] = normals[i].normalized();
        }
        return normals;
    }

    /**
     * Convenience method that changes the value of the Vector in the given
     * array at the given index. If the previous value was null, the new value
     * will be set to the given operand. If the existing value is a Vector, the
     * new value will be the sum of the existing value and the given operand.
     *
     * @param toAdd          Operand to add to the existing value.
     * @param existingValues An array of Vectors. Its elements may or may not be
     *                       null.
     * @param index          The index at which the existing value is located in
     *                       the array.
     */
    private void addVectorToVertex(Vector toAdd, Vector[] existingValues, int index) {
        if (existingValues[index] == null) {
            existingValues[index] = toAdd;
        } else {
            existingValues[index] = existingValues[index].add(toAdd);
        }
    }

    private int getNrTriangles() {
        return (widthInVertices - 1) * (heightInVertices - 1) * 2;
    }

}
