/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import java.util.Arrays;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

/**
 * Classic window functions like triangle, Hamming and Hann.
 * <p/>
 * <em>Note that some implementations may re-use their output buffer!</em>
 * <p/>
 * Date: 5/17/11
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * @see <a href="http://en.wikipedia.org/wiki/Window_function">Window functions on Wikipedia</a>
 */
public final class WindowFunctions {

    private static final double DOUBLE_PI = 2.0 * PI;

    private WindowFunctions() {
    }

    /**
     * Triangle function that always uses a triangle that is as long as the provided data.
     */
    public static MapFunction<float[]> TRIANGLE = new MapFunction<float[]>() {
        public float[] map(final float[] array) {
            return new Triangle(array.length).map(array);
        }

        @Override
        public String toString() {
            return "TRIANGLE_WINDOW";
        }
    };

    /**
     * Triangle function that always uses a Hann window that is as long as the provided data.
     */
    public static MapFunction<float[]> HANN = new MapFunction<float[]>() {
        public float[] map(final float[] array) {
            return new Hann(array.length).map(array);
        }

        @Override
        public String toString() {
            return "HANN_WINDOW";
        }
    };

    /**
     * Triangle function that always uses a Hamming window that is as long as the provided data.
     */
    public static MapFunction<float[]> HAMMING = new MapFunction<float[]>() {
        public float[] map(final float[] array) {
            return new Hamming(array.length).map(array);
        }

        @Override
        public String toString() {
            return "HAMMING_WINDOW";
        }
    };

    /**
     * Triangle window function.
     * <p/>
     * This implementation re-uses its output buffer.
     * Do not hold on to it or rely on it staying unchanged. If you must hold on to it,
     * create a copy using {@link Object#clone()}.
     */
    public static class Triangle implements MapFunction<float[]> {

        private float[] scales;
        private float[] out;

        public Triangle(final int length) {
            this.scales = new float[length];
            this.out = new float[length];
            final float halfLength = length/2;
            for (int n = 0; n < length; n++) {
                if (n <= length / 2) {
                    scales[n] = (float) n/halfLength;
                } else {
                    scales[n] = 2.0f - (float) n/halfLength;
                }
            }
        }

        public float[] map(final float[] data) {
            if (data.length != scales.length) throw new IllegalArgumentException("Data length must equal scales length.");
            for (int i=0; i<data.length; i++) {
                out[i] = data[i] * scales[i];
            }
            return out;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Triangle other = (Triangle) o;
            if (scales.length != other.scales.length) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return scales != null ? Arrays.hashCode(scales) : 0;
        }

        @Override
        public String toString() {
            return "Triangle{" +
                    "length=" + scales.length +
                    '}';
        }
    }


    /**
     * Hann window function.
     * <p/>
     * This implementation re-uses its output buffer.
     * Do not hold on to it or rely on it staying unchanged. If you must hold on to it,
     * create a copy using {@link Object#clone()}.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Window_function#Hann_window">Hann Window</a>
     */
    public static class Hann implements MapFunction<float[]> {

        private float[] scales;
        private float[] out;

        public Hann(final int length) {
            this.scales = new float[length];
            this.out = new float[length];
            final int lengthMinus1 = length - 1;
            for (int n = 0; n < length; n++) {
                final double cosArg = (DOUBLE_PI * n) / lengthMinus1;
                scales[n] = (float) (0.5 - 0.5 * cos(cosArg));
            }
        }

        public float[] map(final float[] data) {
            if (data.length != scales.length) throw new IllegalArgumentException("Data length must equal scales length.");
            for (int i=0; i<data.length; i++) {
                out[i] = data[i] * scales[i];
            }
            return out;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Hann other = (Hann) o;
            if (scales.length != other.scales.length) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return scales != null ? Arrays.hashCode(scales) : 0;
        }

        @Override
        public String toString() {
            return "Hann{" +
                    "length=" + scales.length +
                    '}';
        }
    }

    /**
     * Hamming window function.
     * <p/>
     * This implementation re-uses its output buffer.
     * Do not hold on to it or rely on it staying unchanged. If you must hold on to it,
     * create a copy using {@link Object#clone()}.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Window_function#Hamming_window">Hamming Window</a>
     */
    public static class Hamming implements MapFunction<float[]> {

        private float[] scales;
        private float[] out;

        public Hamming(final int length) {
            this.scales = new float[length];
            this.out = new float[length];
            final int lengthMinus1 = length - 1;
            for (int n = 0; n < length; n++) {
                final double cosArg = (DOUBLE_PI * n) / lengthMinus1;
                scales[n] = (float) (0.54 - 0.46 * cos(cosArg));
            }
        }

        public float[] map(final float[] data) {
            if (data.length != scales.length) throw new IllegalArgumentException("Data length must equal scales length.");
            for (int i=0; i<data.length; i++) {
                out[i] = data[i] * scales[i];
            }
            return out;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Hamming other = (Hamming) o;
            if (scales.length != other.scales.length) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return scales != null ? Arrays.hashCode(scales) : 0;
        }

        @Override
        public String toString() {
            return "Hamming{" +
                    "length=" + scales.length +
                    '}';
        }
    }
}
