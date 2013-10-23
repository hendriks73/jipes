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
 * TestMono.
 * <p/>
 * Date: Jul 23, 2010
 * Time: 9:58:37 AM
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestMono extends TestCase {

    private SignalSource<AudioBuffer> monoSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(
                    0, new float[]{
                    10, 10, 10, 10, 10,
                    10, 10, 10, 10, 10,
                }, new AudioFormat(10000, 8, 1, true, true)
            );
        }
    };

    private SignalSource<AudioBuffer> stereoSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(
                    0, new float[]{
                            10, 20, 10, 20,
                            10, 20, 10, 20
                }, new AudioFormat(10000, 8, 2, true, true)
            );
        }

    };


    public void testMonoGenerator() throws IOException {
        final Mono mono = new Mono();
        mono.connectTo(monoSource);
        assertEquals(1, mono.read().getAudioFormat().getChannels());
        final float[] floats = mono.read().getData();
        final float[] bytes = monoSource.read().getData();
        for (int i = 0; i < floats.length; i++) {
            assertEquals(bytes[i], floats[i], 0.01);
        }
    }

    public void testStereoGenerator() throws IOException {
        final Mono mono = new Mono();
        mono.connectTo(stereoSource);
        assertEquals(1, mono.read().getAudioFormat().getChannels());
        final float[] floats = mono.read().getData();
        final float[] bytes = stereoSource.read().getData();
        assertEquals(bytes.length, floats.length * 2);
        for (final float f : floats) {
            assertEquals(15.0, f, 0.00001);
        }
    }

    public void testNullGenerator() throws IOException {
        final Mono processor = new Mono();
        processor.connectTo(new NullAudioBufferSource());
        assertNull(processor.read());
    }

}
