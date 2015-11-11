/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestConstantQTransformFactory.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestConstantQTransformFactory {

    /*
    // this test seems flawed (hs)
    public void test220HzPeak() {
        final float[] real = new float[2 * 1024];
        float sampleRate = 44100 / 4f;

        for (int i=0; i<real.length; i++) {
            real[i] = (float) Math.sin(Math.PI * 2 * i * 220 / sampleRate);
        }

        final Transform constantQTransform = ConstantQTransformFactory.getInstance().create(25.956543f, 3520.0f, 12 * 3, sampleRate, ConstantQTransformFactory.DEFAULT_THRESHOLD);
        final float[][] transform = constantQTransform.transform(WindowFunctions.HAMMING.map(real));
        final float[] mag = new float[transform[0].length];
        for (int i=0; i<mag.length; i++) {
            mag[i] = (float)Math.sqrt(transform[0][i]*transform[0][i] + transform[1][i]*transform[1][i]);
        }

        final int index = Floats.maxIndex(mag);
        final float[] freqs = transform[2];
        float peakFreq = freqs[index] * sampleRate;
        System.out.println("Min : " + (freqs[0] * sampleRate));
        System.out.println("Max : " + (freqs[freqs.length-1] * sampleRate));
        System.out.println("Bins: " + freqs.length);
        System.out.println("Peak freq: " + peakFreq);
        assertEquals(220f, peakFreq, 0.001f);
    }
    */

    @Test
    public void testSimpleOperation() {
        final float[] real = new float[5000];
        for (int i=0; i<real.length; i++) {
            real[i] = (float)Math.sin(Math.PI * 2 * i * 0.3);
        }

        final Transform constantQTransform = ConstantQTransformFactory.getInstance().create(440, 880, 6, 2000, 0.0054f);
        final float[][] transform = constantQTransform.transform(real);

        for (int i=0; i<transform[0].length; i++) {
            System.out.println(transform[0][i] + "  " + transform[1][i] + "i");
        }

        assertEquals(6.8467204E-4f, transform[0][0], 0.0001f);
        assertEquals(4.954482E-5f, transform[1][0], 0.0001f);

        assertEquals(8.361763E-5f, transform[0][1], 0.0001f);
        assertEquals(-0.001393581f, transform[1][1], 0.0001f);

        assertEquals(0.08648582f, transform[0][2], 0.0001f);
        assertEquals(0.124312595f, transform[1][2], 0.0001f);

        assertEquals(-0.051467948f, transform[0][3], 0.0001f);
        assertEquals(-0.25508842f, transform[1][3], 0.0001f);

        assertEquals(-0.028783837f, transform[0][4], 0.0001f);
        assertEquals(0.12103417f, transform[1][4], 0.0001f);

        assertEquals(0.0055199f, transform[0][5], 0.0001f);
        assertEquals(-0.0063760392f, transform[1][5], 0.0001f);
    }

    @Test
    public void testToString() {
        final Transform constantQTransform = ConstantQTransformFactory.getInstance().create(440, 880, 6, 2000, 0.0054f);
        assertEquals("JavaConstantQTransform{Q=8.165795, minFrequency=440.0, maxFrequency=880.0, binsPerOctave=6, sampleRate=2000.0, threshold=0.0054}", constantQTransform.toString());
    }

    @Test
    public void testEquals() {
        final ConstantQTransformFactory factory = ConstantQTransformFactory.getInstance();
        final Transform constantQTransform0 = factory.create(440, 880, 6, 2000, 0.0054f);
        final Transform constantQTransform1 = factory.create(440, 880 + new Random().nextInt(100), 6, 2000, 0.0054f);
        final Transform constantQTransform2 = factory.create(440, 880, 6, 2000, 0.0054f);
        assertEquals(constantQTransform0, constantQTransform2);
        assertEquals(constantQTransform0.hashCode(), constantQTransform2.hashCode());
        assertNotEquals(constantQTransform0, constantQTransform1);
        assertNotEquals(constantQTransform0.hashCode(), constantQTransform1.hashCode());
    }

}
