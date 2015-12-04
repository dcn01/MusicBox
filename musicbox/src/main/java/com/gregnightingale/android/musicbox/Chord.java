package com.gregnightingale.android.musicbox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Chord extends MusicalObject {

    private Set<Note> notes = new HashSet<>();
    private Map<Note, Boolean> hasFinished = new HashMap<>();

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
            n.play();
            notifyStart();
        }
    }

    @Override
    public void pause() {
        for (Note n : notes) {
            n.pause();
        }
        notifyPaused();
    }

    @Override
    public void resume() {
        for (Note n : notes) {
            n.resume();
        }
        notifyResumed();
    }

    @Override
    public void cancel() {
        for (Note n : notes) {
            n.cancel();
        }
        notifyCancelled();
    }

    private Listener listener = new Listener() {
        @Override
        void onEnd(final MusicalObject obj) {
            /* if all the notes in this chord have finished playing, then notify listeners
                that the chord has finished playing.  Mote that it is allowable for a chord
                to have notes of heterogeneous values (durations).
             */
            markFinished((Note) obj, true);
            if (allNotesHaveFinished()) {
                notifyEnd();
            }
        }
    };

    private void markFinished(Note finishedNote, boolean flag) {
        hasFinished.put(finishedNote, flag);
    }

    private boolean allNotesHaveFinished() {
        return !hasFinished.values().contains(Boolean.FALSE);
    }
}
