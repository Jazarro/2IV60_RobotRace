/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

import javax.media.opengl.GL2;

import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;

/**
 *
 * @author Arjan Boschman
 */
public class Lighting {

    public void initialize(GL2 gl) {
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);

//        float[] ambientLight = {0.1f, 0.f, 0.f, 0f};  // weak RED ambient 
//        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
//
//        float[] diffuseLight = {1f, 2f, 1f, 0f};  // multicolor diffuse 
//        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight, 0);
        //
        //
        float[] ambientLight = {1f, 0.f, 0.f, 0f};  // weak RED ambient 
        gl.glLightfv(GL_LIGHT0, GL_POSITION, ambientLight, 0);
        
    }

    public void setView(GL2 gl) {

    }

    public void drawScene(GL2 gl) {
        
    }
    
}
