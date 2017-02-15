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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestMelSpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMelSpectrum extends TestAudioBuffer {

    @Test
    public void testFromLinearFrequencySpectrum() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
        final LinearFrequencySpectrum linearFrequencySpectrum = new LinearFrequencySpectrum(frameNumber, realData, imaginaryData, audioFormat);
        final int channels = 5;
        final MelSpectrum spectrum = new MelSpectrum(frameNumber, linearFrequencySpectrum, 3000, 6000, channels, false);

        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(channels, spectrum.getNumberOfSamples());
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));
        assertEquals(channels, spectrum.getNumberOfBands());
        assertEquals(channels, spectrum.getFrequencies().length);

        // TODO: this is not a full test. Complete!
    }

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7, 8, 9};
        final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);

        // these boundaries follow the pattern: L C C C C R, for 4 channels
        final float[] boundariesInHz = {3000, 3500, 4000, 4500, 5000, 5500};
        final float[] frequencies = {3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500};
        final float[][] filterBank = MelSpectrum.createFilterBank(frequencies, boundariesInHz);
        final MelSpectrum spectrum = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);

        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.0001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.0001f);

        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(realData.length, spectrum.getNumberOfSamples());
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));
        assertEquals(filterBank.length, spectrum.getNumberOfBands());
        assertEquals(boundariesInHz.length-2, spectrum.getFrequencies().length);

        // TODO: this is not a full test. Complete!
    }

    @Test
    public void testNullImaginary() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8};
        final float[] imaginaryData = null;
        final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);

        // these boundaries follow the pattern: L C C C C R, for 4 channels
        final float[] boundariesInHz = {3000, 3500, 4000, 4500, 5000, 5500};
        final float[] frequencies = {3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500};
        final float[][] filterBank = MelSpectrum.createFilterBank(frequencies, boundariesInHz);
        final MelSpectrum spectrum = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);

        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.0001f);
        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.0001f);
    }

    @Test
    public void testEqualsHashCode() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7, 8, 9};
        final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);

        // these boundaries follow the pattern: L C C C C R, for 4 channels
        final float[] boundariesInHz = {3000, 3500, 4000, 4500, 5000, 5500};
        final float[] frequencies = {3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500};
        final float[][] filterBank = MelSpectrum.createFilterBank(frequencies, boundariesInHz);

        final MelSpectrum spectrum0 = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);
        final MelSpectrum spectrum1 = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);
        final MelSpectrum spectrum2 = new MelSpectrum(frameNumber+1, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);

        assertEquals(spectrum0, spectrum1);
        assertEquals(spectrum0.hashCode(), spectrum1.hashCode());
        assertNotEquals(spectrum0, spectrum2);
        assertNotEquals(spectrum0.hashCode(), spectrum2.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7, 8, 9};
        final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);

        // these boundaries follow the pattern: L C C C C R, for 4 channels
        final float[] boundariesInHz = {3000, 3500, 4000, 4500, 5000, 5500};
        final float[] frequencies = {3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500};
        final float[][] filterBank = MelSpectrum.createFilterBank(frequencies, boundariesInHz);

        final MelSpectrum spectrum = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);
        final MelSpectrum clone = (MelSpectrum)spectrum.clone();

        assertEquals(frameNumber, clone.getFrameNumber());
        assertEquals(realData.length, clone.getNumberOfSamples());
        assertEquals(audioFormat, clone.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), clone.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), clone.getTimestamp(TimeUnit.MICROSECONDS));
        assertEquals(filterBank.length, clone.getNumberOfBands());
        assertEquals(boundariesInHz.length-2, clone.getFrequencies().length);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7, 8, 9};
        final AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);

        // these boundaries follow the pattern: L C C C C R, for 4 channels
        final float[] boundariesInHz = {3000, 3500, 4000, 4500, 5000, 5500};
        final float[] frequencies = {3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500};
        final float[][] filterBank = MelSpectrum.createFilterBank(frequencies, boundariesInHz);

        final MelSpectrum spectrum = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);
        final MelSpectrum clone = new MelSpectrum(spectrum);

        assertEquals(frameNumber, clone.getFrameNumber());
        assertEquals(realData.length, clone.getNumberOfSamples());
        assertEquals(audioFormat, clone.getAudioFormat());
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), clone.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), clone.getTimestamp(TimeUnit.MICROSECONDS));
        assertEquals(filterBank.length, clone.getNumberOfBands());
        assertEquals(boundariesInHz.length-2, clone.getFrequencies().length);

    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7, 8, 9};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);

        // these boundaries follow the pattern: L C C C C R, for 4 channels
        final float[] boundariesInHz = {3000, 3500, 4000, 4500, 5000, 5500};
        final float[] frequencies = {3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500};
        final float[][] filterBank = MelSpectrum.createFilterBank(frequencies, boundariesInHz);
        final MelSpectrum spectrum = new MelSpectrum(frameNumber, realData, imaginaryData, audioFormat, filterBank, boundariesInHz);

        assertEquals("MelSpectrum{timestamp=300, frameNumber=3, channels=4}", spectrum.toString());
    }

}
