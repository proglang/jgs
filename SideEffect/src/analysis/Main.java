package analysis;

import java.util.*;
import java.util.logging.*;

import logging.*;
import model.Settings;
import model.Settings.Setting;

import soot.*;
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
 * </ol>
 * 
 * 
 * @author Thomas Vogel
 * @version 0.3
 * 
 */
public class Main {

	private static SideEffectLogger log;
	private static boolean checkClasses = false;
	private static Level[] logLevels = {};
	private static boolean exportFile = false;

	private static final String PHASE_NAME = "jtp.sideeffect";

	private static class EffectTransformer extends BodyTransformer {

		@Override
		protected void internalTransform(Body body, String phaseName, Map options) {
			UnitGraph g = new BriefUnitGraph(body);
			SootMethod sootMethod = g.getBody().getMethod();
			log.structure(SootUtils.generateMethodSignature(sootMethod, false, true, false));
			log.addStandardFileHandlerForMethod(sootMethod);
			SideEffectAnalysis se = new SideEffectAnalysis(g, g.getBody().getMethod(), log,
					checkClasses);
			se.checkAnnotation();
			log.removeStandardFileHandler();
		}
	}

	public static void main(String[] args) {
		args = GeneralUtils.precheckArguments(args);
		log = new SideEffectLogger(exportFile, logLevels);
		log.configuration(new Settings(
				new Setting("Export File", (exportFile ? "ON" : "OFF")), new Setting(
						"Check Classes", (checkClasses ? "ON" : "OFF")), new Setting("Log levels",
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
		for (Level level : logLevels) {
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
		checkClasses = true;
	}

	/**
	 * Enables the export of the console output to a file.
	 */
	public static void exportFile() {
		exportFile = true;
	}

	/**
	 * Adds the given level to the array which contains the levels that are printed.
	 * 
	 * @param level
	 *            Level which should be printed.
	 */
	public static void addLevel(Level level) {
		Level[] levels = new Level[logLevels.length + 1];
		System.arraycopy(logLevels, 0, levels, 0, logLevels.length);
		levels[logLevels.length] = level;
		logLevels = levels;
	}
}