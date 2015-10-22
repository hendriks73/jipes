/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

/**
 * Transform for signal estimation from modified short-time fourier
 * transform by Daniel W. Griffin and Jae and S. Lim.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.306.7858&rep=rep1&type=pdf">Signal estimation from modified short-time fourier transform</a>
 */
public class GriffinLim implements Transform {

    private final Transform transform;
    private final int iterations;
    private float[] realEstimate;
    private float[] imaginaryEstimate;

    /**
     * @param realStart first real estimate
     * @param imaginaryStart first imaginary estimate (may be empty array)
     * @param iterations number of iterations to perform
     */
    public GriffinLim(final float[] realStart, final float[] imaginaryStart, final int iterations) {
        this.realEstimate = realStart;
        this.imaginaryEstimate = imaginaryStart;
        this.transform = FFTFactory.getInstance().create(realEstimate.length);
        this.iterations = iterations;
    }

    // the magnitude here is really only the first half of the symmetric FFT output
    @Override
    public float[][] transform(final float[] magnitudes) throws UnsupportedOperationException {
        if (magnitudes.length * 2 != this.realEstimate.length) throw new IllegalArgumentException("Magnitudes must not be symmetric, i.e. half the length of the output samples.");
        boolean allZero = true;
        for (final float f : magnitudes) {
            if (f != 0) allZero = false;
        }
        if (allZero) {
            final float[][] f = new float[2][];
            f[0] = new float[magnitudes.length*2];
            f[1] = new float[magnitudes.length*2];
            return f;
        }
        float[][] complexSignal;
        int it = 0;
        double lastDistance = Double.MAX_VALUE;
        do {
            // Fourier transform of the estimated signal
            final float[][] transform = this.transform.transform(realEstimate, imaginaryEstimate);
            double distance = 0;
            // Scale transform values to magnitudes of the desired magnitude spectrum
            final float[] r = transform[0];
            final float[] i = transform[1];
            for (int j=0; j<transform[0].length; j++) {
                final float magnitudej = j<magnitudes.length ? magnitudes[j] : magnitudes[magnitudes.length*2-j-1];
                final float rj = r[j];
                final float ij = i[j];
                final float transformMagnitudes = (float) Math.sqrt(rj * rj + ij * ij);
                distance += (transformMagnitudes - magnitudej) * (transformMagnitudes - magnitudej);
                if (magnitudej == 0) {
                    r[j] = 0;
                    i[j] = 0;
                } else {
                    final float factor = transformMagnitudes == 0
                            ? 0
                            : magnitudej / transformMagnitudes;
                    r[j] *= factor;
                    i[j] *= factor;
                }
            }
            lastDistance = distance;
            // Perform inverse Fourier transform of the re-scaled frequency domain spectrum
            complexSignal = this.transform.inverseTransform(r, i);
            // new estimated signal...
            realEstimate = complexSignal[0];
            // ignore imaginary part
            imaginaryEstimate = new float[realEstimate.length];
            it++;
        } while (condition(lastDistance, it));
        return complexSignal;
    }

    private boolean condition(final double distance, final int iteration) {
        // for now we ignore distance
        return iteration != this.iterations;
    }

    @Override
    public float[][] inverseTransform(final float[] real, final float[] imaginary) throws UnsupportedOperationException {
        return transform(real);
    }

    @Override
    public float[][] transform(final float[] real, final float[] imaginary) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
