/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

/**
 * Matrix.
 * All mathematical operations are <em>lazy</em>&mdash;the resulting matrices
 * are only <em>views</em> still referencing their operands.
 * I.e., If the operands are mutable ({@link MutableMatrix}), they results change
 * dynamically.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public interface Matrix {

    /**
     * Gets the value from <code>row</code> and <code>column</code>.
     * If the matrix supports zero-padding, this method may return
     * <code>0f</code> for undefined rows or columns.
     *
     * @param row row
     * @param column column
     * @return value
     * @throws IndexOutOfBoundsException if row, column or both are out of bounds and the matrix does not
     * support zero padding
     */
    float get(final int row, final int column);

    /**
     * Number of rows
     *
     * @return rows
     */
    int getNumberOfRows();

    /**
     * Number of columns.
     *
     * @return columns
     */
    int getNumberOfColumns();

    /**
     * Indicates whether this matrix is zero padded.
     * I.e. access to undefined rows or columns leads to a return value 0f,
     * instead of an exception.
     *
     * @return true or false
     */
    boolean isZeroPadded();

    /**
     * Creates a <em>view</em> that is equal to the result of an addition.
     *
     * @param m matrix to add to this
     * @return add view
     */
    Matrix add(Matrix m);

    /**
     * Creates a <em>view</em> that is equal to the result of an subtraction.
     *
     * @param m matrix to subtract from this
     * @return subtract view
     */
    Matrix subtract(Matrix m);

    /**
     * Creates a <em>view</em> that is equal to the result of a scalar multiplication.
     *
     * @param f scalar
     * @return multiplication view
     */
    Matrix multiply(float f);

    /**
     * Creates a <em>view</em> that is equal to the result of a matrix multiplication.
     *
     * @param m matrix to multiply this with
     * @return multiplication view
     */
    Matrix multiply(Matrix m);

    /**
     * Creates a <em>view</em> that is equal to the result of a Hadamard multiplication.
     *
     * @param m matrix to multiply this with
     * @return multiplication view
     */
    Matrix hadamardMultiply(Matrix m);

    /**
     * Creates a <em>view</em> that is equal to the result of a matrix transposition.
     *
     * @return transposition view
     */
    Matrix transpose();

    /**
     * Creates a <em>view</em> that is equal to the result of a matrix translation.
     * I.e. the content of the matrix is moved to a different position in a larger matrix.
     *
     * @param rows number of rows to move the matrix
     * @param columns number of columns to move the matrix
     * @return transposition view
     */
    Matrix translate(int rows, int columns);

}
