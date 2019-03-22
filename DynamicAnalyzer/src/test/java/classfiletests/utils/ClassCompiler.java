package classfiletests.utils;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import util.logging.L1Logger;
import main.Main;
import de.unifreiburg.cs.proglang.jgs.typing.FixedTypings;

/**
 * Compiles a given class using the main.Main.main method.
 * @author Nicolas Müller
 */
public class ClassCompiler {

	private static final Logger logger = Logger.getLogger(ClassCompiler.class.getName());
	// TODO: the cast method names, as well as the secdomain should not be hardcoded at this point
	private static final Casts<LowMediumHigh.Level> casts =
			new CastsFromConstants<>(new TypeDomain<>(new LowMediumHigh()),
									 "<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
									 "de.unifreiburg.cs.proglang.jgs.instrumentation.Casts.castCx",
									 "de.unifreiburg.cs.proglang.jgs.instrumentation.Casts.castCxEnd");

	/**
	 * Static Helper Method to compile a given test, and put it into
	 * sootOutput/outputDir
	 * 
	 * @param name		name of the test
	 * @param outputDir	subfolder of the sootOutput/ directory to put compiled binary
	 */
	public static void compile(String name, String outputDir) throws UnsupportedEncodingException {

		String[] args = {"-m", "testclasses." + name, "-o", "sootOutput/" + outputDir};
		logger.info("Compilation of src file started");
		Main.execute(args, FixedTypings.allDynamic(), casts);
		logger.info("Compilation successful, binary put in sootOutput/"
				+ outputDir);
	}

	public static void compileWithFakeTyping(String name, String outputDir,
											 MethodTypings<LowMediumHigh.Level> methodTypings) throws UnsupportedEncodingException {

		String[] args = {"-m", "testclasses." + name, "-o", "sootOutput/" + outputDir};
		logger.info("Compilation of src file started. Using fake static analysis results");

		Main.execute(args, methodTypings, casts);
		logger.info("Compilation successful, binary put in sootOutput/"
				+ outputDir);
	}
}
