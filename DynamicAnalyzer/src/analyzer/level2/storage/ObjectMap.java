package analyzer.level2.storage;

import java.util.HashMap;

import org.apache.commons.collections4.map.ReferenceIdentityMap;

import analyzer.level2.Level;

/**
 * @author koenigr
 *
 */
public class ObjectMap{
	
private ReferenceIdentityMap<Object, HashMap<String, Level>> innerMap;
private Level globalPC;
private static ObjectMap instance = null;
private Level actualReturnLevel;


private ObjectMap() {
	globalPC = Level.LOW; 
	actualReturnLevel = Level.LOW;
	innerMap = new ReferenceIdentityMap<Object, HashMap<String, Level>>();
}

public static synchronized ObjectMap getInstance() {
	if (instance == null) {
		instance = new ObjectMap();
	}
	return instance;
}
  
/**
 * Sets the global program counter to given Level
 * @param l
 */
public void setGlobalPC(Level l) {
	  globalPC = l;
}
 
/**
 * Returns the Level of the global program counter
 * @return 
 */
public Level getGlobalPC() {
	return globalPC;
}

public Level setActualReturnLevel(Level l) {
	actualReturnLevel = l;
	return actualReturnLevel;
}

public Level getActualReturnLevel() {
	return actualReturnLevel;
}
  
  /**
  * Inserts a new object and creates a new map for the objects fields.
  * This method should be called, when a new object is created.
  *  
  * @param o 
  */
  public void insertNewObject(Object o) {
	  innerMap.put(o, new HashMap<String, Level>());
  }
 
  
  /**
  * @param o
  * @param f
  * @param l
  * @return
  */ 
public Level getFieldLevel(Object o, String f) {
	return innerMap.get(o).get(f);
}

public Level setField(Object o, String f, Level l) {
	innerMap.get(o).put(f, l);
	return innerMap.get(o).get(f);
}

public Level setField(Object o, String f) {
	return setField(o, f, Level.LOW);
}
  
 /**
 * Returns the number of objects contained in the map 
 * @return
 */
public int getNumberOfElements() {
	  return innerMap.size();
  }

public boolean contains(Object o) {
	return innerMap.containsKey(o);
}

/**
 * @param o 
 * @return Returns number of fields of the given object
 */
public int getNumberOfFields(Object o) {
	return innerMap.get(o).size();
}
  
  

  
}


