package annotation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.AnalysisUtils;

public abstract class AAnnotationDAO implements IAnnotationDAO {

	protected static final Map<String, Class<?>> PRIM_CLASSES = new HashMap<String, Class<?>>();

	static {
		for (Class<?> c : new Class[] { void.class, boolean.class, byte.class, char.class, short.class, int.class, float.class, double.class,
				long.class })
			PRIM_CLASSES.put(AnalysisUtils.getJNISignature(c), c);
	}

	public abstract List<IAnnotationDAO> getAnnotationArrayFor(String name, Class<? extends Annotation> annotationClass);

	public abstract IAnnotationDAO getAnnotationFor(String name, Class<? extends Annotation> annotationClass);

	public abstract List<Boolean> getBooleanArrayFor(String name);

	public abstract boolean getBooleanFor(String name);

	public abstract List<Byte> getByteArrayFor(String name);

	public abstract byte getByteFor(String name);

	public abstract List<Character> getCharArrayFor(String name);

	public abstract char getCharFor(String name);

	public abstract List<Class<?>> getClassArrayFor(String name);

	public abstract Class<?> getClassFor(String name);

	public abstract List<Double> getDoubleArrayFor(String name);

	public abstract double getDoubleFor(String name);

	public abstract <V extends Enum<V>> List<V> getEnumArrayFor(String name, Class<V> enumClass);

	public abstract <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass);

	public abstract List<Float> getFloatArrayFor(String name);

	public abstract float getFloatFor(String name);

	public abstract List<Integer> getIntArrayFor(String name);

	public abstract int getIntFor(String name);

	public abstract List<Long> getLongArrayFor(String name);

	public abstract long getLongFor(String name);

	public abstract List<Short> getShortArrayFor(String name);

	public abstract short getShortFor(String name);

	public abstract List<String> getStringArrayFor(String name);

	public abstract String getStringFor(String name);

	public abstract boolean hasAnnotation(String name);

	public abstract boolean hasAnnotationArray(String name);

	public abstract boolean hasBoolean(String name);

	public abstract boolean hasBooleanArray(String name);

	public abstract boolean hasByte(String name);

	public abstract boolean hasByteArray(String name);

	public abstract boolean hasChar(String name);

	public abstract boolean hasCharArray(String name);

	public abstract boolean hasClass(String name);

	public abstract boolean hasClassArray(String name);

	public abstract boolean hasDouble(String name);

	public abstract boolean hasDoubleArray(String name);

	public abstract boolean hasEnum(String name);

	public abstract boolean hasEnumArray(String name);

	public abstract boolean hasFloat(String name);

	public abstract boolean hasFloatArray(String name);

	public abstract boolean hasInt(String name);

	public abstract boolean hasIntArray(String name);

	public abstract boolean hasLong(String name);

	public abstract boolean hasLongArray(String name);

	public abstract boolean hasShort(String name);

	public abstract boolean hasShortArray(String name);

	public abstract boolean hasString(String name);

	public abstract boolean hasStringArray(String name);

}
