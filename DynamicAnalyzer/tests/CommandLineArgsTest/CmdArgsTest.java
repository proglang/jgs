package CommandLineArgsTest;

import static org.junit.Assert.assertTrue;

import java.io.File;

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
	
	
}
