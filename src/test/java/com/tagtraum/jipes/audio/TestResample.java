/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * TestResample.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestResample {

    public static final float[] DATA = new float[]{
            1, 2, 1, 2, 1, 2, 1, 2
    };
    private SignalSource<AudioBuffer> monoSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(100, DATA, new AudioFormat(10000, 32, 1, true, true));
        }
    };

    private SignalSource<AudioBuffer> stereoSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(100, DATA, new AudioFormat(10000, 32, 2, true, true));
        }

    };

    private SignalSource<AudioBuffer> nullSource = new NullAudioBufferSource();

    @Test
    public void testResample2Mono() throws IOException {
        final Resample processor = new Resample(1, 2);
        processor.connectTo(monoSource);
        final AudioBuffer resampled = processor.read();
        assertEquals(50, resampled.getFrameNumber());
        assertEquals(DATA.length/2, resampled.getData().length);
        for (final float f : resampled.getData()) {
            //assertEquals(1f, f, 0.0001f);
            assertEquals(monoSource.read().getAudioFormat().getSampleRate(), resampled.getAudioFormat().getSampleRate() * 2, 0.0001f);
        }
        processor.read();
    }

    @Test(expected = IOException.class)
    public void testResample2Stereo() throws IOException {
        final Resample processor = new Resample(1, 2);
        assertEquals(0.5, processor.getFactor(), 0.0001f);
        processor.connectTo(stereoSource);
        processor.read();
    }

    @Test
    public void testResampleNull() throws IOException {
        final Resample processor = new Resample(1, 2);
        processor.connectTo(nullSource);
        final AudioBuffer downsampled = processor.read();
        assertNull(downsampled);
    }

    @Test
    public void testToString() {
        final Resample processor = new Resample(1, 2);
        assertEquals("Resample{factor=1/2}", processor.toString());
    }

    @Test
    public void testEqualsHashCode() {
        final Resample processor0 = new Resample(3, 4);
        final Resample processor1 = new Resample(3, 4);
        final Resample processor2 = new Resample(3, 5);

        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

}
