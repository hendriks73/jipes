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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TestConstantQTransform.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestConstantQTransform {

    @Test
    public void testBasics() throws IOException {
        final ConstantQTransform transform = new ConstantQTransform(300, 5000, 12);
        final MockChildProcessor mock = new MockChildProcessor();
        transform.connectTo(mock);
        transform.process(new RealAudioBuffer(0, new float[] {1,2,3,4,5,6,7,8}, new AudioFormat(44100, 16, 1, true, true)));
        final LogFrequencySpectrum spectrum = mock.getOutput();
        assertEquals(16.817154f, spectrum.getQ(), 0.0001f);
        assertEquals(1f, spectrum.getBinsPerSemitone(), 0.0001f);
        transform.reset();
    }

    @Test(expected = IOException.class)
    public void testMono() throws IOException {
        final ConstantQTransform transform = new ConstantQTransform(300, 5000, 12, 0.0001f);
        transform.process(new RealAudioBuffer(0, new float[] {1,2,3,4,5,6,7,8}, new AudioFormat(44100, 16, 2, true, true)));
    }

    @Test
    public void testToString() throws IOException {
        final ConstantQTransform transform = new ConstantQTransform(300, 5000, 12);
        assertEquals("ConstantQTransform{minFrequency=300.0Hz, maxFrequency=5000.0Hz, binsPerOctave=12, threshold=0.0054}", transform.toString());
    }

    @Test
    public void testEqualsHashCode() throws IOException {
        final ConstantQTransform transform0 = new ConstantQTransform(300, 5000, 12);
        final ConstantQTransform transform1 = new ConstantQTransform(300, 5000, 12);
        final ConstantQTransform transform2 = new ConstantQTransform(300, 5001, 12);
        final ConstantQTransform transform3 = new ConstantQTransform(300, 5001, 13);

        assertEquals(transform0, transform1);
        assertNotEquals(transform0, transform2);
        assertNotEquals(transform0, transform3);

        assertEquals(transform0.hashCode(), transform1.hashCode());
        assertNotEquals(transform0.hashCode(), transform2.hashCode());
        assertNotEquals(transform0.hashCode(), transform3.hashCode());
    }

    private static class MockChildProcessor implements SignalProcessor<LogFrequencySpectrum, LogFrequencySpectrum> {

        private LogFrequencySpectrum output;

        public void process(final LogFrequencySpectrum audioBuffer) throws IOException {
            output = audioBuffer;
        }

        public void flush() throws IOException {
        }

        public LogFrequencySpectrum getOutput() throws IOException {
            return output;
        }

        public Object getId() {
            return null;
        }

        public <O2> SignalProcessor<LogFrequencySpectrum, O2> connectTo(final SignalProcessor<LogFrequencySpectrum, O2> objectO2SignalProcessor) {
            return null;
        }

        public <O2> SignalProcessor<LogFrequencySpectrum, O2> disconnectFrom(final SignalProcessor<LogFrequencySpectrum, O2> objectO2SignalProcessor) {
            return null;
        }

        public SignalProcessor<LogFrequencySpectrum, ?>[] getConnectedProcessors() {
            return new SignalProcessor[0];
        }
    }
}
