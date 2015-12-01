package com.gregnightingale.android.musicbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class Music extends MusicalObject {

    private final String tag = this.getClass().getSimpleName();
    Map<String, Section> sections = new HashMap<>(); // map sections by name
    List<Section> score = new ArrayList<>(); // the order in which to play the sections
    boolean loop = false;
    int sectionPtr = 0;
    private Listener sectionListener = new Listener() {
        @Override
        public void onStart(final MusicalObject obj) {

        }

        @Override
        public void onEnd(final MusicalObject obj) {
            Section section = (Section) obj;
            section.removeListener(sectionListener);
            if (isMore()) {
                incrementPointer();
                play();
            } else {
                resetPointer();
                notifyEnd();
            }
        }
    };

    public Music(final MusicalContext context) {
        super(context);
    }

    public void addSection(String name, Section section) {
        sections.put(name, section);
    }

    public void addSectionToPlaylist(String name) {
        score.add(sections.get(name));
    }

    public void setInfiniteLoop(boolean flag) {
        loop = flag;
    }

    @Override
    public void play() {

        if (isStart()) {
            notifyStart();
        }

        Section section = getNextSection();
        section.addListener(sectionListener);
        section.play();
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

    private Section getNextSection() {
        Section retval = null;
        try {
            retval = score.get(sectionPtr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }

    private boolean isStart() {
        return sectionPtr == 0;
    }

    private boolean isMore() {
        return sectionPtr < (score.size() - 1);
    }

    private void incrementPointer() {
        sectionPtr++;
    }

    private void resetPointer() {
        sectionPtr = 0;
    }

}
