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

/**
 * This class gathers instrumentation information about special classes and methods.
 *
 * These include
 *
 * - Methods that we abuse as annotation syntax. Examples:
 *   - cast methods
 *   - methods to set the dynamic label of a value makeHigh, ...
 *
 * - library methods that the Java compiler uses (like StringBuilder.append, Integer.valueOf)
 *
 * - ...
 *
 * TODO: there should be a way to externally specify commands.
 */
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
		
		// Methods where the argument must have LOW argument
		methodMap.put("<java.io.PrintStream: void println(java.lang.String)>", 
				 new MaxLevelAllowedForPrintOutput("LOW"));
		methodMap.put("<java.io.PrintStream: void println(int)>", 
				 new MaxLevelAllowedForPrintOutput("LOW"));
		methodMap.put("<java.io.PrintStream: void println(boolean)>", 
				 new MaxLevelAllowedForPrintOutput("LOW"));
		methodMap.put("<java.io.PrintStream: void println(java.lang.Object)>", 
				 new MaxLevelAllowedForPrintOutput("LOW"));
		
		// Methods where the argument must have either LOW or MEDIUM argument
		methodMap.put("<utils.printer.SecurePrinter: void printMedium(java.lang.Object)>", 
				 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		methodMap.put("<utils.printer.SecurePrinter: void printMedium(java.lang.int)>", 
				 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		methodMap.put("<utils.printer.SecurePrinter: void printMedium(java.lang.String)>", 
				 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		methodMap.put("<utils.printer.SecurePrinter: void printMedium(boolean)>", 
				 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		
		// Methods where we don't do anything
		methodMap.put("<java.lang.Object: void <init>()>", new DoNothing());

		//
		methodMap.put("<de.unifreiburg.cs.proglang.jgs.support.DynamicLabel: java.lang.Object "
				+ "makeHigh(java.lang.Object)>", new MakeTop());
		methodMap.put("<de.unifreiburg.cs.proglang.jgs.support.DynamicLabel: java.lang.Object "
				+ "makeMedium(java.lang.Object)>", new MakeMedium());
		methodMap.put("<de.unifreiburg.cs.proglang.jgs.support.DynamicLabel: java.lang.Object "
				+ "makeLow(java.lang.Object)>", new MakeBot());

        // TODO: cast methods should be read from an external spec
		// casts
		methodMap.put("<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object cast(java.lang.String,java.lang.Object)>", new DoCast());

		// Dont do anything for ValueOf
        // TODO: this is almost certainly wrong.
		methodMap.put("<java.lang.Boolean: java.lang.Boolean valueOf(boolean)>", new DoNothing());
		methodMap.put("<java.lang.Integer: java.lang.Boolean valueOf(integer)>", new DoNothing());
	}
	
	static AnnotationValueSwitch.RightElement receiveCommand(String method,
															 Unit pos,
															 Local[] params) {
		return methodMap.get(method).execute(pos, params);
	}
	
	
	interface Command {
		AnnotationValueSwitch.RightElement execute(Unit pos, Local[] params);
	}
	
	static class JoinLevels implements Command {
		@Override
		public AnnotationValueSwitch.RightElement execute(Unit pos, Local[] params) {
			logger.fine("Join levels for external class arguments");
			for (Local param : params) {
				if (param != null) {
					JimpleInjector.addLevelInAssignStmt(param, pos);
				}
			}
			return RightElement.IGNORE;
		}
	}
	
	static class MaxLevelAllowedForPrintOutput implements Command {
		
		private String level;
		public MaxLevelAllowedForPrintOutput(String level) {
			this.level = level;
		}
		
		public RightElement execute(Unit pos, Local[] params) {
			logger.fine("Insert check that external class has no " + level + " arguments");
			if (params == null || pos == null) {
				throw new InternalAnalyzerException(
						"Received a null-pointer as argument");
			}
			
			// If print Statement is called, context must not be high: This, we can always check
			JimpleInjector.checkThatPCLe(level, pos);
			
			// Also, we might print in low context: If so, we mustn't print a high-sec param
			for (Local param: params) {
				if (param != null) {
					JimpleInjector.checkThatLe(param, level, pos);
					
				}
			}
			return RightElement.IGNORE;
		}
	}

	static class DoCast implements Command {
		@Override
		public RightElement execute(Unit pos, Local[] params) {
			logger.info("Cast at " + pos);
			return RightElement.CAST;
		}
	}
	
	static class DoNothing implements Command	{
		@Override
		public RightElement execute(Unit pos, Local[] params) {
			logger.fine("Do nothing for external class");
			return RightElement.IGNORE;
		}
	}
	
	static class MakeTop implements Command {
		@Override
		public RightElement execute(Unit pos, Local[] params) {
			logger.info("Right element is a makeHigh method");
			/*assert (params.length == 1);
			logger.fine("Variable" + params[0].toString() + " is set to high");
			JimpleInjector.makeLocalHigh(params[0], pos);*/
			return RightElement.MAKE_HIGH;
		}
	}
	
	static class MakeMedium implements Command {
		@Override
		public RightElement execute(Unit pos, Local[] params) {
			logger.info("Right element is a makeMedium method");
			return RightElement.MAKE_MEDIUM;
		}
	}
	
	static class MakeBot implements Command {
		@Override
		public RightElement execute(Unit pos, Local[] params) {
			return RightElement.MAKE_LOW;
		}
	}
}
