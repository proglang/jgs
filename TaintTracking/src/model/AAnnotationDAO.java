package model;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.SootUtils;

import exception.SootException.NoSuchElementException;

public abstract class AAnnotationDAO implements IAnnotationDAO {
	
	protected static final String EXCEPTION_NO_SUCH_ARRAY = "Annotation does not have an array for name '%s' and kind '%c%c'";
	protected static final String EXCEPTION_NO_SUCH_ELEMENT = "Annotation does not have an element for name '%s' and kind '%c'";
	protected static final char JNI_ANNOTATION = '@';
	protected static final char JNI_ARRAY = '[';
	protected static final char JNI_BOOLEAN = 'Z';
	protected static final char JNI_BYTE = 'B';
	protected static final char JNI_CHAR = 'C';
	protected static final char JNI_CLASS = 'c';
	protected static final char JNI_DOUBLE = 'D';
	protected static final char JNI_ENUM = 'e';
	protected static final char JNI_FLOAT = 'F';
	protected static final char JNI_INT = 'I';
	protected static final char JNI_LONG = 'J';
	protected static final char JNI_SHORT = 'S';
	protected static final char JNI_STRING = 's';

	protected static final Map<String, Class<?>> PRIM_CLASSES = new HashMap<String, Class<?>>();

	static {
		for (Class<?> c : new Class[] { void.class, boolean.class, byte.class,
				char.class, short.class, int.class, float.class, double.class,
				long.class })
			PRIM_CLASSES.put(SootUtils.getJNISignature(c), c);
	}
	
	
	public abstract IAnnotationDAO getAnnotationFor(
			String name, Class<? extends Annotation> annotationClass)
			throws NoSuchElementException;

	public abstract List<IAnnotationDAO> getAnnotationArrayFor(
			String name, Class<? extends Annotation> annotationClass)
			throws NoSuchElementException;

	public abstract boolean getBooleanFor(String name) throws NoSuchElementException;

	public abstract List<Boolean> getBooleanArrayFor(String name)
			throws NoSuchElementException;

	public abstract byte getByteFor(String name) throws NoSuchElementException;

	public abstract List<Byte> getByteArrayFor(String name)
			throws NoSuchElementException;

	public abstract char getCharFor(String name) throws NoSuchElementException;

	public abstract List<Character> getCharArrayFor(String name)
			throws NoSuchElementException;

	public abstract double getDoubleFor(String name) throws NoSuchElementException;

	public abstract List<Double> getDoubleArrayFor(String name)
			throws NoSuchElementException;

	public abstract <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass)
			throws NoSuchElementException;

	public abstract <V extends Enum<V>> List<V> getEnumArrayFor(String name,
			Class<V> enumClass) throws NoSuchElementException;

	public abstract float getFloatFor(String name) throws NoSuchElementException;

	public abstract List<Float> getFloatArrayFor(String name)
			throws NoSuchElementException;

	public abstract int getIntFor(String name) throws NoSuchElementException;
	
	public abstract List<Integer> getIntArrayFor(String name)
			throws NoSuchElementException;

	public abstract Class<?> getClassFor(String name) throws NoSuchElementException;

	public abstract List<Class<?>> getClassArrayFor(String name)
			throws NoSuchElementException;

	public abstract long getLongFor(String name) throws NoSuchElementException;

	public abstract List<Long> getLongArrayFor(String name)
			throws NoSuchElementException;

	public abstract short getShortFor(String name) throws NoSuchElementException;

	public abstract List<Short> getShortArrayFor(String name)
			throws NoSuchElementException;

	public abstract String getStringFor(String name) throws NoSuchElementException;

	public abstract List<String> getStringArrayFor(String name)
			throws NoSuchElementException;

	public abstract boolean hasAnnotation(String name);

	public abstract boolean hasAnnotationArray(String name);

	public abstract boolean hasBoolean(String name);

	public abstract boolean hasBooleanArray(String name);

	public abstract boolean hasByte(String name);

	public abstract boolean hasByteArray(String name);
	
	public abstract boolean hasChar(String name);

	public abstract boolean hasCharArray(String name);

	public abstract boolean hasDouble(String name);

	public abstract boolean hasDoubleArray(String name);

	public abstract boolean hasEnum(String name);

	public abstract boolean hasEnumArray(String name);

	public abstract boolean hasFloat(String name);

	public abstract boolean hasFloatArray(String name);

	public abstract boolean hasInt(String name);

	public abstract boolean hasIntArray(String name);

	public abstract boolean hasClass(String name);

	public abstract boolean hasClassArray(String name);

	public abstract boolean hasLong(String name);

	public abstract boolean hasLongArray(String name);

	public abstract boolean hasShort(String name);

	public abstract boolean hasShortArray(String name);

	public abstract boolean hasString(String name);

	public abstract boolean hasStringArray(String name);

}
