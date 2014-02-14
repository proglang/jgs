package securityNewNew;

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
import resource.Configuration;
import resource.SecurityMessages;
import exception.SootException.SecurityLevelException;

/**
 * <h1>Extended validity checker for {@code SecurityLevel} implementations</h1>
 * 
 * 
 * The {@link DefinitionClassLoader} class extends the {@link SecurityLevelImplChecker}
 * and checks the subclass implementation of the class {@link SecurityLevel}. Therefore, the checker
 * tries to compile the expected class that the developer has to implement. Also, the
 * {@link DefinitionClassLoader} provides information about missing id functions, missing
 * annotations and invalid levels. For more information about the validity of the implementation see
 * {@link SecurityLevelImplChecker}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see SecurityLevelImplChecker
 */
public class DefinitionClassLoader {


	public static ILevelDefinition getDefinitionClass()
			throws Exception {
		if (compileSootSecurityLevelFile() == 0) {
			URLClassLoader classLoader;
			
				classLoader = new URLClassLoader(
						new URL[] { generateURLOfSootSecurityLevelClass() });
				Class<?> sootSecurityLevelClass = classLoader
						.loadClass("security.Definition");
				ILevelDefinition impl = (ILevelDefinition) sootSecurityLevelClass.newInstance();
				classLoader.close();
				removeSootSecurityLevelClass();
				return impl;
		}
		throw new Exception();
	}

	private static int compileSootSecurityLevelFile() throws NullPointerException {
		OutputStream stream = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
			}

		};
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		return javaCompiler.run(null, null, System.err, "security/Definition.java");
	}

	private static URL generateURLOfSootSecurityLevelClass() throws MalformedURLException {
		URL url = new File("").toURI().toURL();
		return url;
	}

	private static void removeSootSecurityLevelClass() throws IOException, SecurityException {
		File file = new File(Configuration.DEF_CLASS_NAME);
		file.delete();
	}


}
