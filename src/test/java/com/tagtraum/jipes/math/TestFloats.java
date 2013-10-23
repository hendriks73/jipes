/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TestFloats.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFloats {


    @Test
    public void testSimplePeak() {
        final int[] peaks = Floats.peaks(new float[]{0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1}, 2, true);
        assertEquals(1, peaks.length);
        assertEquals(6, peaks[0]);
    }

    @Test
    public void testFalsePeak() {
        final int[] peaks = Floats.peaks(new float[]{0, 1, 2, 3, 4, 5, 6, 5, 6, 5, 4, 3}, 2, true);
        assertEquals(0, peaks.length);
    }


    @Test
    public void testNonStrictPeak() {
        final int[] peaks = Floats.peaks(new float[]{0, 0, 2, 0, 0, 0}, 2, false);
        assertEquals(1, peaks.length);
    }
}
