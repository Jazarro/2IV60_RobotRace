/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

import bodies.BufferManager;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LESS;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_NORMALIZE;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import racetrack.RaceTrack;
import racetrack.RaceTrackDefinition;
import racetrack.RaceTrackFactory;
import robot.Robot;
import robot.RobotFactory;

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

    private static final int DEFAULT_STACKS = 25;
    private static final int DEFAULT_SLICES = 25;
    private static final int NUMBER_ROBOTS = 4;

    /**
     * Main program execution body, delegates to an instance of the RobotRace
     * implementation.
     *
     * @param args
     */
    public static void main(String args[]) {
        final RobotRace robotRace = new RobotRace();
        robotRace.run();
    }

    private final BufferManager bodyManager = new BufferManager();
    private final Camera camera = new Camera();
    private final Terrain terrain = new TestingTerrain();
    private final RobotFactory robotFactory = new RobotFactory();
    private final RaceTrackFactory raceTrackFactory = new RaceTrackFactory();
    private final Lighting lighting = new Lighting();
    private final Robot[] robots;
    private final RaceTrack[] raceTracks;

    private double tPrevious = 0d;

    /**
     * Constructs this robot race by initializing robots, camera, track, and
     * terrain.
     */
    public RobotRace() {
        this.robots = new Robot[NUMBER_ROBOTS];
        this.raceTracks = new RaceTrack[5];
        setupObjects();
    }

    private void setupObjects() {
        robots[0] = robotFactory.makeBenderAt(Material.GOLD, new Vector(0, 0, 0), Vector.X);//Just for testing.
        robots[1] = robotFactory.makeBenderAt(Material.SILVER, new Vector(-10, 5, 0), Vector.X);
        robots[2] = robotFactory.makeBenderAt(Material.WOOD, new Vector(-11, 5, 0), Vector.X);
        robots[3] = robotFactory.makeBenderAt(Material.PLASTIC_ORANGE, new Vector(-12, 5, 0), Vector.X);
//        robots[0] = factory.makeBenderAt(Material.GOLD, new Vector(-1, -3, 0), Vector.X);
//        robots[1] = factory.makeBenderAt(Material.SILVER, new Vector(-1, -1, 0), Vector.X);
//        robots[2] = factory.makeBenderAt(Material.WOOD, new Vector(-1, 1, 0), Vector.X);
//        robots[3] = factory.makeBenderAt(Material.PLASTIC_ORANGE, new Vector(-1, 3, 0), Vector.X);

        robots[0].setSpeed(0.005d);
        robots[1].setSpeed(0.005d);
        robots[2].setSpeed(0.005d);
        robots[3].setSpeed(0.005d);

        // Test track
        //raceTracks[0] = new RaceTrack();
        raceTracks[0] = raceTrackFactory.makeTestRaceTrack();
        raceTracks[1] = raceTrackFactory.makeTestElevatedRaceTrack();

        // O-track
        /*raceTracks[1] = new RaceTrack(new Vector[]{ /* add control points like:
         new Vector(10, 0, 1), new Vector(10, 5, 1), new Vector(5, 10, 1),
         new Vector(..., ..., ...), ...
         });*/
        // L-track
        //raceTracks[2] = new RaceTrack(new Vector[]{ /* add control points */});
        // C-track
        //raceTracks[3] = new RaceTrack(new Vector[]{ /* add control points */});
        // Custom track
        //raceTracks[4] = new RaceTrack(new Vector[]{ /* add control points */});
    }

    /**
     * Called upon the start of the application. Primarily used to configure
     * OpenGL.
     */
    @Override
    public void initialize() {
        final BufferManager.Initialiser bmInitialiser = bodyManager.makeInitialiser(gl);
        lighting.initialize(gl, gs);

        // Enable blending.//todo check if it's better with this on.
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);

        // Normalize normals.
        gl.glEnable(GL_NORMALIZE);

        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glBindTexture(GL_TEXTURE_2D, 0);

        robotFactory.initialize(gl, bmInitialiser);
        raceTrackFactory.initialize(gl, bmInitialiser);

        terrain.initialize();

        // Try to load four textures, add more if you like.
        track = loadTexture("track.jpg");
        brick = loadTexture("brick.jpg");
        head = loadTexture("head.jpg");
        torso = loadTexture("torso.jpg");
        bmInitialiser.finish();
    }

    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        lighting.setView(gl);
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);

        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);

        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        //Load the identity matrix.
        gl.glLoadIdentity();

        camera.setPerspective(glu, gs);

        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        //Load the identity matrix.
        gl.glLoadIdentity();

        camera.setLookAt(glu);

        tempScrollVarPrev = tempScrollVar;//TODO: remove temp code.
        tempScrollVar = gs.vDist;//TODO: remove temp code.
    }

    private float tempScrollVar = 10F;//TODO: remove temp code.
    private float tempScrollVarPrev = 10F;//TODO: remove temp code.

    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        bodyManager.startDraw(gl);
        lighting.drawScene(gl);

        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        // Clear background and depth buffer.
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }

        //Draw the robots.
        for (int i = 0; i < robots.length; i++) {
            final Robot robot = robots[i];
            lighting.setMaterial(gl, robot.getMaterial());
            robot.setPosition(RaceTrackDefinition.getLanePoint(robot.getTrackTime(), raceTracks[gs.trackNr], i));
            robot.setDirection(RaceTrackDefinition.getLaneTangent(robot.getTrackTime(), raceTracks[gs.trackNr], i));
            final double laneDistance = RaceTrackDefinition.getLaneDistance(gs.tAnim, tPrevious, raceTracks[gs.trackNr], i);
            robot.moveDistance(RaceTrack.SLICE_COUNT * (gs.tAnim - tPrevious), laneDistance);

            {//TODO: remove temp code.
//                if (Math.abs(tempScrollVarPrev - tempScrollVar) > 0.1F) {
//                    robot.getRobotBody().playAnimation(AnimationType.RUNNING, 2);
//                }
//                if (gs.tAnim <= 0) {
//                    robot.setPosition(Vector.O);
//                }
//                final float velocity = 1.5F;
//                robot.setPosition(new Vector(gs.tAnim * velocity, 0, 0));
//                robot.moveDistance(gs.tAnim - tPrevious, (gs.tAnim - tPrevious) * velocity);
            }
            
            robot.draw(gl, glu, glut, gs.showStick, gs.tAnim);
        }

        // Draw the race track.
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        // Draw the terrain.
        terrain.draw(gl, glu, glut, lighting);

        bodyManager.endDraw(gl);
        tPrevious = gs.tAnim;
    }

    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue), and origin
     * (yellow).
     */
    public void drawAxisFrame() {
        //The radius of the sphere that sits at the origin.
        final float originSphereRadius = 0.05f;
        //Sets the color to yellow.
        lighting.setColor(gl, 1f, 1f, 0f, 1f);
        //Draw a yellow sphere at the origin.
        glut.glutSolidSphere(originSphereRadius, DEFAULT_SLICES, DEFAULT_STACKS);

        //Draw beams and cones along the x, y and z axes, respectively.
        drawAxis(1f, 0f, 0f);
        drawAxis(0f, 1f, 0f);
        drawAxis(0f, 0f, 1f);

        //Reset the color back to black.
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
        lighting.setColor(gl, x, y, z, 1f);

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
}
