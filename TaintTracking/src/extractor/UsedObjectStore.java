package extractor;

import static resource.Messages.getMsg;
import static util.AnalysisUtils.getSignatureOfClass;
import static util.AnalysisUtils.getSignatureOfField;
import static util.AnalysisUtils.getSignatureOfMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import exception.EnvironmentNotFoundException;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * 
 */
public class UsedObjectStore {

    /**
     * DOC
     */
    private final Map<SootClass, ClassEnvironment> classes =
        new HashMap<SootClass, ClassEnvironment>();
    /**
     * DOC
     */
    private final Map<SootField, FieldEnvironment> fields =
        new HashMap<SootField, FieldEnvironment>();
    /**
     * DOC
     */
    private final Map<SootMethod, MethodEnvironment> methods =
        new HashMap<SootMethod, MethodEnvironment>();

    /**
     * DOC
     * 
     * @param sootClass
     * @return
     */
    public ClassEnvironment getClassEnvironment(SootClass sootClass) {
        if (classes.containsKey(sootClass)) {
            ClassEnvironment ce = classes.get(sootClass);
            return ce;
        }
        throw new EnvironmentNotFoundException(getMsg("exception.environment.class_env_not_found",
                                                      getSignatureOfClass(sootClass)));
    }

    /**
     * DOC
     * 
     * @param sootField
     * @return
     */
    public FieldEnvironment getFieldEnvironment(SootField sootField) {
        if (fields.containsKey(sootField)) {
            FieldEnvironment fe = fields.get(sootField);
            return fe;
        }
        throw new EnvironmentNotFoundException(getMsg("exception.environment.field_env_not_found",
                                                      getSignatureOfField(sootField)));
    }

    /**
     * DOC
     * 
     * @param sootMethod
     * @return
     */
    public MethodEnvironment getMethodEnvironment(SootMethod sootMethod) {
        if (methods.containsKey(sootMethod)) {
            MethodEnvironment me = methods.get(sootMethod);
            return me;
        }

        throw new EnvironmentNotFoundException(getMsg("exception.environment.method_env_not_found",
                                                      getSignatureOfMethod(sootMethod)));
    }

    /**
     * DOC
     * 
     * @param ce
     */
    protected void addClassEnvironment(ClassEnvironment ce) {
        classes.put(ce.getSootClass(), ce);
    }

    /**
     * DOC
     * 
     * @param fe
     */
    protected void addFieldEnvironment(FieldEnvironment fe) {
        fields.put(fe.getSootField(), fe);
    }

    /**
     * DOC
     * 
     * @param me
     */
    protected void addMethodEnvironment(MethodEnvironment me) {
        methods.put(me.getSootMethod(), me);
    }

    /**
     * DOC
     * 
     * @param sootClass
     * @return
     */
    protected boolean containsClass(SootClass sootClass) {
        return classes.containsKey(sootClass);
    }

    /**
     * DOC
     * 
     * @param sootField
     * @return
     */
    protected boolean containsField(SootField sootField) {
        return fields.containsKey(sootField);
    }

    /**
     * DOC
     * 
     * @param sootMethod
     * @return
     */
    protected boolean containsMethod(SootMethod sootMethod) {
        return methods.containsKey(sootMethod);
    }

    /**
     * DOC
     * 
     * @return
     */
    protected Set<SootClass> getClasses() {
        return classes.keySet();
    }

    /**
     * DOC
     * 
     * @return
     */
    protected Set<SootField> getFields() {
        return fields.keySet();
    }

    /**
     * DOC
     * 
     * @return
     */
    protected Set<SootMethod> getMethods() {
        return methods.keySet();
    }

}