/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.math.Floats;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * TestMultiBandSpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMultiBandSpectrum extends TestAudioBuffer {

    @Test(expected = IllegalArgumentException.class)
    public void testBadNumberOfBands() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] boundaries = {2, 3, 4, 5, 9, 10};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        new MultiBandSpectrum(frameNumber, realData, null, audioFormat, boundaries);
    }

    @Test
    public void testBasics() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] boundaries = {2, 3, 4, 5, 9, 10, 11};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);
        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(realData.length, spectrum.getNumberOfSamples());
        assertArrayEquals(realData, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, spectrum.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals(boundaries.length-1, spectrum.getNumberOfBands());
        assertArrayEquals(boundaries, spectrum.getBandBoundaries(), 0.0001f);
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getData(), 0.000001f);

        assertEquals(4.5f, spectrum.getFrequency(2), 0.000001f);
    }

    @Test
    public void testNullImaginary() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = null;
        final float[] boundaries = {2, 3, 4, 5, 9, 10, 11};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);
        assertEquals(frameNumber, spectrum.getFrameNumber());
        assertEquals(realData.length, spectrum.getNumberOfSamples());
        assertArrayEquals(realData, spectrum.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, spectrum.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, spectrum.getAudioFormat());
        assertEquals(boundaries.length-1, spectrum.getNumberOfBands());
        assertArrayEquals(boundaries, spectrum.getBandBoundaries(), 0.0001f);
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), spectrum.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), spectrum.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), spectrum.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), spectrum.getData(), 0.000001f);

        assertEquals(4.5f, spectrum.getFrequency(2), 0.000001f);
    }

    @Test
    public void testCopyConstructor() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] boundaries = {2, 3, 4, 5, 9, 10, 11};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);

        final MultiBandSpectrum copy = new MultiBandSpectrum(spectrum);

        assertEquals(frameNumber, copy.getFrameNumber());
        assertEquals(realData.length, copy.getNumberOfSamples());
        assertArrayEquals(realData, copy.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, copy.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, copy.getAudioFormat());
        assertEquals(boundaries.length-1, copy.getNumberOfBands());
        assertArrayEquals(boundaries, copy.getBandBoundaries(), 0.0001f);
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), copy.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), copy.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), copy.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), copy.getData(), 0.000001f);

        assertEquals(4.5f, copy.getFrequency(2), 0.000001f);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] boundaries = {2, 3, 4, 5, 9, 10, 11};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);

        final MultiBandSpectrum clone = (MultiBandSpectrum)spectrum.clone();

        assertEquals(frameNumber, clone.getFrameNumber());
        assertEquals(realData.length, clone.getNumberOfSamples());
        assertArrayEquals(realData, clone.getRealData(), 0.000001f);
        assertArrayEquals(imaginaryData, clone.getImaginaryData(), 0.000001f);
        assertEquals(audioFormat, clone.getAudioFormat());
        assertEquals(boundaries.length-1, clone.getNumberOfBands());
        assertArrayEquals(boundaries, clone.getBandBoundaries(), 0.0001f);
        assertEquals((long)(frameNumber*1000L/audioFormat.getSampleRate()), clone.getTimestamp());
        assertEquals((long) (frameNumber * 1000L * 1000L / audioFormat.getSampleRate()), clone.getTimestamp(TimeUnit.MICROSECONDS));

        assertArrayEquals(toMagnitudes(realData, imaginaryData), clone.getMagnitudes(), 0.000001f);
        assertArrayEquals(toPowers(realData, imaginaryData), clone.getPowers(), 0.000001f);
        assertArrayEquals(toMagnitudes(realData, imaginaryData), clone.getData(), 0.000001f);

        assertEquals(4.5f, clone.getFrequency(2), 0.000001f);
    }

    @Test
    public void testCreateMidiBands() {
        final float[] midiBands = MultiBandSpectrum.createMidiBands(10, 20);
        for (int i=0; i<midiBands.length-1; i++) {
            final float midi0 = frequencyToMidi(midiBands[i]);
            final float midi1 = frequencyToMidi(midiBands[i+1]);
            assertEquals("Index/band " + i + " is not the same", midi0, midi1-1, 0.0001);
        }
    }

    @Test
    public void testToString() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] boundaries = {2, 3, 4, 5, 9, 10, 11};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);
        assertTrue(Pattern.matches("MultiBandSpectrum\\{timestamp=300, frameNumber=3, bandBoundariesInHz=\\[.+, numberOfBands=6}", spectrum.toString()));
    }

    @Test
    public void testHashCodeEquals() {
        final int frameNumber = 3;
        final float[] realData = {1, 2, 3, 4, 5, 6};
        final float[] imaginaryData = {2, 3, 4, 5, 6, 7};
        final float[] boundaries = {2, 3, 4, 5, 9, 10, 11};
        final AudioFormat audioFormat = new AudioFormat(10, 16, 2, true, false);
        final MultiBandSpectrum spectrum0 = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);
        final MultiBandSpectrum spectrum1 = new MultiBandSpectrum(frameNumber, realData, imaginaryData, audioFormat, boundaries);
        final MultiBandSpectrum spectrum2 = new MultiBandSpectrum(frameNumber+1, realData, imaginaryData, audioFormat, boundaries);

        assertEquals(spectrum0, spectrum1);
        assertEquals(spectrum0.hashCode(), spectrum1.hashCode());
        assertNotEquals(spectrum0, spectrum2);
        assertNotEquals(spectrum0.hashCode(), spectrum2.hashCode());
    }

    private static float frequencyToMidi(final float frequency) {
        return (float) (69 + 12.0 * Floats.log2(frequency / 440.0f));
    }

    @Test
    public void testOneBinPerBand() {
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final float[] imaginaryData = new float[realData.length];
        final AudioFormat audioFormat = new AudioFormat(200, 16, 1, true, false);
        final LinearFrequencySpectrum linearSpectrum = new LinearFrequencySpectrum(0, realData, imaginaryData, audioFormat);
        final float[] boundaries = {0, 10, 20, 30, 40, 50, 60};
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(0, linearSpectrum, boundaries);
        assertArrayEquals(new float[]{1, 2, 3, 4, 5, 6}, spectrum.getData(), 0.0001f);
    }

    @Test
    public void testTwoBandsPerBin() {
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final float[] imaginaryData = new float[realData.length];
        final AudioFormat audioFormat = new AudioFormat(400, 16, 1, true, false);
        final LinearFrequencySpectrum linearSpectrum = new LinearFrequencySpectrum(0, realData, imaginaryData, audioFormat);
        final float[] boundaries = {0, 10, 20, 30, 40, 50, 60};
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(0, linearSpectrum, boundaries);

        // TODO: This is not ideal. The power should be spread over multiple bands instead of poured into the first one that matches.

        assertArrayEquals(new float[]{1, 0, 2, 0, 3, 0}, spectrum.getData(), 0.0001f);
    }

    @Test
    public void testTwoBinsPerBand() {
        final float[] realData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final float[] imaginaryData = new float[realData.length];
        final AudioFormat audioFormat = new AudioFormat(100, 16, 1, true, false);
        final LinearFrequencySpectrum linearSpectrum = new LinearFrequencySpectrum(0, realData, imaginaryData, audioFormat);
        final float[] boundaries = {0, 10, 20, 30, 40, 50, 60};
        final MultiBandSpectrum spectrum = new MultiBandSpectrum(0, linearSpectrum, boundaries);
        assertArrayEquals(new float[]{
            (float)Math.sqrt(1*1 + 2*2),
            (float)Math.sqrt(3*3 + 4*4),
            (float)Math.sqrt(5*5 + 6*6),
            (float)Math.sqrt(7*7 + 8*8),
            (float)Math.sqrt(9*9 + 10*10),
            0
        }, spectrum.getData(), 0.0001f);
    }
}
