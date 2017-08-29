/*
 * =================================================
 * Copyright 2017 tagtraum industries incorporated
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
 * TestInstantaneousFrequencySpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestInstantaneousFrequencySpectrum extends TestAudioBuffer {

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);
        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(magnitudes.length, spectrum.getNumberOfSamples());
        assertArrayEquals(magnitudes, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(frequencies, spectrum.getFrequencies(), 0.000001f);
        assertNull(spectrum.getImaginaryData());
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(magnitudes, null, magnitudes.length/2), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(magnitudes, null, magnitudes.length/2), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(magnitudes, null, magnitudes.length / 2), spectrum.getData(), 0.000001f);

        assertEquals(4f, spectrum.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/magnitudes.length, spectrum.getBandwidth(), 0.000001f);
        assertEquals(5f/3f, spectrum.getBandwidth(), 0.000001f);
    }

    @Test
    public void testFrequencies() {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 4, 3, 2};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);

        assertEquals(4f, spectrum.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/magnitudes.length, spectrum.getBandwidth(), 0.000001f);
        assertEquals(5f/3f, spectrum.getBandwidth(), 0.000001f);
        // too high bin
        assertEquals(0f, spectrum.getFrequency(Integer.MAX_VALUE), 0.000001f);
        // too low bin
        assertEquals(0f, spectrum.getFrequency(-1), 0.000001f);
        assertEquals(1, spectrum.getBin(spectrum.getFrequency(1)), 0.000001f);
        // symmetry
        assertEquals(spectrum.getFrequency(0), spectrum.getFrequency(spectrum.getNumberOfSamples()-1), 0.000001f);
    }

    @Test
    public void testEqualsHashCode() {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum0 = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);
        final InstantaneousFrequencySpectrum spectrum1 = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);
        final InstantaneousFrequencySpectrum spectrum2 = new InstantaneousFrequencySpectrum(frameNumber+1, magnitudes, frequencies, audioFormat);

        assertEquals(spectrum0, spectrum1);
        assertEquals(spectrum0.hashCode(), spectrum1.hashCode());
        assertNotEquals(spectrum0, spectrum2);
        assertNotEquals(spectrum0.hashCode(), spectrum2.hashCode());
    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);

        assertTrue(Pattern.matches("InstantaneousFrequencySpectrum\\{audioFormat=PCM_SIGNED 10\\.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian, numberOfSamples=6, timestamp=300, frameNumber=3, magnitudes=\\[.+, frequencies=\\[.+\\}", spectrum.toString()));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);
        final InstantaneousFrequencySpectrum clone = (InstantaneousFrequencySpectrum)spectrum.clone();
        assertEquals(frameNumber, clone.getFrameNumber());
        assertEquals(magnitudes.length, clone.getNumberOfSamples());
        assertArrayEquals(magnitudes, clone.getRealData(), 0.000001f);
        assertArrayEquals(frequencies, clone.getFrequencies(), 0.000001f);
        assertEquals(audioFormat, clone.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), clone.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), clone.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(magnitudes, null, magnitudes.length/2), clone.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(magnitudes, null, magnitudes.length/2), clone.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(magnitudes, null, magnitudes.length/2), clone.getData(), 0.000001f);

        assertEquals(4f, clone.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/magnitudes.length, clone.getBandwidth(), 0.000001f);
        assertEquals(5f/3f, clone.getBandwidth(), 0.000001f);
    }

    @Test
    public void testDerive() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);

        final float[] derivedMagnitudes = {3, 4, 5, 6, 7, 8};
        final float[] derivedFrequencies = {1, 3, 5, 7, 1, 2};
        final InstantaneousFrequencySpectrum derived = spectrum.derive(derivedMagnitudes, derivedFrequencies);
        assertEquals(frameNumber, derived.getFrameNumber());
        assertEquals(derivedMagnitudes.length, derived.getNumberOfSamples());
        assertArrayEquals(derivedMagnitudes, derived.getRealData(), 0.000001f);
        assertArrayEquals(derivedFrequencies, derived.getFrequencies(), 0.000001f);
        assertEquals(audioFormat, derived.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), derived.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), derived.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toPowers(derivedMagnitudes, null, derivedMagnitudes.length/2), derived.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(derivedMagnitudes, null, derivedMagnitudes.length/2), derived.getMagnitudes(), 0.000001f);
        assertArrayEquals(toMagnitudes(derivedMagnitudes, null, derivedMagnitudes.length/2), derived.getData(), 0.000001f);

        assertEquals(5f, derived.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/magnitudes.length, derived.getBandwidth(), 0.000001f);
        assertEquals(5f/3f, derived.getBandwidth(), 0.000001f);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);
        final InstantaneousFrequencySpectrum copy = new InstantaneousFrequencySpectrum(spectrum);
        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(magnitudes.length, copy.getNumberOfSamples());
        assertArrayEquals(magnitudes, copy.getRealData(), 0.000001f);
        assertArrayEquals(frequencies, copy.getFrequencies(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(magnitudes, null, magnitudes.length/2), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(magnitudes, null, magnitudes.length/2), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(magnitudes, null, magnitudes.length/2), copy.getData(), 0.000001f);

        assertEquals(4f, copy.getFrequency(2), 0.000001f);
        assertEquals(audioFormat.getSampleRate()/magnitudes.length, copy.getBandwidth(), 0.000001f);
        assertEquals(5f/3f, copy.getBandwidth(), 0.000001f);
    }

    @Test
    public void testReuse() {
        final int frameNumber = 3;
        final float[] magnitudes = {1, 2, 3, 4, 5, 6};
        final float[] frequencies = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final InstantaneousFrequencySpectrum spectrum = new InstantaneousFrequencySpectrum(frameNumber, magnitudes, frequencies, audioFormat);
        spectrum.reuse(4, new float[] {1, 2}, null, audioFormat);
        // Todo: real test!
    }

    @Test
    public void testLinearFrequencySpectrumConstructor() {
        final float[] realValues = {1, 2, 3, 4, 5, 6};
        final float[] imagValues = {1, 1, 1, 1, 1, 1};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 1, true, false);

        final LinearFrequencySpectrum linearSpectrum1 = new LinearFrequencySpectrum(2, realValues, imagValues, audioFormat);
        final LinearFrequencySpectrum linearSpectrum2 = new LinearFrequencySpectrum(3, realValues, imagValues, audioFormat);
        final InstantaneousFrequencySpectrum instantaneousSpectrum = new InstantaneousFrequencySpectrum(linearSpectrum1, linearSpectrum2);

        assertEquals(linearSpectrum1.getFrameNumber(), instantaneousSpectrum.getFrameNumber());

        assertEquals(realValues.length, instantaneousSpectrum.getNumberOfSamples());

        assertArrayEquals(toMagnitudes(realValues, imagValues, realValues.length/2), instantaneousSpectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realValues, imagValues, realValues.length/2), instantaneousSpectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realValues, imagValues, realValues.length/2), instantaneousSpectrum.getData(), 0.000001f);

        assertEquals(audioFormat, instantaneousSpectrum.getAudioFormat());
        assertEquals((long)(instantaneousSpectrum.getFrameNumber()*1000L/audioFormat.getSampleRate()), instantaneousSpectrum.getTimestamp());
        assertEquals((long) (instantaneousSpectrum.getFrameNumber() * 1000L * 1000L / audioFormat.getSampleRate()), instantaneousSpectrum.getTimestamp(TimeUnit.MICROSECONDS));

        // we have two spectra, that are exactly the same -> frequencies must be 0
        assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, instantaneousSpectrum.getFrequencies(), 0.000001f);
    }

}
