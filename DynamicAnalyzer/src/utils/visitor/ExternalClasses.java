package utils.visitor;

import java.util.ArrayList;
import java.util.HashMap;

import soot.Local;
import soot.Unit;
import analyzer.level1.JimpleInjector;

public class ExternalClasses {

	protected static ArrayList<String> classMap = new ArrayList<String>();
	static {
		classMap.add("java.lang.StringBuilder"); // TODO notwendig?
	}
	
	protected static HashMap<String, Command> methodMap = new HashMap<String,Command>();
	static {
		methodMap.put("<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>", new JoinLevels());
		methodMap.put("<java.lang.String: java.lang.String substring(int,int)>", new JoinLevels());
		methodMap.put("<java.io.PrintStream: void println(java.lang.String)>", new NoHighLevelAllowed());
	}
	
	void receiveCommand(String command,Unit pos, Local params[]) {
	    methodMap.get(command).execute(pos, params);
	}
	
	
	interface Command {
		void execute(Unit pos, Local params[]);
	}
	
	static class JoinLevels implements Command {
		public void execute(Unit pos, Local params[]) {
			for (Local param : params) {
				JimpleInjector.addLevelInAssignStmt(param, pos);
			}
		}
	}
	
	static class NoHighLevelAllowed implements Command {
		public void execute(Unit pos, Local params[]) {
			for (Local param: params) {
				JimpleInjector.checkThatNotHigh(pos, param);
			}
		}
	}
}
