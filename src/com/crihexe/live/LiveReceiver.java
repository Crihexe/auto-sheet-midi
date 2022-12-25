package com.crihexe.live;

import java.util.ArrayList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import com.crihexe.sheet.Note;
import com.crihexe.util.Pair;

public class LiveReceiver implements Receiver {
	
	private LiveMonitor callback;
	
	private ArrayList<Pair<Note, Long>> pressedNotes = new ArrayList<Pair<Note, Long>>();
	private long lastOnTick = 0;
	
	public LiveReceiver(LiveMonitor callback) {
		this.callback = callback;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		if(message instanceof ShortMessage sm) {
			if(sm.getData2() == 0 || sm.getCommand() == ShortMessage.NOTE_OFF) {	// midi off
				for(int j = pressedNotes.size() - 1; j >= 0; j--) {
					if(pressedNotes.get(j).t1.equals(Note.fromShortMessage(sm))) {
						pressedNotes.get(j).t1.setDuration(timeStamp - pressedNotes.get(j).t2);
						
						callback.onMidiOff(pressedNotes.get(j).t1, timeStamp);
						
						pressedNotes.remove(j);
						
						break;
					}
				}
			} else if(sm.getCommand() == ShortMessage.NOTE_ON) {	// midi on
				Note note = Note.fromShortMessage(sm);
				pressedNotes.add(new Pair<Note, Long>(note, timeStamp));
				
				callback.onMidiOn(note, timeStamp, timeStamp - lastOnTick);
				
				lastOnTick = timeStamp;
			} else callback.onOtherShortMessage(sm, timeStamp);
		} else callback.onOtherMidiMessage(message, timeStamp);
	}

	@Override
	public void close() {
		pressedNotes.clear();
		lastOnTick = 0;
	}

}
