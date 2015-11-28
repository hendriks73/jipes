/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalProcessor;
import com.tagtraum.jipes.math.DCTFactory;
import com.tagtraum.jipes.math.Transform;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestDCT.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestDCT {

    @Test
    public void testBasics() throws IOException {
        final DCT dct = new DCT(1024);
        final MockChildProcessor mock = new MockChildProcessor();
        dct.connectTo(mock);
        dct.process(new RealAudioBuffer(0, new float[512], new AudioFormat(44100f, 16, 1, true, true)));
        dct.process(new RealAudioBuffer(1, new float[]{1, 2, 4}, new AudioFormat(44100f, 16, 1, true, true)));
        final LinearFrequencySpectrum spectrum = (LinearFrequencySpectrum)mock.getOutput();

        final Transform transform = DCTFactory.getInstance().create(1024);
        final float[] r = new float[1024];
        r[0] = 1;
        r[1] = 2;
        r[2] = 4;
        final float[] data = transform.transform(r)[0];
        final float[] realData = spectrum.getRealData();

        assertArrayEquals(data, realData, 0.0001f);
    }

    @Test(expected = IOException.class)
    public void testMono() throws IOException {
        final DCT dct = new DCT(1024);
        dct.process(new RealAudioBuffer(0, new float[512], new AudioFormat(44100f, 16, 2, true, true)));
    }

    @Test
    public void testToString() throws IOException {
        final DCT dct0 = new DCT(1024);
        assertEquals("DCT{length=1024}", dct0.toString());

        final DCT dct1 = new DCT();
        assertEquals("DCT{length=equal to first input}", dct1.toString());
    }

    @Test
    public void testEquals() {
        final DCT dct0 = new DCT(1024);
        final DCT dct1 = new DCT(1024);
        final DCT dct2 = new DCT();
        final DCT dct3 = new DCT();

        assertEquals(dct0, dct1);
        assertEquals(dct2, dct3);
        assertNotEquals(dct0, dct2);

        assertEquals(0, dct0.hashCode());
    }

    private static class MockChildProcessor implements SignalProcessor<LinearFrequencySpectrum, Object> {

        private AudioBuffer output;

        public void process(final LinearFrequencySpectrum audioBuffer) throws IOException {
            output = audioBuffer;
        }

        public void flush() throws IOException {
        }

        public Object getOutput() throws IOException {
            return output;
        }

        public Object getId() {
            return null;
        }

        public <O2> SignalProcessor<Object, O2> connectTo(final SignalProcessor<Object, O2> objectO2SignalProcessor) {
            return null;
        }

        public <O2> SignalProcessor<Object, O2> disconnectFrom(final SignalProcessor<Object, O2> objectO2SignalProcessor) {
            return null;
        }

        public SignalProcessor<Object, ?>[] getConnectedProcessors() {
            return new SignalProcessor[0];
        }
    }
}
