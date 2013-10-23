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
}
