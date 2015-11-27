/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;

/**
 *
 * @author Arjan Boschman
 */
public class Lighting {

    private final float[] lightPos = {0.0f, 2f, 0.1f, 0.0f};
    private final float[] diffuseLight = {1f, 1f, 1f, 1};
    private final float[] specularLight = {1, 1, 1, 1};
    private final float[] ambientLight = {0.1f, 0.1f, 0.1f, 1};

    public void initialize(GL2 gl) {
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
    }

    public void setView(GL2 gl) {
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
    }

    public void drawScene(GL2 gl) {
        //Do nothing, for now.
    }

    public void setMaterial(GL2 gl, Material material) {
        gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, material.diffuse, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, material.specular, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, material.shininess);
    }

}
