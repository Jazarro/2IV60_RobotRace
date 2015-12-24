/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

import static javax.media.opengl.GL.GL_FRONT;
import javax.media.opengl.GL2;
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
 * Takes care of all lighting related activities.
 *
 * Right now this class just creates a single, infinitely far away light source.
 * (The sun)
 *
 * @author Arjan Boschman
 */
public class Lighting {

    /**
     * Length 4 float-array representing an all black, fully transparent colour.
     * All four values are zero.
     */
    private static final float[] COLOUR_OFF = new float[]{0, 0, 0, 0};
    /**
     * Value that, when passed as argument when setting the position of a light
     * source, will make the light source infinitely far away.
     */
    private static final int POS_INFINITE = 0;

    private final float[] lightPos = new float[4];
    /**
     * The diffuse component of the light source.
     */
    private final float[] diffuseLight = {1f, 1f, 1f, 1f};
    /**
     * The specular component of the light source.
     */
    private final float[] specularLight = {1f, 1f, 1f, 1f};
    /**
     * The ambient component of the light source.
     */
    private final float[] ambientLight = {0.1f, 0.1f, 0.1f, 1f};

    /**
     * Called during openGL initialisation. This enables the necessary settings
     * and sets up the light source.
     *
     * @param gl The GL2 instance responsible for drawing the scene.
     * @param gs The GlobalState.
     */
    public void initialize(GL2 gl, GlobalState gs) {
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        calculatePosition(gs);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
    }

    /**
     * According to the assignment, the light from the sun has to be shifted ten
     * degrees to the left and upwards with regards to the viewing direction.
     * Since the viewing direction is variable, we'll take this to mean the
     * initial direction.
     *
     * This method calculates the correct position of the sun and immediately
     * assigns it to the {@link #lightPos} field.
     *
     * @param gs The GlobalState, containing information on the initial position
     *           of the camera.
     */
    private void calculatePosition(GlobalState gs) {
        //Calculate the light position to be 10 degrees above and to the left of the starting eye point.
        final float azimuth = getAzimuth(gs) - 10;
        final float inclination = getInclination(gs) - 10;
        //Calculate the x coordinate of the sun point relative to the center point.
        final double xSunLocal = Math.cos(azimuth) * Math.cos(inclination) * gs.vDist;
        //Calculate the y coordinate of the sun point relative to the center point.
        final double ySunLocal = Math.sin(azimuth) * Math.cos(inclination) * gs.vDist;
        //Calculate the z coordinate of the sun point relative to the center point.
        final double zSunLocal = Math.sin(inclination) * gs.vDist;
        //Create a new vector with the local eye coördinates, IE relative to the center point.
        final Vector localSun = new Vector(xSunLocal, ySunLocal, zSunLocal);
        //Add the relative offet of the center point to the newly calculated coordinates of the sun point.
        final Vector worldSun = localSun.add(gs.cnt);
        this.lightPos[0] = (float) worldSun.x();
        this.lightPos[1] = (float) worldSun.y();
        this.lightPos[2] = (float) worldSun.z();
        this.lightPos[3] = POS_INFINITE;
    }

    /**
     * Called during the setView part of the loop. Reset the position of the
     * light source here, otherwise it will move with the camera.
     *
     * @param gl The GL2 instance responsible for drawing the scene.
     */
    public void setView(GL2 gl) {
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
    }

    public void drawScene(GL2 gl) {
        //Do nothing, for now.
    }

    /**
     * Sets the given material values on the given GL2 instance. This will set
     * the light reflective properties (and thus colour) for any object drawn
     * henceforth.
     *
     * @param gl       The GL2 instance responsible for drawing the scene.
     * @param material The material to set.
     *
     * @see #setColor Use this method to set colours without needing a Material.
     * Note that using glColor* has been turned off and doesn't work.
     */
    public void setMaterial(GL2 gl, Material material) {
        gl.glMaterialfv(GL_FRONT, GL_AMBIENT, material.ambient, 0);
        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, material.diffuse, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, material.specular, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, material.shininess);
    }

    /**
     * Sets the current colour on the given GL2 instance. This will set the
     * light reflective properties (and thus colour) for any object drawn
     * henceforth.
     *
     * The given colour will be used for both ambient and diffuse light.
     * Specular light will be turned off.
     *
     * @param gl    The GL2 instance responsible for drawing the scene.
     * @param red   The red component of the colour, between [0,1].
     * @param green The green component of the colour, between [0,1].
     * @param blue  The blue component of the colour, between [0,1].
     * @param alpha The alpha component of the colour, between [0,1].
     *
     * @see #setMaterial Use this alternative method to set the colour if you
     * want more fine grained control over the values. You will need a Material
     * constant though.
     */
    public void setColor(GL2 gl, float red, float green, float blue, float alpha) {
        gl.glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, new float[]{red, green, blue, alpha}, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, COLOUR_OFF, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, 0);
    }

}
