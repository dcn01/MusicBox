package com.gregnightingale.android.musicbox;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gregnightingale on 11/2/15.
 */
public class MusicReader extends XmlReader {

    MusicalContext context = new MusicalContext();

    Music music;
    Section currentSection;
    Bar currentBar;
    Beat currentBeat;
    Chord currentChord;
    Sequence currentSequence;

    public MusicReader(final InputStream xml) throws XmlPullParserException {
        super(xml);
    }

    @Override
    public Music read() throws XmlPullParserException, IOException {
        super.read();
        return music;
    }

    @Override
    protected void startDocument() {

    }

    @Override
    protected void startTag() {
        String tag = parser.getName();
        switch (tag) {
            case "music":
                music = new Music(context);
                int bpm = getIntegerAttribute("bpm");
                context.setTempo(bpm);
                break;
            case "section":
                String sectionName = getStringAttribute("name");
                Section section = new Section(context, sectionName);
                currentSection = section;
                music.addSection(sectionName, section);
                break;
            case "bar":
                TimeSignature sig = new TimeSignature(getStringAttribute("timeSignature"));
                context.setMeter(sig);
                Bar bar = new Bar(context);
                currentBar = bar;
                currentSection.add(bar);
                break;
            case "beat":
                Beat beat = new Beat(context);
                currentBeat = beat;
                if (currentBar != null) {
                    currentBar.addBeat(beat);
                }
                break;
            case "chord":
                Chord chord = new Chord(context);
                currentChord = chord;
                if (currentSequence != null) {
                    currentSequence.add(chord);
                } else if (currentBeat != null) {
                    currentBeat.add(chord);
                }
                break;
            case "note":
                String name = getStringAttribute("name");
                String type = getStringAttribute("type");
                NoteDefinition definition = new NoteDefinition(name, type);
                Note note = new Note(context, definition);
                note.generateBuffer();
//                note.loadAudioTrack();
                if (currentChord != null) {
                    currentChord.add(note);
                } else if (currentSequence != null) {
                    currentSequence.add(note);
                } else if (currentBeat != null) {
                    currentBeat.add(note);
                } else {
                    Log.e(tag, "Bad XML - note is misplaced!");
                }
                break;
            case "sequence":
                Sequence sequence = new Sequence(context);
                currentSequence = sequence;
                if (currentBeat != null) {
                    currentBeat.add(sequence);
                } else {
                    Log.e(tag, "Bad XML - note is misplaced!");
                }
                break;
            case "play":
                final Integer count = getIntegerAttribute("count");
                for (int i = 0; i < count; i++) {
                    music.addSectionToPlaylist(getStringAttribute("name"));
                }
                break;
            case "infiniteLoop":
                music.setInfiniteLoop(true);
                break;
        }

        //TODO this will throw npe exception if xml is not constructed correctly, decide what to do.
    }

    @Override
    protected void text() {

    }

    @Override
    protected void endTag() {
        String tag = parser.getName();
        switch (tag) {
            case "bar":
                currentBar = null;
                break;
            case "beat":
                currentBeat = null;
                break;
            case "chord":
                currentChord = null;
                break;
            case "section":
                currentSection = null;
                break;
            case "sequence":
                currentSequence = null;
                break;
        }
    }

    @Override
    protected void endDocument() {

    }
}
