/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import Texture.ImplementedTexture;
import bodies.Body;
import bodies.BufferManager;
import bodies.Shape;
import bodies.assembly.Vertex;
import java.nio.FloatBuffer;
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
        this.widthInVertices = (int) (widthInMeters / blockScale) + 1;
        this.heightInVertices = (int) (heightInMeters / blockScale) + 1;
    }

    /**
     * Use the given HeightMap to create data- and index buffers, then hand that
     * to the bmInitialiser and construct a {@link Body} for the terrain.
     *
     * @param bmInitialiser The BufferManager.Initialiser that takes the raw
     *                      buffers and gives them to OpenGL.
     * @param heightMap     The HeightMap instance that dictates what the data
     *                      should look like.
     * @param texture
     * @return A Body, which can be used during the draw phase to easily draw
     *         the entire terrain.
     */
    public Body makeTerrain(BufferManager.Initialiser bmInitialiser, HeightMap heightMap, ImplementedTexture texture) {
        final IntBuffer indexBuffer = generateIndexBuffer();
        final Vector[] points = generatePoints(heightMap);
        final Vector[] normals = generateNormals(points, indexBuffer);
        final Vector[] textures = generateTextures(points);
        final FloatBuffer dataBuffer = generateVertices(points, normals, textures);
        final int indexBufferName = bmInitialiser.addData(dataBuffer, indexBuffer);
        return new Shape(indexBufferName, indexBuffer.capacity(), GL2.GL_TRIANGLES).setTexture(texture);
    }

    /**
     * Take the arrays of points and normals that were previously generated and
     * interleave them into a data buffer.
     *
     * @param points  An array of points, parallel to the normals.
     * @param normals An array of normals, parallel to the points.
     * @return A DoubleBuffer with points and vectors interleaved.
     */
    private FloatBuffer generateVertices(Vector[] points, Vector[] normals, Vector[] textures) {
        final int nrVertices = widthInVertices * heightInVertices;
        final FloatBuffer buffer = FloatBuffer.allocate(Vertex.COORD_COUNT * Vertex.NR_VERTEX_ELEMENTS * nrVertices);
        for (int i = 0; i < nrVertices; i++) {
            buffer.put(new float[]{(float) points[i].x(), (float) points[i].y(), (float) points[i].z()});
            buffer.put(new float[]{(float) normals[i].x(), (float) normals[i].y(), (float) normals[i].z()});
            buffer.put(new float[]{(float) textures[i].x(), (float) textures[i].y(), (float) textures[i].z()});
        }
        buffer.position(0);
        return buffer;
    }

    /**
     * Generates the index buffer for this terrain's data buffer.
     *
     * @return An IntBuffer with pointers to each triangle's vertexes.
     */
    private IntBuffer generateIndexBuffer() {
        final IntBuffer buffer = IntBuffer.allocate(getNrTriangles() * 3);
        for (int y = 0; y < heightInVertices - 1; y++) {
            for (int x = 0; x < widthInVertices - 1; x++) {
                final int vertexIndex = (y * widthInVertices) + x;
                // Top triangle (T0)
                buffer.put(vertexIndex + 1);                                    //V1
                buffer.put(vertexIndex + widthInVertices + 1);                  //V3
                buffer.put(vertexIndex);                                        //V0
                // Bottom triangle (T1)
                buffer.put(vertexIndex + widthInVertices + 1);                  //V3
                buffer.put(vertexIndex + widthInVertices);                      //V2
                buffer.put(vertexIndex);                                        //V0
            }
        }
        buffer.position(0);
        return buffer;
    }

    /**
     * Generates an array of points in a grid. The x and y values of the points
     * range from zero to respectively widthInVertices and heightInVertices. The
     * z-values are arranged according to the given HeightMap.
     *
     * @param heightMap A HeightMap that dictates the z-values of the points
     *                  that are generated.
     * @return An array of points, equal in length to widthInVertices *
     *         heightInVertices.
     */
    private Vector[] generatePoints(HeightMap heightMap) {
        final int nrVertices = widthInVertices * heightInVertices;
        final Vector[] points = new Vector[nrVertices];
        for (int y = 0; y < heightInVertices; y++) {
            for (int x = 0; x < widthInVertices; x++) {
                final int vertexIndex = (y * widthInVertices) + x;
                final float xInMeters = x * blockScale - widthInMeters * 0.5f;
                final float yInMeters = y * blockScale - heightInMeters * 0.5f;
                final float zInMeters = heightMap.heightAt(xInMeters, yInMeters);
                points[vertexIndex] = new Vector(xInMeters, yInMeters, zInMeters);
            }
        }
        return points;
    }

    /**
     * Generates the vertex normals for the given points, by first calculating
     * the sum of the face normals of the neighbouring triangles, and then
     * normalising that.
     *
     * @param points      An array of points.
     * @param indexBuffer Index buffer for the given points. For all n where
     *                    n%3=0, the vectors at indices n, n+1 and n+2 describe
     *                    a triangle in a clockwise manner.
     * @return An array of vertex normals parallel to the points array. Each
     *         normal corresponds to the point at the same index in the point
     *         array.
     */
    private Vector[] generateNormals(Vector[] points, IntBuffer indexBuffer) {
        final Vector[] normals = new Vector[points.length];
        //Loop through the triangles, calculate face normals and add them to the corresponding vertices.
        for (int i = 0; i < indexBuffer.capacity(); i += 3) {
            final Vector point0 = points[indexBuffer.get(i + 0)];
            final Vector point1 = points[indexBuffer.get(i + 1)];
            final Vector point2 = points[indexBuffer.get(i + 2)];
            final Vector faceNormal = point1.subtract(point0).cross(point2.subtract(point0)).normalized();
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

    private Vector[] generateTextures(Vector[] points) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (Vector vector : points) {
            if ((float) vector.z() > max) {
                max = (float) vector.z();
            }
            if ((float) vector.z() < min) {
                min = (float) vector.z();
            }
        }
        final Vector[] textures = new Vector[points.length];
        for (int i = 0; i < points.length; i++) {
            final float zTexture = ((float) points[i].z() - min) / (max - min);
            textures[i] = new Vector(zTexture, 0d, 0d);
        }
        return textures;
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
