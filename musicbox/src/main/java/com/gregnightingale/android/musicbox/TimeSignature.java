package com.gregnightingale.android.musicbox;

/**
 * Created by gregnightingale on 11/17/15.
 */
public class TimeSignature {
    protected int beatsPerBar;
    protected int noteValue;

    public TimeSignature(String signature) {
        set(signature);
    }

    private void set(String sig) {
        final String[] split = sig.split("/");
        setBeatsPerBar(Integer.valueOf(split[0]));
        setNoteValue(Integer.valueOf(split[1]));
        //TODO this will throw exception if xml is not right, decide what to do.
    }

    public int getBeatsPerBar() {
        return beatsPerBar;
    }

    public void setBeatsPerBar(final int beatsPerBar) {
        this.beatsPerBar = beatsPerBar;
    }

    public int getNoteValue() {
        return noteValue;
    }

    public void setNoteValue(final int noteValue) {
        this.noteValue = noteValue;
    }

}
