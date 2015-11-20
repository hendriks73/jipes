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
 * TestUpsample.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestUpsample {

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
    public void testUpsample2Mono() throws IOException {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        assertEquals(2, processor.getFactor());
        processor.connectTo(monoSource);
        final AudioBuffer upsampled = processor.read();
        assertEquals(200, upsampled.getFrameNumber());
        assertEquals(DATA.length*2, upsampled.getData().length);

        for (int i=0; i<upsampled.getData().length; i=i+2) {
            final float fEven = upsampled.getData()[i];
            assertEquals(DATA[i/2], fEven, 0.0001f);
            final float fOdd = upsampled.getData()[i+1];
            assertEquals(0f, fOdd, 0.0001f);
            assertEquals(monoSource.read().getAudioFormat().getSampleRate(), upsampled.getAudioFormat().getSampleRate() / 2f, 0.0001f);
        }
    }

    @Test
    public void testUpsample2Stereo() throws IOException {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        processor.connectTo(stereoSource);
        final AudioBuffer upsampled = processor.read();
        for (int i=0; i<upsampled.getData().length; i=i+4) {
            assertEquals(1f, upsampled.getData()[i], 0.0001f);
            assertEquals(2f, upsampled.getData()[i+1], 0.0001f);
            assertEquals(0f, upsampled.getData()[i+2], 0.0001f);
            assertEquals(0f, upsampled.getData()[i+3], 0.0001f);
            assertEquals(stereoSource.read().getAudioFormat().getSampleRate(), upsampled.getAudioFormat().getSampleRate() / 2f, 0.0001f);
        }
        processor.read();
    }

    @Test
    public void testUpsampleNull() throws IOException {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        processor.connectTo(nullSource);
        final AudioBuffer Upsampled = processor.read();
        assertNull(Upsampled);
    }

    @Test
    public void testToString() {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        assertEquals("Upsample{factor=2}", processor.toString());
    }

    @Test
    public void testEqualsHashCode() {
        final Upsample processor0 = new Upsample(2);
        final Upsample processor1 = new Upsample(2);
        final Upsample processor2 = new Upsample(3);

        assertEquals(processor0, processor1);
        assertEquals(processor0.hashCode(), processor1.hashCode());
        assertNotEquals(processor0, processor2);
        assertNotEquals(processor0.hashCode(), processor2.hashCode());
    }

}
