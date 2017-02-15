/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.AbstractSignalProcessor;

import java.io.IOException;
import java.util.Arrays;

import static com.tagtraum.jipes.audio.MelSpectrum.channelBoundaries;
import static com.tagtraum.jipes.audio.MelSpectrum.createFilterBank;

/**
 * Assumes that the input is linear spectral data produced by some {@link com.tagtraum.jipes.math.Transform}
 * and sums up the powers into bins, which are spaced according to the
 * provided frequency boundaries.
 * <br>
 * You may choose between applying the internally created filterbank on the magnitudes or the
 * powers of the linear spectrum.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * @see MultiBandSpectrum#createLogarithmicBands(float, float, int)
 */
public class Mel<T extends LinearFrequencySpectrum> extends AbstractSignalProcessor<T, MelSpectrum> {

    private boolean filterPowers;
    private float[] channelBoundariesInHz;
    private float[][] filterBank;

    public Mel(final float lowerFrequency, final float upperFrequency, final int channels, final boolean filterPowers) {
        this(null, channelBoundaries(lowerFrequency, upperFrequency, channels+2), filterPowers);
    }

    protected Mel(final float[][] filterBank, final float[] channelBoundariesInHz, final boolean filterPowers) {
        this.channelBoundariesInHz = channelBoundariesInHz;
        this.filterBank = filterBank;
        this.filterPowers = filterPowers;
    }

    protected Mel() {
    }

    public float[] getBandBoundaries() {
        return channelBoundariesInHz;
    }

    public void setBandBoundaries(final float[] bandBoundaries) {
        this.channelBoundariesInHz = bandBoundaries;
    }

    public float[][] getFilterBank() {
        return filterBank;
    }

    public void setFilterBank(final float[][] filterBank) {
        this.filterBank = filterBank;
    }

    protected MelSpectrum processNext(final T audioSpectrum) throws IOException {
        if (channelBoundariesInHz == null) throw new IllegalStateException("No boundaries set.");
        if (filterBank == null) {
            filterBank = createFilterBank(audioSpectrum.getFrequencies(), channelBoundariesInHz);
        }
        if (audioSpectrum.getAudioFormat() != null && audioSpectrum.getAudioFormat().getChannels() != 1) {
            throw new IOException("Source must be mono.");
        }
        return new MelSpectrum(audioSpectrum.getFrameNumber(), audioSpectrum, filterBank, channelBoundariesInHz, filterPowers);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mel that = (Mel) o;

        if (!Arrays.equals(channelBoundariesInHz, that.channelBoundariesInHz)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return channelBoundariesInHz != null ? Arrays.hashCode(channelBoundariesInHz) : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (channelBoundariesInHz != null && channelBoundariesInHz.length>0) {
            for (final float boundary : channelBoundariesInHz) {
                sb.append(boundary).append(',');
            }
            if (sb.length()>0) sb.deleteCharAt(sb.length()-1);
        } else {
            sb.append("none");
        }
        return "Mel{" +
                "bandBoundaries=" + sb +
                '}';
    }
}
