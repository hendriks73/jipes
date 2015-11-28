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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * TestInterleavedChannelJoin.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestInterleavedChannelJoin {

    @Test
    public void testSimpleJoin() throws IOException {
        final InterleavedChannelJoin processor = new InterleavedChannelJoin(2);

        final SignalProcessor<AudioBuffer, Object> mock = new MockChildProcessor();
        processor.connectTo(mock);
        processor.process(new RealAudioBuffer(0, new float[]{1, 1, 1, 1, 1, 1, 1, 1}, new AudioFormat(44100, 32, 1, true, true)));
        processor.process(new RealAudioBuffer(0, new float[]{2, 2, 2, 2, 2, 2, 2, 2}, new AudioFormat(44100, 32, 1, true, true)));

        final AudioBuffer output = (AudioBuffer)mock.getOutput();

        assertArrayEquals(new float[]{1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2}, output.getRealData(), 0.00001f);
        assertTrue(new AudioFormat(44100, 32, 2, true, true).matches(output.getAudioFormat()));
        assertEquals(2, output.getAudioFormat().getChannels());
    }

    @Test
    public void testFlush() throws IOException {
        final InterleavedChannelJoin processor = new InterleavedChannelJoin(2);
        final SignalProcessor<AudioBuffer, AudioBuffer> mock = mock(SignalProcessor.class);
        processor.connectTo(mock);

        processor.flush();

        verify(mock).flush();

        assertNull(processor.getOutput());
    }

    @Test
    public void testGetId() {
        final InterleavedChannelJoin processor = new InterleavedChannelJoin(2);
        assertEquals("InterleavedChannelJoin{parts=2}", processor.getId());
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
