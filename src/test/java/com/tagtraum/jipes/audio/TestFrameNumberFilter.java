/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalProcessor;
import com.tagtraum.jipes.SignalSource;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * TestFrameNumberFilter.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFrameNumberFilter {

    private SignalSource<AudioBuffer> source = new SignalSource<AudioBuffer>() {

        private int frameNumber;

        public void reset() {
            frameNumber = 0;
        }

        public AudioBuffer read() throws IOException {
            final RealAudioBuffer buffer = new RealAudioBuffer(
                    frameNumber, new float[]{1, 2, 1, 2, 1, 2, 1, 2}, new AudioFormat(10000, 32, 1, true, true)
            );
            frameNumber++;
            return buffer;
        }
    };

    @Test
    public void testProcess() throws IOException {
        final FrameNumberFilter processor = new FrameNumberFilter(0, 5);
        assertEquals(5, processor.getMaxFrameNumber());
        assertEquals(0, processor.getMinFrameNumber());
        final SignalProcessor<AudioBuffer, AudioBuffer> mock = mock(SignalProcessor.class);
        processor.connectTo(mock);
        final AudioFormat audioFormat = new AudioFormat(1000f, 8, 1, true, true);
        final RealAudioBuffer zeroBuffer = new RealAudioBuffer(0, new float[5], audioFormat);
        final RealAudioBuffer fiveBuffer = new RealAudioBuffer(5, new float[5], audioFormat);
        processor.process(zeroBuffer);
        verify(mock).process(zeroBuffer);
        reset(mock);
        processor.process(fiveBuffer);
        verify(mock, never()).process(zeroBuffer);
    }

    @Test
    public void testRead() throws IOException {
        final FrameNumberFilter processor = new FrameNumberFilter(0, 2);
        processor.connectTo(source);
        assertNotNull(processor.read());
        assertNotNull(processor.read());
        assertNull(processor.read());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFrameNumbers() throws IOException {
        new FrameNumberFilter(5, 4);
    }

    @Test
    public void testEqualsHashCode() {
        final FrameNumberFilter processor0 = new FrameNumberFilter(0, 2);
        final FrameNumberFilter processor1 = new FrameNumberFilter(0, 2);
        final FrameNumberFilter processor2 = new FrameNumberFilter(0, 3);

        assertEquals(processor0, processor1);
        assertNotEquals(processor0, processor2);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

    @Test
    public void testToString() {
        final FrameNumberFilter processor = new FrameNumberFilter(23, 41);
        assertEquals("FrameNumberFilter{minFrameNumber=23, maxFrameNumber=41}", processor.toString());
    }
}
