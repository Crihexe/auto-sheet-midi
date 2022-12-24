package com.crihexe.sheet;

import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.crihexe.Pair;

public class SheetBuilder {
	
	private Sheet sheet;
	private Track tracks[];
	
	public SheetBuilder(Track tracks[]) {
		sheet = new Sheet();
		this.tracks = tracks;
	}
	
	public void build() {
		
		// faccio un merge di tutte le tracks
		Track track = tracks[0];
		for(int i = 1; i < tracks.length; i++)
			for(int j = 0; j < tracks[i].size(); j++) track.add(tracks[i].get(j));
		
		long lastOnTick = -1;	// così il primo if sicuro sarà false e ci sarà l'entry vuota riempita
		
		ArrayList<Pair<Note, Long>> pressedNotes = new ArrayList<Pair<Note, Long>>();
		
		sheet.add(new SheetEntry(0));	// creo un'entry che ha delta 0, che sarebbe quella iniziale, così da non dover controllare ogni volta se ne esiste almeno una
		for(int i = 0; i < track.size(); i++) {
			MidiEvent e = track.get(i);
			
			if(e.getMessage() instanceof ShortMessage sm) {
				if(sm.getCommand() == ShortMessage.NOTE_OFF || (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() == 0)) {
					
					// dobbiamo controllare che la nota che è andata off sia presente tra le note che sono attualmente premute
					for(int j = pressedNotes.size() - 1; j >= 0; j--) {	// al contrario perché è più probabile trovare note che stanno essendo premute per poco che note lunghe
						if(pressedNotes.get(j).t1.equals(Note.fromShortMessage(sm))) {	// controllo che tutti i valori siano uguali tranne la pressione del tasto
							pressedNotes.get(j).t1.setDuration(e.getTick() - pressedNotes.get(j).t2);
							break;
						}
					}
					
				} else if(sm.getCommand() == ShortMessage.NOTE_ON) {
					// prima controllo che ci sia già un'entry a questo delta
					// gli eventi arrivano in ordine, quindi non avrò mai un evento accaduto prima del precedente, al massimo possono essere accaduti contemporaneamente
					// per questo, controllo solo l'ultima entry dello spartito
					if(e.getTick() > lastOnTick+1)	// se l'ultimo tick è diverso da quello precedente abbiamo una nuova entry
						sheet.add(new SheetEntry(e.getTick() - lastOnTick));	// creiamo una nuova entry che abbia un nuovo delta
					
					// a questo punto prendiamo l'ultima entry, che potrebbe non essere sempre la stessa grazie alle scorse 2 righe
					Note note = Note.fromShortMessage(sm);
					sheet.getLast().add(note);	// e aggiungiamo una nota presa dallo ShortMessage
					
					// questa nota però non ha ancora una durata, che deve essere definita quando verrà rilasciata
					// usiamo quindi l'hashmap che ci servirà quando una nota andrà off
					pressedNotes.add(new Pair<Note, Long>(note, e.getTick()));
					
					lastOnTick = e.getTick();
				}
			}
			
		}
		
		System.out.println(sheet.toString() + "\nTicks: " + track.ticks());
		
	}
	
	public Sheet result() {
		return sheet;
	}
	
}
