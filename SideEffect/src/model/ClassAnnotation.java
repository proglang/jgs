package model;

import logging.SideEffectLogger;
import soot.SootClass;

/**
 * Container class for SootClasses that allows accessing and storing existing effect annotations of
 * this SootClass. The ClassAnnotation inherits from the class ElementAnnotation where <T> is the
 * type SootClass. Additionally this container stores at which source line the class occurs (
 * {@link ElementAnnotation#sourceLine}) and also which annotation types are present (
 * {@link ElementAnnotation#presentAnnotations}). The single effects are stored in a Map where the
 * key is the effect identifier and the value is always a Set of String which represent a single
 * effect.
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see ElementAnnotation
 */
public class ClassAnnotation extends ElementAnnotation<SootClass> {

	/**
	 * Constructor of the ClassAnnotation class, that automatically calculates the effect
	 * annotations of the given SootClass. Thus, after the initialization all effects of this
	 * SootClass are via the contained map available.
	 * 
	 * @param sootClass
	 *            SootClass for which this container should calculated the effects.
	 * @param sourceLine
	 *            Source line where this class occurs in the source code.
	 * @param log
	 *            Logger which outputs any errors during the initialization.
	 */
	public ClassAnnotation(SootClass sootClass, long sourceLine, SideEffectLogger log) {
		super();
		this.element = sootClass;
		this.sourceLine = sourceLine;
		super.extractEffectAnnotationsOfElement(this, log);
	}

	/**
	 * Returns the contained SootClass for which the effects are calculated by extracting the
	 * annotations.
	 * 
	 * @return The SootClass contained by this container.
	 */
	public SootClass getSootClass() {
		return this.element;
	}

	/**
	 * Checks whether the contained SootClass is part of the Java library.
	 * 
	 * @return {@code true} if the contained SootClass is a part of the Java Library, otherwise
	 *         {@code false}.
	 * @see model.ElementAnnotation#isLibrary()
	 */
	@Override
	public boolean isLibrary() {
		return this.element.isJavaLibraryClass();
	}
}
