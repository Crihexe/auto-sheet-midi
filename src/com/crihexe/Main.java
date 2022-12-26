package com.crihexe;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.Timer;

import com.crihexe.gui.GUI;
import com.crihexe.live.Device;
import com.crihexe.live.LiveEngine;
import com.crihexe.sheet.SheetBuilder;

public class Main {

	public static void main(String[] args) throws Exception {
		
		/*MidiDevice inputDevice = MidiSystem.getMidiDevice(Device.deviceList()[5]);
		
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
		
		sequencer.startRecording();*/
		
		GUI gui = new GUI();
		gui.show();
		
		Sequence fileSeq = MidiSystem.getSequence(new File("multiple_2track.mid"));
		SheetBuilder sb = new SheetBuilder("multiple2track", fileSeq.getTracks());
		sb.build();
		
		gui.getView().setSheet(sb.result());
		
		LiveEngine engine = new LiveEngine(sb.result());
		engine.setSheetPointerListener(gui.getView());
		
		for(int i = 0; i < Device.deviceList().length; i++) {
			System.out.println(Device.getInfoDetails(Device.deviceList()[i]));
		}
		Device.selectDevice(Device.deviceList()[5]);
		engine.start();
		
		new Timer(10000, e -> {
			//System.exit(0);
		}).start();
		
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
