package security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.LevelEquation.Level;
import security.LevelEquation.LevelMaxEquation;
import security.LevelEquation.LevelMinEquation;

public interface LevelEquationVisitor {

	public void visit(Level level);
	public void visit(LevelMinEquation levelEquation);
	public void visit(LevelMaxEquation levelEquation);
	
	public static class LevelEquationValidityVistitor implements LevelEquationVisitor {
		
		//private List<String> availableLevels;
		private SecurityAnnotation securityAnnotation = null;
		private Set<String> containedLevels = new HashSet<String>(); 
		private List<String> validLevels = new ArrayList<String>();
		private LevelEquation levelEquation = null;
		private boolean valid = true;
		

		protected LevelEquationValidityVistitor(LevelEquation levelEquation, SecurityAnnotation securityAnnotation) {
			this.securityAnnotation = securityAnnotation;
			this.levelEquation = levelEquation;
		}

		@Override
		public void visit(Level level) {
			containedLevels.add(level.getLevel());
			boolean result = securityAnnotation.getAvailableLevels().contains(level.getLevel()) || level.getLevel().equals(SecurityAnnotation.VOID_LEVEL);
			if (! result) {
				valid = false;
			} else {
				validLevels.add(level.getLevel());
			}
		}

		@Override
		public void visit(LevelMinEquation levelEquation) {
			return;	
		}

		@Override
		public void visit(LevelMaxEquation levelEquation) {
			return;			
		}
		
		public List<String> getValidLevels() {
			return validLevels;
		}
		
		public boolean isValid() {
			return valid;
		}
		
		public boolean isVoidInvalid() {
			return containedLevels.size() > 1 && containedLevels.contains(SecurityAnnotation.VOID_LEVEL);
		}

		public LevelEquation getLevelEquation() {
			return levelEquation;
		}		
	}
	
	
	public static class LevelEquationCalculateVoidVisitor implements LevelEquationVisitor {
		
		private Set<String> containedLevels = new HashSet<String>();
		
		protected LevelEquationCalculateVoidVisitor() {
			super();
		}

		@Override
		public void visit(Level level) {
			containedLevels.add(level.getLevel());			
		}

		@Override
		public void visit(LevelMinEquation levelEquation) {
			return;			
		}

		@Override
		public void visit(LevelMaxEquation levelEquation) {
			return;			
		}
		
		public boolean isValid() {
			return containedLevels.size() == 1 && containedLevels.contains(SecurityAnnotation.VOID_LEVEL);
		}
		
	}
	
	public static class LevelEquationEvaluationVisitor implements LevelEquationVisitor {
		
		private Map<String, String> translateMap = new HashMap<String, String>();
		private SecurityAnnotation securityAnnotation = null;
		private List<String> resultingLevel = new ArrayList<String>();

		protected LevelEquationEvaluationVisitor(List<String> argumentLevels,
				List<String> parameterLevels, SecurityAnnotation securityAnnotation) {
			this.securityAnnotation = securityAnnotation;
			if (parameterLevels.size() == argumentLevels.size()) {
				for (int i = 0; i < parameterLevels.size(); i++) {
					String parameterLevel = parameterLevels.get(i);
					String argumentLevel = argumentLevels.get(i);
					if (parameterLevel.startsWith(SecurityAnnotation.LEVEL_PATTERN_SIGN)) {
						translateMap.put(parameterLevel, argumentLevel);
					}					
				}
			} else {
				System.out.println("INIT ERR");
				// TODO throw exception
			}
		}

		@Override
		public void visit(Level level) {
			String lev = level.getLevel();
			if (lev.startsWith(SecurityAnnotation.LEVEL_PATTERN_SIGN)) {
				String trans = translateMap.get(lev);
				resultingLevel.add(trans);
			} else {
				resultingLevel.add(lev);
			}		
		}

		@Override
		public void visit(LevelMinEquation levelEquation) {
			if (resultingLevel.size() == 2) {
				String result = securityAnnotation.getMinLevel(resultingLevel.get(0), resultingLevel.get(1));
				resultingLevel.clear();
				resultingLevel.add(result);
			} else {
				// TODO throw exception
				System.out.println("MIN ERR");
			}
		}

		@Override
		public void visit(LevelMaxEquation levelEquation) {
			if (resultingLevel.size() == 2) {
				String result = securityAnnotation.getMaxLevel(resultingLevel.get(0), resultingLevel.get(1));
				resultingLevel.clear();
				resultingLevel.add(result);
			} else {
				// TODO throw exception
				System.out.println("MAX ERR");
			}
			
		}
		
		public String getResultLevel() {
			if (resultingLevel.size() == 1) {
				return resultingLevel.get(0);
			} else {
				// TODO throw exception
				System.out.println("NONE ERR");
				return null;
			}
		}
		
	}
}
