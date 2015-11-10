/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.universal;

import com.tagtraum.jipes.SignalProcessor;
import com.tagtraum.jipes.math.AggregateFunction;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * TestJoin.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestJoin {

    @Test
    public void testConstructor0() {
        final AggregateFunction<List<Float>, Float> function = new AggregateFunction<List<Float>, Float>() {
            @Override
            public Float aggregate(final List<Float> collection) {
                return 5f;
            }
        };
        final Join<Float, Float> join = new Join<Float, Float>(2, function, "id");

        assertEquals("id", join.getId());
        assertEquals(2, join.getPartsPerUnit());

        System.out.println(join.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1() {
        final AggregateFunction<List<Float>, Float> function = new AggregateFunction<List<Float>, Float>() {
            @Override
            public Float aggregate(final List<Float> collection) {
                return 5f;
            }
        };
        new Join<Float, Float>(-1, function, "id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() {
        new Join<Float, Float>(1, null, "id");
    }

    @Test
    public void testProcessOnePartPerUnit() throws IOException {
        final AggregateFunction<List<Float>, Float> function = new AggregateFunction<List<Float>, Float>() {
            @Override
            public Float aggregate(final List<Float> collection) {
                return 5f;
            }
        };
        final Join<Float, Float> join = new Join<Float, Float>(1, function);
        final SignalProcessor<Float, Float> next = mock(SignalProcessor.class);
        join.connectTo(next);
        join.process(5f);
        assertEquals(5f, join.getOutput().floatValue(), 0.0001f);
        verify(next).process(5f);
        join.flush();
        verify(next).flush();
    }

    @Test
    public void testProcessTwoPartsPerUnit() throws IOException {
        final AggregateFunction<List<Float>, Float> function = new AggregateFunction<List<Float>, Float>() {
            @Override
            public Float aggregate(final List<Float> collection) {
                return 5f;
            }
        };
        final Join<Float, Float> join = new Join<Float, Float>(2, function);
        final SignalProcessor<Float, Float> next = mock(SignalProcessor.class);
        join.connectTo(next);
        join.process(5f);
        join.flush();
        verify(next, never()).process(5f);
        verify(next, never()).flush();
        join.process(5f);
        join.flush();
        verify(next).process(5f);
        verify(next).flush();
        join.flush();
        verify(next).flush();
    }

    @Test
    public void testHashCode() {
        final AggregateFunction<List<Float>, Float> function = new AggregateFunction<List<Float>, Float>() {
            @Override
            public Float aggregate(final List<Float> collection) {
                return 5f;
            }
        };

        final Join<Float, Float> join0 = new Join<Float, Float>(2, function);
        final Join<Float, Float> join1 = new Join<Float, Float>(2, function);
        assertEquals(join0.hashCode(), join1.hashCode());
    }

    @Test
    public void testEquals0() {
        final AggregateFunction<List<Float>, Float> function = new AggregateFunction<List<Float>, Float>() {
            @Override
            public Float aggregate(final List<Float> collection) {
                return 5f;
            }
        };
        final Join<Float, Float> join0 = new Join<Float, Float>(2, function, "id1");
        final Join<Float, Float> join1 = new Join<Float, Float>(2, function, "id2");
        // even though ids are different!!
        assertEquals(join0, join1);
    }

}
