/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.io.*;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * TestFullMatrix.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFullMatrix {

    @Test
    public void testWriteAndRead() {
        final FullMatrix matrix = new FullMatrix(4, 5);
        final float[] values = new float[4*5];
        for (int i=0; i<values.length; i++) {
            values[i] = new Random().nextFloat();
        }
        for (int row=0; row<4; row++) {
            for (int column=0; column<5; column++) {
                matrix.set(row, column, values[row*5+column]);
            }
        }
        for (int row=0; row<4; row++) {
            for (int column=0; column<5; column++) {
                assertEquals(values[row * 5 + column], matrix.get(row, column), 0.0001f);
            }
        }
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeRow() {
        new FullMatrix(4, 5).get(-1, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeColumn() {
        new FullMatrix(4, 5).get(0, -1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeRow() {
        new FullMatrix(4, 5).get(4, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeColumn() {
        new FullMatrix(4, 5).get(0, 5);
    }

    @Test
    public void testReadCSV() throws IOException {
        final File file = extractFile("matrix.csv", ".csv");
        final FullMatrix matrix = new FullMatrix(file, "ASCII", ',', true);
        assertEquals(3, matrix.getNumberOfRows());
        assertEquals(3, matrix.getNumberOfColumns());
        for (int i=0; i<9; i++) {
            assertEquals(i+1f, matrix.get(i/3, i%3), 0.001f);
        }
    }

    private static File extractFile(final String name, final String extension) throws IOException {
        final File audioFile = File.createTempFile("TestFullMatrix", extension);
        audioFile.deleteOnExit();
        final InputStream in = TestFullMatrix.class.getResourceAsStream(name);
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
