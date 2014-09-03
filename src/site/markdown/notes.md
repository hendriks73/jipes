<head><title>Notes</title></head>

Notes
-----

* 0.9.10

    * Fixed potential NPE in AudioBuffer.getTimestamp() implementations
    * Added DCT implementation


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
