package com.crihexe.live.listener;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.crihexe.live.elements.LiveChord;
import com.crihexe.live.elements.LiveNote;
import com.crihexe.sheet.Note;

public interface LiveMonitor {
	
	public void onMidiOn(Note note, long timeStamp, long delta);
	public void onMidiOff(Note note, long timeStamp);
	
	public void onSingleNote(LiveNote note);
	public void onChord(LiveChord chord);
	
	public void onOtherShortMessage(ShortMessage message, long timeStamp);
	public void onOtherMidiMessage(MidiMessage message, long timeStamp);
	
}
