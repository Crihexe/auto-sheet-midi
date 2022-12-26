package com.crihexe.sheet;

import java.util.ArrayList;

import com.crihexe.live.elements.LiveChord;

public class SheetEntry {
	
	private ArrayList<Note> notes;
	private long delta;
	
	public SheetEntry(long delta) {
		notes = new ArrayList<Note>();
		this.delta = delta;
	}
	
	public void add(Note note) {
		notes.add(note);
	}
	
	public void removeLast() {
		notes.remove(notes.size()-1);
	}
	
	public void clear() {
		notes.clear();
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}

	public long getDelta() {
		return delta;
	}
	
	public void setDelta(long delta) {
		this.delta = delta;
	}
	
	public boolean contains(Note note) {
		for(Note n : notes)
			if(n.equals(note)) return true;
		return false;
	}
	
	public boolean checkChord(LiveChord chord) {
		boolean r = true;
		for(int i = 0; i < chord.chord.size(); i++)
			r &= contains(chord.chord.get(i));
		return r;
	}
	
	public boolean strictlyContains(Note note) {
		for(Note n : notes)
			if(n.strictlyEquals(note)) return true;
		return false;
	}
	
	public boolean hasSingleNote(Note note) {
		if(notes.size() != 1) return false;
		return notes.get(0).equals(note);
	}
	
	public String toString() {
		String s = "Notes: ";
		for(Note n : notes) s += n.getNoteName() + n.getOctave() + "(" + n.getDuration() + "t) ";
		s += "Delta = " + delta;
		return s;
	}
	
	public static SheetEntry pause(long delta) {	// c'è qualcosa che non va FORSE
		return new SheetEntry(delta);
	}
	
}
