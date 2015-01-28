package annotation;

import static resource.Messages.getMsg;
import static utils.ExtendedJNI.EXT_JNI_ANNOTATION;
import static utils.ExtendedJNI.EXT_JNI_CLASS;
import static utils.ExtendedJNI.JNI_ARRAY;
import static utils.ExtendedJNI.JNI_BOOLEAN;
import static utils.ExtendedJNI.JNI_BYTE;
import static utils.ExtendedJNI.JNI_CHAR;
import static utils.ExtendedJNI.JNI_DOUBLE;
import static utils.ExtendedJNI.JNI_ENUM;
import static utils.ExtendedJNI.JNI_FLOAT;
import static utils.ExtendedJNI.JNI_INT;
import static utils.ExtendedJNI.JNI_LONG;
import static utils.ExtendedJNI.JNI_SHORT;
import static utils.ExtendedJNI.JNI_STRING;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.AnnotationElementNotFoundException;

public class JavaAnnotationDAO extends AAnnotationDAO {

    private final Annotation annotation;
    private final Map<String, Method> elements = new HashMap<String, Method>();

    public JavaAnnotationDAO(Annotation annotation) {
        this.annotation = annotation;
        for (Method method : annotation.getClass().getDeclaredMethods()) {
            elements.put(method.getName(), method);
        }
    }

    @Override
    public List<IAnnotationDAO> getAnnotationArrayFor(String name,
                                                      Class<? extends Annotation> annotationClass) {
        if (hasAnnotationArray(name)) {
            try {
                List<IAnnotationDAO> list = new ArrayList<IAnnotationDAO>();
                for (Object value : getValue(name, Object[].class)) {
                    list.add(new JavaAnnotationDAO((Annotation) value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    EXT_JNI_ANNOTATION));
            }
        } else {
            throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                name,
                                                                JNI_ARRAY,
                                                                EXT_JNI_ANNOTATION));
        }
    }

    @Override
    public IAnnotationDAO getAnnotationFor(String name,
                                           Class<? extends Annotation> annotationClass) {
        if (hasAnnotation(name)) {
            try {
                return new JavaAnnotationDAO(getValue(name, annotationClass));
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    EXT_JNI_ANNOTATION));
            }
        } else {
            throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                name,
                                                                EXT_JNI_ANNOTATION));
        }
    }

    @Override
    public List<Boolean> getBooleanArrayFor(String name) {
        if (hasBooleanArray(name)) {
            try {
                List<Boolean> list = new ArrayList<Boolean>();
                for (boolean value : getValue(name, boolean[].class)) {
                    list.add(new Boolean(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_BOOLEAN));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_BOOLEAN));
    }

    @Override
    public boolean getBooleanFor(String name) {
        if (hasBoolean(name)) {
            try {
                return getValue(name, Boolean.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_BOOLEAN));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_BOOLEAN));
    }

    @Override
    public List<Byte> getByteArrayFor(String name) {
        if (hasByteArray(name)) {
            try {
                List<Byte> list = new ArrayList<Byte>();
                for (byte value : getValue(name, byte[].class)) {
                    list.add(new Byte(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_BYTE));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_BYTE));
    }

    @Override
    public byte getByteFor(String name) {
        if (hasByte(name)) {
            try {
                return getValue(name, Byte.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_BYTE));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_BYTE));
    }

    @Override
    public List<Character> getCharArrayFor(String name) {
        if (hasCharArray(name)) {
            try {
                List<Character> list = new ArrayList<Character>();
                for (char value : getValue(name, char[].class)) {
                    list.add(new Character(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_CHAR));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_CHAR));
    }

    @Override
    public char getCharFor(String name) {
        if (hasChar(name)) {
            try {
                return getValue(name, Character.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_CHAR));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_CHAR));
    }

    @Override
    public List<Class<?>> getClassArrayFor(String name) {
        if (hasClassArray(name)) {
            try {
                List<Class<?>> list = new ArrayList<Class<?>>();
                for (Class<?> value : getValue(name, Class[].class)) {
                    list.add(value);
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    EXT_JNI_CLASS));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            EXT_JNI_CLASS));
    }

    @Override
    public Class<?> getClassFor(String name) {
        if (hasClass(name)) {
            try {
                return getValue(name, Class.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    EXT_JNI_CLASS));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            EXT_JNI_CLASS));
    }

    @Override
    public List<Double> getDoubleArrayFor(String name) {
        if (hasDoubleArray(name)) {
            try {
                List<Double> list = new ArrayList<Double>();
                for (double value : getValue(name, double[].class)) {
                    list.add(new Double(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_DOUBLE));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_DOUBLE));
    }

    @Override
    public double getDoubleFor(String name) {
        if (hasDouble(name)) {
            try {
                return getValue(name, Double.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_DOUBLE));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_DOUBLE));
    }

    @Override
    public <V extends Enum<V>> List<V> getEnumArrayFor(String name,
                                                       Class<V> enumClass) {
        if (hasEnumArray(name)) {
            try {
                List<V> list = new ArrayList<V>();
                for (Enum<?> value : getValue(name, Enum[].class)) {
                    list.add(Enum.valueOf(enumClass, value.name()));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_ENUM));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_ENUM));
    }

    @Override
    public <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass) {
        if (hasEnum(name)) {
            try {
                return Enum.valueOf(enumClass,
                                    getValue(name, Enum.class).name());
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_FLOAT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_ENUM));
    }

    @Override
    public List<Float> getFloatArrayFor(String name) {
        if (hasFloatArray(name)) {
            try {
                List<Float> list = new ArrayList<Float>();
                for (float value : getValue(name, float[].class)) {
                    list.add(new Float(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_FLOAT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_FLOAT));
    }

    @Override
    public float getFloatFor(String name) {
        if (hasFloat(name)) {
            try {
                return getValue(name, Float.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_FLOAT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_FLOAT));
    }

    @Override
    public List<Integer> getIntArrayFor(String name) {
        if (hasIntArray(name)) {
            try {
                List<Integer> list = new ArrayList<Integer>();
                for (int value : getValue(name, int[].class)) {
                    list.add(new Integer(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_INT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_INT));
    }

    @Override
    public int getIntFor(String name) {
        if (hasInt(name)) {
            try {
                return getValue(name, Integer.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_INT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_INT));
    }

    @Override
    public List<Long> getLongArrayFor(String name) {
        if (hasLongArray(name)) {
            try {
                List<Long> list = new ArrayList<Long>();
                for (long value : getValue(name, long[].class)) {
                    list.add(new Long(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_LONG));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_LONG));
    }

    @Override
    public long getLongFor(String name) {
        if (hasLong(name)) {
            try {
                return getValue(name, Long.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_LONG));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_LONG));
    }

    @Override
    public List<Short> getShortArrayFor(String name) {
        if (hasShortArray(name)) {
            try {
                List<Short> list = new ArrayList<Short>();
                for (short value : getValue(name, short[].class)) {
                    list.add(new Short(value));
                }
                return list;
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_SHORT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_SHORT));
    }

    @Override
    public short getShortFor(String name) {
        if (hasShort(name)) {
            try {
                return getValue(name, Short.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_SHORT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_SHORT));
    }

    @Override
    public List<String> getStringArrayFor(String name) {
        if (hasStringArray(name)) {
            try {
                return Arrays.asList(getValue(name, String[].class));
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    JNI_STRING));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            JNI_STRING));
    }

    @Override
    public String getStringFor(String name) {
        if (hasString(name)) {
            try {
                return getValue(name, String.class);
            } catch (Exception e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_STRING));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_STRING));
    }

    @Override
    public boolean hasAnnotation(String name) {
        if (elements.containsKey(name)) {
            Method method = elements.get(name);
            return method.getReturnType().isAnnotation();
        }
        return false;
    }

    @Override
    public boolean hasAnnotationArray(String name) {
        if (elements.containsKey(name)) {
            Method method = elements.get(name);
            return method.getReturnType().isArray()
                   && method.getReturnType().getComponentType().isAnnotation();
        }
        return false;
    }

    @Override
    public boolean hasBoolean(String name) {
        return hasElement(name, boolean.class);
    }

    @Override
    public boolean hasBooleanArray(String name) {
        return hasElement(name, boolean[].class);
    }

    @Override
    public boolean hasByte(String name) {
        return hasElement(name, byte.class);
    }

    @Override
    public boolean hasByteArray(String name) {
        return hasElement(name, byte[].class);
    }

    @Override
    public boolean hasChar(String name) {
        return hasElement(name, char.class);
    }

    @Override
    public boolean hasCharArray(String name) {
        return hasElement(name, char[].class);
    }

    @Override
    public boolean hasClass(String name) {
        return hasElement(name, Class.class);
    }

    @Override
    public boolean hasClassArray(String name) {
        return hasElement(name, Class[].class);
    }

    @Override
    public boolean hasDouble(String name) {
        return hasElement(name, double.class);
    }

    @Override
    public boolean hasDoubleArray(String name) {
        return hasElement(name, double[].class);
    }

    @Override
    public boolean hasEnum(String name) {
        if (elements.containsKey(name)) {
            Method method = elements.get(name);
            return method.getReturnType().isEnum();
        }
        return false;
    }

    @Override
    public boolean hasEnumArray(String name) {
        if (elements.containsKey(name)) {
            Method method = elements.get(name);
            return method.getReturnType().isArray()
                   && method.getReturnType().getComponentType().isEnum();
        }
        return false;
    }

    @Override
    public boolean hasFloat(String name) {
        return hasElement(name, float.class);
    }

    @Override
    public boolean hasFloatArray(String name) {
        return hasElement(name, float[].class);
    }

    @Override
    public boolean hasInt(String name) {
        return hasElement(name, int.class);
    }

    @Override
    public boolean hasIntArray(String name) {
        return hasElement(name, int[].class);
    }

    @Override
    public boolean hasLong(String name) {
        return hasElement(name, long.class);
    }

    @Override
    public boolean hasLongArray(String name) {
        return hasElement(name, long[].class);
    }

    @Override
    public boolean hasShort(String name) {
        return hasElement(name, short.class);
    }

    @Override
    public boolean hasShortArray(String name) {
        return hasElement(name, short[].class);
    }

    @Override
    public boolean hasString(String name) {
        return hasElement(name, String.class);
    }

    @Override
    public boolean hasStringArray(String name) {
        return hasElement(name, String[].class);
    }

    private <T> T getValue(String name, Class<T> type) throws IllegalAccessException,
                                                      IllegalArgumentException,
                                                      InvocationTargetException {
        Method method = elements.get(name);
        return type.cast(method.invoke(annotation));
    }

    private boolean hasElement(String name, Class<?> type) {
        if (elements.containsKey(name)) {
            Method method = elements.get(name);
            return method.getReturnType().equals(type);
        }
        return false;
    }

}
