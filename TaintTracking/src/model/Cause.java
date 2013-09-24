package model;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import utils.SootUtils;

/**
 * Abstract class which gives the possibility to wrap a Soot element which is the cause of the
 * effect. Each subtype therefore should contain such a field. The class also defines a method
 * which allows to get the reason as a String.
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public abstract class Cause {
	
	/**
	 * Abstract class which gives the possibility to wrap a SootField which is the cause of the
	 * effect. Each subtype therefore should contain such a SootField. The class also implements a
	 * method which returns this SootField.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static abstract class FieldCause extends Cause {

		private SootField sootField;

		/**
		 * Construtor of the abstract class FieldCause which allows to create a cause, where the
		 * real cause is a field (e.g. assignment or reference).
		 * 
		 * @param sootField
		 *            SootField which is the cause.
		 */
		public FieldCause(SootField sootField) {
			super();
			this.sootField = sootField;
		}

		/**
		 * Returns the SootField which is the real cause.
		 * 
		 * @return The real cause which is of type SootField.
		 */
		public SootField getSootField() {
			return this.sootField;
		}

	}

	/**
	 * Class which gives the possibility to wrap a SootField because the cause is an assignment to
	 * this field. The class inherits from the abstract class FieldCause and implements the method
	 * which returns the human readable cause.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class AssignCause extends FieldCause {

		/**
		 * Constructor of an assignment cause which requires the given SootField because to this
		 * there is an assignment, thus it is the real cause.
		 * 
		 * @param sootField
		 *            Field to which something is assigned.
		 */
		public AssignCause(SootField sootField) {
			super(sootField);
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that something assigns to the contained field.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("assignment to the field <%s>", SootUtils.generateFieldSignature(getSootField(), false, true, true));
		}

	}

	/**
	 * Class which gives the possibility to wrap a SootField because the cause is a reference to
	 * this field. The class inherits from the abstract class FieldCause and implements the method
	 * which returns the human readable cause.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ReferenceCause extends FieldCause {

		/**
		 * Constructor of a reference cause which requires the given SootField because to this there
		 * is a reference, thus it is the real cause.
		 * 
		 * @param sootField
		 *            Field to which something has a reference.
		 */
		public ReferenceCause(SootField sootField) {
			super(sootField);
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that something has a reference to the contained field.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("reference to the field <%s>", SootUtils.generateFieldSignature(getSootField(), false, true, true));
		}

	}

	/**
	 * Class which gives the possibility to wrap a Soot Type because the cause is a new expression
	 * of this type. The class inherits from the abstract class Cause and implements the method
	 * which returns the human readable cause.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class NewCause extends Cause {

		private Type type;

		/**
		 * Constructor of a new cause which requires the given Type because a new object of this
		 * type was created, thus it is the real cause.
		 * 
		 * @param type
		 *            Type of which a new object was created.
		 */
		public NewCause(Type type) {
			super();
			this.type = type;
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that a new object with the contained was created.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("instantiation of a new object with type %s", type.toString());
		}

	}

	/**
	 * Class which gives the possibility to wrap a SootMethod because the cause is an effect which
	 * is inherit from this SootMethod. The class inherits from the abstract class Cause and
	 * implements the method which returns the human readable cause as well as a method which
	 * returns the SootMethod.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class MethodCause extends Cause {

		private SootMethod sootMethod;

		/**
		 * Constructor of a method cause which requires the given SootMethod because the effect
		 * which has that cause inherits an effect of this method.
		 * 
		 * @param sootMethod
		 *            Method from which an effect was inherit.
		 */
		public MethodCause(SootMethod sootMethod) {
			super();
			this.sootMethod = sootMethod;
		}

		/**
		 * 
		 * Returns the SootMethod which is the real cause.
		 * 
		 * @return The real cause which is of type SootMethod.
		 */
		public SootMethod getSootMethod() {
			return this.sootMethod;
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that the effect is inherit from the contained method.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("effect annotations of the method <%s> or the invocation of this method",
					SootUtils.generateMethodSignature(sootMethod, false, true, true));
		}

	}

	/**
	 * Class which gives the possibility to wrap a SootClass because the cause is an effect which is
	 * inherit from this SootClass. The class inherits from the abstract class Cause and implements
	 * the method which returns the human readable cause as well as a method which returns the
	 * SootClass.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ClassCause extends Cause {

		private SootClass sootClass;

		/**
		 * Constructor of a class cause which requires the given SootClass because the effect which
		 * has that cause inherits an effect of this class.
		 * 
		 * @param sootClass
		 *            Class from which an effect was inherit.
		 */
		public ClassCause(SootClass sootClass) {
			super();
			this.sootClass = sootClass;
		}

		/**
		 * Returns the SootClass which is the real cause.
		 * 
		 * @return The real cause which is of type SootClass.
		 */
		public SootClass getSootClass() {
			return this.sootClass;
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that the effect is inherit from the contained class.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("effect annotations of the class <%s>", SootUtils.generateClassSignature(getSootClass(), false));
		}

	}
	
	/**
	 * Abstract method which all subtype should implement. The method returns the cause of an
	 * effect as human readable String.
	 * 
	 * @return String which contains the human readable cause.
	 */
	public abstract String getCauseString();

}
