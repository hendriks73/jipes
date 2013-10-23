/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes;

import com.tagtraum.jipes.audio.AudioBuffer;
import com.tagtraum.jipes.audio.Mono;
import com.tagtraum.jipes.audio.SlidingWindow;
import com.tagtraum.jipes.math.WindowFunctions;
import com.tagtraum.jipes.universal.Mapping;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * TestSignalPump.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSignalPump {

    /**
     * Sets up two tree pipelines and prints their description.
     */
    @Test
    public void testDescription() {
        final SignalPump<AudioBuffer> signalPump = new SignalPump<AudioBuffer>();

        // first

        final SignalProcessor<AudioBuffer, AudioBuffer> monoProcessor1 = new Mono();

        final Mapping<AudioBuffer> hammingProcessor = new Mapping<AudioBuffer>(com.tagtraum.jipes.audio.AudioBufferFunctions.createMapFunction(WindowFunctions.HAMMING));
        monoProcessor1.connectTo(hammingProcessor);

        final SlidingWindow slidingWindowProcessor1 = new SlidingWindow(128);

        hammingProcessor.connectTo((SignalProcessor<AudioBuffer,AudioBuffer>) slidingWindowProcessor1);

        final SlidingWindow slidingWindowProcessor2 = new SlidingWindow(256, 64);

        hammingProcessor.connectTo((SignalProcessor<AudioBuffer,AudioBuffer>) slidingWindowProcessor2);

        // second

        final SignalProcessor<AudioBuffer, AudioBuffer> monoProcessor2 = new Mono();

        final Mapping<AudioBuffer> hannProcessor = new Mapping<AudioBuffer>(com.tagtraum.jipes.audio.AudioBufferFunctions.createMapFunction(WindowFunctions.HANN));
        monoProcessor2.connectTo(hannProcessor);

        final SlidingWindow slidingWindowProcessor3 = new SlidingWindow(128);

        hannProcessor.connectTo((SignalProcessor<AudioBuffer,AudioBuffer>) slidingWindowProcessor3);

        final SlidingWindow slidingWindowProcessor4 = new SlidingWindow(256, 64);

        hannProcessor.connectTo((SignalProcessor<AudioBuffer,AudioBuffer>) slidingWindowProcessor4);

        // do it

        signalPump.add(monoProcessor1);
        signalPump.add(monoProcessor2);

        System.out.println(signalPump.getDescription());

        // now check optimizations
        final Set<SignalProcessor<AudioBuffer, ?>> graphs = signalPump.getEffectiveProcessorGraphs();
        assertEquals(1, graphs.size());
        final SignalProcessor<AudioBuffer, ?> firstProcessor = graphs.iterator().next();
        assertEquals(monoProcessor1, firstProcessor);
        final SignalProcessor<?, ?>[] secondProcessors = firstProcessor.getConnectedProcessors();
        assertEquals(2, secondProcessors.length);

        assertEquals(2, secondProcessors[0].getConnectedProcessors().length);
        assertEquals(2, secondProcessors[1].getConnectedProcessors().length);
    }

    /**
     * Test exception that is supposed to
     *
     * @throws IOException IOException
     */
    @Test
    public void testNoSourceSet() throws IOException {
        final SignalPump<AudioBuffer> signalPump = new SignalPump<AudioBuffer>();
        try {
            signalPump.pump();
            fail("Expected IllegalStateException, because no SignalSource was set.");
        } catch (IllegalStateException e) {
            // expected this
        }
    }
}
