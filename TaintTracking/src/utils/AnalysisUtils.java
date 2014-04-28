package utils;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.OFF;
import static logging.AnalysisLogLevel.CONFIGURATION;
import static logging.AnalysisLogLevel.DEBUGGING;
import static logging.AnalysisLogLevel.SECURITY;
import static logging.AnalysisLogLevel.SIDEEFFECT;
import static logging.AnalysisLogLevel.STRUCTURE;
import static logging.AnalysisLogLevel.WARNING;
import static resource.Configuration.CLASS_SIGNATURE_PRINT_PACKAGE;
import static resource.Configuration.DEF_PATH_JAVA;
import static resource.Configuration.FIELD_SIGNATURE_PRINT_TYPE;
import static resource.Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY;
import static resource.Configuration.METHOD_SIGNATURE_PRINT_TYPE;
import static resource.Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY;
import static resource.Configuration.PREFIX_LEVEL_FUNCTION;
import static resource.Messages.getMsg;
import static soot.Modifier.STATIC;
import static soot.SootMethod.constructorName;
import static soot.SootMethod.staticInitializerName;
import static soot.options.Options.output_format_jimple;
import static soot.options.Options.src_prec_java;
import static utils.ExtendedJNI.JNI_ARRAY;
import static utils.ExtendedJNI.JNI_BOOLEAN;
import static utils.ExtendedJNI.JNI_BYTE;
import static utils.ExtendedJNI.JNI_CHAR;
import static utils.ExtendedJNI.JNI_CLASS;
import static utils.ExtendedJNI.JNI_DOUBLE;
import static utils.ExtendedJNI.JNI_FLOAT;
import static utils.ExtendedJNI.JNI_INT;
import static utils.ExtendedJNI.JNI_LONG;
import static utils.ExtendedJNI.JNI_SHORT;
import static utils.ExtendedJNI.JNI_VOID;

import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import main.AnalysisType;
import main.Main;
import security.ILevel;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.util.Chain;
import exception.AnnotationExtractionException;
import exception.ArgumentInvalidException;

/**
 * <h1>Utilities for the Soot framework</h1>
 * 
 * The class {@link AnalysisUtils} offers various methods, which are in relation to the Soot tool. Mostly, the provided methods handle the
 * generation of class, method and field signature or handle the extraction of annotations, e.g. the line number, a String of specific
 * annotation type or a String array of specific annotation type. Also, there are methods which check whether a SootMethod is a static
 * initializer or a constructor.
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * 
 */
public class AnalysisUtils {

	public static class ArgumentParser {

		private static final String ARG_CONSTRAINTS = "-constraints";
		private static final String ARG_SRC_PREC = "-src-prec";
		private static final String ARG_PROGRAM_CLASSPATH = "-program-classpath";
		private static final String OUTPUT_PATH = "./generatedJimple";
		private static final String ARG_DEF_CLASSPATH = "-def-classpath";
		private static final String ARG_SOURCE_PATH = "-source-path";
		private static final String RT_JAR = "rt.jar";
		private static final String ARG_CLASSPATH_1 = "-cp";
		private static final String ARG_CLASSPATH_2 = "-soot-class-path";
		private static final String ARG_CLASSPATH_3 = "-soot-classpath";
		private static final String ARG_EXCLUDE_1 = "-x";
		private static final String ARG_EXCLUDE_2 = "-exclude";
		/**
		 * DOC
		 */
		private static final String ARG_KEEP_LINE_NUMBER = "-keep-line-number";
		/** Command line option for setting up the levels which should be logged. */
		private static final String ARG_LOG_LEVELS = "-log-levels";
		/**
		 * DOC
		 */
		private static final String ARG_NO_BODIES_FOR_EXCLUDE = "-no-bodies-for-excluded";
		private static final String ARG_OUTPUT_1 = "-f";
		private static final String ARG_OUTPUT_2 = "-output-format";
		private static final String ARG_OUTPUT_PATH_1 = "-d";
		private static final String ARG_OUTPUT_PATH_2 = "-output-dir";
		/**
		 * DOC
		 */
		private static final String ARG_PREPEND_CLASSPATH_1 = "-pp";
		private static final String ARG_PREPEND_CLASSPATH_2 = "-prepend-classpath";
		private static final String ARG_WHOLE_PROGRAM_1 = "-w";
		private static final String ARG_WHOLE_PROGRAM_2 = "-whole-program";
		private static final String EXCL_ANNOTATION = "annotation.";
		private static final String EXCL_CONSTRAINTS = "constraints.";
		private static final String EXCL_SECURITY = "security.";
		private static final String EXCL_EXCEPTION = "exception.";
		private static final String LEV_ALL = "all";
		private static final String LEV_CONFIGURATION = "configuration";
		private static final String LEV_DEBUG = "debug";
		private static final String LEV_EFFECT = "effect";
		private static final String LEV_IMPORTANT = "important";
		private static final String LEV_OFF = "off";
		private static final String LEV_SECURITY = "security";
		private static final String LEV_SIDEEFFECT = "sideeffect";

		private static final String LEV_STRUCTURE = "structure";
		private static final String LEV_WARNING = "warning";
		/** Command line option for enabling the instant logging. */
		private static final String OPT_INSTANT_LOGGING = "-instant-logging";

		private String analyzedSourcePath = ".";

		private String[] arguments = {};
		private String definitionClassPath = ".";
		private boolean instantLogging = false;
		private List<Level> logLevels = new ArrayList<Level>();
		private String programClassPath = ".";

		private List<String> sootArguments = new ArrayList<String>();
		private AnalysisType analysisType = AnalysisType.LEVELS;

		private ArgumentParser(String[] args) {
			this.arguments = args;
			parse();
		}

		public final String getAnalyzedSourcePath() {
			return analyzedSourcePath;
		}

		public final String getDefinitionClassPath() {
			return definitionClassPath;
		}

		public final Level[] getLogLevels() {
			Level[] result = new Level[logLevels.size()];
			return logLevels.toArray(result);
		}

		public final String getProgramClassPath() {
			return programClassPath;
		}

		public final String[] getSootArguments() {
			String[] result = new String[sootArguments.size()];
			return sootArguments.toArray(result);
		}

		public final boolean isInstantLogging() {
			return instantLogging;
		}

		private void prepareClassPath(List<String> list) {
			parseDefinitionClassPath(list);
			parseAnalyzedSourcePath(list);
			parseProgramClassPath(list);
			parseClassPath(list, getDefinitionClassPath(), getProgramClassPath(), getAnalyzedSourcePath());
		}

		private boolean containsRT(List<String> paths) {
			for (String string : paths) {
				if (string.endsWith(RT_JAR)) return true;
			}
			return false;
		}

		private String getArgumentsString() {
			StringBuilder builder = new StringBuilder();
			for (String string : arguments) {
				builder.append(string + " ");
			}
			return builder.toString().trim();
		}

		private void parse() {
			List<String> list = new ArrayList<String>(Arrays.asList(arguments));
			parseLogLevels(list);
			parseInstantLogging(list);
			parseKeepLineNumbers(list);
			parsePrependClasspath(list);
			parseNoBodiesForExclude(list);
			parseOutput(list);
			parseWholeProgram(list);
			parseExclude(list);
			prepareClassPath(list);
			parseInput(list);
			parseAnalysisType(list);
			sootArguments.addAll(list);
		}

		private void parseAnalysisType(List<String> list) {
			if (list.contains(ARG_CONSTRAINTS)) {
				this.analysisType = AnalysisType.CONSTRAINTS;
			}
			list.remove(ARG_CONSTRAINTS);
		}

		private void parseInput(List<String> list) {
			if (list.contains(ARG_SRC_PREC)) {
				int optionsPosition = list.indexOf(ARG_SRC_PREC) + 1;
				list.remove(optionsPosition);
				list.remove(ARG_SRC_PREC);
			}
			Options.v().set_src_prec(src_prec_java);
		}

		private void parseAnalyzedSourcePath(List<String> list) {
			if (list.contains(ARG_SOURCE_PATH)) {
				int optionsPosition = list.indexOf(ARG_SOURCE_PATH) + 1;
				if (optionsPosition < list.size()) {
					String options = list.get(optionsPosition);
					Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
					Path absolutePath = basePath.resolve(options).normalize();
					this.analyzedSourcePath = absolutePath.toString();
					list.remove(optionsPosition);
					list.remove(ARG_SOURCE_PATH);
				} else {
					throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
				}
			} else {
				throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
			}
		}

		private void parseClassPath(List<String> list, String dcp, String pcp, String asp) {
			if (list.contains(ARG_CLASSPATH_1) || list.contains(ARG_CLASSPATH_2) || list.contains(ARG_CLASSPATH_3)) {
				int argPos1 = list.indexOf(ARG_CLASSPATH_1);
				int argPos2 = list.indexOf(ARG_CLASSPATH_2);
				int argPos3 = list.indexOf(ARG_CLASSPATH_3);
				if ((argPos1 != -1 && argPos2 == -1 && argPos3 == -1) || (argPos1 == -1 && argPos2 != -1 && argPos3 == -1)
						|| (argPos1 == -1 && argPos2 == -1 && argPos3 != -1)) {
					int argPos = (argPos1 != -1) ? argPos1 : ((argPos2 != -1) ? argPos2 : argPos3);
					int optionsPosition = argPos + 1;
					if (optionsPosition < list.size()) {
						String options = list.get(optionsPosition);
						List<String> paths = new ArrayList<String>(Arrays.asList(options.split(":")));
						if (!containsRT(paths)) {
							throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
						}
						if (!paths.contains(dcp)) paths.add(dcp);
						if (!paths.contains(pcp)) paths.add(pcp);
						if (!paths.contains(asp)) paths.add(asp);
						String pathString = "";
						for (String path : paths) {
							Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
							Path absolutePath = basePath.resolve(path).normalize();
							if (!pathString.equals("")) pathString += ":";
							pathString += absolutePath.toString();
						}
						Options.v().set_soot_classpath(pathString);
						list.remove(optionsPosition);
						list.remove(argPos);
					} else {
						throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
					}
				} else {
					throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
				}
			} else {
				throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
			}
		}

		private void parseDefinitionClassPath(List<String> list) {
			if (list.contains(ARG_DEF_CLASSPATH)) {
				int optionsPosition = list.indexOf(ARG_DEF_CLASSPATH) + 1;
				if (optionsPosition < list.size()) {
					String options = list.get(optionsPosition);
					Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
					Path absolutePath = basePath.resolve(options).normalize();
					this.definitionClassPath = absolutePath.toString();
					list.remove(optionsPosition);
					list.remove(ARG_DEF_CLASSPATH);
				} else {
					throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
				}
			} else {
				throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
			}
		}

		private void parseExclude(List<String> list) {
			List<String> excludes = new ArrayList<String>();
			List<Integer> remove = new ArrayList<Integer>();
			for (int i = 0; i < list.size(); i++) {
				String argument = list.get(i);
				if (argument.equals(ARG_EXCLUDE_1) || argument.equals(ARG_EXCLUDE_2)) {
					int optionsPosition = i + 1;
					if (optionsPosition < list.size()) {
						excludes.add(list.get(optionsPosition));
						remove.addAll(Arrays.asList(new Integer[] { i, optionsPosition }));
					} else {
						throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
					}
				}
			}
			for (int i = remove.size() - 1; i >= 0; i--) {
				list.remove(remove.get(i).intValue());
			}
			if (!excludes.contains(EXCL_SECURITY)) excludes.add(EXCL_SECURITY);
			if (!excludes.contains(EXCL_ANNOTATION)) excludes.add(EXCL_ANNOTATION);
			if (!excludes.contains(EXCL_CONSTRAINTS)) excludes.add(EXCL_CONSTRAINTS);
			if (!excludes.contains(EXCL_EXCEPTION)) excludes.add(EXCL_EXCEPTION);
			Options.v().set_exclude(excludes);
		}

		private void parseInstantLogging(List<String> list) {
			if (list.contains(OPT_INSTANT_LOGGING)) {
				instantLogging = true;
				list.remove(OPT_INSTANT_LOGGING);
			}
		}

		private void parseKeepLineNumbers(List<String> list) {
			if (list.contains(ARG_KEEP_LINE_NUMBER)) {
				list.remove(ARG_KEEP_LINE_NUMBER);

			}
			Options.v().set_keep_line_number(true);
		}

		private void parseLogLevels(List<String> list) {
			if (list.contains(ARG_LOG_LEVELS)) {
				int optionsPosition = list.indexOf(ARG_LOG_LEVELS) + 1;
				String[] levels = list.get(optionsPosition).split(",");
				setLogLevels(levels);
				list.remove(optionsPosition);
				list.remove(ARG_LOG_LEVELS);
			} else {
				setLogLevels(new String[] { LEV_IMPORTANT });
			}
		}

		private void parseNoBodiesForExclude(List<String> list) {
			if (list.contains(ARG_NO_BODIES_FOR_EXCLUDE)) {
				list.remove(ARG_NO_BODIES_FOR_EXCLUDE);
			}
			Options.v().set_no_bodies_for_excluded(true);
		}

		private void parseOutput(List<String> list) {
			if (!list.contains(ARG_OUTPUT_1) || !list.contains(ARG_OUTPUT_2)) {
				Options.v().set_output_format(output_format_jimple);
			}
			if (!list.contains(ARG_OUTPUT_PATH_1) || !list.contains(ARG_OUTPUT_PATH_2)) {
				Options.v().set_output_dir(OUTPUT_PATH);
			}
		}

		private void parsePrependClasspath(List<String> list) {
			if (list.contains(ARG_PREPEND_CLASSPATH_1) || list.contains(ARG_PREPEND_CLASSPATH_2)) {
				list.remove(ARG_PREPEND_CLASSPATH_1);
				list.remove(ARG_PREPEND_CLASSPATH_2);
			}
			Options.v().set_prepend_classpath(true);
		}

		private void parseProgramClassPath(List<String> list) {
			if (list.contains(ARG_PROGRAM_CLASSPATH)) {
				int optionsPosition = list.indexOf(ARG_PROGRAM_CLASSPATH) + 1;
				if (optionsPosition < list.size()) {
					String options = list.get(optionsPosition);
					Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
					Path absolutePath = basePath.resolve(options).normalize();
					this.programClassPath = absolutePath.toString();
					list.remove(optionsPosition);
					list.remove(ARG_PROGRAM_CLASSPATH);
				} else {
					throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
				}
			} else {
				throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
			}
		}

		private void parseWholeProgram(List<String> list) {
			if (!list.contains(ARG_WHOLE_PROGRAM_1) || !list.contains(ARG_WHOLE_PROGRAM_2)) {
				list.remove(ARG_WHOLE_PROGRAM_1);
				list.remove(ARG_WHOLE_PROGRAM_2);
			}
			Options.v().set_whole_program(true);
		}

		/**
		 * Adds valid levels of the given array to the level array of the main class {@link Main}.
		 * 
		 * @param levels
		 *          Array of String which should represent a level.
		 */
		private void setLogLevels(String[] levels) {
			for (String level : levels) {
				switch (level.toLowerCase()) {
					case LEV_WARNING:
						logLevels.add(WARNING);
						break;
					case LEV_CONFIGURATION:
						logLevels.add(CONFIGURATION);
						break;
					case LEV_STRUCTURE:
						logLevels.add(STRUCTURE);
						break;
					case LEV_DEBUG:
						logLevels.add(DEBUGGING);
						break;
					case LEV_SIDEEFFECT:
						logLevels.add(SIDEEFFECT);
						break;
					case LEV_SECURITY:
						logLevels.add(SECURITY);
						break;
					case LEV_EFFECT:
						logLevels.add(SIDEEFFECT);
						break;
					case LEV_OFF:
						logLevels.add(OFF);
						break;
					case LEV_ALL:
						logLevels.add(ALL);
						break;
					case LEV_IMPORTANT:
						logLevels.add(CONFIGURATION);
						logLevels.add(STRUCTURE);
						logLevels.add(SECURITY);
						logLevels.add(SIDEEFFECT);
						break;
					default:
						throw new ArgumentInvalidException(getMsg("exception.arguments.invalid", getArgumentsString()));
				}
			}
		}

		public AnalysisType getAnalysisType() {
			return analysisType;
		}

	}

	/**
	 * DOC
	 */
	private static final String SIGN_POINT = ".";
	/**
	 * DOC
	 */
	private static final String SIGN_SEMICOLON = ";";

	/**
	 * DOC
	 */
	private static final String SIGN_SLASH = "/";

	/**
	 * Checks whether the specified list of methods contains a static initializer method.
	 * 
	 * @param sootMethods
	 *          List of methods for which should be checked whether it contains a static initializer method.
	 * @return {@code true} if the specified list contains a static initializer, otherwise {@code false}.
	 */
	public static boolean containsStaticInitializer(List<SootMethod> sootMethods) {
		for (SootMethod sootMethod : sootMethods) {
			if (isClinitMethod(sootMethod)) return true;
		}
		return false;
	}

	/**
	 * Returns the AnnotationTag with the specified type if it exists in the annotations of the specified {@link VisibilityAnnotationTag}.
	 * Otherwise the method will throw an exception.
	 * 
	 * @param tag
	 *          The {@link VisibilityAnnotationTag} from which the annotation of the specified type should be extracted.
	 * @param type
	 *          The type of the annotation which should be extracted from the specified {@link VisibilityAnnotationTag}.
	 * @return {@code null} if the {@link VisibilityAnnotationTag} does not contains an annotation of the specified type, otherwise the
	 *         {@link AnnotationTag} with the specified type which is contained by the specified {@link VisibilityAnnotationTag}.
	 * @throws ExtractionException
	 *           Only if the specified {@link VisibilityAnnotationTag} doesn't contain an annotation with the specified type.
	 */
	public static AnnotationTag extractAnnotationTagWithType(VisibilityAnnotationTag tag, String type) {
		Iterator<AnnotationTag> iterator = tag.getAnnotations().iterator();
		while (iterator.hasNext()) {
			AnnotationTag next = iterator.next();
			if (next.getType().equals(type)) return next;
		}
		throw new AnnotationExtractionException(getMsg("exception.annotation.tag_not_found", type));
	}

	/**
	 * Extracts the source line number from a given unit, if this unit has the corresponding annotation. If there exists no annotation the
	 * method will return {@code 0} as the line number.
	 * 
	 * @param unit
	 *          {@link Unit} for which the source line number should be extracted.
	 * @return The source line number of the unit. If the statement has no annotation the method will return {@code 0}.
	 * @see AnalysisUtils#TAG_SOURCE_LINE
	 */
	public static long extractLineNumber(Unit unit) {
		if (unit.hasTag(SourceLnPosTag.class.getSimpleName())) {
			SourceLnPosTag sourceLnPosTag = (SourceLnPosTag) unit.getTag(SourceLnPosTag.class.getSimpleName());
			long res = (sourceLnPosTag != null) ? Long.valueOf(sourceLnPosTag.startLn()) : 0;
			return res;
		}
		return 0;
	}

	/**
	 * Returns the {@link VisibilityAnnotationTag} of the specified host.
	 * 
	 * @param host
	 *          The host for which this annotation should be returned.
	 * @return {@code null} if the specified host doesn't contain a {@link VisibilityAnnotationTag}, otherwise the contained
	 *         {@link VisibilityAnnotationTag}.
	 */
	public static VisibilityAnnotationTag extractVisibilityAnnotationTag(Host host) {
		VisibilityAnnotationTag tag = (VisibilityAnnotationTag) host.getTag(VisibilityAnnotationTag.class.getSimpleName());
		if (tag != null) {
			return tag;
		}
		throw new AnnotationExtractionException(getMsg("exception.annotation.no_visibility_tag"));
	}

	/**
	 * Generates a readable class signature for the given {@link SootClass}. Depending on the given flags, the resulting signature can contain
	 * the package name.
	 * 
	 * @param sootClass
	 *          The SootClass for which a class signature should be created.
	 * @param printPackage
	 *          Flag, that indicates whether the package name should be included in the signature.
	 * @return Readable class signature of the given SootClass, depending on the given flag, the signature includes the package name.
	 */
	public static String generateClassSignature(SootClass sootClass) {
		return CLASS_SIGNATURE_PRINT_PACKAGE ? sootClass.getName() : sootClass.getShortName();
	}

	/**
	 * Method creates a static initializer with an empty body for the specified class and returns this method.
	 * 
	 * @param sootClass
	 *          Class for which the empty static initializer should be created.
	 * @return Static initializer method with an empty method body for the specified class.
	 */
	public static SootMethod generatedEmptyStaticInitializer(SootClass sootClass) {
		SootMethod sootMethod = new SootMethod(staticInitializerName, new ArrayList<Object>(), VoidType.v(), STATIC);
		sootMethod.setDeclaringClass(sootClass);
		sootClass.addMethod(sootMethod);
		JimpleBody body = Jimple.v().newBody(sootMethod);
		sootMethod.setActiveBody(body);
		Chain<Unit> units = body.getUnits();
		units.add(Jimple.v().newReturnVoidStmt());
		return sootMethod;
	}

	/**
	 * Generates a readable field signature for a {@link SootField}. Depending on the given flags, the resulting signature can contain the
	 * visibility of the field, the package name and also the type of the field.
	 * 
	 * @param sootField
	 *          The SootField for which a field signature should be created.
	 * @param printPackage
	 *          Flag, that indicates whether the package name should be included in the signature.
	 * @param printType
	 *          Flag, that indicates whether the type of the field should be included in the signature.
	 * @param printVisibility
	 *          Flag, that indicates whether the visibility of the field should be included in the signature.
	 * @return Readable field signature of the given SootField, depending on the given flags, the signature includes the package name, the
	 *         type of the field and the visibility.
	 */
	public static String generateFieldSignature(SootField sootField) {
		String fieldName = sootField.getName();
		String classOfField = generateClassSignature(sootField.getDeclaringClass());
		String fieldTypeName = generateTypeName(sootField.getType());
		String fieldVisibility = generateVisibility(sootField.isPrivate(), sootField.isProtected(), sootField.isPublic());
		return classOfField + "." + fieldName + (FIELD_SIGNATURE_PRINT_TYPE ? " : " + fieldTypeName : "")
				+ (FIELD_SIGNATURE_PRINT_VISIBILITY ? " [" + fieldVisibility + "]" : "");
	}

	/**
	 * Generates the file name of the given class. The resulting file name can differ from the real file name, e.g. for nested classes (see
	 * {@link AnalysisUtils#generateFileName(SootClass)}). The method assumes that the file name of a class is the class name without the
	 * parent class name and without the package name. Note that the file name do not contain the file suffix.
	 * 
	 * @param sootClass
	 *          Class for which the file name should be generated.
	 * @return The file name of the given class, if the class doesn't exist the method will return 'unknown'.
	 */
	public static String generateFileName(SootClass sootClass) {
		String className = sootClass.getShortName();
		String[] classNameComponents = className.split("\\$");
		if (classNameComponents.length > 0) {
			return classNameComponents[0];
		} else {
			return "unknown";
		}
	}

	/**
	 * Generates the file name of the class which declares the given method. The resulting file name can differ from the real file name, e.g.
	 * for nested classes (see {@link AnalysisUtils#generateFileName(SootClass)}) and the file name do not contain the file suffix.
	 * 
	 * @param sootMethod
	 *          Method for which the file name should be generated.
	 * @return The file name of the class which declares the given method.
	 * @see AnalysisUtils#generateFileName(SootClass)
	 */
	public static String generateFileName(SootMethod sootMethod) {
		return generateFileName(sootMethod.getDeclaringClass());
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public static String generateLevelFunctionName(ILevel level) {
		String levelName = level.getName();
		String head = levelName.substring(0, 1).toUpperCase();
		String tail = levelName.substring(1, levelName.length());
		return PREFIX_LEVEL_FUNCTION + head + tail;
	}

	/**
	 * Generates a readable method signature for a {@link SootMethod}. Depending on the given flags, the resulting signature can contain the
	 * visibility of the method, the package name and also the types of the parameters and the return type.
	 * 
	 * @param sootMethod
	 *          The SootMethod for which a method signature should be created.
	 * @param printPackage
	 *          Flag, that indicates whether the package name should be included in the signature.
	 * @param printType
	 *          Flag, that indicates whether the types of the parameters and the return type should be included in the signature.
	 * @param printVisibility
	 *          Flag, that indicates whether the visibility of the method should be included in the signature.
	 * @return Readable method signature of the given SootMethod, depending on the given flags, the signature includes the package name, the
	 *         return and the parameter types and the visibility.
	 */
	public static String generateMethodSignature(SootMethod sootMethod) {
		String methodName = sootMethod.getName();
		String classOfMethod = generateClassSignature(sootMethod.getDeclaringClass());
		String parameters = "";
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			Type type = sootMethod.getParameterType(i);
			if (!parameters.equals("")) parameters += ", ";
			parameters += ("arg" + i + (METHOD_SIGNATURE_PRINT_TYPE ? (" : " + generateTypeName(type)) : ""));
		}
		String methodTypeName = generateTypeName(sootMethod.getReturnType());
		String methodVisibility = generateVisibility(sootMethod.isPrivate(), sootMethod.isProtected(), sootMethod.isPublic());
		return classOfMethod + "." + methodName + "(" + parameters + ")" + (METHOD_SIGNATURE_PRINT_TYPE ? " : " + methodTypeName : "")
				+ (METHOD_SIGNATURE_PRINT_VISIBILITY ? " [" + methodVisibility + "]" : "");
	}

	public static ArgumentParser getArgumentParser(String[] args) {
		return new ArgumentParser(args);
	}

	/**
	 * DOC
	 * 
	 * @param cl
	 * @return
	 */
	public static String getJNISignature(Class<?> cl) {
		if (cl.isPrimitive()) {
			if (cl.equals(boolean.class)) {
				return String.valueOf(JNI_BOOLEAN);
			} else if (cl.equals(byte.class)) {
				return String.valueOf(JNI_BYTE);
			} else if (cl.equals(char.class)) {
				return String.valueOf(JNI_CHAR);
			} else if (cl.equals(short.class)) {
				return String.valueOf(JNI_SHORT);
			} else if (cl.equals(int.class)) {
				return String.valueOf(JNI_INT);
			} else if (cl.equals(long.class)) {
				return String.valueOf(JNI_LONG);
			} else if (cl.equals(float.class)) {
				return String.valueOf(JNI_FLOAT);
			} else if (cl.equals(double.class)) {
				return String.valueOf(JNI_DOUBLE);
			} else if (cl.equals(void.class)) {
				return String.valueOf(JNI_VOID);
			}
		} else if (cl.isArray()) {
			return String.valueOf(JNI_ARRAY) + getJNISignature(cl.getComponentType());
		}
		return String.valueOf(JNI_CLASS) + cl.getName().replace(SIGN_POINT, SIGN_SLASH) + SIGN_SEMICOLON;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static List<SootMethod> getOverridenMethods(SootMethod sootMethod) {
		List<SootMethod> overridenMethods = new ArrayList<SootMethod>();
		SootClass superClass = sootMethod.getDeclaringClass();
		while (superClass.hasSuperclass()) {
			superClass = superClass.getSuperclass();
			if (superClass.declaresMethod(sootMethod.getSubSignature())) {
				SootMethod overridenMethod = superClass.getMethod(sootMethod.getSubSignature());
				overridenMethods.add(overridenMethod);
			}
		}
		return overridenMethods;
	}

	/**
	 * Method extracts the name of the parameters in a ordered list, if they are available.
	 * 
	 * @param sootMethod
	 *          Method for which the parameter names should be returned.
	 * @return Ordered list of the parameter names if they are available.
	 */
	public static List<String> getParameterNames(SootMethod sootMethod) {
		List<String> names = new ArrayList<String>();
		ParamNamesTag tag = (ParamNamesTag) sootMethod.getTag(ParamNamesTag.class.getSimpleName());
		if (tag != null) names.addAll(tag.getNames());
		return names;
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @return
	 */
	public static List<SootClass> getSuperClassesUntilLibrary(SootClass sootClass) {
		List<SootClass> superClasses = new ArrayList<SootClass>();
		boolean library = false;
		while (sootClass.hasSuperclass() && !library) {
			SootClass superClass = sootClass.getSuperclass();
			superClasses.add(superClass);
			library = superClass.isJavaLibraryClass();
			sootClass = superClass;
		}
		return superClasses;
	}

	/**
	 * Checks whether the specified {@link VisibilityAnnotationTag} contains an annotation of the specified type.
	 * 
	 * @param tag
	 *          The {@link VisibilityAnnotationTag} for which should be check whether it contains an annotation of the specified type.
	 * @param type
	 *          The type which the contained annotation should have.
	 * @return {@code true} if the specified {@link VisibilityAnnotationTag} contains an {@link AnnotationTag} with the specified type,
	 *         otherwise {@code false}.
	 */
	public static boolean hasAnnotationOfType(VisibilityAnnotationTag tag, String type) {
		if (tag.hasAnnotations()) {
			Iterator<AnnotationTag> iterator = tag.getAnnotations().iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getType().equals(type)) return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the specified host has a {@link VisibilityAnnotationTag}.
	 * 
	 * @param host
	 *          The host for which should be checked whether it has a {@link VisibilityAnnotationTag}.
	 * @return {@code true} if the host has such a annotation.
	 */
	public static boolean hasVisibilityAnnnotationTag(Host host) {
		return host.hasTag(VisibilityAnnotationTag.class.getSimpleName());
	}

	/**
	 * Checks whether the given method is a static initializer method. I.e. the corresponding flag of the method is {@code true} and the name
	 * of the method is equals to {@link SootMethod#staticInitializerName}.
	 * 
	 * @param sootMethod
	 *          Method for which should be checked whether it is a static initializer.
	 * @return {@code true} if the given method is an static initializer, otherwise {@code false}.
	 */
	public static boolean isClinitMethod(SootMethod sootMethod) {
		return sootMethod.isEntryMethod() && sootMethod.getName().equals(staticInitializerName);
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @return
	 */
	public static boolean isDefinitionClass(SootClass sootClass) {
		return sootClass.getName().equals(DEF_PATH_JAVA);
	}

	/**
	 * Checks whether the given method is a constructor method. I.e. the corresponding flag of the method is {@code true} and the name of the
	 * method is equals to {@link SootMethod#constructorName}.
	 * 
	 * @param sootMethod
	 *          Method for which should be checked whether it is a constructor.
	 * @return {@code true} if the given method is an constructor, otherwise {@code false}.
	 */
	public static boolean isInitMethod(SootMethod sootMethod) {
		return sootMethod.isConstructor() && sootMethod.getName().equals(constructorName);
	}

	/**
	 * DOC
	 * 
	 * @param sootClass
	 * @return
	 */
	public static boolean isInnerClassOfDefinitionClass(SootClass sootClass) {
		if (sootClass.hasOuterClass()) {
			return isDefinitionClass(sootClass.getOuterClass());
		} else {
			return false;
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @param levels
	 * @return
	 */
	public static boolean isLevelFunction(SootMethod sootMethod, List<ILevel> levels) {
		if (isMethodOfDefinitionClass(sootMethod) && sootMethod.getParameterCount() == 1 && sootMethod.isStatic() && sootMethod.isPublic()) {
			for (ILevel level : levels) {
				if (sootMethod.getName().equals(generateLevelFunctionName(level))) return true;
			}
		}
		return false;
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean isMethodOfDefinition(SootMethod sootMethod) {
		SootClass sootClass = sootMethod.getDeclaringClass();
		return isDefinitionClass(sootClass) || isInnerClassOfDefinitionClass(sootClass);
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean isMethodOfDefinitionClass(SootMethod sootMethod) {
		SootClass sootClass = sootMethod.getDeclaringClass();
		return isDefinitionClass(sootClass);
	}

	/**
	 * Indicates whether the specified method is a method which doesn't return a value, i.e. is a void method.
	 * 
	 * @param sootMethod
	 *          Method for which should be checked whether it is a void method.
	 * @return true} if the method doesn't return a value. Otherwise {@code false} .
	 */
	public static boolean isVoidMethod(SootMethod sootMethod) {
		return sootMethod.getReturnType().equals(VoidType.v());
	}

	/**
	 * DOC
	 * 
	 * @param jni
	 * @return
	 */
	public static String jniSignatureToJavaPath(String jni) {
		if (jni.startsWith(String.valueOf(JNI_ARRAY))) {
			return jni.replace(SIGN_SLASH, SIGN_POINT);
		} else if (jni.startsWith(String.valueOf(JNI_BOOLEAN))) {
			return boolean.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_BYTE))) {
			return byte.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_CHAR))) {
			return char.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_SHORT))) {
			return short.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_INT))) {
			return int.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_LONG))) {
			return long.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_FLOAT))) {
			return float.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_DOUBLE))) {
			return double.class.getSimpleName();
		} else if (jni.startsWith(String.valueOf(JNI_VOID))) {
			return void.class.getSimpleName();
		} else {
			if (jni.startsWith(String.valueOf(JNI_CLASS))) {
				return jni.substring(1, jni.length() - 1).replace(SIGN_SLASH, SIGN_POINT);
			} else {
				return jni;
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param sootMethod
	 * @return
	 */
	public static boolean overridesMethod(SootMethod sootMethod) {
		SootClass superClass = sootMethod.getDeclaringClass();
		while (superClass.hasSuperclass()) {
			superClass = superClass.getSuperclass();
			if (superClass.declaresMethod(sootMethod.getSubSignature())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the given type as String.
	 * 
	 * @param type
	 *          Type which should be represented as String.
	 * @return String which represents the given type.
	 */
	private static String generateTypeName(Type type) {
		return type.toString();
	}

	/**
	 * Depending on the given flags, the method will return the <em>UML</em> visibility literal. I.e. if the private flag is {@code true}, the
	 * method returns '{@code -}', if the protected flag is {@code true}, the method returns '{@code #}' or if the public flag is {@code true}
	 * , the method returns '{@code +}'. Otherwise if none flag is {@code true}, the method returns ' {@code ?}'. If multiple flags are
	 * {@code true}, the method will return the literal for which the flag is true and in the above order occurs first.
	 * 
	 * @param isPrivate
	 *          Flags that indicates whether the private <em>UML</em> visibility literal should be returned.
	 * @param isProtected
	 *          Flags that indicates whether the protected <em>UML</em> visibility literal should be returned.
	 * @param isPublic
	 *          Flags that indicates whether the public <em>UML</em> visibility literal should be returned.
	 * @return The <em>UML</em> visibility literal, depending on the given flags: '{@code -}' for private, '{@code #}' for protected and '
	 *         {@code +}' for public. Otherwise '{@code ?}'.
	 */
	private static String generateVisibility(boolean isPrivate, boolean isProtected, boolean isPublic) {
		return (isPrivate ? "-" : (isProtected ? "#" : (isPublic ? "+" : "?")));
	}

	/**
	 * 
	 * @param method
	 * @return
	 */
	public static String generateSignature(Method method) {
		StringBuffer buffer = new StringBuffer();
		Class<?> cl = method.getDeclaringClass();
		buffer.append("<" + cl.getName() + ": ");
		buffer.append(getSubSignatureImpl(method.getName(), method.getParameterTypes(), method.getReturnType()));
		buffer.append(">");
		return buffer.toString().intern();
	}

	/**
	 * 
	 * @param name
	 * @param parameters
	 * @param returnType
	 * @return
	 */
	private static String getSubSignatureImpl(String name, Class<?>[] parameters, Class<?> returnType) {
		StringBuffer buffer = new StringBuffer();
		Class<?> cl = returnType;

		buffer.append(cl.getName() + " " + name + "(");
		List<Class<?>> params = Arrays.asList(parameters);
		Iterator<Class<?>> typeIt = params.iterator();

		if (typeIt.hasNext()) {
			cl = typeIt.next();

			buffer.append(cl.getName());

			while (typeIt.hasNext()) {
				buffer.append(",");

				cl = typeIt.next();
				buffer.append(cl.getName());
			}
		}
		buffer.append(")");
		return buffer.toString().intern();
	}

}