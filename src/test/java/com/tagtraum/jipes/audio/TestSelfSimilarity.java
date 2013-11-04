/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * TestSelfSimilarity.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSelfSimilarity {

    @Test
    public void testSelfSimilarity() throws IOException {
        final SelfSimilarity<AudioBuffer> selfSimilarity = new SelfSimilarity<AudioBuffer>();
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

}
