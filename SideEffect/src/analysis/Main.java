package analysis;

import java.util.*;
import java.util.logging.*;

import logging.*;
import model.Configurations;
import model.Configurations.Config;
import model.MessageStore.Message;

import soot.*;
import soot.tagkit.Host;
import soot.tagkit.Tag;
import soot.toolkits.graph.*;
import utils.*;

/**
 * Main class of analysis. There are some special parameters for this analysis:
 * <ol>
 * <li>{@code -export-jimple} : a file with the jimple source code will be generated for each analyzed method.</li>
 * <li>{@code -export-file} : a file with the console output will be generated for each analyzed method.</li>
 * <li>{@code -check-classes} : the analysis will be stronger - a method requires also the clinit effects.</li>
 * <li>{@code -log-levels} : followed by a comma separated list of levels which should be printed. (see logging levels)</li>
 * </ol>
 * <h3>Logging levels:</h3> This logger ({@link SootLog}) defines following actions:
 * <ol>
 * <li><b>EXCEPTION</b>: Exceptions</li>
 * <li><b>ERROR</b>: Analysis errors</li>
 * <li><b>WARNING</b>: Analysis warnings</li>
 * <li><b>INFORMATION</b>: Other analysis information</li>
 * <li><b>CONFIGURATION</b>: Configuration information</li>
 * <li><b>STRUCTURE</b>: Structural information</li>
 * <li><b>DEBUG</b>: Debug information</li>
 * <li><b>JIMPLE</b>: Jimple Source Code</li>
 * </ol>
 * 
 * 
 * @author Thomas Vogel
 * @version 0.3
 * 
 */
public class Main {

	private static SideEffectLogger LOG;
	private static boolean CHECK_CLASSES = false;
	private static Level[] LOG_LEVEL = {};
	private static boolean EXPORT_JIMPLE = false;
	private static boolean EXPORT_FILE = false;

	private static final String PHASE_NAME = "jtp.sideeffect";

	private static class EffectTransformer extends BodyTransformer {

		@Override
		protected void internalTransform(Body body, String phaseName, Map options) {
			UnitGraph g = new BriefUnitGraph(body);
			SootMethod sootMethod = g.getBody().getMethod();
			LOG.structure(SootUtils.generateMethodSignature(sootMethod));
			LOG.addAdditionalHandlerFor(sootMethod);
			SideEffectAnalysis se = new SideEffectAnalysis(g, g.getBody().getMethod(), LOG,
					CHECK_CLASSES);
			se.checkAnnotation();
			LOG.removeAdditional();
		}
	}

	public static void main(String[] args) {
		args = GeneralUtils.precheckArguments(args);
		LOG = new SideEffectLogger(EXPORT_FILE, EXPORT_JIMPLE, LOG_LEVEL);
		LOG.configuration(new Configurations(
				new Config("Export File", (EXPORT_FILE ? "ON" : "OFF")), new Config(
						"Export Jimple", (EXPORT_JIMPLE ? "ON" : "OFF")), new Config(
						"Check Classes", (CHECK_CLASSES ? "ON" : "OFF")), new Config("Log levels",
						generateLevelList())));
		PackManager.v().getPack("jtp").add(new Transform(PHASE_NAME, new EffectTransformer()));
		soot.Main.main(args);
	}

	/**
	 * Generates a comma separated list of the selected levels.
	 * 
	 * @return List of levels as String.
	 */
	public static String generateLevelList() {
		String levels = "";
		for (Level level : LOG_LEVEL) {
			if (!levels.equals(""))
				levels += ", ";
			levels += level.getLocalizedName();
		}
		return levels;
	}

	/**
	 * Enables the check of classes.
	 */
	public static void checkClasses() {
		CHECK_CLASSES = true;
	}

	/**
	 * Enables the export of the jimple source code to a file.
	 */
	public static void exportJimple() {
		EXPORT_JIMPLE = true;
	}

	/**
	 * Enables the export of the console output to a file.
	 */
	public static void exportFile() {
		EXPORT_FILE = true;
	}

	/**
	 * Adds the given level to the array which contains the levels that are printed.
	 * 
	 * @param level
	 *            Level which should be printed.
	 */
	public static void addLevel(Level level) {
		Level[] levels = new Level[LOG_LEVEL.length + 1];
		System.arraycopy(LOG_LEVEL, 0, levels, 0, LOG_LEVEL.length);
		levels[LOG_LEVEL.length] = level;
		LOG_LEVEL = levels;
	}
}