/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * TestNoopSignalProcessor.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestNoopSignalProcessor {

    @Test
    public void testHashCode() {
        final NoopSignalProcessor<Integer> processor = new NoopSignalProcessor<Integer>();
        assertEquals(0, processor.hashCode());
    }

    @Test
    public void testEquals() {
        final NoopSignalProcessor<Integer> processor0 = new NoopSignalProcessor<Integer>();
        final NoopSignalProcessor<Integer> processor1 = new NoopSignalProcessor<Integer>();
        assertEquals(processor0, processor1);
    }

    @Test
    public void testToString() {
        final NoopSignalProcessor<Integer> processor = new NoopSignalProcessor<Integer>();
        assertEquals("Noop", processor.toString());
    }

    @Test
    public void testProcess() throws IOException {
        final NoopSignalProcessor<Integer> processor = new NoopSignalProcessor<Integer>();
        final SignalProcessor<Integer, Integer> next = mock(SignalProcessor.class);
        processor.connectTo(next);
        processor.process(1);
        verify(next).process(1);
    }

    @Test
    public void testFlush() throws IOException {
        final NoopSignalProcessor<Integer> processor = new NoopSignalProcessor<Integer>();
        final SignalProcessor<Integer, Integer> next = mock(SignalProcessor.class);
        processor.connectTo(next);
        processor.flush();
        verify(next).flush();
    }
}
