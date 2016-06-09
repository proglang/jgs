package analyzer.level2;

import utils.exceptions.IllegalFlowException;

public class HandleStmtForTests extends HandleStmt {

	@Override
	public void abort(String sink) {
		System.out.println("System.exit because of illegal flow to " + sink);
		new IllegalFlowException("System.exit because of illegal flow to " + sink);
	}



}
