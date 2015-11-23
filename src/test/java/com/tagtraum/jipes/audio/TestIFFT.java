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

import static org.junit.Assert.*;

/**
 * TestIFFT
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestIFFT {

    private SignalSource<LinearFrequencySpectrum> monoSource = new SignalSource<LinearFrequencySpectrum>() {

        public void reset() {
        }

        public LinearFrequencySpectrum read() throws IOException {
            return new LinearFrequencySpectrum(
                    0, new float[]{1, 2, 1, 2, 1, 2, 1, 2}, new float[]{2, 1, 2, 1, 2, 1, 2, 1}, new AudioFormat(10000, 32, 1, true, true)
            );
        }
    };

    @Test
    public void testSimpleIFFT() throws IOException {
        final IFFT ifft = new IFFT();
        assertEquals(0, ifft.getLength());
        assertEquals(0, ifft.getRequiredResolutionInHz(), 0.0001f);
        ifft.connectTo(monoSource);
        final AudioBuffer samples = ifft.read();
        float[] realData = samples.getRealData();

        assertArrayEquals(new float[]{1.5f, 0.0f, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, 0.0f}, realData, 0.0001f);
    }

    @Test
    public void testNullGenerator() throws IOException {
        final IFFT ifft = new IFFT();
        ifft.connectTo(new NullAudioBufferSource<LinearFrequencySpectrum>());
        assertNull(ifft.read());
    }

    @Test
    public void testToString() {
        final IFFT ifft1024 = new IFFT(1024);
        assertEquals("IFFT{length=1024}", ifft1024.toString());
        final IFFT ifft40Hz = new IFFT(40f);
        assertEquals("IFFT{requiredResolutionInHz=40.0Hz}", ifft40Hz.toString());
        final IFFT ifftFirstInput = new IFFT(0);
        assertEquals("IFFT{length=equal to first input}", ifftFirstInput.toString());
    }

    @Test
    public void testHashCode() {
        final IFFT ifft = new IFFT();
        assertEquals(0, ifft.hashCode());
    }

    @Test(expected = IOException.class)
    public void testMono() throws IOException {
        final IFFT ifft = new IFFT();
        ifft.processNext(new LinearFrequencySpectrum(1, new float[1024], new float[1024], new AudioFormat(10000, 8, 2, true, true)));
    }

    @Test
    public void testEquals() {
        final IFFT ifft0 = new IFFT(1024);
        final IFFT ifft1 = new IFFT(1024);
        final IFFT ifft2 = new IFFT(1024 * 2);
        final IFFT ifft3 = new IFFT(40f);
        final IFFT ifft4 = new IFFT(40f);
        final IFFT ifft5 = new IFFT(50f);
        final IFFT ifft6 = new IFFT();
        final IFFT ifft7 = new IFFT();

        assertEquals(ifft0, ifft1);
        assertNotEquals(ifft0, ifft2);
        assertNotEquals(ifft0, ifft3);

        assertEquals(ifft3, ifft4);
        assertNotEquals(ifft3, ifft5);
        assertNotEquals(ifft3, ifft0);

        assertEquals(ifft6, ifft7);
        assertNotEquals(ifft6, ifft0);
        assertNotEquals(ifft6, ifft3);
    }

    @Test
    public void testTargetResolution() throws IOException {
        final IFFT ifft = new IFFT(40f);
        ifft.processNext(new LinearFrequencySpectrum(0, new float[1024], new float[1024], new AudioFormat(10000, 8, 1, true, true)));
        ifft.processNext(new LinearFrequencySpectrum(1, new float[1024], new float[1024], new AudioFormat(10000, 8, 1, true, true)));
    }
}
