package db;

import java.util.*;

import soot.*;
import utils.SootUtils;

import model.*;

import effect.EffectAnnotation;
import effect.EffectAnnotation.*;

/**
 * Class which provides the storing of effects with specific effect types as well as invoked methods
 * and used classes.
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public class EffectsWrapperStorage {

	/**
	 * Wrapper for storing effects which are of a single type (e.g. read effect, new effect or write
	 * effect). The class provides a map which maps the name of an effected class to a list of
	 * effects. All these effects affect the same class.
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 * @see db.EffectsWrapperStorage.Effect
	 */
	private class EffectsStorage {

		private Map<String, List<Effect>> effects;

		/**
		 * Constructor of a EffectsStorage which stores effects of the same type depending on the
		 * class which the effects affect.
		 */
		private EffectsStorage() {
			super();
			this.effects = new HashMap<String, List<Effect>>();
		}

		/**
		 * Adds an effect depending on the effected class to the storage.
		 * 
		 * @param effect
		 *            The effect which should be stored.
		 */
		private void addEffect(Effect effect) {
			if (!this.effects.containsKey(effect.getEffected())) {
				this.effects.put(effect.getEffected(), new ArrayList<Effect>());
			}
			this.effects.get(effect.getEffected()).add(effect);
		}

		/**
		 * Returns a set of all effected class names, thus the key set of the map.
		 * 
		 * @return The set of all effected classes
		 */
		private Set<String> makeEffectsSet() {
			return this.effects.keySet();
		}

		/**
		 * Returns the list of effects for a specific effected class name.
		 * 
		 * @param effected
		 *            Name of the class which is effected.
		 * @return List of effects which affect the given class.
		 */
		private List<Effect> getEffects(String effected) {
			return this.effects.get(effected);
		}

	}

	/**
	 * Abstract class which gives the possibility to wrap a Soot element which is the cause of the
	 * effect. Each subtype therefore should contain such a field. The class also defines a method
	 * which allows to get the reason as a String.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * 
	 */
	public static abstract class Cause {

		/**
		 * Abstract method which all subtype should implement. The method returns the cause of an
		 * effect as human readable String.
		 * 
		 * @return String which contains the human readable cause.
		 */
		public abstract String getCauseString();

	}

	/**
	 * Abstract class which gives the possibility to wrap a SootField which is the cause of the
	 * effect. Each subtype therefore should contain such a SootField. The class also implements a
	 * method which returns this SootField.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see db.EffectsWrapperStorage.Cause
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
	 * @see db.EffectsWrapperStorage.FieldCause
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
		 * @see db.EffectsWrapperStorage.Cause#getCauseString()
		 */
		@Override
		public String getCauseString() {
			String readableClassName = this.getSootField().getDeclaringClass().getName();
			String readableFieldName = this.getSootField().getName();
			return String.format("assignment to the field %s.%s", readableClassName,
					readableFieldName);
		}

	}

	/**
	 * Class which gives the possibility to wrap a SootField because the cause is a reference to
	 * this field. The class inherits from the abstract class FieldCause and implements the method
	 * which returns the human readable cause.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see db.EffectsWrapperStorage.FieldCause
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
		 * @see db.EffectsWrapperStorage.Cause#getCauseString()
		 */
		@Override
		public String getCauseString() {
			String readableClassName = this.getSootField().getDeclaringClass().getName();
			String readableFieldName = this.getSootField().getName();
			return String.format("reference to the field %s.%s", readableClassName,
					readableFieldName);
		}

	}

	/**
	 * Class which gives the possibility to wrap a Soot Type because the cause is a new expression
	 * of this type. The class inherits from the abstract class Cause and implements the method
	 * which returns the human readable cause.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see db.EffectsWrapperStorage.Cause
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
		 * @see db.EffectsWrapperStorage.Cause#getCauseString()
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
	 * @see db.EffectsWrapperStorage.Cause
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
		 * @see db.EffectsWrapperStorage.Cause#getCauseString()
		 */
		@Override
		public String getCauseString() {
			return String.format("effect annotations of the method %s or the invocation of this method",
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
	 * @see db.EffectsWrapperStorage.Cause
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
		 * @see db.EffectsWrapperStorage.Cause#getCauseString()
		 */
		@Override
		public String getCauseString() {
			return String.format("effect annotations of the class %s", sootClass.getName());
		}

	}

	/**
	 * The effect class represents a calculated effect to a class/object which occurs at a specific
	 * source line. Each effect has also a cause why this effect has occurred, or from where this
	 * effect was inherited.
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see db.EffectsWrapperStorage.Cause
	 */
	public static class Effect {

		private long sourceLine;
		private String effected;
		private Cause cause;

		/**
		 * Constructor of an effect that requires the class of the effected object, the source line
		 * number on which the effect occurs and also a cause.
		 * 
		 * @param effected
		 *            Class of the effected object as String.
		 * @param sourceLine
		 *            Source line where the effect occurs.
		 * @param cause
		 *            Subtype of cause which specifies the reason of this effect.
		 */
		public Effect(String effected, long sourceLine, Cause cause) {
			super();
			this.effected = effected;
			this.sourceLine = sourceLine;
			this.cause = cause;
		}

		/**
		 * Returns the source line number where the effect occurs.
		 * 
		 * @return The source line number where the effect occurs.
		 */
		public long getSourceLine() {
			return this.sourceLine;
		}

		/**
		 * Returns the effected class name as String.
		 * 
		 * @return The effected class.
		 */
		public String getEffected() {
			return this.effected;
		}

		/**
		 * Returns a subtype of cause which represents the reason of the effect.
		 * 
		 * @return The cause of the effect.
		 */
		public Cause getCause() {
			return this.cause;
		}

	}

	private Map<String, EffectsStorage> storageTable = new HashMap<String, EffectsStorage>();
	private List<MethodAnnotation> effectedMethods = new ArrayList<MethodAnnotation>();
	private List<ClassAnnotation> effectedClasses = new ArrayList<ClassAnnotation>();

	/**
	 * Constructor of an EffectsWrapperStorage object which stores effects of all effect types.
	 */
	public EffectsWrapperStorage() {
		super();
		for (String effectID : EffectAnnotation.getListOfEffectIDs()) {
			this.storageTable.put(effectID, new EffectsStorage());
		}
	}

	/**
	 * Adds a read effect to the storage.
	 * 
	 * @param effect
	 *            Read effect which should be stored.
	 */
	public void addReadEffect(Effect effect) {
		this.storageTable.get(EffectAnnotation.getIdentifier(ReadEffect.class))
				.addEffect(effect);
	}

	/**
	 * Adds a write effect to the storage.
	 * 
	 * @param effect
	 *            Write effect which should be stored.
	 */
	public void addWriteEffect(Effect effect) {
		this.storageTable.get(EffectAnnotation.getIdentifier(WriteEffect.class))
				.addEffect(effect);
	}

	/**
	 * Adds a new effect to the storage.
	 * 
	 * @param effect
	 *            New effect which should be stored.
	 */
	public void addNewEffect(Effect effect) {
		this.storageTable.get(EffectAnnotation.getIdentifier(NewEffect.class)).addEffect(
				effect);
	}

	/**
	 * Adds a given effect of the given type to the storage.
	 * 
	 * @param effectType
	 *            Type of the effect which should be stored.
	 * @param effect
	 *            Effect which should be stored.
	 */
	public void addEffect(String effectType, Effect effect) {
		if (EffectAnnotation.getListOfEffectIDs().contains(effectType)) {
			this.storageTable.get(effectType).addEffect(effect);
		}
	}

	/**
	 * Returns a set of the class names which are effected by a read effect.
	 * 
	 * @return Set containing the read effect class names.
	 */
	public Set<String> getReadEffectSet() {
		return this.storageTable.get(EffectAnnotation.getIdentifier(ReadEffect.class))
				.makeEffectsSet();
	}

	/**
	 * Returns a set of the class names which are effected by a write effect.
	 * 
	 * @return Set containing the write effect class names.
	 */
	public Set<String> getWriteEffectSet() {
		return this.storageTable.get(EffectAnnotation.getIdentifier(WriteEffect.class))
				.makeEffectsSet();
	}

	/**
	 * Returns a set of the class names which are effected by a new effect.
	 * 
	 * @return Set containing the new effect class names.
	 */
	public Set<String> getNewEffectSet() {
		return this.storageTable.get(EffectAnnotation.getIdentifier(NewEffect.class))
				.makeEffectsSet();
	}

	/**
	 * Returns a set of the class names which are effected by a the given effect type.
	 * 
	 * @param effectType
	 *            Effect type which the resulting set of effected class names should have.
	 * @return Set containing the effect class names with the given effect type.
	 */
	public Set<String> getEffectSet(String effectType) {
		if (EffectAnnotation.getListOfEffectIDs().contains(effectType)) {
			return this.storageTable.get(effectType).makeEffectsSet();
		} else {
			return new HashSet<String>();
		}
	}

	/**
	 * Returns a list of effects where each effect is a read effect and affects the given class.
	 * 
	 * @param effected
	 *            Class name which should be affected by the resulting effects.
	 * @return List of read effects affecting the given class.
	 */
	public List<Effect> getReadEffects(String effected) {
		return this.storageTable.get(EffectAnnotation.getIdentifier(ReadEffect.class))
				.getEffects(effected);
	}

	/**
	 * Returns a list of effects where each effect is a write effect and affects the given class.
	 * 
	 * @param effected
	 *            Class name which should be affected by the resulting effects.
	 * @return List of write effects affecting the given class.
	 */
	public List<Effect> getWriteEffects(String effected) {
		return this.storageTable.get(EffectAnnotation.getIdentifier(WriteEffect.class))
				.getEffects(effected);
	}

	/**
	 * Returns a list of effects where each effect is a new effect and affects the given class.
	 * 
	 * @param effected
	 *            Class name which should be affected by the resulting effects.
	 * @return List of new effects affecting the given class.
	 */
	public List<Effect> getNewEffects(String effected) {
		return this.storageTable.get(EffectAnnotation.getIdentifier(NewEffect.class))
				.getEffects(effected);
	}

	/**
	 * Returns a list of effects where each effect is of the given effect type and affects the given
	 * class.
	 * 
	 * @param effected
	 *            Class name which should be affected by the resulting effects.
	 * @param effectType
	 *            Effect type which the resulting list of effects should have.
	 * @return List of effects which affect the given class and which have the given effect type.
	 */
	public List<Effect> getEffects(String effectType, String effected) {
		if (EffectAnnotation.getListOfEffectIDs().contains(effectType)) {
			return this.storageTable.get(effectType).getEffects(effected);
		}
		return new ArrayList<Effect>();
	}

	/**
	 * Adds a MethodAnnotation object to the storage, because this method was invoked.
	 * 
	 * @param methodAnnotation
	 *            MethodAnnotation object that should be stored.
	 */
	public void storeEffectedMethod(MethodAnnotation methodAnnotation) {
		this.effectedMethods.add(methodAnnotation);
	}

	/**
	 * Adds a ClassAnnotation object to the storage, because this class was used.
	 * 
	 * @param classAnnotation
	 *            ClassAnnotation object that should be stored.
	 */
	public void storeEffectedClass(ClassAnnotation classAnnotation) {
		this.effectedClasses.add(classAnnotation);
	}

	/**
	 * Returns the stored list of invoke methods which are contained by a MethodAnnotation object.
	 * 
	 * @return List containing all invoked methods.
	 */
	public List<MethodAnnotation> getEffectedMethodAnnotations() {
		return this.effectedMethods;
	}

	/**
	 * Returns the stored list of used class which are contained by a ClassAnnotation object.
	 * 
	 * @return List containing all used classes.
	 */
	public List<ClassAnnotation> getEffectedClassAnnotations() {
		return this.effectedClasses;
	}
}
