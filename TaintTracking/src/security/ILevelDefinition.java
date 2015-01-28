package security;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import constraints.LEQConstraint;

import annotation.IAnnotationDAO;

public interface ILevelDefinition {

    public int compare(ILevel level1, ILevel level2);

    public Set<LEQConstraint> extractConstraints(IAnnotationDAO dao,
                                                 String signature);

    public List<ILevel> extractEffects(IAnnotationDAO dao);

    public List<ILevel> extractFieldLevel(IAnnotationDAO dao);

    public List<ILevel> extractParameterLevels(IAnnotationDAO dao);

    public ILevel extractReturnLevel(IAnnotationDAO dao);

    public Class<? extends Annotation> getAnnotationClassConstraints();

    public Class<? extends Annotation> getAnnotationClassEffects();

    public Class<? extends Annotation> getAnnotationClassFieldLevel();

    public Class<? extends Annotation> getAnnotationClassParameterLevel();

    public Class<? extends Annotation> getAnnotationClassReturnLevel();

    public ILevel getDefaultVariableLevel();

    public ILevel getGreatesLowerBoundLevel();

    public ILevel getGreatestLowerBoundLevel(ILevel level1, ILevel level2);

    public ILevel getLeastUpperBoundLevel();

    public ILevel getLeastUpperBoundLevel(ILevel level1, ILevel level2);

    public ILevel[] getLevels();

    public List<ILevel> getLibraryClassWriteEffects(String className);

    public Set<LEQConstraint> getLibraryConstraints(String methodName,
                                                    List<String> parameterTypes,
                                                    String returnType,
                                                    String declaringClassName,
                                                    String signature);

    public Set<LEQConstraint> getLibraryConstraints(String className);

    public List<ILevel> getLibraryFieldLevel(String fieldName,
                                             String declaringClassName,
                                             String signature,
                                             int dimension);

    public List<ILevel> getLibraryMethodWriteEffects(String methodName,
                                                     List<String> parameterTypes,
                                                     String declaringClassName,
                                                     String signature);

    public List<ILevel> getLibraryParameterLevel(String methodName,
                                                 List<String> parameterTypes,
                                                 String declaringClassName,
                                                 String signature);

    public ILevel getLibraryReturnLevel(String methodName,
                                        List<String> parameterTypes,
                                        String declaringClassName,
                                        String signature,
                                        List<ILevel> levels);

}
