package com.crihexe.sheet;

import java.util.ArrayList;

public class Sheet {
	
	private ArrayList<SheetEntry> entries;
	
	public Sheet() {
		entries = new ArrayList<SheetEntry>();
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
	
	public ArrayList<SheetEntry> getNearEntries(int index) {
		return (ArrayList<SheetEntry>) entries.subList(index(index-1), index(index+1));
	}
	
	private int index(int index) {
		if(index < 0) return 0;
		if(index >= entries.size()) return entries.size()-1;
		return index;
	}
	
}
