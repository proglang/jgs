package analyzer.level1.storage;

import java.util.ArrayList;

import soot.Local;

public class LocalStore {
	
	ArrayList<Local> locals = new ArrayList<Local>();

	
	public void insertElement(Local e) {
		locals.add(e);
	}
	
	public ArrayList<Local> getElements() {
		return locals;
	}
	
}
