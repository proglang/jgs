package model;

import static main.AnalysisType.CONSTRAINTS;
import static main.AnalysisType.LEVELS;
import static resource.Messages.getMsg;
import static utils.AnalysisUtils.getDimension;
import static utils.AnalysisUtils.getSignatureOfField;

import java.util.ArrayList;
import java.util.List;

import logging.AnalysisLog;
import main.AnalysisType;
import security.ILevel;
import security.ILevelMediator;
import soot.SootClass;
import soot.SootField;
import exception.AnalysisTypeException;
import exception.AnnotationInvalidException;
import exception.InvalidDimensionException;
import exception.LevelInvalidException;

/**
 * <h1>Analysis environment for fields</h1>
 * 
 * The {@link FieldEnvironment} provides a environment for analyzing a
 * {@link SootField}. Therefore it extends the base analysis environment
 * {@link Environment} in order to access a logger and the security annotation.
 * The environment provides methods for getting the required annotations at the
 * field as well as at the class which declares the field, and also methods
 * which checks the validity of the levels and effects that are given by those
 * annotations. In addition the environment gives direct access to some methods
 * of the analyzed {@link SootField}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see Environment
 */
public class FieldEnvironment extends Environment {

    /**
     * The <em>write effects</em> of the class which declares the
     * {@link SootField}.
     */
    private final List<ILevel> classWriteEffects = new ArrayList<ILevel>();
    /** The <em>security level</em> of the {@link SootField}. */
    private final List<ILevel> levels = new ArrayList<ILevel>();

    /** The {@link SootField} for which this is the environment. */
    private final SootField sootField;

    private final int dimension;

    public FieldEnvironment(SootField sootField, List<ILevel> levels,
            List<ILevel> classWriteEffect, AnalysisLog log,
            ILevelMediator mediator) {
        super(log, mediator);
        this.sootField = sootField;
        this.levels.addAll(levels);
        this.classWriteEffects.addAll(classWriteEffect);
        this.dimension = getDimension(sootField.getType());
    }

    /**
     * Returns the <em>write effects</em> of the class which declares the
     * analyzed field.
     * 
     * @return The class <em>write effects</em>.
     */
    public List<ILevel> getClassWriteEffects() {
        return classWriteEffects;
    }

    /**
     * Method that returns the {@link SootClass} that declares the field for
     * which this is the environment.
     * 
     * @return The class which declares the analyzed field.
     */
    public SootClass getDeclaringSootClass() {
        return sootField.getDeclaringClass();
    }

    /**
     * Returns the <em>security level</em> of the field
     * {@link FieldEnvironment#sootField}.
     * 
     * @return The <em>security level</em> of the field.
     */
    public ILevel getLevel() {
        return getLevel(0);
    }

    public ILevel getLevel(int dim) {
        if (dim > dimension || dim >= levels.size())
            throw new InvalidDimensionException(getMsg("exception.level.field.invalid_dim_access",
                                                       dim,
                                                       getSignatureOfField(sootField)));
        return levels.get(dim);
    }

    /**
     * Method that returns the {@link SootField} for which this is the
     * environment (see {@link FieldEnvironment#sootField}).
     * 
     * @return The analyzed field.
     */
    public SootField getSootField() {
        return sootField;
    }

    /**
     * Indicates whether the {@link SootClass} which declares the analyzed field
     * is a library class.
     * 
     * @return {@code true} if the class is a library class, otherwise
     *         {@code false}.
     */
    public boolean isLibraryClass() {
        return sootField.getDeclaringClass().isJavaLibraryClass();
    }

    /**
     * TODO: documentation
     * 
     * @param type
     * 
     * @return
     */
    public void isReasonable(AnalysisType type) {
        if (type.equals(CONSTRAINTS)) {
            if (levels.size() == dimension + 1) { // some level given
                // if ((dimension == 0 && levels.size() == 1) || (dimension ==
                // levels.size() && levels.size() > 0)) { // some level given
                for (int i = 0; i < levels.size(); i++) {
                    ILevel level = levels.get(i);
                    if (!getLevelMediator().checkLevelValidity(level)) {
                        // level isn't a valid security level
                        throw new LevelInvalidException(getMsg("exception.level.field.invalid",
                                                               level.getName(),
                                                               getSignatureOfField(sootField)));
                    }
                    if (!isLibraryClass() && sootField.isStatic()
                        && sootField.isFinal()
                        && !getWeakestSecurityLevel().equals(levels)) {
                        // static final field has to be the weakest security
                        // level
                        throw new AnnotationInvalidException(getMsg("exception.level.field.public",
                                                                    level.getName(),
                                                                    getSignatureOfField(sootField)));
                    }
                }
            } else { // no level given
                throw new AnnotationInvalidException(getMsg("exception.level.field.invalid_dim_count",
                                                            getSignatureOfField(sootField),
                                                            dimension + 1,
                                                            levels.size()));
            }
        } else if (type.equals(LEVELS)) {
            if (levels.size() == 1) { // some level given
                if (!getLevelMediator().checkLevelValidity(getLevel())) {
                    // level isn't a valid security level
                    throw new LevelInvalidException(getMsg("exception.level.field.invalid",
                                                           getLevel().getName(),
                                                           getSignatureOfField(sootField)));
                }
            } else { // no level or too many given
                if (levels.size() < 1) {
                    throw new AnnotationInvalidException(getMsg("exception.level.field.no_level",
                                                                getSignatureOfField(sootField)));
                } else {
                    throw new AnnotationInvalidException(getMsg("exception.level.field.invalid_dim_count",
                                                                getSignatureOfField(sootField),
                                                                1,
                                                                levels.size()));
                }
            }
        } else {
            throw new AnalysisTypeException(getMsg("exception.analysis_type.unknown",
                                                   type.toString()));
        }
    }

    /**
     * Indicates whether the analyzed {@link SootField} is a static field (see
     * {@link FieldEnvironment#sootField}).
     * 
     * @return {@code true} if the field is a static field, otherwise
     *         {@code false}.
     */
    public boolean isStatic() {
        return sootField.isStatic();
    }

    public int getDimesion() {
        return dimension;
    }
}
