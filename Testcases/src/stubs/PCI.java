package stubs;

import security.Definition.Constraints;

public class PCI {

	@Constraints("@pc <= low")
	public void lowPC() {}

	@Constraints("@pc <= high")
	public void highPC() {}

}
