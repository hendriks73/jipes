/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalProcessor;
import junit.framework.TestCase;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

/**
 * TestInterleavedChannelSplit.
 * <p/>
 * Date: 3/24/11
 * Time: 4:22 PM
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestInterleavedChannelSplit extends TestCase {

    public void testSimpleSplitting() throws IOException {
        final InterleavedChannelSplit split = new InterleavedChannelSplit();
        final MockChildProcessor channel0 = new MockChildProcessor();
        final MockChildProcessor channel1 = new MockChildProcessor();
        split.connectTo(0, channel0);
        split.connectTo(1, channel1);
        split.process(new RealAudioBuffer(0, new float[]{1, 2, 3, 4, 5, 6, 7, 8}, new AudioFormat(44100, 32, 2, true, true)));

        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[0], 1f);
        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[1], 3f);
        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[2], 5f);

        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[0], 2f);
        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[1], 4f);
        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[2], 6f);
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
