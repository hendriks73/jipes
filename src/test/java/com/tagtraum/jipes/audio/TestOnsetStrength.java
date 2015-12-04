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
import java.util.Random;

import static org.junit.Assert.*;

/**
 * TestOnsetStrength.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestOnsetStrength {

    @Test
    public void testBasics() throws IOException {
        final OnsetStrength processor = new OnsetStrength("id", 300, 4000, 512, 1f);
        assertEquals("id", processor.getId());
        assertNull(processor.getOutput());
        final MockChildProcessor mock = new MockChildProcessor();
        processor.connectTo(mock);
        assertEquals(mock, processor.getConnectedProcessors()[0]);
        final float[] real = new float[512];
        for (int i=0; i<512; i++) {
            real[i] = i;
        }
        final LinearFrequencySpectrum spectrum = new LinearFrequencySpectrum(0, real, new float[512], new AudioFormat(10000, 16, 1, true, true));
        processor.process(spectrum);
        assertNull(mock.getOutput());
        processor.process(spectrum);
        assertNull(mock.getOutput());
        processor.process(spectrum);
        assertNull(mock.getOutput());
        processor.flush();
        final AudioBuffer output = mock.getOutput();
        assertNotNull(output);
        assertEquals(10000f/512, output.getAudioFormat().getSampleRate(), 0.0001f);
        assertArrayEquals(new float[]{0,0}, output.getData(), 0.0001f);
    }

    @Test
    public void testRandom() throws IOException {
        final OnsetStrength processor = new OnsetStrength();
        final MockChildProcessor mock = new MockChildProcessor();
        processor.connectTo(mock);
        final float[] real = new float[512];
        final Random random = new Random();
        for (int i=0; i<512; i++) {
            real[i] = random.nextInt(512);
        }
        processor.process(new LinearFrequencySpectrum(0, real, new float[512], new AudioFormat(10000, 16, 1, true, true)));
        assertNull(mock.getOutput());
        for (int i=0; i<512; i++) {
            real[i] = random.nextInt(512);
        }
        processor.process(new LinearFrequencySpectrum(1, real, new float[512], new AudioFormat(10000, 16, 1, true, true)));
        assertNull(mock.getOutput());
        for (int i=0; i<512; i++) {
            real[i] = random.nextInt(512);
        }
        processor.process(new LinearFrequencySpectrum(2, real, new float[512], new AudioFormat(10000, 16, 1, true, true)));
        assertNull(mock.getOutput());
        processor.flush();
        final AudioBuffer output = mock.getOutput();
        assertNotNull(output);
        assertEquals(10000f/512, output.getAudioFormat().getSampleRate(), 0.0001f);
    }

    @Test
    public void testEqualsHashCode() {
        final OnsetStrength processor0 = new OnsetStrength("id", 300, 4000, 512, 1f);
        final OnsetStrength processor1 = new OnsetStrength("id", 300, 4000, 512, 1f);
        final OnsetStrength processor2 = new OnsetStrength("id", 300, 4001, 512, 1f);

        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

    @Test
    public void testToString() {
        final OnsetStrength processor = new OnsetStrength("id", 300, 4000, 512, 1f);
        assertEquals("OnsetStrength{low=300Hz, high=4000Hz, hop=512, effectiveHop=512, incFactor=1.0}", processor.toString());
    }


    private static class MockChildProcessor implements SignalProcessor<AudioBuffer, AudioBuffer> {

        private AudioBuffer output;

        public void process(final AudioBuffer audioBuffer) throws IOException {
            output = audioBuffer;
        }

        public void flush() throws IOException {
        }

        public AudioBuffer getOutput() throws IOException {
            return output;
        }

        public Object getId() {
            return null;
        }

        public <O2> SignalProcessor<AudioBuffer, O2> connectTo(final SignalProcessor<AudioBuffer, O2> objectO2SignalProcessor) {
            return null;
        }

        public <O2> SignalProcessor<AudioBuffer, O2> disconnectFrom(final SignalProcessor<AudioBuffer, O2> objectO2SignalProcessor) {
            return null;
        }

        public SignalProcessor<AudioBuffer, ?>[] getConnectedProcessors() {
            return new SignalProcessor[0];
        }
    }
}
