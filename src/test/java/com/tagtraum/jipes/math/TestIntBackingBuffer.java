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
 * TestIntBackingBuffer.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestIntBackingBuffer {

    @Test
    public void testDirectBuffer() throws CloneNotSupportedException {
        final IntBackingBuffer buffer = new IntBackingBuffer(true);
        assertFalse(buffer.isAllocated());
        buffer.allocate(1024);
        assertTrue(buffer.isAllocated());
        assertEquals(0f, buffer.get(0), 0.001f);
        buffer.set(0, 5);
        assertEquals(5f, buffer.get(0), 0.001f);
        final IntBackingBuffer clone = (IntBackingBuffer)buffer.clone();
        assertEquals(5f, clone.get(0), 0.001f);
    }

    @Test
    public void testArrayBuffer() throws CloneNotSupportedException {
        final IntBackingBuffer buffer = new IntBackingBuffer(false);
        assertFalse(buffer.isAllocated());
        buffer.allocate(1024);
        assertTrue(buffer.isAllocated());
        assertEquals(0f, buffer.get(0), 0.001f);
        buffer.set(0, 5f);
        assertEquals(5f, buffer.get(0), 0.001f);
        final IntBackingBuffer clone = (IntBackingBuffer)buffer.clone();
        assertEquals(5f, clone.get(0), 0.001f);
    }
}
