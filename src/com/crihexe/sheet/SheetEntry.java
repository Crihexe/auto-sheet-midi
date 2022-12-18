package com.crihexe.sheet;

import java.util.ArrayList;

public class SheetEntry {
	
	private ArrayList<Note> notes;
	private int delta;
	
	public SheetEntry(int delta) {
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

	public int getDelta() {
		return delta;
	}
	
	public void setDelta(int delta) {
		this.delta = delta;
	}
	
	public static SheetEntry pause(int delta) {
		return new SheetEntry(delta);
	}
	
}
