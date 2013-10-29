package analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import logging.SecurityLogger;
import logging.SootLoggerLevel;
import logging.SootLoggerUtils;
import model.Settings;
import security.SecurityAnnotation;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import utils.ExtendedSecurityLevelImplChecker;
import utils.GeneralUtils;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.SecurityLevelException;

/**
 * <h1>Main class of the security analysis</h1>
 * 
 * Main class of the security analysis which uses the {@link SecurityTransformer} to transform the
 * body of the methods, as well as the {@link TaintTracking} analysis to check for violations
 * against the <em>security level</em> and <em>write effect</em> policy.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.5
 */
public class Main {

	

	/**
	 * <h1>Security Transformer</h1>
	 * 
	 * The class {@link SecurityTransformer} acts on a {@link Body}. This class provides a harness
	 * and acts as an interface for classes that wish to transform a {@link Body}. Subclasses
	 * provide the actual Body transformation implementation.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 */
	public static class SecurityTransformer extends BodyTransformer {
		
		/** TODO */
		private List<SootClass> visitedClasses = new ArrayList<SootClass>();
		
		/**
		 * TODO
		 * 
		 * @param sootMethod
		 * @param graph
		 */
		private void doAnalysis(SootMethod sootMethod, UnitGraph graph){
			if (!securityAnnotation.isMethodOfSootSecurityLevelClass(sootMethod)) {
				if (instantLogging) {
					log.structure(SootUtils.generateMethodSignature(sootMethod, false, true, false));
					log.addStandardFileHandlerForMethod(sootMethod);
				}
				TaintTracking tt = new TaintTracking(log, sootMethod, securityAnnotation, graph);
				tt.checkAnalysis();
				if (instantLogging)
					log.removeStandardFileHandler();
			}
		}

		/**
		 * This method is called to perform the transformation itself. The method executes the
		 * {@link TaintTracking} analysis and checks the <em>write effects</em>, if the given body
		 * isn't from a {@code SootSecurityLevel} method.
		 * 
		 * @param body
		 *            The body on which to apply the transformation.
		 * @param phaseName
		 *            The phase name for this transform; not typically used by implementations.
		 * @param options
		 *            The actual computed options; a combination of default options and Scene
		 *            specified options.
		 * @see soot.BodyTransformer#internalTransform(soot.Body, java.lang.String, java.util.Map)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		protected void internalTransform(Body body, String phaseName, Map options) {
			UnitGraph graph = new BriefUnitGraph(body);
			SootMethod sootMethod = graph.getBody().getMethod();
			SootClass sootClass = sootMethod.getDeclaringClass();
			if (! visitedClasses.contains(sootClass)) {				
				if (! SootUtils.containsStaticInitializer(sootClass.getMethods())) {
					SootMethod clinit = SootUtils.generatedEmptyStaticInitializer(sootClass);
					UnitGraph clinitGraph = new BriefUnitGraph(clinit.getActiveBody());
					doAnalysis(clinit, clinitGraph);
				} 
				visitedClasses.add(sootClass);
			}
			if (!securityAnnotation.isMethodOfSootSecurityLevelClass(sootMethod)) {
				doAnalysis(sootMethod, graph);
			}
		}
	}

	/** Indicates whether the messages should be logged also in a file. */
	private static boolean exportFile = false;
	/**
	 * Indicates whether the messages should be exported exactly in the moment the logger receives
	 * the message.
	 */
	private static boolean instantLogging = false;
	/** The logger that allows to log informations of different {@link SootLoggerLevel}. */
	private static SecurityLogger log;
	/** Array of levels which are enabled for the logging. */
	private static Level[] logLevels = {};
	/** The transformers phase name. */
	private static final String PHASE_NAME = "jtp.tainttracking";
	/** The security annotation object that provides the handling of <em>security level</em>. */
	private static SecurityAnnotation securityAnnotation;

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

	/**
	 * Enables the export of the console output to a file.
	 */
	public static void exportFile() {
		exportFile = true;
	}

	/**
	 * Enables the instant logging.
	 */
	public static void instantLogging() {
		instantLogging = true;
	}

	/**
	 * Method that starts the transformation as well as the analysis. The given options can contain
	 * the normal Soot options and also the following customized options:
	 * <ul>
	 * <li>{@code -instant-logging}: All logged messages are displayed immediately</li>
	 * <li>{@code -export-file}: All logged messages are exported to a file</li>
	 * <li>{@code -log-levels}: Expects a list of the log-levels {@code exception}, {@code error},
	 * {@code warning}, {@code information}, {@code effect}, {@code security},
	 * {@code securitychecker}, {@code debug}, {@code all}, {@code off}, {@code structure} and
	 * {@code configuration}. Messages of the listed levels are printed from the logger.</li>
	 * </ul>
	 * To perform the analysis, the implementation of the subclass of the class
	 * {@code SecurityLevel} is compiled, checked and finally the available <em>security levels</em>
	 * exported.
	 * 
	 * @param args
	 *            Command line options for the transformation and the security analysis.
	 */
	public static void main(String[] args) {
		args = GeneralUtils.precheckArguments(args);
		log = new SecurityLogger(exportFile, instantLogging, logLevels);
		log.configuration(new Settings(SootLoggerUtils.makeExportFileSetting(exportFile),
				SootLoggerUtils.makeLoggerLevelSetting(logLevels), SootLoggerUtils
						.makeInstantLoggingSetting(instantLogging), SootLoggerUtils
						.makeTimeSetting()));
		try {
			ExtendedSecurityLevelImplChecker extendedSecurityLevelImplChecker = ExtendedSecurityLevelImplChecker
					.getExtendedSecurityLevelImplChecker(log, true, true);
			String[] orderedLevels = extendedSecurityLevelImplChecker.getOrderedLevels();
			securityAnnotation = new SecurityAnnotation(new ArrayList<String>(
					Arrays.asList(orderedLevels)));
			PackManager.v().getPack("jtp")
					.add(new Transform(PHASE_NAME, new SecurityTransformer()));
			soot.Main.main(args);
		} catch (SecurityLevelException e) {
			log.securitychecker(SecurityMessages.reflectionInvalidSootSecurityLevelClass(), e);
		}
		if (!instantLogging)
			log.printAllMessages();
		log.storeSerializedMessageStore();
	}

}
