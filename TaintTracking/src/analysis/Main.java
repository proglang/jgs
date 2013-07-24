package analysis;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

import logging.SecurityLogger;
import model.Configurations;
import model.Configurations.Config;
import security.SecurityAnnotation;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.SootMethod;
import soot.Transform;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import utils.GeneralUtils;
import utils.SecurityLevelChecker;
import utils.SootUtils;

public class Main {
	
	private static final String PHASE_NAME = "jtp.tainttracking";

	private static Level[] LOG_LEVEL = {};
	private static SecurityLogger LOG;
	private static boolean EXPORT_JIMPLE;
	private static boolean EXPORT_FILE;
	private static SecurityAnnotation SECURITY_ANNOTATIONS;
	
	public static class SecurityTransformer extends BodyTransformer {
		
		@Override
		protected void internalTransform(Body b, String phaseName, Map options) {
			UnitGraph g = new BriefUnitGraph(b);
			SootMethod sootMethod = g.getBody().getMethod();
			if (! SECURITY_ANNOTATIONS.isMethodOfSootSecurity(sootMethod)) {
				LOG.structure(SootUtils.generateMethodSignature(sootMethod));
				LOG.addAdditionalHandlerFor(sootMethod);
				TaintTracking tt = new TaintTracking(LOG, sootMethod, SECURITY_ANNOTATIONS, g);
				LOG.removeAdditional();
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args = GeneralUtils.precheckArguments(args);
		LOG = new SecurityLogger(EXPORT_FILE, EXPORT_JIMPLE, LOG_LEVEL);
		LOG.configuration(
				new Configurations(new Config("Export File", (EXPORT_FILE ? "ON" : "OFF")), 
						new Config("Export Jimple", (EXPORT_JIMPLE ? "ON" : "OFF")), 
						new Config("Log level", generateLevelList())));
		if (SecurityLevelChecker.checkSecurityLevel(LOG)) {
			String[] orderedLevels = new String[] {};
			try {
				orderedLevels = SecurityLevelChecker.getOrderSecurityLevels();
			} catch (NullPointerException e) {
				LOG.exception("Couldn't fetch the security levels from the SootSecurityLevel file. The analysis will not performed.", e);
				return;
			}
			SECURITY_ANNOTATIONS = new SecurityAnnotation(new ArrayList<String>(Arrays.asList(orderedLevels)));
			PackManager.v().getPack("jtp").add(new Transform(PHASE_NAME,new SecurityTransformer()));
			soot.Main.main(args);
		} else {
			LOG.securitychecker("The SootSecurityLevel class is invalid. The analysis will not performed.");
			return;
		}
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
