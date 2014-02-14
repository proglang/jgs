package security;

import exception.SootException.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public interface LevelEquation {
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class Level implements LevelEquation {
		
		/** */
		String level;

		/**
		 * 
		 * @param level
		 */
		public Level(String level) {
			this.level = level;
		}

		/**
		 * 
		 * @param visitor
		 * @see security.LevelEquation#accept(security.LevelEquationVisitor)
		 */
		@Override
		public void accept(LevelEquationVisitor visitor) {
			visitor.visit(this);
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
		 * @return
		 * @see security.LevelEquation#isEquation()
		 */
		@Override
		public boolean isEquation() {
			return false;
		}

		/**
		 * 
		 * @return
		 * @see security.LevelEquation#isLevel()
		 */
		@Override
		public boolean isLevel() {
			return true;
		}

	}
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class LevelEquationCreator {
		
		/**
		 * 
		 * @param equation
		 * @return
		 * @throws InvalidLevelException
		 * @throws InvalidEquationException
		 */
		public static LevelEquation createFrom(String equation) throws InvalidLevelException, InvalidEquationException {
			equation = equation.trim();
			if (equation.equals("")) throw new InvalidLevelException("Invalid level: " + equation);
			if (equation.startsWith("max(")) {
				if (equation.charAt(equation.length() - 1) != ")".charAt(0)) throw new InvalidEquationException("Invalid max equation: " + equation);
				String inner = equation.substring(4, equation.length() - 1);
				int commaIndex = getIndexOfComma(inner);
				if (commaIndex == -1) throw new InvalidEquationException("Invalid max equation: " + equation);
				String partOne = inner.substring(0, commaIndex);
				String partTwo = inner.substring(commaIndex + 1);
				return new LevelMaxEquation(createFrom(partOne), createFrom(partTwo));
			} else if (equation.startsWith("min(")) {
				if (equation.charAt(equation.length() - 1) != ")".charAt(0)) throw new InvalidEquationException("Invalid min equation: " + equation);
				String inner = equation.substring(4, equation.length() - 1);
				int commaIndex = getIndexOfComma(inner);
				if (commaIndex == -1) throw new InvalidEquationException("Invalid min equation: " + equation);
				String partOne = inner.substring(0, commaIndex);
				String partTwo = inner.substring(commaIndex + 1);
				return new LevelMinEquation(createFrom(partOne), createFrom(partTwo));
			} else {
				return new Level(equation);
			}
		}
		
		/**
		 * 
		 * @param equation
		 * @return
		 */
		private static int getIndexOfComma(String equation) {
			int open = 0;
			int close = 0;
			for (int index = 0; index < equation.length(); index++) {
				char current = equation.charAt(index);
				if (current == "(".charAt(0)) open++;
				if (current == ")".charAt(0)) close++;
				if (current == ",".charAt(0)) {
					if (open == close) return index;
				}
			}
			return -1;
		}
	}
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class LevelMaxEquation extends LevelTwoOperantsEquation {

		/**
		 * 
		 * @param lhs
		 * @param rhs
		 */
		public LevelMaxEquation(LevelEquation lhs, LevelEquation rhs) {
			super(lhs, rhs);
		}

		/**
		 * 
		 * @param visitor
		 * @see security.LevelEquation#accept(security.LevelEquationVisitor)
		 */
		@Override
		public void accept(LevelEquationVisitor visitor) {
			this.lhs.accept(visitor);
			this.rhs.accept(visitor);
			visitor.visit(this);
		}

	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class LevelMinEquation extends LevelTwoOperantsEquation {

		/**
		 * 
		 * @param lhs
		 * @param rhs
		 */
		public LevelMinEquation(LevelEquation lhs, LevelEquation rhs) {
			super(lhs, rhs);
		}
		
		/**
		 * 
		 * @param visitor
		 * @see security.LevelEquation#accept(security.LevelEquationVisitor)
		 */
		@Override
		public void accept(LevelEquationVisitor visitor) {
			this.lhs.accept(visitor);
			this.rhs.accept(visitor);
			visitor.visit(this);
		}

	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static abstract class LevelTwoOperantsEquation implements LevelEquation {

		/** */
		protected LevelEquation lhs;
		/** */
		protected LevelEquation rhs;

		/**
		 * 
		 * @param lhs
		 * @param rhs
		 */
		public LevelTwoOperantsEquation(LevelEquation lhs, LevelEquation rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		/**
		 * 
		 * @return
		 */
		public LevelEquation getLhs() {
			return lhs;
		}

		/**
		 * 
		 * @return
		 */
		public LevelEquation getRhs() {
			return rhs;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isEquation() {
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isLevel() {
			return false;
		}

	}
	
	/** */
	public void accept(LevelEquationVisitor visitor);
	
	/** */
	public boolean isEquation();
	
	/** */
	public boolean isLevel();
}
