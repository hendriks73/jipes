/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;
import junit.framework.TestCase;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

/**
 * TestMultiBand.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMultiBand extends TestCase {
    private SignalSource<LinearFrequencySpectrum> constSource = new SignalSource<LinearFrequencySpectrum>() {
        public AudioFormat getProcessedAudioFormat() {
            return new AudioFormat(10000, 32, 1, true, true);
        }

        public void reset() {
        }

        public LinearFrequencySpectrum read() throws IOException {
            return new LinearFrequencySpectrum(0, new float[] {
                    10, 10, 10, 10, 10,
                    10, 10, 10, 10, 10,
                    10, 10, 10, 10, 10,
                    10
            }, new float[16], new AudioFormat(10000, 32, 1, true, true));
        }
    };


    public void testOneBand() throws IOException {
        final float[] logarithmicBands = MultiBandSpectrum.createLogarithmicBands(1000, 3000, 1);
        final MultiBand<LinearFrequencySpectrum> processor = new MultiBand<LinearFrequencySpectrum>(logarithmicBands);
        processor.connectTo(constSource);
        MultiBandSpectrum multiBandSpectrum = processor.read();
        final float[] real = multiBandSpectrum.getRealData();
        assertEquals(1, real.length);
        assertEquals(Math.sqrt(300), real[0], 0.0001);
        final float[] powers = multiBandSpectrum.getPowers();
        assertEquals(1, real.length);
        assertEquals(300, powers[0], 0.0001);
    }

    public void testThreeBands() throws IOException {
        final MultiBand<LinearFrequencySpectrum> processor = new MultiBand<LinearFrequencySpectrum>(MultiBandSpectrum.createLogarithmicBands(2500, 5000, 3));
        processor.connectTo(constSource);
        final float[] powers = processor.read().getPowers();
        assertEquals(3, powers.length);
        assertEquals(200, powers[0], 0.0001);
        assertEquals(100, powers[1], 0.0001);
        assertEquals(100, powers[2], 0.0001);
    }

    public void testGetBin() throws IOException {
        final MultiBand<LinearFrequencySpectrum> processor = new MultiBand<LinearFrequencySpectrum>(new float[]{1000, 2000, 3000});
        processor.connectTo(constSource);
        final MultiBandSpectrum spectrum = processor.read();
        assertEquals(-1, spectrum.getBin(999));
        assertEquals(0, spectrum.getBin(1000));
        assertEquals(0, spectrum.getBin(1999));
        assertEquals(1, spectrum.getBin(2000));
        assertEquals(1, spectrum.getBin(2999));
        assertEquals(-1, spectrum.getBin(3000));
        assertEquals(-1, spectrum.getBin(3001));
    }


    public void testNullGenerator() throws IOException {
        final MultiBand<AudioSpectrum> processor = new MultiBand<AudioSpectrum>(new float[] {50, 60});
        processor.connectTo(new NullAudioSpectrumSource());
        assertNull(processor.read());
    }}
