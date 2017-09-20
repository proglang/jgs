package utils.jimple;

import soot.*;
import soot.jimple.*;
import utils.logging.L1Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

/**
 * This Class provides some Factory Methods, that may help to Create Jimple Expressions
 * and Statements, that can be inserted.
 *
 * @author Karsten Fix, 15.08.17
 * @version 2.0 (18.08.17)
 */
public class JimpleFactory {

    // <editor-fold desc="Cache Definitions">
    /** Defines a Cache, that allows a quick access to the Class Methods */
    private Map<String, Map<String, SootMethodRef>> methodCache = new HashMap<>();

    /** Caches the Constructor Methods, that are used for special invokes */
    private Map<String, Map<String, SootMethodRef>> constructorCache = new HashMap<>();
    // </editor-fold>

    // <editor-fold desc="Reference Definitions">
    /** Defines the Class for which this Factory is created. */
    private Class reference;

    /** Defines the Local, that shall be used to create Jimple Statements */
    private Local instance;
    // </editor-fold>

    // <editor-fold desc="Initialise Methods">
    /**
     * Creates a new JimpleFactory for the given Class and the given Local, that
     * is an instance of the Class for the Soot Framework.
     * @param c The reference Class with Methods shall be called.
     * @param inst The instance of the given class, which is used to execute the
     *             invokes.
     */
    public JimpleFactory(Class c, Local inst) {
        instance = inst; reference = c;
        initialise();
    }

    /**
     * Initialises the JimpleFactory. This Method has to be called once after
     * creating a new JimpleFactory, otherwise the other methods would not work
     * proper.
     */
    public void initialise() {

        // <editor-fold desc="Constructor initialization">

        for (Constructor c : reference.getConstructors()) {
            // Creating new HashMap in the Cache for the Constructor Name
            // if not present yet
            Map<String, SootMethodRef> overloads = constructorCache.containsKey(c.getName())
                    ? constructorCache.get(c.getName()) : new HashMap<>();

            // Getting the Signature List as List of Type. And use it to calculate
            // a String Representation, that can be used as key for the overloads
            List<Type> types = Soots.createParameters(c.getParameterTypes());
            String key = calcKey(types);

            SootMethodRef cRef = Scene.v().makeConstructorRef(
                    Scene.v().getSootClass(reference.getName()),types);

            if (overloads.containsKey(key))
                L1Logger.getLogger().info("Overwriting ConstructorRef for: "+c.getName() + "(" + key + ")");
            else L1Logger.getLogger().info("Creating ConstructorRef for: "+c.getName() + "(" + key + ")");

            overloads.put(key, cRef);
            if (!constructorCache.containsKey(c.getName()))
                constructorCache.put(c.getName(), overloads);
        }
        //</editor-fold>

        //<editor-fold desc="Method initialization">

        for (Method m : reference.getMethods()) {
            // Creating new HashMap in the Cache for the Methods Name
            // if not present yet
            Map<String, SootMethodRef > overloads
                    = methodCache.containsKey(m.getName()) ? methodCache.get(m.getName()) : new HashMap();

            // Getting the Signature List as List of Type. And use it to calculate
            // a String Representation, that can be used as key for the overloads
            List<Type> types = Soots.createParameters(m.getParameterTypes());
            String key = calcKey(types);

            // Calculating the SootMethodRef for the current Method m
            SootMethodRef mRef = Scene.v().makeMethodRef(
                    Scene.v().getSootClass(reference.getName()),
                    m.getName(), types, Soots.toSootType(m.getReturnType()),
                    java.lang.reflect.Modifier.isStatic(m.getModifiers()));

            // Adding the calculated Method in the cache and warn/inform the user
            // for changes, that might not be wanted.
            if (overloads.containsKey(key))
                L1Logger.getLogger().info("Overwriting SootMethodRef for: "+m.getName() + "(" + key + ")");
            else L1Logger.getLogger().info("Creating SootMethodRef for: "+m.getName() + "(" + key + ")");

            overloads.put(key, mRef);
            if (!methodCache.containsKey(m.getName()))
                methodCache.put(m.getName(), overloads);
        }
        // </editor-fold>
    }
    //</editor-fold>

    // <editor-fold desc="Cache Interaction">
    /**
     * Gets the cached SootMethodRef for the given name and values.
     * It is either cached in the constructorCache or in the methodCache.
     * <br><b>NOTE:</b> A constructor shall not be named as a Method.
     * @param name The name of the method or the constructor
     * @param args The arguments that shall be passed to the Method
     * @return The MethodRef, that is cached.
     * @throws IllegalStateException if a Constructor is named like a Method.
     * @throws IllegalStateException if an Type Conversion failure appears.
     * @throws IllegalArgumentException if There is no suitable Method or Constructor for the given Value Types
     * @throws NoSuchElementException if there is no constructor nor a method with the given name.
     *
     */
    private SootMethodRef getMethodRefFor(String name, Value... args) {
        if (methodCache.containsKey(name) && constructorCache.containsKey(name))
            throw new IllegalStateException("Multiple Names for Constructor and Methods: "+name);
        if (!methodCache.containsKey(name) && !constructorCache.containsKey(name))
            throw new NoSuchElementException("Couldn't find Method or Constructor: "+name);

        Map<String, SootMethodRef> overloads = methodCache.containsKey(name)
                                               ? methodCache.get(name)
                                               : constructorCache.get(name);

        // calculating Signature String
        List<Type> types = new ArrayList<>();
        for (Value arg : args) { types.add(arg.getType()); }
        String key = calcKey(types);

        // Getting the right name or a other one, that may fit.
        SootMethodRef sootMethod = overloads.get(key);

        //<editor-fold desc="Check for another signature, that may fit the passed arguments">
        if (sootMethod == null) {
            // There is no exact key. That may be the Case, if there is a
            // Signature like fkt(Object o) and is now called with
            // fkt(SimpleObject so)
            // There are two cases: Case one - there is only one overload

            //<editor-fold desc="Checking all available overloads for that name name">
            for (Map.Entry<String, SootMethodRef> entry : overloads.entrySet()) {
                // Getting the Signature Types
                String[] savArgs = entry.getKey().split(", ");

                // Checking for number of Arguments, if does not fit continue
                if (savArgs.length != args.length) continue;

                // <editor-fold desc="Creating Superclass List out of savedArgs">
                List<Class> savArgsClasses = new ArrayList<>();
                for (String savArg : savArgs) {
                    try { savArgsClasses.add(Class.forName(savArg)); }
                    catch (ClassNotFoundException e) {
                        // The primitive data types are not creatable with Class.forClass
                        // do it manually here
                        if (Soots.isPrimitiveType(savArg))
                            savArgsClasses.add(Soots.getPrimitiveType(savArg));
                        else if (savArg.contains("[]")) {
                            int beg = savArg.indexOf("[]");
                            int end = savArg.lastIndexOf("[]");
                            int dim = ((end - beg) / 2) + 1;
                            Class base = null;
                            String baseString = savArg.replaceAll("\\[]", "");
                            if (Soots.isPrimitiveType(baseString))
                                base = Soots.getPrimitiveType(baseString);
                            if (base == null) {
                                try {
                                    base = Class.forName(baseString);
                                } catch (ClassNotFoundException inner) {
                                    new IllegalStateException("Class cast failed for array value: "+savArg);
                                }
                            }
                            savArgsClasses.add(Array.newInstance(base, dim).getClass());
                        }
                        else throw new IllegalStateException("Class cast failed for: " + savArg);
                    }
                }
                // </editor-fold>

                // <editor-fold desc="Creating Classes of Args">
                List<Class> argsClass = new ArrayList<>();
                for (Value arg : args) {
                    try { argsClass.add(Class.forName(arg.getType().toString())); }
                    catch (ClassNotFoundException e) {
                        // The primitive data types are not creatable with Class.forClass
                        // do it manually here
                        if (Soots.isPrimitiveType(arg.getType().toString()))
                            argsClass.add(Soots.getPrimitiveType(arg.getType().toString()));
                        // Array Types need special Handling as well.
                        else if (arg.getType() instanceof ArrayType) {
                            ArrayType arr = (ArrayType) arg.getType();
                            int[] dim = new int[arr.numDimensions];
                            Class base = null;
                            if (Soots.isPrimitiveType(arr.baseType.toString()))
                                base = Soots.getPrimitiveType(arr.baseType.toString());
                            if (base == null) {
                                try {
                                    base = Class.forName(arr.baseType.toString());
                                } catch (ClassNotFoundException inner) {
                                    new IllegalStateException("Class cast failed for given array value: "+arg + " with Type: " + arg.getType());
                                }
                            }
                            argsClass.add(Array.newInstance(base, dim).getClass());
                        }
                        else throw  new IllegalStateException("Class cast failed for given value: "+arg + " with Type: " + arg.getType());
                    }
                }
                // </editor-fold>

                //<editor-fold desc="Finally checking if each class will fit"
                for (int i=0; i < argsClass.size(); i++) {
                    if (!savArgsClasses.get(i).isAssignableFrom(argsClass.get(i)))
                        // In Case one type does not fit throw an exception
                        throw new IllegalArgumentException("Type "+argsClass.get(i)
                                                           + " does not match required Type "+ savArgsClasses.get(i) + " for Method: "+name);
                }
                //</editor-fold>

                // If no Exception is thrown, then we could assign here
                sootMethod = entry.getValue();
                L1Logger.getLogger().info("Used the signature: "+entry.getKey()+
                                          " as suitable for wanted call of "
                                          +name+"("+key+")");
            }
            //</editor-fold>

            // Checking if still not assigned, that's the case of no suitable
            // name found: wrong number of Arguments
            if (sootMethod == null)
                throw new IllegalArgumentException("The number of Arguments does not fit any overload of "+name);
        }
        //</editor-fold>
        else {
            L1Logger.getLogger().info("Used identical signature for call of "+name);
        }
        return sootMethod;
    }
    // </editor-fold>

    // <editor-fold desc="Expr Creation">
    /**
     * Creates new new SpecialInvokeExpr, that represents a Constructor.
     * @param name The name of the Constructor
     * @param args The arguments, that shall be passed to the constructor.
     * @return The SpecialInvokeExpr for the Constructor fitting the name and arguments
     * @see JimpleFactory#getMethodRefFor(String, Value...)
     */
    private SpecialInvokeExpr createConstructorExpression(String name, Value... args) {
        return Jimple.v().newSpecialInvokeExpr(instance, getMethodRefFor(name, args));
    }

    /**
     * Creates a VirtualInvokeExpr that can be turned into an Stmt, such that
     * it could be inserted.
     * @param name The Method, that shall be executed by the created Expr
     * @param args The Arguments, that shall be passed to the called Method.
     * @return A VirtualInvokeExpr, that could be inserted.
     * @see JimpleFactory#getMethodRefFor(String, Value...)
     */
    private VirtualInvokeExpr createVirtualExpr(String name, Value... args) {
        return Jimple.v().newVirtualInvokeExpr(instance, getMethodRefFor(name, args), args);
    }

    /**
     * Creates a new static invoke Expression that is then turned into a Stmt,
     * such that it could be inserted easier.
     * @param name The name of the method, that shall be invoked
     * @param args The arguments that shall be passed.
     * @return A StaticInvokeExpr, that fits the given name and arguments
     * @see JimpleFactory#getMethodRefFor(String, Value...)
     */
    private StaticInvokeExpr createStaticExpr(String name, Value... args) {
        return Jimple.v().newStaticInvokeExpr(getMethodRefFor(name, args), args);
    }
    // </editor-fold>

    //<editor-fold desc="Stmt Creation Public">
    /**
     * Creates a Stmt using the Parameters. This statement could be inserted by
     * the JimpleInjector directly.
     * It checks for Method being a constructor, virtual or static invoke.
     * @param name The Method, that shall be executed by the created Expr
     * @param args The Arguments, that shall be passed to the called Method.
     * @return A Stmt, that the JimpleInjector could inject.
     */
    public InvokeStmt createStmt(String name, Value... args) {
        Value op;
        SootMethodRef s = getMethodRefFor(name, args);
        // Assuming being a constructor is defined over name of constructor
        // if this assumption is incorrect, then both caches has the same name
        // and getMethodRefFor will throw an Exception, not nice, but okay
        // at least for the moment
        if (constructorCache.containsKey(name))
            op = createConstructorExpression(name, args);
        else if (s.isStatic())
            op = createStaticExpr(name, args);
        else op = createVirtualExpr(name,args);
        return Jimple.v().newInvokeStmt(op);
    }
    // </editor-fold>

    // <editor-fold desc="Internal Helper Methods">
    /**
     * Calculates the Key String to Save the Signature of a Method
     * out of the Types of the Signature passed as a List.
     * @param types A List of Type, that shall be translated into a String
     * @return A String containing the Signature of the given Types.
     */
    public static String calcKey(List<Type> types) {
        String key = "";
        for (Type t : types) { key += t + ", "; }
        return types.isEmpty() ? key : key.substring(0, key.length() - 2);
    }
    // </editor-fold>
}
