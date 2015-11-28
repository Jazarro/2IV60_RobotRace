/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utility;

import robotrace.bender.Bender;

/**
 *
 * @author Arjan Boschman
 */
public final class AssemblyUtils {

    private AssemblyUtils() {
    }
    
    public static double[] makeConicalFrustum(double lowerRadius, double upperRadius, double lowerHeight, double upperHeight, int sliceCount) {
        final double[] array = new double[(sliceCount + 1) * Bender.NUMCOORD * 2];
        for (int i = 0; i < sliceCount + 1; i++) {
            final double[] lowerCoords = Bender.calcCoord(i, lowerRadius, lowerHeight, sliceCount);
            final double[] upperCoords = Bender.calcCoord(i, upperRadius, upperHeight, sliceCount);
            final double azimuth = (360 / (double) sliceCount) * i;
            final double deltaRadius = Math.abs(upperRadius - lowerRadius);
            final double deltaHeight = upperHeight - lowerHeight;
            final double elevation = Math.atan(deltaRadius / deltaHeight);
            final double[] normal = {
                Math.cos(azimuth) * Math.sin(elevation),
                Math.sin(azimuth) * Math.sin(elevation),
                Math.cos(elevation)
            };
            final int indexInArray = i * 3 * Bender.NUMCOORD;
            System.arraycopy(lowerCoords, 0, array, indexInArray, Bender.NUMCOORD);
            System.arraycopy(upperCoords, 0, array, indexInArray + Bender.NUMCOORD, Bender.NUMCOORD);
            System.arraycopy(normal, 0, array, indexInArray + Bender.NUMCOORD, Bender.NUMCOORD);
        }
        return array;
    }
    
}
