/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;
import org.junit.Ignore;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * TestMel.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMel {

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


    @Test
    @Ignore
    public void testOneBand() throws IOException {
        final Mel<LinearFrequencySpectrum> processor = new Mel<LinearFrequencySpectrum>(1000, 3000, 1);
        processor.connectTo(constSource);
        MelSpectrum melSpectrum = processor.read();
        final float[] real = melSpectrum.getRealData();
        assertEquals(1, real.length);
        assertEquals(Math.sqrt(300), real[0], 0.0001);
        final float[] powers = melSpectrum.getPowers();
        assertEquals(1, real.length);
        assertEquals(300, powers[0], 0.0001);
    }

    @Test
    @Ignore
    public void testThreeBands() throws IOException {
        final Mel<LinearFrequencySpectrum> processor = new Mel<LinearFrequencySpectrum>(1000, 3000, 3);
        processor.connectTo(constSource);
        final float[] powers = processor.read().getPowers();
        assertEquals(3, powers.length);
        assertEquals(200, powers[0], 0.0001);
        assertEquals(100, powers[1], 0.0001);
        assertEquals(100, powers[2], 0.0001);
    }

    @Test
    public void testGetBin() throws IOException {
        final Mel<LinearFrequencySpectrum> processor = new Mel<LinearFrequencySpectrum>(1000, 3000, 2);
        final float[] bandBoundaries = processor.getBandBoundaries();
        processor.connectTo(constSource);
        final MelSpectrum spectrum = processor.read();
        assertEquals(-1, spectrum.getBin(999));
        assertEquals(0, spectrum.getBin(1000));
        assertEquals(0, spectrum.getBin(bandBoundaries[1] + (bandBoundaries[2] - bandBoundaries[1])/2 - 1));
        assertEquals(1, spectrum.getBin(bandBoundaries[1] + (bandBoundaries[2] - bandBoundaries[1])/2));
        assertEquals(1, spectrum.getBin(2999));
        assertEquals(-1, spectrum.getBin(3000));
        assertEquals(-1, spectrum.getBin(3001));
    }

}
