/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * TestSymmetricBandMatrix.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSymmetricBandMatrix {

    @Test
    public void testWriteAndRead() {
        final SymmetricBandMatrix matrix = new SymmetricBandMatrix(5, 3, 0f);
        final float[] values = new float[5*5];
        for (int i=0; i<values.length; i++) {
            values[i] = new Random().nextFloat();
        }
        for (int row=0; row<5; row++) {
            for (int column=row; column<Math.min(row+2, 5); column++) {
                matrix.set(row, column, values[row*5+column]);
            }
        }

        // for visual confirmation
        /*
        for (int row=0; row<5; row++) {
            for (int column=0; column<5; column++) {
                System.out.print(matrix.get(row, column) + "\t");
                if (column == 4) System.out.println();
            }
        }
        */

        for (int row=0; row<5; row++) {
            for (int column=row; column<Math.min(row+2, 5); column++) {
                assertEquals(values[row * 5 + column], matrix.get(row, column), 0.0001f);
            }
        }

        for (int column=0; column<5; column++) {
            for (int row=column; row<Math.min(column+2, 5); row++) {
                assertEquals(values[column * 5 + row], matrix.get(row, column), 0.0001f);
            }
        }
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsOutOfBand() {
        new SymmetricBandMatrix(5, 3, 0f).set(0, 2, 5);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeRow() {
        new SymmetricBandMatrix(5, 3, 0f).get(-1, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeColumn() {
        new SymmetricBandMatrix(5, 3, 0f).get(0, -1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeRow() {
        new SymmetricBandMatrix(5, 3, 0f).get(5, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeColumn() {
        new SymmetricBandMatrix(5, 3, 0f).get(0, 5);
    }

    @Test
    public void testFill() {
        final SymmetricBandMatrix matrix = new SymmetricBandMatrix(3, 3, 0f);
        matrix.fill(5);
        // works, because fill() also adjusts the default value
        assertArrayEquals(new float[]{5, 5, 5}, matrix.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{5, 5, 5}, matrix.getRow(1), 0.000001f);
        assertArrayEquals(new float[]{5, 5, 5}, matrix.getRow(2), 0.000001f);
    }

    @Test
    public void testCopy() {
        final SymmetricBandMatrix matrix = new SymmetricBandMatrix(3, 5, 0f);

        matrix.copy(new float[]{5, 5, 5, 5, 5, 5, 5, 5, 5});

        assertArrayEquals(new float[]{5, 5, 5}, matrix.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{5, 5, 5}, matrix.getRow(1), 0.000001f);
        assertArrayEquals(new float[]{5, 5, 5}, matrix.getRow(2), 0.000001f);
    }

    @Test
    public void testToString() {
        final SymmetricBandMatrix matrix = new SymmetricBandMatrix(3, 5, 0f);
        assertEquals("SymmetricBandMatrix{bandwidth=5, length=3, offset=0, zeroPad=false}", matrix.toString());
    }

}
