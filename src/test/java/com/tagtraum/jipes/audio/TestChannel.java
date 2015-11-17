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
 * TestChannel.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestChannel {

    private SignalSource<AudioBuffer> channelSource = new SignalSource<AudioBuffer>() {

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


    @Test
    public void testChannelGenerator() throws IOException {
        final Channel channel = new Channel(0);
        channel.connectTo(channelSource);
        assertEquals(1, channel.read().getAudioFormat().getChannels());
        final float[] floats = channel.read().getData();
        final float[] bytes = channelSource.read().getData();
        for (int i = 0; i < floats.length; i++) {
            assertEquals(bytes[i], floats[i], 0.01);
        }
    }

    @Test
    public void testStereoGenerator0() throws IOException {
        final Channel channel = new Channel(0);
        channel.connectTo(stereoSource);
        assertEquals(1, channel.read().getAudioFormat().getChannels());
        final float[] floats = channel.read().getData();
        final float[] bytes = stereoSource.read().getData();
        assertEquals(bytes.length, floats.length * 2);
        for (final float f : floats) {
            assertEquals(10.0, f, 0.00001);
        }
    }

    @Test
    public void testStereoGenerator1() throws IOException {
        final Channel channel = new Channel(1);
        channel.connectTo(stereoSource);
        assertEquals(1, channel.read().getAudioFormat().getChannels());
        final float[] floats = channel.read().getData();
        final float[] bytes = stereoSource.read().getData();
        assertEquals(bytes.length, floats.length * 2);
        for (final float f : floats) {
            assertEquals(20.0, f, 0.00001);
        }
    }

    @Test
    public void testNullGenerator() throws IOException {
        final Channel processor = new Channel(0);
        processor.connectTo(new NullAudioBufferSource());
        assertNull(processor.read());
    }

    @Test
    public void testEqualsHashCode() throws IOException {
        final Channel channel0 = new Channel(0);
        final Channel channel1 = new Channel(0);
        final Channel channel2 = new Channel(1);

        assertEquals(channel0, channel1);
        assertEquals(channel0.hashCode(), channel1.hashCode());
        assertNotEquals(channel0, channel2);
        assertNotEquals(channel0.hashCode(), channel2.hashCode());
    }

    @Test
    public void testToString() throws IOException {
        final Channel channel = new Channel(0);
        assertEquals("Channel[0]", channel.toString());
    }

}
