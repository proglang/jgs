package model;

import logging.SideEffectLogger;

import soot.SootMethod;

/**
 * Container class for SootMethods that allows accessing and storing existing effect annotations of
 * this SootMethod. The MethodAnnotation inherits from the class ElementAnnotation where <T> is the
 * type SootMethod. Additionally this container stores at which source line the method occurs (
 * {@link ElementAnnotation#sourceLine}) and also which annotation types are present (
 * {@link ElementAnnotation#presentAnnotations}). The single effects are stored in a Map where the
 * key is the effect identifier and the value is always a Set of String which represent a single
 * effect.
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see ElementAnnotation
 */
public class MethodAnnotation extends ElementAnnotation<SootMethod> {

	/**
	 * Constructor of the MethodAnnotation class, that automatically calculates the effect
	 * annotations of the given SootMethod. Thus, after the initialization all effects of this
	 * SootMethod are via the contained map available.
	 * 
	 * @param sootMethod
	 *            SootMethod for which this container should calculated the effects.
	 * @param sourceLine
	 *            Source line where this method occurs in the source code.
	 * @param log
	 *            Logger which outputs any errors during the initialization.
	 */
	public MethodAnnotation(SootMethod sootMethod, long sourceLine, SideEffectLogger log) {
		super();
		this.element = sootMethod;
		this.sourceLine = sourceLine;
		super.extractEffectAnnotationsOfElement(this, log);
	}

	/**
	 * Returns the contained SootMethod for which the effects are calculated by extracting the
	 * annotations.
	 * 
	 * @return The SootMethod contained by this container.
	 */
	public SootMethod getSootMethod() {
		return this.element;
	}

	/**
	 * Checks whether the contained SootMethod is part of the Java library.
	 * 
	 * @return {@code true} if the contained SootMethod is a part of the Java Library, otherwise
	 *         {@code false}.
	 * @see model.ElementAnnotation#isLibrary()
	 */
	@Override
	public boolean isLibrary() {
		return this.element.isJavaLibraryMethod();
	}
}
