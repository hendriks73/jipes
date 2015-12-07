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
 * TestComplexAudioBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestComplexAudioBuffer extends TestAudioBuffer {

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {3, 2, 1, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final ComplexAudioBuffer buffer = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);

        assertEquals(frameNumber, buffer.getFrameNumber());
        assertEquals(realData.length, buffer.getNumberOfSamples());
        assertArrayEquals(realData, buffer.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, buffer.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, buffer.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), buffer.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), buffer.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), buffer.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), buffer.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), buffer.getData(), 0.000001f);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {3, 2, 1, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final ComplexAudioBuffer buffer = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);

        final ComplexAudioBuffer copy = new ComplexAudioBuffer(buffer);

        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertNotSame(realData, copy.getRealData());
        assertArrayEquals(imaginaryData, copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getData(), 0.000001f);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {3, 2, 1, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final ComplexAudioBuffer buffer = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);

        final ComplexAudioBuffer copy = (ComplexAudioBuffer)buffer.clone();

        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertNotSame(realData, copy.getRealData());
        assertArrayEquals(imaginaryData, copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getData(), 0.000001f);
    }

    @Test
    public void testDerive() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {3, 2, 1, 4};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final ComplexAudioBuffer buffer = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);

        final float[] derivedReal = {2, 3, 4, 5};
        final float[] derivedImaginary = {5, 6, 7, 8};
        final ComplexAudioBuffer derived = buffer.derive(derivedReal, derivedImaginary);

        assertEquals(frameNumber, derived.getFrameNumber());
        assertEquals(derivedReal.length, derived.getNumberOfSamples());
        assertArrayEquals(derivedReal, derived.getRealData(), 0.000001f);
        assertSame(derivedReal, derived.getRealData());
        assertArrayEquals(derivedImaginary, derived.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, derived.getAudioFormat());
        assertEquals((long) (frameNumber * 1000L / audioFormat.getSampleRate()), derived.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), derived.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toPowers(derivedReal, derivedImaginary), derived.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(derivedReal, derivedImaginary), derived.getMagnitudes(), 0.000001f);
        assertArrayEquals(toMagnitudes(derivedReal, derivedImaginary), derived.getData(), 0.000001f);
    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {3, 2, 1, 4};
        final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 10, 16, 2, 16, 10, true);
        final ComplexAudioBuffer buffer = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);
        assertTrue(Pattern.matches("ComplexAudioBuffer\\{audioFormat=PCM_SIGNED 10\\.0 Hz, 16 bit, stereo, 16 bytes/frame, big-endian, timestamp=300, frameNumber=3, realData=\\[.+, imaginaryData=\\[.+\\}", buffer.toString()));
    }

    @Test
    public void testEqualsHashCode() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {3, 2, 1, 4};
        final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 10, 16, 2, 16, 10, true);
        final ComplexAudioBuffer buffer0 = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);
        final ComplexAudioBuffer buffer1 = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, audioFormat);
        final ComplexAudioBuffer buffer2 = new ComplexAudioBuffer(frameNumber+1, realData, imaginaryData, audioFormat);

        final ComplexAudioBuffer buffer3 = new ComplexAudioBuffer(frameNumber, new float[] {2, 3, 1, 3}, imaginaryData, audioFormat);
        final ComplexAudioBuffer buffer4 = new ComplexAudioBuffer(frameNumber, realData, imaginaryData, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 10, 16, 2, 16, 10, false));

        assertEquals(buffer0, buffer1);
        assertEquals(buffer0.hashCode(), buffer1.hashCode());
        assertNotEquals(buffer0.hashCode(), buffer2.hashCode());
        assertNotEquals(buffer0, buffer2);
        assertNotEquals(buffer0, buffer3);
        assertNotEquals(buffer0, buffer4);
    }

}
