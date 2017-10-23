package annotation;

import static resource.Messages.getMsg;
import static util.AnalysisUtils.getJNISignature;
import static util.AnalysisUtils.jniSignatureToJavaPath;
import static util.ExtendedJNI.EXT_JNI_ANNOTATION;
import static util.ExtendedJNI.EXT_JNI_CLASS;
import static util.ExtendedJNI.JNI_ARRAY;
import static util.ExtendedJNI.JNI_BOOLEAN;
import static util.ExtendedJNI.JNI_BYTE;
import static util.ExtendedJNI.JNI_CHAR;
import static util.ExtendedJNI.JNI_DOUBLE;
import static util.ExtendedJNI.JNI_ENUM;
import static util.ExtendedJNI.JNI_FLOAT;
import static util.ExtendedJNI.JNI_INT;
import static util.ExtendedJNI.JNI_LONG;
import static util.ExtendedJNI.JNI_SHORT;
import static util.ExtendedJNI.JNI_STRING;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import exception.AnnotationElementNotFoundException;

public class SootAnnotationDAO extends AAnnotationDAO {

    private final Map<String, AnnotationElem> elements =
        new HashMap<String, AnnotationElem>();
    private final Class<? extends Annotation> type;

    public SootAnnotationDAO(Class<? extends Annotation> annotationClass,
            AnnotationTag annotation) {
        this.type = annotationClass;
        for (int i = 0; i < annotation.getNumElems(); i++) {
            AnnotationElem ae = annotation.getElemAt(i);
            this.elements.put(ae.getName(), ae);
        }
    }

    public List<IAnnotationDAO> getAnnotationArrayFor(String name,
                                                      Class<? extends Annotation> annotationClass) {
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
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                                    name,
                                                                    JNI_ARRAY,
                                                                    EXT_JNI_ANNOTATION));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_array",
                                                            name,
                                                            JNI_ARRAY,
                                                            EXT_JNI_ANNOTATION));
    }

    public IAnnotationDAO getAnnotationFor(String name,
                                           Class<? extends Annotation> annotationClass) {
        if (hasAnnotation(name)) {
            try {
                return convertAnnotation(elements.get(name), annotationClass);
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    EXT_JNI_ANNOTATION));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            EXT_JNI_ANNOTATION));
    }

    public List<Boolean> getBooleanArrayFor(String name) {
        if (hasBooleanArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Boolean> list = new ArrayList<Boolean>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Boolean(convertBoolean(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public boolean getBooleanFor(String name) {
        if (hasBoolean(name)) {
            try {
                return convertBoolean(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_BOOLEAN));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_BOOLEAN));
    }

    public List<Byte> getByteArrayFor(String name) {
        if (hasByteArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Byte> list = new ArrayList<Byte>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Byte(convertByte(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public byte getByteFor(String name) {
        if (hasByte(name)) {
            try {
                return convertByte(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_BYTE));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_BYTE));
    }

    public List<Character> getCharArrayFor(String name) {
        if (hasCharArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Character> list = new ArrayList<Character>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Character(convertChar(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public char getCharFor(String name) {
        if (hasChar(name)) {
            try {
                return convertChar(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_CHAR));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_CHAR));
    }

    public List<Class<?>> getClassArrayFor(String name) {
        if (hasClassArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Class<?>> list = new ArrayList<Class<?>>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(convertClass(aae.getValueAt(i)));
                }
                return list;
            } catch (ClassCastException e) {
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

    public Class<?> getClassFor(String name) {
        if (hasClass(name)) {
            try {
                return convertClass(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    EXT_JNI_CLASS));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            EXT_JNI_CLASS));
    }

    public List<Double> getDoubleArrayFor(String name) {
        if (hasDoubleArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Double> list = new ArrayList<Double>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Double(convertDouble(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public double getDoubleFor(String name) {
        if (hasDouble(name)) {
            try {
                return convertDouble(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_DOUBLE));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_DOUBLE));
    }

    public <V extends Enum<V>> List<V> getEnumArrayFor(String name,
                                                       Class<V> enumClass) {
        if (hasEnumArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<V> list = new ArrayList<V>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(convertEnum(aae.getValueAt(i), enumClass));
                }
                return list;
            } catch (ClassCastException e) {
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

    public <V extends Enum<V>> V getEnumFor(String name, Class<V> enumClass) {
        if (hasEnum(name)) {
            try {
                return convertEnum(elements.get(name), enumClass);
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_ENUM));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_ENUM));
    }

    public List<Float> getFloatArrayFor(String name) {
        if (hasFloatArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Float> list = new ArrayList<Float>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Float(convertFloat(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public float getFloatFor(String name) {
        if (hasFloat(name)) {
            try {
                return convertFloat(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_FLOAT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_FLOAT));
    }

    public List<Integer> getIntArrayFor(String name) {
        if (hasIntArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Integer(convertInt(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public int getIntFor(String name) {
        if (hasInt(name)) {
            try {
                return convertInt(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_INT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_INT));
    }

    public List<Long> getLongArrayFor(String name) {
        if (hasLongArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Long> list = new ArrayList<Long>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Long(convertLong(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public long getLongFor(String name) {
        if (hasLong(name)) {
            try {
                return convertLong(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_LONG));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_LONG));
    }

    public List<Short> getShortArrayFor(String name) {
        if (hasShortArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<Short> list = new ArrayList<Short>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(new Short(convertShort(aae.getValueAt(i))));
                }
                return list;
            } catch (ClassCastException e) {
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

    public short getShortFor(String name) {
        if (hasShort(name)) {
            try {
                return convertShort(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_SHORT));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_SHORT));
    }

    public List<String> getStringArrayFor(String name) {
        if (hasStringArray(name)) {
            try {
                AnnotationArrayElem aae = getArrayElem(name);
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < aae.getNumValues(); i++) {
                    list.add(convertString(aae.getValueAt(i)));
                }
                return list;
            } catch (ClassCastException e) {
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

    public String getStringFor(String name) {
        if (hasString(name)) {
            try {
                return convertString(elements.get(name));
            } catch (ClassCastException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                                    name,
                                                                    JNI_STRING));
            }
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.no_such_element",
                                                            name,
                                                            JNI_STRING));
    }

    public boolean hasAnnotation(String name) {
        return containsElement(name, EXT_JNI_ANNOTATION);
    }

    public boolean hasAnnotationArray(String name) {
        return containsArray(name, EXT_JNI_ANNOTATION);
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

    public boolean hasClass(String name) {
        return containsElement(name, EXT_JNI_CLASS);
    }

    public boolean hasClassArray(String name) {
        return containsArray(name, EXT_JNI_CLASS);
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
                                                Class<? extends Annotation> annotationClass) {
        AnnotationAnnotationElem aae = (AnnotationAnnotationElem) ae;
        AnnotationTag at = aae.getValue();
        if (at.getType().equals(getJNISignature(annotationClass))) {
            return new SootAnnotationDAO(annotationClass, at);
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.invalid_annotation_type",
                                                            annotationClass.getName()));
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

    private Class<?> convertClass(AnnotationElem ae) throws ClassCastException {
        AnnotationClassElem ace = (AnnotationClassElem) ae;
        String jni = ace.getDesc();
        return getUsedClass(jni);
    }

    private double convertDouble(AnnotationElem ae) throws ClassCastException {
        AnnotationDoubleElem ade = (AnnotationDoubleElem) ae;
        return ade.getValue();
    }

    private <V extends Enum<V>> V convertEnum(AnnotationElem ae,
                                              Class<V> enumClass) throws ClassCastException {
        AnnotationEnumElem aee = (AnnotationEnumElem) ae;
        if (getJNISignature(enumClass).equals(aee.getTypeName())) {
            return Enum.valueOf(enumClass, aee.getConstantName());
        }
        throw new AnnotationElementNotFoundException(getMsg("exception.annotation.invalid_enum_type",
                                                            enumClass.getName()));
    }

    private float convertFloat(AnnotationElem ae) throws ClassCastException {
        AnnotationFloatElem afe = (AnnotationFloatElem) ae;
        return afe.getValue();
    }

    private int convertInt(AnnotationElem ae) throws ClassCastException {
        AnnotationIntElem aie = (AnnotationIntElem) ae;
        return aie.getValue();
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

    private AnnotationArrayElem getArrayElem(String name) throws ClassCastException {
        AnnotationArrayElem aae = (AnnotationArrayElem) elements.get(name);
        return aae;
    }

    private Class<?> getUsedClass(String jni) {
        if (PRIM_CLASSES.containsKey(jni)) {
            return PRIM_CLASSES.get(jni);
        } else {
            String javaPath = jniSignatureToJavaPath(jni);
            try {
                return Class.forName(javaPath);
            } catch (ClassNotFoundException e) {
                throw new AnnotationElementNotFoundException(getMsg("exception.annotation.not_found_class",
                                                                    javaPath));
            }
        }
    }

    protected Class<? extends Annotation> getType() {
        return type;
    }

}
