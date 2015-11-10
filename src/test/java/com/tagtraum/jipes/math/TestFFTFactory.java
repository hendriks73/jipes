/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TestFFTFactory.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFFTFactory {

    @Test
    public void testResultLength() {
        final float[] in = new float[] {
                1, 2, 3, 4, 5, 6, 7, 100, 9, 10, 11, 12, 13, 14, 15, 16
        };
        final Transform fft = FFTFactory.getInstance().create(in.length);
        final float[] result = fft.transform(in)[0];
        assertEquals(in.length, result.length);
    }

    @Test
    public void testForwardFFT() {
        final float[] realIn = new float[]{1, 2, 1, 0, -1, 0, -1, 3};
        final float[][] result = FFTFactory.getInstance().create(realIn.length).transform(realIn);

        final float[] real = result[0];
        assertEquals(5f, real[0], 0.0001f);
        assertEquals(5.53553f, real[1], 0.0001f);
        assertEquals(0f, real[2], 0.0001f);
        assertEquals(-1.53553f, real[3], 0.0001f);
        assertEquals(-5f, real[4], 0.0001f);
        assertEquals(-1.53553f, real[5], 0.0001f);
        assertEquals(0f, real[6], 0.0001f);
        assertEquals(5.53553f, real[7], 0.0001f);

        final float[] imag = result[1];
        assertEquals(0f, imag[0], 0.0001f);
        assertEquals(-1.29289f, imag[1], 0.0001f);
        assertEquals(1f, imag[2], 0.0001f);
        assertEquals(2.70711f, imag[3], 0.0001f);
        assertEquals(0f, imag[4], 0.0001f);
        assertEquals(-2.70711f, imag[5], 0.0001f);
        assertEquals(-1f, imag[6], 0.0001f);
        assertEquals(1.29289f, imag[7], 0.0001f);

    }

    @Test
    public void testForwardFFTWithImag() {
        final float[] realIn = new float[]{1, 2, 1, 0, -1, 0, -1, 3};
        final float[][] result = FFTFactory.getInstance().create(realIn.length).transform(realIn, new float[realIn.length]);

        final float[] real = result[0];
        assertEquals(5f, real[0], 0.0001f);
        assertEquals(5.53553f, real[1], 0.0001f);
        assertEquals(0f, real[2], 0.0001f);
        assertEquals(-1.53553f, real[3], 0.0001f);
        assertEquals(-5f, real[4], 0.0001f);
        assertEquals(-1.53553f, real[5], 0.0001f);
        assertEquals(0f, real[6], 0.0001f);
        assertEquals(5.53553f, real[7], 0.0001f);

        final float[] imag = result[1];
        assertEquals(0f, imag[0], 0.0001f);
        assertEquals(-1.29289f, imag[1], 0.0001f);
        assertEquals(1f, imag[2], 0.0001f);
        assertEquals(2.70711f, imag[3], 0.0001f);
        assertEquals(0f, imag[4], 0.0001f);
        assertEquals(-2.70711f, imag[5], 0.0001f);
        assertEquals(-1f, imag[6], 0.0001f);
        assertEquals(1.29289f, imag[7], 0.0001f);

    }

    @Test
    public void testInverseFFT() {
        final float[] realIn = new float[]{5f, 5.53553f, 0f, -1.53553f, -5f, -1.53553f, 0f, 5.53553f};
        final float[] imagIn = new float[]{0f, -1.29289f, 1f, 2.70711f, 0f, -2.70711f, -1f, 1.29289f};
        final Transform fft = FFTFactory.getInstance().create(realIn.length);
        final float[] result = fft.inverseTransform(realIn, imagIn)[0];

        final float[] expectedResult = new float[]{1, 2, 1, 0, -1, 0, -1, 3};
        for (int i=0; i<expectedResult.length; i++) {
            assertEquals(expectedResult[i], result[i], 0.0001f);
        }
    }

    @Test
    public void testRoundtripFFT() {
        final float[] realIn = new float[]{1, 2, 1, 0, -1, 0, -1, 3};
        final Transform fft = FFTFactory.getInstance().create(realIn.length);
        final float[][] result = fft.transform(realIn);
        final float[] realOut = fft.inverseTransform(result[0], result[1])[0];
        for (int i=0; i<realIn.length; i++) {
            assertEquals(realIn[i], realOut[i], 0.0001f);
        }
    }

    @Test
    public void testZeroFFT() {
        final float[] floats = new float[8 * 1024];
        final Transform fft = FFTFactory.getInstance().create(floats.length);
        final float[][] transform = fft.transform(floats);
        for (int i=0; i<floats.length; i++) {
            assertEquals(0f, transform[0][i], 0.0001f);
        }
    }

    @Test
    public void testToString() {
        final Transform fft = FFTFactory.getInstance().create(256);
        assertEquals("JavaFFT{N=256}", fft.toString());
    }

    @Test
    public void testEquals() {
        final Transform fft0 = FFTFactory.getInstance().create(256);
        final Transform fft1 = FFTFactory.getInstance().create(256);
        final Transform fft2 = FFTFactory.getInstance().create(512);
        assertEquals(fft0, fft1);
        assertEquals(fft0.hashCode(), fft1.hashCode());
        assertNotEquals(fft0, fft2);
        assertNotEquals(fft0.hashCode(), fft2.hashCode());
    }

}
