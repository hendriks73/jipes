<head><title>Notes</title></head>

Notes
-----

* 0.9.18

    * Replaced javazoom test dependency with
      [FFSampledSP](http://www.tagtraum.com/ffsampledsp/). 


* 0.9.17

    * Added sparse matrices.
    * Added matrix methods sum, rowSum, columnSum.
    * Faster implementation of 16 bit AudioSignalSources.
    * Faster implementation of 4th order IIR filters. 


* 0.9.16

    * Artifacts are now signed and deployed to Maven Central.


* 0.9.15

    * Added support for instantaneous frequency spectra.
    * Added createLinearBands() to MultiBandSpectrum.
    * Zero-padding processor.


* 0.9.14

    * Added support for not-power-of-two DCT.
    * Allow building Mel spectra from powers (not just magnitudes).
    * Fixed bug in MultiBandSpectrum.createMidiBands().
    * Added SignalPipeline.getProcessorWithClass()
    * Added methods to Floats (unbiased variance, corrected stddev, skewness, deltas, ...).


* 0.9.13

    * Downgraded maven site plugin back to version 3.3
    * More tests for Floats, additional methods.


* 0.9.12

    * Improved test coverage
    * Fixed Welch window
    * Fixed equals()/hashCode() in LinearFrequencySpectrum
    * Fixed bug in MultiBandSpectrum.createMidiBands()
    * Fixed bugs in Floats.interpolate()
    * Fixed FrameNumberFilter.read()
    * Updated Maven plugins


* 0.9.11

    * Added OLA (overlap-add) processor
    * Added IFFT (inverse FFT) processor
    * Added Channel processor
    * Added GriffinLim transform
    * Added ComplexAudioBuffer
    * Added bulk getRow and getColumn methods to Matrix
    * Added bulk setRow and setColumn methods to MutableMatrix
    * Fixed missing AudioFormat of InterleavedChannelJoin buffers
    * Fixed InterleavedChannelJoin to use real instead of data
    * Fixed mutability issue in AudioBufferFunctions.createMapFunction()
    * Renamed WindowFunctions to WindowFunction.
    * Avoid double-flushing by Join


* 0.9.10

    * Fixed potential NPE in AudioBuffer.getTimestamp() implementations
    * Fixed MultiBandSpectrum.getBin(freq)
    * Added DCT implementation
    * Renamed SignalSplit.connections(int) to getConnectedProcessors(int)
    * Java 8 style Javadocs.


* 0.9.9

    * Fixed concurrency issue with MapFunctions.createShortToOneNormalization()
    

* 0.9.8
    * Enhanced SelfSimilarity to better deal with long tracks (BandMatrix)
    * Increased SelfSimilarity performance
    * Added Matrix.enlarge(m)
    * Allow construction of FullMatrix from CSV file
    * Added Welch window function
    * Added ability to create MIDI-based frequency bands


* 0.9.7
    * Added close method to AudioSignalSource for better resource management
    * Added NoopSignalProcessor, Novelty, OnsetStrength, SelfSimilarity, AudioMatrix
    * Added mathematical classes for Matrix handling
    * Added no-op FIRFilter
    * Added TimestampLimitedSignalSource
    * Moved to Maven 3.0.5, JUnit 4.11
    * Added missing toString() methods
    * Changed meaning of parameters to Floats.arithmeticMean(float[], int, int)!
    * Switched docs format to Markdown
    * Added Javadocs links to site documentation
    * Added color-coding to code samples


* 0.9.6
    * Small performance optimizations.


* 0.9.5
    * Added support for 24bit audio in AudioSignalSource.
    * Fixed endianness issue in AudioSignalSource.
    * Normalization of float buffers now optional.


* 0.9.4
    * Added better resampling support (MultirateFilters).
    * Added InterleavedChannelJoin processor.
    * Fixed wrong framenumbers produced by the Downsample processor.


* 0.9.3
    * Fixed NPE in AudioSignalSource (Thx Joren!)


* 0.9.2
    * Added logo.
    * Some documentation improvements.


* 0.9.1
    * Fixed concurrency issue in normalization functions.


* 0.9.0
    * Initial release
