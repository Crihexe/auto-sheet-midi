package com.crihexe.sheet;

import java.util.ArrayList;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Note {
	
	private static final String[] NOTE_NAMES = {"DO", "DO#", "RE", "RE#", "MI", "FA", "FA#", "SOL", "SOL#", "LA", "LA#", "SI"};
	
	private int key;
	private int octave;
	private int note;
	private int velocity;
	
	private int duration = 0;
	
	public Note(int key, int octave, int note, int velocity) {
		this.key = key;
		this.octave = octave;
		this.note = note;
		this.velocity = velocity;
	}

	public static String[] getNoteNames() {
		return NOTE_NAMES;
	}

	public int getKey() {
		return key;
	}

	public int getOctave() {
		return octave;
	}

	public int getNote() {
		return note;
	}
	
	public String getNoteName() {
		return NOTE_NAMES[note];
	}

	public int getVelocity() {
		return velocity;
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String toString() {
		return "Note " + getNoteName() + octave + " key=" + key + " velocity: " + velocity;
	}
	
	public boolean equals(Note note) {
		return note.key == key && note.note == this.note && note.octave == octave;
	}
	
	public boolean strictlyEquals(Note note) {
		return note.key == key && note.note == this.note && note.octave == octave && note.velocity == velocity;
	}
	
	public static Note fromShortMessage(ShortMessage m) {
		int key = m.getData1();
		return new Note(key, (key/12)-1, key % 12, m.getData2());
	}
	
	public static Note[] trackToNoteArray(Track track) {
		ArrayList<Note> notes = new ArrayList<Note>();
		ArrayList<Note> queue = new ArrayList<Note>();
		
		for(int i = 0; i < track.size(); i++) {
			if(track.get(i).getMessage() instanceof ShortMessage sm) {
				if(sm.getCommand() == ShortMessage.NOTE_ON)
					queue.add(fromShortMessage(sm));
				else if(sm.getCommand() == ShortMessage.NOTE_OFF) {
					Note temp = fromShortMessage(sm);
					System.out.println(queue.size());
					for(int j = queue.size() - 1; j >= 0; j--)
						if(queue.get(j).equals(temp)) {
							notes.add(queue.get(j));
							queue.remove(j);
						}
				}
			}
		}
		
		Note[] arr = new Note[notes.size()];
		return notes.toArray(arr);
	}
	
}
