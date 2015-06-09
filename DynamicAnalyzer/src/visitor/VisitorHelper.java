package visitor;

import java.util.List;
import java.util.logging.Logger;

import logging.L1Logger;
import soot.Local;
import soot.Value;
import soot.jimple.InvokeExpr;

public class VisitorHelper {

	Logger logger = L1Logger.getLogger();

	
	protected Local[] getArgumentsForInvokedMethod(InvokeExpr invokeExpr) {
		logger.finer("Arguments of invoked method: " + invokeExpr.getArgs());
		
		List<Value> list = invokeExpr.getArgs();
		int sizeOfList = list.size();

		Local[] retList = new Local[sizeOfList];
		
		for (int i = 0; i < sizeOfList; i++) {
			Value e = list.get(i);
			if (e instanceof Local) {
			  retList[i] = (Local) e;
			}
			logger.finer("Type of argument: " + e.getType());
		}
		

		
		return retList;
	}
}
