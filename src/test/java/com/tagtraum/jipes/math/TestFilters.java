/*
 * =================================================
 * Copyright 2015 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.math;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * TestFilters.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class TestFilters {

    @Test
    public void testBasicFIRFilter() {
        final Filters.FIRFilter filter = new Filters.FIRFilter();
        assertArrayEquals(new double[] {1.0}, filter.getCoefficients(), 0.0001);
        final float[] input = {1, 2, 1, 4};
        final float[] output = filter.map(input);
        assertArrayEquals(input, output, 0.00001f);
    }

    @Test
    public void testFIRFilterReset() {
        final Filters.FIRFilter filter = new Filters.FIRFilter(new double[] {1.0, 2.0});
        final float[] input = {1, 2, 1, 4};
        final float[] output0 = filter.map(input);
        final float[] output1 = filter.map(input);
        assertFalse(Arrays.equals(output0, output1));
        filter.reset();
        final float[] output2 = filter.map(input);
        assertArrayEquals(output0, output2, 0.00001f);
    }

    @Test
    public void testFIRFilterEqualsHashCode() {
        final Filters.FIRFilter filter0 = new Filters.FIRFilter(new double[] {1, 2});
        final Filters.FIRFilter filter1 = new Filters.FIRFilter(new double[] {1, 2});
        final Filters.FIRFilter filter2 = new Filters.FIRFilter(new double[] {1, 3});

        assertEquals(filter0, filter1);
        assertEquals(filter0.hashCode(), filter1.hashCode());
        assertNotEquals(filter0, filter2);
        assertNotEquals(filter0.hashCode(), filter2.hashCode());
    }

    @Test
    public void testFIRFilterToString() {
        final Filters.FIRFilter filter = new Filters.FIRFilter(new double[] {1, 2});
        assertEquals("FIRFilter{coefficients=[1.0, 2.0]}", filter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFIRFilterBadCoefficient() {
        new Filters.FIRFilter(new double[0]);
    }

    @Test
    public void testBasicIIRFilter() {
        final Filters.IIRFilter filter = new Filters.IIRFilter(new double[] {1.0, 1.0}, new double[] {1.0, 1.0});
        final float[] input = {1, 2, 1, 4};
        final float[] output = filter.map(input);
        assertArrayEquals(input, output, 0.00001f);
    }

    @Test
    public void testIIRFilterReset() {
        final Filters.IIRFilter filter = new Filters.IIRFilter(new double[] {1.0, 2.0}, new double[] {0.5, 0.9});
        final float[] input = {1, 2, 1, 4};
        final float[] output0 = filter.map(input).clone();
        final float[] output1 = filter.map(input).clone();
        assertFalse(Arrays.equals(output0, output1));
        filter.reset();
        final float[] output2 = filter.map(input);
        assertArrayEquals(output0, output2, 0.00001f);
    }

    @Test
    public void testIIRFilterEqualsHashCode() {
        final Filters.IIRFilter filter0 = new Filters.IIRFilter(new double[] {1, 2}, new double[] {1.0, 2.0});
        final Filters.IIRFilter filter1 = new Filters.IIRFilter(new double[] {1, 2}, new double[] {1.0, 2.0});
        final Filters.IIRFilter filter2 = new Filters.IIRFilter(new double[] {1, 3}, new double[] {1.0, 2.0});

        assertEquals(filter0, filter1);
        assertEquals(filter0.hashCode(), filter1.hashCode());
        assertNotEquals(filter0, filter2);
        assertNotEquals(filter0.hashCode(), filter2.hashCode());
    }

    @Test
    public void testIIRFilterToString() {
        final Filters.IIRFilter filter = new Filters.IIRFilter(new double[] {1, 2}, new double[] {3, 4});
        assertEquals("IIRFilter{order=2, inputCoefficients=[1.0, 2.0], outputCoefficients=[3.0, 4.0]}", filter.toString());
    }

    @Test
    public void testBasicFourthOrderIIRFilter() {
        final Filters.FourthOrderIIRFilter filter = new Filters.FourthOrderIIRFilter(new double[] {1.0, 1.0, 1.0, 1.0, 1.0}, new double[] {1.0, 1.0, 1.0, 1.0, 1.0});
        final float[] input = {1, 2, 1, 4};
        final float[] output = filter.map(input);
        assertArrayEquals(input, output, 0.00001f);
    }

    @Test
    public void testFourthOrderIIRFilterReset() {
        final Filters.FourthOrderIIRFilter filter = new Filters.FourthOrderIIRFilter(new double[] {1.0, 2.0, 3.0, 4.0, 5.0}, new double[] {0.5, 0.9, 0.1, 0.3, 0.1});
        final float[] input = {1, 2, 1, 4};
        final float[] output0 = filter.map(input).clone();
        final float[] output1 = filter.map(input).clone();
        assertFalse(Arrays.equals(output0, output1));
        filter.reset();
        final float[] output2 = filter.map(input);
        assertArrayEquals(output0, output2, 0.00001f);
    }

    @Test
    public void testFourthOrderIIRFilterToString() {
        final Filters.FourthOrderIIRFilter filter = new Filters.FourthOrderIIRFilter(new double[] {1, 2, 3, 4, 5}, new double[] {3, 4, 5, 6, 9});
        assertEquals("FourthOrderIIRFilter{b0=1.0, b1=2.0, b2=3.0, b3=4.0, b4=5.0, a0=3.0, a1=4.0, a2=5.0, a3=6.0, a4=9.0}", filter.toString());
    }

    @Test
    public void testCreateStaticFIRFilters() {
        Filters.createFir1_16thOrderLowpass(1);
        Filters.createFir1_16thOrderLowpass(2);
        Filters.createFir1_16thOrderLowpass(3);
        Filters.createFir1_16thOrderLowpass(4);
        Filters.createFir1_16thOrderLowpass(5);
        Filters.createFir1_16thOrderLowpass(7);
        Filters.createFir1_16thOrderLowpass(8);
        Filters.createFir1_16thOrderLowpass(160);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadStaticFIRFilters() {
        Filters.createFir1_16thOrderLowpass(6);
    }

    @Test
    public void testElliptic8thOrderLowpass() {
        Filters.createElliptic8thOrderLowpass(2);
        Filters.createElliptic8thOrderLowpass(4);
        Filters.createElliptic8thOrderLowpass(8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadElliptic8thOrderLowpass() {
        Filters.createElliptic8thOrderLowpass(5);
    }

    @Test
    public void testButterworth4thOrderLowpass() {
        Filters.createButterworth4thOrderLowpass(2);
        Filters.createButterworth4thOrderLowpass(4);
        Filters.createButterworth4thOrderLowpass(8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadButterworth4thOrderLowpass() {
        Filters.createButterworth4thOrderLowpass(5);
    }

    @Test
    public void testCreateStaticMIDIFilters() {
        Filters.createMidiFilterBank(44100, 60, 80);
        Filters.createMidiFilterBank(22050, 60, 80);
        Filters.createMidiFilterBank(11025, 60, 80);
        Filters.createMidiFilterBank(5512.5f, 60, 80);
        Filters.createMidiFilterBank(2756.25f, 60, 80);
        Filters.createMidiFilterBank(4410f, 60, 80);
        Filters.createMidiFilterBank(882, 60, 65);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadStaticMidiFilters1() {
        Filters.createMidiFilterBank(34, 60, 80);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadStaticMidiFilters2() {
        Filters.createMidiFilterBank(44100, 80, 60);
    }
}
