package de.unifreiburg.cs.proglang.jgs.rt;


import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined;


/**
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CurrentSecurityDomainInstance {
	@SuppressWarnings("rawtypes")
	public static SecDomain get() {
		return UserDefined.aliceBobCharlie();
	}

}
