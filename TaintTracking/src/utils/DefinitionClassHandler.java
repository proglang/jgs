package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.ToolProvider;

import resource.Configuration;
import security.ILevelDefinition;

/**
 * DOC
 * 
 * @author Thomas Vogel
 *
 */
public class DefinitionClassHandler {

	/**
	 * DOC
	 */
	private static final String EXCEPTION_COMPILATION_ERROR = "Can't not compile the definition source '%s'.";

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static ILevelDefinition getDefinitionClass() throws IOException, NullPointerException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (compileDefinitionClass()) {
			URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(".").toURI().toURL() });
			Class<?> sootSecurityLevelClass = classLoader.loadClass(Configuration.DEF_PATH_JAVA);
			ILevelDefinition impl = (ILevelDefinition) sootSecurityLevelClass.newInstance();
			classLoader.close();
			return impl;
		}
		throw new IOException(String.format(EXCEPTION_COMPILATION_ERROR, Configuration.DEF_PATH_FILE_EXT));
	}

	/**
	 * DOC
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	private static boolean compileDefinitionClass() throws NullPointerException {
		return ToolProvider.getSystemJavaCompiler().run(null, null, null, Configuration.DEF_PATH_FILE_EXT) == 0;
	}

	/**
	 * DOC
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static void removeDefinitionClassFiles() throws IOException, SecurityException {
		File dir = new File(Configuration.DEF_PACKAGE_NAME);
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(Configuration.DEF_CLASS_NAME) && name.endsWith(Configuration.DEF_EXT_JAVA_CLASS);
			}

		};
		for (File file : dir.listFiles(filter)) {
			file.deleteOnExit();
		}
	}

}
