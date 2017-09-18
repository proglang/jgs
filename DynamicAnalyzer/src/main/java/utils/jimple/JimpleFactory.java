package utils.jimple;

import soot.*;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.VirtualInvokeExpr;

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
     * <b>Note:</b> This Map is cached (again). The Access so shall be O(1) in
     * average.
     * <br><br>
     * <b>Note:</b> The Key String is not only the name of the Method, but also
     * the type Signature. <br>
     * <b>Example</b>: Let C be a Class, which at least the Method some(int, String),
     * then use JimpleFactory.getAllMethodsOf(C.class).get("some int, java.util.String")
     * to get the SootMethodRef.
     * @param c The Class Type of that class which Methods are needed as SootMethodRefs,
     *          such that they could be inserted as a call with the help of the
     *          Soot - Framework.
     * @return A Map that maps the name of a Method to the SootMethodRef
     * @see JimpleFactory#generateKey(String, Type...)
     */
    public static Map<String, SootMethodRef> getAllMethodsOf (Class c) {
        if (cache.containsKey(c.getName()))
            return cache.get(c.getName());
        Map content = new HashMap<>();
        for (Method m : c.getMethods()) {
            List<Type> types = createParameters(m.getParameterTypes());
            SootMethodRef mRef = Scene.v().makeMethodRef(
                    Scene.v().getSootClass(c.getName()),
                    m.getName(),
                    types,
                    toSootType(m.getReturnType()),
                    java.lang.reflect.Modifier.isStatic(m.getModifiers()));

            // Creating a key, that contains not only the name, but also
            // the Signature of the Types, such that overloaded Methods are
            // supported.
            // Extract it as own Function, such that the access is easier and
            // it is assured, that every one has the same key.
            content.put(generateKey(m.getName(), types.toArray(new Type[0])), mRef);
        }
        cache.put(c.getName(), content);
        return content;
    }

    /**
     * Calculates the key for a given Method name with the given Types as signature.
     * @param method The name of the Method, that wants to be accessed by this key.
     * @param signature A List of Types defining the signature of the method.
     * @return The key String to access the Map, in order to gain the SootMethodRef
     * @see JimpleFactory#getAllMethodsOf(Class)
     */
    public static String generateKey(String method, Type... signature) {
        List<Type> types = Arrays.asList(signature);
        String key = types.isEmpty() ? method : method + " ";
        for (Type t : types) {
            key += t + ", ";
        }
        // Cut off the last Comma
        return types.isEmpty() ? key : key.substring(0, key.length() - 2);
    }

    /**
     * Resets the cache. This function shall be called directly after (or before)
     * Soot is reset. Because, if Soot is reset and this function is not called,
     * then there are hard internal problems.
     */
    public static void reset() { cache.clear(); }

    /**
     * Creates an Expression, that can be used to create a Unit, which can then be inserted.
     * @param base The Local Variable, that will be used to execute the invoke.
     *             (At least it seems to be logical. May look {@link Jimple#newVirtualInvokeExpr})
     * @param callRef The Class, that contains the Method that shall be executed.
     * @param callMethod The Name of the Method, that shall be Executed.
     * @param args A list of Values, that are passed by the execution of the invokeStmt.
     *             It is also used to calculate the signature of the method, in case of overloading.
     * @return A VirtualInvokeExpr, that can be used to create a new Unit, means an invokeStmt.
     */
    public static VirtualInvokeExpr createExpr(Local base, Class callRef, String callMethod, Value... args) {
        // Creating a the list of Types as signature, due to the given values.
        List<Type> sign = new ArrayList<>(args.length);
        for (Value val : args) { sign.add(val.getType()); }

        // Use the signature to get the key.
        String key = generateKey(callMethod, sign.toArray(new Type[0]));

        // Getting the right SootMethod using that key
        SootMethodRef method = getAllMethodsOf(callRef).get(key);

        // Creating and return the Expression
        return Jimple.v().newVirtualInvokeExpr(base, method, args);
    }
}
