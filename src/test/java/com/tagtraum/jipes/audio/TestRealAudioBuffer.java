/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * TestRealAudioBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestRealAudioBuffer extends TestAudioBuffer {

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final RealAudioBuffer buffer = new RealAudioBuffer(frameNumber, realData, audioFormat);

        assertEquals(frameNumber, buffer.getFrameNumber());
        assertEquals(realData.length, buffer.getNumberOfSamples());
        assertArrayEquals(realData, buffer.getRealData(), 0.000001f);
        assertArrayEquals(new float[realData.length], buffer.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, buffer.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), buffer.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), buffer.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, null), buffer.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, null), buffer.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, null), buffer.getData(), 0.000001f);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final RealAudioBuffer buffer = new RealAudioBuffer(frameNumber, realData, audioFormat);

        final RealAudioBuffer copy = new RealAudioBuffer(buffer);

        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertNotSame(realData, copy.getRealData());
        assertArrayEquals(new float[realData.length], copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, null), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, null), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, null), copy.getData(), 0.000001f);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final RealAudioBuffer buffer = new RealAudioBuffer(frameNumber, realData, audioFormat);

        final RealAudioBuffer copy = (RealAudioBuffer)buffer.clone();

        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertNotSame(realData, copy.getRealData());
        assertArrayEquals(new float[realData.length], copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, null), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, null), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, null), copy.getData(), 0.000001f);
    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 10, 16, 2, 16, 10, true);
        final RealAudioBuffer buffer = new RealAudioBuffer(frameNumber, realData, audioFormat);
        assertTrue(Pattern.matches("RealAudioBuffer\\{audioFormat=PCM_SIGNED 10\\.0 Hz, 16 bit, stereo, 16 bytes/frame, big-endian, timestamp=300, frameNumber=3, data=\\[.+\\}", buffer.toString()));
    }

    @Test
    public void testEqualsHashCode() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 10, 16, 2, 16, 10, true);
        final RealAudioBuffer buffer0 = new RealAudioBuffer(frameNumber, realData, audioFormat);
        final RealAudioBuffer buffer1 = new RealAudioBuffer(frameNumber, realData, audioFormat);
        final RealAudioBuffer buffer2 = new RealAudioBuffer(frameNumber+1, realData, audioFormat);

        final RealAudioBuffer buffer3 = new RealAudioBuffer(frameNumber, new float[] {2, 3, 1, 3}, audioFormat);
        final RealAudioBuffer buffer4 = new RealAudioBuffer(frameNumber, realData, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 10, 16, 2, 16, 10, false));

        assertEquals(buffer0, buffer1);
        assertEquals(buffer0.hashCode(), buffer1.hashCode());
        assertNotEquals(buffer0.hashCode(), buffer2.hashCode());
        assertNotEquals(buffer0, buffer2);
        assertNotEquals(buffer0, buffer3);
        assertNotEquals(buffer0, buffer4);
    }
}
