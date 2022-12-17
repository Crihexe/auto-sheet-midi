package com.crihexe;

import java.util.ArrayList;

public class MidiQueue {
	
	private static ArrayList<Note> notes = new ArrayList<Note>();
	
	public MidiQueue() {
		
	}
	
	public static void addNote(Note n) {
		notes.add(n);
	}
	
	public static boolean ready() {
		return notes.size() > 0;
	}
	
	public static Note pop() {
		Note n = notes.get(0);
		notes.remove(0);
		return n;
	}
	
}
