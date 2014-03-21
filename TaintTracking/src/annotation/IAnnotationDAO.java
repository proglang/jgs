package annotation;

import java.lang.annotation.Annotation;
import java.util.List;

import exception.SootException.NoSuchElementException;

public interface IAnnotationDAO {

	public List<IAnnotationDAO> getAnnotationArrayFor(String name, Class<? extends Annotation> annotationClass) throws NoSuchElementException;

	public IAnnotationDAO getAnnotationFor(String name, Class<? extends Annotation> annotationClass) throws NoSuchElementException;

	public List<Boolean> getBooleanArrayFor(String name) throws NoSuchElementException;

	public boolean getBooleanFor(String name) throws NoSuchElementException;

	public List<Byte> getByteArrayFor(String name) throws NoSuchElementException;

	public byte getByteFor(String name) throws NoSuchElementException;

	public List<Character> getCharArrayFor(String name) throws NoSuchElementException;

	public char getCharFor(String name) throws NoSuchElementException;

	public List<Class<?>> getClassArrayFor(String name) throws NoSuchElementException;

	public Class<?> getClassFor(String name) throws NoSuchElementException;

	public List<Double> getDoubleArrayFor(String name) throws NoSuchElementException;

	public double getDoubleFor(String name) throws NoSuchElementException;

	public <V extends Enum<V>> List<V> getEnumArrayFor(String name, Class<V> enumClass) throws NoSuchElementException;

	public <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass) throws NoSuchElementException;

	public List<Float> getFloatArrayFor(String name) throws NoSuchElementException;

	public float getFloatFor(String name) throws NoSuchElementException;

	public List<Integer> getIntArrayFor(String name) throws NoSuchElementException;

	public int getIntFor(String name) throws NoSuchElementException;

	public List<Long> getLongArrayFor(String name) throws NoSuchElementException;

	public long getLongFor(String name) throws NoSuchElementException;

	public List<Short> getShortArrayFor(String name) throws NoSuchElementException;

	public short getShortFor(String name) throws NoSuchElementException;

	public List<String> getStringArrayFor(String name) throws NoSuchElementException;

	public String getStringFor(String name) throws NoSuchElementException;

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
