package junit.model;

import java.io.File;

/**
 * TODO: documentation
 * 
 */
public class TestFile {
	
	private static final String SEP = File.separator;
	private static final String WORKING_DIR = System.getProperty("user.dir");
	private static final String EXT_JAVA = ".java";
	private final String packageName;
	private final String className;
	
	public TestFile(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
	}
	
	public File getPackageDirectory() {
		return new File(WORKING_DIR + SEP + packageName);
		
	}
	
	public String getPackagePath() {
		return getPackageDirectory().getPath();
	}
	
	public String getPackageClasspath() {
		return packageName;
	}
	
	public String getClassClasspath() {
		return String.format("%s.%s", packageName, className);
	}
	
	public File getSourceFile() {
		return new File(WORKING_DIR + SEP + packageName + SEP + className + EXT_JAVA);
	}
	
	public String getSourcePath() {
		return getSourceFile().getPath();
	}
	
	public String getClassName() {
		return className;
	}
	
}