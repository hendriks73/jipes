/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;
import junit.framework.TestCase;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

/**
 * TestUpsample.
 * <p/>
 * Date: Sep 29, 2011
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestUpsample extends TestCase {

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testUpsample2Mono() throws IOException {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        processor.connectTo(monoSource);
        final AudioBuffer upsampled = processor.read();
        assertEquals(200, upsampled.getFrameNumber());
        assertEquals(DATA.length*2, upsampled.getData().length);

        for (int i=0; i<upsampled.getData().length; i=i+2) {
            final float fEven = upsampled.getData()[i];
            assertEquals(DATA[i/2], fEven);
            final float fOdd = upsampled.getData()[i+1];
            assertEquals(0f, fOdd);
            assertEquals(monoSource.read().getAudioFormat().getSampleRate(), upsampled.getAudioFormat().getSampleRate() / 2f);
        }
    }


    public void testUpsample2Stereo() throws IOException {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        processor.connectTo(stereoSource);
        final AudioBuffer upsampled = processor.read();
        for (int i=0; i<upsampled.getData().length; i=i+4) {
            assertEquals(1f, upsampled.getData()[i]);
            assertEquals(2f, upsampled.getData()[i+1]);
            assertEquals(0f, upsampled.getData()[i+2]);
            assertEquals(0f, upsampled.getData()[i+3]);
            assertEquals(stereoSource.read().getAudioFormat().getSampleRate(), upsampled.getAudioFormat().getSampleRate() / 2f);
        }
    }

    public void testUpsampleNull() throws IOException {
        final Upsample processor = new Upsample();
        processor.setFactor(2);
        processor.connectTo(nullSource);
        final AudioBuffer Upsampled = processor.read();
        assertNull(Upsampled);
    }

}
