package classfiletests.utils;


import java.util.logging.Logger;

import utils.logging.L1Logger;
import main.Main;


/**
 * Compiles a given class using the Main.main method.
 * 
 * @author Nicolas Müller
 */
public class ClassCompiler {
	
	static Logger logger = L1Logger.getLogger();
	
	/**
	 * Static Helper Method to compile a given test, and put it into sootOutput/outputDir
	 * @param name name of the test
	 * @param outputDir subfolder of sootOutput to put compiled binary
	 */
	public static void compile(String name, String outputDir) {
		
		String[] args = {"-f", "c", "--classes", 
				"main.testclasses." + name, "--main_class", "main.testclasses." + name, "--d", "sootOutput/" + outputDir};
		logger.info("Compilation of src file started");
		Main.main(args);
		logger.info("Compilation successful, binary put in sootOutput/" + outputDir);
	}
}
