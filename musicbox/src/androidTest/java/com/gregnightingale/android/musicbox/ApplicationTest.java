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
                assert note == started;
            }

            @Override
            void onEnd(final MusicalObject ended) {
                assert note == ended;
                latch.countDown();
                note.removeListener(this);
            }
        };
        note.addListener(listener);

        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    assert note == started;
                }

                @Override
                public void onEnd(final MusicalObject ended) {
                    assert note == ended;
                    note.removeListener(this);
                    latch.countDown();
                }

                @Override
                public void onPause(final MusicalObject paused) {
                    assert note == paused;
                }

                @Override
                void onResume(final MusicalObject resumed) {
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
                        note.pause();
                    }
                }, 1000); //pause the note after one second.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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

    public void testCancelNote() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        class LooperThread extends Thread {

            Listener listener = new Listener() {
                @Override
                void onStart(final MusicalObject started) {
                    assert note == started;
                }

                @Override
                public void onEnd(final MusicalObject ended) {
                    assert note != ended;
                    note.removeListener(this);
                }

                @Override
                public void onPause(final MusicalObject paused) {
                    assert note != paused;
                }

                @Override
                void onResume(final MusicalObject resumed) {
                    assert note != resumed;
                }

                @Override
                void onCancel(final MusicalObject cancelled) {
                    assert note == cancelled;
                    latch.countDown();
                }
            };

            public LooperThread() {
            }

            private Note note;

            @Override
            public void run() {

                TimeSignature meter = new TimeSignature("4/4");
                MusicalContext context = new MusicalContext().setTempo(30).setMeter(meter); // each beat is 2 seconds
                NoteDefinition noteDef = new NoteDefinition("F5", "1"); // play a whole note, should take 8 seconds
                note = new Note(context, noteDef);
                note.generateBuffer();
                note.addListener(listener);

                Looper.prepare();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        note.cancel();
                    }
                }, 1000); // cancel after 1 second.
                note.play();
                Looper.loop();
            }

        }

        new LooperThread().start();

        boolean latchTriggered = latch.await(12, TimeUnit.SECONDS);
        assertTrue(latchTriggered);
    }

    public void testBarPlay1() throws Exception {
        final CountDownLatch noteTestLatch = new CountDownLatch(4);
        final CountDownLatch beatTestLatch = new CountDownLatch(6);

        class TestThread extends Thread {

            private Note noteA, noteB, noteC, noteD;
            private Beat beat1, beat2, beat3, beat4;
            private Bar bar;

            @Override
            public void run() {
                TimeSignature meter = new TimeSignature("4/4");
                MusicalContext context = new MusicalContext().setTempo(30).setMeter(meter);

                noteA = new Note(context, new NoteDefinition("A4", "4"));
                noteB = new Note(context, new NoteDefinition("B4", "4"));
                noteC = new Note(context, new NoteDefinition("C5", "4"));
                noteD = new Note(context, new NoteDefinition("D5", "4"));
                beat1 = new Beat(context);
                beat1.add(noteA);
                beat2 = new Beat(context);
                beat2.add(noteB);
                beat3 = new Beat(context);
                beat3.add(noteC);
                beat4 = new Beat(context);
                beat4.add(noteD);
                bar = new Bar(context);
                bar.addBeat(beat1);
                bar.addBeat(beat2);
                bar.addBeat(beat3);
                bar.addBeat(beat4);

                Looper.prepare();
                final Handler handler = new Handler();

                noteA.addListener(new Listener() {
                    @Override
                    void onStart(final MusicalObject obj) {
                        assert obj == noteA;
                        noteTestLatch.countDown();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noteA.pause();
                            }
                        }, 500);
                    }

                    @Override
                    void onPause(final MusicalObject obj) {
                        assert obj == noteA;
                        noteTestLatch.countDown();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noteA.resume();
                            }
                        }, 175);
                    }

                    @Override
                    void onResume(final MusicalObject obj) {
                        assert obj == noteA;
                        noteTestLatch.countDown();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noteA.cancel();
                            }
                        }, 175);
                    }

                    @Override
                    void onCancel(final MusicalObject obj) {
                        assert obj == noteA;
                        noteTestLatch.countDown();
                        noteA.removeListener(this);
                    }
                });

                final Listener beatTest2Listener = new Listener() {
                    @Override
                    void onStart(final MusicalObject obj) {
                        beatTestLatch.countDown();
                    }

                    @Override
                    void onEnd(final MusicalObject obj) {
                        beatTestLatch.countDown();
                    }
                };

                final Listener beatTest1Listener = new Listener() {
                    @Override
                    void onStart(final MusicalObject obj) {
                        assert obj == beat1;
                        beatTestLatch.countDown();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                beat1.pause();
                            }
                        }, 100);
                    }

                    @Override
                    void onPause(final MusicalObject obj) {
                        assert obj == beat1;
                        beatTestLatch.countDown();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                beat1.resume();
                            }
                        }, 100);
                    }

                    @Override
                    void onCancel(final MusicalObject obj) {
                        assert obj == beat1;
                        beatTestLatch.countDown();
                        beat1.removeListener(this);
                        beat1.addListener(beatTest2Listener);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                beat1.play();
                            }
                        }, 100);
                    }

                    @Override
                    void onResume(final MusicalObject obj) {
                        assert obj == beat1;
                        beatTestLatch.countDown();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                beat1.cancel();
                            }
                        }, 100);
                    }
                };


                beat1.addListener(beatTest1Listener);
                bar.play();
                Looper.loop();

            }
        }
        new TestThread().start();

        boolean test1pass = noteTestLatch.await(12, TimeUnit.SECONDS);
        assertTrue(test1pass);
        boolean test2pass = beatTestLatch.await(12, TimeUnit.SECONDS);
        assertTrue(test2pass);
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
