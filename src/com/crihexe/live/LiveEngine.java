package com.crihexe.live;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.crihexe.sheet.Note;
import com.crihexe.sheet.Sheet;

public class LiveEngine implements Runnable, LiveMonitor {
	
	private final Sheet sheet;
	private int pointer = 0;
	
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

	/*
	 * 
	 * Dobbiamo dividere il lavoro in vari obbiettivi che porteranno al miglioramento
	 * 
	 * 1) Essere in grado di riconoscere se lo spartito è stato suonato nell'ordine giusto
	 * 	 a) Spartito semplice con SheetEntry singole ad una sola traccia
	 * 	 b) Spartito con due tracce ma con SheetEntry singola
	 *   c) Spartito con accordi e due tracce
	 * 2) Riconoscere se le note sono state premute al momento giusto
	 * 	 a) Spartito semplice con SheetEntry singole ad una sola traccia
	 *   b) Spartito con accordi e due tracce
	 *   c) Saperlo fare con tempi strani (tipo 3/4)
	 *   d) Avere tolleranza allo swing
	 * 3) Essere in grado di Handlare le note suonate in modo errato permettendo di continuare
	 * 	  a suonare lo spartito
	 *   a) Controllando se nell'ordine giusto
	 *   b) Controllando se nel momento giusto
	 *   c) Essere in grado di riconoscere, dopo molti errori di seguito, se l'utente ha deciso
	 *      di riprendere dalla stessa o da battute precedenti
	 * 4) per ora boh, è già abbastanza difficile LOL
	 * 
	 */
	
	@Override
	public void run() {
		while(true);
	}

	@Override
	public void onMidiOn(Note note, long timeStamp, long delta) {
		if(note.getKey() == 108) System.exit(0);
		//System.out.println("ON " + note + " tick:" + timeStamp + " delta:" + delta);
		
		if(pointer == sheet.size()) {
			System.out.println("Hai completato lo spartito!");
			return;
		}
		
		if(sheet.getEntry(pointer).contains(note)) {
			pointer++;
			System.out.println("yes, " + note);
		} else System.out.println("no. retry");
		
	}

	@Override
	public void onMidiOff(Note note, long timeStamp) {
		//System.out.println("OFF " + note + " tick:" + timeStamp);
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
