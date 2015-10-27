package utils.visitor;

import java.util.ArrayList;
import java.util.HashMap;

public class ExternalClasses {

	protected static ArrayList<String> classMap = new ArrayList<String>();
	static {
		classMap.add("java.lang.StringBuilder"); // TODO notwendig?
	}
	
	protected static HashMap<String, Command> methodMap = new HashMap<String,Command>();
	static {
		methodMap.put("<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>", new JoinLevels()); // TODO
		methodMap.put("<java.lang.String: java.lang.String substring(int,int)>", new JoinLevels()); // TODO
		
	}
	
	void receiveCommand(String command, Object params[]) {
	    methodMap.get(command).execute(params);
	}
	
	
	interface Command {
		void execute(Object params[]);
	}
	
	static class JoinLevels implements Command {
		public void execute(Object params[]) {
			// TODO
		}
	}
	
	static class NoHighLevelAllowed implements Command {
		public void execute(Object params[]) {
			// TODO
		}
	}
}
