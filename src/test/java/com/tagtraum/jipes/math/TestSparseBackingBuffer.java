/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TestSparseBackingBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestSparseBackingBuffer {

    @Test
    public void testBuffer() throws CloneNotSupportedException {
        final SparseBackingBuffer buffer = new SparseBackingBuffer();
        assertFalse(buffer.isAllocated());
        buffer.allocate(1024);
        assertTrue(buffer.isAllocated());
        assertEquals(0f, buffer.get(0), 0.001f);
        buffer.set(0, 5f);
        assertEquals(5f, buffer.get(0), 0.001f);
        final SparseBackingBuffer clone = (SparseBackingBuffer)buffer.clone();
        assertEquals(5f, clone.get(0), 0.001f);
    }

}
