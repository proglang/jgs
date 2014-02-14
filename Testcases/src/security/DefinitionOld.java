package security;

import security.Annotations;
import securityNew.ALevelDefinition;
import securityNew.StringLevel;

public final class DefinitionOld extends ALevelDefinition<StringLevel> {
	
	public DefinitionOld() {
		super(StringLevel.class);
	}

	private StringLevel high = new StringLevel("high");
	private StringLevel middle = new StringLevel("middle");
	private StringLevel low = new StringLevel("low");

	@Override
	public int compare(StringLevel level1, StringLevel level2) {
		if (level1.equals(level2)) {
			return 0;
		} else {
			if (level1.equals(high)) {
				return 1;
			} else { 
				if (level1.equals(low) || level2.equals(high)) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}

	@Override
	public StringLevel getGreatesLowerBoundLevel() {
		return low;
	}

	@Override
	public StringLevel getLeastUpperBoundLevel() {
		return high;
	}

	@Override
	public StringLevel[] getLevels() {
		return new StringLevel[] { low, middle, high };
	}
	
	@Annotations.ReturnSecurity("high")
	public static <T> T highId(T object) {
		return object;
	}
	
	@Annotations.ReturnSecurity("middle")
	public static <T> T middleId(T object) {
		return object;
	}

	@Annotations.ReturnSecurity("low")
	public static <T> T lowId(T object) {
		return object;
	}
	
	public static void main(String[] args) {
	
	}

}
