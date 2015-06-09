package visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import exceptions.InternalAnalyzerException;
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
			logger.finer("Type of argument: " + e.getType());
			if (!(e instanceof Local)) {
				new InternalAnalyzerException("Argument for "
						+ "invoked method is not a Local");
			}
			retList[i] = (Local) e;
		}
		
		return retList;
	}
}
