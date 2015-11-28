package robotrace.bender;

import javax.media.opengl.GL2;

public class Bender{

    public static final int NUMCOORD = 3;

    private final Body body;
    private final Limb limb;

    private final double[] leftLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightLegAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] leftArmAnglesBend = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesAxis = new double[Limb.RING_COUNT + 1];
    private final double[] rightArmAnglesBend = new double[Limb.RING_COUNT + 1];

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

        
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.1d, 0d, 0d);
        limb.draw(gl, leftLegAnglesAxis, leftLegAnglesBend, Limb.LEG, Limb.LEFT);
        /*gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.1d, 0d, 0.6d);
        limb.draw(gl, rightLegAnglesAxis, rightLegAnglesBend, Limb.LEG, Limb.RIGHT);
        
        body.draw(gl);
        
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.2d, 0d, 0.975d);
        gl.glRotated(90d, 0d, 1d, 0d);
        limb.draw(gl, leftArmAnglesAxis, leftArmAnglesBend, Limb.ARM, Limb.LEFT);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.2d, 0d, 0.975d);
        gl.glRotated(-90d, 0d, 1d, 0d);
        limb.draw(gl, rightArmAnglesAxis, rightArmAnglesBend, Limb.ARM, Limb.RIGHT);*/

        gl.glPopMatrix();
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    public void setLeftLegAngles(double[] anglesAxis, double[] anglesBend){
        System.arraycopy(anglesAxis, 0, this.leftLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.leftLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    public void setRightLegAngles(double[] anglesAxis, double[] anglesBend){
        System.arraycopy(anglesAxis, 0, this.rightLegAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightLegAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    public void setLeftArmAngles(double[] anglesAxis, double[] anglesBend){
        System.arraycopy(anglesAxis, 0, this.leftArmAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.leftArmAnglesBend, 0, Limb.RING_COUNT + 1);
    }

    public void setRightArmAngles(double[] anglesAxis, double[] anglesBend){
        System.arraycopy(anglesAxis, 0, this.rightArmAnglesAxis, 0, Limb.RING_COUNT + 1);
        System.arraycopy(anglesBend, 0, this.rightArmAnglesBend, 0, Limb.RING_COUNT + 1);
    }
}
