package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import logging.SootLogger;

import security.SecurityAnnotation;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.util.Chain;
import soot.util.Switch;

public class LocalMap {
	
	public static class ExtendedLocal {
		
		private Local local = null;
		private String level = null;
		
		public ExtendedLocal(Local local) {
			super();
			this.local = local;
		}
		
		public ExtendedLocal(Local local, String level) {
			super();
			this.local = local;
			this.level = level;
		}

		public String getLevel() {
			return level;
		}
		
		public void setLevel(String level) {
			this.level = level;
		}
		
		public Local getLocal() {
			return local;
		}
		
		public List getUseBoxes() {
			return local.getUseBoxes();
		}

		public Type getType() {
			return local.getType();
		}

		public void toString(UnitPrinter up) {
			local.toString(up);
		}

		public void apply(Switch sw) {
			local.apply(sw);
		}

		public boolean equivTo(Object o) {
			return local.equivTo(o);
		}

		public int equivHashCode() {
			return local.equivHashCode();
		}

		public void setNumber(int number) {
			local.setNumber(number);
		}

		public int getNumber() {
			return local.getNumber();
		}

		public String getName() {
			return local.getName();
		}

		public void setName(String name) {
			local.setName(name);
		}

		public void setType(Type t) {
			local.setType(t);
			
		}
		
	}
	
	private Map<Local, String> localMap = new HashMap<Local, String>();
	private Stack<String> programCounter = new Stack<String>();
	private SecurityAnnotation securityAnnotation = null;
	public static int number = 0;
	private int count = 0;
	
	public String getStrongestProgramCounterLevel() {
		String resultLevel = securityAnnotation.getWeakestSecurityLevel();
		for (String pcLevel : programCounter) {
			if (securityAnnotation.isWeakerOrEqualsThan(resultLevel, pcLevel)) {
				resultLevel = pcLevel;
			}
		}
		return resultLevel;
	}
	
	public void addProgramCounterLevel(String level) {
		programCounter.push(level);
	}
	
	public void removeLastProgramCounterLevel() {
		if (!programCounter.isEmpty()) programCounter.pop();
		else System.err.println("Tries to remove but no element");
	}
	
	public boolean hasProgramCounterLevel() {
		return ! programCounter.isEmpty();
	}
	
	public LocalMap(Chain<Local> chain, SecurityAnnotation securityAnnotation) {
		super();
		this.securityAnnotation = securityAnnotation;
		addAll(convertLocals(chain), null);
		count = number++;
	}
	
	public boolean update(Local local, String level) {
		return localMap.put(local, level) != null;
	}
	
	public boolean containsLocal(Local local) {
		return localMap.containsKey(local);
	}
	
	public String getLevelOfLocal(Local local) {
		return localMap.get(local);
	}
	
	public void addAll(Collection<ExtendedLocal> locals, Stack<String> stack) {
		if (stack != null) this.programCounter = stack;
		for (ExtendedLocal extendedLocal : locals) {
			if (extendedLocal.getLevel() == null) {
				localMap.put(extendedLocal.getLocal(), securityAnnotation.getWeakestSecurityLevel());
			} else {
				localMap.put(extendedLocal.getLocal(), extendedLocal.getLevel());
			}
		}
	}
	
	public List<ExtendedLocal> getExtendedLocals() {
		List<ExtendedLocal> extendedLocals = new ArrayList<ExtendedLocal>();
		for (Local key : localMap.keySet()) {
			extendedLocals.add(new ExtendedLocal(key, localMap.get(key)));
		}
		return extendedLocals;
	}
	
	private List<ExtendedLocal> convertLocals(Collection<Local> locals) {
		List<ExtendedLocal> extendedLocals = new ArrayList<ExtendedLocal>();
		for (Local local : locals) {
			extendedLocals.add(new ExtendedLocal(local));
		}
		return extendedLocals;
	}
	
	public void clear() {
		this.localMap.clear();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        LocalMap other = (LocalMap) obj;
		return localMap.equals(other.localMap) && programCounter.equals(other.programCounter);
	}
	
	public void prettyPrint(SootLogger logger, String methodName) {
		String newline = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append("LocalMap " + count + " --> " + methodName + newline);
		for (Local local : this.localMap.keySet()) {
			sb.append(local.getName() + "^" + this.localMap.get(local) + " / ");
		}
		//logger.debug(sb.toString());
	}

	public void addAllStronger(List<ExtendedLocal> locals) {
		for (ExtendedLocal extendedLocal : locals) {
			if (extendedLocal.getLevel() == null) {
				extendedLocal.setLevel(securityAnnotation.getWeakestSecurityLevel());
			}
			if (localMap.containsKey(extendedLocal.getLocal())) {
				String existingLevel = localMap.get(extendedLocal);
				String possibleNewLevel = extendedLocal.getLevel();
				if (existingLevel == null) existingLevel = securityAnnotation.getWeakestSecurityLevel();
				if (securityAnnotation.isWeakerOrEqualsThan(existingLevel, possibleNewLevel)) {
					localMap.put(extendedLocal.getLocal(), possibleNewLevel);
				}
			}
		}
		
	}

	public Stack<String> getStack() {
		return this.programCounter;
	}
	
}
