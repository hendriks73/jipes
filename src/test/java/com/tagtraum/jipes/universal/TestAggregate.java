/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.universal;

import com.tagtraum.jipes.math.AggregateFunction;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * TestAggregate.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestAggregate {

    @Test
    public void testConstructor0() {
        final AggregateFunction<float[], Float> function = new AggregateFunction<float[], Float>() {
            @Override
            public Float aggregate(final float[] collection) {
                return 1f;
            }
        };
        final Aggregate<float[], Float> aggregate = new Aggregate<float[], Float>(function, "id");

        assertEquals("id", aggregate.getId());
        assertEquals(function, aggregate.getAggregateFunction());
    }

    @Test
    public void testConstructor1() {
        final Aggregate<float[], Float> aggregate = new Aggregate<float[], Float>();

        assertNotNull(aggregate.getAggregateFunction());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() {
        new Aggregate<float[], Float>(null);
    }

    @Test
    public void testToString() {
        final AggregateFunction<float[], Float> function = new AggregateFunction<float[], Float>() {
            @Override
            public Float aggregate(final float[] collection) {
                return 1f;
            }

            @Override
            public String toString() {
                return "1";
            }
        };

        final Aggregate<float[], Float> aggregate = new Aggregate<float[], Float>(function);
        assertEquals("Aggregate{1}", aggregate.toString());
    }

    @Test
    public void testHashCode() {
        final AggregateFunction<float[], Float> function = new AggregateFunction<float[], Float>() {
            @Override
            public Float aggregate(final float[] collection) {
                return 1f;
            }
        };

        final Aggregate<float[], Float> aggregate = new Aggregate<float[], Float>(function);
        assertEquals(function.hashCode(), aggregate.hashCode());
    }

    @Test
    public void testEquals0() {
        final AggregateFunction<float[], Float> function = new AggregateFunction<float[], Float>() {
            @Override
            public Float aggregate(final float[] collection) {
                return 1f;
            }
        };
        final Aggregate<float[], Float> mapping0 = new Aggregate<float[], Float>(function, "id1");
        final Aggregate<float[], Float> mapping1 = new Aggregate<float[], Float>(function, "id2");
        // even though ids are different!!
        assertEquals(mapping0, mapping1);
    }

    @Test
    public void testEquals1() {
        final AggregateFunction<float[], Float> function = new AggregateFunction<float[], Float>() {
            @Override
            public Float aggregate(final float[] collection) {
                return 1f;
            }
        };
        final Aggregate<float[], Float> mapping0 = new Aggregate<float[], Float>(function, "id1");
        final Aggregate<float[], Float> mapping1 = new Aggregate<float[], Float>();
        // even though ids are different!!
        assertNotEquals(mapping0, mapping1);
    }

    @Test
    public void testProcess() throws IOException {
        final AggregateFunction<float[], Float> function = new AggregateFunction<float[], Float>() {
            @Override
            public Float aggregate(final float[] collection) {
                return 1f;
            }
        };
        final Aggregate<float[], Float> mapping = new Aggregate<float[], Float>(function, "id1");
        final Float result = mapping.processNext(new float[]{1f, 2f});
        assertEquals(1f, result.floatValue(), 0.00001f);
    }
}
