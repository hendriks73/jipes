/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import org.junit.Test;

import javax.sound.sampled.AudioFormat;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestInterpolate.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestInterpolate {

    @Test
    public void testFactor() {
        final Interpolate processor = new Interpolate(); // default is 2
        assertEquals(2, processor.getFactor());
        assertEquals(0, processor.getTargetSampleRate(), 0.00001f);
        processor.setFactor(4);
        assertEquals(4, processor.getFactor());
        assertEquals("Interpolate{factor=4}", processor.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFactor() {
        new Interpolate(-22);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedFactor() {
        new Interpolate(1000);
    }

    @Test
    public void testTargetSampleRate() {
        final Interpolate processor = new Interpolate(44100f);
        assertEquals(0, processor.getFactor());
        assertEquals(44100f, processor.getTargetSampleRate(), 0.00001f);
        processor.setTargetSampleRate(10000f);
        assertEquals(10000f, processor.getTargetSampleRate(), 0.00001f);
        assertEquals("Interpolate{target=10000.0Hz}", processor.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTargetSampleRate() {
        new Interpolate(-22f);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedTargetSampleRate() throws IOException {
        final Interpolate interpolate = new Interpolate(1000f);
        interpolate.processNext(new RealAudioBuffer(0, new float[1], new AudioFormat(999f, 8, 2, true, true)));
    }

    @Test
    public void testProcess() throws IOException {
        final Interpolate interpolate = new Interpolate(1000f);
        final AudioBuffer buffer = interpolate.processNext(new RealAudioBuffer(0, new float[100], new AudioFormat(500f, 8, 1, true, true)));
        assertArrayEquals(new float[200], buffer.getData(), 0.0001f);
        interpolate.processNext(new RealAudioBuffer(1, new float[100], new AudioFormat(500f, 8, 1, true, true)));
    }

    @Test
    public void testEqualsHashCode1() {
        final Interpolate processor0 = new Interpolate(2);
        final Interpolate processor1 = new Interpolate(2);
        final Interpolate processor2 = new Interpolate(3);
        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

    @Test
    public void testEqualsHashCode2() {
        final Interpolate processor0 = new Interpolate(1000f);
        final Interpolate processor1 = new Interpolate(1000f);
        final Interpolate processor2 = new Interpolate(2000f);
        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }
}
