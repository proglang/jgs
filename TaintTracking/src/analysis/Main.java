package analysis;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

import exception.SecurityLevelException;

import logging.SecurityLogger;
import logging.SootLoggerConfiguration;
import model.Configurations;
import model.Configurations.Config;
import security.SecurityAnnotation;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.SimpleDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import utils.ExtendedSecurityLevelImplChecker;
import utils.GeneralUtils;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * 
 * 
 * @author Thomas Vogel
 * @version 0.4
 */
public class Main {

	/** */
	private static final String OFF = "OFF";
	/** */
	private static final String ON = "ON";
	/**  */
	private static final String PHASE_NAME = "jtp.tainttracking";
	/**  */
	private static Level[] logLevels = {};
	/**  */
	private static SecurityLogger log;
	/**  */
	private static boolean exportFile;
	/**  */
	private static SecurityAnnotation securityAnnotation;
	
	/**
	 * 
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 */
	public static class SecurityTransformer extends BodyTransformer {
		
		/**
		 * 
		 * 
		 * @param b
		 * @param phaseName
		 * @param options
		 * @see soot.BodyTransformer#internalTransform(soot.Body, java.lang.String, java.util.Map)
		 */
		@Override
		protected void internalTransform(Body b, String phaseName, Map options) {
			UnitGraph g = new BriefUnitGraph(b);
			SootMethod sootMethod = g.getBody().getMethod();
			if (! securityAnnotation.isMethodOfSootSecurity(sootMethod)) {
				log.structure(SootUtils.generateMethodSignature(sootMethod));
				log.addAdditionalHandlerFor(sootMethod);				
				TaintTracking tt = new TaintTracking(log, sootMethod, securityAnnotation, g);
				tt.checkAnalysis();
				log.removeAdditional();
			}
		}
		
	}

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		args = GeneralUtils.precheckArguments(args);
		log = new SecurityLogger(exportFile, logLevels);
		log.configuration(
				new Configurations(new Config(SootLoggerConfiguration.EXPORT_FILE_TXT, (exportFile ? ON : OFF)), 
						new Config(SootLoggerConfiguration.LOG_LEVELS_TXT, generateLevelList())));
		try {
			ExtendedSecurityLevelImplChecker extendedSecurityLevelImplChecker = ExtendedSecurityLevelImplChecker.getExtendedSecurityLevelImplChecker(log, true, true);
			String[] orderedLevels = extendedSecurityLevelImplChecker.getOrderedLevels();
			securityAnnotation = new SecurityAnnotation(new ArrayList<String>(Arrays.asList(orderedLevels)));
			PackManager.v().getPack("jtp").add(new Transform(PHASE_NAME, new SecurityTransformer()));
			soot.Main.main(args);
		} catch (SecurityLevelException e) {
			log.securitychecker(SecurityMessages.reflectionInvalidSootSecurityLevelClass(), e);
		}
		log.storeSerializedMessageStore();
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
