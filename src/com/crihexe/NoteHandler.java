package com.crihexe;

import javax.sound.midi.Track;

public class NoteHandler implements Runnable {
	
	private Note[] track;
	private int counter = 0;
	
	public NoteHandler(Note[] track) {
		this.track = track;
	}

	@Override
	public void run() {
		while(true) {	// migliorare sta cosa dell'aspettare ogni 400 secondi
					// con gli interrupt dei thread che spaccherebbe.
				// sveglio questo thread ogni volta che viene aggiunto qualcosa alla coda
			if(MidiQueue.ready()) {
				Note note = MidiQueue.pop();
				System.out.println(note);
				if(note.equals(track[counter])) {
					counter++;
					System.out.println("GIUSTO");
				}
				
			} else
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
}
