/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.universal;

import com.tagtraum.jipes.SignalSource;
import com.tagtraum.jipes.audio.AudioBuffer;
import com.tagtraum.jipes.audio.RealAudioBuffer;
import com.tagtraum.jipes.math.*;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * TestMapping.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMapping {

    private SignalSource<AudioBuffer> constSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(
                    0, new float[] {
                    10, 10, 10, 10, 10,
                    10, 10, 10, 10, 10,
                    10
            }, new AudioFormat(10000, 32, 1, true, true)
            );
        }
    };

    private SignalSource<AudioBuffer> halfNyquistSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            final float[] buf = new float[1024];
            for (int i=0; i<buf.length; i++) {
                buf[i] = i % 2 == 0 ? 100 : -100;
            }
            return new RealAudioBuffer(
                    0, buf, new AudioFormat(20000, 32, 1, true, true)
            );
        }
    };

    @Test
    public void testConstructor() {
        final MapFunction<Integer> function = new MapFunction<Integer>() {
            @Override
            public Integer map(final Integer data) {
                return data;
            }
        };
        final Mapping<Integer> mapping = new Mapping<Integer>(function, "id");
        assertEquals("id", mapping.getId());
        assertEquals(function, mapping.getMapFunction());
    }

    @Test
    public void testReset() {
        final StatefulMapFunction<Integer> function = mock(StatefulMapFunction.class);
        final Mapping<Integer> mapping = new Mapping<Integer>(function, "id");

        mapping.reset();
        verify(function).reset();
    }

    @Test
    public void testHashCode() {
        final MapFunction<Integer> function = new MapFunction<Integer>() {
            @Override
            public Integer map(final Integer data) {
                return data;
            }
        };

        final Mapping<Integer> mapping0 = new Mapping<Integer>(function, "id");
        assertEquals(function.hashCode(), mapping0.hashCode());

        final Mapping<Integer> mapping1 = new Mapping<Integer>(null, "id");
        assertEquals(0, mapping1.hashCode());
    }

    @Test
    public void testEquals0() {
        final MapFunction<Integer> function = new MapFunction<Integer>() {
            @Override
            public Integer map(final Integer data) {
                return data;
            }
        };
        final Mapping<Integer> mapping0 = new Mapping<Integer>(function, "id1");
        final Mapping<Integer> mapping1 = new Mapping<Integer>(function, "id2");
        // even though ids are different!!
        assertEquals(mapping0, mapping1);
    }

    @Test
    public void testEquals1() {
        final MapFunction<Integer> function = new MapFunction<Integer>() {
            @Override
            public Integer map(final Integer data) {
                return data;
            }
        };
        final Mapping<Integer> mapping0 = new Mapping<Integer>(function, "id1");
        final Mapping<Integer> mapping1 = new Mapping<Integer>(null, "id2");
        // even though ids are different!!
        assertNotEquals(mapping0, mapping1);
    }

    @Test
    public void testIIRLowPass() throws IOException {
        final Mapping<AudioBuffer> processor = new Mapping<AudioBuffer>(com.tagtraum.jipes.audio.AudioBufferFunctions.createStatefulMapFunction(Filters.createButterworth4thOrderLowpassCutoffHalf()));
        processor.connectTo(halfNyquistSource);
        final float[] filtered = processor.read().getData();
        final float[] unfiltered = halfNyquistSource.read().getData();
        /*
        for (int i=0; i<filtered.length; i++) {
            System.out.println(filtered[i] + "\t\t" + unfiltered[i]);
        }
        */
        final float filteredStandardDeviation = Floats.standardDeviation(filtered);
        final float unfilteredStandardDeviation = Floats.standardDeviation(unfiltered);
        assertTrue(filteredStandardDeviation * 20 < unfilteredStandardDeviation);
    }

    @Test
    public void testFIRLowPass() throws IOException {
        final Mapping<AudioBuffer> processor = new Mapping<AudioBuffer>(com.tagtraum.jipes.audio.AudioBufferFunctions.createStatefulMapFunction(Filters.createFir1_16thOrderLowpassCutoffHalf()));
        processor.connectTo(halfNyquistSource);
        final float[] filtered = processor.read().getData();
        final float[] unfiltered = halfNyquistSource.read().getData();
        /*
        for (int i=0; i<filtered.length; i++) {
            System.out.println(filtered[i] + "\t\t" + unfiltered[i]);
        }
        */
        final float filteredStandardDeviation = Floats.standardDeviation(filtered);
        final float unfilteredStandardDeviation = Floats.standardDeviation(unfiltered);
        assertTrue(filteredStandardDeviation * 20 < unfilteredStandardDeviation);
    }


    @Test
    public void testBasicHamming() throws IOException {
        final Mapping<AudioBuffer> processor = new Mapping<AudioBuffer>(com.tagtraum.jipes.audio.AudioBufferFunctions.createMapFunction(WindowFunction.HAMMING));
        processor.connectTo(constSource);
        final float[] constFloats = constSource.read().getData();
        final float[] floats = processor.read().getData();
        for (int i=0; i<floats.length; i++) {
            if (i == (floats.length-1)/2) {
                // peak
                assertEquals(constFloats[i], floats[i], 0.00001);
            } else {
                // less than source data
                assertTrue(floats[i] < constFloats[i]);
                // greater than 0
                assertTrue(floats[i] > 0);
                // symmetry
                assertEquals(floats[floats.length-i-1], floats[i], 0.00001);
            }
        }
    }

    @Test
    public void testBasicHann() throws IOException {
        final Mapping<AudioBuffer> processor = new Mapping<AudioBuffer>(com.tagtraum.jipes.audio.AudioBufferFunctions.createMapFunction(WindowFunction.HANN));
        processor.connectTo(constSource);
        final float[] constFloats = constSource.read().getData();
        final float[] floats = processor.read().getData();
        for (int i=0; i<floats.length; i++) {
            if (i == (floats.length-1)/2) {
                // peak
                assertEquals(constFloats[i], floats[i], 0.00001);
            } else {
                // less than source data
                assertTrue(floats[i] < constFloats[i]);
                // greater than 0, if not first or last data point
                if (i > 0 && i < floats.length-1) assertTrue(floats[i] > 0);
                else assertEquals(0, floats[i], 0.0001);
                // symmetry
                assertEquals(floats[floats.length-i-1], floats[i], 0.00001);
            }
        }
    }

    @Test
    public void testNullGenerator() throws IOException {
        final Mapping<float[]> processor = new Mapping<float[]>();
        processor.connectTo(new NullFloatArraySource());
        assertNull(processor.read());
    }

}
