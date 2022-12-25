package com.crihexe.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class GUI {
	
	public static final String WINDOW_TITLE = "Auto Sheet Midi";
	
	private static int WINDOW_WIDTH = 1280;
	private static int WINDOW_HEIGHT = 720;
	
	private JFrame frame;
	private View view;
	
	/*
	 * 
	 * jframe, jpanel e in generale AWT sono un botto lenti
	 * quindi per ora farò una bozza di visualizzazione ma dovrò
	 * refactorarla penso (ma spero di no) in opengl
	 * 
	 * tipo ora devo lasciare la grandezza della finestra piccolissima
	 * altrimenti ci mette mezz'ora per fare il drawImage() sul panel
	 * 
	 */
	
	public GUI() {
		view = new View();
		
		frame = GUI.generateJFrame();
		frame.setVisible(false);
		
		frame.addWindowListener(generateWindowListener());
		view.addComponentListener(generateComponentListener());
		
		frame.add(view);
		
		frame.pack();
	}
	
	public View getView() {
		return view;
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public void exit() {
		System.exit(0);
	}
	
	// custom exit operations
	private WindowListener generateWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		};
	}
	
	// auto resize
	private ComponentListener generateComponentListener() {
		return new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				WINDOW_WIDTH = e.getComponent().getWidth();
				WINDOW_HEIGHT = e.getComponent().getHeight();
				frame.getContentPane().setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
				view.resize();
			}
		};
	}
	
	public static int getWindowWidth() {
		return WINDOW_WIDTH;
	}
	public static int getWindowHeight() {
		return WINDOW_HEIGHT;
	}
	
	private static JFrame generateJFrame() {
		JFrame frame = new JFrame(WINDOW_TITLE);
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.getContentPane().setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		return frame;
	}
	
}
