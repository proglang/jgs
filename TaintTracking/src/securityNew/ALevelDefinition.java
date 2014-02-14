package securityNew;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public abstract class ALevelDefinition<T extends ILevel> implements
		ILevelDefinition<T> {
	
	protected final Class<T> cl;
	
	public ALevelDefinition(Class<T> cl) {
		 this.cl = cl;
	}
	
	public final Class<T> getBaseClass() {
		return this.cl;
	}

	public abstract int compare(T level1, T level2);

	public T getDefaultVariableLevel() {
		return this.getGreatesLowerBoundLevel();
	}

	public abstract T getGreatesLowerBoundLevel();

	public T getGreatestLowerBoundLevel(T level1, T level2) {
		return this.compare(level1, level2) < 0 ? level1 : level2;
	}

	public abstract T getLeastUpperBoundLevel();

	public T getLeastUpperBoundLevel(T level1, T level2) {
		return this.compare(level1, level2) > 0 ? level1 : level2;
	}

	public abstract T[] getLevels();

	public List<T> getLibraryClassWriteEffects(SootClass sootClass) {
		return new ArrayList<T>();
	}

	public T getLibraryFieldLevel(SootField sootField) {
		return this.getGreatesLowerBoundLevel();
	}

	public List<T> getLibraryMethodWriteEffects(SootMethod sootMethod) {
		return new ArrayList<T>();
	}

	public List<T> getLibraryParameterLevel(SootMethod sootMethod) {
		List<T> param = new ArrayList<T>();
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			param.add(getLeastUpperBoundLevel());
		}
		return param;
	}

	public T getLibraryReturnLevel(SootMethod sootMethod, List<T> levels) {
		if (levels.size() == 0) {
			return getGreatesLowerBoundLevel();
		} else {
			T result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = getLeastUpperBoundLevel(result, levels.get(i));
			}
			return result;
		}
	}

}
