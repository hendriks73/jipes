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
 * TestWindowFunction.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestWindowFunction {

    @Test
    public void testAbstractClass() {
        final float[] coefficients = {1f, 2f, 3f, 4f};
        final WindowFunction function = new WindowFunction(coefficients) {};
        assertArrayEquals(coefficients, function.getCoefficients(), 0.00001f);
        assertEquals(coefficients.length, function.getLength());
        final WindowFunction otherFunction = new WindowFunction(coefficients) {
        };
        assertEquals(otherFunction, function);
        assertEquals(otherFunction.hashCode(), function.hashCode());
        final float[] mapped = function.map(new float[]{1, 1, 0, 1});
        assertArrayEquals(new float[]{1f, 2f, 0f, 4f}, mapped, 0.0001f);
    }

    @Test
    public void testInvert() {
        final float[] coefficients = {1f, 2f, 3f, 4f};
        final WindowFunction function = new WindowFunction(coefficients) {
            @Override
            public String toString() {
                return "WINDOW";
            }
        };
        final WindowFunction invertedFunction = function.invert();
        assertArrayEquals(new float[]{1f, 1/2f, 1/3f, 1/4f}, invertedFunction.getCoefficients(), 0.00001f);
        assertEquals("InverseWindowFunction{function=WINDOW}", invertedFunction.toString());
        final WindowFunction doubleInvertedFunction = invertedFunction.invert();
        assertArrayEquals(coefficients, doubleInvertedFunction.getCoefficients(), 0.00001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadDataLength() {
        final float[] coefficients = {1f, 2f, 3f, 4f};
        final WindowFunction function = new WindowFunction(coefficients) {};
        function.map(new float[]{1f});
    }

    @Test
    public void testWelch() {
        final MapFunction<float[]> function = WindowFunction.WELCH;
        final float[] mapped = function.map(new float[]{1f, 1f, 1f});
        assertArrayEquals(new float[]{0.0f, 1f, 0.0f}, mapped, 0.00001f);
        assertEquals("WELCH_WINDOW", function.toString());
    }

    @Test
    public void testWelch2() {
        final MapFunction<float[]> function = new WindowFunction.Welch(12);
        assertEquals("Welch{length=12}", function.toString());
    }

    @Test
    public void testHann() {
        final MapFunction<float[]> function = WindowFunction.HANN;
        final float[] mapped = function.map(new float[]{1f, 1f, 1f});
        assertArrayEquals(new float[]{
                (float) (0.5-0.5 * Math.cos(2*Math.PI*0/2)),
                (float) (0.5-0.5 * Math.cos(2*Math.PI*1/2)),
                (float) (0.5-0.5 * Math.cos(2*Math.PI*2/2)),
        }, mapped, 0.00001f);
        assertEquals("HANN_WINDOW", function.toString());
    }

    @Test
    public void testHann2() {
        final MapFunction<float[]> function = new WindowFunction.Hann(12);
        assertEquals("Hann{length=12}", function.toString());
    }

    @Test
    public void testHamming() {
        final MapFunction<float[]> function = WindowFunction.HAMMING;
        final float[] mapped = function.map(new float[]{1f, 1f, 1f});
        assertArrayEquals(new float[]{
                (float) (0.54-0.46 * Math.cos(2*Math.PI*0/2)),
                (float) (0.54-0.46 * Math.cos(2*Math.PI*1/2)),
                (float) (0.54-0.46 * Math.cos(2*Math.PI*2/2)),
        }, mapped, 0.00001f);
        assertEquals("HAMMING_WINDOW", function.toString());
    }

    @Test
    public void testHamming2() {
        final MapFunction<float[]> function = new WindowFunction.Hamming(12);
        assertEquals("Hamming{length=12}", function.toString());
    }

    @Test
    public void testTriangle() {
        final MapFunction<float[]> function = WindowFunction.TRIANGLE;
        final float[] mapped = function.map(new float[]{1f, 1f, 1f, 1f, 1f});
        assertArrayEquals(new float[]{
                0f,
                0.5f,
                1f,
                0.5f,
                0f
        }, mapped, 0.00001f);
        assertEquals("TRIANGLE_WINDOW", function.toString());
    }

    @Test
    public void testTriangle2() {
        final MapFunction<float[]> function = new WindowFunction.Triangle(12);
        assertEquals("Triangle{length=12}", function.toString());
    }
}
