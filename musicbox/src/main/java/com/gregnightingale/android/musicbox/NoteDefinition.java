package com.gregnightingale.android.musicbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregnightingale on 11/17/15.
 */
public class NoteDefinition {

    private static final Map<String, Double> noteValueMap;
    private static final Map<String, Double> freqMap;

    static {
        Map<String, Double> map = new HashMap<>();
        map.put("1", 1.0);
        map.put("2", 1.0 / 2);
        map.put("4", 1.0 / 4);
        map.put("8", 1.0 / 8);
        map.put("16", 1.0 / 16);
        noteValueMap = Collections.unmodifiableMap(map);
    }

    //@formatter:off
    static {
        Map<String, Double> map = new HashMap<>();

        map.put("C0" ,16.352); map.put("C1" ,32.703); map.put("C2" ,65.406); map.put("C3" ,130.81); map.put("C4" ,261.63); map.put("C5" ,523.25); map.put("C6" ,1046.5); map.put("C7", 2093.0); map.put("C8" ,4186.0); map.put("C9" , 8372.0); map.put("C10" ,16744.0);
        map.put("C#0",17.324); map.put("C#1",34.648); map.put("C#2",69.296); map.put("C#3",138.59); map.put("C#4",277.18); map.put("C#5",554.37); map.put("C#6",1108.7); map.put("C#7",2217.5); map.put("C#8",4434.9); map.put("C#9", 8869.8); map.put("C#10",17739.7);
        map.put("Db0",17.324); map.put("Db1",34.648); map.put("Db2",69.296); map.put("Db3",138.59); map.put("Db4",277.18); map.put("Db5",554.37); map.put("Db6",1108.7); map.put("Db7",2217.5); map.put("Db8",4434.9); map.put("Db9", 8869.8); map.put("Db10",17739.7);
        map.put("D0" ,18.354); map.put("D1" ,36.708); map.put("D2" ,74.416); map.put("D3" ,146.83); map.put("D4" ,293.66); map.put("D5" ,587.33); map.put("D6" ,1174.7); map.put("D7" ,2349.3); map.put("D8" ,4698.6); map.put("D9" , 9397.3); map.put("D10" ,18794.5);
        map.put("D#0",19.445); map.put("D#1",38.891); map.put("D#2",77.782); map.put("D#3",155.56); map.put("D#4",311.13); map.put("D#5",622.25); map.put("D#6",1244.5); map.put("D#7",2489.0); map.put("D#8",4978.0); map.put("D#9", 9956.1); map.put("D#10",19912.1);
        map.put("Eb0",19.445); map.put("Eb1",38.891); map.put("Eb2",77.782); map.put("Eb3",155.56); map.put("Eb4",311.13); map.put("Eb5",622.25); map.put("Eb6",1244.5); map.put("Eb7",2489.0); map.put("Eb8",4978.0); map.put("Eb9", 9956.1); map.put("Eb10",19912.1);
        map.put("E0" ,20.602); map.put("E1" ,41.203); map.put("E2" ,82.407); map.put("E3" ,164.81); map.put("E4" ,329.63); map.put("E5" ,659.26); map.put("E6" ,1318.5); map.put("E7" ,2637.0); map.put("E8" ,5274.0); map.put("E9" ,10548.1); map.put("E10" ,21096.2);
        map.put("F0" ,21.827); map.put("F1" ,43.654); map.put("F2" ,87.307); map.put("F3" ,174.61); map.put("F4" ,349.23); map.put("F5" ,698.46); map.put("F6" ,1396.9); map.put("F7" ,2793.8); map.put("F8" ,5587.7); map.put("F9" ,11175.3); map.put("F10" ,22350.6);
        map.put("F#0",23.125); map.put("F#1",46.249); map.put("F#2",92.499); map.put("F#3",185.00); map.put("F#4",369.99); map.put("F#5",739.99); map.put("F#6",1480.0); map.put("F#7",2960.0); map.put("F#8",5919.9); map.put("F#9",11839.8); map.put("F#10",23679.6);
        map.put("Gb0",23.125); map.put("Gb1",46.249); map.put("Gb2",92.499); map.put("Gb3",185.00); map.put("Gb4",369.99); map.put("Gb5",739.99); map.put("Gb6",1480.0); map.put("Gb7",2960.0); map.put("Gb8",5919.9); map.put("Gb9",11839.8); map.put("Gb10",23679.6);
        map.put("G0" ,24.500); map.put("G1" ,48.999); map.put("G2" ,97.999); map.put("G3" ,196.00); map.put("G4" ,392.00); map.put("G5" ,783.99); map.put("G6" ,1568.0); map.put("G7" ,3136.0); map.put("G8" ,6271.9); map.put("G9" ,12543.9); map.put("G10" ,25087.7);
        map.put("G#0",25.957); map.put("G#1",51.913); map.put("G#2",103.83); map.put("G#3",207.65); map.put("G#4",415.30); map.put("G#5",830.61); map.put("G#6",1661.2); map.put("G#7",3322.4); map.put("G#8",6644.9); map.put("G#9",13289.8); map.put("G#10",26579.5);
        map.put("Ab0",25.957); map.put("Ab1",51.913); map.put("Ab2",103.83); map.put("Ab3",207.65); map.put("Ab4",415.30); map.put("Ab5",830.61); map.put("Ab6",1661.2); map.put("Ab7",3322.4); map.put("Ab8",6644.9); map.put("Ab9",13289.8); map.put("Ab10",26579.5);
        map.put("A0" ,27.500); map.put("A1" ,55.000); map.put("A2" ,110.00); map.put("A3" ,220.00); map.put("A4" ,440.00); map.put("A5" ,880.00); map.put("A6" ,1760.0); map.put("A7" ,3520.0); map.put("A8" ,7040.0); map.put("A9" ,14080.0); map.put("A10" ,28160.0);
        map.put("A#0",29.135); map.put("A#1",58.270); map.put("A#2",116.54); map.put("A#3",233.08); map.put("A#4",466.16); map.put("A#5",932.33); map.put("A#6",1864.7); map.put("A#7",3729.3); map.put("A#8",7458.6); map.put("A#9",14917.2); map.put("A#10",29834.5);
        map.put("Bb0",29.135); map.put("Bb1",58.270); map.put("Bb2",116.54); map.put("Bb3",233.08); map.put("Bb4",466.16); map.put("Bb5",932.33); map.put("Bb6",1864.7); map.put("Bb7",3729.3); map.put("Bb8",7458.6); map.put("Bb9",14917.2); map.put("Bb10",29834.5);
        map.put("B0" ,30.868); map.put("B1" ,61.735); map.put("B2" ,123.47); map.put("B3" ,246.94); map.put("B4" ,493.88); map.put("B5" ,987.77); map.put("B6" ,1975.5); map.put("B7" ,3951.1); map.put("B8" ,7902.1); map.put("B9" ,15804.3); map.put("B10" ,31608.5);

        freqMap = Collections.unmodifiableMap(map);
    }
    //@formatter:on

    private String name;
    private String type;
    private double frequency;
    private double noteValue;

    public NoteDefinition(String name, String type) {
        this.name = name;
        this.type = type;
        frequency = freqMap.get(name);
        noteValue = noteValueMap.get(type);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getNoteValue() {
        return noteValue;
    }
}
