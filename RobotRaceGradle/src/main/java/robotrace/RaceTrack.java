/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package robotrace;

import bodies.assembly.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.util.gl2.GLUT;
import java.nio.*;
import java.util.*;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.swing.event.*;
import robotrace.Vector;

/**
 Implementation of a race track that is made from Bezier segments.
 */
class RaceTrack{

    private static final double STEP_T = 0.01;
    private static final double LANE_WIDTH = 1.22;
    private static final int LANE_COUNT = 4;
    
    private DoubleBuffer data;
    private List<IntBuffer> indices;

    /**
     The width of one lane. The total width of the track is 4 * laneWidth.
     */
    //private final static float laneWidth = 1.22f;
    /**
     Array with 3N control points, where N is the number of segments.
     */
    private Vector[] controlPoints = null;

    /**
     Constructor for the default track.
     */
    public RaceTrack(){
    }

    /**
     Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints){
        this.controlPoints = controlPoints;
    }

    /**
     Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut){
        if(controlPoints == null){
            // draw the test track

        }
        else{
            // draw the spline track
        }
    }

    /**
     Returns the center of a lane at 0 <= t < 1. Use this method to find the
     position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){
        if(controlPoints == null){
            return Vector.O; // <- code goes here
        }
        else{
            return Vector.O; // <- code goes here
        }
    }

    /**
     Returns the tangent of a lane at 0 <= t < 1. Use this method to find the
     orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){
        if(controlPoints == null){
            return Vector.O; // <- code goes here
        }
        else{
            return Vector.O; // <- code goes here
        }
    }

    /**
     Returns a point on the test track at 0 <= t < 1.
     */
    private Vector getPoint(double t){
        return new Vector(10 * Math.cos(2d * Math.PI * t), 14 * Math.sin(2d * Math.PI * t), 1);
    }

    /**
     Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t){
        return new Vector(10 * Math.sin(2d * Math.PI * t), 14 * Math.cos(2d * Math.PI * t), 1);
    }

    /**
     Returns a point on a bezier segment with control points P0, P1, P2, P3 at
     0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
                                       Vector P2, Vector P3){
        return Vector.O; // <- code goes here
    }

    /**
     Returns a tangent on a bezier segment with control points P0, P1, P2, P3
     at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
                                         Vector P2, Vector P3){
        return Vector.O; // <- code goes here
    }

    private void calcTestTrack(){
        List<Vertex> vertices = new ArrayList<>();
        for(double t = 0; t < 1; t += STEP_T){
            vertices.add(new Vertex(getPoint(t)));
        }
        for(Vertex vertex : vertices){
            Vertex previous, next;
            if(vertices.indexOf(vertex) == 0){
                previous = vertices.get(vertices.size() - 1);
                next = vertices.get(vertices.indexOf(vertex) + 1);
            }
            else{
                if(vertices.indexOf(vertex) == (vertices.size() - 1)){
                    previous = vertices.get(vertices.indexOf(vertex) - 1);
                    next = vertices.get(0);
                }
                else{
                    previous = vertices.get(vertices.indexOf(vertex) - 1);
                    next = vertices.get(vertices.indexOf(vertex) + 1);
                }
            }
            vertices = new ArrayList<>();
            final double halfTrackWidth = LANE_WIDTH * LANE_COUNT / 2d;
            final Vector delta1 = next.getPositionV().subtract(vertex.getPositionV());
            final Vector normal1 = new Vector(-delta1.y, delta1.x, delta1.z).normalized();
            final Vector delta2 = vertex.getPositionV().subtract(previous.getPositionV());
            final Vector normal2 = new Vector(-delta2.y, delta2.x, delta2.z).normalized();
            final Vector outerNormal = normal1.add(normal2).normalized();
            final Vector innerNormal = outerNormal.scale(-1d);
            //Top Outer
            vertices.add(new Vertex(vertex.getPositionV().add(outerNormal.scale(halfTrackWidth)), outerNormal));
            //Top Inner
            vertices.add(new Vertex(vertex.getPositionV().add(innerNormal.scale(halfTrackWidth)), innerNormal));
            vertex.setPositionC(vertex.getPositionV().x(), vertex.getPositionV().y(), vertex.getPositionV().z() - 1d);
            //Bottom Outer
            vertices.add(new Vertex(vertex.getPositionV().add(outerNormal.scale(halfTrackWidth)), outerNormal));
            //Bottom Inner
            vertices.add(new Vertex(vertex.getPositionV().add(innerNormal.scale(halfTrackWidth)), innerNormal));
        }
        final List<Integer> top = new ArrayList<>();
        final List<Integer> bottom = new ArrayList<>();
        final List<Integer> inner = new ArrayList<>();
        final List<Integer> outer = new ArrayList<>();
        for(int i = 0; i < vertices.size() / 4; i++){
            top.add((i * 4) + 0);
            top.add((i * 4) + 1);
            bottom.add((i * 4) + 2);
            bottom.add((i * 4) + 3);
            inner.add((i * 4) + 1);
            inner.add((i * 4) + 3);
            outer.add((i * 4) + 0);
            outer.add((i * 4) + 2);
        }
        //TODO: Convert top, bottom, inner, outer, vertices to IntBuffer, DoubleBuffer
    }
}
