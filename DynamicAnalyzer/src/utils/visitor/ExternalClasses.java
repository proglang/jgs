package utils.visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.Unit;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import utils.visitor.AnnotationValueSwitch.RightElement;

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
		
		// Methods where the return level is the join of the arguments levels
		methodMap.put("<java.lang.StringBuilder: java.lang.StringBuilder "
					+ "append(java.lang.String)>",
					new JoinLevels());
		methodMap.put("<java.lang.String: java.lang.String "
				+ "substring(int,int)>", new JoinLevels());
		
		// Methods where the argument cannot have a High argument
		methodMap.put("<java.io.PrintStream: void println(java.lang.String)>", 
				 new NotAllowedForPrintOutput("MEDIUM"));
		methodMap.put("<java.io.PrintStream: void println(int)>", 
				 new NotAllowedForPrintOutput("MEDIUM"));
		methodMap.put("<java.io.PrintStream: void println(boolean)>", 
				 new NotAllowedForPrintOutput("MEDIUM"));
		methodMap.put("<java.io.PrintStream: void println(java.lang.Object)>", 
				 new NotAllowedForPrintOutput("MEDIUM"));
		
		// Methods 
		
		// Methods where we don't do anything
		methodMap.put("<java.lang.Object: void <init>()>", new DoNothing());
		
		methodMap.put("<utils.analyzer.HelperClass: java.lang.Object "
				+ "makeHigh(java.lang.Object)>", new MakeHigh());
		methodMap.put("<utils.analyzer.HelperClass: java.lang.Object "
				+ "makeMedium(java.lang.Object)>", new MakeMedium());
		methodMap.put("<utils.analyzer.HelperClass: java.lang.Object "
				+ "makeLow(java.lang.Object)>", new MakeLow());
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
				if (param != null) {
					JimpleInjector.addLevelInAssignStmt(param, pos);
				}
			}
		}
	}
	
	static class NotAllowedForPrintOutput implements Command {
		
		private String level;
		public NotAllowedForPrintOutput(String level) {
			this.level = level;
		}
		
		public void execute(Unit pos, Local[] params) {
			logger.fine("Insert check that external class has no " + level + " arguments");
			if (params == null || pos == null) {
				throw new InternalAnalyzerException(
						"Received a null-pointer as argument");
			}
			
			// If print Statement is called, context must not be high: This, we can always check
			JimpleInjector.checkThatPCLessThan(level, pos);
			
			// Also, we might print in low context: If so, we mustn't print a high-sec param
			for (Local param: params) {
				if (param != null) {
					JimpleInjector.checkThatNot(param, level, pos);
					
				}
			} 
		}
	}
	
	static class DoNothing implements Command	{
		@Override
		public void execute(Unit pos, Local[] params) {
			logger.fine("Do nothing for external class");
		}	
	}
	
	static class MakeHigh implements Command {
		@Override
		public void execute(Unit pos, Local[] params) {
			logger.info("Right element is a makeHigh method");
			/*assert (params.length == 1);
			logger.fine("Variable" + params[0].toString() + " is set to high");
			JimpleInjector.makeLocalHigh(params[0], pos);*/
			AnnotationValueSwitch.rightElement = RightElement.MAKE_HIGH;
		}
	}
	
	static class MakeMedium implements Command {
		@Override
		public void execute(Unit pos, Local[] params) {
			logger.info("Right element is a makeMedium method");
			AnnotationValueSwitch.rightElement = RightElement.MAKE_MEDIUM;
		}
	}
	
	static class MakeLow implements Command {
		@Override
		public void execute(Unit pos, Local[] params) {
			AnnotationValueSwitch.rightElement = RightElement.MAKE_LOW;
		}
	}
}
