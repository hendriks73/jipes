/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * TestAggregateFunctions.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAggregateFunctions {

    @Test
    public void testArithmeticMean() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.ARITHMETIC_MEAN;
        final float[] array = {1, 2, 3, 4, 5};
        final float mean = function.aggregate(array);
        assertEquals(Floats.arithmeticMean(array), mean, 0.000001f);
        assertEquals("ARITHMETIC_MEAN", function.toString());
    }

    @Test
    public void testEuclideanNorm() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.EUCLIDEAN_NORM;
        final float[] array = {1, 2, 3, 4, 5};
        final float norm = function.aggregate(array);
        assertEquals(Floats.euclideanNorm(array), norm, 0.000001f);
        assertEquals("EUCLIDEAN_NORM", function.toString());
    }

    @Test
    public void testSum() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.SUM;
        final float[] array = {1, 2, 3, 4, 5};
        final float sum = function.aggregate(array);
        assertEquals(Floats.sum(array), sum, 0.000001f);
        assertEquals("SUM", function.toString());
    }

    @Test
    public void testMinimum() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.MINIMUM;
        final float[] array = {1, 2, 3, 4, 5};
        final float minimum = function.aggregate(array);
        assertEquals(Floats.min(array), minimum, 0.000001f);
        assertEquals("MINIMUM", function.toString());
    }

    @Test
    public void testMaximum() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.MAXIMUM;
        final float[] array = {1, 2, 3, 4, 5};
        final float maximum = function.aggregate(array);
        assertEquals(Floats.max(array), maximum, 0.000001f);
        assertEquals("MAXIMUM", function.toString());
    }

    @Test
    public void testStandardDeviation() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.STANDARD_DEVIATION;
        final float[] array = {1, 2, 3, 4, 5};
        final float standardDeviation = function.aggregate(array);
        assertEquals(Floats.standardDeviation(array), standardDeviation, 0.000001f);
        assertEquals("STANDARD_DEVIATION", function.toString());
    }

    @Test
    public void testVariance() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.VARIANCE;
        final float[] array = {1, 2, 3, 4, 5};
        final float variance = function.aggregate(array);
        assertEquals(Floats.variance(array), variance, 0.000001f);
        assertEquals("VARIANCE", function.toString());
    }

    @Test
    public void testCorrectedStandardDeviation() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.CORRECTED_STANDARD_DEVIATION;
        final float[] array = {1, 2, 3, 4, 5};
        final float standardDeviation = function.aggregate(array);
        assertEquals(Floats.correctedStandardDeviation(array), standardDeviation, 0.000001f);
        assertEquals("CORRECTED_STANDARD_DEVIATION", function.toString());
    }

    @Test
    public void testUnbiasedVariance() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.UNBIASED_VARIANCE;
        final float[] array = {1, 2, 3, 4, 5};
        final float variance = function.aggregate(array);
        assertEquals(Floats.unbiasedVariance(array), variance, 0.000001f);
        assertEquals("UNBIASED_VARIANCE", function.toString());
    }

    @Test
    public void testElementWiseSum() {
        final AggregateFunction<List<float[]>, float[]> function = AggregateFunctions.ELEMENT_WISE_SUM;
        List<float[]> list = Arrays.asList(
                new float[] {1, 2, 3, 4, 5},
                new float[] {1, 2, 3, 4, 5},
                new float[] {1, 2, 3, 4, 5}
        );
        final float[] sum = function.aggregate(list);
        assertArrayEquals(Floats.sum(list), sum, 0.000001f);
        assertEquals("ELEMENT_WISE_SUM", function.toString());
    }

    @Test
    public void testRootMeanSquare() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.ROOT_MEAN_SQUARE;
        final float[] array = {1, 2, 3, 4, 5};
        final float rootMeanSquare = function.aggregate(array);
        assertEquals(Floats.rootMeanSquare(array), rootMeanSquare, 0.000001f);
        assertEquals("ROOT_MEAN_SQUARE", function.toString());
    }

    @Test
    public void testZeroCrossingRate() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.ZERO_CROSSING_RATE;
        final float[] array = {1, 2, 3, 4, 5};
        final float zeroCrossingRate = function.aggregate(array);
        assertEquals(Floats.zeroCrossingRate(array), zeroCrossingRate, 0.000001f);
        assertEquals("ZERO_CROSSING_RATE", function.toString());
    }

    @Test
    public void testPercentageBelowAverage() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.PERCENTAGE_BELOW_AVERAGE;
        final float[] array = {1, 2, 3, 4, 5};
        final float percentageBelowAverage = function.aggregate(array);
        assertEquals(Floats.percentageBelowAverage(array), percentageBelowAverage, 0.000001f);
        assertEquals("PERCENTAGE_BELOW_AVERAGE", function.toString());
    }

    @Test
    public void testRelativeEntropy() {
        final AggregateFunction<float[], Float> function = AggregateFunctions.RELATIVE_ENTROPY;
        final float[] array = {1, -1, 3};
        final float relativeEntropy = function.aggregate(array);
        final float sum = array[0] + array[2];
        final double entropy = -(1f / sum * Math.log(1f / sum) + 3f / sum * Math.log(3f / sum)) / Math.log(array.length);
        assertEquals(entropy, relativeEntropy, 0.00001f);
        assertEquals("RELATIVE_ENTROPY", function.toString());
    }
}
