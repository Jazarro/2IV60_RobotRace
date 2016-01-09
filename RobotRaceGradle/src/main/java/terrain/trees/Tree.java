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
import robotrace.Camera;
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

    public void draw(GL2 gl, Camera camera, Lighting lighting) {
        gl.glPushMatrix();
        gl.glTranslated(position.x(), position.y(), position.z());
        lighting.setMaterial(gl, Material.BARK);
        final int requiredDetailLevel = getRequiredDetailLevel(camera);
        trunk.draw(gl, lighting, foliage, requiredDetailLevel, 0);
        gl.glPopMatrix();
    }

    private int getRequiredDetailLevel(Camera camera) {
        final float distanceToEye = (float) Math.abs(camera.getEye().subtract(position).length());
        if (distanceToEye > 1000) {
            return 0;
        } else if (distanceToEye > 300) {
            return 1;
        } else if (distanceToEye > 150) {
            return 2;
        } else {
            return 3;
        }
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

        public void draw(GL2 gl, Lighting lighting, Foliage foliage, int requiredDetailLevel, int depth) {
            gl.glPushMatrix();
            gl.glTranslated(0, 0, zTranslation);
            gl.glRotated(zRotation, 0, 0, 1);
            gl.glRotated(yRotation, 0, 1, 0);
            gl.glPushMatrix();
            if (isLeaf) {
                final float scaleMultiplier = requiredDetailLevel == 0 ? 4 : requiredDetailLevel == 1 ? 2 : 1;
                gl.glScaled(scale.x() * scaleMultiplier, scale.y() * scaleMultiplier, scale.z() * scaleMultiplier);
                gl.glTranslated(sidewaysTranslation, 0, 0);
                gl.glRotated(zRotation, 0, 0, 1);
                foliage.drawLeaf(gl);
            } else {
                gl.glScaled(scale.x(), scale.y(), scale.z());
                foliage.drawBranch(gl, calcRequiredDetailForBranch(requiredDetailLevel, (float) scale.x()));
            }
            gl.glPopMatrix();
            final int maxNodes = requiredDetailLevel == 0 ? 1
                    : requiredDetailLevel == 1 ? 4
                            : requiredDetailLevel == 2 ? 10
                                    : Integer.MAX_VALUE;
            if (!childLeafs.isEmpty()) {
                lighting.setMaterial(gl, Material.LEAF);
                childLeafs.stream().limit(maxNodes).forEach((node) -> node.draw(gl, lighting, foliage, requiredDetailLevel, depth + 1));
                lighting.setMaterial(gl, Material.BARK);
            }
            if (!keepGoing(requiredDetailLevel, depth)) {
                gl.glPopMatrix();
                return;
            }
            childBranches.stream().limit(maxNodes).forEach((child) -> child.draw(gl, lighting, foliage, requiredDetailLevel, depth + 1));
            gl.glPopMatrix();
        }

        private boolean keepGoing(int requiredDetailLevel, int depth) {
            return (requiredDetailLevel) * 2 > depth || depth < 3;
        }

        private int calcRequiredDetailForBranch(int requiredDetailLevel, float branchRadius) {
            return (int) ((branchRadius / TreeGenerator.MAX_TRUNK_RADIUS) * requiredDetailLevel);
        }
    }
}
