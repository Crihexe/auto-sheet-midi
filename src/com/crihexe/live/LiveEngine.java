package com.crihexe.live;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.crihexe.sheet.Note;
import com.crihexe.sheet.Sheet;

public class LiveEngine implements Runnable, LiveMonitor {
	
	private final Sheet sheet;
	
	private final Thread thread;
	
	public LiveEngine(Sheet sheet) {
		this.sheet = sheet;
		thread = new Thread(this, "Live Engine - \"" + this.sheet.getID() + "\"");
	}
	
	public LiveEngine start() throws IllegalStateException {
		Device.setReceiver(new LiveReceiver(this));
		
		if(!Device.ready()) throw new IllegalStateException("Device not selected");
		
		thread.start();
		return this;
	}

	@Override
	public void run() {
		while(true);
	}

	@Override
	public void onMidiOn(Note note, long timeStamp, long delta) {
		System.out.println("ON " + note + " tick:" + timeStamp + " delta:" + delta);
	}

	@Override
	public void onMidiOff(Note note, long timeStamp) {
		System.out.println("OFF " + note + " tick:" + timeStamp);
	}

	@Override
	public void onOtherShortMessage(ShortMessage message, long timeStamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOtherMidiMessage(MidiMessage message, long timeStamp) {
		// TODO Auto-generated method stub
		
	}
	
}
