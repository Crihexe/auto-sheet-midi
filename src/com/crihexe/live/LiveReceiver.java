package com.crihexe.live;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import com.crihexe.live.elements.LiveChord;
import com.crihexe.live.elements.LiveNote;
import com.crihexe.live.listener.LiveMonitor;
import com.crihexe.sheet.Note;
import com.crihexe.util.Pair;

public class LiveReceiver implements Receiver {
	
	private LiveMonitor callback;
	
	private ArrayList<Pair<Note, Long>> pressedNotes = new ArrayList<Pair<Note, Long>>();
	private long lastOnTick = 0;
	
	private LiveChord chordStack = new LiveChord();
	
	private Timer chordStackTimer = new Timer();
	
	public LiveReceiver(LiveMonitor callback) {
		this.callback = callback;
		chordStackTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				clearChordStack();
			}
			
			public synchronized void clearChordStack() {
				
				/*if(chordStack.chord.size() > 1) {
					if(chordStack.getDelta() > 80) {	// QUI NO
						// non devo controllare che il delta tra la prima e l'ultima nota dell'accordo sia maggiore di 80
						// ma devo controllare che il tick della prima nota dell'accordo sottratto al tick in cui viene
						// eseguito il thread sia maggiore di 80, il che significa che in ogni caso alla prossima
						// nota premuta non avremmo un continuo di accordo, ciò significa che possiamo prendere il contenuto
						// dello stack come buono per essere mandato al callback e svuotarlo.
						if(chordStack.chord.size() == 2) {
							callback.onSingleNote(chordStack.chord.remove(0));
						} else {
							LiveChord chord = chordStack.subList(0, chordStack.chord.size()-1);
							
							chordStack.chord.removeAll(chord.chord);
							
							callback.onChord(chord);
						}
					}
				}*/
				
				if(chordStack.chord.size() > 0) {
					System.out.println("Device Delta: " + (Device.getTime() - chordStack.chord.get(0).getTimeStamp()));
					if(Device.getTime() - chordStack.chord.get(0).getTimeStamp() > 80) {
						if(chordStack.chord.size() == 1) {
							callback.onSingleNote(chordStack.chord.remove(0));
							System.out.println("NOTE !!!!!!!!!!!!!!!!!!!!!called by timer");
						} else {
							LiveChord chord = chordStack.subList(0, chordStack.chord.size());	// da risistemare, è inefficente
							chordStack.chord.removeAll(chord.chord);
							System.out.println("CHORD !!!!!!!!!!!!!!!!!!!!!called by timer");
							callback.onChord(chord);
						}
					}
				}
				
			}
			
		}, 0, 20);	// forse troppo poco il delay, 20 ms sono 50 volte al secondo!!!!
					// TODO Fare test con delay più alto
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		timeStamp /= 1000;	// come nel formato dei file
		
		if(message instanceof ShortMessage sm) {
			if(sm.getData2() == 0 || sm.getCommand() == ShortMessage.NOTE_OFF) {	// midi off
				for(int j = pressedNotes.size() - 1; j >= 0; j--) {
					if(pressedNotes.get(j).t1.equals(Note.fromShortMessage(sm))) {
						pressedNotes.get(j).t1.setDuration(timeStamp - pressedNotes.get(j).t2);
						
						try {
							callback.onMidiOff(pressedNotes.get(j).t1, timeStamp);
						} catch(Exception e) {
							e.printStackTrace();
						}
						
						pressedNotes.remove(j);
						
						break;
					}
				}
			} else if(sm.getCommand() == ShortMessage.NOTE_ON) {	// midi on
				Note note = Note.fromShortMessage(sm);
				pressedNotes.add(new Pair<Note, Long>(note, timeStamp));
				try {
					onMidiOn(note, timeStamp, timeStamp - lastOnTick);
					callback.onMidiOn(note, timeStamp, timeStamp - lastOnTick);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				lastOnTick = timeStamp;
			} else callback.onOtherShortMessage(sm, timeStamp);
		} else callback.onOtherMidiMessage(message, timeStamp);
	}
	
	public synchronized void onMidiOn(Note note, long timeStamp, long delta) {
		try {
			LiveNote ln = new LiveNote(note, timeStamp, delta);
			
			// funziona malissimo: solo se inizia con un accordo e ogni accordo è separato dall'altro da una sola nota
			/*chordStack.chord.add(ln);
			if(chordStack.chord.size() > 1) {
				if(chordStack.getDelta() > 80) {
					
					callback.onSingleNote(chordStack.chord.remove(chordStack.chord.size()-1)); 
					
					callback.onChord(chordStack);	// facciamo finta che clear non distrugge il contenuto dell'oggetto appena passato O.o
					chordStack.chord.clear();
					
				}
			}*/
			
			chordStack.chord.add(ln);
			if(chordStack.chord.size() > 1) {
				if(chordStack.getDelta() > 80) {
					if(chordStack.chord.size() == 2) {
						callback.onSingleNote(chordStack.chord.remove(0));
					} else {
						LiveChord chord = chordStack.subList(0, chordStack.chord.size()-1);
						
						chordStack.chord.removeAll(chord.chord);
						
						callback.onChord(chord);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			chordStack.chord.clear();
		}
	}

	@Override
	public void close() {
		pressedNotes.clear();
		lastOnTick = 0;
	}

}
