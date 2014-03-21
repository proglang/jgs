package extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;

import resource.Configuration;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import utils.AnalysisUtils;
import exception.SootException.NoSuchElementException;

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
	private static final String EXCEPTION_NONEXISTENT_CLASS_ENV = "Class '%s' wasn't prepared for the analysis, i.e. the class environment doesn't exist.";
	/**
	 * DOC
	 */
	private static final String EXCEPTION_NONEXISTENT_FIELD_ENV = "Field '%s' wasn't prepared for the analysis, i.e. the field environment doesn't exist.";
	/**
	 * DOC
	 */
	private static final String EXCEPTION_NONEXISTENT_METHOD_ENV = "Method '%s' wasn't prepared for the analysis, i.e. the method environment doesn't exist.";
	/**
	 * DOC
	 */
	private final Map<SootClass, ClassEnvironment> classes = new HashMap<SootClass, ClassEnvironment>();
	/**
	 * DOC
	 */
	private final Map<SootField, FieldEnvironment> fields = new HashMap<SootField, FieldEnvironment>();
	/**
	 * DOC
	 */
	private final Map<SootMethod, MethodEnvironment> methods = new HashMap<SootMethod, MethodEnvironment>();

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @return
	 * @throws NoSuchElementException
	 */
	public ClassEnvironment getClassEnvironment(SootClass sootClass) throws NoSuchElementException {
		if (classes.containsKey(sootClass)) {
			ClassEnvironment ce = classes.get(sootClass);
			return ce;
		}
		throw new NoSuchElementException(String.format(EXCEPTION_NONEXISTENT_CLASS_ENV,
				AnalysisUtils.generateClassSignature(sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE)));
	}

	/**
	 * DOC
	 * 
	 * @param sootField
	 * @return
	 * @throws NoSuchElementException
	 */
	public FieldEnvironment getFieldEnvironment(SootField sootField) throws NoSuchElementException {
		if (fields.containsKey(sootField)) {
			FieldEnvironment fe = fields.get(sootField);
			return fe;
		}
		throw new NoSuchElementException(String.format(EXCEPTION_NONEXISTENT_FIELD_ENV, AnalysisUtils.generateFieldSignature(sootField,
				Configuration.FIELD_SIGNATURE_PRINT_PACKAGE, Configuration.FIELD_SIGNATURE_PRINT_TYPE,
				Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY)));
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 * @throws NoSuchElementException
	 */
	public MethodEnvironment getMethodEnvironment(SootMethod sootMethod) throws NoSuchElementException {
		if (methods.containsKey(sootMethod)) {
			MethodEnvironment me = methods.get(sootMethod);
			return me;
		}
		throw new NoSuchElementException(String.format(EXCEPTION_NONEXISTENT_METHOD_ENV, AnalysisUtils.generateMethodSignature(sootMethod,
				Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
				Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
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