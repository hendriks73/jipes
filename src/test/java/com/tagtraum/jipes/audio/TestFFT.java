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
import static org.junit.Assert.assertNull;

/**
 * TestFFT
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFFT {

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

    @Test
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

    @Test
    public void testNullGenerator() throws IOException {
        final FFT fft = new FFT();
        fft.connectTo(new NullAudioBufferSource());
        assertNull(fft.read());
    }

    @Test
    public void testToString() {
        final FFT fft1024 = new FFT(1024);
        assertEquals("FFT{length=1024}", fft1024.toString());
        final FFT fft40Hz = new FFT(40f);
        assertEquals("FFT{requiredResolutionInHz=40.0Hz}", fft40Hz.toString());
        final FFT fftFirstInput = new FFT(0);
        assertEquals("FFT{length=equal to first input}", fftFirstInput.toString());
    }

    @Test
    public void testHashCode() {
        final FFT fft = new FFT();
        assertEquals(0, fft.hashCode());
    }

    @Test(expected = IOException.class)
    public void testMono() throws IOException {
        final FFT fft = new FFT();
        fft.processNext(new RealAudioBuffer(1, new float[1024], new AudioFormat(10000, 8, 2, true, true)));
    }

}
