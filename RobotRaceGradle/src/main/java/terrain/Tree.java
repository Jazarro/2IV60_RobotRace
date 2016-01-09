/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import robotrace.Lighting;
import robotrace.Vector;

/**
 *
 * @author Arjan Boschman
 */
public class Tree {

    private final Foliage foliage;
    private final Vector position;

    public Tree(Foliage foliage, Vector position) {
        this.foliage = foliage;
        this.position = position;
    }

    public void draw(GL2 gl, GLUT glut, Lighting lighting) {
        gl.glPushMatrix();
        gl.glTranslated(position.x(), position.y(), position.z());
        for (int i = 0; i < 10; i++) {
            drawOne(gl, glut, lighting, i);
        }
        gl.glPopMatrix();
    }

    private void drawOne(GL2 gl, GLUT glut, Lighting lighting, int i) {
        gl.glPushMatrix();
        gl.glTranslated(0, 0, i);
        foliage.drawBranch(gl, glut);
        gl.glTranslated(0, 0, 1);
        foliage.drawLeaf(gl, glut);
        gl.glPopMatrix();
    }
}
