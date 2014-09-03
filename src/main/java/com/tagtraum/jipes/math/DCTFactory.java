/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for DC{@link com.tagtraum.jipes.math.Transform}s. Using the factory allows for sliding-in a faster, perhaps
 * native implementation.
 * <p/>
 * In order to use a factory other than the default factory, you need to specify
 * its classname with the system property <code>com.tagtraum.jipes.math.DCTFactory</code>.
 * I.e.
 * <xmp>-Dcom.tagtraum.jipes.math.DCTFactory=YOUR.CLASSNAME.HERE</xmp>
 * <p/>
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public abstract class DCTFactory {

    public static final String FACTORYCLASS_PROPERTY_NAME = DCTFactory.class.getName();
    private static Logger LOG = Logger.getLogger(DCTFactory.class.getName());
    private static DCTFactory instance;

    protected DCTFactory() {
    }

    /**
     * Creates a factory for DCT {@link com.tagtraum.jipes.math.Transform} objects.
     *
     * @return factory instance
     */
    public synchronized static DCTFactory getInstance() {
        if (instance == null) {
            final String factoryClassname = System.getProperty(FACTORYCLASS_PROPERTY_NAME);
            if (factoryClassname != null) {
                try {
                    instance = (DCTFactory) Class.forName(factoryClassname, true, Thread.currentThread().getContextClassLoader()).newInstance();
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Unable to instantiate configured DCTFactory \"" + factoryClassname + "\". Will fall back to standard implementation. Problem: " + e, e);
                }
            }
            if (instance == null) {
                // fall back to built-in factory
                instance = new FFTBasedDCTFactory();
            }
        }
        return instance;
    }

    /**
     * Creates an instance of the discrete cosine transform (DCT).
     * By specifying the number of samples implementations can pre-create lookup tables to speed
     * up the actual transform.
     *
     * @param numberOfSamples number of samples the DCT instance should be able to process
     * @return DCT instance
     */
    public abstract Transform create(int numberOfSamples);

    /**
     * Default implementation for a DCT factory.
     */
    private static class FFTBasedDCTFactory extends DCTFactory {

        private FFTBasedDCT last;

        @Override
        public synchronized Transform create(final int numberOfSamples) {
            if (last != null && last.numberOfSamples == numberOfSamples) return last;
            last = new FFTBasedDCT(numberOfSamples);
            return last;
        }
    }

    /**
     * Default implementation for a FFT based DCT using
     * the <a href="http://dsp.stackexchange.com/questions/2807/fast-cosine-transform-via-fft#10606">4N FFT, no shifts approach</a>.
     */
    private static class FFTBasedDCT implements Transform {

        private final int numberOfSamples;
        private final Transform fft;
        private float[] frequencies;

        private FFTBasedDCT(final int numberOfSamples) {
            final int fftLength = numberOfSamples * 4;
            if (!isPowerOfTwo(fftLength)) throw new IllegalArgumentException("N is not a power of 2");
            if (numberOfSamples <=0) throw new IllegalArgumentException("N must be greater than 0");
            this.numberOfSamples = numberOfSamples;
            this.fft = FFTFactory.getInstance().create(fftLength);

            this.frequencies = new float[numberOfSamples];
            for (int index=0; index<numberOfSamples; index++) {
                if (index <= numberOfSamples / 2) {
                    this.frequencies[index] = index / numberOfSamples;
                } else {
                    this.frequencies[index] = -((numberOfSamples - index) / (float) numberOfSamples);
                }
            }
        }

        public float[][] inverseTransform(final float[] real, final float[] imaginary) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public float[][] transform(final float[] real) throws UnsupportedOperationException {
            final float[] padded = new float[real.length * 4];
            for (int i=0; i<real.length; i++) {
                final int a = i * 2 + 1;
                final int b = padded.length - a;
                padded[a] = real[i];
                padded[b] = real[i];
            }
            final float[][] fftOut = fft.transform(padded);
            // for now we return only a single array of real values, no frequency array
            final float[][] out = new float[1][real.length];
            System.arraycopy(fftOut[0], 0, out[0], 0, real.length);
            return out;
        }

        public float[][] transform(final float[] real, final float[] imaginary) throws UnsupportedOperationException {
            return transform(real);
        }

        private static boolean isPowerOfTwo(final int number) {
            return (number & (number - 1)) == 0;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final FFTBasedDCT FFTBasedDCT = (FFTBasedDCT) o;
            if (numberOfSamples != FFTBasedDCT.numberOfSamples) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return numberOfSamples;
        }

        @Override
        public String toString() {
            return "FFTBasedDCT{" +
                    "N=" + numberOfSamples +
                    '}';
        }
    }


}
