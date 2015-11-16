/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.math.Floats;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TestMultiBandSpectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMultiBandSpectrum {

    @Test
    public void testCreateMidiBands() {
        final float[] midiBands = MultiBandSpectrum.createMidiBands(10, 20);
        for (int i=0; i<midiBands.length-1; i++) {
            final float midi0 = frequencyToMidi(midiBands[i]);
            final float midi1 = frequencyToMidi(midiBands[i+1]);
            assertEquals("Index/band " + i + " is not the same", midi0, midi1-1, 0.0001);
        }
    }

    private static float frequencyToMidi(final float frequency) {
        return (float) (69 + 12.0 * Floats.log2(frequency / 440.0f));
    }

}
