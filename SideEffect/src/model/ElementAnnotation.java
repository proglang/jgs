package model;

import java.util.*;

import logging.SideEffectLogger;

import effect.EffectAnnotation;

import soot.tagkit.AbstractHost;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;

/**
 * Abstract class of a container for SootClasses or SootMethods that allows accessing and storing
 * existing effect annotations of the object. Therefore the object has an element (
 * {@link ElementAnnotation#element}) of type T which should be either a SootClass or a SootMethod.
 * Additionally this container stores at which source line the method or class occurs (
 * {@link ElementAnnotation#sourceLine}) and also which annotation types are present (
 * {@link ElementAnnotation#presentAnnotations}). The single effects are stored in a Map where the
 * key is the effect identifier and the value is always a Set of String which represent a single
 * effect. In order to distinguish whether the contained method or class is a component of the Java
 * Library all subclasses have to declare a method {@link ElementAnnotation#isLibrary()}.
 * 
 * @author Thomas Vogel
 * @version 0.1
 * 
 * @param <T>
 *            Type T should be either a SootClass or a SootMethod.
 */
public abstract class ElementAnnotation<T extends AbstractHost> {

	private static final List<String> EFFECT_TYPES = EffectAnnotation.getListOfEffectTags();
	private static final String VISIBILITY_ANNOTATION_TAG = "VisibilityAnnotationTag";

	protected T element = null;
	protected Set<String> presentAnnotations = new HashSet<String>();
	protected Map<String, Set<String>> effectsMap = new HashMap<String, Set<String>>();
	protected long sourceLine = 0;

	/**
	 * The source line where the contained element occurs.
	 * 
	 * @return The source line.
	 */
	public long getSourceLine() {
		return sourceLine;
	}

	/**
	 * Sets the source line where the contained element occurs.
	 * 
	 * @param sourceLine
	 *            The number to which the source line should be set.
	 */
	public void setSourceLine(long sourceLine) {
		this.sourceLine = sourceLine;
	}

	/**
	 * Abstract method, which checks whether this element is part of the Java library.
	 * 
	 * @return {@code true} if the contained element is a part of the Java Library, otherwise
	 *         {@code false}.
	 */
	public abstract boolean isLibrary();

	/**
	 * Fetches the List of Tag from the contained element.
	 * 
	 * @return List of Tags of the element.
	 */
	public List<Tag> getTags() {
		return this.element.getTags();
	}

	/**
	 * Returns the list of the present annotations. Means that the resulting list contains all
	 * effect identifier for which an annotation is present.
	 * 
	 * @return List of effect identifier for which annotations are present.
	 */
	public Set<String> getPresentAnnotations() {
		return this.presentAnnotations;
	}

	/**
	 * Adds a String (a valid effect identifier) to the list which stores the available annotations
	 * of the contained element.
	 * 
	 * @param effectID
	 *            The effect identifier which should be added to the present annotation list.
	 */
	public void addPresentAnnotation(String effectID) {
		ensureInstantiationOfPresentAnnotations();
		this.presentAnnotations.add(effectID);
	}

	/**
	 * Ensures that the Set for the present annotations is initialized.
	 */
	private void ensureInstantiationOfPresentAnnotations() {
		if (this.presentAnnotations == null)
			this.presentAnnotations = new HashSet<String>();
	}

	/**
	 * Returns the whole map of effects. This map has as keys an effect identifier and as value a
	 * set of String, which are the single effects given by the annotations.
	 * 
	 * @return Map containing sets of effects.
	 */
	public Map<String, Set<String>> getEffectsMap() {
		return this.effectsMap;
	}

	/**
	 * Returns the set of Strings containing the effects which has the given effect identifier. If
	 * the map doesn't contain the given effect identifier as key the method will return an empty
	 * set.
	 * 
	 * @param effectID
	 *            The effect identifier which represents the effect type of the effects which should
	 *            be returned.
	 * @return Set of effects which have the type of the given effect identifier.
	 */
	public Set<String> getEffectsMapSetWith(String effectID) {
		Set<String> result = new HashSet<String>();
		if (this.effectsMap.containsKey(effectID))
			result.addAll(this.effectsMap.get(effectID));
		return result;
	}

	/**
	 * Adds a single effect to the set, which is available in the map using the given effect
	 * identifier.
	 * 
	 * @param effectID
	 *            The effect identifier which is the key of the map.
	 * @param effect
	 *            The effect itself represented as String.
	 */
	public void addEffectWith(String effectID, String effect) {
		ensureInstantiationOfEffectsMap();
		ensureInstantiationOfEffectsMapElement(effectID);
		this.effectsMap.get(effectID).add(effect);
	}

	/**
	 * Ensures that the Map for the Sets of effects is initialized.
	 */
	private void ensureInstantiationOfEffectsMap() {
		if (this.effectsMap == null)
			this.effectsMap = new HashMap<String, Set<String>>();
	}

	/**
	 * Ensures that the Set for the given effect identifier is initialized inside of the Map.
	 * 
	 * @param effectID
	 *            Effect identifier for which should be ensured that a Set is initialized.
	 */
	private void ensureInstantiationOfEffectsMapElement(String effectID) {
		if (!this.effectsMap.containsKey(effectID))
			this.effectsMap.put(effectID, new HashSet<String>());
	}

	/**
	 * Method that extracts the effect annotations of a SootMethod or a SootClass. For this, the
	 * method is given an appropriate container with a subtype of {@link ElementAnnotation} which
	 * either contains a SootClass or SootMethod. The extracted effect annotations are stored in
	 * this container and are thus available after the execution of this method.
	 * 
	 * @param elementStore
	 *            Container of a SootClass or a SootMethod in which also the extracted effect
	 *            annotations should be stored.
	 */
	protected void extractEffectAnnotationsOfElement(
			ElementAnnotation<? extends AbstractHost> elementStore, SideEffectLogger log) {
		List<Tag> tags = elementStore.getTags();
		Iterator<Tag> iterator = tags.iterator();
		while (iterator.hasNext()) {
			Tag current = iterator.next();
			if (current.getName().equals(VISIBILITY_ANNOTATION_TAG)) {
				VisibilityAnnotationTag visibilityAnnotationTag = (VisibilityAnnotationTag) current;
				for (AnnotationTag annotationTag : visibilityAnnotationTag.getAnnotations()) {
					if (EFFECT_TYPES.contains(annotationTag.getType())) {
						try {
							String id = EffectAnnotation.getIdentifier(EffectAnnotation
									.getClassOfSootAnnotationTag(annotationTag.getType()));
							elementStore.addPresentAnnotation(id);
							for (int i = 0; i < annotationTag.getNumElems(); i++) {
								AnnotationElem annotationElem1 = annotationTag.getElemAt(i);
								if (annotationElem1.getKind() == "[".charAt(0)) {
									AnnotationArrayElem annotationArrayElem = (AnnotationArrayElem) annotationElem1;
									for (int j = 0; j < annotationArrayElem.getNumValues(); j++) {
										AnnotationElem annotationElem2 = annotationArrayElem
												.getValueAt(j);
										if (annotationElem2.getKind() == "s".charAt(0)) {
											AnnotationStringElem annotationStringElem = (AnnotationStringElem) annotationElem2;
											elementStore.addEffectWith(id,
													annotationStringElem.getValue());

										}
									}
								}
							}
						} catch (ClassNotFoundException e) {
							log.exception("", 0, "Class of annotation type was not found.", e);
						}
					}
				}
			}
		}
	}
}
