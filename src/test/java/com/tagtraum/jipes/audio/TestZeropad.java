/*
 * =================================================
 * Copyright 2017 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestZeropad.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestZeropad {

    @Test
    public void testFrontPadding() throws IOException {
        final Zeropad<AudioBuffer> zeropad = new Zeropad<AudioBuffer>(Zeropad.Position.FRONT, 10);
        final float[] a = new float[5];
        Arrays.fill(a, 1);
        final RealAudioBuffer buffer = new RealAudioBuffer(0, a , null);
        zeropad.process(buffer);
        final AudioBuffer paddedBuffer = zeropad.getOutput();
        final float[] expected = new float[10];
        Arrays.fill(expected, 5, 10, 1);
        Assert.assertArrayEquals(expected, paddedBuffer.getRealData(), 0.00001f);
    }

    @Test
    public void testBackPadding() throws IOException {
        final Zeropad<AudioBuffer> zeropad = new Zeropad<AudioBuffer>(Zeropad.Position.BACK, 10);
        final float[] a = new float[5];
        Arrays.fill(a, 1);
        final RealAudioBuffer buffer = new RealAudioBuffer(0, a , null);
        zeropad.process(buffer);
        final AudioBuffer paddedBuffer = zeropad.getOutput();
        final float[] expected = new float[10];
        Arrays.fill(expected, 0, 5, 1);
        Assert.assertArrayEquals(expected, paddedBuffer.getRealData(), 0.00001f);
    }

    @Test
    public void testBothPadding() throws IOException {
        final Zeropad<AudioBuffer> zeropad = new Zeropad<AudioBuffer>(Zeropad.Position.BOTH, 10);
        final float[] a = new float[5];
        Arrays.fill(a, 1);
        final RealAudioBuffer buffer = new RealAudioBuffer(0, a , null);
        zeropad.process(buffer);
        final AudioBuffer paddedBuffer = zeropad.getOutput();
        final float[] expected = new float[10];
        Arrays.fill(expected, 2, 7, 1);
        Assert.assertArrayEquals(expected, paddedBuffer.getRealData(), 0.00001f);
    }

    @Test
    public void testImaginaryPadding() throws IOException {
        final Zeropad<AudioBuffer> zeropad = new Zeropad<AudioBuffer>(Zeropad.Position.BOTH, 10);
        final float[] r = new float[5];
        Arrays.fill(r, 1);
        final float[] i = new float[5];
        Arrays.fill(i, 2);
        final ComplexAudioBuffer buffer = new ComplexAudioBuffer(0, r , i, null);
        zeropad.process(buffer);
        final AudioBuffer paddedBuffer = zeropad.getOutput();
        final float[] expectedR = new float[10];
        Arrays.fill(expectedR, 2, 7, 1);
        Assert.assertArrayEquals(expectedR, paddedBuffer.getRealData(), 0.00001f);
        final float[] expectedI = new float[10];
        Arrays.fill(expectedI, 2, 7, 2);
        Assert.assertArrayEquals(expectedI, paddedBuffer.getImaginaryData(), 0.00001f);
    }

    @Test
    public void testBasics() {
        final Zeropad<AudioBuffer> zeropad = new Zeropad<AudioBuffer>("Id", Zeropad.Position.BOTH, 10);
        final Zeropad<AudioBuffer> zeropad0 = new Zeropad<AudioBuffer>("Id", Zeropad.Position.BOTH, 10);
        final Zeropad<AudioBuffer> zeropad1 = new Zeropad<AudioBuffer>("Id", Zeropad.Position.FRONT, 10);
        assertEquals(zeropad, zeropad0);
        assertNotEquals(zeropad, zeropad1);
        assertEquals(zeropad.hashCode(), zeropad0.hashCode());
        assertEquals("Zeropad{id=Id, BOTH, sizeAfterPadding=10}", zeropad.toString());
    }
}
