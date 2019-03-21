package util.visitor;

import analyzer.level1.JimpleInjector;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import util.exceptions.InternalAnalyzerException;
import util.logging.L1Logger;
import util.visitor.AnnotationValueSwitch.RequiredActionForRHS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * This class gathers instrumentation information about special classes and methods.
 *
 * These include
 *
 * - MethodTypings that we abuse as annotation syntax. Examples:
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

	private static final Logger logger = Logger.getLogger(ExternalClasses.class.getName());

	protected static ArrayList<String> classMap = new ArrayList<String>();

	static {
		classMap.add("java.lang.StringBuilder"); // TODO notwendig?
	}
	
	private static HashMap<String, Command> instrumentationForSpecialMethods = new HashMap<String,Command>();

	static {
		
		// MethodTypings where the return level is the join of the arguments levels
		instrumentationForSpecialMethods.put("<java.lang.StringBuilder: void <init>()>",
											 new DoNothing());
		instrumentationForSpecialMethods.put("<java.lang.StringBuilder: java.lang.StringBuilder "
											 + "append(java.lang.String)>",
											 new JoinLevels());
		instrumentationForSpecialMethods.put("<java.lang.StringBuilder: java.lang.StringBuilder "
											 + "append(int)>",
											 new JoinLevels());
		instrumentationForSpecialMethods.put("<java.lang.String: java.lang.String "
											 + "substring(int,int)>", new JoinLevels());

		instrumentationForSpecialMethods.put("<java.lang.StringBuilder: java.lang.String toString()>", new JoinLevels());
		instrumentationForSpecialMethods.put("<java.lang.String: java.lang.String toString()>", new JoinLevels());
		instrumentationForSpecialMethods.put("<de.unifreiburg.cs.proglang.jgs.support.StringUtil: java.util.List bits(java.lang.String)>", new JoinLevels());

		// MethodTypings where the argument must have LOW argument
		instrumentationForSpecialMethods.put("<java.io.PrintStream: void println(java.lang.String)>",
											 new MaxLevelAllowedForPrintOutput
													 ("LOW"));
		instrumentationForSpecialMethods.put("<java.io.PrintStream: void print(java.lang.String)>",
											 new MaxLevelAllowedForPrintOutput("LOW"));
		instrumentationForSpecialMethods.put("<java.io.PrintStream: void println(int)>",
											 new MaxLevelAllowedForPrintOutput("LOW"));
		instrumentationForSpecialMethods.put("<java.io.PrintStream: void println(boolean)>",
											 new MaxLevelAllowedForPrintOutput("LOW"));
		instrumentationForSpecialMethods.put("<java.io.PrintStream: void println(java.lang.Object)>",
											 new MaxLevelAllowedForPrintOutput("LOW"));
		instrumentationForSpecialMethods.put("<java.io.PrintStream: void println()>",
											 new MaxLevelAllowedForPrintOutput("LOW"));
		instrumentationForSpecialMethods.put("<de.unifreiburg.cs.proglang.jgs.support.IOUtils: void printPublicDynamic(java.lang.String)>",
											 new MaxLevelAllowedForPrintOutput("LOW"));

		// MethodTypings where the argument must have either LOW or MEDIUM argument
		instrumentationForSpecialMethods.put("<util.printer.SecurePrinter: void printMedium(java.lang.Object)>",
											 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		instrumentationForSpecialMethods.put("<util.printer.SecurePrinter: void printMedium(java.lang.int)>",
											 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		instrumentationForSpecialMethods.put("<util.printer.SecurePrinter: void printMedium(java.lang.String)>",
											 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		instrumentationForSpecialMethods.put("<util.printer.SecurePrinter: void printMedium(boolean)>",
											 new MaxLevelAllowedForPrintOutput("MEDIUM"));
		
		// MethodTypings where we don't do anything
		instrumentationForSpecialMethods.put("<java.lang.Object: void <init>()>", new DoNothing());

		//
		instrumentationForSpecialMethods.put("<de.unifreiburg.cs.proglang.jgs"
											 + ".support.DynamicLabel: java.lang.Object "
											 + "makeHigh(java.lang.Object)>", new MakeTop());
		instrumentationForSpecialMethods.put("<de.unifreiburg.cs.proglang.jgs"
											 + ".support.DynamicLabel: java.lang.Object "
											 + "makeMedium(java.lang.Object)>", new MakeMedium());
		instrumentationForSpecialMethods.put("<de.unifreiburg.cs.proglang.jgs.support.DynamicLabel: java.lang.Object "
											 + "makeLow(java.lang.Object)>", new MakeBot());

		// Dont do anything for ValueOf... its level is determined by the
		// method's receiver
		instrumentationForSpecialMethods.put("<java.lang.Boolean: java.lang"
											 + ".Boolean valueOf(boolean)>", new DoNothing());
		instrumentationForSpecialMethods.put("<java.lang.Integer: java.lang.Boolean valueOf(integer)>", new JoinLevels());
		instrumentationForSpecialMethods.put("<java.lang.Integer: java.lang.Integer valueOf(int)>", new JoinLevels());
		instrumentationForSpecialMethods.put("<java.lang.Integer: java.lang.String valueOf(int)>", new JoinLevels());
		instrumentationForSpecialMethods.put("<java.lang.Integer: int intValue()>", new DoNothing());
		instrumentationForSpecialMethods.put("<java.util.List: java.util.Iterator iterator()>", new DoNothing());
		instrumentationForSpecialMethods.put("<java.util.Iterator: boolean hasNext()>", new DoNothing());
		instrumentationForSpecialMethods.put("<java.util.Iterator: java.lang.Object next()>", new DoNothing());
		instrumentationForSpecialMethods.put("<java.lang.Boolean: boolean "
											 + "booleanValue()>", new DoNothing());

		// Handling of uninstrumented methods that occur in the testcases
		instrumentationForSpecialMethods.put("<testclasses.util.SimpleObject: void <init>()>", new DoNothing());
	}

	public static boolean isSpecialMethod(SootMethod m) {
		return instrumentationForSpecialMethods.containsKey(m.toString());
	}

	static Optional<AnnotationValueSwitch.RequiredActionForRHS> instrumentSpecialMethod(SootMethod method,
																			  Unit pos,
																			  Local[] params) {
		return instrumentationForSpecialMethods.get(method.toString())
											   .execute(pos, params);
	}
	
	
	interface Command {
		Optional<RequiredActionForRHS> execute(Unit pos, Local[] params);
	}

	// TODO: the commands should be abstract. Then we can move ExternalClasses to InstrumentationSupport (where it belongs, together with the external signatures for type checking
	static class JoinLevels implements Command {
		@Override
		public Optional<RequiredActionForRHS> execute(Unit pos, Local[] params) {
			logger.fine("Join levels for external class arguments");
			for (Local param : params) {
				if (param != null) {
					JimpleInjector.addLevelInAssignStmt(param, pos);
				}
			}
			return Optional.empty();
		}
	}
	
	static class MaxLevelAllowedForPrintOutput implements Command {
		
		private String level;
		public MaxLevelAllowedForPrintOutput(String level) {
			this.level = level;
		}
		
		public Optional<AnnotationValueSwitch.RequiredActionForRHS> execute(Unit pos, Local[] params) {
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
			return Optional.empty();
		}
	}

	static class DoCast implements Command {
		@Override
		public Optional<RequiredActionForRHS> execute(Unit pos, Local[] params) {
			logger.info("Cast at " + pos);
			return Optional.of(AnnotationValueSwitch.RequiredActionForRHS.CAST);
		}
	}
	
	static class DoNothing implements Command	{
		@Override
		public Optional<RequiredActionForRHS> execute(Unit pos, Local[] params) {
			logger.fine("Do nothing for external class");
			return Optional.empty();
		}
	}
	
	static class MakeTop implements Command {
		@Override
		public Optional<RequiredActionForRHS> execute(Unit pos, Local[] params) {
			logger.info("Right element is a makeHigh method");
			/*assert (params.length == 1);
			logger.fine("Variable" + params[0].toString() + " is set to high");
			JimpleInjector.makeLocalHigh(params[0], pos);*/
			return Optional.of(RequiredActionForRHS.MAKE_HIGH);
		}
	}
	
	static class MakeMedium implements Command {
		@Override
		public Optional<RequiredActionForRHS> execute(Unit pos, Local[] params) {
			logger.info("Right element is a makeMedium method");
			return Optional.of(RequiredActionForRHS.MAKE_MEDIUM);
		}
	}
	
	static class MakeBot implements Command {
		@Override
		public Optional<RequiredActionForRHS> execute(Unit pos, Local[] params) {
			return Optional.of(RequiredActionForRHS.MAKE_LOW);
		}
	}
}
