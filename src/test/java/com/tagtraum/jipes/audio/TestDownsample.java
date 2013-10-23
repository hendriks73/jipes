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
 * TestDownsample.
 * <p/>
 * Date: Jul 22, 2010
 * Time: 11:31:38 PM
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestDownsample extends TestCase {

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

    public void testDownsample2Mono() throws IOException {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        processor.connectTo(monoSource);
        final AudioBuffer downsampled = processor.read();
        assertEquals(50, downsampled.getFrameNumber());
        assertEquals(DATA.length/2, downsampled.getData().length);
        for (final float f : downsampled.getData()) {
            assertEquals(1f, f);
            assertEquals(monoSource.read().getAudioFormat().getSampleRate(), downsampled.getAudioFormat().getSampleRate() * 2);
        }
    }


    public void testDownsample2Stereo() throws IOException {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        processor.connectTo(stereoSource);
        final AudioBuffer downsampled = processor.read();
        for (int i=0; i<downsampled.getData().length; i=i+2) {
            assertEquals(1f, downsampled.getData()[i]);
            assertEquals(2f, downsampled.getData()[i+1]);
            assertEquals(stereoSource.read().getAudioFormat().getSampleRate(), downsampled.getAudioFormat().getSampleRate() * 2);
        }
    }

    public void testDownsampleNull() throws IOException {
        final Downsample processor = new Downsample();
        processor.setNthFrameToKeep(2);
        processor.connectTo(nullSource);
        final AudioBuffer downsampled = processor.read();
        assertNull(downsampled);
    }

}
