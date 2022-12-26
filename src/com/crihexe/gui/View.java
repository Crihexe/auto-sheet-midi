package com.crihexe.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.crihexe.live.listener.SheetPointerListener;
import com.crihexe.sheet.Note;
import com.crihexe.sheet.Sheet;

public class View extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, SheetPointerListener	 {
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	
	private BufferedImage renderImage, drawImage;
	private Graphics2D r, g;
	
	private Sheet sheet;
	private int pointer;
	
	public View() {
		super.setFocusable(true);
		super.setSize(GUI.getWindowWidth(), GUI.getWindowHeight());
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		addNotifify();
	}
	
	private void addNotifify() {
		addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public void run() {
		init();
		
		while(true) {
			update();
			draw();
			render();
		}
	}
	
	private void init() {
		drawImage = new BufferedImage(GUI.getWindowWidth(), GUI.getWindowHeight(), BufferedImage.TYPE_INT_ARGB);
		renderImage = new BufferedImage(GUI.getWindowWidth(), GUI.getWindowHeight(), BufferedImage.TYPE_INT_ARGB);
		
		g = (Graphics2D) drawImage.getGraphics();
		g.setBackground(Color.white);
		r = renderImage.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	}
	
	private void update() {
	}
	
	
	/*
	 * OVVIAMENTE TUTTO QUESTO E' COMPLETAMENTE PROVVISORIO E A PURO SCOPO
	 * DI FARE IN FRETTA MA ALLO STESSO TEMPO DARE UNO SPUNTO QUANDO LO
	 * FARO' MEGLIO
	 */
	private void draw() {
		refresh();
		
		if(sheet == null) return;
		
		int defaultPageXOffset = 100;
		int defaultPageYOffset = 100;
		
		int spaceBetweenLinesY = 10;
		int spaceBetweenStaffsY = 100; // staffs = pentagrammi
		
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1));
		for(int j = 0; j < 1; j++) {
			for(int i = 0; i < 5; i++) {
				g.drawLine(defaultPageXOffset, defaultPageYOffset + i*spaceBetweenLinesY + j*spaceBetweenStaffsY,
						   GUI.getWindowWidth()-defaultPageXOffset, defaultPageYOffset + i*spaceBetweenLinesY + j*spaceBetweenStaffsY);
			}
		}
		int lastLocalX = 0;
		int noteWidth = 14;
		int noteHeight = 9;
		// TODO | ALTAMENTE INEFFICIENTE, ma proprio di brutto
		for(int i = 0; i < sheet.size(); i++) {
			ArrayList<Note> notes = sheet.getEntry(i).getNotes();
			
			// x : 60 = duration : 455
			int spaceBetweenNotesX = (int)(60f * notes.get(0).getDuration() / 455f);
			//int noteLocalX = i*spaceBetweenNotesX;
			int noteLocalX = lastLocalX;	// ora funziona, ci ho pensato :)
			lastLocalX = noteLocalX + spaceBetweenNotesX;
			
			for(int j = 0; j < notes.size(); j++) {
				Note n = notes.get(j);
				int notesFromSheetBoundXOffset = defaultPageXOffset + 40;
				
				int noteX = notesFromSheetBoundXOffset + noteLocalX;
				
				int semiToneOffset = spaceBetweenLinesY/2;
				
				int noteC5LocalY = defaultPageYOffset + 10;	// defaultPageYOffset sarebbe un MI5
				int noteYNormalKey = getNormalKey(n);
				int noteYOffset = noteYNormalKey * semiToneOffset;
				int noteY = noteC5LocalY - noteYOffset;

				boolean additionalLine = noteYNormalKey > 4 || noteYNormalKey < -6;
				
				int noteLineHeight = 30;
				
				int noteLineXOffset = -1;
				int noteLineYOffset = 5;
				
				int noteLineLocalX = noteWidth + noteLineXOffset;
				int noteLineX = noteX + noteLineLocalX;
				
				int noteLineY = noteY + noteLineYOffset;
				
				boolean hollow = n.getDuration() > 455;
				boolean chrome = n.getDuration() <= 227;
				
				g.drawLine(noteLineX, noteLineY, 
						   noteLineX, noteLineY - noteLineHeight);
				if(chrome) g.drawLine(noteLineX, noteLineY - noteLineHeight, noteLineX + 12, noteLineY - noteLineHeight + 3);	// TEMP DA CAMBIARE E FARE MEGLIO
				if(additionalLine) {
					int noteAddLineWidth = 22;
					int noteAddLineX = (noteX + noteWidth/2) - (noteAddLineWidth/2);
					int noteAddLineY = noteY + noteHeight/2;
					g.drawLine(noteAddLineX, noteAddLineY, noteAddLineX + noteAddLineWidth, noteAddLineY);
				}
				
				g.rotate(Math.toRadians(-20), noteX + noteWidth/2, noteY + spaceBetweenLinesY/2);
				if(hollow) g.drawOval(noteX, noteY, noteWidth, noteHeight);
				else g.fillOval(noteX, noteY, noteWidth, noteHeight);
				g.rotate(Math.toRadians(20), noteX + noteWidth/2, noteY + spaceBetweenLinesY/2);
				
				// ora il pointer
				if(i == pointer && j == notes.size()-1) {	// faccio sta cazzata perché così se disegno il pointer nel for delle note ho accesso automaticamente a tutti i valori senza doverli ricalcolare
					g.setColor(new Color(49, 179, 235, 64));	// boh tipo colore azzurrino swag
					g.fillRect(noteX - 10, defaultPageYOffset - 20, noteWidth + 10 + 10, spaceBetweenLinesY*5 + 40);	// quest'ultimo 5 sta per il numero di linee in un PENTAgramma (5)
					g.setColor(Color.black);
				}
			}
			
		}
		
	}
	
	public int getNormalKey(Note note) {
		int offsets[] = {0, 0, 1, 2, 2, 3, 3, 4, 4, 5, 6, 6};
		return offsets[note.getNote()] + (7 * (note.getOctave()-5));
	}
	
	private void refresh() {
		g.clearRect(0, 0, GUI.getWindowWidth(), GUI.getWindowHeight());
	}
	
	private void render() {
		r.drawImage(drawImage, 0, 0, null);
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(renderImage, 0, 0, null);
	}
	
	public void resize() {
		super.setSize(GUI.getWindowWidth(), GUI.getWindowHeight());
		init();
	}
	
	@Override
	public void onPointerChange(int pointer) {
		this.pointer = pointer;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
