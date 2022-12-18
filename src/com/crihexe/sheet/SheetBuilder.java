package com.crihexe.sheet;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SheetBuilder {
	
	private Sheet sheet;
	private Track tracks[];
	
	public SheetBuilder(Track tracks[]) {
		sheet = new Sheet();
		this.tracks = tracks;
	}
	
	public void build() {
		
		/*for(Track track : tracks) {
			
			
			
		}*/
		
		Track track = tracks[0];
		for(int i = 0; i < track.size(); i++) {
			MidiEvent e = track.get(i);
			
			if(e.getMessage() instanceof ShortMessage sm) {
				if(sm.getCommand() == ShortMessage.NOTE_ON) {
					
				}
				if(sm.getCommand() == ShortMessage.NOTE_OFF) {
					
				}
			}
			
		}
		
	}
	
	public Sheet result() {
		return sheet;
	}

	public void send(MidiMessage message, long timeStamp) {
		
		// per ora prendo traccia di tutti gli ShortMessage.
		// gli ShortMessages sono eventi sulla pressione di tasti sul piano in pratica
		if(message instanceof ShortMessage sm) {
			if(sm.getCommand() == ShortMessage.NOTE_ON) {
				
			}
			if(sm.getCommand() == ShortMessage.NOTE_OFF) {
				
			}
		}
		
	}
	
}
