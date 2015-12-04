package com.gregnightingale.android.musicbox;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by gregnightingale on 10/27/15.
 */
public class Note extends MusicalObject {

    protected AudioTrack audioTrack;
    protected int bufferSize;
    protected byte[] buffer;
    private NoteDefinition designation;
    private int duration;
    private AudioTrack.OnPlaybackPositionUpdateListener listener = new AudioTrack.OnPlaybackPositionUpdateListener() {
        @Override
        public void onMarkerReached(final AudioTrack track) {
            notifyEnd();
        }

        @Override
        public void onPeriodicNotification(final AudioTrack track) {

        }
    };

    public Note(MusicalContext context, NoteDefinition definition) {
        super(context);
        designation = definition;
        duration = calculateDuration();
        bufferSize = ToneData.calculateBufferSize(duration);
    }

    private int calculateDuration() {
        /*
            a whole note is equal to four beats in 4/4 time,
            a quarter note is one quarter of the duration of a whole,
            per wikipedia: https://en.wikipedia.org/wiki/Whole_note
         */
        final double fraction = designation.getNoteValue();
        final double beatDurationMs = ((1.0 / (double) context.tempo) * 60) * 1000.0;
        final double fourBeatDuration = 4.0 * beatDurationMs;
        return (int) (fourBeatDuration * fraction);
    }

    /**
     * Note: you should call Note.generateBuffer() prior to calling play(), failure to do so
     * could cause delays...
     */
    @Override
    public void play() {

        if (buffer == null) {
            generateBuffer();
        }

        if (audioTrack == null) {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    ToneData.sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize,
                    AudioTrack.MODE_STATIC);
        }

        if (audioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
            Log.e(this.toString(), "AudioTrack not initialized!");
        }

        if (audioTrack.getState() == AudioTrack.STATE_NO_STATIC_DATA) {
            audioTrack.write(buffer, 0, bufferSize);
        }

        if (audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
            int errorCode = audioTrack.setNotificationMarkerPosition((int) (((double) duration * (double) ToneData.sampleRate) / 1000.0));
            audioTrack.setPlaybackPositionUpdateListener(listener);
            audioTrack.play();
            notifyStart();
        }

    }

    /**
     * generate the sounds byte buffer, should be done prior to playback to avoid stutter
     */
    public void generateBuffer() {
        ToneData toneData = new ToneData(designation.getFrequency(), duration, ToneData.MONO);
        buffer = toneData.getBuffer();
    }

    @Override
    public void pause() {
        audioTrack.pause();
        notifyPaused();
    }

    @Override
    public void resume() {
        audioTrack.play();
        notifyResumed();
    }

    @Override
    public void cancel() {
        audioTrack.pause();
        audioTrack.flush();
        notifyCancelled();
    }

    public int getDuration() {
        return duration;
    }

    public double getFrequency() {
        return designation.getFrequency();
    }

    public void setVolume(float leftGain, float rightGain) {
        audioTrack.setStereoVolume(leftGain, rightGain);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
//        audioTrack.release();
    }

    @Override
    public String toString() {
        return "[Note " + designation.getName() + "(" + designation.getType() + ")]";
    }

}
