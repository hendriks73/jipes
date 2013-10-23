/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * TestDecimator.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestDecimator {


    @Test
    public void testDecimator() {

        final float[] data = new float[]{1,2,1,2,1,2,1,2,1,2,1,2,1,2};

        final MultirateFilters.Decimator decimator = new MultirateFilters.Decimator(Filters.createFir1_16thOrderLowpassCutoffHalf(), 2);
        final float[] decimated = decimator.map(data);


        final Filters.FIRFilter lowPassFilter = Filters.createFir1_16thOrderLowpassCutoffHalf();
        final float[] lowPassed = lowPassFilter.map(data);
        final float[] downSampled = new float[lowPassed.length/2];
        for (int i=0; i<lowPassed.length; i+=2) {
            downSampled[i/2] = lowPassed[i];
        }

        assertTrue(Arrays.equals(downSampled, decimated));
    }

}
