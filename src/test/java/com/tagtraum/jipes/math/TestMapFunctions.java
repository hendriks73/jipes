/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestMapFunctions.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMapFunctions {

    @Test
    public void testAutoCorrelation() {
        final MapFunction<float[]> function = MapFunctions.AUTO_CORRELATION;
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        assertArrayEquals(Floats.autoCorrelation(a), result, 0.000001f);
        assertEquals("AUTO_CORRELATION", function.toString());
    }

    @Test
    public void testAutoCorrelationCoeff() {
        final MapFunction<float[]> function = MapFunctions.AUTO_CORRELATION_COEFF;
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        final float[] correlation = Floats.autoCorrelation(a);
        final float zeroLag = correlation[0];
        for (int i=0; i<correlation.length; i++) {
            correlation[i] = correlation[i]/ zeroLag;
        }
        assertArrayEquals(correlation, result, 0.000001f);
        assertEquals("AUTO_CORRELATION_COEFF", function.toString());
    }

    @Test
    public void testAutoCorrelationBiased() {
        final MapFunction<float[]> function = MapFunctions.AUTO_CORRELATION_BIASED;
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        final float[] correlation = Floats.autoCorrelation(a);
        for (int i=0; i<correlation.length; i++) {
            correlation[i] = correlation[i]/a.length;
        }
        assertArrayEquals(correlation, result, 0.000001f);
        assertEquals("AUTO_CORRELATION_BIASED", function.toString());
    }

    @Test
    public void testAutoCorrelationUnbiased() {
        final MapFunction<float[]> function = MapFunctions.AUTO_CORRELATION_UNBIASED;
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        final float[] correlation = Floats.autoCorrelation(a);
        for (int i=0; i<correlation.length; i++) {
            correlation[i] = correlation[i]/(a.length-i);
        }
        assertArrayEquals(correlation, result, 0.000001f);
        assertEquals("AUTO_CORRELATION_UNBIASED", function.toString());
    }

    @Test
    public void testAbsFunction() {
        final MapFunction<float[]> function = MapFunctions.createAbsFunction();
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        assertArrayEquals(Floats.abs(a), result, 0.000001f);
        assertEquals("ABS", function.toString());

        assertEquals(MapFunctions.createAbsFunction(), function);
        assertEquals(MapFunctions.createAbsFunction().hashCode(), function.hashCode());
    }

    @Test
    public void testSquareFunction() {
        final MapFunction<float[]> function = MapFunctions.createSquareFunction();
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        assertArrayEquals(Floats.square(a), result, 0.000001f);
        assertEquals("SQUARE", function.toString());

        assertEquals(MapFunctions.createSquareFunction(), function);
        assertEquals(MapFunctions.createSquareFunction().hashCode(), function.hashCode());
    }

    @Test
    public void testEuclideanNormalizationFunction() {
        final MapFunction<float[]> function = MapFunctions.createEuclideanNormalization();
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        final double norm = Floats.euclideanNorm(a);
        Floats.multiply(a, 1f/(float)norm);
        assertArrayEquals(a, result, 0.000001f);
        assertEquals("EUCLIDEAN_NORMALIZATION", function.toString());
    }

    @Test
    public void testDivByMaxNormalization() {
        final MapFunction<float[]> function = MapFunctions.createDivByMaxNormalization();
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        final float norm = Floats.max(a);
        Floats.multiply(a, 1f/norm);
        assertArrayEquals(a, result, 0.000001f);
        assertEquals("DIV_BY_MAX_NORMALIZATION", function.toString());
    }

    @Test
    public void testShortToOneNormalization() {
        final MapFunction<float[]> function = MapFunctions.createShortToOneNormalization();
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        Floats.multiply(a, 1f/(Short.MAX_VALUE + 1));
        assertArrayEquals(a, result, 0.000001f);
        assertEquals("SHORT_TO_ONE_NORMALIZATION", function.toString());

        assertEquals(MapFunctions.createShortToOneNormalization(), function);
        assertEquals(MapFunctions.createShortToOneNormalization().hashCode(), function.hashCode());

    }

    @Test
    public void testReverseFunction() {
        final MapFunction<float[]> function = MapFunctions.createReverseFunction();
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        final float[] reverse = Floats.reverse(a);
        assertArrayEquals(reverse, result, 0.000001f);
        assertEquals("REVERSE", function.toString());

        assertEquals(MapFunctions.createReverseFunction(), function);
        assertEquals(MapFunctions.createReverseFunction().hashCode(), function.hashCode());
    }

    @Test
    public void testWrapFunction() {
        final MapFunction<float[]> function = MapFunctions.createWrapFunction(3);
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        assertArrayEquals(Floats.wrap(a, 3), result, 0.000001f);
        assertEquals("WRAP_3", function.toString());

        assertEquals(MapFunctions.createWrapFunction(3), function);
        assertNotEquals(MapFunctions.createWrapFunction(4), function);
        assertEquals(MapFunctions.createWrapFunction(3).hashCode(), function.hashCode());
    }

    @Test
    public void testInterpolateFunction() {
        final MapFunction<float[]> function = MapFunctions.createInterpolateFunction(0.5f, 1);
        final float[] a = {5, 3, 3, 6, 1};
        final float[] result = function.map(a);
        assertArrayEquals(Floats.interpolate(a, 0.5f, 1), result, 0.000001f);
        assertEquals("InterpolateFunction{indicesPerOneShift=1, shift=0.5}", function.toString());

        assertEquals(MapFunctions.createInterpolateFunction(0.5f, 1), function);
        assertNotEquals(MapFunctions.createInterpolateFunction(0.6f, 1), function);
        assertNotEquals(MapFunctions.createInterpolateFunction(0.5f, 2), function);
        assertEquals(MapFunctions.createInterpolateFunction(0.5f, 1).hashCode(), function.hashCode());
    }

    @Test
    public void testTemporalCentroidFunction() {
        final StatefulMapFunction<Float> function = MapFunctions.createTemporalCentroidFunction();
        assertEquals(1f, function.map(1f), 0.000001f);
        assertEquals(1.5f, function.map(1f), 0.000001f);
        assertEquals(2.25f, function.map(2f), 0.000001f);
        assertEquals("TEMPORAL_CENTROID", function.toString());

        function.reset();
        assertEquals(1f, function.map(1f), 0.000001f);
    }

    @Test
    public void testVarianceFunction() {
        final StatefulMapFunction<Float> function = MapFunctions.createVarianceFunction();
        assertEquals(0.0f, function.map(1f), 0.000001f);
        assertEquals(0.0f, function.map(1f), 0.000001f);
        assertEquals(0.222222f, function.map(2f), 0.000001f);
        assertEquals("VARIANCE", function.toString());

        function.reset();
        assertEquals(0f, function.map(1f), 0.000001f);
    }

    @Test
    public void testStandardDeviationFunction() {
        final StatefulMapFunction<Float> function = MapFunctions.createStandardDeviationFunction();
        assertEquals(0.0f, function.map(1f), 0.000001f);
        assertEquals(0.0f, function.map(1f), 0.000001f);
        assertEquals(Floats.standardDeviation(new float[]{1f, 1f, 2f}), function.map(2f), 0.000001f);
        assertEquals("STANDARD_DEVIATION", function.toString());

        function.reset();
        assertEquals(0f, function.map(1f), 0.000001f);
    }

    @Test
    public void testArithmeticMeanFunction() {
        final StatefulMapFunction<Float> function = MapFunctions.createArithmeticMeanFunction();
        assertEquals(Floats.arithmeticMean(new float[]{1}), function.map(1f), 0.000001f);
        assertEquals(Floats.arithmeticMean(new float[]{1, 1}), function.map(1f), 0.000001f);
        assertEquals(Floats.arithmeticMean(new float[]{1, 1, 2}), function.map(2f), 0.000001f);
        assertEquals("ARITHMETIC_MEAN", function.toString());

        function.reset();
        assertEquals(1f, function.map(1f), 0.000001f);
    }

    @Test
    public void testFractionFunction() {
        final StatefulMapFunction<Float> function = MapFunctions.createFractionFunction(0.5f);
        assertEquals(0f, function.map(1f), 0.000001f);
        assertEquals(0f, function.map(1f), 0.000001f);
        assertEquals(0f, function.map(1f), 0.000001f);
        assertEquals(0.25f, function.map(0.1f), 0.000001f);
        assertEquals("FRACTION_BELOW_0.5", function.toString());

        function.reset();
        assertEquals(0f, function.map(1f), 0.000001f);
    }
}
