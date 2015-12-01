package com.gregnightingale.android.musicbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Section extends MusicalObject {

    List<Bar> bars = new ArrayList<>();
    int barPtr = 0;
    Listener barListener = new Listener() {
        @Override
        public void onStart(final MusicalObject obj) {

        }

        @Override
        public void onEnd(final MusicalObject obj) {
            Bar bar = (Bar) obj;
            if (isMore()) {
                incrementPointer();
                play();
            } else {
                resetPtr();
                notifyEnd();
            }
            bar.removeListener(barListener);
        }
    };
    private String name;

    public Section(MusicalContext context, String name) {
        super(context);
        this.name = name;
    }

    public void add(Bar bar) {
        bars.add(bar);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void play() {
        if (barPtr == 0) {
            notifyStart();
        }
        Bar bar = bars.get(barPtr);
        bar.addListener(barListener);
        bar.play();
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

    private boolean isMore() {
        return bars.size() - 1 > barPtr;
    }

    private void resetPtr() {
        barPtr = 0;
    }

    private void incrementPointer() {
        barPtr++;
    }

}
