package analyzer.level2.storage;

import analyzer.level2.CurrentSecurityDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import util.exceptions.InternalAnalyzerException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The LocalMap
 * 1. maps the dynamically tracked locals of a method body to their current security levels.
 * 2. Maintains the stack of local pcs.
 *
 * Any local not present in the map is not dynamically tracked (i.e. statically checked).
 * A dynamically tracked local may be "uninitialized".
 *
 * @author Regina König (2015), Nicolas Müller (2016), fennell (2017), Karsten Fix (2017)
 *
 */
public class LocalMap<Level> {

	// <editor-fold desc="Fields">

	/** The logger for the messages */
	private static final Logger logger = Logger.getLogger(LocalMap.class.getName());

	/** The internal representation of the Local PC or simply LPC */
	private LinkedList<LPCDominatorPair<Level>> localPC = new LinkedList<>();

	/** The internal representation of the Local Map.
	 *  It maps an identifier String of a Local variable to its Security Level
	 *  */
	private HashMap<String, Level> localMap = new HashMap<>();

	/** The {@link SecDomain} instance, that is used for the calculations. */
	private SecDomain<Level> secDomain = CurrentSecurityDomain.getInstance();

	// </editor-fold>

	/**
	 * Creates a new LocalMap
	 */
	public LocalMap() {
		localPC.push(new LPCDominatorPair<>(secDomain.bottom() , -1));
	}

	// <editor-fold desc="LPC Operations">

	/**
	 * Check whether the lpc stack is empty. This method is invoked at the end of every method.
	 * If the stack is not empty, than the program is closed with an InternalAnalyzerException.
	 * At the end there should be only one element left since there is one bottom()
	 * element at the beginning.
	 */
	public void isEmptyLPC() {
		localPC.pop();
		if (!localPC.isEmpty())
			throw new InternalAnalyzerException("LocalPC is not empty at the end of the method. "
												+"There are still " + localPC.size() + " elements.");
	}

    // <editor-fold desc="Putting stuff into LPC">
	/**
	 * Adds the given Security Level and its corresponding identity-value to the top of the LPC.
	 * @param securityLevel New security-level for the LPC.
	 * @param dominatorIdentity Its identity.
	 */
	public void pushLocalPC(Level securityLevel, int dominatorIdentity) {
		localPC.push(new LPCDominatorPair<>(securityLevel, dominatorIdentity));
	}
	// </editor-fold>

	// <editor-fold desc="Getting stuff from LPC">

	/**
	 * Gets the Security Level of the LPC.
	 * @return The first element of the LPC stack without removing it.
	 * @throws InternalAnalyzerException If LPC is empty.
	 */
	public Level getLocalPC() {
		if (localPC == null || localPC.size() < 1) throw new InternalAnalyzerException("LocalPCStack is empty");
		return localPC.getFirst().getSecurityLevel();
	}

	// </editor-fold>

	// <editor-fold desc="Removing Stuff from LPC">

	/**
	 * Removes the first element of LPC-stack, if the given identity-value matches.
	 * @param dominatorIdentity Identity of the expected first element in LPC-list.
	 * @throws InternalAnalyzerException If LPC is Empty <b>or</b> the Identity is wrong.
	 */
	public void popLocalPC(int dominatorIdentity) {
		int n = localPC.size();
		if (n < 1) throw new InternalAnalyzerException("localPC-stack is empty");
		if (!dominatorIdentityEquals(dominatorIdentity))
			throw new InternalAnalyzerException("Trying to pop LPC with wrong identity");
		localPC.pop();
		logger.finer("Reduced stack size from " + n+ " to " + localPC.size() + " elements.");
	}
	// </editor-fold>

	// </editor-fold>

	// <editor-fold desc="LMap Operations"

    // <editor-fold desc="Putting Stuff into LMap"
    /**
     * Sets the security level of the given Signature to the given Level.
     * @param signature The signature of the local, which security Level will now be tracked.
     * @param securityLevel The security level, the Local will have now.
     */
    public void setLevel(String signature, Level securityLevel) {
        localMap.put(signature, securityLevel);
    }

    /**
     * Sets the security level of the given signature to {@link SecDomain#bottom()}.
     * @param signature The signature of the local, which level shall be reset.
     */
    public void setToBottom(String signature) {
        localMap.put(signature, secDomain.bottom());
    }
    // </editor-fold>

    // <editor-fold desc="Getting Stuff from LMap"

    /**
     * Tells, if the given local identification is tracked by this Local Map
     * @param signature The identification of the local, that could be tracked
     * @return <b>true</b> if the identification is within the Local Map or <b>false</b> otherwise.
     */
    public boolean isTracked(String signature) {
        return localMap.containsKey(signature);
    }

    /**
     * Gets the security level of the given identification of a local. If the signature is <i>unknown</i>, it will
     * create an entry for it with the default security level {@link SecDomain#bottom()}.
     * @param signature The signature of a local, which security level is wanted.
     * @return The security Level of the identification.
     */
    public Level getLevel(String signature) {
        Level result = (localMap.containsKey(signature)) ? localMap.get(signature) : secDomain.bottom();
        // Todo: Check, if the Bottom shall be saved... asumed yes - but was not
        if (!localMap.containsKey(signature)) logger.fine("Local "+signature+" is not tracked!");
        return result;
    }

    // </editor-fold>

    // <editor-fold desc="Removing stuff from LMap">

    /**
     * Removes the Security Level of the given Signature. This methods stops the tracking of the
     * given identification of a Local.
     * @param signature The identification of a Local.
     */
    public void removeLocal(String signature) {
        localMap.remove(signature);
    }
    // </editor-fold>
	// </editor-fold>

	/**
	 * Print elements of localmap in a readable form.
	 */
	public void printElements() {
		for (Map.Entry<String, Level> entry : localMap.entrySet()) {
			System.out.println("Key " + entry.getKey() + " , Value: " 
					+ entry.getValue());
		}
	}
	
	/**
	 * Compare the given identity value with the first element on the LPC stack.
	 * @param dominatorIdentity The identity.
	 * @return true if the identity equals to the first element on the stack.
	 */
	public boolean dominatorIdentityEquals(int dominatorIdentity) {
		return localPC.getFirst().getPostDominatorIdentity() == dominatorIdentity;	
	}
}
