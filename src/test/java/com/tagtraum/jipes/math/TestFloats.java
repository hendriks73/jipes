/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TestFloats.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFloats {


    @Test
    public void testSimplePeak() {
        final int[] peaks = Floats.peaks(new float[]{0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1}, 2, true);
        assertEquals(1, peaks.length);
        assertEquals(6, peaks[0]);
    }

    @Test
    public void testFalsePeak() {
        final int[] peaks = Floats.peaks(new float[]{0, 1, 2, 3, 4, 5, 6, 5, 6, 5, 4, 3}, 2, true);
        assertEquals(0, peaks.length);
    }


    @Test
    public void testNonStrictPeak() {
        final int[] peaks = Floats.peaks(new float[]{0, 0, 2, 0, 0, 0}, 2, false);
        assertArrayEquals(new int[]{2}, peaks);
    }

    @Test
    public void testStrictPeakWithPlateau() {
        final int[] peaks = Floats.peaks(new float[]{0, 0, 2, 2, 0, 0}, 2, true);
        assertEquals(0, peaks.length);
    }

    @Test
    public void testVariance() {
        final float variance = Floats.variance(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertEquals(6.6666666666667f, variance, 0.000001f);
    }

    @Test
    public void testLog2() {
        assertEquals(0f, Floats.log2(1), 0.00000001f);
    }

    @Test
    public void testReverse() {
        final float[] array = {1f, 2f, 3f, 4f, 5f};
        Floats.reverse(array);
        assertArrayEquals(new float[]{5f, 4f, 3f, 2f, 1f}, array, 0.000001f);
    }

    @Test
    public void testZeroPad() {
        final float[] array = {1f, 2f, 3f, 4f, 5f};
        final float[] padded = Floats.zeroPadAtEnd(array);
        assertEquals(8, padded.length);
    }

    @Test
    public void testPercentageBelowAverage() {
        final float[] array = {2f, 2f, 2f, 4f, 4f, 4f};
        final float percentage = Floats.percentageBelowAverage(array);
        assertEquals(0.5f, percentage, 0.000001f);
    }

    @Test
    public void testSubtractSameLength() {
        final float[] a = {1f, 2f, 3f, 4f};
        final float[] b = {1f, 2f, 3f, 4f};
        final float[] result = Floats.subtract(a, b);
        assertArrayEquals(new float[]{0f, 0f, 0f, 0f}, result, 0.000001f);
    }

    @Test
    public void testSubtractDifferentLength() {
        final float[] a = {1f, 2f, 3f, 4f};
        final float[] b = {1f, 2f, 3f, 4f, 5f, 6f};
        final float[] result = Floats.subtract(a, b);
        assertArrayEquals(new float[]{0f, 0f, 0f, 0f, -5f, -6f}, result, 0.000001f);

        final float[] result2 = Floats.subtract(b, a);
        assertArrayEquals(new float[]{0f, 0f, 0f, 0f, 5f, 6f}, result2, 0.000001f);
    }

    @Test
    public void testAddSameLength() {
        final float[] a = {1f, 2f, 3f, 4f};
        final float[] b = {1f, 2f, 3f, 4f};
        final float[] result = Floats.add(a, b);
        assertArrayEquals(new float[]{2f, 4f, 6f, 8f}, result, 0.000001f);
    }

    @Test
    public void testAddDifferentLength() {
        final float[] a = {1f, 2f, 3f, 4f};
        final float[] b = {1f, 2f, 3f, 4f, 5f, 6f};
        final float[] result = Floats.add(a, b);
        assertArrayEquals(new float[]{2f, 4f, 6f, 8f, 5f, 6f}, result, 0.000001f);

        final float[] result2 = Floats.add(b, a);
        assertArrayEquals(new float[]{2f, 4f, 6f, 8f, 5f, 6f}, result2, 0.000001f);
    }

    @Test
    public void testSum() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final float result = Floats.sum(array);
        assertEquals(21f, result, 0.000001f);
    }

    @Test
    public void testArithmeticMean() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final float result = Floats.arithmeticMean(array);
        assertEquals(3.5f, result, 0.000001f);
    }

    @Test
    public void testArithmeticMeanWithOffset() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final float result = Floats.arithmeticMean(array, 1, 4);
        assertEquals(3.5f, result, 0.000001f);
    }

    @Test
    public void testAbs() {
        final float[] array = {-1f, 2f, -3f, 4f, -5f, 6f};
        Floats.abs(array);
        assertArrayEquals(new float[]{1f, 2f, 3f, 4f, 5f, 6f}, array, 0.000001f);
    }

    @Test
    public void testSquare() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        Floats.square(array);
        assertArrayEquals(new float[]{1f*1f, 2f*2f, 3f*3f, 4f*4f, 5f*5f, 6f*6f}, array, 0.000001f);
    }

    @Test
    public void testMin() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final float min = Floats.min(array);
        assertEquals(1f, min, 0.000001f);
    }

    @Test
    public void testMax() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final float max = Floats.max(array);
        assertEquals(6f, max, 0.000001f);
    }

    @Test
    public void testMaxIndex() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final int maxIndex = Floats.maxIndex(array);
        assertEquals(5, maxIndex);
    }

    @Test
    public void testMaxIndices() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final int[] maxIndices = Floats.maxIndices(array);
        assertArrayEquals(new int[] {5, 4, 3, 2, 1, 0}, maxIndices);
    }

    @Test
    public void testMaxIndicesWithOffset() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        final int[] maxIndices = Floats.maxIndices(array, 2, 3);
        assertArrayEquals(new int[] {4, 3, 2}, maxIndices);
    }

    @Test
    public void testMultiply() {
        final float[] array = {1f, 2f, 3f, 4f, 5f, 6f};
        Floats.multiply(array, 2f);
        assertArrayEquals(new float[]{1f*2f, 2f*2f, 3f*2f, 4f*2f, 5f*2f, 6f*2f}, array, 0.000001f);
    }

    @Test
    public void testDotProduct() {
        final float[] a = {3f, 2f};
        final float[] b = {5f, 3f};
        final double dotProduct = Floats.dotProduct(a, b);
        assertEquals(3f*5f + 2f*3f, dotProduct, 0.000001f);
    }

    @Test
    public void testMedianEven() {
        final float[] array = {1f, 1f, 3f, 4f, 5f, 11f, 10f, 1f};
        final float median = Floats.median(array);
        assertEquals(3.5f, median, 0.00001f);
    }

    @Test
    public void testMedianOdd() {
        final float[] array = {12f, 1f, 3f, 4f, 5f, 10f, 11f};
        final float median = Floats.median(array);
        assertEquals(5f, median, 0.00001f);
    }

    @Test
    public void testEuclideanNorm() {
        final float[] array = {1f, 2f, 3f, 4f, 5f};
        final double norm = Floats.euclideanNorm(array);
        assertEquals(Math.sqrt(1f*1f + 2f*2f + 3f*3f + 4f*4f + 5f*5f), norm, 0.00001);

        final double norm2 = Floats.pNorm(array, 2.0);
        assertEquals(Math.sqrt(1f*1f + 2f*2f + 3f*3f + 4f*4f + 5f*5f), norm2, 0.00001);
    }

    @Test
    public void testCityBlockDistance() {
        final float[] a = {3f, 2f};
        final float[] b = {5f, 3f};
        final double distance = Floats.cityBlockDistance(a, b);
        assertEquals(Math.abs(3f-5f) + Math.abs(2f-3f), distance, 0.00001);
    }

    @Test
    public void testCosineSimilarity() {
        final float[] a = {3f, 2f};
        final float[] b = {5f, 3f};

        final double similarity = Floats.cosineSimilarity(a, b);
        assertEquals(Floats.dotProduct(a, b) / (Floats.euclideanNorm(a) * Floats.euclideanNorm(b)), similarity, 0.00001);
    }

    @Test
    public void testCosineDistance() {
        final float[] a = {3f, 2f};
        final float[] b = {5f, 3f};

        final double distance = Floats.cosineDistance(a, b);
        assertEquals(Floats.dotProduct(a, b) / (Floats.euclideanNorm(a) * Floats.euclideanNorm(b)), 1-distance, 0.00001);
    }

    @Test
    public void testSumListOfFloats() {
        final float[] a = {3f, 2f};
        final float[] b = {5f, 3f, 4f};

        final float[] sums = Floats.sum(Arrays.asList(a, b));
        assertArrayEquals(new float[]{8f, 5f, 4f}, sums, 0.00001f);
    }

    @Test
    public void testMeanAbsoluteDeviation() {
        final float meanAbsoluteDeviation = Floats.meanAbsoluteDeviation(0, new float[]{-2, -1, 1, 2});
        assertEquals(1.5f, meanAbsoluteDeviation, 0.00001f);
    }

    @Test
    public void testCityBlockNorm() {
        final float[] a = {5f, 2f};

        final double norm = Floats.cityBlockNorm(a);
        assertEquals(5f + 2f, norm, 0.0000001);

        final double norm2 = Floats.pNorm(a, 1.0);
        assertEquals(5f + 2f, norm2, 0.0000001);
    }

    @Test
    public void testPNorm() {
        final float[] a = {5f, 2f};

        final double norm = Floats.pNorm(a, 3.0);
        assertEquals(Math.pow(5f*5f*5f + 2f*2f*2f, 1.0/3.0), norm, 0.0000001);
    }

    @Test
    public void testEuclideanDistance() {
        final float[] a = {3f, 2f};
        final float[] b = {5f, 4f};

        final double distance = Floats.euclideanDistance(a, b);
        assertEquals(Math.sqrt((3f - 5f) * (3f - 5f) + (2f - 4f) * (2f - 4f)), distance, 0.00001);
    }

    @Test
    public void testPositiveCorrelation() {
        final float[] a = {1f, 2f};
        final float[] b = {2f, 4f};

        final double correlation = Floats.correlation(a, b);
        assertEquals(1f, correlation, 0.00001f);
    }

    @Test
    public void testNegativeCorrelation() {
        final float[] a = {1f, 2f};
        final float[] b = {4f, 2f};

        final double correlation = Floats.correlation(a, b);
        assertEquals(-1f, correlation, 0.00001f);
    }

    @Test
    public void testNoCovariance() {
        final float[] a = {1f, 1f};
        final float[] b = {4f, 4f};

        final double correlation = Floats.correlation(a, b);
        assertTrue(Double.isNaN(correlation));
    }

    @Test
    public void testRootMeanSquare() {
        final float[] a = {1f, 2f, 3f};

        final double rms = Floats.rootMeanSquare(a);
        assertEquals(Math.sqrt((1f * 1f + 2f * 2f + 3f * 3f) / 3.0f), rms, 0.0001);
    }

    @Test
    public void testZeroCrossingRate() {
        final float[] a = {-11f, 2f, 3f, -1f};

        final float zcr = Floats.zeroCrossingRate(a);
        assertEquals(2f/(a.length-1), zcr, 0.0001f);
    }

    @Test
    public void testInterpolatePositive() {
        final float[] a = {1, 2, 1, 2};
        final float[] interpolate = Floats.interpolate(a, 0.5f, 1);
        assertArrayEquals(new float[]{0.5f, 1.5f, 1.5f, 1.5f}, interpolate, 0.0001f);
    }

    @Test
    public void testInterpolateNegative() {
        final float[] a = {1, 2, 1, 2};
        final float[] interpolate = Floats.interpolate(a, -0.5f, 1);
        assertArrayEquals(new float[]{1.5f, 1.5f, 1.5f, 1f}, interpolate, 0.0001f);
    }

    @Test
    public void testInterpolateQuarterPositive() {
        final float[] a = {1, 2, 1, 2};
        final float[] interpolate = Floats.interpolate(a, 0.25f, 1);
        System.out.println(Arrays.toString(interpolate));
        assertArrayEquals(new float[]{0.75f, 1.75f, 1.25f, 1.75f}, interpolate, 0.0001f);
    }

    @Test
    public void testInterpolateQuarterNegative() {
        final float[] a = {1, 2, 1, 2};
        final float[] interpolate = Floats.interpolate(a, -0.25f, 1);
        System.out.println(Arrays.toString(interpolate));
        assertArrayEquals(new float[]{1.25f, 1.75f, 1.25f, 1.5f}, interpolate, 0.0001f);
    }

    // TODO: add more tests!!
}
