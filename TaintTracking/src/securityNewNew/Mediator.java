package securityNewNew;

import java.util.List;

import securityNewNew.ILevel;

public class Mediator extends ALevelMediator {

	public Mediator(ILevelDefinition definition) {
		super(definition);
	}

	@Override
	public boolean checkParameterLevelsValidity(List<ILevel> levels) {
		return checkLevelsValidity(levels);
	}

	@Override
	public List<ILevel> getInvalidParameterLevels(List<ILevel> levels) {
		return getInvalidLevels(levels);
	}

}
