package CommandLineArgsTest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class CmdArgsTest {
	
	String outputPath = "sootOutput/argsTest";
	/**
	 * NSUPolicy1 is present only on external_path, not in src/main...
	 * NSUPolicy is present only in src/main, not on external_path...
	 */
	
	@Test
	public void pathTest() {
		String testFile = "NSUPolicy";
		
		main.Main.main(new String[] {"main.testclasses." + testFile, "-o", outputPath});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, outputPath + "/main/testclasses/"+ testFile +".class");
		File outJar = new File(outParent, outputPath + "/main/testclasses/" + testFile + ".jar");
		
		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
		
		// delete for valid run next time
		outFile.delete();
		outJar.delete();
				
		assertTrue(!outFile.exists());
		assertTrue(!outJar.exists());
	}
	
	@Test
	public void pathTestJimple() {
		String testFile = "NSUPolicy";
		
		main.Main.main(new String[] {"main.testclasses." + testFile, "-o", outputPath, "-j"});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, outputPath + "/main.testclasses."+ testFile +".jimple");
		File outJar = new File(outParent, outputPath + "/main/testclasses/" + testFile + ".jar");
		
		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
		
		// delete for valid run next time
		outFile.delete();
		outJar.delete();
		
		assertTrue(!outFile.exists());
		assertTrue(!outJar.exists());
	}
	
	/**
	 * Note that the externalPath must be set individually on each computer!
	 */
	@Test
	public void pathTestP() {
		String testFile = "NSUPolicy1";		// SHOULD BE NSUPolicy1 !! Not working right now
		String externalPath = "/Users/NicolasM/Dropbox/hiwi/progLang/jgs/DynamicAnalyzer/testing_external";
		
		main.Main.main(new String[] {"main.testclasses." + testFile, "-o", outputPath, "-p", externalPath});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, outputPath + "/main/testclasses/"+ testFile +".class");
		File outJar = new File(outParent, outputPath + "/main/testclasses/" + testFile + ".jar");
		
		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
		
		// delete for valid run next time
		outFile.delete();
		outJar.delete();
		
		assertTrue(!outFile.exists());
		assertTrue(!outJar.exists());
	}
	
	/**
	 * NSUPolicy1 is present only on external_path, not in src/main...
	 * NSUPolicy is present only in src/main, not on external_path...
	 */
	@Test
	public void pathTestPRelative() {
		String testFile = "NSUPolicy1"; 
		String externalPath = "testing_external";
		
		main.Main.main(new String[] {"main.testclasses." + testFile, "-o", outputPath, "-p", externalPath});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, outputPath + "/main/testclasses/"+ testFile +".class");
		File outJar = new File(outParent, outputPath + "/main/testclasses/" + testFile + ".jar");
		
		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
		
		// delete for valid run next time
		outFile.delete();
		outJar.delete();
		
		assertTrue(!outFile.exists());
		assertTrue(!outJar.exists());
	}
	
	/**
	 * Super awesome test: Builds a jar with -f flag, instruments and includes external classes,
	 * builds to jar, runs it and tests for IllegalFlowException.
	 */
	@Test
	public void addFilesTest() {
		boolean hasIllegalFlow = true;
		
		String testFile = "StaticMethodsFail"; 
		String externalPath = "testing_external";
		String additionalFile = "main.testclasses.utils.SimpleObject";
		String additionalFile2 = "main.testclasses.utils.C";
		
		main.Main.main(new String[] {"main.testclasses." + testFile, "-o", outputPath, "-p", externalPath, "-f", additionalFile, additionalFile2});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, outputPath + "/main/testclasses/"+ testFile +".class");
		File outJar = new File(outParent, outputPath + "/main/testclasses/" + testFile + ".jar");
		File addFile = new File(outParent, outputPath + "/" + additionalFile.replace(".", "/") + ".class");
		File addFile2 = new File(outParent, outputPath + "/" + additionalFile2.replace(".", "/") + ".class");
		
		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
		assertTrue(addFile.exists());
		assertTrue(addFile2.exists());
		
		// now run it and check wheter or not it produces an IllegalFlowException
		// Run a java app in a separate system process
		System.out.println("Running " + outJar.toString() + "...");
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("java -jar " + outJar.toString());
			// Then retreive the process output
			InputStream err = proc.getErrorStream();
			String output = convertStreamToString(err);
			System.out.println(output);
			if (hasIllegalFlow) {
				assertTrue(output.contains("utils.exceptions.IllegalFlowException"));
			} else {
				assertTrue(!err.toString().contains("IllegalFlowException"));
			}
		} catch (IOException e) {
			assertTrue(false);
		}
		
		// delete for valid run next time
		outFile.delete();
		outJar.delete();
		addFile.delete();
		addFile2.delete();
		
		assertTrue(!outFile.exists());
		assertTrue(!outJar.exists());
		assertTrue(!addFile.exists());
		assertTrue(!addFile2.exists());
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    String ret = s.hasNext() ? s.next() : "";
	    s.close();
	    return ret;
	}
	
}
