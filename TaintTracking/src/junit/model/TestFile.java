package junit.model;

import java.io.File;

/**
 * TODO: documentation
 * 
 */
public class TestFile {

	private static final String EXT_JAVA = ".java";
	private static final String SEP = File.separator;
	private static final String WORKING_DIR = System.getProperty("user.dir");
	private final String className;
	private final String packageName;

	public TestFile(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
	}

	public String getClassClasspath() {
		return String.format("%s.%s", packageName, className);
	}

	public String getClassName() {
		return className;
	}

	public String getPackageClasspath() {
		return packageName;
	}

	public File getPackageDirectory() {
		return new File(WORKING_DIR + SEP + packageName);

	}

	public String getPackagePath() {
		return getPackageDirectory().getPath();
	}

	public File getSourceFile() {
		return new File(WORKING_DIR + SEP + packageName + SEP + className + EXT_JAVA);
	}

	public String getSourcePath() {
		return getSourceFile().getPath();
	}

}