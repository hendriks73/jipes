/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalProcessor;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * TestBandSplit.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestBandSplit {

    @Test
    public void testProcessAndFlush() throws IOException {
        final BandSplit<LinearFrequencySpectrum> processor = new BandSplit<LinearFrequencySpectrum>(3);
        final MockChildProcessor mock0 = new MockChildProcessor();
        final MockChildProcessor mock1 = new MockChildProcessor();
        processor.connectTo(0, mock0);
        processor.connectTo(1, mock1);

        assertEquals(2, processor.getChannelCount());
        assertEquals(mock0, processor.getConnectedProcessors(0)[0]);

        processor.process(new LinearFrequencySpectrum(0, new float[]{1, 2, 3, 4}, new float[4], new AudioFormat(20, 16, 1, true, true)));
        assertNull(mock0.getOutput());
        assertNull(mock1.getOutput());
        processor.process(new LinearFrequencySpectrum(1, new float[]{1, 2, 3, 4}, new float[4], new AudioFormat(20, 16, 1, true, true)));
        assertNull(mock0.getOutput());
        assertNull(mock1.getOutput());
        processor.process(new LinearFrequencySpectrum(2, new float[]{1, 2, 3, 4}, new float[4], new AudioFormat(20, 16, 1, true, true)));

        assertArrayEquals(new float[]{1,1,1}, ((AudioBuffer)mock0.getOutput()).getRealData(), 0.0001f);
        assertArrayEquals(new float[]{2,2,2}, ((AudioBuffer)mock1.getOutput()).getRealData(), 0.0001f);

        processor.process(new LinearFrequencySpectrum(3, new float[]{1, 2, 3, 4}, new float[4], new AudioFormat(20, 16, 1, true, true)));

        processor.flush();

        assertArrayEquals(new float[]{1,0,0}, ((AudioBuffer)mock0.getOutput()).getRealData(), 0.0001f);
        assertArrayEquals(new float[]{2,0,0}, ((AudioBuffer)mock1.getOutput()).getRealData(), 0.0001f);
    }

    @Test
    public void testToString() {
        assertEquals("BandSplit{windowLength=3}", new BandSplit<LinearFrequencySpectrum>(3).toString());
    }

    @Test
    public void testOutput() throws IOException {
        final BandSplit<LinearFrequencySpectrum> bandSplit = new BandSplit<LinearFrequencySpectrum>(3);
        assertNull(bandSplit.getOutput());
    }

    private static class MockChildProcessor implements SignalProcessor<AudioBuffer, Object> {

        private AudioBuffer output;

        public void process(final AudioBuffer audioBuffer) throws IOException {
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
