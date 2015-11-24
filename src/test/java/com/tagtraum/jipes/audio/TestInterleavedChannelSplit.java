/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * TestInterleavedChannelSplit.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestInterleavedChannelSplit {

    @Test
    public void testSimpleSplitting() throws IOException {
        final InterleavedChannelSplit processor = new InterleavedChannelSplit();
        final MockChildProcessor channel0 = new MockChildProcessor();
        final MockChildProcessor channel1 = new MockChildProcessor();
        processor.connectTo(0, channel0);
        processor.connectTo(1, channel1);
        processor.process(new RealAudioBuffer(0, new float[]{1, 2, 3, 4, 5, 6, 7, 8}, new AudioFormat(44100, 32, 2, true, true)));

        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[0], 1f, 0.0001f);
        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[1], 3f, 0.0001f);
        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[2], 5f, 0.0001f);

        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[0], 2f, 0.0001f);
        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[1], 4f, 0.0001f);
        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[2], 6f, 0.0001f);

        processor.process(new RealAudioBuffer(1, new float[]{1, 2, 3, 4, 5, 6, 7, 8}, new AudioFormat(44100, 32, 2, true, true)));

        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[0], 1f, 0.0001f);
        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[1], 3f, 0.0001f);
        assertEquals(((AudioBuffer)channel0.getOutput()).getData()[2], 5f, 0.0001f);

        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[0], 2f, 0.0001f);
        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[1], 4f, 0.0001f);
        assertEquals(((AudioBuffer)channel1.getOutput()).getData()[2], 6f, 0.0001f);
    }

    @Test
    public void testFlush() throws IOException {
        final InterleavedChannelSplit processor = new InterleavedChannelSplit();
        final SignalProcessor<AudioBuffer, AudioBuffer> mock0 = mock(SignalProcessor.class);
        final SignalProcessor<AudioBuffer, AudioBuffer> mock1 = mock(SignalProcessor.class);
        processor.connectTo(0, mock0);
        processor.connectTo(1, mock1);

        processor.flush();

        verify(mock0).flush();
        verify(mock1).flush();

        assertNull(processor.getOutput());
    }

    @Test
    public void testConnection1() throws IOException {
        final InterleavedChannelSplit processor = new InterleavedChannelSplit();
        final SignalProcessor<AudioBuffer, AudioBuffer> mock0 = mock(SignalProcessor.class);
        final SignalProcessor<AudioBuffer, AudioBuffer> mock1 = mock(SignalProcessor.class);
        processor.connectTo(0, mock0);
        processor.connectTo(1, mock1);

        assertEquals(2, processor.getChannelCount());

        assertArrayEquals(new SignalProcessor[]{mock0}, processor.getConnectedProcessors(0));
        assertArrayEquals(new SignalProcessor[]{mock1}, processor.getConnectedProcessors(1));

        processor.disconnectFrom(0, mock0);
        processor.disconnectFrom(1, mock1);

        assertArrayEquals(new SignalProcessor[0], processor.getConnectedProcessors(0));
        assertArrayEquals(new SignalProcessor[0], processor.getConnectedProcessors(1));
    }

    @Test
    public void testConnection2() throws IOException {
        final InterleavedChannelSplit processor = new InterleavedChannelSplit();
        final SignalProcessor<AudioBuffer, AudioBuffer> mock0 = mock(SignalProcessor.class);
        final SignalProcessor<AudioBuffer, AudioBuffer> mock1 = mock(SignalProcessor.class);
        processor.connectTo(mock0);
        processor.connectTo(mock1);

        assertEquals(1, processor.getChannelCount());

        assertArrayEquals(new SignalProcessor[]{mock0, mock1}, processor.getConnectedProcessors(0));

        processor.disconnectFrom(mock0);
        processor.disconnectFrom(mock1);

        assertArrayEquals(new SignalProcessor[0], processor.getConnectedProcessors());
    }

    @Test
    public void testGetId() {
        final InterleavedChannelSplit processor = new InterleavedChannelSplit();
        assertEquals("InterleavedChannelSplit", processor.getId());
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
