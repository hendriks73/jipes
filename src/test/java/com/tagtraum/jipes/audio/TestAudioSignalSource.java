/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * TestAudioSignalSource.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAudioSignalSource {

    private static final boolean WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("win");
    private static final boolean MAC = System.getProperty("os.name").toLowerCase().contains("mac");
    private static final boolean NOT_MAC_WINDOWS = !MAC && !WINDOWS;

    // TODO: test big endian

    @Test
    public void testBasics() throws IOException, UnsupportedAudioFileException {
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("mono_10_s16le.wav", ".wav"));
        assertTrue(signalSource.isNormalize());
        signalSource.setNormalize(false);
        assertFalse(signalSource.isNormalize());
    }

    @Test
    public void testEqualsHashCode() throws IOException, UnsupportedAudioFileException {
        final File file = extractFile("mono_10_s16le.wav", ".wav");
        try {
            final AudioInputStream stream0 = AudioSystem.getAudioInputStream(file);
            final AudioInputStream stream1 = AudioSystem.getAudioInputStream(file);
            final AudioSignalSource signalSource0 = new AudioSignalSource(stream0);
            final AudioSignalSource signalSource1 = new AudioSignalSource(stream0);
            final AudioSignalSource signalSource2 = new AudioSignalSource(stream1);

            assertEquals(signalSource0, signalSource1);
            assertEquals(signalSource0.hashCode(), signalSource1.hashCode());
            assertNotEquals(signalSource0, signalSource2);
            assertNotEquals(signalSource0.hashCode(), signalSource2.hashCode());
        } finally {
            file.delete();
        }
    }

    @Test
    public void testToString() throws IOException, UnsupportedAudioFileException {
        final File file = extractFile("mono_10_s16le.wav", ".wav");
        try {
            final AudioSignalSource signalSource = new AudioSignalSource(AudioSystem.getAudioInputStream(file));

            assertTrue(Pattern.matches("AudioSignalSource\\{audioInputStream=.+\\}", signalSource.toString()));
        } finally {
            file.delete();
        }
    }

    @Test
    public void testMonoSigned16BitSignal() throws IOException, UnsupportedAudioFileException {
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("mono_10_s16le.wav", ".wav"));
        AudioBuffer buffer;
        while ((buffer = signalSource.read()) != null) {
            final AudioFormat audioFormat = buffer.getAudioFormat();
            // The float buffer returned by AudioSignalSource, always has 32 bits sample size.
            assertEquals(32, audioFormat.getSampleSizeInBits());
            assertEquals(1, audioFormat.getChannels());
            assertEquals(44100f, audioFormat.getSampleRate(), 0.01f);
            assertEquals(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getEncoding());
            for (final float f : buffer.getData()) {
                assertTrue(f<=1f);
                assertTrue(f>=-1f);
            }
        }
    }

    @Test
    public void testStereoSigned16BitSignal() throws IOException, UnsupportedAudioFileException {
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("audio_10_s16le.wav", ".wav"));
        AudioBuffer buffer;
        while ((buffer = signalSource.read()) != null) {
            final AudioFormat audioFormat = buffer.getAudioFormat();
            // The float buffer returned by AudioSignalSource, always has 32 bits sample size.
            assertEquals(32, audioFormat.getSampleSizeInBits());
            assertEquals(2, audioFormat.getChannels());
            assertEquals(44100f, audioFormat.getSampleRate(), 0.01f);
            assertEquals(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getEncoding());
            for (final float f : buffer.getData()) {
                assertTrue(f<=1f);
                assertTrue(f>=-1f);
            }
        }
    }

    @Test
    public void testStereoSigned24BitSignal() throws IOException, UnsupportedAudioFileException {
        if (NOT_MAC_WINDOWS) {
            System.out.println("Skipped test, as the necessary SPI is not available.\n" + new Exception().getStackTrace()[0]);
            return;
        }
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("audio_10_s24le.wav", ".wav"));
        AudioBuffer buffer;
        while ((buffer = signalSource.read()) != null) {
            final AudioFormat audioFormat = buffer.getAudioFormat();
            // The float buffer returned by AudioSignalSource, always has 32 bits sample size.
            assertEquals(32, audioFormat.getSampleSizeInBits());
            assertEquals(2, audioFormat.getChannels());
            assertEquals(44100f, audioFormat.getSampleRate(), 0.01f);
            assertEquals(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getEncoding());
            for (final float f : buffer.getData()) {
                assertTrue(f<=1f);
                assertTrue(f>=-1f);
            }
        }
    }

    @Test
    public void testStereoSigned32BitSignal() throws IOException, UnsupportedAudioFileException {
        if (NOT_MAC_WINDOWS) {
            System.out.println("Skipped test, as the necessary SPI is not available.\n" + new Exception().getStackTrace()[0]);
            return;
        }
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("audio_10_s32le.wav", ".wav"));
        AudioBuffer buffer;
        while ((buffer = signalSource.read()) != null) {
            final AudioFormat audioFormat = buffer.getAudioFormat();
            // The float buffer returned by AudioSignalSource, always has 32 bits sample size.
            assertEquals(32, audioFormat.getSampleSizeInBits());
            assertEquals(2, audioFormat.getChannels());
            assertEquals(44100f, audioFormat.getSampleRate(), 0.01f);
            assertEquals(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getEncoding());
            for (final float f : buffer.getData()) {
                assertTrue(f<=1f);
                assertTrue(f>=-1f);
            }
        }
    }

    @Test
    public void testStereoUnsigned8BitSignal() throws IOException, UnsupportedAudioFileException {
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("audio_10_u8.wav", ".wav"));
        AudioBuffer buffer;
        while ((buffer = signalSource.read()) != null) {
            final AudioFormat audioFormat = buffer.getAudioFormat();
            // The float buffer returned by AudioSignalSource, always has 32 bits sample size.
            assertEquals(32, audioFormat.getSampleSizeInBits());
            assertEquals(2, audioFormat.getChannels());
            assertEquals(44100f, audioFormat.getSampleRate(), 0.01f);
            assertEquals(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getEncoding());
            for (final float f : buffer.getData()) {
                assertTrue(f<=1f);
                assertTrue(f>=-1f);
            }
        }
    }

    @Test
    public void testStereoMP3Signal() throws IOException, UnsupportedAudioFileException {
        if (NOT_MAC_WINDOWS) {
            System.out.println("Skipped test, as the necessary SPI is not available.\n" + new Exception().getStackTrace()[0]);
            return;
        }
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("audio_10.mp3", ".mp3"));
        AudioBuffer buffer;
        while ((buffer = signalSource.read()) != null) {
            final AudioFormat audioFormat = buffer.getAudioFormat();
            assertEquals(2, audioFormat.getChannels());
            assertEquals(44100f, audioFormat.getSampleRate(), 0.01f);
            assertEquals(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getEncoding());
            for (final float f : buffer.getData()) {
                assertTrue(f<=1f);
                assertTrue(f>=-1f);
            }
        }
    }

    @Test
    public void testIllegalAudioFormats() {
        try {
            new AudioSignalSource(new AudioInputStream(null, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 1f, AudioSystem.NOT_SPECIFIED, 2, 4, 1f, true), 0l));
            fail("Expected IllegalArgumentException because of illegal sample size in bits");
        } catch (IllegalArgumentException e) {
            // expected this
        }

        try {
            new AudioSignalSource(new AudioInputStream(null, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 1f, 16, AudioSystem.NOT_SPECIFIED, 4, 1f, true), 0l));
            fail("Expected IllegalArgumentException because of illegal channels");
        } catch (IllegalArgumentException e) {
            // expected this
        }

        try {
            new AudioSignalSource(new AudioInputStream(null, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 1f, 16, 2, AudioSystem.NOT_SPECIFIED, 1f, true), 0l));
            fail("Expected IllegalArgumentException because of illegal framesize");
        } catch (IllegalArgumentException e) {
            // expected this
        }
    }

    @Test
    public void testResetMustHaveNoEffect() throws IOException, UnsupportedAudioFileException {
        if (NOT_MAC_WINDOWS) {
            System.out.println("Skipped test, as the necessary SPI is not available.\n" + new Exception().getStackTrace()[0]);
            return;
        }
        final AudioSignalSource signalSource = new AudioSignalSource(extractFile("audio_10.mp3", ".mp3"));
        signalSource.reset();
        assertNotNull(signalSource.read());
    }

    private static File extractFile(final String name, final String extension) throws IOException {
        final File audioFile = File.createTempFile("TestAudioSignalSource", extension);
        audioFile.deleteOnExit();
        final InputStream in = TestAudioSignalSource.class.getResourceAsStream(name);
        final OutputStream out = new FileOutputStream(audioFile);
        final byte[] buf = new byte[1024*64];
        int justRead;
        while ((justRead = in.read(buf)) != -1) {
            out.write(buf, 0, justRead);
        }
        in.close();
        out.close();
        return audioFile;
    }

}
