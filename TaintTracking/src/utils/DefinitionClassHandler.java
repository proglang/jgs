package utils;

import static resource.Configuration.DEF_CLASS_NAME;
import static resource.Configuration.DEF_PATH_FILE_EXT;
import static resource.Configuration.DEF_PATH_JAVA;
import static resource.Messages.getMsg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.ToolProvider;

import security.ILevelDefinition;
import exception.DefinitionNotFoundException;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * 
 */
public class DefinitionClassHandler {

	/**
	 * 
	 * @return
	 */
	public static ILevelDefinition getDefinitionClass() {
		try {
			if (compileDefinitionClass()) {
				URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(".").toURI().toURL() });
				Class<?> sootSecurityLevelClass = classLoader.loadClass(DEF_PATH_JAVA);
				ILevelDefinition impl = (ILevelDefinition) sootSecurityLevelClass.newInstance();
				classLoader.close();
				return impl;
			} else {
				throw new IOException(getMsg("exception.utils.error_compiling_class", DEF_PATH_FILE_EXT));
			}
		} catch (IOException | NullPointerException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new DefinitionNotFoundException(getMsg("exception.utils.error_loading_class", DEF_CLASS_NAME), e);
		}
	}

	/**
	 * DOC
	 * 
	 * @return
	 * @throws NullPointerException
	 */
	private static boolean compileDefinitionClass() throws NullPointerException {
		return ToolProvider.getSystemJavaCompiler().run(null, null, null, DEF_PATH_FILE_EXT) == 0;
	}

}
