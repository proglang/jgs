package analyzer.level2;

import exceptions.IllegalFlowException;

public class HandleStmtForTests extends HandleStmt {

	@Override
	public void abort(String sink) {
		System.out.println("System.exit because of illegal flow to " + sink);
		throw new IllegalFlowException("System.exit because of illegal flow to " + sink);
	}




}
