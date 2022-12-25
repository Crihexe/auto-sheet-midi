package com.crihexe.live;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiDevice.Info;

public class Device {
	
	private static MidiDevice inputDevice;
	private static Transmitter transmitter;
	private static LiveReceiver receiver;
	
	public static Info[] deviceList() {
		return MidiSystem.getMidiDeviceInfo();
	}
	
	public static String getInfoDetails(Info info) {
		return info.getName() + " - " + info.getDescription();
	}
	
	public static void setReceiver(LiveReceiver lr) {
		if(receiver != null) receiver.close();
		receiver = lr;
		if(transmitter != null) 
			transmitter.setReceiver(receiver);
	}
	
	public static void selectDevice(Info device) {
		if(inputDevice != null) {	// rimuovo eventuali listener vecchi
			inputDevice.close();	// chiude automaticamente il transmitter e il receiver
		}
		
		try {
			inputDevice = MidiSystem.getMidiDevice(device);
			inputDevice.open();
			transmitter = inputDevice.getTransmitter();
			if(receiver != null) 
				transmitter.setReceiver(receiver);
		} catch(Exception e) {
			e.printStackTrace();
			inputDevice = null;
		}
	}
	
	public static boolean ready() {
		return inputDevice != null && receiver != null && transmitter != null;
	}
	
	public static MidiDevice device() {
		return inputDevice;
	}
	
}
