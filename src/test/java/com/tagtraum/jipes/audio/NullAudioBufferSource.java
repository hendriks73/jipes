/*
 * =================================================
 * Copyright 2010 tagtraum industries incorporated
 * All rights reserved.
 * =================================================
 */
package com.tagtraum.jipes.audio;

import com.tagtraum.jipes.SignalSource;

import java.io.IOException;

/**
* NullAudioBufferSource.
*
* @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
*/
class NullAudioBufferSource<T> implements SignalSource<T> {

    public void reset() {
    }

    public T read() throws IOException {
        return null;
    }
}
