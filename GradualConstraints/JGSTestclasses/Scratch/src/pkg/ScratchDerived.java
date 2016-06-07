package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class ScratchDerived extends Scratch {
    
    @Override
    @Constraints({"? <= @ret"})
    public int aVirtualMethod(int i) {
	return 0;
    }
}
