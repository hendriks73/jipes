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
 * TestFFT
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFFT {

    private SignalSource<AudioBuffer> monoSource = new SignalSource<AudioBuffer>() {

        public void reset() {
        }

        public AudioBuffer read() throws IOException {
            return new RealAudioBuffer(
                    0, new float[]{1, 2, 1, 2, 1, 2, 1, 2}, new AudioFormat(10000, 32, 1, true, true)
            );
        }
    };

    @Test
    public void testSimpleFFT() throws IOException {
        final FFT fft = new FFT();
        assertEquals(0, fft.getLength());
        assertEquals(0, fft.getRequiredResolutionInHz(), 0.0001f);
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
        fft.connectTo(new NullAudioBufferSource<AudioBuffer>());
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

    @Test
    public void testEquals() {
        final FFT fft0 = new FFT(1024);
        final FFT fft1 = new FFT(1024);
        final FFT fft2 = new FFT(1024 * 2);
        final FFT fft3 = new FFT(40f);
        final FFT fft4 = new FFT(40f);
        final FFT fft5 = new FFT(50f);
        final FFT fft6 = new FFT();
        final FFT fft7 = new FFT();

        assertEquals(fft0, fft1);
        assertNotEquals(fft0, fft2);
        assertNotEquals(fft0, fft3);

        assertEquals(fft3, fft4);
        assertNotEquals(fft3, fft5);
        assertNotEquals(fft3, fft0);

        assertEquals(fft6, fft7);
        assertNotEquals(fft6, fft0);
        assertNotEquals(fft6, fft3);
    }

    @Test
    public void testTargetResolution() throws IOException {
        final FFT fft = new FFT(40f);
        fft.processNext(new RealAudioBuffer(0, new float[1024], new AudioFormat(10000, 8, 1, true, true)));
        fft.processNext(new RealAudioBuffer(1, new float[1024], new AudioFormat(10000, 8, 1, true, true)));
    }
}
