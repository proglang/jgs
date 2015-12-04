package utils.visitor;

import java.util.List;
import java.util.logging.Logger;

import utils.logging.L1Logger;
import soot.Local;
import soot.Value;
import soot.jimple.InvokeExpr;

public class VisitorHelper {

  Logger logger = L1Logger.getLogger();

	
  /**
   * @param invokeExpr
   * @return
   */
  protected Local[] getArgumentsForInvokedMethod(InvokeExpr invokeExpr) {
    logger.finer("Arguments of invoked method: " + invokeExpr.getArgs());
		
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
