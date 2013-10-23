/*
 * =================================================
 * Copyright 2013 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalProcessor;
import com.tagtraum.jipes.SignalProcessorSupport;
import com.tagtraum.jipes.math.DistanceFunction;
import com.tagtraum.jipes.math.DistanceFunctions;
import com.tagtraum.jipes.math.MutableMatrix;
import com.tagtraum.jipes.math.SymmetricMatrix;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tagtraum.jipes.audio.AudioBufferFunctions.createDistanceFunction;

/**
 * Self similarity processor computes a self-similarity matrix from {@link AudioBuffer} features.
 * The actual value is produced on {@link #flush()}.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class SelfSimilarity<I extends AudioBuffer> implements SignalProcessor<I, AudioMatrix> {

    private SignalProcessorSupport<AudioMatrix> signalProcessorSupport = new SignalProcessorSupport<AudioMatrix>();
    private List<I> spectra = new ArrayList<I>();
    private AudioFormat audioFormat;
    private DistanceFunction<I> distanceFunction = createDistanceFunction(DistanceFunctions.createCosineDistanceFunction());
    private AudioMatrix matrix;
    private int firstFrame = -1;
    private int secondFrame = -1;
    private Object id;

    /**
     * Self similarity processor.
     *
     * @param id               id
     * @param distanceFunction distance function
     */
    public SelfSimilarity(final Object id, final DistanceFunction<I> distanceFunction) {
        setId(id);
        setDistanceFunction(distanceFunction);
    }

    /**
     * Self similarity processor.
     *
     * @param id               id
     */
    public SelfSimilarity(final Object id) {
        setId(id);
    }

    /**
     * Self similarity processor.
     */
    public SelfSimilarity() {
        setId("SelfSimilarity");
    }

    public void setId(final Object id) {
        this.id = id;
    }

    /**
     * Sets the distance function used to compute the self similarity matrix.
     *
     * @param distanceFunction distance function
     * @see DistanceFunctions#createCosineDistanceFunction()
     */
    public void setDistanceFunction(final DistanceFunction<I> distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    @Override
    public void process(final I input) throws IOException {
        try {
            spectra.add((I)input.clone());
            if (firstFrame < 0) firstFrame = input.getFrameNumber();
            else if (secondFrame < 0) secondFrame = input.getFrameNumber();
            else if (audioFormat == null) deriveOutputFormat(input.getAudioFormat());
        } catch (CloneNotSupportedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void flush() throws IOException {
        final int length = spectra.size();
        final MutableMatrix distances = createMatrix(length);
        for (int row=0; row<length; row++) {
            for (int column=row+1; column<length; column++) {
                final float d = distanceFunction.distance(spectra.get(row), spectra.get(column));
                distances.set(row, column, d);
            }
        }
        this.matrix = new RealAudioMatrix(firstFrame, distances, audioFormat);
        this.signalProcessorSupport.process(matrix);
        this.signalProcessorSupport.flush();
    }

    /**
     * By default this creates a symmetric float-backed {@link com.tagtraum.jipes.math.Matrix}.
     * You may override this method to use a different matrix implementation,
     * e.g. one that is backed by an {@link com.tagtraum.jipes.math.UnsignedByteBackingBuffer}
     * to preserve memory.
     *
     * @param length length
     * @return mutable matrix
     */
    protected MutableMatrix createMatrix(final int length) {
        return new SymmetricMatrix(length, false, false);
    }

    private void deriveOutputFormat(final AudioFormat inputFormat) {
        final int frameDiff = secondFrame - firstFrame;
        final float sampleRate = inputFormat.getSampleRate() / frameDiff;
        audioFormat = new AudioFormat(
                inputFormat.getEncoding(), sampleRate, inputFormat.getSampleSizeInBits(),
                inputFormat.getChannels(), inputFormat.getFrameSize(), sampleRate,
                inputFormat.isBigEndian());
    }


    @Override
    public AudioMatrix getOutput() throws IOException {
        return matrix;
    }

    @Override
    public Object getId() {
        return id == null ? toString() : id;
    }

    @Override
    public <O2> SignalProcessor<AudioMatrix, O2> disconnectFrom(final SignalProcessor<AudioMatrix, O2> signalProcessor) {
        return signalProcessorSupport.disconnectFrom(signalProcessor);
    }

    @Override
    public SignalProcessor<AudioMatrix, ?>[] getConnectedProcessors() {
        return signalProcessorSupport.getConnectedProcessors();
    }

    @Override
    public <O2> SignalProcessor<AudioMatrix, O2> connectTo(final SignalProcessor<AudioMatrix, O2> signalProcessor) {
        return signalProcessorSupport.connectTo(signalProcessor);
    }

}
