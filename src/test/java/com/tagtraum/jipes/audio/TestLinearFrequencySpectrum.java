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
 * TestLinearFrequencySpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestLinearFrequencySpectrum {

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
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

        assertEquals(3.3333335f, spectrum.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/realData.length, spectrum.getBandwidth(), 0.000001f);
        assertEquals(spectrum.getFrequency(1), spectrum.getBandwidth(), 0.000001f);
    }

    @Test
    public void testFrequencies() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);

        assertEquals(3.3333335f, spectrum.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/realData.length, spectrum.getBandwidth(), 0.000001f);
        assertEquals(spectrum.getFrequency(1), spectrum.getBandwidth(), 0.000001f);
        // too high bin
        assertEquals(0f, spectrum.getFrequency(Integer.MAX_VALUE), 0.000001f);
        // too low bin
        assertEquals(0f, spectrum.getFrequency(-1), 0.000001f);
        assertEquals(1, spectrum.getBin(spectrum.getFrequency(1)), 0.000001f);
        // symmetry
        assertEquals(spectrum.getFrequency(1), spectrum.getFrequency(spectrum.getNumberOfSamples()-1), 0.000001f);
    }

    @Test
    public void testEqualsHashCode() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum0 = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
        final LinearFrequencySpectrum spectrum1 = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
        final LinearFrequencySpectrum spectrum2 = new LinearFrequencySpectrum(frameNumber+1, realData, imaginaryData, audioFormat);

        assertEquals(spectrum0, spectrum1);
        assertEquals(spectrum0.hashCode(), spectrum1.hashCode());
        assertNotEquals(spectrum0, spectrum2);
        assertNotEquals(spectrum0.hashCode(), spectrum2.hashCode());
    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);

        assertTrue(Pattern.matches("LinearFrequencySpectrum\\{audioFormat=PCM_SIGNED 10\\.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian, numberOfSamples=6, timestamp=300, frameNumber=3, realData=\\[.+, imaginaryData=\\[.+\\}", spectrum.toString()));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
        final LinearFrequencySpectrum clone = (LinearFrequencySpectrum)spectrum.clone();
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

        assertEquals(3.3333335f, clone.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/realData.length, clone.getBandwidth(), 0.000001f);
        assertEquals(clone.getFrequency(1), clone.getBandwidth(), 0.000001f);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
        final LinearFrequencySpectrum copy = new LinearFrequencySpectrum(spectrum);
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

        assertEquals(3.3333335f, copy.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/realData.length, copy.getBandwidth(), 0.000001f);
        assertEquals(copy.getFrequency(1), copy.getBandwidth(), 0.000001f);
    }

    @Test
    public void testReuse() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
        spectrum.reuse(4, new float[] {1, 2}, null, audioFormat);
        // Todo: real test!
    }

    @Test
    public void testDeviations() {
        final int frameNumber = 3;
        final float[] realData = new float[880];
        final AudioFormat audioFormat = new AudioFormat(440*2, 16, 2, true, false);
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(frameNumber, realData, null, audioFormat);
        final int[] deviationsInCents = spectrum.getDeviationsInCents();
        assertEquals(spectrum.getMagnitudes().length, deviationsInCents.length);
        assertEquals(0, deviationsInCents[0]);
        assertEquals(0, deviationsInCents[deviationsInCents.length/2]);
        assertEquals(0, deviationsInCents[deviationsInCents.length/4]);
    }

    private static float[] toMagnitudes(final float[] real, final float[] imaginary) {
        final float[] magnitudes = new float[real.length/2];
        for (int i=0; i<magnitudes.length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            magnitudes[i] = (float)Math.sqrt(real[i] * real[i] + v * v);
        }
        return magnitudes;
    }

    private static float[] toPowers(final float[] real, final float[] imaginary) {
        final float[] powers = new float[real.length/2];
        for (int i=0; i<powers.length; i++) {
            final float v = imaginary == null ? 0 : imaginary[i];
            powers[i] = real[i] * real[i] + v * v;
        }
        return powers;
    }
}
