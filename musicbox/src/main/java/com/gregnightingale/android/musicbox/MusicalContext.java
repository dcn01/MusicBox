package com.gregnightingale.android.musicbox;

/**
 * Created by gregnightingale on 11/17/15.
 */
public class MusicalContext {
    protected int tempo;  // beats per minute
    protected TimeSignature meter;

    public MusicalContext setMeter(final TimeSignature meter) {
        this.meter = meter;
        return this;
    }

    public MusicalContext setTempo(final int tempo) {
        this.tempo = tempo;
        return this;
    }
}
