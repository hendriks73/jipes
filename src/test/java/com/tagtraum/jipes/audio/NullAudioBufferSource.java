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
class NullAudioBufferSource implements SignalSource<AudioBuffer> {

    public void reset() {
    }

    public AudioBuffer read() throws IOException {
        return null;
    }
}
