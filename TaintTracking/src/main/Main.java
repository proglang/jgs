package main;

import static logging.AnalysisLogUtils.makeInstantLoggingSetting;
import static logging.AnalysisLogUtils.makeLoggerLevelSetting;
import static logging.AnalysisLogUtils.makeTimeSetting;
import static utils.AnalysisUtils.getArgumentParser;
import static utils.DefinitionClassHandler.getDefinitionClass;
import logging.AnalysisLog;
import logging.Settings;
import model.MessageStore;
import security.ILevelDefinition;
import security.ILevelDefinitionChecker;
import security.ILevelMediator;
import soot.PackManager;
import soot.Transform;
import utils.AnalysisUtils.ArgumentParser;
import analysis.SecurityTypeAnalysis;
import analysis.SecurityTypeTransformer;
import extractor.AnnotationExtractor;

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

	/** The annotation transformer phase name. */
	private static final String PHASE_NAME_ANNOTATION = "wjtp.annotation";
	/** The security transformer phase name. */
	private static final String PHASE_NAME_SECURITY = "jtp.security";

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
		ArgumentParser parser = getArgumentParser(args);
		args = parser.getSootArguments();
		AnalysisLog log = new AnalysisLog(parser.isInstantLogging(), parser.getLogLevels());
		log.configuration(new Settings(makeLoggerLevelSetting(parser.getLogLevels()), makeInstantLoggingSetting(parser.isInstantLogging()),
				makeTimeSetting()));
		ILevelDefinition definition = getDefinitionClass();
		@SuppressWarnings("unused")
		ILevelDefinitionChecker checker = new DefinitionChecker(definition, log, true);
		ILevelMediator mediator = new Mediator(definition);
		AnnotationExtractor extractor = new AnnotationExtractor(log, mediator);
		PackManager.v().getPack("wjtp").add(new Transform(PHASE_NAME_ANNOTATION, extractor));
		PackManager.v().getPack("jtp")
				.add(new Transform(PHASE_NAME_SECURITY, new SecurityTypeTransformer(mediator, log, extractor, parser.isInstantLogging())));
		soot.Main.main(args);
		if (!parser.isInstantLogging()) {
			log.printAllMessages();
		}
		return log;
	}

}
