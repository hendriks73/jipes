<head><title>Spectra</title></head>

## Spectra

Most of the time, just working with samples in the time domain does not get you very far. You need to
work in the frequency domain. Jipes offers built-in support for a regular discrete fast Fourier transform
(DFT or FFT) and a constant Q transform. It also comes with a couple of built-in filters.

### FFT

To apply an FFT to a signal, you need to specify somehow how long the FFT window is supposed to be. Jipes
lets you do this in multiple ways. The most useful one is probably to use a
[SlidingWindow](./apidocs/com/tagtraum/jipes/audio/SlidingWindow.html). That's
a processor that lets you say: I always want my signal in windows of *X* frames (window size) and I want those
frames to be *Y* frames apart (hop size).

Any following [FFT](./apidocs/com/tagtraum/jipes/audio/FFT.html) processor will then apply the FFT to the
whole window. In practice the code looks like this:

    fftPipe = new SignalPipeline<AudioBuffer, LinearFrequencySpectrum>(
        new Mono(),
        new SlidingWindow(windowLength, hopSize),
        new Mapping<AudioBuffer>(AudioBufferFunctions.createMapFunction(new WindowFunctions.Hamming(windowLength))),
        new FFT()
    );

This will produce a pipeline that takes stereo [AudioBuffers](./apidocs/com/tagtraum/jipes/audio/AudioBuffer.html),
applies a window, a [Hamming](http://en.wikipedia.org/wiki/Window_function#Hamming_window) function, and
transforms them into [LinearFrequencySpectrum](./apidocs/com/tagtraum/jipes/audio/LinearFrequencySpectrum.html) objects.
Those objects let you easily access magnitudes,
powers and frequencies. Keep in mind, that if you *pump* this pipeline, you will only get the *last* frame
as result in the result map. To actually process *all* frames, you will need to add another processor to
the pipeline after `new FFT()`.

Note that the default FFT implementation is written in pure Java. If you need a higher performance FFT, consider
implementing a native version (as it is done in [beaTunes](http://www.beatunes.com/)).
See the documentation of [FFTFactory](./apidocs/com/tagtraum/jipes/math/FFTFactory.html) for details on how to
register your optimized FFT.


### Constant Q Transform

Instead of a linear spectrum, a [constant Q transform](./apidocs/com/tagtraum/jipes/audio/ConstantQTransform.html)
produces a log frequency spectrum. This is especially useful,
when trying to extract pitch related information. The approach is pretty much the same as for the FFT.
The main difference is, that one needs to specify the frequency boundaries of the lowest and the frequency of the
highest bin. Additionally, the number of bins per octave is required.

    constantQPipe = new SignalPipeline<AudioBuffer, LogFrequencySpectrum>(
        new Mono(),
        new SlidingWindow(windowLength, hopSize),
        new Mapping<AudioBuffer>(AudioBufferFunctions.createMapFunction(new WindowFunctions.Hamming(windowLength))),
        new ConstantQTransform(25.956543f, 3520.0f, 12 * 3)
    );

The sample code shown above would split each octave into 36 bins (3 per semitone).
Since the built-in constant Q transform makes use of the FFT, it benefits from a fast
[FFTFactory](./apidocs/com/tagtraum/jipes/math/FFTFactory.html).

Again, keep in mind, that if you *pump* this pipeline, you will only get the *last* frame
as result in the result map. To process *all* frames, you will need to add another processor to
the pipeline after `new ConstantQTransform(...)`.

### Multiple Bands

Often one does neither need a log frequency spectrum, nor a linear spectrum, but rather one for a defined
set of bands, e.g. [bark](http://en.wikipedia.org/wiki/Bark_scale) bands. To create such a
[MultiBandSpectrum](./apidocs/com/tagtraum/jipes/audio/MultiBandSpectrum.html), just process the given
regular spectrum one step further:

    float[] bandBoundaries = new float[] {1000, 2000, 3000};
    multiBandPipe = new SignalPipeline<AudioBuffer, MultiBandSpectrum>(
        new Mono(),
        new SlidingWindow(windowLength, hopSize),
        new Mapping<AudioBuffer>(AudioBufferFunctions.createMapFunction(new WindowFunctions.Hamming(windowLength))),
        new FFT()
        new MultiBand(bandBoundaries)
    );

The configured [MultiBand](./apidocs/com/tagtraum/jipes/audio/MultiBand.html) reduces the FFT spectrum to two bands.
The first containing the powers between 1kHz and 2kHz, the second
containing the powers between 2kHz and 3kHz.

If now, for whatever reason, you want to process each band with a different pipeline, you can use a `BandSplit`
signal splitter (see [Split/Join](./split_join.html)).
