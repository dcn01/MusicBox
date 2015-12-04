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
    private long beatDuration;
    private Handler handler;
    private BeatLauncherRunnable nextBeatLauncherRunnable;
    private Listener beatListener = new Listener() {
        @Override
        public void onStart(final MusicalObject obj) {
            currentBeat = (Beat) obj;
            scheduleNextBeat();
        }

        @Override
        public void onEnd(final MusicalObject obj) {
            Beat beat = (Beat) obj;
            if (beat.isLastBeatInBar()) {
                notifyEnd();
            }
            beat.removeListener(this);
        }

        @Override
        public void onPause(final MusicalObject obj) {
            notifyPaused();
        }

        @Override
        public void onResume(final MusicalObject obj) {
            //TODO
        }

        @Override
        public void onCancel(final MusicalObject obj) {
            //TODO
        }
    };

    public Bar(MusicalContext context) {
        super(context);
        beatDuration = Beat.getDuration(getTempo());
    }

    /**
     * add beats sequentially
     *
     * @param beat
     */
    public void addBeat(final Beat beat) {
        beat.setBeatIndex(beats.size());
        beat.addListener(beatListener);
        beats.add(beat);
    }

    public void addBeat(int index, Beat beat) {
        beat.setBeatIndex(index);
        beat.addListener(beatListener);
        beats.add(index, beat);
    }

    @Override
    public void play() {
        if (beats.size() == 0) return;
        notifyStart();
        handler = new Handler();
        beats.get(0).play();
    }

    @Override
    public void pause() {
        /* pausing mid-beat is not supported, pausing will simply resume at the next beat. */
        currentBeat.cancel();
        handler.removeCallbacks(nextBeatLauncherRunnable);
    }

    @Override
    public void resume() {
        if (isLastBeat(currentBeat)) {
            notifyEnd();
        } else {
            playNextBeatNow();
        }
    }

    private long beatDelay(final int index) {
        return index * beatDuration;
    }

    @Override
    public void cancel() {
        currentBeat = null;
        currentBeat.cancel();
        handler.removeCallbacks(nextBeatLauncherRunnable);
    }

    private void scheduleNextBeat() {
        if (!isLastBeat(currentBeat)) {
            final int nextIndex = currentBeat.getBeatIndex() + 1;
            nextBeatLauncherRunnable = new BeatLauncherRunnable(nextIndex);
            handler.postDelayed(nextBeatLauncherRunnable, beatDuration);
        }
    }

    private void playNextBeatNow() {
        if (!isLastBeat(currentBeat)) {
            final int nextIndex = currentBeat.getBeatIndex() + 1;
            nextBeatLauncherRunnable = new BeatLauncherRunnable(nextIndex);
            handler.post(nextBeatLauncherRunnable);
        }
    }

    private boolean isLastBeat(final Beat beat) {
        return isLastBeat(beat.getBeatIndex());
    }

    private boolean isLastBeat(final int beatIndex) {
        return beatIndex == context.meter.beatsPerBar - 1;
    }

    public Beat getCurrentBeat() {
        return currentBeat;
    }

    class BeatLauncherRunnable implements Runnable {
        final Beat beat;

        public BeatLauncherRunnable(int beatNdx) {
            beat = beats.get(beatNdx);
        }

        @Override
        public void run() {
            beat.play();
        }
    }

}
