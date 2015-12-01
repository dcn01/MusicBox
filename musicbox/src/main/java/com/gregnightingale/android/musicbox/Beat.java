package com.gregnightingale.android.musicbox;

import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Beat extends MusicalObject {

    private Set<MusicalObject> tones = new HashSet<>();
    private int beatIndex;

    public Beat(MusicalContext context) {
        super(context);
    }

    public void add(MusicalObject obj) {
        tones.add(obj);
    }

    @Override
    public void play() {
        for (MusicalObject mo : tones) {
            mo.play();
        }
        notifyStart();
        notifyWhenDone();
    }

    @Override
    public void pause() {
        for (MusicalObject mo : tones) {
            mo.pause();
        }
    }

    @Override
    public void resume() {
        for (MusicalObject mo : tones) {
            mo.resume();
        }
    }

    @Override
    public void cancel() {
        for (MusicalObject mo : tones) {
            mo.cancel();
        }
    }

    /**
     * notify observer when the duration of the beat has
     * elapsed (not necessarily when the notes are finished
     * playing.)
     */
    private void notifyWhenDone() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyEnd();
            }
        }, getDuration(getTempo()));
    }

    /**
     * @param tempo in beats per minute
     * @return the duration in ms of the beat
     */
    public static long getDuration(int tempo) {
        return (long) ((1.0 / tempo) * 60.0 * 1000.0);
    }

    public long getDuration() {
        return getDuration(context.tempo);
    }

    public int getBeatIndex() {
        return beatIndex;
    }

    public void setBeatIndex(int ndx) {
        this.beatIndex = ndx;
    }

    public boolean isLastBeatInBar() {
        return beatIndex == context.meter.beatsPerBar - 1;
    }

}
