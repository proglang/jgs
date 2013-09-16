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

import exception.SecurityLevelException;

import logging.SecurityLogger;
import security.SecurityLevel;
import security.SecurityLevelImplChecker;

/**
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class ExtendedSecurityLevelImplChecker extends SecurityLevelImplChecker {

	/**
	 * 
	 * @param log
	 * @param impl
	 */
	private ExtendedSecurityLevelImplChecker(SecurityLogger log, SecurityLevel impl, boolean shoudLog, boolean throwException) {
		super(log, impl, shoudLog, throwException);
	}
	
	/**
	 * 
	 * @param log
	 * @param b 
	 * @return
	 * @throws SecurityLevelException
	 */
	public static ExtendedSecurityLevelImplChecker getExtendedSecurityLevelImplChecker(SecurityLogger log, boolean shoudLog, boolean throwException) throws SecurityLevelException {
		if (compileSootSecurityLevelFile() == 0) {
			URLClassLoader classLoader;
			try {
				classLoader = new URLClassLoader(new URL[] { generateURLOfSootSecurityLevelClass() });
				Class<?> sootSecurityLevelClass = classLoader.loadClass(CLASS_NAME);
				SecurityLevel impl = (SecurityLevel) sootSecurityLevelClass.newInstance();
				classLoader.close();
				removeSootSecurityLevelClass();
				return new ExtendedSecurityLevelImplChecker(log, impl, shoudLog, throwException);
			} catch (MalformedURLException e) {
				log.securitychecker(SecurityMessages.invalidURLOfWorkingFolder(), e);
			} catch (ClassNotFoundException e) {
				log.securitychecker(SecurityMessages.impossibleToLoadSecurityLevelClass(CLASS_NAME), e);
			} catch (InstantiationException | IllegalAccessException e) {
				log.securitychecker(SecurityMessages.impossibleToInstantiateSecurityLevelClass(), e);
			} catch (IOException | SecurityException e) {
				log.securitychecker(SecurityMessages.errorInstantiationSecurityLevelClass(), e);
			} 
		}
		throw new SecurityLevelException(SecurityMessages.errorCompilationSecurityLevelClass(JAVA_PATH));
	}
	
	/**
	 * 
	 * @return
	 */
	private static int compileSootSecurityLevelFile() {
		OutputStream nullStream = new OutputStream() {

			/**
			 * 
			 * 
			 * @param b
			 * @see java.io.OutputStream#write(int)
			 */
			@Override
			public void write(int b) throws IOException {

			}
			
		};
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		return javaCompiler.run(null, null, nullStream, JAVA_PATH);
	}
	
	/**
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	private static URL generateURLOfSootSecurityLevelClass() throws MalformedURLException {
		URL url = new File("").toURI().toURL();
		return url;
	}

	/**
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 */
	private static void removeSootSecurityLevelClass() throws IOException, SecurityException {
		File file = new File(CLASS_PATH);
		file.delete();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isOrderedLevelMethodAvailable() {
		return super.isOrderedLevelMethodAvailable();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOrderedLevelMethodCorrect() {
		return super.isOrderedLevelMethodCorrect();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getIllegalLevelNames() {
		return super.getIllegalLevelNames();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getUnavailableIdMethods() {
		return super.getUnavailableIdMethods();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getUnavailableIdMethodsAnnotation() {
		return super.getUnavailableIdMethodsAnnotation();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getInvalidIdMethods() {
		return super.getInvalidIdMethods();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getInvalidIdMethodsAnnotation() {
		return super.getInvalidIdMethodsAnnotation();
	}
	
}
