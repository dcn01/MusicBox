package com.gregnightingale.android.musicbox;

import android.os.Handler;

import java.util.HashSet;
import java.util.Set;
import android.os.SystemClock;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Beat extends MusicalObject {

    private Set<MusicalObject> tones = new HashSet<>();
    private int beatIndex;
    private long beatTimeRemaining;
    private long timeMarker;
    private long beatDuration;
    private Handler handler;

    public Beat(MusicalContext context) {
        super(context);
        beatDuration = Beat.getDuration(getTempo());
    }

    public void add(MusicalObject obj) {
        tones.add(obj);
    }

    @Override
    public void play() {
        handler = new Handler();
        setTimeMarker();
        for (MusicalObject mo : tones) {
            mo.play();
        }
        beatTimeRemaining = beatDuration;
        handler.postAtTime(beatEndRunnable, SystemClock.uptimeMillis() + beatTimeRemaining);
        notifyStart();
    }

    @Override
    public void pause() {
        for (MusicalObject mo : tones) {
            mo.pause();
        }
        handler.removeCallbacks(beatEndRunnable);
        adjustBeatTimeRemaining();
        notifyPaused();
    }

    @Override
    public void resume() {
        adjustBeatTimeRemaining();
        setTimeMarker();
        for (MusicalObject mo : tones) {
            mo.resume();
        }
        handler.postAtTime(beatEndRunnable, SystemClock.uptimeMillis() + beatTimeRemaining);
        notifyResumed();
    }

    @Override
    public void cancel() {
        for (MusicalObject mo : tones) {
            mo.cancel();
        }
        handler.removeCallbacks(beatEndRunnable);
        notifyCancelled();
        handler = null;
    }

    private void setTimeMarker() {
        timeMarker = android.os.SystemClock.uptimeMillis();
    }

    private void adjustBeatTimeRemaining() {
        beatTimeRemaining = beatTimeRemaining - (SystemClock.uptimeMillis() - timeMarker);
        if (beatTimeRemaining < 0) {
            beatTimeRemaining = 0;
        }
    }

    private Runnable beatEndRunnable = new Runnable() {
        @Override
        public void run() {
            notifyEnd();
        }
    };

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
