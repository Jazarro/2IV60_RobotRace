/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robot;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import racetrack.RaceTrack;
import robotrace.Lighting;
import robotrace.Material;
import robotrace.Vector;
import terrain.Terrain;

/**
 * Represents a Robot, to be implemented according to the Assignments.
 *
 * @author Arjan Boschman
 */
public class Robot {

    /**
     * The position of the robot.
     */
    private Vector position = Vector.O;
    /**
     * The direction in which the robot is facing.
     */
    private Vector direction = Vector.Y;

    private double speed = 1d;
    private double trackT = 0d;
    private double distanceTravelled = 0d;
    private int laneNumber = 0;

    /**
     * The material from which this robot is built.
     */
    private final Material material;
    /**
     * The object in charge of drawing the robot's actual physical body.
     */
    private final RobotBody robotBody;

    /**
     * Constructs a new instance of robot.
     *
     * @param material    The material that the robot is to be made of.
     * @param robotBody   The aesthetics of the body used by this robot.
     */
    public Robot(Material material, RobotBody robotBody) {
        this.material = material;
        this.robotBody = robotBody;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setPositionOnTrack(RaceTrack raceTrack) {
        this.position = raceTrack.getLanePoint(trackT, laneNumber);
    }

    public Vector getPosition() {
        return position;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public void setDirectionOnTrack(RaceTrack raceTrack) {
        this.direction = raceTrack.getLaneTangent(trackT, laneNumber);
    }

    public Vector getDirection() {
        return direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCurrentSpeed() {
        return speed * getGravityDrag();
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void resetDistance() {
        this.trackT = 0d;
        this.distanceTravelled = 0d;
    }

    public void moveDistance(RaceTrack raceTrack, double deltaTime) {
        distanceTravelled += deltaTime * getCurrentSpeed();
        final double newTrackT = raceTrack.getLaneT(distanceTravelled, laneNumber);
        if (newTrackT < (trackT - Math.floor(trackT))) {
            trackT = Math.floor(trackT) + 1d + newTrackT;
        } else {
            trackT = Math.floor(trackT) + newTrackT;
        }
    }

    private double getGravityDrag() {
        final double zInclination = direction.normalized().dot(Vector.Z);
        final double gravity = Math.pow(2d, Math.pow(zInclination, 2d) * 4d); //TODO: Find nice value!
        return (zInclination < 0d) ? (gravity) : (1d / gravity);
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public double getTrackT() {
        return trackT;
    }

    public int getLapsCompleted() {
        return (int) Math.floor(trackT);
    }

    public Material getMaterial() {
        return material;
    }

    public RobotBody getRobotBody() {
        return robotBody;
    }

    public void update(RaceTrack raceTrack, double deltaTime) {
        moveDistance(raceTrack, deltaTime);
        setPositionOnTrack(raceTrack);
        setDirectionOnTrack(raceTrack);
    }

    /**
     * Draws this robot on the screen.
     *
     * @param gl          The instance of GL2 responsible for drawing the robot
     *                    on the screen.
     * @param glut        An instance of GLUT to optionally aid in drawing the
     *                    robot body.
     * @param stickFigure If true, the robot must draw itself as a stick figure
     *                    rather than a solid body.
     * @param tAnim       Time since the start of the animation in seconds.
     * @param lighting    The Lighting instance responsible for calculating the
     *                    lighting in this scene. Can be used to set the color
     *                    of bodies before drawing them.
     */
    public void draw(GL2 gl, GLUT glut, boolean stickFigure, float tAnim, Lighting lighting) {
        lighting.setMaterial(gl, getMaterial());
        gl.glPushMatrix();
        {
            gl.glTranslated(position.x(), position.y(), position.z() + Terrain.TERRAIN_LEVEL);
            final double rotationDotY = direction.dot(Vector.Y) / (direction.length() * Vector.Y.length());
            final double rotationDotX = direction.dot(Vector.X) / (direction.length() * Vector.X.length());
            final double rotationAngle = Math.toDegrees(Math.acos(rotationDotY));
            gl.glRotated((rotationDotX > 0d) ? (-rotationAngle) : (rotationAngle), Vector.Z.x(), Vector.Z.y(), Vector.Z.z());
            final double elevationDot = direction.dot(Vector.Z) / (direction.length() * Vector.Z.length());
            final double elevationAngle = Math.toDegrees(Math.asin(elevationDot));
            gl.glRotated(elevationAngle, Vector.X.x(), Vector.X.y(), Vector.X.z());
            robotBody.draw(gl, glut, stickFigure, tAnim);
        }
        gl.glPopMatrix();
    }

}
