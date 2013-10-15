package model;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.ArrayRef;
import utils.SootUtils;

/**
 * <h1>Cause of an effect</h1>
 * 
 * Abstract class which gives the possibility to wrap a Soot element which is the cause of the
 * effect. Each subclass therefore should contain such a field. The class also defines a method
 * which allows to get the reason as a String.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public abstract class Cause {

	/**
	 * <h1>Array assignment cause</h1>
	 * 
	 * The {@link ArrayAssignCause} gives the possibility to wrap a {@link ArrayRef} because the
	 * cause is an assignment to this array. The class inherits from the abstract class
	 * {@link Cause} and implements the method which returns the human readable cause.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ArrayAssignCause extends Cause {

		/** The {@link ArrayRef} which is the real cause. */
		private ArrayRef arrayRef;

		/**
		 * Constructor of an {@link ArrayAssignCause} which requires the given {@link ArrayRef}
		 * because to this an assignment exists, thus it is the real cause.
		 * 
		 * @param arrayRef
		 *            ArrayRef to which something is assigned.
		 */
		public ArrayAssignCause(ArrayRef arrayRef) {
			super();
			this.arrayRef = arrayRef;
		}

		/**
		 * Returns an {@link ArrayRef} which is the real cause.
		 * 
		 * @return The real cause which is of type {@link ArrayRef}.
		 */
		public ArrayRef getArrayRef() {
			return this.arrayRef;
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that an assignment exits to the contained array.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("assignment to an array '%s'", arrayRef.getBase().toString());
		}

	}

	/**
	 * <h1>Assignment Cause</h1>
	 * 
	 * The {@link AssignCause} gives the possibility to wrap a {@link SootField} because the cause
	 * is an assignment to this field. The class inherits from the abstract class {@link FieldCause}
	 * and implements the method which returns the human readable cause.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class AssignCause extends FieldCause {

		/**
		 * Constructor of an {@link AssignCause} which requires the given {@link SootField} because
		 * to this an assignment exists, thus it is the real cause.
		 * 
		 * @param sootField
		 *            Field to which something is assigned.
		 */
		public AssignCause(SootField sootField) {
			super(sootField);
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that something is assigned to the contained field.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("assignment to the field <%s>",
					SootUtils.generateFieldSignature(getSootField(), false, true, true));
		}

	}

	/**
	 * <h1>Class cause</h1>
	 * 
	 * The {@link ClassCause} gives the possibility to wrap a SootClass because the cause is an
	 * effect which is inherit from this {@link SootClass}. The class inherits from the abstract
	 * class {@link Cause} and implements the method which returns the human readable cause as well
	 * as a method which returns the SootClass.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ClassCause extends Cause {

		/** {@link SootClass} which is the real cause. */
		private SootClass sootClass;

		/**
		 * Constructor of a {@link ClassCause} which requires the given {@link SootClass} because
		 * the effect which has that cause inherits an effect of this class.
		 * 
		 * @param sootClass
		 *            Class from which an effect was inherit.
		 */
		public ClassCause(SootClass sootClass) {
			super();
			this.sootClass = sootClass;
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that the effect is inherit from the contained class.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("effect annotations of the class <%s>",
					SootUtils.generateClassSignature(getSootClass(), false));
		}

		/**
		 * Returns the SootClass which is the real cause.
		 * 
		 * @return The real cause which is of type SootClass.
		 */
		public SootClass getSootClass() {
			return this.sootClass;
		}

	}

	/**
	 * <h1>Field cause</h1>
	 * 
	 * Abstract class which gives the possibility to wrap a {@link SootField} which is the cause of
	 * the effect. Each subclass therefore should contain such a SootField. The class also
	 * implements a method which returns this {@link SootField}.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static abstract class FieldCause extends Cause {

		/** {@link SootField} which is the real cause. */
		private SootField sootField;

		/**
		 * Constructor of the abstract class {@link FieldCause} which allows to create a cause,
		 * where the real cause is a field (e.g. assignment or reference).
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
		 * @return The real cause which is of type {@link SootField}.
		 */
		public SootField getSootField() {
			return this.sootField;
		}

	}

	/**
	 * <h1>Method cause</h1>
	 * 
	 * The {@link MethodCause} gives the possibility to wrap a {@link SootMethod} because the cause
	 * is an effect which is inherit from this {@link SootMethod}. The class inherits from the
	 * abstract class {@link Cause} and implements the method which returns the human readable cause
	 * as well as a method which returns the {@link SootMethod}.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class MethodCause extends Cause {

		/** {@link SootMethod} which is the real cause. */
		private SootMethod sootMethod;

		/**
		 * Constructor of a method cause which requires the given {@link SootMethod} because the
		 * effect which has that cause inherits an effect of this method.
		 * 
		 * @param sootMethod
		 *            Method from which an effect was inherit.
		 */
		public MethodCause(SootMethod sootMethod) {
			super();
			this.sootMethod = sootMethod;
		}

		/**
		 * Returns the human readable cause, means the resulting String contains the information
		 * that the effect is inherit from the contained method.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format(
					"effect annotations of the method <%s> or the invocation of this method",
					SootUtils.generateMethodSignature(sootMethod, false, true, true));
		}

		/**
		 * Returns the {@link SootMethod} which is the real cause.
		 * 
		 * @return The real cause which is of type {@link SootMethod}.
		 */
		public SootMethod getSootMethod() {
			return this.sootMethod;
		}

	}

	/**
	 * <h1>New cause</h1>
	 * 
	 * Class which gives the possibility to wrap a Soot Type because the cause is a new expression
	 * of this type. The class inherits from the abstract class Cause and implements the method
	 * which returns the human readable cause.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class NewCause extends Cause {

		/** {@link Type} which is the real cause. */
		private Type type;

		/**
		 * Constructor of a new cause which requires the given {@link Type} because a new object of
		 * this type was created, thus it is the real cause.
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
		 * that a new object of the contained {@link Type} was created.
		 * 
		 * @return Human readable cause.
		 */
		@Override
		public String getCauseString() {
			return String.format("instantiation of a new object with type %s", type.toString());
		}

		/**
		 * Returns the {@link Type} which is the real cause.
		 * 
		 * @return The real cause which is of type {@link Type}.
		 */
		public Type getType() {
			return this.type;
		}

	}

	/**
	 * <h1>Reference cause</h1>
	 * 
	 * The {@link ReferenceCause} gives the possibility to wrap a {@link SootField} because the
	 * cause is a reference to this field. The class inherits from the abstract class
	 * {@link FieldCause} and implements the method which returns the human readable cause.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class ReferenceCause extends FieldCause {

		/**
		 * Constructor of a reference cause which requires the given {@link SootField} because to
		 * this a reference exists, thus it is the real cause.
		 * 
		 * @param sootField
		 *            Field to which a reference exist.
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
			return String.format("reference to the field <%s>",
					SootUtils.generateFieldSignature(getSootField(), false, true, true));
		}

	}

	/**
	 * Abstract method which all subclasses should implement. The method returns the cause of an
	 * effect as human readable String.
	 * 
	 * @return String which contains the human readable cause of an effect.
	 */
	public abstract String getCauseString();

}
