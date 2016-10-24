package classfiletests.utils;


import java.util.logging.Logger;

import utils.logging.L1Logger;
import main.Main;


/**
 * Compiles a given class using the Main.main method.
 */
public class ClassCompiler {
	
	static Logger logger = L1Logger.getLogger();
	
	public static void compile(String name) {
		String[] args = {"-f", "c", "--classes", "main.testclasses." + name, "--main_class", "main.testclasses." + name};
		logger.info("Compilation of src file started");
		Main.main(args);
		logger.info("Compilatoin successful");
	}
}
