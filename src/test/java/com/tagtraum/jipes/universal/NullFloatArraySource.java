/*
 * =================================================
 * Copyright 2011 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.universal;

import com.tagtraum.jipes.SignalSource;

import java.io.IOException;

/**
 * NullFloatArraySource.
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
class NullFloatArraySource implements SignalSource<float[]> {

    public float[] read() throws IOException {
        return null;
    }

    public void reset() {
    }
}
