package utils.visitor;

import java.util.HashMap;

public class ExternalClasses {

	protected static HashMap<String, Command> classMap = new HashMap<String,Command>();
	static {
		classMap.put("jkg", new JoinLevels()); // TODO
	}
	
	protected static HashMap<String, Command> methodMap = new HashMap<String,Command>();
	static {
		methodMap.put("<java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)>", new JoinLevels()); // TODO
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
			(( TODO))
		}
	}
}
