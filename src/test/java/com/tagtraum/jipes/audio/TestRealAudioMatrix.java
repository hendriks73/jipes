/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.math.FullMatrix;
import com.tagtraum.jipes.math.Matrix;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * TestRealAudioMatrix.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestRealAudioMatrix {


    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final Matrix realData = new FullMatrix(5, 6);
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final RealAudioMatrix matrix = new RealAudioMatrix(frameNumber, realData, audioFormat);

        assertEquals(frameNumber, matrix.getFrameNumber());
        assertEquals(realData.getNumberOfRows(), matrix.getNumberOfSamples());
        assertArrayEquals(realData.getRow(0), matrix.getRealData(), 0.000001f);
        assertArrayEquals(new float[realData.getNumberOfColumns()], matrix.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, matrix.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), matrix.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), matrix.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData.getRow(0), null), matrix.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData.getRow(0), null), matrix.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData.getRow(0), null), matrix.getData(), 0.000001f);
    }

    private static float[] toMagnitudes(final float[] real, final float[] imaginary) {
        final float[] magnitudes = new float[real.length];
        for (int i=0; i<real.length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            magnitudes[i] = (float)Math.sqrt(real[i] * real[i] + v * v);
        }
        return magnitudes;
    }

    private static float[] toPowers(final float[] real, final float[] imaginary) {
        final float[] powers = new float[real.length];
        for (int i=0; i<real.length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            powers[i] = real[i] * real[i] + v * v;
        }
        return powers;
    }
}
