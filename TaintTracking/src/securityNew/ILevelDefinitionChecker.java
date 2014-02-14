package securityNew;

import java.util.List;


public interface ILevelDefinitionChecker<U extends ILevel, T extends ILevelDefinition<U>> {
	
	public static final String ID_SIGNATURE_PATTERN = "public static <T> T %s(T)";
	public static final String LEVELS_SIGNATURE_PATTERN = "public %s[] %s()";
	
	public List<U> getLevels();
	

}
