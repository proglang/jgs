package CommandLineArgsTest;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class CmdArgsTest {

	@Test
	public void pathTest() {
		main.Main.main(new String[] {"main.testclasses.NSUPolicy1", "-o", "PathOutputTest"});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, "PathOutputTest/main/testclasses/NSUPolicy1.class");
		File outJar = new File(outParent, "PathOutputTest/main/testclasses/NSUPolicy1.jar");
		
		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
	}
}
