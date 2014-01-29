package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import analysis.SecurityTransformer;
import analysis.TaintTracking;
import preanalysis.AnnotationExtractor;

import logging.SecurityLogger;
import logging.SootLoggerLevel;
import logging.SootLoggerUtils;
import model.MessageStore;
import model.Settings;
import security.SecurityAnnotation;
import soot.PackManager;
import soot.Transform;
import utils.ExtendedSecurityLevelImplChecker;
import utils.GeneralUtils;
import utils.SecurityMessages;
import exception.SootException.SecurityLevelException;

/**
 * <h1>Main class of the security analysis</h1>
 * 
 * Main class of the security analysis which uses the
 * {@link SecurityTransformer} to transform the body of the methods, as well as
 * the {@link TaintTracking} analysis to check for violations against the
 * <em>security level</em> and <em>write effect</em> policy.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.5
 */
public class Main {

	/** Indicates whether the messages should be logged also in a file. */
	private static boolean exportFile = false;
	/**
	 * Indicates whether the messages should be exported exactly in the moment
	 * the logger receives the message.
	 */
	private static boolean instantLogging = false;
	/**
	 * The logger that allows to log informations of different
	 * {@link SootLoggerLevel}.
	 */
	private static SecurityLogger log;
	/** Array of levels which are enabled for the logging. */
	private static Level[] logLevels = {};
	/** The annotation transformer phase name. */
	private static final String PHASE_NAME_ANNOTATION = "wjtp.annotation";
	/** The security transformer phase name. */
	private static final String PHASE_NAME_SECURITY = "jtp.security";
	/**
	 * The security annotation object that provides the handling of
	 * <em>security level</em>.
	 */
	private static SecurityAnnotation securityAnnotation;

	/**
	 * Adds the given level to the array which contains the levels that are
	 * printed.
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
	 * Method that starts the transformation as well as the analysis. The given
	 * options can contain the normal Soot options and also the following
	 * customized options:
	 * <ul>
	 * <li>{@code -instant-logging}: All logged messages are displayed
	 * immediately</li>
	 * <li>{@code -export-file}: All logged messages are exported to a file</li>
	 * <li>{@code -log-levels}: Expects a list of the log-levels
	 * {@code exception}, {@code error}, {@code warning}, {@code information},
	 * {@code effect}, {@code security}, {@code securitychecker}, {@code debug},
	 * {@code all}, {@code off}, {@code structure} and {@code configuration}.
	 * Messages of the listed levels are printed from the logger.</li>
	 * </ul>
	 * To perform the analysis, the implementation of the subclass of the class
	 * {@code SecurityLevel} is compiled, checked and finally the available
	 * <em>security levels</em> exported.
	 * 
	 * @param args
	 *            Command line options for the transformation and the security
	 *            analysis.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		execute(args);
		if (log != null && Configuration.MESSAGE_STORE_PERSISTENT) {
			log.storeSerializedMessageStore();
		}
	}

	/**
	 * DOC
	 * 
	 * @param args
	 * @return
	 */
	public static MessageStore executeAndReturnMessageStore(String[] args) {
		execute(args);
		return (log != null) ? log.getMessageStore() : new MessageStore();
	}

	/**
	 * DOC
	 * 
	 * @param args
	 */
	private static void execute(String[] args) {
		args = GeneralUtils.precheckArguments(args);
		log = new SecurityLogger(exportFile, instantLogging, logLevels);
		log.configuration(new Settings(SootLoggerUtils
				.makeExportFileSetting(exportFile), SootLoggerUtils
				.makeLoggerLevelSetting(logLevels), SootLoggerUtils
				.makeInstantLoggingSetting(instantLogging), SootLoggerUtils
				.makeTimeSetting()));
		try {
			ExtendedSecurityLevelImplChecker extendedSecurityLevelImplChecker = ExtendedSecurityLevelImplChecker
					.getExtendedSecurityLevelImplChecker(log, true, true);
			String[] orderedLevels = extendedSecurityLevelImplChecker
					.getOrderedLevels();
			securityAnnotation = new SecurityAnnotation(new ArrayList<String>(
					Arrays.asList(orderedLevels)));
			AnnotationExtractor extractor = new AnnotationExtractor(log,
					securityAnnotation);
			if (!Configuration.OLD_ANALYSIS) { // RENEW
				PackManager.v().getPack("wjtp")
						.add(new Transform(PHASE_NAME_ANNOTATION, extractor));
			}
			PackManager
					.v()
					.getPack("jtp")
					.add(new Transform(PHASE_NAME_SECURITY,
							new SecurityTransformer(securityAnnotation, log,
									extractor, instantLogging)));
			soot.Main.main(args);
		} catch (SecurityLevelException e) {
			log.securitychecker(
					SecurityMessages.reflectionInvalidSootSecurityLevelClass(),
					e);
		}
		if (!instantLogging) {
			log.printAllMessages();
		}
	}

}
