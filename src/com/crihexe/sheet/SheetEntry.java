package com.crihexe.sheet;

import java.util.ArrayList;

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
	
	public String toString() {
		String s = "Notes: ";
		for(Note n : notes) s += n.getNoteName() + "(" + n.getDuration() + "t) ";
		s += "Delta = " + delta;
		return s;
	}
	
	public static SheetEntry pause(long delta) {	// c'è qualcosa che non va FORSE
		return new SheetEntry(delta);
	}
	
}
