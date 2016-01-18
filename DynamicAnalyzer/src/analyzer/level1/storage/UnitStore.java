package analyzer.level1.storage;

import soot.Unit;

import java.util.ArrayList;

public class UnitStore {
	
  ArrayList<Element> units = new ArrayList<Element>();
  
  String name = "Default UnitStore";
  
  public UnitStore(String name) {
    this.name = name;
  }
	
  public void insertElement(Element e) {
    units.add(e);
  }
	
  public ArrayList<Element> getElements() {
    return units;
  }
	
  public class Element {
    Unit unit;
    Unit position;
		
    public Element(Unit newUnit, Unit position) {
      unit = newUnit;
      this.position = position;
    }

    public Unit getUnit() {
      return unit;
    }
		
    public Unit getPosition() {
      return position;
    }
  }

  public void flush() {
    units.clear();
  }
  
  /**
   * Print all stored elements.
   */
  public void print() {
    System.out.println("Content of UnitStore '" + name + "'");
    for (Element e : units) {
      System.out.println("Unit: " + e.getUnit().toString() + ", Position: "
          + e.getPosition().toString());
    }
  }
}
