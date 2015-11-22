/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPOT_DIRECTION;

/**
 *
 * @author Arjan Boschman
 */
public class Lighting {

    public void initialize(GL2 gl) {
        final float[] lightPos = {0.0f, 0.0f, 2f, 0.0f};
        final float[] lightDirection = {0f, 0f, -1f};

        final float[] diffuseLight = {1, 1, 1, 1};
        final float[] specularLight = {1, 1, 1, 1};
        final float[] ambientLight = {0.1f, 0.1f, 0.1f, 1};

        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glShadeModel(GL_SMOOTH);
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, lightDirection, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
    }

    public void setMaterial(GL2 gl, Material material) {
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, new float[]{0.1f, 0.1f, 0.1f, 1}, 0);
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material.diffuse, 0);
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, material.specular, 0);
        gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material.shininess);
    }

    public void setView(GL2 gl) {

// color
//        gl.glBegin(GL_TRIANGLES);
//        gl.glNormal3fv(n1);
//        gl.glVertex3fv(v1); // draw triangle, give
//        gl.glNormal3fv(n2);
//        gl.glVertex3fv(v2); // first normal, followed
//        gl.glNormal3fv(n3);
//        gl.glVertex3fv(v3); // by vertex
//        gl.glEnd();
    }

    public void drawScene(GL2 gl) {

    }

}
