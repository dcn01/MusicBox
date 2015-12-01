package com.gregnightingale.android.musicbox;

/**
 * Created by gregnightingale on 10/27/15.
 */
public class ToneData {

    public static final int sampleRate = 48000;
    public static final int MONO = 1;
    public static final int STEREO = 2;
    private int numSamples;
    private double frequencyOfTone;
    private int duration;
    private double sample[];
    private byte buffer[];

    /**
     * @param frequencyOfTone in Hz
     * @param duration        in milliseconds
     * @param channels        MONO | STEREO
     */
    public ToneData(double frequencyOfTone, int duration, int channels) {
        this.frequencyOfTone = frequencyOfTone;
        this.duration = duration;

        numSamples = calculateNumberOfSamples(duration);
        sample = new double[numSamples];
        buffer = new byte[calculateBufferSize(duration)];

        generateTone();
        applyRamp();
        generateSound();
        cleanup();
    }

    public static int calculateNumberOfSamples(int duration) {
        return duration * (sampleRate / 1000);
    }

    public static int calculateBufferSize(int duration) {
        return calculateNumberOfSamples(duration) * 2;
    }

    private void generateTone() {
        for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
            sample[i] = Math.sin(frequencyOfTone * 2.0 * Math.PI * (double) i / (double) sampleRate);
        }
    }

    private void applyRamp() {
        Ramp ramp = new SinusoidalRamp(0.2, duration);
        double[] rampFactors = ramp.getRamp();
        for (int i = 0; i < rampFactors.length; i++) {
            sample[i] *= rampFactors[i]; // front end ramp
            sample[sample.length - 1 - i] *= rampFactors[i]; // back end ramp
        }
    }

    private void generateSound() {
        int idx = 0;
        for (int i = 0; i < sample.length; i++) {
            double dVal = sample[i];
            final short val = (short) ((dVal * 32767));
            buffer[idx++] = (byte) (val & 0x00ff);
            buffer[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    private void cleanup() {
        sample = null;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    private interface Ramp {
        double[] getRamp();
    }

    private abstract class RampBase {

        private static final double highestFrequency = 10000;
        private static final double rampDuration = (1 / highestFrequency) / 4;
        private int numSamplesInRamp;
        private int minimumRampSamples; // a ramp any smaller than this will cause 'popping'
        private double[] ramp;

        public RampBase(double fraction, int duration) {
            //TODO validate fraction parameter, should never be greater than 1.0.
            minimumRampSamples = (int) Math.floor(rampDuration * ToneData.sampleRate);
            int requestedNumSamples = (int) Math.floor(fraction * duration * (ToneData.sampleRate / 1000));
            numSamplesInRamp = Math.max(requestedNumSamples, minimumRampSamples);
            ramp = new double[numSamplesInRamp];
            calculateRamp();
        }

        protected abstract void calculateRamp();

        public double[] getRamp() {
            return ramp;
        }
    }

    private class LinearRamp extends RampBase implements Ramp { // Strategy/Template Pattern

        public LinearRamp(double fraction, int duration) {
            super(fraction, duration);
        }

        @Override
        protected void calculateRamp() {
            for (int i = 0; i < super.numSamplesInRamp; i++) {
                double fraction = ((double) i) / super.numSamplesInRamp; // linear ramp
                super.ramp[i] = fraction;
            }
        }

    }

    private class SinusoidalRamp extends RampBase implements Ramp { // Strategy/Template Pattern

        public SinusoidalRamp(double fraction, int duration) {
            super(fraction, duration);
        }

        @Override
        protected void calculateRamp() {
            for (int i = 0; i < super.numSamplesInRamp; i++) {
                double rad = (double) i / super.numSamplesInRamp * Math.PI / 2.0; // sinusoidal ramp
                double fraction = Math.sin(rad);
                super.ramp[i] = fraction;
            }
        }
    }

    //TODO implement a nice quadratic ramp

}
