///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package robotrace;
//
//import com.jogamp.common.nio.Buffers;
//import java.nio.FloatBuffer;
//import javax.media.opengl.GL2;
//
///**
// *
// * @author Arjan Boschman
// */
//public class Something {
//
//    public void init(GL2 gl) {
//        int vertices = 3;
//
//        int vertex_size = 3; // X, Y, Z,
//        int color_size = 3; // R, G, B,
//
//        FloatBuffer vertex_data = Buffers.newDirectFloatBuffer(vertices * vertex_size);
//        vertex_data.put(new float[]{-1f, -1f, 0f,});
//        vertex_data.put(new float[]{1f, -1f, 0f,});
//        vertex_data.put(new float[]{1f, 1f, 0f,});
//        vertex_data.flip();
//
//        FloatBuffer color_data = Buffers.newDirectFloatBuffer(vertices * color_size);
//        color_data.put(new float[]{1f, 0f, 0f,});
//        color_data.put(new float[]{0f, 1f, 0f,});
//        color_data.put(new float[]{0f, 0f, 1f,});
//        color_data.flip();
//
//        int vbo_vertex_handle = gl.glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
//        glBufferData(GL_ARRAY_BUFFER, vertex_data, GL_STATIC_DRAW);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//
//        int vbo_color_handle = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
//        glBufferData(GL_ARRAY_BUFFER, color_data, GL_STATIC_DRAW);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//    }
//
//    public void draw() {
//        glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
//        glVertexPointer(vertex_size, GL_FLOAT, 0, 0l);
//
//        glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
//        glColorPointer(color_size, GL_FLOAT, 0, 0l);
//
//        glEnableClientState(GL_VERTEX_ARRAY);
//        glEnableClientState(GL_COLOR_ARRAY);
//
//        glDrawArrays(GL_TRIANGLES, 0, vertices);
//
//        glDisableClientState(GL_COLOR_ARRAY);
//        glDisableClientState(GL_VERTEX_ARRAY);
//    }
//
//}
