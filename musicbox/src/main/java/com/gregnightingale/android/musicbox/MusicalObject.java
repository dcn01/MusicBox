package com.gregnightingale.android.musicbox;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by gregnightingale on 11/3/15.
 */
abstract public class MusicalObject {

    protected MusicalContext context;
    private List<Listener> listenerList = new CopyOnWriteArrayList<>();

    public MusicalObject(MusicalContext context) {
        this.context = context;
    }

    public int getTempo() {
        return context.tempo;
    }

    public void setTempo(final int beatsPerMinute) {
        context.tempo = beatsPerMinute;
    }

    abstract public void play();

    abstract public void pause();

    abstract public void resume();

    abstract public void cancel();

    public void addListener(final Listener listener) {
        listenerList.add(listener);
    }

    public void removeListener(Listener listener) {
        listenerList.remove(listener);
    }

    protected void notifyStart() {
        for (Iterator<Listener> i = listenerList.iterator(); i.hasNext(); ) {
            Listener listener = i.next();
            try {
                listener.onStart(this);
            } catch (RuntimeException e) {
                e.printStackTrace();
                i.remove();
            }
        }
    }

    protected void notifyEnd() {
        for (Iterator<Listener> i = listenerList.iterator(); i.hasNext(); ) {
            Listener listener = i.next();
            try {
                listener.onEnd(this);
            } catch (RuntimeException e) {
                e.printStackTrace();
                i.remove();
            }
        }
    }

    protected void notifyPaused() {
        for (Iterator<Listener> i = listenerList.iterator(); i.hasNext(); ) {
            Listener listener = i.next();
            try {
                listener.onPause(this);
            } catch (RuntimeException e) {
                e.printStackTrace();
                i.remove();
            }
        }
    }

    protected void notifyResumed() {
        for (Iterator<Listener> i = listenerList.iterator(); i.hasNext(); ) {
            Listener listener = i.next();
            try {
                listener.onResume(this);
            } catch (RuntimeException e) {
                e.printStackTrace();
                i.remove();
            }
        }
    }

    protected void notifyCancelled() {
        for (Iterator<Listener> i = listenerList.iterator(); i.hasNext(); ) {
            Listener listener = i.next();
            try {
                listener.onCancel(this);
            } catch (RuntimeException e) {
                e.printStackTrace();
                i.remove();
            }
        }
    }

}
