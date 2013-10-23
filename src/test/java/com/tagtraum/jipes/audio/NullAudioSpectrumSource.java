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
* NullAudioSpectrumSource.
*
* @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
*/
class NullAudioSpectrumSource implements SignalSource<AudioSpectrum> {

    public void reset() {
    }

    public AudioSpectrum read() throws IOException {
        return null;
    }
}
