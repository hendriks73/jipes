/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes;

import com.tagtraum.jipes.audio.AudioBuffer;
import com.tagtraum.jipes.audio.RealAudioBuffer;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * TestSignalProcessorSupport.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSignalProcessorSupport {

    @Test
    public void testConnectTo() {
        final SignalProcessorSupport<AudioBuffer> support = new SignalProcessorSupport<AudioBuffer>();
        final SignalProcessor<AudioBuffer, AudioBuffer> mockedProcessor = mock(SignalProcessor.class);
        support.connectTo(mockedProcessor);

        assertEquals(mockedProcessor, support.getConnectedProcessors()[0]);
        assertEquals(1, support.getConnectedProcessors().length);
    }

    @Test
    public void testDisconnectFrom() {
        final SignalProcessorSupport<AudioBuffer> support = new SignalProcessorSupport<AudioBuffer>();
        final SignalProcessor<AudioBuffer, AudioBuffer> mockedProcessor = mock(SignalProcessor.class);
        support.connectTo(mockedProcessor);
        support.disconnectFrom(mockedProcessor);

        assertEquals(0, support.getConnectedProcessors().length);
    }

    @Test
    public void testProcess() throws IOException {
        final SignalProcessorSupport<AudioBuffer> support = new SignalProcessorSupport<AudioBuffer>();
        final SignalProcessor<AudioBuffer, AudioBuffer> mockedProcessor = mock(SignalProcessor.class);
        support.connectTo(mockedProcessor);

        final RealAudioBuffer buffer = new RealAudioBuffer(0, new float[1024], new AudioFormat(10, 16, 2, true, true));
        support.process(buffer);
        verify(mockedProcessor).process(buffer);
    }

    @Test
    public void testProcessMultichannel() throws IOException {
        final SignalProcessorSupport<AudioBuffer> support = new SignalProcessorSupport<AudioBuffer>();
        final SignalProcessor<AudioBuffer, AudioBuffer> mockedProcessor = mock(SignalProcessor.class);
        support.connectTo(mockedProcessor);

        final RealAudioBuffer buffer = new RealAudioBuffer(0, new float[1024], new AudioFormat(10, 16, 2, true, true));

        support.process(0, buffer);
        verify(mockedProcessor).process(buffer);
        reset(mockedProcessor);

        support.process(1, buffer);
        verifyZeroInteractions(mockedProcessor);
    }

    @Test
    public void testFlush() throws IOException {
        final SignalProcessorSupport<AudioBuffer> support = new SignalProcessorSupport<AudioBuffer>();
        final SignalProcessor<AudioBuffer, AudioBuffer> mockedProcessor = mock(SignalProcessor.class);
        support.connectTo(mockedProcessor);
        support.flush();
        verify(mockedProcessor).flush();
    }

}
