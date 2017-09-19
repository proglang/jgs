package utils.jimple;

import soot.*;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.VirtualInvokeExpr;
import utils.logging.L1Logger;

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

    /** Defines a Cache, that allows a quick access to the Class Methods */
    private Map<String, Map<String, SootMethodRef>> cache = new HashMap<>();

    /** Defines the Class for which this Factory is created. */
    private Class reference;

    /** Defines the Local, that shall be used to create Jimple Statements */
    private Local instance;

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
        for (Method m : reference.getMethods()) {
            // Creating new HashMap in the Cache for the Methods Name
            // if not present yet
            Map<String, SootMethodRef > overloads
                    = cache.containsKey(m.getName()) ? cache.get(m.getName()) : new HashMap();

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
                L1Logger.getLogger().info("Overwriting SootMethodRef for: "+m.getName() + "( " + key + ")");

            overloads.put(key, mRef);
            if (!cache.containsKey(m.getName()))
                cache.put(m.getName(), overloads);
        }
    }

    /**
     * Creates a VirtualInvokeExpr that can be turned into an Stmt, such that
     * it could be inserted.
     * @param method The Method, that shall be executed by the created Expr
     * @param args The Arguments, that shall be passed to the called Method.
     * @return A VirtualInvokeExpr, that could be inserted.
     */
    public VirtualInvokeExpr createExpr(String method, Value... args) {
        if (!cache.containsKey(method))
            throw new NoSuchElementException("Couldn't find the Method: "+method);

        Map<String, SootMethodRef> overloads = cache.get(method);

        // calculating Signature String
        List<Type> types = new ArrayList<>();
        for (Value arg : args) { types.add(arg.getType()); }
        String key = calcKey(types);

        // Getting the right method or a other one, that may fit.
        SootMethodRef sootMethod = overloads.get(key);

        //<editor-fold desc="Check for another signature, that may fit the passed arguments">
        if (sootMethod == null) {
            // There is no exact key. That may be the Case, if there is a
            // Signature like fkt(Object o) and is now called with
            // fkt(SimpleObject so)
            // There are two cases: Case one - there is only one overload

            //<editor-fold desc="Checking all available overloads for that method name">
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
                        // Normally impossible Case; these are the Strings
                        // of the saved signature, but in any case throw an exception
                        throw new IllegalStateException("Class cast failed for: " + savArg);
                    }
                }
                // </editor-fold>

                // <editor-fold desc="Creating Classes of Args">
                List<Class> argsClass = new ArrayList<>();
                for (Value arg : args) {
                    try { argsClass.add(Class.forName(arg.getType().toString())); }
                    catch (ClassNotFoundException e) {
                        // This Case can only happen, if one of the given values are not
                        // castable into an Java Class, shall be impossible as well as
                        // in the case before, but throw Exception to assure
                        throw  new IllegalStateException("Class cast failed for given value: "+arg);
                    }
                }
                // </editor-fold>

                //<editor-fold desc="Finally checking if each class will fit"
                for (int i=0; i < argsClass.size(); i++) {
                    if (!savArgsClasses.get(i).isAssignableFrom(argsClass.get(i)))
                        // In Case one type does not fit throw an exception
                        throw new IllegalArgumentException("Type "+argsClass.get(i)
                        + " does not match required Type "+ savArgsClasses.get(i));
                }
                //</editor-fold>

                // If no Exception is thrown, then we could assign here
                sootMethod = entry.getValue();
                L1Logger.getLogger().info("Used the signature: "+entry.getKey()+
                                          " as suitable for wanted call of "
                                          +method+"("+key+")");
            }
            //</editor-fold>

            // Checking if still not assigned, that's the case of no suitable
            // method found: wrong number of Arguments
            if (sootMethod == null)
                throw new IllegalArgumentException("The number of Arguments does not fit any overload of "+method);
        }
        //</editor-fold>
        else {
            L1Logger.getLogger().info("Used identical signature for call of "+method);
        }

        // Creating and return the Expression
        return Jimple.v().newVirtualInvokeExpr(instance, sootMethod, args);
    }

    /**
     * Creates a Stmt using the Parameters. This statement could be inserted by
     * the JimpleInjector directly.
     * @param method The Method, that shall be executed by the created Expr
     * @param args The Arguments, that shall be passed to the called Method.
     * @return A Stmt, that the JimpleInjector could inject.
     * @see JimpleFactory#createExpr(String, Value...) 
     */
    public InvokeStmt createStmt(String method, Value... args) {
        return Jimple.v().newInvokeStmt(createExpr(method, args));
    }

    private String calcKey(List<Type> types) {
        String key = "";
        for (Type t : types) { key += t + ", "; }
        return types.isEmpty() ? key : key.substring(0, key.length() - 2);
    }
}
