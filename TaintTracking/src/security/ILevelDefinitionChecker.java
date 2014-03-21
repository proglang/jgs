package security;

import java.util.List;

public interface ILevelDefinitionChecker {

	public List<ILevel> getLevels();

	public boolean isValid();

}
