package analyzer.level1.storage;

import java.util.ArrayList;
import soot.Unit;

public class UnitStore {
	
	ArrayList<Element> units = new ArrayList<Element>();

	public Unit lastPos;
	
	public void insertElement(Element e) {
		units.add(e);
	}
	
	public ArrayList<Element> getElements() {
		return units;
	}
	
	public class Element {
		Unit i;
		Unit j;
		
		public Element(Unit newUnit, Unit position) {
			i = newUnit;
			j = position;
		}
		
		public Unit getUnit() {
			return i;
		}
		
		public Unit getPosition() {
			return j;
		}
	}

	public void flush() {
		units.clear();
	}
}
