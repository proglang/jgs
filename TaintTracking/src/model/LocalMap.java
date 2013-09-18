package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logging.SootLogger;

import security.SecurityAnnotation;
import exception.SootException.*;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.jimple.IfStmt;
import soot.util.Chain;
import soot.util.Switch;
import utils.SecurityMessages;

/**
 * 
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class LocalMap {
	
	/**
	 * 
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ExtendedLocal {
		
		/** */
		private Local local = null;
		/** */
		private String level = null;
		
		/**
		 * 
		 * @param local
		 */
		public ExtendedLocal(Local local) {
			super();
			this.local = local;
		}
		
		/**
		 * 
		 * @param local
		 * @param level
		 */
		public ExtendedLocal(Local local, String level) {
			super();
			this.local = local;
			this.level = level;
		}

		/**
		 * 
		 * @return
		 */
		public String getLevel() {
			return level;
		}
		
		/**
		 * 
		 * @param level
		 */
		public void setLevel(String level) {
			this.level = level;
		}
		
		/**
		 * 
		 * @return
		 */
		public Local getLocal() {
			return local;
		}
		
		/**
		 * 
		 * @return
		 */
		public List getUseBoxes() {
			return local.getUseBoxes();
		}

		/**
		 * 
		 * @return
		 */
		public Type getType() {
			return local.getType();
		}

		/**
		 * 
		 * @param up
		 */
		public void toString(UnitPrinter up) {
			local.toString(up);
		}

		/**
		 * 
		 * @param sw
		 */
		public void apply(Switch sw) {
			local.apply(sw);
		}

		/**
		 * 
		 * @param o
		 * @return
		 */
		public boolean equivTo(Object o) {
			return local.equivTo(o);
		}

		/**
		 * 
		 * @return
		 */
		public int equivHashCode() {
			return local.equivHashCode();
		}

		/**
		 * 
		 * @param number
		 */
		public void setNumber(int number) {
			local.setNumber(number);
		}

		/**
		 * 
		 * @return
		 */
		public int getNumber() {
			return local.getNumber();
		}

		/**
		 * 
		 * @return
		 */
		public String getName() {
			return local.getName();
		}

		/**
		 * 
		 * @param name
		 */
		public void setName(String name) {
			local.setName(name);
		}

		/**
		 * 
		 * @param t
		 */
		public void setType(Type t) {
			local.setType(t);
			
		}
		
	}
	
	/** */
	private Map<Local, String> localMap = new HashMap<Local, String>();
	/** */
	private Map<IfStmt, String> programCounter = new HashMap<IfStmt, String>();
	/** */
	private SecurityAnnotation securityAnnotation = null;
	
	/**
	 * 
	 * @return
	 */
	public String getStrongestProgramCounterLevel() {
		String resultLevel = securityAnnotation.getWeakestSecurityLevel();
		for (String pcLevel : programCounter.values()) {
			if (securityAnnotation.isWeakerOrEqualsThan(resultLevel, pcLevel)) {
				resultLevel = pcLevel;
			}
		}
		return resultLevel;
	}
	
	/**
	 * 
	 * @param ifStmt
	 * @param level
	 */
	public void addProgramCounterLevel(IfStmt ifStmt, String level) {
		programCounter.put(ifStmt, level);
	}
	
	/**
	 * 
	 * @param ifStmt
	 */
	public void removeProgramCounterLevel(IfStmt ifStmt) {
		if (!programCounter.isEmpty()) {
			if (null != programCounter.remove(ifStmt)) {
				//throw new ProgramCounterException(SecurityMessages.noSuchProgramCounter(ifStmt.toString()));
			}
		} else {
			//throw new ProgramCounterException(SecurityMessages.noProgramCounter(ifStmt.toString()));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasProgramCounterLevel() {
		return ! programCounter.isEmpty();
	}
	
	/**
	 * 
	 * @param chain
	 * @param securityAnnotation
	 */
	public LocalMap(Chain<Local> chain, SecurityAnnotation securityAnnotation) {
		super();
		this.securityAnnotation = securityAnnotation;
		addAll(convertLocals(chain), null);
	}
	
	/**
	 * 
	 * @param local
	 * @param level
	 * @return
	 */
	public boolean update(Local local, String level) {
		return localMap.put(local, level) != null;
	}
	
	/**
	 * 
	 * @param local
	 * @return
	 */
	public boolean containsLocal(Local local) {
		return localMap.containsKey(local);
	}
	
	/**
	 * 
	 * @param local
	 * @return
	 */
	public String getLevelOfLocal(Local local) {
		return localMap.get(local);
	}
	
	/**
	 * 
	 * @param locals
	 * @param programCounter
	 */
	public void addAll(Collection<ExtendedLocal> locals, Map<IfStmt, String> programCounter) {
		if (programCounter != null) this.programCounter = programCounter;
		for (ExtendedLocal extendedLocal : locals) {
			if (extendedLocal.getLevel() == null) {
				localMap.put(extendedLocal.getLocal(), securityAnnotation.getWeakestSecurityLevel());
			} else {
				localMap.put(extendedLocal.getLocal(), extendedLocal.getLevel());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ExtendedLocal> getExtendedLocals() {
		List<ExtendedLocal> extendedLocals = new ArrayList<ExtendedLocal>();
		for (Local key : localMap.keySet()) {
			extendedLocals.add(new ExtendedLocal(key, localMap.get(key)));
		}
		return extendedLocals;
	}
	
	/**
	 * 
	 * @param locals
	 * @return
	 */
	private List<ExtendedLocal> convertLocals(Collection<Local> locals) {
		List<ExtendedLocal> extendedLocals = new ArrayList<ExtendedLocal>();
		for (Local local : locals) {
			extendedLocals.add(new ExtendedLocal(local));
		}
		return extendedLocals;
	}
	
	/**
	 * 
	 */
	public void clear() {
		this.localMap.clear();
	}
	
	/**
	 * 
	 * 
	 * @param obj
	 * @return 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        LocalMap other = (LocalMap) obj;
		return localMap.equals(other.localMap) && programCounter.equals(other.programCounter);
	}

	/**
	 * 
	 * @param locals
	 */
	public void addAllStronger(List<ExtendedLocal> locals) {
		for (ExtendedLocal extendedLocal : locals) {
			if (extendedLocal.getLevel() == null) {
				extendedLocal.setLevel(securityAnnotation.getWeakestSecurityLevel());
			}
			if (localMap.containsKey(extendedLocal.getLocal())) {
				String existingLevel = localMap.get(extendedLocal.getLocal());
				String possibleNewLevel = extendedLocal.getLevel();
				if (existingLevel == null) existingLevel = securityAnnotation.getWeakestSecurityLevel();
				if (securityAnnotation.isWeakerOrEqualsThan(existingLevel, possibleNewLevel)) {
					localMap.put(extendedLocal.getLocal(), possibleNewLevel);
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public Map<IfStmt, String> getProgramCounter() {
		return this.programCounter;
	}
	
}
