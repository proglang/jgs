package securityNew;

public interface ILevelConfiguration<U extends ILevel, T extends ILevelDefinition<U>> {
	
	public Class<?> getAnnotationClassOfFieldLevel();
	public Class<?> getAnnotationClassOfParameterLevels();
	public Class<?> getAnnotationClassOfReturnLevel();
	public Class<?> getAnnotationClassOfWriteEffects();
	public ILevelDefinitionChecker<U, T> getDefinitionChecker();

}
