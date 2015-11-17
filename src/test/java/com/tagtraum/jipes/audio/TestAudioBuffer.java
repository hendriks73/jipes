/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

/**
 * TestAudioBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAudioBuffer {

    public static float[] toMagnitudes(final float[] real, final float[] imaginary, final int length) {
        final float[] magnitudes = new float[length];
        for (int i=0; i<length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            magnitudes[i] = (float)Math.sqrt(real[i] * real[i] + v * v);
        }
        return magnitudes;
    }

    public static float[] toPowers(final float[] real, final float[] imaginary, final int length) {
        final float[] powers = new float[length];
        for (int i=0; i<length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            powers[i] = real[i] * real[i] + v * v;
        }
        return powers;
    }

    public static float[] toPowers(final float[] real, final float[] imaginary) {
        return toPowers(real, imaginary, real.length);
    }

    public static float[] toMagnitudes(final float[] real, final float[] imaginary) {
        return toMagnitudes(real, imaginary, real.length);
    }
}
