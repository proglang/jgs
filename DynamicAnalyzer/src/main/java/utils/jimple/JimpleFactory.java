package utils.jimple;

import soot.*;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.VirtualInvokeExpr;
import utils.logging.L1Logger;

import java.lang.reflect.Method;
import java.util.*;

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
            Map<String, SootMethodRef> overloads
                    = cache.containsKey(m.getName()) ? cache.get(m.getName()) : new HashMap();

            // Getting the Signature List as List of Type. And use it to calculate
            // a String Representation, that can be used as key for the overloads
            List<Type> types = Soots.createParameters(m.getParameterTypes());
            String key = "";
            for (Type t : types) { key += t + ", "; }
            key = types.isEmpty() ? key : key.substring(0, key.length() - 2);

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


    public VirtualInvokeExpr createExpr(String method, Value... args) {
        if (!cache.containsKey(method))
            throw new NoSuchElementException("Couldn't find the Method: "+method);

        Map<String, SootMethodRef> overloads = cache.get(method);

        // calculating Signature String
        String key = "";
        for (Value arg : args) { key += arg.getType() + ", "; }
        key = args.length > 0 ? key.substring(0, key.length() -2) : key;

        // Getting the right method or a other one, that may fit.
        SootMethodRef sootMethod = overloads.get(key);
        if (sootMethod == null) {
            // There is no exact key. That may be the Case, if there is a
            // Signature like fkt(Object o) and is now called with
            // fkt(SimpleObject so)
            // There are two cases: Case one - there is only one overload
            if (overloads.size() == 1) {
                String sig = "";
                for (Map.Entry<String, SootMethodRef> entry : overloads.entrySet()) {
                    sootMethod = entry.getValue(); sig = entry.getKey();
                }
                L1Logger.getLogger().warning("Using Method with different Signature: " + sig);
            } else {
                for (Map.Entry<String, SootMethodRef> entry : overloads.entrySet()) {
                    // Todo: Implement handling of overladed Methods.
                }
            }
        }

        // Creating and return the Expression
        return Jimple.v().newVirtualInvokeExpr(instance, sootMethod, args);
    }

    public InvokeStmt createStmt(String callMethod, Value... args) {
        return Jimple.v().newInvokeStmt(createExpr(callMethod, args));
    }
}
