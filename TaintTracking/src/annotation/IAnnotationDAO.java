package annotation;

import java.lang.annotation.Annotation;
import java.util.List;

public interface IAnnotationDAO {

	public List<IAnnotationDAO> getAnnotationArrayFor(String name, Class<? extends Annotation> annotationClass);

	public IAnnotationDAO getAnnotationFor(String name, Class<? extends Annotation> annotationClass);

	public List<Boolean> getBooleanArrayFor(String name);

	public boolean getBooleanFor(String name);

	public List<Byte> getByteArrayFor(String name);

	public byte getByteFor(String name);

	public List<Character> getCharArrayFor(String name);

	public char getCharFor(String name);

	public List<Class<?>> getClassArrayFor(String name);

	public Class<?> getClassFor(String name);

	public List<Double> getDoubleArrayFor(String name);

	public double getDoubleFor(String name);

	public <V extends Enum<V>> List<V> getEnumArrayFor(String name, Class<V> enumClass);

	public <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass);

	public List<Float> getFloatArrayFor(String name);

	public float getFloatFor(String name);

	public List<Integer> getIntArrayFor(String name);

	public int getIntFor(String name);

	public List<Long> getLongArrayFor(String name);

	public long getLongFor(String name);

	public List<Short> getShortArrayFor(String name);

	public short getShortFor(String name);

	public List<String> getStringArrayFor(String name);

	public String getStringFor(String name);

	public boolean hasAnnotation(String name);

	public boolean hasAnnotationArray(String name);

	public boolean hasBoolean(String name);

	public boolean hasBooleanArray(String name);

	public boolean hasByte(String name);

	public boolean hasByteArray(String name);

	public boolean hasChar(String name);

	public boolean hasCharArray(String name);

	public boolean hasClass(String name);

	public boolean hasClassArray(String name);

	public boolean hasDouble(String name);

	public boolean hasDoubleArray(String name);

	public boolean hasEnum(String name);

	public boolean hasEnumArray(String name);

	public boolean hasFloat(String name);

	public boolean hasFloatArray(String name);

	public boolean hasInt(String name);

	public boolean hasIntArray(String name);

	public boolean hasLong(String name);

	public boolean hasLongArray(String name);

	public boolean hasShort(String name);

	public boolean hasShortArray(String name);

	public boolean hasString(String name);

	public boolean hasStringArray(String name);

}
