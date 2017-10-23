package util.jimple;

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
public class SootTypeUtil {

    /**
     * Tells whether the given type Name is a primitive type or not
     * @param typeName that may be something like "int" or "java.lang.String" or..
     * @return true, if it is primitive and false otherwise.
     */
    public static boolean isPrimitiveType(String typeName) {
        switch (typeName) {
            case "boolean" : return true;
            case "byte": return true;
            case "char": return true;
            case "double" : return true;
            case "float" : return true;
            case "int" : return true;
            case "long" : return true;
            case "short": return true;
            case "void" : return true;
            default:
                return false;
        }
    }

    /**
     * Returns the Class of the primitive Type, that is given in the String
     * @param typeName The name of a primitive type, such as "int"
     * @return The class of that primitive Type
     * @throws IllegalArgumentException when the String is not primitive.
     */
    public static Class getPrimitiveType(String typeName) {
        switch (typeName) {
            case "boolean" : return boolean.class;
            case "byte": return byte.class;
            case "char": return char.class;
            case "double" : return double.class;
            case "float" : return float.class;
            case "int" : return int.class;
            case "long" : return long.class;
            case "short": return short.class;
            case "void" : return void.class;
            default:
                throw new IllegalArgumentException("The String is no name of an primitive data type: "+typeName);
        }
    }

    /**
     * Creates a Type out of a given Parameter as Class.
     * @param param The Parameter of a {@link Method} or its return Type.
     * @return The SootType, that refers to the given parameter Type
     * @see Method#getParameterTypes()
     * @see Method#getReturnType()
     * @throws IllegalStateException if the given parameter is an unsupported primitive type.
     */
    public static Type toSootType(Class param) {
        if (param == null) return NullType.v();
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
        // if not primitive, then it hopefully is a RefType
        if (param.isArray()) {
            int dim = param.getName().lastIndexOf("[") + 1;
            String type = param.getTypeName().replaceAll("\\[\\]", "");
            return ArrayType.v(RefType.v(type), dim);
        }
        return RefType.v(param.getTypeName());
    }

    /**
     * Creates a list of SootTypes that are used as parameters
     * @param args An array of Classes, that are used as parameters.
     * @return a List of SootType parameters
     * @see SootTypeUtil#toSootType
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
     * Calculates the First Unit within a given Body, that could be used to
     * insert further more other Units either before or after this returned Unit.
     * <br><b>Because</b> there are some Units, that have to be at the very beginning
     * of a chain. By using this function it is guaranteed, that those Units remain
     * at that place.
     * @param b The body, where furthermore Units are going to be inserted.
     * @return That first Unit, where those Units could be inserted even before.
     */
    public static Unit getFirstUnit(Body b) {
        // Getting the Method, that is currently calculated.
        SootMethod m = b.getMethod();

       /* Depending on the number of arguments and whether it is a static method
       * or a Constructor the position is calculated.
       * Because there are statements, which shouldn't be preceded by other
       * statements.
       * */
        int pos = (!m.isStatic()) ? 1 : 0;
        pos = (m.isConstructor()) ? pos + 1 : pos;
        pos += m.getParameterCount();

        int idx = 0;
        for (Unit u : b.getUnits()) {
            if (idx == pos) return u;
            idx++;
        }
        return b.getUnits().getLast();
    }
}
