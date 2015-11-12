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
 * TestGriffinLim.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestGriffinLim {

    @Test(expected = IllegalArgumentException.class)
    public void testBadInitialValues() {
        new GriffinLim(new float[16], new float[15], 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInverseTransform() {
        final GriffinLim griffinLim = new GriffinLim(new float[16], new float[16], 5);
        griffinLim.inverseTransform(new float[2], new float[2]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransformWithImaginary() {
        final GriffinLim griffinLim = new GriffinLim(new float[4], new float[4], 5);
        griffinLim.transform(new float[4], new float[]{1,2,3,4});
    }

    @Test(expected = NullPointerException.class)
    public void testNullInitialValue() {
        new GriffinLim(null, new float[15], 5);
    }

    @Test
    public void testNoImaginaryValue() {
        new GriffinLim(new float[16], null, 5);
    }

    @Test
    public void testRoundtrip() {
        final float[] signal = {100, -100, 100, -100, 100, -100, 100, -100, 100, -100, 100, -100, 100, -100, 100, -100};
        final float[] magnitudes = getMagnitudes(signal);
        // manipulate magnitudes
        magnitudes[magnitudes.length/2] = 0;
        final GriffinLim griffinLim = new GriffinLim(signal, new float[signal.length], 3, 0.01f);
        final float[] recreatedSignal = griffinLim.transform(magnitudes)[0];
        assertEquals(signal.length, recreatedSignal.length);
        // now
        final float[] recreatedMagnitudes = getMagnitudes(recreatedSignal);
        assertArrayEquals(magnitudes, recreatedMagnitudes, 0.0001f);
    }

    @Test
    public void testAllZero() {
        final float[] signal = {100, -100, 100, -100, 100, -100, 100, -100, 100, -100, 100, -100, 100, -100, 100, -100};
        final float[] magnitudes = new float[signal.length/2];
        final GriffinLim griffinLim = new GriffinLim(signal, new float[signal.length], 3, 0.01f);
        final float[] recreatedSignal = griffinLim.transform(magnitudes)[0];
        assertArrayEquals(new float[signal.length], recreatedSignal, 0.0001f);
    }

    private static float[] getMagnitudes(final float[] signal) {
        final Transform fft = FFTFactory.getInstance().create(signal.length);
        final float[][] signalFreqDomain = fft.transform(signal);
        final float[] magnitudes = new float[signal.length/2];
        for (int i=0; i<magnitudes.length; i++) {
            final float real = signalFreqDomain[0][i];
            final float imaginary = signalFreqDomain[1][i] * signalFreqDomain[1][i];
            final float magnitude = (float)Math.sqrt(real * real + imaginary);
            magnitudes[i] = magnitude;
        }
        return magnitudes;
    }
}
