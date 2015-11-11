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

}
