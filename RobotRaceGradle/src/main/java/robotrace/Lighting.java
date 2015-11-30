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
import static utility.GsUtils.getAzimuth;
import static utility.GsUtils.getInclination;

/**
 *
 * @author Arjan Boschman
 */
public class Lighting {

    private static final float[] COLOUR_BLACK = new float[]{0, 0, 0, 0};
    private final float[] lightPos = new float[4];
    private final float[] diffuseLight = {1f, 1f, 1f, 1f};
    private final float[] specularLight = {1f, 1f, 1f, 1f};
    private final float[] ambientLight = {0.1f, 0.1f, 0.1f, 1f};

    public void initialize(GL2 gl, GlobalState gs) {
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        calculatePosition(gs);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
    }

    private void calculatePosition(GlobalState gs) {
        //Calculate the light position to be 10 degrees above and to the left of the starting eye point.
        final float azimuth = getAzimuth(gs) - 10;
        final float inclination = getInclination(gs) - 10;
        //Calculate the x coordinate of the eye point relative to the center point.
        final double xEyeLocal = Math.cos(azimuth) * Math.cos(inclination) * gs.vDist;
        //Calculate the y coordinate of the eye point relative to the center point.
        final double yEyeLocal = Math.sin(azimuth) * Math.cos(inclination) * gs.vDist;
        //Calculate the z coordinate of the eye point relative to the center point.
        final double zEyeLocal = Math.sin(inclination) * gs.vDist;
        //Create a new vector with the local eye coördinates, IE relative to the center point.
        final Vector localSun = new Vector(xEyeLocal, yEyeLocal, zEyeLocal);
        //Add the relative offet of the center point to the newly calculated coordinates of the eye point.
        final Vector worldSun = localSun.add(gs.cnt);
        this.lightPos[0] = (float) worldSun.x();
        this.lightPos[1] = (float) worldSun.y();
        this.lightPos[2] = (float) worldSun.z();
        this.lightPos[3] = 0;//Makes it infinite.
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

    public void setColor(GL2 gl, float red, float green, float blue, float alpha) {
        gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, new float[]{red, green, blue, alpha}, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, COLOUR_BLACK, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, 0);
    }

}
