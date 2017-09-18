package utils.jimple;

import soot.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides some helper Tools for the Soot Framework,
 * that contains some things, like counting things, transforming things,...
 *
 * In General: The here collected Methods are extensions for the framework.
 *
 * @author Karsten Fix, 18.08.17
 * @version 1.0
 */
public class Soots {

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
     * @see Soots#toSootType
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
}
