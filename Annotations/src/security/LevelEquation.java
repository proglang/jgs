package security;

import exception.SootException.*;

public interface LevelEquation {
	
	public static class LevelEquationCreator {
		
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
	

	public void accept(LevelEquationVisitor visitor);
	public boolean isLevel();
	public boolean isEquation();
	

	public static abstract class LevelTwoOperantsEquation implements LevelEquation {

		protected LevelEquation lhs;
		public LevelEquation getLhs() {
			return lhs;
		}

		public LevelEquation getRhs() {
			return rhs;
		}

		protected LevelEquation rhs;

		public LevelTwoOperantsEquation(LevelEquation lhs, LevelEquation rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}
		
		public boolean isLevel() {
			return false;
		}
		
		public boolean isEquation() {
			return true;
		}

	}

	public static class LevelMaxEquation extends LevelTwoOperantsEquation {

		public LevelMaxEquation(LevelEquation lhs, LevelEquation rhs) {
			super(lhs, rhs);
		}

		@Override
		public void accept(LevelEquationVisitor visitor) {
			this.lhs.accept(visitor);
			this.rhs.accept(visitor);
			visitor.visit(this);
		}

	}

	public static class LevelMinEquation extends LevelTwoOperantsEquation {

		public LevelMinEquation(LevelEquation lhs, LevelEquation rhs) {
			super(lhs, rhs);
		}
		
		@Override
		public void accept(LevelEquationVisitor visitor) {
			this.lhs.accept(visitor);
			this.rhs.accept(visitor);
			visitor.visit(this);
		}

	}
	
	public static class Level implements LevelEquation {
		
		String level;

		public Level(String level) {
			this.level = level;
		}

		@Override
		public void accept(LevelEquationVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public boolean isLevel() {
			return true;
		}

		@Override
		public boolean isEquation() {
			return false;
		}

		public String getLevel() {
			return level;
		}

	}

}
