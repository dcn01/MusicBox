package com.gregnightingale.android.musicbox;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Bar extends MusicalObject {

    private List<Beat> beats = new ArrayList<>();
    private Beat currentBeat;

    private Listener beatListener = new Listener() {
        @Override
        public void onStart(final MusicalObject obj) {
            currentBeat = (Beat) obj;
        }

        @Override
        public void onEnd(final MusicalObject obj) {
            Beat beat = (Beat) obj;
            if (beat.isLastBeatInBar()) {
                notifyEnd();
            }
            beat.removeListener(this);
        }
    };

    public Bar(MusicalContext context) {
        super(context);
    }

    /**
     * add beats sequentially
     *
     * @param beat
     */
    public void addBeat(Beat beat) {
        beat.setBeatIndex(beats.size());
        beat.addListener(beatListener);
        beats.add(beat);
    }

    @Override
    public void play() {
        Handler handler = new Handler();
        notifyStart();

        long beatDuration = Beat.getDuration(getTempo());
        class LaunchBeat implements Runnable {
            final Beat beat;

            public LaunchBeat(int beatNdx) {
                beat = beats.get(beatNdx);
            }

            @Override
            public void run() {
                beat.play();
            }
        }
        // launch the beats at the appropriate time in the future by using postDelayed()
        for (int beatIndex = 0; beatIndex < beats.size(); beatIndex++) {
            handler.postDelayed(new LaunchBeat(beatIndex), beatIndex * beatDuration);
        }

    }

    @Override
    public void pause() {
        //TODO implement pause
    }

    @Override
    public void resume() {
        //TODO implement resume
    }

    @Override
    public void cancel() {
        //TODO implement cancel
    }

    public Beat getCurrentBeat() {
        return currentBeat;
    }

    private boolean isLastBeat(int beatIndex) {
        return beatIndex == context.meter.beatsPerBar - 1;
    }

}
