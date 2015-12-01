package com.gregnightingale.android.musicbox;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gregnightingale on 10/28/15.
 */
public class Sequence extends MusicalObject {

    private List<MusicalObject> tones = new ArrayList<>();

    private int ptr = 0;
    private Listener seqToneListener = new Listener() {
        @Override
        public void onStart(final MusicalObject obj) {

        }

        @Override
        public void onEnd(final MusicalObject tone) {
            tone.removeListener(this);
            if (isLast(tone)) {
                notifyEnd();
            } else {
                playToneAfter(tone);
            }
        }
    };

    public Sequence(MusicalContext context) {
        super(context);
    }

    public void add(MusicalObject obj) {
        tones.add(obj);
    }

    @Override
    public void play() { // play sequentially (synchronously)

        final MusicalObject tone = tones.get(ptr);
        tone.addListener(seqToneListener);
        notifyStart();
        tone.play();
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

    private boolean isLast(MusicalObject obj) {
        int indexOfLast = tones.indexOf(obj);
        return indexOfLast >= tones.size() - 1;
    }

    private void playToneAfter(MusicalObject obj) {
        int indexOfLast = tones.indexOf(obj);
        try {
            tones.get(indexOfLast + 1).play();
        } catch (Exception e) {
            Log.e(this.toString(), e.toString());
        }
    }
}
