/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;
import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TestTimestampLimitedSignalSource.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestTimestampLimitedSignalSource {

    private SignalSource<AudioBuffer> monoSource;

    @Before
    public void setup() {
        this.monoSource = new SignalSource<AudioBuffer>() {

            private int frameNumber = 0;

            public void reset() {
                frameNumber = 0;
            }

            public AudioBuffer read() throws IOException {
                final RealAudioBuffer buffer = new RealAudioBuffer(
                        frameNumber, new float[]{
                        10, 10, 10, 10, 10,
                        10, 10, 10, 10, 10,
                }, new AudioFormat(10000, 8, 1, true, true)
                );
                frameNumber += buffer.getNumberOfSamples();
                return buffer;
            }
        };
    }

    @Test
    public void testBasics() throws IOException {
        final long maxTimestampInMS = 1000l;
        final TimestampLimitedSignalSource<AudioBuffer> source = new TimestampLimitedSignalSource<AudioBuffer>(monoSource, maxTimestampInMS);
        assertEquals(maxTimestampInMS, source.getMaxTimestamp(TimeUnit.MILLISECONDS));
        assertEquals(monoSource, source.getSignalSource());

        AudioBuffer buffer;
        while ((buffer = source.read()) != null) {
            assertTrue(buffer.getTimestamp() < maxTimestampInMS);
        }
        source.close();
        // attempt to close twice
        source.close();
    }

    @Test
    public void testToString() throws IOException {
        final long maxTimestampInMS = 1000l;
        final TimestampLimitedSignalSource<AudioBuffer> source = new TimestampLimitedSignalSource<AudioBuffer>(monoSource, maxTimestampInMS);

        assertTrue(Pattern.matches("TimestampLimitedSignalSource\\{maxTimestamp=1000 milliseconds, signalSource=.+\\}", source.toString()));
    }
}
