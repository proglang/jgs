package utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import logging.SecurityLogger;
import security.SecurityLevel;
import security.SecurityLevelImplChecker;
import exception.SootException.SecurityLevelException;

/**
 * <h1>Extended validity checker for {@code SecurityLevel} implementations</h1>
 * 
 * 
 * The {@link ExtendedSecurityLevelImplChecker} class extends the {@link SecurityLevelImplChecker}
 * and checks the subclass implementation of the class {@link SecurityLevel}. Therefore, the checker
 * tries to compile the expected class that the developer has to implement. Also, the
 * {@link ExtendedSecurityLevelImplChecker} provides information about missing id functions, missing
 * annotations and invalid levels. For more information about the validity of the implementation see
 * {@link SecurityLevelImplChecker}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see SecurityLevelImplChecker
 */
public class ExtendedSecurityLevelImplChecker extends SecurityLevelImplChecker {

	/**
	 * Method that creates an {@link ExtendedSecurityLevelImplChecker} instance. Therefore the
	 * method tries to create also an instance of the {@link SecurityLevel} subclass, which the
	 * developer has to implement and which should be check whether this subclass satisfies the
	 * guidelines of a valid implementation of {@link SecurityLevel} subclasses. By invoking this
	 * method all violations against the guidelines of this implementation will be printed to the
	 * given logger (if it is {@code null}, the standard and error output stream will be used). The
	 * printing itself depends on the given flag 'shouldLog', too. Also, if the implementation is
	 * not valid and the flag 'throwException' is enabled, the method will throw an exception.
	 * 
	 * @param log
	 *            {@link SecurityLogger} which should print the warnings and errors that occur
	 *            during the check of the class of the given instance.
	 * @param shouldLog
	 *            Indicates whether warnings and errors should be printed ({@code true}), or should
	 *            not be printed ({@code false}). This value dis- and enables the printing to the
	 *            logger as well as the printing to the standard and error output stream.
	 * @param throwException
	 *            Indicates whether an exception should be thrown if the implementation isn't valid
	 *            ({@code true}), or no exception should be thrown ({@code false}).
	 * @return
	 * @throws SecurityLevelException
	 *             Throws an exception if the class of the given instance violates the guidelines of
	 *             valid implementation of {@link SecurityLevel} subclasses and if the given
	 *             argument 'throwException' is {@code true}.
	 */
	public static ExtendedSecurityLevelImplChecker getExtendedSecurityLevelImplChecker(
			SecurityLogger log, boolean shoudLog, boolean throwException)
			throws SecurityLevelException {
		if (compileSootSecurityLevelFile() == 0) {
			URLClassLoader classLoader;
			try {
				classLoader = new URLClassLoader(
						new URL[] { generateURLOfSootSecurityLevelClass() });
				Class<?> sootSecurityLevelClass = classLoader
						.loadClass(Predefined.IMPL_SL_PATH_JAVA);
				SecurityLevel impl = (SecurityLevel) sootSecurityLevelClass.newInstance();
				classLoader.close();
				removeSootSecurityLevelClass();
				return new ExtendedSecurityLevelImplChecker(log, impl, shoudLog, throwException);
			} catch (MalformedURLException e) {
				log.securitychecker(SecurityMessages.invalidURLOfWorkingFolder(), e);
			} catch (ClassNotFoundException e) {
				log.securitychecker(SecurityMessages
						.impossibleToLoadSecurityLevelClass(Predefined.IMPL_SL_PATH_JAVA), e);
			} catch (InstantiationException | IllegalAccessException e) {
				log.securitychecker(SecurityMessages.impossibleToInstantiateSecurityLevelClass(), e);
			} catch (IOException | SecurityException e) {
				log.securitychecker(SecurityMessages.errorInstantiationSecurityLevelClass(), e);
			}
		}
		throw new SecurityLevelException(
				SecurityMessages.errorCompilationSecurityLevelClass(JAVA_PATH));
	}

	/**
	 * Method that compiles the implementation of the {@link SecurityLevel} class that the developer
	 * has implemented. This implementation has to be located as defined in the class
	 * {@link Predefined} (see {@link Predefined#IMPL_SL_PATH_JAVA}).
	 * 
	 * @return {@code 0} if the compilation is successfully, nonzero otherwise.
	 * @throws NullPointerException
	 *             If the array of arguments of the {@code run} contains any {@code null} elements.
	 */
	private static int compileSootSecurityLevelFile() throws NullPointerException {
		OutputStream stream = new OutputStream() {

			/**
			 * Writes the given byte to this output stream.
			 * 
			 * @param b
			 *            Byte which should be write.
			 * @see java.io.OutputStream#write(int)
			 */
			@Override
			public void write(int b) throws IOException {
			}

		};
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		return javaCompiler.run(null, null, stream, JAVA_PATH);
	}

	/**
	 * Generates the {@link URL} of the working directory.
	 * 
	 * @return The URL of the working directory.
	 * @throws MalformedURLException
	 *             If a protocol handler for the URL could not be found, or if some other error
	 *             occurred while constructing the URL.
	 */
	private static URL generateURLOfSootSecurityLevelClass() throws MalformedURLException {
		URL url = new File("").toURI().toURL();
		return url;
	}

	/**
	 * Removes the compiled implementation of the {@link SecurityLevel} class that the developer has
	 * implemented. This implementation is located as defined in the class {@link Predefined} (see
	 * {@link Predefined#IMPL_SL_PATH_FILE}).
	 * 
	 * @throws IOException
	 *             Throws an {@link IOException} when the file cannot be deleted.
	 * @throws SecurityException
	 *             If file cannot be deleted because of denied access to the file.
	 */
	private static void removeSootSecurityLevelClass() throws IOException, SecurityException {
		File file = new File(CLASS_PATH);
		file.delete();
	}

	/**
	 * Private constructor for an {@link ExtendedSecurityLevelImplChecker} object which checks the
	 * given instance of a subclass of the class {@link SecurityLevel}. By invoking this constructor
	 * all violations against the guidelines of an implementation of a subclass of
	 * {@link SecurityLevel} will be printed to the given logger (if it is {@code null}, the
	 * standard and error output stream will be used). The printing itself depends on the given flag
	 * 'shouldLog', too. Also, if the implementation is not valid and the flag 'throwException' is
	 * enabled, the constructor will throw an exception.
	 * 
	 * @param log
	 *            {@link SecurityLogger} which should print the warnings and errors that occur
	 *            during the check of the class of the given instance.
	 * @param impl
	 *            Instance of a {@link SecurityLevel} subclass, which should be check whether this
	 *            subclass satisfies the guidelines of a valid implementation of
	 *            {@link SecurityLevel} subclasses.
	 * @param shouldLog
	 *            Indicates whether warnings and errors should be printed ({@code true}), or should
	 *            not be printed ({@code false}). This value dis- and enables the printing to the
	 *            logger as well as the printing to the standard and error output stream.
	 * @param throwException
	 *            Indicates whether an exception should be thrown if the implementation isn't valid
	 *            ({@code true}), or no exception should be thrown ({@code false}).
	 * @see SecurityLevelImplChecker#SecurityLevelImplChecker(SecurityLogger, SecurityLevel,
	 *      boolean, boolean)
	 */
	private ExtendedSecurityLevelImplChecker(SecurityLogger log, SecurityLevel impl,
			boolean shoudLog, boolean throwException) {
		super(log, impl, shoudLog, throwException);
	}

	/**
	 * Returns the list which contains all invalid <em>security levels</em>. I.e. all the levels
	 * which are given by the implementation of the {@link SecurityLevel} subclass and which are
	 * invalid. A <em>security level</em> is invalid if it is equals to the '{@code void}' return
	 * <em>security level</em> or if it contains at least one of the characters '{@code *}', '
	 * {@code (}', '{@code )}' or '{@code ,}'.
	 * 
	 * @return List of invalid <em>security levels</em> which are defined by the implemented
	 *         {@link SecurityLevel} subclass.
	 * @see SecurityLevelImplChecker#getIllegalLevelNames()
	 */
	public List<String> getIllegalLevelNames() {
		return super.getIllegalLevelNames();
	}

	/**
	 * Returns the list which contains all method names of id functions which have an invalid return
	 * <em>security level</em> annotation. Note that this list includes only those method names
	 * which exist and which are valid id functions.
	 * 
	 * @return List containing method names of id function with invalid return
	 *         <em>security level</em> annotation.
	 * @see SecurityLevelImplChecker#getInvalidIdFunctionAnnotation()
	 */
	public List<String> getInvalidIdFunctionAnnotation() {
		return super.getInvalidIdFunctionAnnotation();
	}

	/**
	 * Returns the list which contains all method names of id functions which are invalid. I.e. the
	 * list contains the id function which are not {@code static}. Note that non {@code public} id
	 * functions are not contained by the resulting list.
	 * 
	 * @return List containing the method names of invalid id functions.
	 * @see SecurityLevelImplChecker#getInvalidIdFunctions()
	 */
	public List<String> getInvalidIdFunctions() {
		return super.getInvalidIdFunctions();
	}

	/**
	 * Returns the list which contains all method names of id functions which do not have a return
	 * <em>security level</em> annotation. Note that this list includes only those method names
	 * which exist and which are valid id functions.
	 * 
	 * @return List containing the method names of id function with unavailable return
	 *         <em>security level</em> annotation.
	 * @see SecurityLevelImplChecker#getUnavailableIdFunctionAnnotation()
	 */
	public List<String> getUnavailableIdFunctionAnnotation() {
		return super.getUnavailableIdFunctionAnnotation();
	}

	/**
	 * Returns the list which contains all method names of id functions which not exist. I.e. for
	 * all defined <em>security levels</em> for which no method exist with the name {@code levelId}
	 * or for which the method isn't {@code public}. Note that non {@code static} id functions are
	 * not included by this list.
	 * 
	 * @return List containing the method names of unavailable id functions.
	 * @see SecurityLevelImplChecker#getUnavailableIdFunctions()
	 */
	public List<String> getUnavailableIdFunctions() {
		return super.getUnavailableIdFunctions();
	}

	/**
	 * Indicates whether the implementation of {@link SecurityLevel#getOrderedSecurityLevels()} is
	 * available or not. The case that this method will return {@code false} is implausible, because
	 * a subclass has to implement the abstract methods of the super class.
	 * 
	 * @return {@code true} if the implementation of the method which returns the ordered
	 *         <em>security level</em> list is available. Otherwise {@code false}.
	 * @see SecurityLevelImplChecker#isOrderedLevelMethodAvailable()
	 */
	public boolean isOrderedLevelMethodAvailable() {
		return super.isOrderedLevelMethodAvailable();
	}

	/**
	 * Indicates whether the implementation of {@link SecurityLevel#getOrderedSecurityLevels()} is
	 * correct or not. If it isn't correct, the method returns less than 2 <em>security levels</em>
	 * or {@code null}.
	 * 
	 * @return {@code true} if the implementation of the method which returns the ordered
	 *         <em>security level</em> list is correct, i.e. it returns at least 2
	 *         <em>security levels</em>. Otherwise {@code false}.
	 * @see SecurityLevelImplChecker#isOrderedLevelMethodCorrect()
	 */
	public boolean isOrderedLevelMethodCorrect() {
		return super.isOrderedLevelMethodCorrect();
	}

}
