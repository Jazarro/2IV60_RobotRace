/*
 * TU/e Eindhoven University of Technology
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * 
 * Copyright (C) 2015 Arjan Boschman, Robke Geenen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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

    /**
     * Draws the tree.
     *
     * @param gl       The instance of GL2 responsible for drawing the body.
     * @param camPos   The position of the camera in terrain coordinates.
     * @param lighting The Lighting instance responsible for calculating the
     *                 lighting in this scene. Can be used to set the colours of
     *                 bodies before drawing them.
     */
    public void draw(GL2 gl, Vector camPos, Lighting lighting) {
        gl.glPushMatrix();
        gl.glTranslated(position.x(), position.y(), position.z());
        lighting.setMaterial(gl, Material.BARK);
        final int requiredDetailLevel = getRequiredDetailLevel(camPos);
        trunk.draw(gl, lighting, foliage, requiredDetailLevel, 0);
        gl.glPopMatrix();
    }

    /**
     * Check how far away the camera eye point is from the tree and calculate a
     * required detail level based on that. The further away it is, the less
     * detail is required in drawing the tree.
     *
     * @param camPos The position of the camera relative to the terrain in
     *               meters. Because the trees are defined in the same frame of
     *               reference, this value can be safely compared to the tree's
     *               position.
     * @return An arbitrarily defined integer value between 0 <= x <= 3. The
     *         greater this value, the more detail is required.
     */
    private int getRequiredDetailLevel(Vector camPos) {
        final float distanceToEye = (float) Math.abs(camPos.subtract(position).length());
        if (distanceToEye > 1500f) {
            return 0;
        } else if (distanceToEye > 300f) {
            return 1;
        } else if (distanceToEye > 150f) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * A branch or leaf on this tree.
     */
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

        /**
         * This constructor overload is meant for creating leaf nodes.
         *
         * @param zTranslation        The translation in the z direction,
         *                            relative to the branch that this node is
         *                            on.
         * @param scale               The scaling vector. This is absolute, NOT
         *                            relative to the scaling of the previous
         *                            branch.
         * @param zRotation           The rotation around the Z-axis relative to
         *                            the parent branch in degrees. This is in
         *                            effect the rotation around the parent
         *                            branch.
         * @param yRotation           The rotation around the Y-axis relative to
         *                            the parent branch in degrees. The is in
         *                            effect the angle between this node and the
         *                            parent branch, if one imagines both in the
         *                            same plane.
         * @param sidewaysTranslation The translation sideways. This is
         *                            especially for leafs, since it doesn't
         *                            look good if they start out in the center
         *                            of the parent branch.
         */
        public Node(float zTranslation, Vector scale, float zRotation, float yRotation, float sidewaysTranslation) {
            this(zTranslation, scale, zRotation, yRotation, new HashSet<>(0), new HashSet<>(0), true, sidewaysTranslation);
        }

        /**
         * This constructor overload is meant for creating branch nodes.
         *
         * @param zTranslation  The translation in the z direction, relative to
         *                      the branch that this node is on.
         * @param scale         The scaling vector. This is absolute, NOT
         *                      relative to the scaling of the previous branch.
         * @param zRotation     The rotation around the Z-axis relative to the
         *                      parent branch in degrees. This is in effect the
         *                      rotation around the parent branch.
         * @param yRotation     The rotation around the Y-axis relative to the
         *                      parent branch in degrees. The is in effect the
         *                      angle between this node and the parent branch,
         *                      if one imagines both in the same plane.
         * @param childLeafs    A set of leaf nodes attached to this branch.
         * @param childBranches A set of child branch nodes attached to this
         *                      branch.
         */
        public Node(float zTranslation, Vector scale, float zRotation, float yRotation, Set<Node> childLeafs, Set<Node> childBranches) {
            this(zTranslation, scale, zRotation, yRotation, childLeafs, childBranches, false, 0);
        }

        private Node(float zTranslation, Vector scale, float zRotation, float yRotation, Set<Node> childLeafs, Set<Node> childBranches, boolean isLeaf, float sidewaysTranslation) {
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
            gl.glTranslated(0d, 0d, zTranslation);
            gl.glRotated(zRotation, 0d, 0d, 1d);
            gl.glRotated(yRotation, 0d, 1d, 0d);
            gl.glPushMatrix();
            if (isLeaf) {
                final float scaleMultiplier = (requiredDetailLevel == 0) ? (4f) : ((requiredDetailLevel == 1) ? (2f) : (1f));
                gl.glScaled(scale.x() * scaleMultiplier, scale.y() * scaleMultiplier, scale.z() * scaleMultiplier);
                gl.glTranslated(sidewaysTranslation, 0d, 0d);
                gl.glRotated(zRotation, 0d, 0d, 1d);
                foliage.drawLeaf(gl);
            } else {
                gl.glScaled(scale.x(), scale.y(), scale.z());
                foliage.drawBranch(gl, calcRequiredDetailForBranch(requiredDetailLevel, (float) scale.x()));
            }
            gl.glPopMatrix();
            final int maxNodes = (requiredDetailLevel == 0) ? (1)
                    : ((requiredDetailLevel == 1) ? (4)
                            : ((requiredDetailLevel == 2) ? (10)
                                    : (Integer.MAX_VALUE)));
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
