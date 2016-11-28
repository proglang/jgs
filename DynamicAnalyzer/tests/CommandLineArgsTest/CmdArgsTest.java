package CommandLineArgsTest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import org.junit.Test;

public class CmdArgsTest {
	
	String outputPath = "sootOutput/argsTest";
	
	
	@Test
	public void pathTest() {
		String testFile = "NSUPolicy1";
		
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
		String testFile = "NSUPolicy1";
		
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
		String testFile = "NSUPolicy1";
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
