/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * TestAbstractSignalProcessor.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAbstractSignalProcessor {

    @Test
    public void testConnection() {
        final ConcreteSignalProcessor<Integer, Integer> processor = new ConcreteSignalProcessor<Integer, Integer>("id");
        final SignalProcessor<Integer, Integer> noopProcessor = new NoopSignalProcessor<Integer>();

        processor.connectTo(noopProcessor);
        assertArrayEquals(new SignalProcessor[]{noopProcessor}, processor.getConnectedProcessors());

        processor.disconnectFrom(noopProcessor);
        assertEquals(0, processor.getConnectedProcessors().length);
    }

    @Test
    public void testId() {
        final ConcreteSignalProcessor<Integer, Integer> processor = new ConcreteSignalProcessor<Integer, Integer>("id");
        assertEquals("id", processor.getId());
    }

    @Test
    public void testPullConnection() {
        final ConcreteSignalProcessor<Integer, Integer> processor = new ConcreteSignalProcessor<Integer, Integer>("id");
        final SignalSource<Integer> signalSource = mock(SignalSource.class);
        processor.connectTo(signalSource);
        assertEquals(signalSource, processor.getConnectedSource());
    }

    @Test
    public void testRead() throws IOException {
        final AbstractSignalProcessor<Integer, Integer> processor = mock(AbstractSignalProcessor.class, CALLS_REAL_METHODS);
        final SignalSource<Integer> signalSource = mock(SignalSource.class);
        when(signalSource.read()).thenReturn(1).thenReturn(2).thenReturn(null);
        processor.connectTo(signalSource);
        processor.read();
        verify(processor).processNext(1);
        reset(processor);
        processor.read();
        verify(processor).processNext(2);
        reset(processor);
        processor.read();
        reset(processor);
        verify(processor, never()).processNext(2);
        verify(signalSource, times(3)).read();
    }

    @Test
    public void testProcess() throws IOException {
        final AbstractSignalProcessor<Integer, Integer> processor = new ConcreteSignalProcessor<Integer, Integer>("id");
        final AbstractSignalProcessor<Integer, Integer> next = spy(AbstractSignalProcessor.class);
        processor.connectTo((SignalProcessor<Integer, Integer>) next);
        processor.process(1);
        verify(next).process(1);
        reset(next);
        processor.process(2);
        verify(next).processNext(2);
        reset(next);
        processor.flush();
        verify(next).flush();
    }

    /**
     * Mock processor, very much like {@link NoopSignalProcessor}.
     *
     * @param <I> input type
     * @param <O> output type
     */
    private static class ConcreteSignalProcessor<I,O> extends AbstractSignalProcessor<I,O> {

        public ConcreteSignalProcessor(final Object id) {
            super(id);
        }

        @Override
        protected O processNext(final I input) throws IOException {
            return (O)input;
        }
    }
}
