package robotrace.bender;

import static java.lang.Math.*;
import java.nio.*;
import java.util.*;
import javax.media.opengl.*;
import utility.*;

public class Limb{

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int LEG = 0;
    public static final int ARM = 1;
    public static final int RING_COUNT = 6;

    private static final int FINGER_COUNT = 3;
    private static final double FINGER_OFFCENTER = 0.03d;

    private static final double HEIGHT_RING = 0.5d / 6d;
    private static final double HEIGHT_FOOT = 0.1d;
    private static final double HEIGHT_HAND = 0.07d;
    private static final double HEIGHT_FINGER = 0.0625d;

    private static final double RADIUS_RING = 0.04d;
    private static final double RADIUS_FOOT = Math.sqrt(Math.pow(HEIGHT_FOOT, 2d) + Math.pow(RADIUS_RING, 2d));
    private static final double RADIUS_HAND = 0.06d;
    private static final double RADIUS_FINGER = HEIGHT_FINGER - 0.05d;

    private static final int SLICE_COUNT = 50;
    private static final int STACK_COUNT = 20;

    private int ringGLDataBufferName;
    private int[] ringGLIndicesBufferNames;
    DoubleBuffer ringDataBuffer;
    List<IntBuffer> ringIndicesBufferList;
    List<Boolean> ringSurfaceTypeList;

    private int footGLDataBufferName;
    private int[] footGLIndicesBufferNames;
    DoubleBuffer footDataBuffer;
    List<IntBuffer> footIndicesBufferList;
    List<Boolean> footSurfaceTypeList;

    private int handGLDataBufferName;
    private int[] handGLIndicesBufferNames;
    DoubleBuffer handDataBuffer;
    List<IntBuffer> handIndicesBufferList;
    List<Boolean> handSurfaceTypeList;

    private int fingerGLDataBufferName;
    private int[] fingerGLIndicesBufferNames;
    DoubleBuffer fingerDataBuffer;
    List<IntBuffer> fingerIndicesBufferList;
    List<Boolean> fingerSurfaceTypeList;

    public void initialize(GL2 gl){//todo: refactor heavily
        final Assembler ringAssembler = new Assembler();
        ringAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_RING, RADIUS_RING, 0d, -HEIGHT_RING, true, true);
        ringAssembler.compileSurfaceCompilation();
        ringDataBuffer = ringAssembler.getDataBuffer();
        ringIndicesBufferList = ringAssembler.getIndicesBuffers();
        ringSurfaceTypeList = ringAssembler.getSurfaceTypeList();

        final Assembler footAssembler = new Assembler();
        footAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_FOOT, 0d, 0d, HEIGHT_FOOT, true, false);
        footAssembler.compileSurfaceCompilation();
        footDataBuffer = footAssembler.getDataBuffer();
        footIndicesBufferList = footAssembler.getIndicesBuffers();
        footSurfaceTypeList = footAssembler.getSurfaceTypeList();

        final Assembler handAssembler = new Assembler();
        handAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_RING, RADIUS_HAND, 0d, -HEIGHT_HAND, true, true);
        handAssembler.compileSurfaceCompilation();
        handDataBuffer = handAssembler.getDataBuffer();
        handIndicesBufferList = handAssembler.getIndicesBuffers();
        handSurfaceTypeList = handAssembler.getSurfaceTypeList();

        final Assembler fingerAssembler = new Assembler();
        fingerAssembler.addConicalFrustum(SLICE_COUNT, RADIUS_FINGER, RADIUS_FINGER, RADIUS_FINGER, -HEIGHT_FINGER + RADIUS_FINGER, false, false);
        fingerAssembler.addPartialTorus(SLICE_COUNT, STACK_COUNT, RADIUS_FINGER, 0d, -HEIGHT_FINGER + RADIUS_FINGER, -HEIGHT_FINGER, false, false);
        fingerAssembler.compileSurfaceCompilation();
        fingerDataBuffer = fingerAssembler.getDataBuffer();
        fingerIndicesBufferList = fingerAssembler.getIndicesBuffers();
        fingerSurfaceTypeList = fingerAssembler.getSurfaceTypeList();

        final int[] ringTempBufferNames = new int[ringIndicesBufferList.size() + 1];
        gl.glGenBuffers(ringTempBufferNames.length, ringTempBufferNames, 0);
        this.ringGLIndicesBufferNames = new int[ringIndicesBufferList.size()];
        System.arraycopy(ringTempBufferNames, 0, ringGLIndicesBufferNames, 0, ringGLIndicesBufferNames.length);
        this.ringGLDataBufferName = ringTempBufferNames[ringTempBufferNames.length - 1];

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, ringGLDataBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, ringDataBuffer.capacity() * Double.BYTES, ringDataBuffer, GL2.GL_STATIC_DRAW);

        for(IntBuffer buffer : ringIndicesBufferList){
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, ringGLIndicesBufferNames[ringIndicesBufferList.indexOf(buffer)]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * Integer.BYTES, buffer, GL2.GL_STATIC_DRAW);
        }

        final int[] footTempBufferNames = new int[footIndicesBufferList.size() + 1];
        gl.glGenBuffers(footTempBufferNames.length, footTempBufferNames, 0);
        this.footGLIndicesBufferNames = new int[footIndicesBufferList.size()];
        System.arraycopy(footTempBufferNames, 0, footGLIndicesBufferNames, 0, footGLIndicesBufferNames.length);
        this.footGLDataBufferName = footTempBufferNames[footTempBufferNames.length - 1];

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, footGLDataBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, footDataBuffer.capacity() * Double.BYTES, footDataBuffer, GL2.GL_STATIC_DRAW);

        for(IntBuffer buffer : footIndicesBufferList){
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, footGLIndicesBufferNames[footIndicesBufferList.indexOf(buffer)]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * Integer.BYTES, buffer, GL2.GL_STATIC_DRAW);
        }

        final int[] handTempBufferNames = new int[handIndicesBufferList.size() + 1];
        gl.glGenBuffers(handTempBufferNames.length, handTempBufferNames, 0);
        this.handGLIndicesBufferNames = new int[handIndicesBufferList.size()];
        System.arraycopy(handTempBufferNames, 0, handGLIndicesBufferNames, 0, handGLIndicesBufferNames.length);
        this.handGLDataBufferName = handTempBufferNames[handTempBufferNames.length - 1];

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, handGLDataBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, handDataBuffer.capacity() * Double.BYTES, handDataBuffer, GL2.GL_STATIC_DRAW);

        for(IntBuffer buffer : handIndicesBufferList){
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, handGLIndicesBufferNames[handIndicesBufferList.indexOf(buffer)]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * Integer.BYTES, buffer, GL2.GL_STATIC_DRAW);
        }

        final int[] fingerTempBufferNames = new int[fingerIndicesBufferList.size() + 1];
        gl.glGenBuffers(fingerTempBufferNames.length, fingerTempBufferNames, 0);
        this.fingerGLIndicesBufferNames = new int[fingerIndicesBufferList.size()];
        System.arraycopy(fingerTempBufferNames, 0, fingerGLIndicesBufferNames, 0, fingerGLIndicesBufferNames.length);
        this.fingerGLDataBufferName = fingerTempBufferNames[fingerTempBufferNames.length - 1];

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, fingerGLDataBufferName);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, fingerDataBuffer.capacity() * Double.BYTES, fingerDataBuffer, GL2.GL_STATIC_DRAW);

        for(IntBuffer buffer : fingerIndicesBufferList){
            gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, fingerGLIndicesBufferNames[fingerIndicesBufferList.indexOf(buffer)]);
            gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, buffer.capacity() * Integer.BYTES, buffer, GL2.GL_STATIC_DRAW);
        }

    }

    public void draw(GL2 gl, double[] anglesAxis, double[] anglesBend, int type, int side){
        double currAngleAxis;
        double currAngleBend;
        double newPos[] = {0d, 0d, 0d};
        gl.glPushMatrix();
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, ringGLDataBufferName);
        gl.glVertexPointer(3, GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, 0);
        gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, Vertex.COORD_COUNT * Double.BYTES);
        for(int i = 0; i < RING_COUNT; i++){
            gl.glPushMatrix();
            currAngleAxis = (side == RIGHT) ? (-anglesAxis[i]) : (anglesAxis[i]);
            currAngleBend = anglesBend[i];
            gl.glTranslated(newPos[0], newPos[1], newPos[2]);
            gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);
            newPos = nextPos(newPos, HEIGHT_RING, currAngleBend, currAngleAxis);
            for(int j = 0; j < ringIndicesBufferList.size(); j++){
                ringDrawBuffer(gl, j);
            }
            gl.glPopMatrix();
        }
        gl.glPushMatrix();
        currAngleAxis = (side == RIGHT) ? (-anglesAxis[RING_COUNT]) : (anglesAxis[RING_COUNT]);
        currAngleBend = anglesBend[RING_COUNT];
        switch(type){
            case LEG:
                newPos = nextPos(newPos, HEIGHT_FOOT, currAngleBend, currAngleAxis);
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);

                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, footGLDataBufferName);
                gl.glVertexPointer(3, GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, 0);
                gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, Vertex.COORD_COUNT * Double.BYTES);
                for(int i = 0; i < footIndicesBufferList.size(); i++){
                    footDrawBuffer(gl, i);
                }

                break;
            case ARM:
                gl.glTranslated(newPos[0], newPos[1], newPos[2]);
                gl.glRotated(currAngleBend, Math.cos(Math.toRadians(currAngleAxis)), Math.sin(Math.toRadians(currAngleAxis)), 0d);

                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, handGLDataBufferName);
                gl.glVertexPointer(3, GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, 0);
                gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, Vertex.COORD_COUNT * Double.BYTES);
                for(int i = 0; i < handIndicesBufferList.size(); i++){
                    handDrawBuffer(gl, i);
                }

                gl.glTranslated(0d, 0d, -HEIGHT_HAND);

                for(int j = 0; j < FINGER_COUNT; j++){
                    //todo: add FINGER_PHASE
                    //todo: add fingerAngle
                    gl.glPushMatrix();
                    gl.glTranslated(FINGER_OFFCENTER * cos(toRadians(j * 360 / FINGER_COUNT)), FINGER_OFFCENTER * sin(toRadians(j * 360 / FINGER_COUNT)), 0d);
                    gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, fingerGLDataBufferName);
                    gl.glVertexPointer(3, GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, 0);
                    gl.glNormalPointer(GL2.GL_DOUBLE, 2 * Vertex.COORD_COUNT * Double.BYTES, Vertex.COORD_COUNT * Double.BYTES);
                    for(int i = 0; i < fingerIndicesBufferList.size(); i++){
                        fingerDrawBuffer(gl, i);
                    }
                    gl.glPopMatrix();
                }

                break;
        }
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    public double height(double[] anglesAxis, double[] anglesBend, int type, int side){
        double currAngleAxis;
        double currAngleBend;
        double newPos[] = {0d, 0d, 0d};
        for(int i = 0; i < RING_COUNT; i++){
            currAngleAxis = (side == RIGHT) ? (-anglesAxis[i]) : (anglesAxis[i]);
            currAngleBend = anglesBend[i];
            newPos = nextPos(newPos, HEIGHT_RING, currAngleBend, currAngleAxis);
        }
        currAngleAxis = (side == RIGHT) ? (-anglesAxis[RING_COUNT]) : (anglesAxis[RING_COUNT]);
        currAngleBend = anglesBend[RING_COUNT];
        switch(type){
            case LEG:
                newPos = nextPos(newPos, HEIGHT_FOOT, currAngleBend, currAngleAxis);
                newPos[2] -= RADIUS_FOOT * Math.sin(Math.toRadians(currAngleBend));
                break;
            case ARM://todo: arm does not return correct height
                //newPos[2] -= ?;
                break;
        }
        return Math.abs(newPos[2]);
    }

    //todo: fix relative angles?
    private double[] nextPos(double[] currPos, double height, double currAngleBend, double currAngleAxis){
        currPos[0] -= height * Math.sin(Math.toRadians(currAngleBend)) * Math.sin(Math.toRadians(currAngleAxis));
        currPos[1] += height * Math.sin(Math.toRadians(currAngleBend)) * Math.cos(Math.toRadians(currAngleAxis));
        currPos[2] -= height * Math.cos(Math.toRadians(currAngleBend));
        return currPos;
    }

    private void ringDrawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, ringGLIndicesBufferNames[buffInd]);
        gl.glDrawElements((ringSurfaceTypeList.get(buffInd) ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)), ringIndicesBufferList.get(buffInd).capacity(), GL2.GL_UNSIGNED_INT, 0);
    }

    private void footDrawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, footGLIndicesBufferNames[buffInd]);
        gl.glDrawElements((footSurfaceTypeList.get(buffInd) ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)), footIndicesBufferList.get(buffInd).capacity(), GL2.GL_UNSIGNED_INT, 0);
    }

    private void handDrawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, handGLIndicesBufferNames[buffInd]);
        gl.glDrawElements((handSurfaceTypeList.get(buffInd) ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)), handIndicesBufferList.get(buffInd).capacity(), GL2.GL_UNSIGNED_INT, 0);
    }

    private void fingerDrawBuffer(GL2 gl, int buffInd){
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, fingerGLIndicesBufferNames[buffInd]);
        gl.glDrawElements((fingerSurfaceTypeList.get(buffInd) ? (GL2.GL_POLYGON) : (GL2.GL_QUAD_STRIP)), fingerIndicesBufferList.get(buffInd).capacity(), GL2.GL_UNSIGNED_INT, 0);
    }
}
