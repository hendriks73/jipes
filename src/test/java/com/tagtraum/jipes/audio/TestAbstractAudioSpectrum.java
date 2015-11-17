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

import static org.junit.Assert.*;

/**
 * TestAbstractAudioSpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAbstractAudioSpectrum extends TestAudioBuffer {

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {5, 6, 7, 8};
        final float[] frequencies = {1, 3, 5, 6};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final AbstractAudioSpectrum spectrum = new MockAbstractAudioSpectrum(frameNumber, realData, imaginaryData, audioFormat, frequencies);

        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(realData.length, spectrum.getNumberOfSamples());
        assertArrayEquals(realData, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, spectrum.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getData(), 0.000001f);

        assertEquals(5f, spectrum.getFrequency(2), 0.000001f);
        assertArrayEquals(frequencies, spectrum.getFrequencies(), 0.000001f);
    }

    @Test
    public void testImaginaryIsNull() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = null;
        final float[] frequencies = {1, 3, 5, 6};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final AbstractAudioSpectrum spectrum = new MockAbstractAudioSpectrum(frameNumber, realData, imaginaryData, audioFormat, frequencies);

        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(realData.length, spectrum.getNumberOfSamples());
        assertArrayEquals(realData, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, spectrum.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getData(), 0.000001f);

        assertEquals(5f, spectrum.getFrequency(2), 0.000001f);
        assertArrayEquals(frequencies, spectrum.getFrequencies(), 0.000001f);
    }

    @Test
    public void testReuse() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {5, 6, 7, 8};
        final float[] frequencies = {1, 3, 5, 6};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final AbstractAudioSpectrum spectrum = new MockAbstractAudioSpectrum(frameNumber, realData, imaginaryData, audioFormat, frequencies);

        final int frameNumber1 = 2;
        final float[] realData1 = {1, 2, 3};
        final float[] imaginaryData1 = {2, 3, 4};
        final AudioFormat audioFormat1 = new AudioFormat(20, 4, 1, false, true);
        spectrum.reuse(frameNumber1, realData1, imaginaryData1, audioFormat1);

        assertEquals(frameNumber1, spectrum.getFrameNumber());
        assertEquals(realData1.length, spectrum.getNumberOfSamples());
        assertArrayEquals(realData1, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData1, spectrum.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat1, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber1*1000L/audioFormat1.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber1 * 1000L * 1000L / audioFormat1.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toPowers(realData1, imaginaryData1), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData1, imaginaryData1), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData1, imaginaryData1), spectrum.getData(), 0.000001f);

        assertEquals(5f, spectrum.getFrequency(2), 0.000001f);
        assertArrayEquals(frequencies, spectrum.getFrequencies(), 0.000001f);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {5, 6, 7, 8};
        final float[] frequencies = {1, 3, 5, 6};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final AbstractAudioSpectrum spectrum = new MockAbstractAudioSpectrum(frameNumber, realData, imaginaryData, audioFormat, frequencies);

        final AbstractAudioSpectrum clone = (AbstractAudioSpectrum)spectrum.clone();

        assertEquals(frameNumber, clone.getFrameNumber());
        assertEquals(realData.length, clone.getNumberOfSamples());
        assertArrayEquals(realData, clone.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, clone.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, clone.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), clone.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), clone.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), clone.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), clone.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), clone.getData(), 0.000001f);

        assertEquals(5f, clone.getFrequency(2), 0.000001f);
        assertArrayEquals(frequencies, clone.getFrequencies(), 0.000001f);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4};
        final float[] imaginaryData = {5, 6, 7, 8};
        final float[] frequencies = {1, 3, 5, 6};
        final AudioFormat audioFormat = new AudioFormat(10, 8, 2, true, false);
        final AbstractAudioSpectrum spectrum = new MockAbstractAudioSpectrum(frameNumber, realData, imaginaryData, audioFormat, frequencies);

        final MockAbstractAudioSpectrum copy = new MockAbstractAudioSpectrum(spectrum, frequencies);
        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getData(), 0.000001f);

        assertEquals(5f, copy.getFrequency(2), 0.000001f);
        assertArrayEquals(frequencies, copy.getFrequencies(), 0.000001f);

        assertNotSame(spectrum.getRealData(), copy.getRealData());
        assertNotSame(spectrum.getImaginaryData(), copy.getImaginaryData());
        assertNotSame(spectrum.getPowers(), copy.getPowers());
        assertNotSame(spectrum.getMagnitudes(), copy.getMagnitudes());
        assertSame(spectrum.getAudioFormat(), copy.getAudioFormat());
        assertSame(spectrum.getFrameNumber(), copy.getFrameNumber());
    }

    private static class MockAbstractAudioSpectrum extends AbstractAudioSpectrum {
        private final float[] frequencies;

        public MockAbstractAudioSpectrum(final int frameNumber, final float[] realData, final float[] imaginaryData, final AudioFormat audioFormat, final float[] frequencies) {
            super(frameNumber, realData, imaginaryData, audioFormat);
            this.frequencies = frequencies;
        }

        public MockAbstractAudioSpectrum(final AbstractAudioSpectrum abstractAudioSpectrum, final float[] frequencies) {
            super(abstractAudioSpectrum);
            this.frequencies = frequencies;
        }

        @Override
        public void reuse(final int frameNumber, final float[] realData, final float[] imaginaryData, final AudioFormat audioFormat) {
            super.reuse(frameNumber, realData, imaginaryData, audioFormat);
        }

        @Override
        public AudioSpectrum derive(final float[] real, final float[] imaginary) {
            return null;
        }

        @Override
        public float[] getFrequencies() {
            return frequencies;
        }

        @Override
        public int getBin(final float frequency) {
            return 0;
        }
    }
}
