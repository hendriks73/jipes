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
public class TestInterpolator {


    @Test
    public void testInterpolator() {

        final float[] data = new float[]{1,2,1,2,1,2,1,2,1,2,1,2,1,2};

        final MultirateFilters.Interpolator interpolator = new MultirateFilters.Interpolator(4);
        final float[] interpolated = interpolator.map(data);

        final Filters.FIRFilter lowPassFilter = Filters.createFir1_16thOrderLowpassCutoffQuarter();
        final float[] upSampled = new float[data.length*4];
        for (int i=0; i<data.length; i++) {
            upSampled[i*4] = data[i];
        }
        final float[] lowPassed = lowPassFilter.map(upSampled);

        assertTrue(Arrays.equals(lowPassed, interpolated));
    }

}
