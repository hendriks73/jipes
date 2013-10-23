/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

/**
* Fully-backed full-float-precision {@link Matrix}.
*
* @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
*/
public class FullMatrix extends AbstractMatrix implements MutableMatrix, Cloneable {

    /**
     * Creates a fully backed matrix with the given number of rows and columns.
     * Memory is allocated from the Java heap. The matrix is <em>not</em> zero-padded.
     *
     * @param rows rows
     * @param columns columns
     */
    public FullMatrix(final int rows, final int columns) {
        this(rows, columns, new FloatBackingBuffer(false), false);
    }

    /**
     * Creates a fully backed matrix with the given number of rows and columns.
     * Memory is allocated through the given backing buffer. The matrix may be zero-padded.
     *
     * @param rows rows
     * @param columns columns
     * @param buffer backing buffer
     * @param zeroPadded shall the matrix be zero padded?
     */
    public FullMatrix(final int rows, final int columns, final MatrixBackingBuffer buffer, final boolean zeroPadded) {
        this(rows, columns, buffer, zeroPadded, true);
    }

    /**
     * Creates a fully backed matrix, filled with the values from the given matrix.
     * Memory is allocated through the given backing buffer. The matrix may be zero-padded.
     *
     * @param matrix matrix to copy
     * @param buffer backing buffer
     * @param zeroPadded shall the matrix be zero padded?
     */
    public FullMatrix(final Matrix matrix, final MatrixBackingBuffer buffer, final boolean zeroPadded) {
        this(matrix.getNumberOfRows(), matrix.getNumberOfColumns(), buffer, zeroPadded, true);
        copyValuesFrom(matrix);
    }

    /**
     * Creates a fully backed matrix with the given number of rows and columns.
     * Memory is allocated through a {@link FloatBackingBuffer}, either on the heap or natively.
     * The matrix may be zero-padded.
     *
     * @param rows rows
     * @param columns columns
     * @param direct shall the memory be allocated directly?
     * @param zeroPadded shall the matrix be zero padded?
     */
    public FullMatrix(final int rows, final int columns, final boolean direct, final boolean zeroPadded) {
        this(rows, columns, new FloatBackingBuffer(direct), zeroPadded);
    }

    protected FullMatrix(final int rows, final int columns, final MatrixBackingBuffer buffer, final boolean zeroPadded, final boolean allocate) {
        this.buffer = buffer;
        this.rows = rows;
        this.columns = columns;
        this.zeroPadded = zeroPadded;
        if (allocate) {
            buffer.allocate(rows*columns);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
