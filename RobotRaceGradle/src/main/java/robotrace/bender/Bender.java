package robotrace.bender;

import javax.media.opengl.GL2;

public class Bender{

    public static final int NUMCOORD = 3;

    private final Body body;
    private final Limb limb;

    private final double[] leftLegAnglesInOut = new double[Limb.RING_COUNT + 1];
    private final double[] leftLegAnglesFrontBack = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesInOut = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesFrontBack = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesInOut = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesFrontBack = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesInOut = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesFrontBack = new double[Limb.RING_COUNT + 1];

    public static double[] calcCoord(int angleIndex, double radius, double height, int sliceCount){
        double coord[] = new double[3];
        coord[0] = radius * Math.cos(Math.toRadians(angleIndex * 360 / sliceCount));
        coord[1] = radius * Math.sin(Math.toRadians(angleIndex * 360 / sliceCount));
        coord[2] = height;
        return coord;
    }

    public Bender(){
        body = new Body();
        limb = new Limb();
    }

    public void initialize(GL2 gl){
        body.initialize(gl);
        limb.initialize(gl);

        setLeftLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        setRightLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        setLeftArmAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        setRightArmAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{45d, -45d, 45d, -45d, 45d, -45d, 45d});
        
        /*setLeftLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d});
        setRightLegAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d});
        setLeftArmAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d});
        setRightArmAngles(new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d}, new double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d});*/
    }

    public void draw(GL2 gl){
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        gl.glPushMatrix();

        body.draw(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.1d, 0d, 0.6d);
        limb.draw(gl, leftLegAnglesInOut, leftLegAnglesFrontBack, Limb.LEG, Limb.LEFT);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.1d, 0d, 0.6d);
        limb.draw(gl, rightLegAnglesInOut, rightLegAnglesFrontBack, Limb.LEG, Limb.RIGHT);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.2d, 0d, 0.975d);
        gl.glRotated(90d, 0d, 1d, 0d);
        limb.draw(gl, leftArmAnglesInOut, leftArmAnglesFrontBack, Limb.ARM, Limb.LEFT);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.2d, 0d, 0.975d);
        gl.glRotated(-90d, 0d, 1d, 0d);
        limb.draw(gl, rightArmAnglesInOut, rightArmAnglesFrontBack, Limb.ARM, Limb.RIGHT);

        gl.glPopMatrix();
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    public void setLeftLegAngles(double[] anglesInOut, double[] anglesFrontBack){
        System.arraycopy(anglesInOut, 0, this.leftLegAnglesInOut, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesFrontBack, 0, this.leftLegAnglesFrontBack, 0, Limb.RING_COUNT + 1);
    }

    public void setRightLegAngles(double[] anglesInOut, double[] anglesFrontBack){
        System.arraycopy(anglesInOut, 0, this.rightLegAnglesInOut, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesFrontBack, 0, this.rightLegAnglesFrontBack, 0, Limb.RING_COUNT + 1);
    }

    public void setLeftArmAngles(double[] anglesInOut, double[] anglesFrontBack){
        System.arraycopy(anglesInOut, 0, this.leftArmAnglesInOut, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesFrontBack, 0, this.leftArmAnglesFrontBack, 0, Limb.RING_COUNT + 1);
    }

    public void setRightArmAngles(double[] anglesInOut, double[] anglesFrontBack){
        System.arraycopy(anglesInOut, 0, this.rightArmAnglesInOut, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesFrontBack, 0, this.rightArmAnglesFrontBack, 0, Limb.RING_COUNT + 1);
    }
}
