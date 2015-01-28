package constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import security.ILevel;
import error.ISubSignatureError;
import error.SubSignatureParameterError;
import error.SubSignatureProgramCounterError;
import error.SubSignatureReturnError;

public class ConstraintsUtils {

    public static boolean containsSetParameterReference(Collection<LEQConstraint> constraints) {
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsParameterReference())
                return true;
        }
        return false;
    }

    public static boolean containsSetParameterReferenceFor(Collection<LEQConstraint> constraints,
                                                           String signature,
                                                           int position) {
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsParameterReferenceFor(signature, position))
                return true;
        }
        return false;
    }

    public static boolean containsSetProgramCounterReference(Collection<LEQConstraint> constraints) {
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsProgramCounterReference())
                return true;
        }
        return false;
    }

    public static boolean containsSetReturnReference(Collection<LEQConstraint> constraints) {
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsReturnReference())
                return true;
        }
        return false;
    }

    public static boolean containsSetReturnReferenceFor(Collection<LEQConstraint> constraints,
                                                        String signature) {
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsReturnReferenceFor(signature))
                return true;
        }
        return false;
    }

    public static Set<ILevel> getContainedLevelsOfSet(Collection<LEQConstraint> constraints) {
        Set<ILevel> levels = new HashSet<ILevel>();
        for (LEQConstraint constraint : constraints) {
            levels.addAll(constraint.getContainedLevel());
        }
        return levels;
    }

    public static List<IComponent> getInvalidParameterReferencesOfSet(Collection<LEQConstraint> constraints,
                                                                      String signature,
                                                                      int count,
                                                                      List<Integer> dimensions) {
        List<IComponent> invalid = new ArrayList<IComponent>();
        for (LEQConstraint constraint : constraints) {
            invalid.addAll(constraint.getInvalidParameterReferencesFor(signature,
                                                                       count,
                                                                       dimensions));
        }
        return invalid;
    }

    public static List<IComponent> getInvalidReturnReferencesOfSet(Collection<LEQConstraint> constraints,
                                                                   String signature,
                                                                   int dimension) {
        List<IComponent> invalid = new ArrayList<IComponent>();
        for (LEQConstraint constraint : constraints) {
            invalid.addAll(constraint.getInvalidReturnReferencesFor(signature,
                                                                    dimension));
        }
        return invalid;
    }

    public static boolean isLevel(IComponent component) {
        return component instanceof ILevel;
    }

    public static boolean isParameterReference(IComponent component) {
        return component instanceof ComponentParameterRef;
    }

    public static boolean isParameterReference(IComponent component,
                                               String signature) {
        if (component instanceof ComponentParameterRef) {
            ComponentParameterRef paramRef = (ComponentParameterRef) component;
            return paramRef.getSignature().equals(signature);
        }
        return false;
    }

    public static boolean isParameterReference(IComponent component,
                                               int position) {
        if (isParameterReference(component)) {
            ComponentParameterRef paramRef = (ComponentParameterRef) component;
            return paramRef.getParameterPos() == position;
        }
        return false;
    }

    public static boolean isParameterReference(IComponent component,
                                               String signature,
                                               int position) {
        if (isParameterReference(component)) {
            ComponentParameterRef paramRef = (ComponentParameterRef) component;
            return paramRef.getSignature().equals(signature)
                   && paramRef.getParameterPos() == position;
        }
        return false;
    }

    public static boolean isProgramCounterReference(IComponent component) {
        return component instanceof ComponentProgramCounterRef;
    }

    public static boolean isProgramCounterReference(IComponent component,
                                                    String signature) {
        if (component instanceof ComponentProgramCounterRef) {
            ComponentProgramCounterRef pc =
                (ComponentProgramCounterRef) component;
            return pc.getSignature().equals(signature);
        }
        return false;
    }

    public static boolean isReturnReference(IComponent component) {
        return component instanceof ComponentReturnRef;
    }

    public static boolean isReturnReference(IComponent component,
                                            String signature) {
        if (component instanceof ComponentReturnRef) {
            ComponentReturnRef returnRef = (ComponentReturnRef) component;
            return returnRef.getSignature().equals(signature);
        }
        return false;
    }

    public static boolean isLocal(IComponent component) {
        return component instanceof ComponentLocal;
    }

    public static boolean isArrayReference(IComponent component) {
        return component instanceof ComponentArrayRef;
    }

    public static Set<LEQConstraint> changeAllComponentsSignature(String newSignature,
                                                                  Set<LEQConstraint> constraints) {
        Set<LEQConstraint> result = new HashSet<LEQConstraint>();
        for (LEQConstraint constraint : constraints) {
            result.add(constraint.changeAllComponentsSignature(newSignature));
        }
        return result;
    }

    public static String getCalleeSignatureFor(String signature) {
        return "#" + signature;
    }

    public static String constraintsAsString(Set<LEQConstraint> constraints) {
        StringBuilder sb = new StringBuilder("{ ");
        int count = 0;
        for (LEQConstraint constraint : constraints) {
            if (0 != count++)
                sb.append(", ");
            sb.append(constraint.toString());
        }
        sb.append(" }");
        return sb.toString();
    }

    public static String[] constraintsAsStringArray(Set<LEQConstraint> constraints) {
        String[] result = new String[constraints.size()];
        int i = 0;
        for (LEQConstraint constraint : constraints) {
            result[i++] = constraint.toString();
        }
        return result;
    }

    public static String levelsAsString(Set<ILevel> levels) {
        StringBuilder sb = new StringBuilder("{ ");
        int count = 0;
        for (ILevel level : levels) {
            if (0 != count++)
                sb.append(", ");
            sb.append(level.toString());
        }
        sb.append(" }");
        return sb.toString();
    }

    public static Set<LEQConstraint> getConstraintsContaining(Set<LEQConstraint> constraints,
                                                              IComponent component) {
        Set<LEQConstraint> result = new HashSet<LEQConstraint>();
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsComponent(component)) {
                result.add(constraint);
            }
        }
        return result;
    }

    private static LEQConstraint changeSignatureOf(LEQConstraint constraint,
                                                   String signature) {
        IComponent left = constraint.getLhs();
        IComponent right = constraint.getRhs();
        if (left instanceof IComponentVar && right instanceof IComponentVar) {
            IComponentVar leftVar = (IComponentVar) left;
            IComponentVar rightVar = (IComponentVar) right;
            return new LEQConstraint(changeSignatureOf(leftVar, signature),
                                     changeSignatureOf(rightVar, signature));
        } else if (left instanceof IComponentVar) {
            IComponentVar leftVar = (IComponentVar) left;
            return new LEQConstraint(changeSignatureOf(leftVar, signature),
                                     right);
        } else if (right instanceof IComponentVar) {
            IComponentVar rightVar = (IComponentVar) right;
            return new LEQConstraint(left, changeSignatureOf(rightVar,
                                                             signature));
        }
        return new LEQConstraint(left, right);
    }

    private static IComponentVar changeSignatureOf(IComponentVar component,
                                                   String signature) {
        if (isProgramCounterReference(component)) {
            return new ComponentProgramCounterRef(signature);
        } else if (isParameterReference(component)) {
            ComponentParameterRef paramRef = (ComponentParameterRef) component;
            return new ComponentParameterRef(paramRef.getParameterPos(),
                                             signature);
        } else if (isReturnReference(component)) {
            return new ComponentReturnRef(signature);
        }
        return component;
    }

    public static List<ISubSignatureError> isSubSignature(Set<LEQConstraint> mPlus,
                                                          String mSignature,
                                                          Set<LEQConstraint> _m) {
        List<ISubSignatureError> errors = new ArrayList<ISubSignatureError>();
        for (LEQConstraint constraint : _m) {
            if (isReturnReference(constraint.getRhs())) {
                if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
                    errors.add(new SubSignatureReturnError(constraint));
                }
            }
            if (isParameterReference(constraint.getLhs())) {
                ComponentParameterRef paramRef =
                    (ComponentParameterRef) constraint.getLhs();
                if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
                    errors.add(new SubSignatureParameterError(constraint,
                                                              paramRef.getParameterPos()));
                }
            }
            if (isProgramCounterReference(constraint.getLhs())) {
                if (!mPlus.contains(changeSignatureOf(constraint, mSignature))) {
                    if (!mPlus.contains(changeSignatureOf(constraint,
                                                          mSignature))) {
                        errors.add(new SubSignatureProgramCounterError(constraint));
                    }
                }
            }
        }
        return errors;
    }

    public static Set<ILevel> getLevelOfEqualConstraintsContainingLevelAndComponent(Set<LEQConstraint> constraints,
                                                                                    IComponent component) {
        Set<ILevel> result = new HashSet<ILevel>();
        for (LEQConstraint constraint : constraints) {
            if (constraint.getLhs().equals(component)
                && isLevel(constraint.getRhs())) {
                ILevel level = (ILevel) constraint.getRhs();
                LEQConstraint reverse = new LEQConstraint(level, component);
                if (constraints.contains(reverse))
                    result.add(level);
            }
        }
        return result;
    }

    public static Set<LEQConstraint> getUpdatedPCConstraints(Set<LEQConstraint> constraints,
                                                             String signature) {
        Set<LEQConstraint> result = new HashSet<LEQConstraint>();
        for (LEQConstraint constraint : constraints) {
            if (constraint.containsProgramCounterReference()) {
                if (isLevel(constraint.getLhs())
                    || isLevel(constraint.getRhs())) {
                    result.add(constraint.changeAllComponentsSignature(signature));
                }
            }
        }
        return result;
    }

}
