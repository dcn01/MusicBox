package com.gregnightingale.android.musicbox;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void testNotePlay1() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        TimeSignature meter = new TimeSignature("4/4");
        MusicalContext context = new MusicalContext().setTempo(200).setMeter(meter);
        NoteDefinition noteDef = new NoteDefinition("A4", "1");
        final Note note = new Note(context, noteDef);
        note.generateBuffer();
        Listener listener = new Listener() {
            @Override
            void onStart(final MusicalObject started) {
                Log.i("test1", "note started");
                assert note == started;
            }

            @Override
            void onEnd(final MusicalObject ended) {
                Log.i("test1", "note ended");
                assert note == ended;
                latch.countDown();
                note.removeListener(this);
            }
        };
        note.addListener(listener);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("test1", "play note");
                note.play();
            }
        }).start();

        boolean latchTriggered = latch.await(15, TimeUnit.SECONDS);
        assertTrue(latchTriggered);
    }

    public void testPauseNote() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);


        class LooperThread extends Thread {

            Listener listener = new Listener() {
                @Override
                public void onStart(final MusicalObject started) {
                    Log.i("testPauseNote", "note started");
                    assert note == started;
                }

                @Override
                public void onEnd(final MusicalObject ended) {
                    Log.i("testPauseNote", "note ended");
                    assert note == ended;
                    note.removeListener(this);
                    latch.countDown();
                }

                @Override
                public void onPause(final MusicalObject paused) {
                    Log.i("testPauseNote", "note paused");
                    assert note == paused;
                }

                @Override
                void onResume(final MusicalObject resumed) {
                    Log.i("testPauseNote", "note resumed");
                    assert note == resumed;
                }


            };

            public LooperThread() {
            }

            private Note note;

            @Override
            public void run() {

                TimeSignature meter = new TimeSignature("4/4");
                MusicalContext context = new MusicalContext().setTempo(30).setMeter(meter); // each beat is 2 seconds
                NoteDefinition noteDef = new NoteDefinition("C5", "1"); // play a whole note, should take 8 seconds
                note = new Note(context, noteDef);
                note.generateBuffer();
                note.addListener(listener);

                Looper.prepare();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("testPauseNote", "pause the note");
                        note.pause();
                    }
                }, 1000); //pause the note after one second.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("testPauseNote", "resume the note");
                        note.resume();
                    }
                }, 3000); //resume after 3 seconds, should play 5 more seconds
                note.play();
                Looper.loop();
            }

        }

        new LooperThread().start();

        boolean latchTriggered = latch.await(12, TimeUnit.SECONDS);
        assertTrue(latchTriggered);
    }

    public void testBarPlay1() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        Note noteA, noteB, noteC, noteD;

        TimeSignature meter = new TimeSignature("4/4");
        MusicalContext context = new MusicalContext().setTempo(200).setMeter(meter);

        noteA = new Note(context, new NoteDefinition("A4", "4"));
        noteB = new Note(context, new NoteDefinition("B4", "4"));
        noteC = new Note(context, new NoteDefinition("C5", "4"));
        noteD = new Note(context, new NoteDefinition("D5", "4"));

        Beat beat1 = new Beat(context);
        beat1.add(noteA);
        Beat beat2 = new Beat(context);
        beat2.add(noteB);
        Beat beat3 = new Beat(context);
        beat3.add(noteC);
        Beat beat4 = new Beat(context);
        beat4.add(noteD);
        final Bar bar = new Bar(context);
        bar.addBeat(beat1);
        bar.addBeat(beat2);
        bar.addBeat(beat3);
        bar.addBeat(beat4);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Log.i("test2", "play bar");
                bar.play();
                Looper.loop();
            }
        });

        bar.addListener(new Listener() {
            @Override
            public void onEnd(final MusicalObject ended) {
                Log.i("test2", ended.toString() + " ended.");
                assert bar == ended;
                latch.countDown();
            }
        });

        thread.start();
        boolean latchTriggered = latch.await(15, TimeUnit.SECONDS);
        assertTrue(latchTriggered);

    }

    public void testXmlMusicReader() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        MusicReader musicReader = new MusicReader(getContext().getResources().openRawResource(R.raw.input));
        final Music music = musicReader.read();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Log.i("testMusic", "play music");
                music.play();
                Looper.loop();
            }
        });

        music.addListener(new Listener() {
            @Override
            public void onStart(final MusicalObject obj) {

            }

            @Override
            public void onEnd(final MusicalObject ended) {
                Log.i("testMusic", ended.toString() + " ended.");
                assert music == ended;
                latch.countDown();
            }

        });
        thread.start();
        boolean latchTriggered = latch.await(15, TimeUnit.SECONDS);
        assertTrue(latchTriggered);
    }

}
