/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

/**
* Mutable {@link Matrix}.
*
* @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
*/
public interface MutableMatrix extends Matrix {

    /**
     * Sets the given value.
     *
     * @param row row
     * @param column column
     * @param value value
     * @throws IndexOutOfBoundsException row, column or both are out of bounds
     * @throws IllegalArgumentException if the given value cannot be stored in the matrix, e.g. because it is
     * not in the valid range
     */
    void set(final int row, final int column, final float value);


    /**
     * Copies values from another {@link Matrix} to this matrix.
     *
     * @param fromMatrix other matrix
     */
    void copy(Matrix fromMatrix);

    /**
     * Copies values from a defined offset from another matrix to a defined offset of this matrix.
     *
     * @param fromMatrix other matrix
     * @param fromRow row start offset of from matrix
     * @param fromColumn column start offset of from matrix
     * @param toRow column start offset of this matrix
     * @param toColumn column start offset of this matrix
     * @param rows number of rows to copy
     * @param columns number of column to copy
     */
    void copy(Matrix fromMatrix, int fromRow, int fromColumn,
              int toRow, int toColumn,
              int rows, int columns);
}
