package com.crihexe.live;

import java.util.ArrayList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.crihexe.live.elements.LiveChord;
import com.crihexe.live.elements.LiveNote;
import com.crihexe.live.listener.LiveMonitor;
import com.crihexe.live.listener.SheetPointerListener;
import com.crihexe.sheet.Note;
import com.crihexe.sheet.Sheet;
import com.crihexe.util.Pair;

public class LiveEngine implements Runnable, LiveMonitor {
	
	private final Sheet sheet;
	private int pointer = 0;
	
	private final Thread thread;
	
	private SheetPointerListener pointerListener;
	
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
	
	public void setSheetPointerListener(SheetPointerListener pointerListener) {
		this.pointerListener = pointerListener;
	}

	/*
	 * 
	 * Dobbiamo dividere il lavoro in vari obbiettivi che porteranno al miglioramento
	 * 
	   1) Essere in grado di riconoscere se lo spartito è stato suonato nell'ordine giusto
	   	 a) Spartito semplice con SheetEntry singole ad una sola traccia
	  	 b) Spartito con due tracce ma con SheetEntry singola
	     c) Spartito con accordi e due tracce
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
	 * 
	 * Problemi creati dal cercare di risolvere questi passi:
	 *  - Per determinare se siamo di fronte ad un accordo oppure ad una nota, i callback
	 *    rispettivi vengono richiamati solo quando un'altro evento determinante fa capire
	 *    al programma che siamo di fronte o all'uno o all'altro. Per esempio:
	 *    Per capire se è un accordo o no, bisogna aspettare fino a quando non arriva una
	 *    nota dopo tanto tempo, ma fino a quando non arriva non c'è modo di sapere se è
	 *    un accordo o meno.
	 *    Questo porta anche un problema a livello grafico, siccome il pointer dello
	 *    spartito non va avanti finché non riceve un nuovo evento, e creerebbe confusione
	 *    nell'utente.
	 *    Le possibili soluzioni sarebbero:
	 *     1) Dopo 80tick (80 è il numero determinante per un accordo) passati dal elemento
	 *        a indice 0 del chordStack, sicuramente non possiamo avere un ulteriore
	 *        aggiunta o aggiornamento dell'accordo quindi prenderemo quello che c'è.
	 *        Se è una sola sarà una nota, altrimenti un accordo.
	 *        Il problema è che essendo in 2 thread separati potrebbe esserci una sfiga tale
	 *        che viene premuta una nota mentre il chordStack viene usato per fare sta roba
	 *        e sarebbe un casino assurdo. forse funziona con synchronized(obj) {} ma non so
	 *        come si usa, devo studiarmelo
	 *     2) Usare i onMidiOff() callback per capire quando hai finito di suonare un
	 *        determinato accordo o nota.
	 *        Problema: è difficile da constatare che cazzo stai cercando di far finire.
	 * 
	 */
	
	@Override
	public void run() {
		while(true);
	}
	
	public void next() {
		pointer++;
		if(pointerListener != null) pointerListener.onPointerChange(pointer);
	}
	
	ArrayList<Pair<Note, Long>> chord = new ArrayList<Pair<Note, Long>>();
	
	public long getChordDelta() {
		return chord.get(chord.size()-1).t2 - chord.get(0).t2;
	}
	
	@Override
	public void onSingleNote(LiveNote note) {
		System.out.println("checking NOTE: " + note + " tick:" + note.getTimeStamp() + " delta:" + note.getDelta());
		
		if(pointer == sheet.size()) {
			System.out.println("Hai completato lo spartito!");
			return;
		}
		
		if(sheet.getEntry(pointer).contains(note)) {
			System.out.println("yes, " + note);
			next();
		} else System.out.println("no. retry");
	}

	@Override
	public void onChord(LiveChord chord) {
		System.out.println("checking CHORD: " + chord);
		
		if(pointer == sheet.size()) {
			System.out.println("Hai completato lo spartito!");
			return;
		}
		
		if(sheet.getEntry(pointer).checkChord(chord)) {
			System.out.println("yes, " + chord);
			next();
		} else System.out.println("no. retry");
	}
	
	@Override
	public void onMidiOn(Note note, long timeStamp, long delta) {
		if(note.getKey() == 108) System.exit(0);
		//System.out.println("ON " + note + " tick:" + timeStamp + " delta:" + delta);
		
		/*if(pointer == sheet.size()) {
			System.out.println("Hai completato lo spartito!");
			return;
		}
		
		boolean checkSingleNote = false;
		
		chord.add(new Pair<Note, Long>(note, timeStamp));
		if(getChordDelta() > 80) {
			if(chord.size() > 2) {	// accordo trovato
				// non rimuovo l'ultimo perché magari ci sono due accordi di fila
				
				ArrayList<Note> c = new ArrayList<Note>();
				for(int i = 0; i < chord.size()-1; i++)	// tutti tranne l'ultimo
					c.add(chord.remove(i).t1);	// rimuovo da chord e aggiungo a c
				
				if(sheet.getEntry(pointer).checkChord(c)) {
					System.out.println("yes CHORD, " + sheet.getEntry(pointer));
					next();
				}
				
			} else {	// se ce ne sono 2, e abbiamo superato il delta massimo di 80tick, allora sicuramente non abbiamo un accordo
				chord.remove(0);	// quindi togliamo solo il precedente così magari questo appena aggiunto fa parte di un accordo
				checkSingleNote = true;
			}
		} else checkSingleNote = true;
		
		if(checkSingleNote) if(sheet.getEntry(pointer).contains(note)) {
			System.out.println("yes, " + note);
			next();
		} else System.out.println("no. retry");
		
		System.out.println("currentChord: " + getChordDelta() + "tick");
		for(int i = 0; i < chord.size(); i++) {
			System.out.print(chord.get(i).t1 + " tick: " + chord.get(i).t2 + " - ");
		}
		System.out.println();*/
		
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
