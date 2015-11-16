/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * TestLogFrequencySpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestLogFrequencySpectrum {

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] frequencies = {2, 3, 4, 5, 9, 10};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat, q, frequencies);
        assertEquals(q, spectrum.getQ(), 0.00001f);
        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(realData.length, spectrum.getNumberOfSamples());
        assertArrayEquals(realData, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, spectrum.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertEquals(0.60604507f, spectrum.getBinsPerSemitone(), 0.00001f);

        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getData(), 0.000001f);

        assertEquals(frequencies[2], spectrum.getFrequency(2), 0.000001f);
    }

    @Test
    public void testDerive() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] frequencies = {2, 3, 4, 5, 9, 10};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat, q, frequencies);

        final float[] realData2 = {2, 3, 4, 5, 6, 7 };
        final float[] imaginaryData2 = {2, 5, 4, 1, 6, 7};
        final LogFrequencySpectrum derivative = spectrum.derive(realData2, imaginaryData2);

        assertEquals(q, derivative.getQ(), 0.00001f);
        assertEquals(frameNumber, derivative.getFrameNumber());
        assertEquals(realData2.length, derivative.getNumberOfSamples());
        assertArrayEquals(realData2, derivative.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData2, derivative.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, derivative.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), derivative.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), derivative.getTimestamp(TimeUnit.MICROSECONDS));

        assertEquals(0.60604507f, derivative.getBinsPerSemitone(), 0.00001f);

        assertArrayEquals(toMagnitudes(realData2, imaginaryData2), derivative.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData2, imaginaryData2), derivative.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData2, imaginaryData2), derivative.getData(), 0.000001f);

        assertEquals(frequencies[2], derivative.getFrequency(2), 0.000001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongFrequencies() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] frequencies = {2, 3, 4, 5, 9, 10, 12};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        new LogFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat, q, frequencies);
    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] frequencies = {2, 3, 4, 5, 9, 10};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat, q, frequencies);
        assertTrue(Pattern.matches("LogFrequencySpectrum\\{audioFormat=PCM_SIGNED 10\\.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian, Q=10\\.0, binsPerSemitone=0\\.60604507, timestamp=300, frameNumber=3, realData=\\[.+, imaginaryData=\\[.+\\}", spectrum.toString()));
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] frequencies = {2, 3, 4, 5, 9, 10};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat, q, frequencies);
        final LogFrequencySpectrum copy = new LogFrequencySpectrum(spectrum);

        assertEquals(q, copy.getQ(), 0.00001f);
        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertEquals(0.60604507f, copy.getBinsPerSemitone(), 0.00001f);

        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getData(), 0.000001f);

        assertEquals(frequencies[2], copy.getFrequency(2), 0.000001f);
    }

    @Test
    public void testDeriveShiftedBy1() {
        final int frameNumber = 3;
        final float[] frequencies = MultiBandSpectrum.createMidiBands(30, 35);
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] shiftedRealData = {0, 1, 2, 3, 4, 5};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = (float)(1.0/(Math.pow(2.0, 1.0/12.0) - 1.0)); // this is equivalent to one semitone
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, null, audioFormat, q, frequencies);
        assertEquals(1f, spectrum.getBinsPerSemitone(), 0.0001f);

        final LogFrequencySpectrum shifted = spectrum.derive(1);

        assertEquals(q, shifted.getQ(), 0.00001f);
        assertEquals(frameNumber, shifted.getFrameNumber());
        assertEquals(realData.length, shifted.getNumberOfSamples());
        assertArrayEquals(shiftedRealData, shifted.getRealData(), 0.000001f);
        assertArrayEquals(null, shifted.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, shifted.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), shifted.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), shifted.getTimestamp(TimeUnit.MICROSECONDS));

        assertEquals(1f, shifted.getBinsPerSemitone(), 0.00001f);

        assertArrayEquals(toMagnitudes(shiftedRealData, null), shifted.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(shiftedRealData, null), shifted.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(shiftedRealData, null), shifted.getData(), 0.000001f);

        assertEquals(frequencies[2], shifted.getFrequency(2), 0.000001f);
    }

    @Test
    public void testGetBin() {
        final int frameNumber = 3;
        final float[] frequencies = MultiBandSpectrum.createMidiBands(65, 80);
        final float[] realData = new float[16];
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = (float)(1.0/(Math.pow(2.0, 1.0/12.0) - 1.0)); // this is equivalent to one semitone
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, null, audioFormat, q, frequencies);

        for (int i=0; i<frequencies.length; i++) {
            final float testFreqPos = frequencies[i] + 0.01f;
            assertEquals("Bin for " + testFreqPos + " is wrong. Frequencies: " + Arrays.toString(frequencies), i, spectrum.getBin(testFreqPos));
            final float testFreqNeg = frequencies[i] - 0.01f;
            assertEquals("Bin for " + testFreqNeg + " is wrong. Frequencies: " + Arrays.toString(frequencies), i, spectrum.getBin(testFreqNeg));
        }
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] frequencies = {2, 3, 4, 5, 9, 10};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat, q, frequencies);
        final LogFrequencySpectrum clone = (LogFrequencySpectrum)spectrum.clone();

        assertEquals(q, clone.getQ(), 0.00001f);
        assertEquals(frameNumber, clone.getFrameNumber());
        assertEquals(realData.length, clone.getNumberOfSamples());
        assertArrayEquals(realData, clone.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, clone.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, clone.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), clone.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), clone.getTimestamp(TimeUnit.MICROSECONDS));

        assertEquals(0.60604507f, clone.getBinsPerSemitone(), 0.00001f);

        assertArrayEquals(toMagnitudes(realData, imaginaryData), clone.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), clone.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), clone.getData(), 0.000001f);

        assertEquals(frequencies[2], clone.getFrequency(2), 0.000001f);
    }

    @Test
    public void testMidi() {
        final int frameNumber = 3;
        final float[] frequencies = MultiBandSpectrum.createMidiBands(30, 40);
        final float[] realData = new float[frequencies.length];

        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final float q = 10;
        final LogFrequencySpectrum spectrum = new LogFrequencySpectrum(frameNumber, realData, null, audioFormat, q, frequencies);

        final float[] midiNotes = spectrum.getMidiNotes();
        for (int i=0; i<midiNotes.length; i++) {
            assertEquals(i+29.5f, midiNotes[i], 0.00001f);
        }
    }

    private static float[] toMagnitudes(final float[] real, final float[] imaginary) {
        final float[] magnitudes = new float[real.length];
        for (int i=0; i<magnitudes.length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            magnitudes[i] = (float)Math.sqrt(real[i] * real[i] + v * v);
        }
        return magnitudes;
    }

    private static float[] toPowers(final float[] real, final float[] imaginary) {
        final float[] powers = new float[real.length];
        for (int i=0; i<powers.length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            powers[i] = real[i] * real[i] + v * v;
        }
        return powers;
    }

}
