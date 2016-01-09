/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package terrain.trees;

import java.util.HashSet;
import java.util.Set;
import javax.media.opengl.GL2;
import robotrace.Lighting;
import robotrace.Material;
import robotrace.Vector;

/**
 *
 * @author Arjan Boschman
 */
public class Tree {

    private final Foliage foliage;
    private final Vector position;
    private final Node trunk;

    public Tree(Foliage foliage, Vector position, Node trunk) {
        this.foliage = foliage;
        this.position = position;
        this.trunk = trunk;
    }

    public void draw(GL2 gl, Lighting lighting) {
        gl.glPushMatrix();
        gl.glTranslated(position.x(), position.y(), position.z());
        lighting.setMaterial(gl, Material.BARK);
        trunk.draw(gl, foliage, lighting);
        gl.glPopMatrix();
    }

    @SuppressWarnings("PublicInnerClass")
    public static class Node {

        private final float zTranslation;
        private final Vector scale;
        private final float zRotation;
        private final float yRotation;
        private final Set<Node> childLeafs;
        private final Set<Node> childBranches;
        private final boolean isLeaf;
        private final float sidewaysTranslation;

        public Node(float zTranslation, Vector scale, float zRotation, float yRotation, float sidewaysTranslation) {
            this(zTranslation, scale, zRotation, yRotation, new HashSet<>(0), new HashSet<>(0), true, sidewaysTranslation);
        }

        public Node(float zTranslation, Vector scale, float zRotation, float yRotation, Set<Node> childLeafs, Set<Node> childBranches) {
            this(zTranslation, scale, zRotation, yRotation, childLeafs, childBranches, false, 0);
        }

        public Node(float zTranslation, Vector scale, float zRotation, float yRotation, Set<Node> childLeafs, Set<Node> childBranches, boolean isLeaf, float sidewaysTranslation) {
            this.zTranslation = zTranslation;
            this.scale = scale;
            this.zRotation = zRotation;
            this.yRotation = yRotation;
            this.childLeafs = childLeafs;
            this.childBranches = childBranches;
            this.isLeaf = isLeaf;
            this.sidewaysTranslation = sidewaysTranslation;
        }

        public void draw(GL2 gl, Foliage foliage, Lighting lighting) {
            gl.glPushMatrix();
            gl.glTranslated(0, 0, zTranslation);
            gl.glRotated(zRotation, 0, 0, 1);
            gl.glRotated(yRotation, 0, 1, 0);
            gl.glPushMatrix();
            gl.glScaled(scale.x(), scale.y(), scale.z());
            if (isLeaf) {
                gl.glTranslated(sidewaysTranslation, 0, 0);
                gl.glRotated(zRotation, 0, 0, 1);
                foliage.drawLeaf(gl);
            } else {
                foliage.drawBranch(gl);
            }
            gl.glPopMatrix();

            if (!childLeafs.isEmpty()) {
                lighting.setMaterial(gl, Material.LEAF);
                childLeafs.stream().forEach((node) -> node.draw(gl, foliage, lighting));
                lighting.setMaterial(gl, Material.BARK);
            }
            childBranches.stream().forEach((child) -> child.draw(gl, foliage, lighting));
            gl.glPopMatrix();
        }
    }
}
