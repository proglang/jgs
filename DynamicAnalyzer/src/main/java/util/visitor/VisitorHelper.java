package util.visitor;

import soot.Local;
import soot.Value;
import soot.jimple.InvokeExpr;
import util.logging.L1Logger;

import java.util.List;
import java.util.logging.Logger;

public class VisitorHelper {

	Logger logger = L1Logger.getLogger();

	
	/**
	 * This Method returns a list of the arguments for an invoked method
	 * for further proceedings. If an argument is a constant the returned
	 * list has a null-value at the respective position.
	 * @param invokeExpr The expression to invoke a method
	 * @return list containing all non-constant arguments
	 */
	protected Local[] getArgumentsForInvokedMethod(InvokeExpr invokeExpr) {
		logger.finer("Arguments of invoked method: " + invokeExpr.getArgs().toString());
		
		List<Value> list = invokeExpr.getArgs();
		int sizeOfList = list.size();

		Local[] retList = new Local[sizeOfList];
		
		for (int i = 0; i < sizeOfList; i++) {
			Value argument = list.get(i);
			if (argument instanceof Local) {
				retList[i] = (Local) argument;
			}
			logger.finer("Type of argument: " + argument.getType());
		}
		return retList;
	}
	
}
