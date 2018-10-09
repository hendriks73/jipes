/*
 * =================================================
 * Copyright 2018 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * TestSparseMatrix.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSparseMatrix {

    @Test
    public void testConstructor0() {
        final SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setRow(0, new float[]{1, 1});
        matrix.setRow(1, new float[]{2, 2});
        assertArrayEquals(new float[]{1, 1}, matrix.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{2, 2}, matrix.getRow(1), 0.000001f);
    }

    @Test
    public void testConstructor1() {
        final SparseMatrix matrix = new SparseMatrix(2, 2, true);

        matrix.set(0, 0, 1);
        assertEquals(1, matrix.get(0, 0), 0.00001f);
        assertEquals(0, matrix.get(1, 0), 0.00001f);

        assertEquals(2, matrix.getNumberOfColumns());
        assertEquals(2, matrix.getNumberOfRows());
    }

    @Test
    public void testFill() {
        final SparseMatrix matrix = new SparseMatrix(2, 2, true);
        matrix.fill(5);
        assertArrayEquals(new float[]{5, 5}, matrix.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{5, 5}, matrix.getRow(1), 0.000001f);
    }

    @Test
    public void testCopyFromMatrix() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2, true);
        final SparseMatrix matrixB = new SparseMatrix(2, 2, true);
        matrixB.fill(5);
        matrixA.copy(matrixB);
        assertArrayEquals(new float[]{5, 5}, matrixA.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{5, 5}, matrixA.getRow(1), 0.000001f);
    }

    @Test
    public void testToString() {
        final SparseMatrix matrix = new SparseMatrix(2, 2);
        assertEquals("SparseMatrix{rows=2, columns=2, zeroPadded=false}", matrix.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet() {
        final SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setRow(0, new float[]{1, 1});
        matrix.setRow(1, new float[]{2, 3});

        assertEquals(3, matrix.get(1, 1), 0.00001f);
        matrix.get(2, 1);
    }

    @Test
    public void testGetColumn() {
        final SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setRow(0, new float[]{1, 1});
        matrix.setRow(1, new float[]{2, 3});

        assertArrayEquals(new float[]{1, 2}, matrix.getColumn(0), 0.000001f);
        assertArrayEquals(new float[]{1, 3}, matrix.getColumn(1), 0.000001f);
    }

    @Test
    public void testGetRow() {
        final SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setRow(0, new float[]{1, 1});
        matrix.setRow(1, new float[]{2, 3});

        assertArrayEquals(new float[]{1, 1}, matrix.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{2, 3}, matrix.getRow(1), 0.000001f);
    }

    @Test
    public void testSetColumn() {
        final SparseMatrix matrix = new SparseMatrix(2, 2);
        matrix.setColumn(0, new float[]{1, 1});
        matrix.setColumn(1, new float[]{2, 3});

        assertArrayEquals(new float[]{1, 2}, matrix.getRow(0), 0.000001f);
        assertArrayEquals(new float[]{1, 3}, matrix.getRow(1), 0.000001f);
    }

    @Test
    public void testAdd() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final SparseMatrix matrixB = new SparseMatrix(2, 2);
        matrixB.setRow(0, new float[]{1, -1});
        matrixB.setRow(1, new float[]{3, 4});

        final Matrix result = matrixA.add(matrixB);

        assertArrayEquals(new float[] {2, 0}, result.getRow(0), 0.00001f);
        assertArrayEquals(new float[] {5, 6}, result.getRow(1), 0.00001f);
        assertEquals(2, result.getNumberOfColumns());
        assertEquals(2, result.getNumberOfRows());
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testSubtract() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final SparseMatrix matrixB = new SparseMatrix(2, 2);
        matrixB.setRow(0, new float[]{1, -1});
        matrixB.setRow(1, new float[]{3, 4});

        final Matrix result = matrixA.subtract(matrixB);

        assertArrayEquals(new float[] {0, 2}, result.getRow(0), 0.00001f);
        assertArrayEquals(new float[] {-1, -2}, result.getRow(1), 0.00001f);
        assertEquals(2, result.getNumberOfColumns());
        assertEquals(2, result.getNumberOfRows());
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testMultiply() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final SparseMatrix matrixB = new SparseMatrix(2, 2);
        matrixB.setRow(0, new float[]{1, -1});
        matrixB.setRow(1, new float[]{3, 4});

        final Matrix result = matrixA.multiply(matrixB);

        assertArrayEquals(new float[] {1*1 + 1*3, 1*-1 + 1*4}, result.getRow(0), 0.00001f);
        assertArrayEquals(new float[] {2*1 + 2*3, 2*-1 + 2*4}, result.getRow(1), 0.00001f);
        assertEquals(2, result.getNumberOfColumns());
        assertEquals(2, result.getNumberOfRows());
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testScalarMultiply() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final Matrix result = matrixA.multiply(2);

        assertArrayEquals(new float[] {2, 2}, result.getRow(0), 0.00001f);
        assertArrayEquals(new float[] {4, 4}, result.getRow(1), 0.00001f);
        assertEquals(2, result.getNumberOfColumns());
        assertEquals(2, result.getNumberOfRows());
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testHadamardMultiply() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final SparseMatrix matrixB = new SparseMatrix(2, 2);
        matrixB.setRow(0, new float[]{1, -1});
        matrixB.setRow(1, new float[]{3, 4});

        final Matrix result = matrixA.hadamardMultiply(matrixB);

        assertArrayEquals(new float[] {1*1, 1*-1}, result.getRow(0), 0.00001f);
        assertArrayEquals(new float[] {2*3, 2*4}, result.getRow(1), 0.00001f);
        assertEquals(2, result.getNumberOfColumns());
        assertEquals(2, result.getNumberOfRows());
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testEnlarge() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final SparseMatrix matrixB = new SparseMatrix(3, 3);
        matrixB.setRow(0, new float[]{1, -1, 5});
        matrixB.setRow(1, new float[]{3, 4, 5});
        matrixB.setRow(2, new float[]{5, 5, 5});

        final Matrix result = matrixA.enlarge(matrixB);

        assertArrayEquals(new float[] {1, 1, 5}, result.getRow(0), 0.00001f);
        assertArrayEquals(new float[] {2, 2, 5}, result.getRow(1), 0.00001f);
        assertArrayEquals(new float[] {5, 5, 5}, result.getRow(2), 0.00001f);
        assertEquals(3, result.getNumberOfColumns());
        assertEquals(3, result.getNumberOfRows());
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testEquals0() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final SparseMatrix matrixB = new SparseMatrix(2, 2);
        matrixB.setRow(0, new float[]{1, 1});
        matrixB.setRow(1, new float[]{2, 2});

        assertEquals(matrixA, matrixB);
        assertEquals(matrixA.hashCode(), matrixB.hashCode());
    }

    @Test
    public void testEquals1() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 3});

        final SparseMatrix matrixB = new SparseMatrix(2, 2);
        matrixB.setRow(0, new float[]{1, 1});
        matrixB.setRow(1, new float[]{2, 2});

        assertNotEquals(matrixA, matrixB);
    }

    @Test
    public void testTranspose() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final Matrix result = matrixA.transpose();

        assertArrayEquals(result.getRow(0), new float[] {1, 2}, 0.00001f);
        assertArrayEquals(result.getRow(1), new float[] {1, 2}, 0.00001f);
        assertFalse(result.isZeroPadded());
    }

    @Test
    public void testTranslate() {
        final SparseMatrix matrixA = new SparseMatrix(2, 2, true);
        matrixA.setRow(0, new float[]{1, 1});
        matrixA.setRow(1, new float[]{2, 2});

        final Matrix result = matrixA.translate(1, 2);

        assertEquals(3, result.getNumberOfRows());
        assertEquals(4, result.getNumberOfColumns());

        assertArrayEquals(result.getRow(0), new float[] {0, 0, 0, 0}, 0.00001f);
        assertArrayEquals(result.getRow(1), new float[] {0, 0, 1, 1}, 0.00001f);
        assertArrayEquals(result.getRow(2), new float[] {0, 0, 2, 2}, 0.00001f);
        assertTrue(result.isZeroPadded());
    }


    @Test
    public void testWriteAndRead() {
        final SparseMatrix matrix = new SparseMatrix(4, 5);
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
        new SparseMatrix(4, 5).get(-1, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsNegativeColumn() {
        new SparseMatrix(4, 5).get(0, -1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeRow() {
        new SparseMatrix(4, 5).get(4, 0);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsTooLargeColumn() {
        new SparseMatrix(4, 5).get(0, 5);
    }

}
