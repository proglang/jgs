package utils.visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.Unit;
import utils.logging.L1Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class ExternalClasses {

  static Logger logger = L1Logger.getLogger();

  protected static ArrayList<String> classMap = new ArrayList<String>();

  static {
    classMap.add("java.lang.StringBuilder"); // TODO notwendig?
  }
	
  protected static HashMap<String, Command> methodMap = new HashMap<String,Command>();

  static {
    methodMap.put("<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>",
          new JoinLevels());
    methodMap.put("<java.lang.String: java.lang.String substring(int,int)>", new JoinLevels());
    methodMap.put("<java.io.PrintStream: void println(java.lang.String)>", 
    	   new NoHighLevelAllowed());
    methodMap.put("<java.lang.Object: void <init>()>", new DoNothing());
    methodMap.put("<utils.analyzer.HelperClass: java.lang.Object makeHigh(java.lang.Object)>", new MakeHigh());
    methodMap.put("<utils.analyzer.HelperClass: java.lang.Object makeLow(java.lang.Object)>", new MakeLow());
  }
	
  static void receiveCommand(String method,Unit pos, Local[] params) {
    methodMap.get(method).execute(pos, params);
  }
	
	
  interface Command {
    void execute(Unit pos, Local[] params);
  }
	
  static class JoinLevels implements Command {
    public void execute(Unit pos, Local[] params) {
      logger.fine("Join levels for external class arguments");
      for (Local param : params) {
        JimpleInjector.addLevelInAssignStmt(param, pos);
      }
    }
  }
	
  static class NoHighLevelAllowed implements Command {
    public void execute(Unit pos, Local[] params) {
      logger.fine("Check that external class has no high argument");
      for (Local param: params) {
        if (param != null) {
          JimpleInjector.checkThatNotHigh(pos, param);
        }
      }
    }
  }

  static class DoNothing implements Command  {
    @Override
    public void execute(Unit pos, Local[] params) {
      logger.fine("Do nothing for external class");
    }  
  }
  
  static class MakeHigh implements Command {
    @Override
    public void execute(Unit pos, Local[] params) {
      assert(params.length == 1);
      logger.fine("Variable" + params[0].toString() + " is going to be set to high");
      JimpleInjector.makeLocalHigh(params[0], pos);
    }
  }
  
  static class MakeLow implements Command {
    @Override
    public void execute(Unit pos, Local[] params) {
 
    }
  }
}
