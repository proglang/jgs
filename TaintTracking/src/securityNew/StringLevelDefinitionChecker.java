package securityNew;

import java.lang.annotation.Annotation;

import exception.SootException.SecurityLevelException;

public final class StringLevelDefinitionChecker extends ALevelDefinitionChecker<StringLevel, ILevelDefinition<StringLevel>> {

	public StringLevelDefinitionChecker(ILevelDefinition<StringLevel> implementation)
			throws SecurityLevelException {
		super(implementation, StringLevel.ReturnSecurity.class, StringLevel.ParameterSecurity.class);
	}

	@Override
	protected final boolean checkAdditionalValidityOfImplementation() {
		return true;
	}

	@Override
	protected final StringLevel extractLevelFromReturnAnnotation(Annotation annotation) {
		StringLevel.ReturnSecurity returnAnnotation = (StringLevel.ReturnSecurity) annotation;
		return new StringLevel(returnAnnotation.value());
	}

	@Override
	protected final StringLevel[] extractLevelsFromParameterAnnotation(
			Annotation annotation) {
		StringLevel.ParameterSecurity parameterAnnotation = (StringLevel.ParameterSecurity) annotation;
		String[] parameterLevels = parameterAnnotation.value();
		StringLevel[] levels = new StringLevel[parameterLevels.length];
		for (int i = 0; i < parameterLevels.length; i++) {
			levels[i] = new StringLevel(parameterLevels[i]);
		}
		return levels;
	}	
}
