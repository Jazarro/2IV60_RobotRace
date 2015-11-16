package robotrace;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LESS;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_NORMALIZE;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Handles all of the RobotRace graphics functionality, which should be extended
 * per the assignment.
 *
 * OpenGL functionality: - Basic commands are called via the gl object; -
 * Utility commands are called via the glu and glut objects;
 *
 * GlobalState: The gs object contains the GlobalState as described in the
 * assignment: - The camera viewpoint angles, phi and theta, are changed
 * interactively by holding the left mouse button and dragging; - The camera
 * view width, vWidth, is changed interactively by holding the right mouse
 * button and dragging upwards or downwards; - The center point can be moved up
 * and down by pressing the 'q' and 'z' keys, forwards and backwards with the
 * 'w' and 's' keys, and left and right with the 'a' and 'd' keys; - Other
 * settings are changed via the menus at the top of the screen.
 *
 * Textures: Place your "track.jpg", "brick.jpg", "head.jpg", and "torso.jpg"
 * files in the same folder as this file. These will then be loaded as the
 * texture objects track, bricks, head, and torso respectively. Be aware, these
 * objects are already defined and cannot be used for other purposes. The
 * texture objects can be used as follows:
 *
 * gl.glColor3f(1f, 1f, 1f); track.bind(gl); gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0); gl.glVertex3d(0, 0, 0); gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0); gl.glTexCoord2d(1, 1); gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1); gl.glVertex3d(0, 1, 0); gl.glEnd();
 *
 * Note that it is hard or impossible to texture objects drawn with GLUT. Either
 * define the primitives of the object yourself (as seen above) or add
 * additional textured primitives to the GLUT object.
 */
public class RobotRace extends Base {

    private static final int DEFAULT_STACKS = 10;
    private static final int DEFAULT_SLICES = 10;

    /**
     * Array of the four robots.
     */
    private final Robot[] robots;

    /**
     * Instance of the camera.
     */
    private final Camera camera;

    /**
     * Instance of the race track.
     */
    private final RaceTrack[] raceTracks;

    /**
     * Instance of the terrain.
     */
    private final Terrain terrain;

    /**
     * Constructs this robot race by initializing robots, camera, track, and
     * terrain.
     */
    public RobotRace() {

        // Create a new array of four robots
        robots = new Robot[4];

        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
        /* add other parameters that characterize this robot */);

        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
        /* add other parameters that characterize this robot */);

        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
        /* add other parameters that characterize this robot */);

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
        /* add other parameters that characterize this robot */);

        // Initialize the camera
        camera = new Camera();

        // Initialize the race tracks
        raceTracks = new RaceTrack[5];

        // Test track
        raceTracks[0] = new RaceTrack();

        // O-track
        raceTracks[1] = new RaceTrack(new Vector[]{ /* add control points like:
         new Vector(10, 0, 1), new Vector(10, 5, 1), new Vector(5, 10, 1),
         new Vector(..., ..., ...), ...
         */});

        // L-track
        raceTracks[2] = new RaceTrack(new Vector[]{ /* add control points */});

        // C-track
        raceTracks[3] = new RaceTrack(new Vector[]{ /* add control points */});

        // Custom track
        raceTracks[4] = new RaceTrack(new Vector[]{ /* add control points */});

        // Initialize the terrain
        terrain = new Terrain();
    }

    /**
     * Called upon the start of the application. Primarily used to configure
     * OpenGL.
     */
    @Override
    public void initialize() {

        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);

        // Normalize normals.
        gl.glEnable(GL_NORMALIZE);

        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glBindTexture(GL_TEXTURE_2D, 0);

        // Try to load four textures, add more if you like.
        track = loadTexture("track.jpg");
        brick = loadTexture("brick.jpg");
        head = loadTexture("head.jpg");
        torso = loadTexture("torso.jpg");
    }

    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);

        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        // Modify this to meet the requirements in the assignment.
        glu.gluPerspective(40, (float) gs.w / (float) gs.h, 0.1, 100);

        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();

        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(), camera.eye.y(), camera.eye.z(),
                camera.center.x(), camera.center.y(), camera.center.z(),
                camera.up.x(), camera.up.y(), camera.up.z());
    }

    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);

        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);

        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);

        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }

        // Get the position and direction of the first robot.
        robots[0].position = raceTracks[gs.trackNr].getLanePoint(0, 0);
        robots[0].direction = raceTracks[gs.trackNr].getLaneTangent(0, 0);

        // Draw the first robot.
        robots[0].draw(gl, glu, glut, false, gs.tAnim);

        // Draw the race track.
        raceTracks[gs.trackNr].draw(gl, glu, glut);

        // Draw the terrain.
        terrain.draw(gl, glu, glut);

        // Unit box around origin.
        glut.glutWireCube(1f);

        // Move in x-direction.
        gl.glTranslatef(2f, 0f, 0f);

        // Rotate 30 degrees, around z-axis.
        gl.glRotatef(30f, 0f, 0f, 1f);

        // Scale in z-direction.
        gl.glScalef(1f, 1f, 2f);

        // Translated, rotated, scaled box.
        glut.glutWireCube(1f);
    }

    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue), and origin
     * (yellow).
     */
    public void drawAxisFrame() {
        //The radius of the sphere that sits at the origin.
        final float originSphereRadius = 0.05f;
        //Sets the color to yellow.
        gl.glColor3f(1f, 1f, 0f);
        //Draw a yellow sphere at the origin.
        glut.glutSolidSphere(originSphereRadius, DEFAULT_SLICES, DEFAULT_STACKS);

        //Draw beams and cones along the x, y and z axes, respectively.
        drawAxis(1f, 0f, 0f);
        drawAxis(0f, 1f, 0f);
        drawAxis(0f, 0f, 1f);

        //Reset the color back to black.ß
        gl.glColor3f(0f, 0f, 0f);
    }

    private void drawAxis(float x, float y, float z) {
        //The width of the axis beams' short edges.
        final float axisThickness = 0.01f;
        //The height of the cone topping the axis.
        final float coneHeight = 0.1f;
        //The length of axis beams' long edge.
        final float axisLength = 1 - coneHeight;
        //Sets the color relative to the axis being drawn. (x=red,y=green,z=blue)
        gl.glColor3f(x, y, z);

        //Store the current matrix.
        gl.glPushMatrix();
        //Translate the cone to the correct position, depending on the axis being drawn.
        gl.glTranslatef(x * axisLength, y * axisLength, z * axisLength);
        /**
         * Rotate the cone so that it points along the axis. The cone is rotated
         * around the axis perpendicular to the one being drawn. The z-cone is
         * already rotated correctly, so it is not rotated.
         */
        gl.glRotatef(z == 1 ? 0 : 90, -y, x, 0);
        //Draw the cone, make base five times wider than the axis beam.
        glut.glutSolidCone(axisThickness * 5, coneHeight, DEFAULT_SLICES, DEFAULT_STACKS);
        //Restore the original matrix.
        gl.glPopMatrix();

        //Store the current matrix.
        gl.glPushMatrix();
        //Translate the beam half its length into the direction of its axis.
        gl.glTranslatef(axisLength / 2 * x,
                axisLength / 2 * y,
                axisLength / 2 * z);
        //Stretch the beam along its axis to make it fit the previously defined axisLength.
        gl.glScalef(x == 0 ? 1f : axisLength / axisThickness,
                y == 0 ? 1f : axisLength / axisThickness,
                z == 0 ? 1f : axisLength / axisThickness);
        //Draw the beam.
        glut.glutSolidCube(axisThickness);
        //Restore the original matrix.
        gl.glPopMatrix();
    }

    /**
     * Main program execution body, delegates to an instance of the RobotRace
     * implementation.
     *
     * @param args
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
}
