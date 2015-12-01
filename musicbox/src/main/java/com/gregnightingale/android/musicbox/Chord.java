package com.gregnightingale.android.musicbox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Chord extends MusicalObject {

    private final String tag = this.getClass().getSimpleName();
    private final boolean debug = false;
    private Set<Note> notes = new HashSet<>();
    private Map<Note, Boolean> hasFinished = new HashMap<>();
    private Listener listener = new Listener() {
        @Override
        public void onStart(final MusicalObject obj) {

        }

        @Override
        public void onEnd(final MusicalObject note) {
            note.removeListener(listener);
            markFinished((Note) note, true);
            if (isFinished()) {
                notifyEnd();
            }
        }
    };

    public Chord(MusicalContext context) {
        super(context);
    }

    public void add(Note note) {
        notes.add(note);
    }

    @Override
    public void play() { // play simultaneously (asynchronously)
        hasFinished.clear();
        for (Note n : notes) {
            markFinished(n, false);
            n.addListener(listener);
            notifyStart();
            n.play();
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

    private void markFinished(Note finishedNote, boolean flag) {
        hasFinished.put(finishedNote, flag);
    }

    private boolean isFinished() {
        return !hasFinished.values().contains(Boolean.FALSE);
    }
}
