package robotrace;

import com.jogamp.opengl.util.gl2.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

/**
 Implementation of the terrain.
 */
class Terrain{

    /**
     Can be used to set up a display list.
     */
    public Terrain(){
        // code goes here ...
    }

    public void initialize(){

    }

    /**
     Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, Lighting lighting){
        // code goes here ...
    }

    /**
     Computes the elevation of the terrain at (x, y).
     */
    public float heightAt(float x, float y){
        return 0; // <- code goes here
    }
}
