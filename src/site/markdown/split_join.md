<head><title>Split / Join</title></head>

## Split / Join

In many cases processing one band or one channel is absolutely sufficient. But if you're serious about DSP, you
will discover quickly that you need multiple bands for certain tasks. Even a simple
[ReplayGain](http://replaygain.org/) implementation will need to operate with two or more channels, as the spec
requires a specific way of dealing with multi-channel signals.


### Splitting Signals

The `AudioBuffer`s for a stereo file delivered by an `AudioSignalSource` come in a single pipeline.
But they (usually) contain an interleaved signal, i.e. samples are in the order LRLRLR (L=Left, R=Right).
You could simply process the buffers with the `Mono` processor, which transforms the signal into a single
channel. But what if you actually want to process the left channel separately from the right? Then you need
to *split* the signal into two separate channels and that means two different pipelines.

To do so, you can use the class [InterleavedChannelSplit](./apidocs/com/tagtraum/jipes/audio/InterleavedChannelSplit.html)
from the `audio` subpackage. Once you split the signal into multiple bands or channels, you can connect each band
to its own processing pipeline.

This is how it's done:

    SignalSplit<AudioBuffer, AudioBuffer> split = new InterleavedChannelSplit();

    SignalProcessor<AudioBuffer, AudioBuffer> fictitiousLeftProcessor = new FictitiousLeftProcessor("Left-ID");
    SignalProcessor<AudioBuffer, AudioBuffer> fictitiousRightProcessor = new FictitiousRightProcessor("Right-ID");

    split.connectTo(0, fictitiousLeftProcessor);
    split.connectTo(1, fictitiousRightProcessor);

    pump.add(split);
    Map<Object, Object> result = pump.pump();

As you can see from the code above, `InterleavedChannelSplit` implements a special interface
called [SignalSplit](./apidocs/com/tagtraum/jipes/SignalSplit.html).
It allows us to specify a channel when connecting it to a down stream processor.
Since a signal can be split in many ways (e.g. along frequency bands with
[BandSplit](apidocs/com/tagtraum/jipes/audio/BandSplit.html)), there isn't *one*
standard implementation for splitting. If you want to implement your own split, just implement the `SignalSplit`
interface and take advantage of the `SignalProcessorSupport` class.


### Joining Signals

Of course splitting is only the first part of the story. Signals also need to be joined.
Since joining is really just a special kind of aggregation, Jipes offers a special
[Join](./apidocs/com/tagtraum/jipes/universal/Join.html) processor, that
uses an `AggregateFunction` to combine the data handed to it by its parents into a single signal.
So to join the two sample channels from the split example, we proceed like this:

    Join<AudioBuffer, AudioBuffer> join
       = new Join<AudioBuffer, AudioBuffer>(2, new MagicAggregateFunction<List<AudioBuffer>, AudioBuffer>(), "ID");
    fictitiousLeftProcessor.connectTo(join);
    fictitiousRightProcessor.connectTo(join);

In this case (the fictitious) `MagicAggregateFunction` combines an `AudioBuffer` list of length 2
into a new, single `AudioBuffer`. We can collect the results under the id `"ID"`.

In order to join signals split by an `InterleavedChannelSplit` you may also simply use the provided
[InterleavedChannelJoin](./apidocs/com/tagtraum/jipes/audio/InterleavedChannelJoin.html).
