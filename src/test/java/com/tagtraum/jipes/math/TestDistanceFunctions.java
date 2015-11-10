/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TestDistanceFunctions.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestDistanceFunctions {

    @Test
    public void testEuclideanDistance() {
        final DistanceFunction<float[]> function = DistanceFunctions.EUCLIDEAN_DISTANCE;
        final float[] a = {1, 2, 3, 4, 5};
        final float[] b = {5, 3, 3, 6, 1};
        final float distance = function.distance(a, b);
        assertEquals(Floats.euclideanDistance(a, b), distance, 0.000001f);
        assertEquals("EUCLIDEAN_DISTANCE", function.toString());
    }

    @Test
    public void testCityBlockDistance() {
        final DistanceFunction<float[]> function = DistanceFunctions.CITY_BLOCK_DISTANCE;
        final float[] a = {1, 2, 3, 4, 5};
        final float[] b = {5, 3, 3, 6, 1};
        final float distance = function.distance(a, b);
        assertEquals(Floats.cityBlockDistance(a, b), distance, 0.000001f);
        assertEquals("CITY_BLOCK_DISTANCE", function.toString());
    }

    @Test
    public void testCityBlockIncreaseDistance() {
        final DistanceFunction<float[]> function = DistanceFunctions.CITY_BLOCK_INCREASE_DISTANCE;
        final float[] a = {1, 2, 3, 4, 5};
        final float[] b = {5, 3, 3, 6, 1};
        final float distance = function.distance(a, b);
        assertEquals(Floats.cityBlockDistance(a, b, true), distance, 0.000001f);
        assertEquals("CITY_BLOCK_INCREASE_DISTANCE", function.toString());
    }

    @Test
    public void testCosineDistance() {
        final DistanceFunction<float[]> function = DistanceFunctions.COSINE_DISTANCE;
        final float[] a = {1, 2, 3, 4, 5};
        final float[] b = {5, 3, 3, 6, 1};
        final float distance = function.distance(a, b);
        assertEquals(Floats.cosineDistance(a, b), distance, 0.000001f);
        assertEquals("COSINE_DISTANCE", function.toString());
    }

    @Test
    public void testCosineSimilarity() {
        final DistanceFunction<float[]> function = DistanceFunctions.COSINE_SIMILARITY;
        final float[] a = {1, 2, 3, 4, 5};
        final float[] b = {5, 3, 3, 6, 1};
        final float distance = function.distance(a, b);
        assertEquals(Floats.cosineSimilarity(a, b), distance, 0.000001f);
        assertEquals("COSINE_SIMILARITY", function.toString());
    }

}
