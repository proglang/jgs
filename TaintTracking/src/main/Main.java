package main;

import java.io.IOException;
import java.util.logging.Level;

import analysis.SecurityTypeTransformer;
import analysis.SecurityTypeAnalysis;

import extractor.AnnotationExtractor;

import resource.Configuration;
import static resource.Messages.getMsg;

import logging.AnalysisLog;
import logging.AnalysisLogUtils;
import logging.Settings;
import model.MessageStore;
import security.ILevelDefinition;
import security.ILevelDefinitionChecker;
import security.ILevelMediator;
import soot.PackManager;
import soot.Transform;
import utils.AnalysisUtils;
import utils.DefinitionClassHandler;

/**
 * <h1>Main class of the security analysis</h1>
 * 
 * Main class of the security analysis which uses the {@link SecurityTypeTransformer} to transform the body of the methods, as well as the
 * {@link SecurityTypeAnalysis} analysis to check for violations against the <em>security level</em> and <em>write effect</em> policy.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.6
 */
public class Main {

	/**
	 * Indicates whether the messages should be exported exactly in the moment the logger receives the message.
	 */
	private static boolean instantLogging = false;
	/**
	 * The logger that allows to log informations of different {@link SootLoggerLevel}.
	 */
	/** Array of levels which are enabled for the logging. */
	private static Level[] logLevels = {};
	/** The annotation transformer phase name. */
	private static final String PHASE_NAME_ANNOTATION = "wjtp.annotation";
	/** The security transformer phase name. */
	private static final String PHASE_NAME_SECURITY = "jtp.security";

	/**
	 * Adds the given level to the array which contains the levels that are printed.
	 * 
	 * @param level
	 *          Level which should be printed.
	 */
	public static void addLevel(Level level) {
		Level[] levels = new Level[logLevels.length + 1];
		System.arraycopy(logLevels, 0, levels, 0, logLevels.length);
		levels[logLevels.length] = level;
		logLevels = levels;
	}

	/**
	 * DOC
	 * 
	 * @param args
	 * @return
	 */
	public static MessageStore executeAndReturnMessageStore(String[] args) {
		AnalysisLog log = execute(args);
		return (log != null) ? log.getMessageStore() : new MessageStore();
	}

	/**
	 * Enables the instant logging.
	 */
	public static void instantLogging() {
		instantLogging = true;
	}

	/**
	 * Method that starts the transformation as well as the analysis. The given options can contain the normal Soot options and also the
	 * following customized options:
	 * <ul>
	 * <li>{@code -instant-logging}: All logged messages are displayed immediately</li>
	 * <li>{@code -export-file}: All logged messages are exported to a file</li>
	 * <li>{@code -log-levels}: Expects a list of the log-levels {@code exception}, {@code error}, {@code warning}, {@code information},
	 * {@code effect}, {@code security}, {@code securitychecker}, {@code debug}, {@code all}, {@code off}, {@code structure} and
	 * {@code configuration}. Messages of the listed levels are printed from the logger.</li>
	 * </ul>
	 * To perform the analysis, the implementation of the subclass of the class {@code SecurityLevel} is compiled, checked and finally the
	 * available <em>security levels</em> exported.
	 * 
	 * @param args
	 *          Command line options for the transformation and the security analysis.
	 */
	public static void main(String[] args) {
		execute(args);
	}

	/**
	 * DOC
	 * 
	 * @param args
	 */
	private static AnalysisLog execute(String[] args) {
		args = AnalysisUtils.precheckArguments(args);
		AnalysisLog log = new AnalysisLog(instantLogging, logLevels);
		log.configuration(new Settings(AnalysisLogUtils.makeLoggerLevelSetting(logLevels), AnalysisLogUtils
				.makeInstantLoggingSetting(instantLogging), AnalysisLogUtils.makeTimeSetting()));
		try {
			ILevelDefinition definition = DefinitionClassHandler.getDefinitionClass();
			DefinitionClassHandler.removeDefinitionClassFiles();
			ILevelDefinitionChecker checker = new DefinitionChecker(definition, log, true, false);
			if (checker.isValid()) {
				ILevelMediator mediator = new Mediator(definition);
				AnnotationExtractor extractor = new AnnotationExtractor(log, mediator);
				PackManager.v().getPack("wjtp").add(new Transform(PHASE_NAME_ANNOTATION, extractor));
				PackManager.v().getPack("jtp")
						.add(new Transform(PHASE_NAME_SECURITY, new SecurityTypeTransformer(mediator, log, extractor, instantLogging)));
				soot.Main.main(args);
			} else {
				log.error(Configuration.DEF_CLASS_NAME, 0, getMsg("analysis.fail.invalid_definition", Configuration.DEF_CLASS_NAME));
			}
		} catch (IOException | NullPointerException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error(Configuration.DEF_CLASS_NAME, 0, getMsg("analysis.fail.load_definition", Configuration.DEF_CLASS_NAME));
		}
		if (!instantLogging) {
			log.printAllMessages();
		}
		return log;
	}

}
