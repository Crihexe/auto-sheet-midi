package com.crihexe;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import com.crihexe.sheet.Note;

public class LiveReceiver implements Receiver {
	
	
	@Override
	public void send(MidiMessage message, long timeStamp) {
		if(message instanceof ShortMessage sm && sm.getCommand() == ShortMessage.NOTE_ON) {
			Note note = Note.fromShortMessage(sm);
			System.out.println(note);
			MidiQueue.addNote(note);
		}
	}

	@Override
	public void close() {
		
	}
	
}
