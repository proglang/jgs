package securityNew;

public abstract class ALevelConfiguration<U extends ILevel, T extends ILevelDefinition<U>> implements ILevelConfiguration<U,T> {

	public abstract Class<U> getClassOfLevel();
	public abstract Class<?> getAnnotationClassOfFieldLevel();
	public abstract Class<?> getAnnotationClassOfParameterLevels();
	public abstract Class<?> getAnnotationClassOfReturnLevel();
	public abstract Class<?> getAnnotationClassOfWriteEffects();
	public abstract ILevelMediator<U> getLevelMediator();

}
