/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TestUnsignedByteBackingBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestUnsignedByteBackingBuffer {

    @Test
    public void testReadWriteOne() {
        final UnsignedByteBackingBuffer buffer = new UnsignedByteBackingBuffer(false);
        buffer.allocate(1);
        buffer.set(0, 1f);
        final float f = buffer.get(0);
        assertEquals(1f, f, 0.00001f);
    }

    @Test
    public void testReadWriteZero() {
        final UnsignedByteBackingBuffer buffer = new UnsignedByteBackingBuffer(false);
        buffer.allocate(1);
        buffer.set(0, 0f);
        final float f = buffer.get(0);
        assertEquals(0f, f, 0.00001f);
    }

    @Test
    public void testReadWriteThreeQuarter() {
        final UnsignedByteBackingBuffer buffer = new UnsignedByteBackingBuffer(false);
        buffer.allocate(1);
        buffer.set(0, 0.75f);
        final float f = buffer.get(0);
        assertEquals(0.75f, f, 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWriteTooSmall() {
        final UnsignedByteBackingBuffer buffer = new UnsignedByteBackingBuffer(false);
        buffer.allocate(1);
        buffer.set(0, -0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWriteTooLarge() {
        final UnsignedByteBackingBuffer buffer = new UnsignedByteBackingBuffer(false);
        buffer.allocate(1);
        buffer.set(0, 1.1f);
    }

}
