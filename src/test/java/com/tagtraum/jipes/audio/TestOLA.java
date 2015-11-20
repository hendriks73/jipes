/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.AbstractSignalProcessor;
import com.tagtraum.jipes.SignalSource;
import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TestOLA.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestOLA {

    private SignalSource<AudioBuffer> smallBufferSource4;
    private SignalSource<AudioBuffer> smallBufferSource8;

    @Before
    public void setUp() throws Exception {
        smallBufferSource4 = new SignalSource<AudioBuffer>() {

            private int count;

            public void reset() {
            }

            public AudioBuffer read() throws IOException {
                if (count < 4*4) {
                    final AudioBuffer buffer = new RealAudioBuffer(0, new float[]{1, 1, 1, 1}, new AudioFormat(10000, 32, 1, true, true));
                    count += 4;
                    return buffer;
                }
                return null;
            }
        };
        smallBufferSource8 = new SignalSource<AudioBuffer>() {

            private int count;

            public void reset() {
            }

            public AudioBuffer read() throws IOException {
                if (count < 4*16) {
                    final AudioBuffer buffer = new RealAudioBuffer(0, new float[]{1, 1, 1, 1, 1, 1, 1, 1}, new AudioFormat(10000, 32, 1, true, true));
                    count += 8;
                    return buffer;
                }
                return null;
            }
        };
    }

    @Test
    public void testSmallWindowPull() throws IOException {
        final OLA processor = new OLA(6, 2);
        processor.connectTo(smallBufferSource4);
        AudioBuffer buffer;

        buffer = processor.read();
        System.out.println(Arrays.toString(buffer.getData()));
        assertEquals(processor.getSliceLengthInFrames(), buffer.getData().length);
        assertEquals(processor.getHopSizeInFrames(), 2);
        assertArrayEquals(new float[]{1, 1, 2, 2, 2, 2}, buffer.getData(), 0.00001f);

        buffer = processor.read();
        System.out.println(Arrays.toString(buffer.getData()));
        assertEquals(processor.getSliceLengthInFrames(), buffer.getData().length);
        assertArrayEquals(new float[]{2, 2, 1, 1, 0, 0}, buffer.getData(), 0.00001f);

    }

    @Test
    public void testSmallWindowPush1() throws IOException {
        final OLA processor = new OLA(6, 2);
        final DataCollector dataCollector = new DataCollector();
        processor.connectTo(dataCollector);
        AudioBuffer buffer;
        float sum = 0;
        while ((buffer = smallBufferSource4.read()) != null) {
            final float[] realData = buffer.getRealData();
            for (final float f : realData) {
                sum+=f;
            }
            processor.process(buffer);
        }
        processor.flush();
        final List<float[]> results = dataCollector.getOutput();
        System.out.println("results: " + results.size());
        float measuredSum = 0;
        for (final float[] floats : results) {
            System.out.println(Arrays.toString(floats));
            for (final float f : floats) {
                measuredSum+=f;
            }
        }
        assertEquals(sum, measuredSum, 0.0001f);
        assertFalse(results.isEmpty());
        assertArrayEquals(new float[]{1, 1, 2, 2, 2, 2}, results.get(0), 0.00001f);
        assertArrayEquals(new float[]{2, 2, 1, 1, 0, 0}, results.get(1), 0.00001f);
    }

    @Test
    public void testSmallWindowPush2() throws IOException {
        final OLA processor = new OLA(8, 2);
        final DataCollector dataCollector = new DataCollector();
        processor.connectTo(dataCollector);
        AudioBuffer buffer;
        float sum = 0;
        while ((buffer = smallBufferSource8.read()) != null) {
            final float[] realData = buffer.getRealData();
            for (final float f : realData) {
                sum+=f;
            }
            processor.process(buffer);
        }
        processor.flush();
        final List<float[]> results = dataCollector.getOutput();
        System.out.println("results: " + results.size());
        float measuredSum = 0;
        for (final float[] floats : results) {
            System.out.println(Arrays.toString(floats));
            for (final float f : floats) {
                measuredSum+=f;
            }
        }
        assertEquals(sum, measuredSum, 0.0001f);
        assertFalse(results.isEmpty());
        assertArrayEquals(new float[]{1, 1, 2, 2, 3, 3, 4, 4}, results.get(0), 0.00001f);
        assertArrayEquals(new float[]{4, 4, 4, 4, 4, 4, 4, 4}, results.get(1), 0.00001f);
        assertArrayEquals(new float[]{3, 3, 2, 2, 1, 1, 0, 0}, results.get(2), 0.00001f);
    }

    @Test
    public void testNullGenerator() throws IOException {
        final OLA processor = new OLA();
        processor.connectTo(new NullAudioBufferSource());
        assertNull(processor.read());
    }

    @Test
    public void testToString() {
        final OLA processor = new OLA(100, 50);
        assertEquals("OLA{window=100, hop=50}", processor.toString());
    }

    @Test
    public void testEqualsHashCode() {
        final OLA processor0 = new OLA(2);
        final OLA processor1 = new OLA(2);
        final OLA processor2 = new OLA(3);

        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

    private static class DataCollector extends AbstractSignalProcessor<AudioBuffer, List<float[]>> {
        private final List<float[]> results = new ArrayList<float[]>();

        @Override
        protected List<float[]> processNext(final AudioBuffer buffer) throws IOException {
            return null;
        }

        @Override
        public void process(final AudioBuffer in) throws IOException {
            results.add(in.getData().clone());
        }

        @Override
        public List<float[]> getOutput() throws IOException {
            return results;
        }
    }
}
