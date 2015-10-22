<head><title>Introduction</title></head>


## Introduction

Jipes is an open source library that allows you to efficiently compute audio features. Possible uses for these
features are general music information retrieval ([MIR](http://en.wikipedia.org/wiki/Music_information_retrieval))
applications or more specifically personal music software like [beaTunes](http://www.beatunes.com).

Unlike many other digital signal processing (DSP) libraries or frameworks, Jipes is not meant for real time
processing of a single audio stream that is manipulated and eventually played back. Instead, it focuses on
efficiently executing multiple processing pipelines that transform a signal into a feature or feature set. While
doing so, Jipes attempts to avoid duplication of work, by applying some simple optimizations. For example, if two
pipelines each require the same audio data, a certain window size, application of a Hann window and the
transformation of the signal via FFT, Jipes will perform these processing steps exactly once. Only when processing
steps are different or too complex to optimize, two different steps are executed.

Since Jipes focuses on features instead of the raw, untyped signal streams; it supports rich types to be used,
where other frameworks only offer arrays of raw data. Also, by using Java generics, many core interfaces and classes
can be typed to whatever class you see fit for the purpose. That does not mean that Jipes comes without any useful
pre-defined types or support for raw arrays. Classes for both simple audio buffers and different kind of spectra
can be found in the *audio* specific sub-package of the library, while many useful functions for processing data
arrays are offered in the *math* sub-package.

Even though Jipes is written in and for Java 6, an object-oriented language, it borrows some functional concepts.
One of the key interfaces in Jipes is that of the [MapFunction](apidocs/com/tagtraum/jipes/math/MapFunction.html).
It does what you might suspect - it maps some
sort of data into other data of the same type. This principle is useful for example for a simple filter or mapping
of a pitch vector to a chroma feature. Other functions let you aggregate data or compute the distance between two
objects. Most of these functions can be defined for simple float arrays and then wrapped by classes appropriate for
richer level objects like audio buffers.

So rather than writing horrendous spaghetti code, Jipes fosters re-use by encouraging the programmer to code
solutions in functions. And if it is not possible to create a simple function for the desired purpose, one can
still create a new signal processor-the next higher level abstraction.

In Jipes, signal processors connected to each other, form so called pipelines. To support abstract and better
handling of long processor chains, these pipelines aren't just called pipelines, there is also a real pipeline
class. It lets you create pipelines you can handle just like a regular, single signal processor
([Composite pattern](http://en.wikipedia.org/wiki/Composite_pattern)). In the end, features are nothing else
but the product of a pipeline.

[Let's get started!](./getting_started.html)


### License

Jipes is licensed under [LGPL](./license.html).

### Other DSP projects

Other Java DSP projects worth mentioning are:

* [TarsosDSP](https://github.com/JorenSix/TarsosDSP)
* [jAudio](http://jaudio.sourceforge.net/)

Significant Non-Java DSP projects are:

* Matlab: [MIRToolbox](https://www.jyu.fi/hum/laitokset/musiikki/en/research/coe/materials/mirtoolbox)
* C++: [Marsyas](http://marsyas.info/)
* C++: [Essentia](http://essentia.upf.edu)
* C/C++: [Vamp](http://vamp-plugins.org/)

There are certainly more frameworks. Feel free to contact me, if you want your framework included in this list.


<a href="https://github.com/hendriks73/jipes"><img style="position: absolute; top: 0; left: 0; border: 0;" src="https://s3.amazonaws.com/github/ribbons/forkme_left_gray_6d6d6d.png" alt="Fork me on GitHub" /></a>
