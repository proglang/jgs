package model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.SootException.NoSuchElementException;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import utils.SootUtils;

public class SootAnnotationDAO extends AAnnotationDAO {

	private static final String EXCEPTION_CLASS_NOT_FOUND = "The class '%s' which is used in the annontations does not exist in the class path.";
	private static final String EXCEPTION_INVALID_ANNOTATION_TYPE = "The specified annotation type '%s' does not match the type of the inner annotation provided by the outer annotation.";
	private static final String EXCEPTION_INVALID_ENUM_TYPE = "The specified enum '%s' does not match the type of the enum provided by the annotation.";

	private final Map<String, AnnotationElem> elements = new HashMap<String, AnnotationElem>();
	private final Class<? extends Annotation> type;

	public SootAnnotationDAO(Class<? extends Annotation> annotationClass,
			AnnotationTag annotation) {
		this.type = annotationClass;
		for (int i = 0; i < annotation.getNumElems(); i++) {
			AnnotationElem ae = annotation.getElemAt(i);
			this.elements.put(ae.getName(), ae);
		}
	}

	public IAnnotationDAO getAnnotationFor(String name,
			Class<? extends Annotation> annotationClass)
			throws NoSuchElementException {
		if (hasAnnotation(name)) {
			try {
				return convertAnnotation(elements.get(name), annotationClass);
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_ANNOTATION));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_ANNOTATION));
	}

	public List<IAnnotationDAO> getAnnotationArrayFor(String name,
			Class<? extends Annotation> annotationClass)
			throws NoSuchElementException {
		if (hasAnnotationArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<IAnnotationDAO> list = new ArrayList<IAnnotationDAO>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(convertAnnotation(aae.getValueAt(i),
							annotationClass));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY,
						JNI_ANNOTATION));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_ANNOTATION));
	}

	public boolean getBooleanFor(String name) throws NoSuchElementException {
		if (hasBoolean(name)) {
			try {
				return convertBoolean(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_BOOLEAN));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_BOOLEAN));
	}

	public List<Boolean> getBooleanArrayFor(String name)
			throws NoSuchElementException {
		if (hasBooleanArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Boolean> list = new ArrayList<Boolean>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Boolean(convertBoolean(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_BOOLEAN));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_BOOLEAN));
	}

	public byte getByteFor(String name) throws NoSuchElementException {
		if (hasByte(name)) {
			try {
				return convertByte(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_BYTE));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_BYTE));
	}

	public List<Byte> getByteArrayFor(String name)
			throws NoSuchElementException {
		if (hasByteArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Byte> list = new ArrayList<Byte>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Byte(convertByte(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_BYTE));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_BYTE));
	}

	public char getCharFor(String name) throws NoSuchElementException {
		if (hasChar(name)) {
			try {
				return convertChar(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_CHAR));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_CHAR));
	}

	public List<Character> getCharArrayFor(String name)
			throws NoSuchElementException {
		if (hasCharArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Character> list = new ArrayList<Character>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Character(convertChar(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_CHAR));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_CHAR));
	}

	public double getDoubleFor(String name) throws NoSuchElementException {
		if (hasDouble(name)) {
			try {
				return convertDouble(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_DOUBLE));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_DOUBLE));
	}

	public List<Double> getDoubleArrayFor(String name)
			throws NoSuchElementException {
		if (hasDoubleArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Double> list = new ArrayList<Double>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Double(convertDouble(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_DOUBLE));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_DOUBLE));
	}

	public <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass)
			throws NoSuchElementException {
		if (hasEnum(name)) {
			try {
				return convertEnum(elements.get(name), enumClass);
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_ENUM));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_ENUM));
	}

	public <V extends Enum<V>> List<V> getEnumArrayFor(String name,
			Class<V> enumClass) throws NoSuchElementException {
		if (hasEnumArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<V> list = new ArrayList<V>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(convertEnum(aae.getValueAt(i), enumClass));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_ENUM));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_ENUM));
	}

	public float getFloatFor(String name) throws NoSuchElementException {
		if (hasFloat(name)) {
			try {
				return convertFloat(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_FLOAT));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_FLOAT));
	}

	public List<Float> getFloatArrayFor(String name)
			throws NoSuchElementException {
		if (hasFloatArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Float> list = new ArrayList<Float>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Float(convertFloat(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_FLOAT));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_FLOAT));
	}

	public int getIntFor(String name) throws NoSuchElementException {
		if (hasInt(name)) {
			try {
				return convertInt(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_INT));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_INT));
	}

	public List<Integer> getIntArrayFor(String name)
			throws NoSuchElementException {
		if (hasIntArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Integer> list = new ArrayList<Integer>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Integer(convertInt(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_INT));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_INT));
	}

	public Class<?> getClassFor(String name) throws NoSuchElementException {
		if (hasClass(name)) {
			try {
				return convertClass(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_CLASS));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_CLASS));
	}

	public List<Class<?>> getClassArrayFor(String name)
			throws NoSuchElementException {
		if (hasClassArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Class<?>> list = new ArrayList<Class<?>>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(convertClass(aae.getValueAt(i)));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_CLASS));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_CLASS));
	}

	public long getLongFor(String name) throws NoSuchElementException {
		if (hasLong(name)) {			
			try {
				return convertLong(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_LONG));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_LONG));
	}

	public List<Long> getLongArrayFor(String name)
			throws NoSuchElementException {
		if (hasLongArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Long> list = new ArrayList<Long>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Long(convertLong(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_LONG));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_LONG));
	}

	public short getShortFor(String name) throws NoSuchElementException {
		if (hasShort(name)) {			
			try {
				return convertShort(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_SHORT));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_SHORT));
	}

	public List<Short> getShortArrayFor(String name)
			throws NoSuchElementException {
		if (hasShortArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<Short> list = new ArrayList<Short>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(new Short(convertShort(aae.getValueAt(i))));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_SHORT));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_SHORT));
	}

	public String getStringFor(String name) throws NoSuchElementException {
		if (hasString(name)) {			
			try {
				return convertString(elements.get(name));
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ELEMENT, name, JNI_STRING));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ELEMENT, name, JNI_STRING));
	}

	public List<String> getStringArrayFor(String name)
			throws NoSuchElementException {
		if (hasStringArray(name)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < aae.getNumValues(); i++) {
					list.add(convertString(aae.getValueAt(i)));
				}
				return list;
			} catch (ClassCastException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_STRING));
			}
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_NO_SUCH_ARRAY, name, JNI_ARRAY, JNI_STRING));
	}

	public boolean hasAnnotation(String name) {
		return containsElement(name, JNI_ANNOTATION);
	}

	public boolean hasAnnotationArray(String name) {
		return containsArray(name, JNI_ANNOTATION);
	}

	public boolean hasBoolean(String name) {
		return containsElement(name, JNI_BOOLEAN);
	}

	public boolean hasBooleanArray(String name) {
		return containsArray(name, JNI_BOOLEAN);
	}

	public boolean hasByte(String name) {
		return containsElement(name, JNI_BYTE);
	}

	public boolean hasByteArray(String name) {
		return containsArray(name, JNI_BYTE);
	}

	public boolean hasChar(String name) {
		return containsElement(name, JNI_CHAR);
	}

	public boolean hasCharArray(String name) {
		return containsArray(name, JNI_CHAR);
	}

	public boolean hasDouble(String name) {
		return containsElement(name, JNI_DOUBLE);
	}

	public boolean hasDoubleArray(String name) {
		return containsArray(name, JNI_DOUBLE);
	}

	public boolean hasEnum(String name) {
		return containsElement(name, JNI_ENUM);
	}

	public boolean hasEnumArray(String name) {
		return containsArray(name, JNI_ENUM);
	}

	public boolean hasFloat(String name) {
		return containsElement(name, JNI_FLOAT);
	}

	public boolean hasFloatArray(String name) {
		return containsArray(name, JNI_FLOAT);
	}

	public boolean hasInt(String name) {
		return containsElement(name, JNI_INT);
	}

	public boolean hasIntArray(String name) {
		return containsArray(name, JNI_INT);
	}

	public boolean hasClass(String name) {
		return containsElement(name, JNI_CLASS);
	}

	public boolean hasClassArray(String name) {
		return containsArray(name, JNI_CLASS);
	}

	public boolean hasLong(String name) {
		return containsElement(name, JNI_LONG);
	}

	public boolean hasLongArray(String name) {
		return containsArray(name, JNI_LONG);
	}

	public boolean hasShort(String name) {
		return containsElement(name, JNI_SHORT);
	}

	public boolean hasShortArray(String name) {
		return containsArray(name, JNI_SHORT);
	}

	public boolean hasString(String name) {
		return containsElement(name, JNI_STRING);
	}

	public boolean hasStringArray(String name) {
		return containsArray(name, JNI_STRING);
	}

	private boolean containsArray(String name, char kind) {
		if (containsElement(name, JNI_ARRAY)) {
			try {
				AnnotationArrayElem aae = getArrayElem(name);
				return aae.getNumValues() == 0
						|| aae.getValueAt(0).getKind() == kind;
			} catch (ClassCastException e) {
				return false;
			}
		}
		return false;
	}

	private boolean containsElement(String name, char kind) {
		return elements.containsKey(name)
				&& elements.get(name).getKind() == kind;
	}

	private SootAnnotationDAO convertAnnotation(AnnotationElem ae,
			Class<? extends Annotation> annotationClass)
			throws NoSuchElementException {
		AnnotationAnnotationElem aae = (AnnotationAnnotationElem) ae;
		AnnotationTag at = aae.getValue();
		if (at.getType().equals(SootUtils.getJNISignature(annotationClass))) {
			return new SootAnnotationDAO(annotationClass, at);
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_INVALID_ANNOTATION_TYPE, annotationClass.getName()));
	}

	private boolean convertBoolean(AnnotationElem ae) throws ClassCastException {
		AnnotationIntElem aie = (AnnotationIntElem) ae;
		return aie.getValue() > 0;
	}

	private byte convertByte(AnnotationElem ae) throws ClassCastException {
		AnnotationIntElem aie = (AnnotationIntElem) ae;
		return (byte) aie.getValue();
	}

	private char convertChar(AnnotationElem ae) throws ClassCastException {
		AnnotationIntElem aie = (AnnotationIntElem) ae;
		return (char) aie.getValue();
	}

	private double convertDouble(AnnotationElem ae) throws ClassCastException {
		AnnotationDoubleElem ade = (AnnotationDoubleElem) ae;
		return ade.getValue();
	}

	private <V extends Enum<V>> V convertEnum(AnnotationElem ae,
			Class<V> enumClass) throws NoSuchElementException,
			ClassCastException {
		AnnotationEnumElem aee = (AnnotationEnumElem) ae;
		if (SootUtils.getJNISignature(enumClass).equals(aee.getTypeName())) {
			return Enum.valueOf(enumClass, aee.getConstantName());
		}
		throw new NoSuchElementException(String.format(
				EXCEPTION_INVALID_ENUM_TYPE, enumClass.getName()));
	}

	private float convertFloat(AnnotationElem ae) throws ClassCastException {
		AnnotationFloatElem afe = (AnnotationFloatElem) ae;
		return afe.getValue();
	}

	private int convertInt(AnnotationElem ae) throws ClassCastException {
		AnnotationIntElem aie = (AnnotationIntElem) ae;
		return aie.getValue();
	}

	private Class<?> convertClass(AnnotationElem ae)
			throws NoSuchElementException, ClassCastException {
		AnnotationClassElem ace = (AnnotationClassElem) ae;
		String jni = ace.getDesc();
		return getUsedClass(jni);
	}

	private long convertLong(AnnotationElem ae) throws ClassCastException {
		AnnotationLongElem ale = (AnnotationLongElem) ae;
		return ale.getValue();
	}

	private short convertShort(AnnotationElem ae) throws ClassCastException {
		AnnotationIntElem aie = (AnnotationIntElem) ae;
		return (short) aie.getValue();
	}

	private String convertString(AnnotationElem ae) throws ClassCastException {
		AnnotationStringElem ase = (AnnotationStringElem) ae;
		return ase.getValue();
	}

	private AnnotationArrayElem getArrayElem(String name)
			throws ClassCastException {
		AnnotationArrayElem aae = (AnnotationArrayElem) elements.get(name);
		return aae;
	}

	private Class<?> getUsedClass(String jni) throws NoSuchElementException {
		if (PRIM_CLASSES.containsKey(jni)) {
			return PRIM_CLASSES.get(jni);
		} else {
			String javaPath = SootUtils.jniSignatureToJavaPath(jni);
			try {
				return Class.forName(javaPath);
			} catch (ClassNotFoundException e) {
				throw new NoSuchElementException(String.format(
						EXCEPTION_CLASS_NOT_FOUND, javaPath));
			}
		}
	}

	protected Class<? extends Annotation> getType() {
		return type;
	}

}
