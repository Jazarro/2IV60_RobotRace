/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package Texture;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

public class ImplementedTexture {

    private Texture texture;
    private double scaleWidth = 1d;
    private double scaleHeight = 1d;
    private boolean stretchWidth = false;
    private boolean stretchHeight = false;
    private String originalFilename;

    public ImplementedTexture(GL2 gl, String filename, boolean interpolate, boolean mirror) {
        originalFilename = filename;
        final File file = new File("src/main/java/robotrace/" + filename);
        try {
            texture = TextureIO.newTexture(file, true);
            texture.enable(gl);
            texture.bind(gl);
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, (interpolate) ? (GL2.GL_LINEAR) : (GL2.GL_NEAREST));
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, (interpolate) ? (GL2.GL_LINEAR) : (GL2.GL_NEAREST));
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, (mirror) ? (GL2.GL_MIRRORED_REPEAT) : (GL2.GL_REPEAT));
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, (mirror) ? (GL2.GL_MIRRORED_REPEAT) : (GL2.GL_REPEAT));
            texture.disable(gl);
        } catch (IOException | GLException ex) {
            Logger.getLogger(ImplementedTexture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public ImplementedTexture setScale(double scale) {
        this.scaleWidth = scale;
        this.scaleHeight = scale;
        return this;
    }

    public ImplementedTexture setScale(double scaleWidth, double scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        return this;
    }

    public ImplementedTexture setStretch(boolean stretch) {
        this.stretchWidth = stretch;
        this.stretchHeight = stretch;
        return this;
    }

    public ImplementedTexture setStretch(boolean stretchWidth, boolean stretchHeight) {
        this.stretchWidth = stretchWidth;
        this.stretchHeight = stretchHeight;
        return this;
    }

    public boolean getStretchWidth() {
        return stretchWidth;
    }

    public boolean getStretchHeight() {
        return stretchHeight;
    }

    public float getImageWidth() {
        return (float) (texture.getImageWidth() * scaleWidth);
    }

    public float getImageHeight() {
        return (float) (texture.getImageHeight() * scaleHeight);
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void drawStart(GL2 gl) {
        texture.enable(gl);
        texture.bind(gl);
    }

    public void drawEnd(GL2 gl) {
        texture.disable(gl);
    }

}
