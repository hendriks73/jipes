/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;
import com.tagtraum.jipes.math.DistanceFunction;
import com.tagtraum.jipes.math.Matrix;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * TestNovelty.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestNovelty {

    private SignalSource<AudioBuffer> monoSource = new SignalSource<AudioBuffer>() {

        private int frameNumber;

        public void reset() {
            frameNumber = 0;
        }

        public AudioBuffer read() throws IOException {
            if (frameNumber == 10) return null;
            final RealAudioBuffer buffer = new RealAudioBuffer(
                    frameNumber, new float[]{
                    10, 10, 10, 10, 10,
                    10, 10, 10, 10, 10,
            }, new AudioFormat(10000, 8, 1, true, true));
            frameNumber++;
            return buffer;
        }
    };

    @Test
    public void testEvenGaussianCheckerboardKernel() {
        // just prints the values
        int dimension = 4;
        final Novelty.GaussianCheckerboardKernel kernel = Novelty.GaussianCheckerboardKernel.getInstance(dimension);
        final Matrix k = kernel.getKernel();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.print(k.get(i, j) + "f, ");
            }
            System.out.println();
        }
        final float[] referenceValues = new float[]{
                0.011108996f, 0.082085f, -0.082085f, -0.011108996f,
                0.082085f, 0.60653067f, -0.60653067f, -0.082085f,
                -0.082085f, -0.60653067f, 0.60653067f, 0.082085f,
                -0.011108996f, -0.082085f, 0.082085f, 0.011108996f
        };
        for (int i = 0; i < referenceValues.length; i++) {
            assertEquals(referenceValues[i], k.get(i / dimension, i % dimension), 0.001f);
        }
        //assertTrue(Arrays.equals(new float[]{0.011108996f, 0.082085f, -0.082085f, -0.011108996f}, k[0]));
        //assertTrue(Arrays.equals(new float[]{0.082085f, 0.60653067f, -0.60653067f, -0.082085f}, k[1]));
        //assertTrue(Arrays.equals(new float[]{-0.082085f, -0.60653067f, 0.60653067f, 0.082085f}, k[2]));
        //assertTrue(Arrays.equals(new float[]{-0.011108996f, -0.082085f, 0.082085f, 0.011108996f}, k[3]));
    }

    @Test
    public void testOddGaussianCheckerboardKernel() {
        // just prints the values
        int dimension = 5;
        final Novelty.GaussianCheckerboardKernel kernel = Novelty.GaussianCheckerboardKernel.getInstance(dimension);
        final Matrix k = kernel.getKernel();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.print(k.get(i, j) + "f, ");
            }
            System.out.println();
        }
        final float[] referenceValues = new float[]{
                0.028565492f, 0.10836801f, 0.16901329f, -0.10836801f, -0.028565492f,
                0.10836801f, 0.41111225f, 0.6411804f, -0.41111225f, -0.10836801f,
                0.16901329f, 0.6411804f, 1.0f, -0.6411804f, -0.16901329f,
                -0.10836801f, -0.41111225f, -0.6411804f, 0.41111225f, 0.10836801f,
                -0.028565492f, -0.10836801f, -0.16901329f, 0.10836801f, 0.028565492f
        };
        for (int i = 0; i < referenceValues.length; i++) {
            assertEquals(referenceValues[i], k.get(i / dimension, i % dimension), 0.001f);
        }

        //assertTrue(Arrays.equals(new float[]{0.028565492f, 0.10836801f, 0.16901329f, -0.10836801f, -0.028565492f}, k[0]));
        //assertTrue(Arrays.equals(new float[]{0.10836801f, 0.41111225f, 0.6411804f, -0.41111225f, -0.10836801f}, k[1]));
        //assertTrue(Arrays.equals(new float[]{0.16901329f, 0.6411804f, 1.0f, -0.6411804f, -0.16901329f}, k[2]));
        //assertTrue(Arrays.equals(new float[]{-0.10836801f, -0.41111225f, -0.6411804f, 0.41111225f, 0.10836801f}, k[3]));
        //assertTrue(Arrays.equals(new float[]{-0.028565492f, -0.10836801f, -0.16901329f, 0.10836801f, 0.028565492f}, k[4]));
    }


    @Test
    public void testNoveltyPush() throws IOException {
        final DistanceFunction<AudioBuffer> distanceFunction = AudioBufferFunctions.createDistanceFunction(Novelty.createCosineDistanceFunction(4, 0, Integer.MAX_VALUE));
        final Novelty<AudioBuffer> novelty = new Novelty<AudioBuffer>("id", Novelty.GaussianCheckerboardKernel.getInstance(4),
                distanceFunction,
                false);
        assertEquals(distanceFunction, novelty.getDistanceFunction());
        final AudioFormat bogusAudioFormat = new AudioFormat(10f, 8, 1, true, true);
        for (int i = 0; i < 10; i++) {
            novelty.process(new RealAudioBuffer(2 * i, new float[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, bogusAudioFormat));
            novelty.process(new RealAudioBuffer(2 * i + 1, new float[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, bogusAudioFormat));
        }
        novelty.flush();
        final AudioBuffer noveltyCurve = novelty.getOutput();
        //System.out.println(Arrays.toString(noveltyCurve.getData()));
        for (final float n : noveltyCurve.getData()) {
            assertEquals(0.38283625f, n, 0.001f);
        }
    }

    @Test
    public void testNoveltyPull() throws IOException {
        final Novelty.GaussianCheckerboardKernel kernel = Novelty.GaussianCheckerboardKernel.getInstance(4);
        final Novelty<AudioBuffer> novelty = new Novelty<AudioBuffer>("id", kernel,
                AudioBufferFunctions.createDistanceFunction(Novelty.createCosineDistanceFunction(4, 0, Integer.MAX_VALUE)),
                false);
        assertEquals(kernel, novelty.getKernel());
        monoSource.reset();
        novelty.connectTo(monoSource);
        final AudioBuffer noveltyCurve = novelty.read();
        assertArrayEquals(new float[noveltyCurve.getData().length], noveltyCurve.getData(), 0.0001f);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessNext() throws IOException {
        final Novelty<AudioBuffer> novelty = new Novelty<AudioBuffer>() {
            @Override
            public AudioBuffer processNext(final AudioBuffer buffer) throws IOException {
                return super.processNext(buffer);
            }
        };
        novelty.processNext(null);
    }

    @Test
    public void testToString() throws IOException {
        final DistanceFunction<AudioBuffer> distanceFunction = AudioBufferFunctions.createDistanceFunction(Novelty.createCosineDistanceFunction(4, 0, Integer.MAX_VALUE));
        final Novelty<AudioBuffer> novelty = new Novelty<AudioBuffer>("id", Novelty.GaussianCheckerboardKernel.getInstance(4),
                distanceFunction,
                false);
        assertEquals("Novelty{kernel=GaussianCheckerboardKernel{size=4}, distanceFunction=<AudioBuffer>{CosineDistance{kernelSize=4, minBin=0, maxBin=2147483647}(data)}, padSimilarityMatrixWithZeros=false}", novelty.toString());
    }
}
