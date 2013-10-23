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
 * TestResampler.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestResampler {

    @Test
    public void testDecimating() {

        final float[] data = new float[]{1,2,1,2,1,2,1,2,1,2,1,2,1,2};

        final MultirateFilters.Resampler resampler = new MultirateFilters.Resampler(1, 2);
        final float[] decimated = resampler.map(data);


        final Filters.FIRFilter lowPassFilter = Filters.createFir1_16thOrderLowpassCutoffHalf();
        final float[] lowPassed = lowPassFilter.map(data);
        final float[] downSampled = new float[lowPassed.length/2];
        for (int i=0; i<lowPassed.length; i+=2) {
            downSampled[i/2] = lowPassed[i];
        }

        assertTrue(Arrays.equals(downSampled, decimated));
    }


    @Test
    public void testInterpolating() {

        final float[] data = new float[]{1,2,1,2,1,2,1,2,1,2,1,2,1,2};

        final MultirateFilters.Resampler resampler = new MultirateFilters.Resampler(4, 1);
        final float[] interpolated = resampler.map(data);

        final Filters.FIRFilter lowPassFilter = Filters.createFir1_16thOrderLowpassCutoffQuarter();
        final float[] upSampled = new float[data.length*4];
        for (int i=0; i<data.length; i++) {
            upSampled[i*4] = data[i];
        }
        final float[] lowPassed = lowPassFilter.map(upSampled);

        assertTrue(Arrays.equals(lowPassed, interpolated));
    }


}
