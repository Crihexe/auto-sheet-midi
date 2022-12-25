package com.crihexe.live;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.crihexe.sheet.Note;

public interface LiveMonitor {
	
	public void onMidiOn(Note note, long timeStamp, long delta);
	public void onMidiOff(Note note, long timeStamp);
	
	public void onOtherShortMessage(ShortMessage message, long timeStamp);
	public void onOtherMidiMessage(MidiMessage message, long timeStamp);
	
}
