package com.crihexe.sheet;

import java.util.ArrayList;

public class Sheet {
	
	private final String id;
	
	private ArrayList<SheetEntry> entries;
	
	public Sheet(String id) {
		this.id = id;
		entries = new ArrayList<SheetEntry>();
	}
	
	public String getID() {
		return id;
	}
	
	public void add(SheetEntry entry) {
		entries.add(entry);
	}
	
	public void add(int index, SheetEntry entry) {
		entries.add(index, entry);
	}
	
	public void removeLast() {
		entries.remove(entries.size()-1);
	}
	
	public void remove(int index) {
		entries.remove(index);
	}
	
	public void clear() {
		entries.clear();
	}
	
	public ArrayList<SheetEntry> getEntries() {
		return entries;
	}
	
	public int size() {
		return entries.size();
	}
	
	public SheetEntry getLast() {
		return entries.get(entries.size()-1);
	}
	
	public SheetEntry getEntry(int index) {
		return entries.get(index);
	}
	
	public ArrayList<SheetEntry> getNearEntries(int index) {
		return (ArrayList<SheetEntry>) entries.subList(index(index-1), index(index+1));
	}
	
	public String toString() {
		String s = "";
		long time = 0;
		for(int i = 0; i < entries.size(); i++) {
			time += entries.get(i).getDelta();
			s += entries.get(i) + "\n\n";
		}
		s += "\n\n\ntotal time: " + time;
		return s;
	}
	
	private int index(int index) {
		if(index < 0) return 0;
		if(index >= entries.size()) return entries.size()-1;
		return index;
	}
	
}
