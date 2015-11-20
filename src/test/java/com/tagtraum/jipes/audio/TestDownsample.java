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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * TestDownsample.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestDownsample {

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
    public void testDownsample2Mono() throws IOException {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        processor.connectTo(monoSource);
        final AudioBuffer downsampled = processor.read();
        assertEquals(50, downsampled.getFrameNumber());
        assertEquals(DATA.length/2, downsampled.getData().length);
        for (final float f : downsampled.getData()) {
            assertEquals(1f, f, 0.0001f);
            assertEquals(monoSource.read().getAudioFormat().getSampleRate(), downsampled.getAudioFormat().getSampleRate() * 2, 0.0001f);
        }
    }

    @Test
    public void testDownsample2Stereo() throws IOException {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        assertEquals(2, processor.getNthFrameToKeep());
        processor.connectTo(stereoSource);
        final AudioBuffer downsampled = processor.read();
        for (int i=0; i<downsampled.getData().length; i=i+2) {
            assertEquals(1f, downsampled.getData()[i], 0.0001f);
            assertEquals(2f, downsampled.getData()[i+1], 0.0001f);
            assertEquals(stereoSource.read().getAudioFormat().getSampleRate(), downsampled.getAudioFormat().getSampleRate() * 2, 0.0001f);
        }
        processor.read();
    }

    @Test
    public void testDownsampleNull() throws IOException {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        processor.connectTo(nullSource);
        final AudioBuffer downsampled = processor.read();
        assertNull(downsampled);
    }

    @Test
    public void testToString() {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        assertEquals("Downsample{nthFrameToKeep=2}", processor.toString());
    }

    @Test
    public void testEqualsHashCode() {
        final Downsample processor0 = new Downsample(2);
        final Downsample processor1 = new Downsample(2);
        final Downsample processor2 = new Downsample(3);

        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

}
