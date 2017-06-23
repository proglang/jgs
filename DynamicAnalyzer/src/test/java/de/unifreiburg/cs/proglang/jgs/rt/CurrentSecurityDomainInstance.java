package de.unifreiburg.cs.proglang.jgs.rt;


import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;


/**
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CurrentSecurityDomainInstance {
	@SuppressWarnings("rawtypes")
	public static SecDomain get() {
		return new LowMediumHigh();
	}

}
