package com.crihexe.live.elements;

import java.util.ArrayList;

import com.crihexe.sheet.Note;

public class LiveChord {
	
	public ArrayList<LiveNote> chord = new ArrayList<LiveNote>();
	
	public LiveChord() {}
	
	public long getDelta() {
		return chord.get(chord.size()-1).getTimeStamp() - chord.get(0).getTimeStamp();
	}
	
	// fare attenzione ai costruttori copia
	public LiveChord subList(int from, int to) {
		LiveChord c = new LiveChord();
		for(int i = from; i < to; i++) c.chord.add(chord.get(i));
		return c;
	}
	
	public String toString() {
		String s = "Notes: ";
		for(Note n : chord) s += n.getNoteName() + n.getOctave() + "(" + n.getDuration() + "t) ";
		s += "Delta = " + getDelta();
		return s;
	}
	
}
