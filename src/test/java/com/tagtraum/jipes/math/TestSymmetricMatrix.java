/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * TestSymmetricMatrix.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSymmetricMatrix {

    @Test
    public void testWriteAndRead() {
        final SymmetricMatrix matrix = new SymmetricMatrix(5);
        final float[] values = new float[5*5];
        for (int i=0; i<values.length; i++) {
            values[i] = new Random().nextFloat();
        }
        for (int row=0; row<5; row++) {
            for (int column=row; column<5; column++) {
                matrix.set(row, column, values[row*5+column]);
            }
        }
        for (int row=0; row<5; row++) {
            for (int column=row; column<5; column++) {
                assertEquals(values[row * 5 + column], matrix.get(row, column), 0.0001f);
            }
        }

        for (int column=0; column<5; column++) {
            for (int row=column; row<5; row++) {
                assertEquals(values[column * 5 + row], matrix.get(row, column), 0.0001f);
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

    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeRow() {
        new SymmetricMatrix(5).get(-1, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeColumn() {
        new SymmetricMatrix(5).get(0, -1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeRow() {
        new SymmetricMatrix(5).get(5, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeColumn() {
        new SymmetricMatrix(5).get(0, 5);
    }
}
