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
 * TestFFT.
 * <p/>
 * Date: Jul 22, 2010
 * Time: 11:43:40 PM
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFFT extends TestCase {

    private SignalSource<AudioBuffer> monoSource = new SignalSource<AudioBuffer>() {
        public AudioFormat getProcessedAudioFormat() {
            return new AudioFormat(10000, 32, 1, true, true);
        }

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(
                    0, new float[] { 1, 2, 1, 2, 1, 2, 1, 2 }, new AudioFormat(10000, 32, 1, true, true)
            );
        }
    };

    public void testSimpleFFT() throws IOException {
        final FFT fft = new FFT();
        fft.connectTo(monoSource);
        final LinearFrequencySpectrum spectrum = fft.read();
        float[] realData = spectrum.getRealData();
        assertEquals(8, realData.length);
        assertEquals(12.0, realData[0], 0.0001);
        assertEquals(0.0, realData[1], 0.0001);
        assertEquals(0.0, realData[2], 0.0001);
        assertEquals(0.0, realData[3], 0.0001);
    }

    public void testNullGenerator() throws IOException {
        final FFT fft = new FFT();
        fft.connectTo(new NullAudioBufferSource());
        assertNull(fft.read());
    }

}
