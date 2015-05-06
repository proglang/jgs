package analyzer.level2.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.collections4.map.ReferenceIdentityMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;

import analyzer.level2.SecurityLevel;

/**
 * @author koenigr
 *
 */
public class ObjectMap{
	
private ReferenceIdentityMap<Object, HashMap<String, SecurityLevel>> innerMap;
private SecurityLevel globalPC;
private static ObjectMap instance = null;
private SecurityLevel actualReturnLevel;
private ArrayList<SecurityLevel> actualArguments;
private LinkedList<LocalMap> localMapStack;


private ObjectMap() {
	globalPC = SecurityLevel.LOW; 
	actualReturnLevel = SecurityLevel.LOW;
	actualArguments = new ArrayList<SecurityLevel>();
	localMapStack = new LinkedList<LocalMap>();
	innerMap = new ReferenceIdentityMap<Object, HashMap<String, SecurityLevel>>(AbstractReferenceMap.ReferenceStrength.WEAK, AbstractReferenceMap.ReferenceStrength.WEAK);
}

public static synchronized ObjectMap getInstance() {
	if (instance == null) {
		instance = new ObjectMap();
	}
	return instance;
}

public LinkedList<LocalMap> pushLocalMap(LocalMap localMap) {
	localMapStack.push(localMap);
	return localMapStack;
}

public LinkedList<LocalMap> popLocalMap() {
	localMapStack.pop();
	return localMapStack;
}

public LocalMap getLastLocalMap() {
	return localMapStack.getFirst();
}

public LinkedList<LocalMap>	getLocalMapStack() {
	return localMapStack;
}

public int sizeOfLocalMapStack() {
	return localMapStack.size();
}

public void deleteLocalMapStack() {
	localMapStack.clear();
}

public ArrayList<SecurityLevel> setActualArguments(ArrayList<SecurityLevel> args) {
	actualArguments = args;
	return actualArguments;
}

public ArrayList<SecurityLevel> getActualArguments() {
	return actualArguments;
}

public SecurityLevel getArgLevelAt(int i) {
	// TODO Fehlerbehandlung
	return actualArguments.get(i);
}
  
/**
 * Sets the global program counter to given Level
 * @param l
 */
public void setGlobalPC(SecurityLevel l) {
	  globalPC = l;
}
 
/**
 * Returns the Level of the global program counter
 * @return 
 */
public SecurityLevel getGlobalPC() {
	return globalPC;
}

public SecurityLevel setActualReturnLevel(SecurityLevel l) {
	actualReturnLevel = l;
	return actualReturnLevel;
}

public SecurityLevel getActualReturnLevel() {
	return actualReturnLevel;
}
  
  /**
  * Inserts a new object and creates a new map for the objects fields.
  * This method should be called, when a new object is created.
  *  
  * @param o 
  */
  public void insertNewObject(Object o) {
	  if (!innerMap.containsKey(o)) {
	    innerMap.put(o, new HashMap<String, SecurityLevel>());
	  }
  }
 
  
  /**
  * @param o
  * @param f
  * @param l
  * @return
  */ 
public SecurityLevel getFieldLevel(Object o, String f) {
	return innerMap.get(o).get(f);
}

public SecurityLevel setField(Object o, String f, SecurityLevel l) {
	innerMap.get(o).put(f, l);
	return innerMap.get(o).get(f);
}

public SecurityLevel setField(Object o, String f) {
	return setField(o, f, SecurityLevel.LOW);
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


