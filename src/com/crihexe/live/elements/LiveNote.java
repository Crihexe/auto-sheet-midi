package com.crihexe.live.elements;

import com.crihexe.sheet.Note;

public class LiveNote extends Note {
	
	private long timeStamp;
	private long delta;
	
	public LiveNote(Note note, long timeStamp, long delta) {
		super(note);
		this.timeStamp = timeStamp;
		this.delta = delta;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getDelta() {
		return delta;
	}

	public void setDelta(long delta) {
		this.delta = delta;
	}
	
}
