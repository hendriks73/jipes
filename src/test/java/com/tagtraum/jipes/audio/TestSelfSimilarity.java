/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalProcessor;
import com.tagtraum.jipes.math.DistanceFunction;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * TestSelfSimilarity.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSelfSimilarity {

    @Test
    public void testBasics() {
        final String id = "some id";
        final SelfSimilarity<AudioBuffer> selfSimilarity = new SelfSimilarity<AudioBuffer>(id);
        assertEquals(id, selfSimilarity.getId());
        final String other = "other";
        selfSimilarity.setId(other);
        assertEquals(other, selfSimilarity.getId());

        assertFalse(selfSimilarity.isCopyOnMatrixEnlargement());
        selfSimilarity.setCopyOnMatrixEnlargement(true);
        assertTrue(selfSimilarity.isCopyOnMatrixEnlargement());
    }

    @Test
    public void testSelfSimilarity() throws IOException {
        final SelfSimilarity<AudioBuffer> selfSimilarity = new SelfSimilarity<AudioBuffer>();
        selfSimilarity.setCopyOnMatrixEnlargement(true);
        final AudioFormat bogusAudioFormat = new AudioFormat(10f, 8, 1, true, true);
        for (int i=0; i<10; i++) {
            selfSimilarity.process(new RealAudioBuffer(2 * i, new float[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, bogusAudioFormat));
            selfSimilarity.process(new RealAudioBuffer(2 * i + 1, new float[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, bogusAudioFormat));
        }
        selfSimilarity.flush();
        final AudioMatrix matrix = selfSimilarity.getOutput();

        assertEquals(20, matrix.getNumberOfColumns());
        assertEquals(20, matrix.getNumberOfRows());

        for (int row=0; row<matrix.getNumberOfRows(); row++) {
            for (int column=0; column<matrix.getNumberOfColumns(); column++) {
                // exploit the fact that we have only two different values
                if ((row+column)%2==0) assertEquals(1.0f, matrix.getData(row, column), 0.001f);
                else assertEquals(0.4516129f, matrix.getData(row, column), 0.001f);
                //System.out.print(matrix.getData(row, column) + "\t");
            }
            //System.out.println();
        }
    }

    @Test
    public void testBandSelfSimilarity() throws IOException {
        for (int bandwidth=1; bandwidth<12; bandwidth+=2) {
            final SelfSimilarity<AudioBuffer> selfSimilarity = new SelfSimilarity<AudioBuffer>();
            selfSimilarity.setBandwidth(bandwidth);
            final AudioFormat bogusAudioFormat = new AudioFormat(10f, 8, 1, true, true);
            for (int i=0; i<10; i++) {
                selfSimilarity.process(new RealAudioBuffer(2 * i, new float[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, bogusAudioFormat));
                selfSimilarity.process(new RealAudioBuffer(2 * i + 1, new float[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, bogusAudioFormat));
            }
            selfSimilarity.flush();
            final AudioMatrix matrix = selfSimilarity.getOutput();

            assertEquals(20, matrix.getNumberOfColumns());
            assertEquals(20, matrix.getNumberOfRows());

            for (int row=0; row<matrix.getNumberOfRows(); row++) {
                for (int column=0; column<matrix.getNumberOfColumns(); column++) {
                    // exploit the fact that we have only two different values
                    if (Math.abs(row - column) > selfSimilarity.getBandwidth()/2) assertEquals(0f, matrix.getData(row, column), 0.001f);
                    else if ((row+column)%2==0) assertEquals(1.0f, matrix.getData(row, column), 0.001f);
                    else assertEquals(0.4516129f, matrix.getData(row, column), 0.001f);
                    //System.out.print(matrix.getData(row, column) + "\t");
                }
                //System.out.println();
            }
        }
    }

    @Test
    public void testConnections() {
        final SignalProcessor<AudioBuffer, AudioMatrix> processor = new SelfSimilarity<AudioBuffer>("", 5, new DistanceFunction<AudioBuffer>() {
            @Override
            public float distance(final AudioBuffer a, final AudioBuffer b) {
                return 0;
            }
        });
        assertArrayEquals(new SignalProcessor[0], processor.getConnectedProcessors());
        final SignalProcessor mock = mock(SignalProcessor.class);
        processor.connectTo(mock);
        assertArrayEquals(new SignalProcessor[]{mock}, processor.getConnectedProcessors());
        processor.disconnectFrom(mock);
        assertArrayEquals(new SignalProcessor[0], processor.getConnectedProcessors());
    }

}
