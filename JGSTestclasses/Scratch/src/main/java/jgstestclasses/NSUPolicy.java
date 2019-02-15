package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Class to test the basics of NSU policy
 * @author Nicolas Müller
 *
 */
public class NSUPolicy {

	@Constraints({"LOW <= @0 "})
	public static void main(String[] args) {
		int y = 5;
		int secret = 42;
		
		//y = DynamicLabel.makeLow(5);		// just for clarity
		secret = Casts.cast("HIGH ~> ?", 5);
		
		if (secret > 0) {
			y += 1;						// NSU IFCError, Illegal
		}

		// This call stops the compiler (or soot) from optimizing away y's update.
		DynamicLabel.makeHigh(y);

	}

}

/* secret is a public variable, hence casting it as HIGH causes an error. Commenting it can eliminate the error. */