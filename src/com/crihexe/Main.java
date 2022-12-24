package com.crihexe;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import javax.swing.Timer;

import com.crihexe.sheet.SheetBuilder;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		for(int i = 0; i < infos.length; i++) {
			System.out.println(infos[i].getName() + " - " + infos[i].getDescription());
		}
		
		MidiDevice inputDevice = MidiSystem.getMidiDevice(infos[5]);
		
		Sequencer sequencer = MidiSystem.getSequencer();
		LiveReceiver receiver;
		Receiver recReceiver;
		Transmitter transmitter, recTransmitter;
		
		inputDevice.open();
		sequencer.open();
		
		transmitter = inputDevice.getTransmitter();
		recTransmitter = inputDevice.getTransmitter();
		receiver = new LiveReceiver();
		recReceiver = sequencer.getReceiver();
		transmitter.setReceiver(receiver);
		recTransmitter.setReceiver(recReceiver);
		
		Sequence seq = new Sequence(Sequence.PPQ, 24);
		
		Track currentTrack = seq.createTrack();
		
		sequencer.setSequence(seq);
		sequencer.setTickPosition(0);
		
		sequencer.recordEnable(currentTrack, -1);
		
		sequencer.startRecording();
		
		
		Sequence fileSeq = MidiSystem.getSequence(new File("merry.mid"));
		//System.out.println(fileSeq.getTracks().length);
		SheetBuilder sb = new SheetBuilder(fileSeq.getTracks());
		sb.build();
		
		
		//new Thread(new NoteHandler(Note.trackToNoteArray(fileSeq.getTracks()[0]))).start();
		
		/*new Timer(10000, e -> {
			
			Sequence tmp = sequencer.getSequence();
			sequencer.stopRecording();
			
			try {
				MidiSystem.write(tmp, 0, new File("midi.mid"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			SheetBuilder sb = new SheetBuilder(new Track[] {currentTrack});
			sb.build();
		}).start();*/
		
	}

}
