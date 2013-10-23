<head><title>Defer Data Flow</title></head>

##Deferring Data Flow

Sometimes a processor first needs to collect some data before it can emit results. This means, it has to defer
the flow of data. Since most of these cases underlie a very specific custom logic, Jipes does not have a processor
superclass you can simply inherit from for this purpose. Instead you should implement the
[SignalProcessor](./apidocs/com/tagtraum/jipes/SignalProcessor.html) interface yourself.

To do so is actually not hard. As an example we will implement the zero crossing rate from the
[Getting Started](./getting_started.html) chapter in a slightly different way. Instead of always emitting the
*current* zero crossing rate, our processor will emit it only once at the very end of the pumping process, when
the `flush()` method is called.

While pumping, the pump repeatedly called the `process()` method of its registered pipelines. Since our
processor is going to be part of a pipeline, that's where we need to do our calculations. But unlike in an
ordinary processor, we do not call our children's `process()` methods. Instead we wait until our `flush()`
method is called. Only then we propagate the last zero crossing rate to the kids and then call their
`flush()` method.

Here's the complete implementation:

    public class ZeroCrossingRate implements SignalProcessor<AudioBuffer, Float> {
    
        private SignalProcessorSupport<Float> signalProcessorSupport = new SignalProcessorSupport<Float>();
        private int samples;
        private int crossings;
        private float lastSample;
        private Float rate;
        private Object id;
    
        public ZeroCrossingRate(final Object id) {
            this.id = id;
        }
    
        public void process(final AudioBuffer audioBuffer) throws IOException {
            final float[] data = audioBuffer.getData();
            samples += data.length;
            for (final float sample : data) {
                crossings += lastSample * sample >= 0 ? 0 : 1;
                lastSample = sample;
            }
            // at this point we *don't* call our kids process' method
        }
    
        public void flush() throws IOException {
            if (samples > 0) {
                rate = crossings / (float) samples;
                crossings = 0;
                samples = 0;
                // now that we have the result for the entire source, we call our kids
                signalProcessorSupport.process(rate);
            }
            signalProcessorSupport.flush();
        }
    
        public Float getOutput() throws IOException {
            return rate;
        }
    
        public Object getId() {
            return id;
        }
    
        public <O> SignalProcessor<Float, O> connectTo(final SignalProcessor<Float, O> processor) {
            return signalProcessorSupport.connectTo(processor);
        }
    
        public <O> SignalProcessor<Float, O> disconnectFrom(final SignalProcessor<Float, O> processor) {
            return signalProcessorSupport.disconnectFrom(processor);
        }
    
        public SignalProcessor<Float, ?>[] getConnectedProcessors() {
            return signalProcessorSupport.getConnectedProcessors();
        }
    }

Note how the [SignalProcessorSupport](apidocs/com/tagtraum/jipes/SignalProcessorSupport.html)
class takes care of the simple and annoying householding tasks, like
connecting and disconnecting other processors. You really only need to fill out the interface, add your
own data flow logic and you're done!

Obviously, calling your children's `process()` method only on `flush()` is only one option. You
could also call it on every 42nd `process()` call you receive yourself.

The only thing you need to understand is, that your `process()` method is called by your parent processor as
it sees fit and you in turn can call your children's `process()` method as you see fit. The same is true
for the `flush()` method.


