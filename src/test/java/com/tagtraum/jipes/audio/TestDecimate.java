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

import static org.junit.Assert.*;

/**
 * TestDecimate.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestDecimate {

    @Test
    public void testFactor() {
        final Decimate processor = new Decimate(); // default is 2
        assertEquals(2, processor.getFactor());
        assertEquals(0, processor.getTargetSampleRate(), 0.00001f);
        processor.setFactor(4);
        assertEquals(4, processor.getFactor());
        assertEquals("Decimate{factor=4}", processor.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFactor() {
        new Decimate(-22);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedFactor() {
        new Decimate(1000);
    }

    @Test
    public void testTargetSampleRate() {
        final Decimate processor = new Decimate(44100f);
        assertEquals(0, processor.getFactor());
        assertEquals(44100f, processor.getTargetSampleRate(), 0.00001f);
        processor.setTargetSampleRate(10000f);
        assertEquals(10000f, processor.getTargetSampleRate(), 0.00001f);
        assertEquals("Decimate{target=10000.0Hz}", processor.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTargetSampleRate() {
        new Decimate(-22f);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedTargetSampleRate() throws IOException {
        final Decimate decimate = new Decimate(1000f);
        decimate.processNext(new RealAudioBuffer(0, new float[1], new AudioFormat(999f, 8, 2, true, true)));
    }

    @Test
    public void testProcess() throws IOException {
        final Decimate decimate = new Decimate(500f);
        final AudioBuffer buffer = decimate.processNext(new RealAudioBuffer(0, new float[100], new AudioFormat(1000f, 8, 1, true, true)));
        assertArrayEquals(new float[50], buffer.getData(), 0.0001f);
        decimate.processNext(new RealAudioBuffer(1, new float[100], new AudioFormat(1000f, 8, 1, true, true)));
    }

    @Test
    public void testEqualsHashCode1() {
        final Decimate processor0 = new Decimate(2);
        final Decimate processor1 = new Decimate(2);
        final Decimate processor2 = new Decimate(3);
        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

    @Test
    public void testEqualsHashCode2() {
        final Decimate processor0 = new Decimate(1000f);
        final Decimate processor1 = new Decimate(1000f);
        final Decimate processor2 = new Decimate(2000f);
        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }
}
