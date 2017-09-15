package utils.jimple;

import soot.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * This Class provides some Factory Methods, that may help to Create Jimple Expressions
 * and Statements, that can be inserted.
 *
 * @author Karsten Fix, 15.08.17
 */
public class JimpleFactory {

    /** Defines a Cache, that it has only to be calculated Once for a Class */
    private static Map<String, Map<String, SootMethodRef>> cache = new HashMap<>();

    /**
     * Creates a Type out of a given Parameter as Class.
     * @param param The Parameter of a {@link Method} or its return Type.
     * @return The SootType, that refers to the given parameter Type
     * @see Method#getParameterTypes()
     * @see Method#getReturnType()
     * @throws IllegalStateException if the given parameter is an unsupported primitive type.
     */
    public static Type toSootType(Class param) {
        if (param.isPrimitive()) {
            switch (param.getName()) {
                case "boolean" : return BooleanType.v();
                case "byte": return ByteType.v();
                case "char": return CharType.v();
                case "double" : return DoubleType.v();
                case "float" : return FloatType.v();
                case "int" : return IntType.v();
                case "long" : return LongType.v();
                case "short": return ShortType.v();
                case "void" : return VoidType.v();
                default:
                    throw new IllegalStateException("Unsupported Primative Type: "+param.getName());
            }
        }
        // if not primitive, then it is hopefully a RefType
        return RefType.v(param.getTypeName());
    }

    /**
     * Creates a list of SootTypes that are used as parameters
     * @param args An array of Classes, that are used as parameters.
     * @return a List of SootType parameters
     * @see JimpleFactory#toSootType
     * @see Class#getMethods()
     */
    public static List<Type> createParameters(Class... args) {
        ArrayList params = new ArrayList();
        for (Class arg : args) {
            Type t = toSootType(arg);
            params.add(t);
        }
        return params;
    }

    /**
     * Returns a Map of Method names to the SootMethodRef, such it could be
     * directly accessed over the Map Interface. <br>
     * <b>Note:</b> This Map is not cached (anymore). It could only be cached, if
     * Soot.Reset() would work proper.
     * See <a href="https://mailman.cs.mcgill.ca/pipermail/soot-list/2008-December/002174.html">This article</a>.
     * <br><br>
     * <b>Example</b>: Let C be a Class, which at least the Method some(), then
     * use JimpleFactory.getAllMethodsOf(C.class).get("some") to get the SootMethodRef.
     * @param c The Class Type of that class which Methods are needed as SootMethodRefs,
     *          such that they could be inserted as a call with the help of the
     *          Soot - Framework.
     * @return A Map that maps the name of a Method to the SootMethodRef
     */
    public static Map<String, SootMethodRef> getAllMethodsOf (Class c) {
      //  if (cache.containsKey(c.getName()))
      //      return cache.get(c.getName());
        Map content = new HashMap<>();
        for (Method m : c.getMethods()) {
            SootMethodRef mRef = Scene.v().makeMethodRef(
                    Scene.v().getSootClass(c.getName()),
                    m.getName(),
                    createParameters(m.getParameterTypes()),
                    toSootType(m.getReturnType()),
                    java.lang.reflect.Modifier.isStatic(m.getModifiers()));

            content.put(m.getName(), mRef);
        }
     //   cache.put(c.getName(), content);
        return content;
    }

}
