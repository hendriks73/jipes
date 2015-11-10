/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * TestFloatBackingBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFloatBackingBuffer {

    @Test
    public void testDirectBuffer() throws CloneNotSupportedException {
        final FloatBackingBuffer buffer = new FloatBackingBuffer(true);
        assertFalse(buffer.isAllocated());
        buffer.allocate(1024);
        assertTrue(buffer.isAllocated());
        assertEquals(0f, buffer.get(0), 0.001f);
        buffer.set(0, 5f);
        assertEquals(5f, buffer.get(0), 0.001f);
        final FloatBackingBuffer clone = (FloatBackingBuffer)buffer.clone();
        assertEquals(5f, clone.get(0), 0.001f);
    }

    @Test
    public void testArrayBuffer() throws CloneNotSupportedException {
        final FloatBackingBuffer buffer = new FloatBackingBuffer(false);
        assertFalse(buffer.isAllocated());
        buffer.allocate(1024);
        assertTrue(buffer.isAllocated());
        assertEquals(0f, buffer.get(0), 0.001f);
        buffer.set(0, 5f);
        assertEquals(5f, buffer.get(0), 0.001f);
        final FloatBackingBuffer clone = (FloatBackingBuffer)buffer.clone();
        assertEquals(5f, clone.get(0), 0.001f);
    }
}
