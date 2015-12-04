/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.math.AggregateFunction;
import com.tagtraum.jipes.math.DistanceFunction;
import com.tagtraum.jipes.math.Floats;
import com.tagtraum.jipes.math.MapFunction;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TestAudioBufferFunctions.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAudioBufferFunctions {

    @Test
    public void testPowerMapFunctionFunction() {
        final FloatIdentityFunction identityFunction = new FloatIdentityFunction();
        final MapFunction<AudioBuffer> function0 = AudioBufferFunctions.createPowerMapFunction(identityFunction);
        final MapFunction<AudioBuffer> function1 = AudioBufferFunctions.createPowerMapFunction(identityFunction);
        final AudioBuffer mapped0 = function0.map(new RealAudioBuffer(0, new float[]{2}, new AudioFormat(10, 16, 1, true, true)));
        assertArrayEquals(new float[]{2}, mapped0.getRealData(), 0.0001f);
        final AudioBuffer mapped1 = function0.map(new RealAudioBuffer(1, new float[]{2}, new AudioFormat(10, 16, 1, true, true)));
        assertArrayEquals(new float[]{2}, mapped1.getRealData(), 0.0001f);

        assertEquals(function0, function1);
        assertEquals(function0.hashCode(), function1.hashCode());
        assertTrue(function0.toString().matches("<AudioBuffer\\.Power>\\{.*\\}"));
    }

    @Test
    public void testMagnitudeMapFunction() {
        final FloatIdentityFunction identityFunction = new FloatIdentityFunction();
        final MapFunction<AudioBuffer> function0 = AudioBufferFunctions.createMagnitudeMapFunction(identityFunction);
        final MapFunction<AudioBuffer> function1 = AudioBufferFunctions.createMagnitudeMapFunction(identityFunction);
        final AudioBuffer mapped0 = function0.map(new RealAudioBuffer(0, new float[]{2}, new AudioFormat(10, 16, 1, true, true)));
        assertArrayEquals(new float[]{2}, mapped0.getRealData(), 0.0001f);
        final AudioBuffer mapped1 = function0.map(new RealAudioBuffer(0, new float[]{2}, new AudioFormat(10, 16, 1, true, true)));
        assertArrayEquals(new float[]{2}, mapped1.getRealData(), 0.0001f);

        assertEquals(function0, function1);
        assertEquals(function0.hashCode(), function1.hashCode());
        assertTrue(function0.toString().matches("<AudioBuffer\\.Magnitude>\\{.*\\}"));
    }

    @Test
    public void testPowerDistanceFunction() {
        final FirstValueDistanceFunction firstValueDistanceFunction = new FirstValueDistanceFunction();
        final DistanceFunction<AudioBuffer> function0 = AudioBufferFunctions.createPowerDistanceFunction(firstValueDistanceFunction);
        final DistanceFunction<AudioBuffer> function1 = AudioBufferFunctions.createPowerDistanceFunction(firstValueDistanceFunction);
        final float distance = function0.distance(
                new RealAudioBuffer(0, new float[]{4}, new AudioFormat(10, 16, 1, true, true)),
                new RealAudioBuffer(0, new float[]{2}, new AudioFormat(10, 16, 1, true, true))
        );
        assertEquals(4f*4f - 2f*2f, distance, 0.0001f);

        assertEquals(function0, function1);
        assertEquals(function0.hashCode(), function1.hashCode());
        assertTrue(function0.toString().matches("<AudioBuffer>\\{.*\\(powers\\)\\}"));
    }

    @Test
    public void testAggregateFunction() {
        final ValueSumFunction sumFunction = new ValueSumFunction();
        final AggregateFunction<AudioBuffer, Float> function0 = AudioBufferFunctions.createAggregateFunction(sumFunction);
        final AggregateFunction<AudioBuffer, Float> function1 = AudioBufferFunctions.createAggregateFunction(sumFunction);
        final float aggregate = function0.aggregate(
                new RealAudioBuffer(0, new float[]{4, 5, 6, 1}, new AudioFormat(10, 16, 1, true, true))
        );
        assertEquals(4f+5f+6f+1f, aggregate, 0.0001f);

        assertEquals(function0, function1);
        assertEquals(function0.hashCode(), function1.hashCode());
        assertTrue(function0.toString().matches("<AudioBuffer>\\{.*\\(data\\)\\}"));
    }

    private static class FloatIdentityFunction implements MapFunction<float[]> {
        @Override
        public float[] map(final float[] data) {
            return data;
        }
    }

    private static class FirstValueDistanceFunction implements DistanceFunction<float[]> {
        @Override
        public float distance(final float[] a, final float[] b) {
            return a[0] - b[0];
        }
    }

    private static class ValueSumFunction implements AggregateFunction<float[], Float> {
        @Override
        public Float aggregate(final float[] collection) {
            return Floats.sum(collection);
        }
    }
}
