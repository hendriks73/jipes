/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes;

import com.tagtraum.jipes.audio.AudioBuffer;
import com.tagtraum.jipes.audio.Mono;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * TestSignalPipeline.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSignalPipeline {

    @Test
    public void testFirstLastConstructor() {
        final Mono mono0 = new Mono();
        final Mono mono1 = new Mono();
        final Mono mono2 = new Mono();

        mono0.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono1);
        mono1.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono2);

        final SignalPipeline<AudioBuffer, AudioBuffer> pipeline = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0, mono2, true);
        assertEquals(mono0, pipeline.getFirstProcessor());
        assertArrayEquals(new SignalProcessor[0], pipeline.getConnectedProcessors());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFirstLastConstructorNotStraight() {
        final Mono mono0 = new Mono();
        final Mono mono1 = new Mono();
        final Mono mono2 = new Mono();
        final Mono mono3 = new Mono();

        mono0.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono1);
        mono0.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono3);
        mono1.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono2);

        new SignalPipeline<AudioBuffer, AudioBuffer>(mono0, mono2, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFirstLastConstructor2() {
        final Mono mono0 = new Mono();
        final Mono mono1 = new Mono();
        final Mono mono2 = new Mono();

        mono0.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono1);

        new SignalPipeline<AudioBuffer, AudioBuffer>(mono0, mono2, true);
    }

    @Test
    public void testArrayConstructor2() {
        final Mono mono0 = new Mono();
        final Mono mono1 = new Mono();
        final Mono mono2 = new Mono();

        mono0.connectTo((SignalProcessor<AudioBuffer, AudioBuffer>) mono1);

        final SignalPipeline<AudioBuffer, AudioBuffer> pipeline = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0, mono1, mono2);
        assertEquals(mono0, pipeline.getFirstProcessor());
        assertArrayEquals(new SignalProcessor[0], pipeline.getConnectedProcessors());
    }

    @Test
    public void testGetProcessorWithId() {
        final Mono mono0 = new Mono();
        mono0.setId("0");
        final Mono mono1 = new Mono();
        mono1.setId("1");
        final Mono mono2 = new Mono();
        mono2.setId("2");

        final SignalPipeline<AudioBuffer, AudioBuffer> pipeline = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0, mono1, mono2);
        assertEquals("2", pipeline.getId());

        assertEquals(mono0, pipeline.getProcessorWithId("0"));
        assertEquals(mono1, pipeline.getProcessorWithId("1"));
        assertEquals(mono2, pipeline.getProcessorWithId("2"));
        assertNull(pipeline.getProcessorWithId("3"));
    }

    @Test
    public void testProcessAndFlush() throws IOException {
        final SignalProcessor<AudioBuffer, AudioBuffer> processor = mock(SignalProcessor.class);
        final SignalPipeline<AudioBuffer, AudioBuffer> pipeline = new SignalPipeline<AudioBuffer, AudioBuffer>(processor);
        final AudioBuffer audioBuffer = mock(AudioBuffer.class);

        when(processor.getOutput()).thenReturn(audioBuffer);

        pipeline.process(audioBuffer);
        pipeline.flush();
        assertEquals(audioBuffer, pipeline.getOutput());

        verify(processor).process(audioBuffer);
        verify(processor).flush();
    }

    @Test
    public void testEquals() {
        final Mono mono0a = new Mono();
        final Mono mono1a = new Mono();
        final Mono mono2a = new Mono();

        final Mono mono0b = new Mono();
        final Mono mono1b = new Mono();
        final Mono mono2b = new Mono();

        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineA = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0a, mono1a, mono2a);
        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineB = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0b, mono1b, mono2b);

        assertEquals("SignalPipeline[Mono, Mono, Mono]", pipelineA.toString());
        assertEquals(pipelineA, pipelineB);
        assertEquals(pipelineA.hashCode(), pipelineB.hashCode());
    }

    @Test
    public void testJoinWithPipeline() {
        final Mono mono0a = new Mono();
        final Mono mono1a = new Mono();
        final Mono mono2a = new Mono();

        final Mono mono0b = new Mono();
        final Mono mono1b = new Mono();
        final Mono mono2b = new Mono();

        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineA = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0a, mono1a, mono2a);
        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineB = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0b, mono1b, mono2b);

        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineC = pipelineA.joinWith(pipelineB);

        assertEquals(pipelineA.getConnectedProcessors()[0], pipelineB.getFirstProcessor());
        assertEquals(pipelineC.getFirstProcessor(), pipelineA.getFirstProcessor());
    }

    @Test
    public void testJoinWithProcessor() {
        final Mono mono0a = new Mono();
        final Mono mono1a = new Mono();
        final Mono mono2a = new Mono();

        final Mono mono0b = new Mono();

        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineA = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0a, mono1a, mono2a);

        final SignalPipeline<AudioBuffer, AudioBuffer> pipelineC = pipelineA.joinWith(mono0b);

        assertEquals(pipelineA.getConnectedProcessors()[0], mono0b);
        assertEquals(pipelineC.getFirstProcessor(), pipelineA.getFirstProcessor());
    }

    @Test
    public void testConnectToDisconnect() {
        final Mono mono0a = new Mono();
        final Mono mono1a = new Mono();
        final Mono mono2a = new Mono();

        final Mono mono0b = new Mono();

        final SignalPipeline<AudioBuffer, AudioBuffer> pipeline = new SignalPipeline<AudioBuffer, AudioBuffer>(mono0a, mono1a, mono2a);

        pipeline.connectTo(mono0b);

        assertEquals(mono2a.getConnectedProcessors()[0], mono0b);

        pipeline.disconnectFrom(mono0b);

        assertEquals(0, mono2a.getConnectedProcessors().length);

    }
}
