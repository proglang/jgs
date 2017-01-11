package end2endtest;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.junit.Test;
import utils.exceptions.IllegalFlowException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class compileToJarTests {
	
	private String outputPath = "sootOutput/argsTest";

	private static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    String ret = s.hasNext() ? s.next() : "";
	    s.close();
	    return ret;
	}
	
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
	

	@Test
    public void staticMethodsFail() {
	    addFilesTest("StaticMethodsFail", "main.testclasses.utils.SimpleObject");
    }

    @Test
    public void externalFail1() {
	    addFilesTest("ExternalFail1", "main.testclasses.utils.simpleClassForTests");
    }

    /**
     * Super awesome test: Builds a jar with -f flag, instruments and includes external classes,
     * builds to jar, runs it and tests for IllegalFlowException.
     */
	private void addFilesTest(String testFile, String additionalFile) {
		boolean hasIllegalFlow = true;
        final String TARGET_NAME = "exec";

		String externalPath = "testing_external";

		main.Main.main(new String[] {"main.testclasses." + testFile, "-o", outputPath, "-p", externalPath, "-f", additionalFile});
		File outParent = new File(System.getProperty("user.dir"));
		File outFile = new File(outParent, outputPath + "/main/testclasses/"+ testFile +".class");
		File outJar = new File(outParent, outputPath + "/main/testclasses/" + testFile + ".jar");
		File addFile = new File(outParent, outputPath + "/" + additionalFile.replace(".", "/") + ".class");

		assertTrue(outFile.exists());
		assertTrue(outJar.exists());
		assertTrue(addFile.exists());

		// now run it and check wheter or not it produces an IllegalFlowException
		System.out.println("Running " + outJar.toString() + "...");
        File out = new File(outputPath + "/outStreamFile");

        try {
            Project project = new Project();
            project.setName("ExecuteJar");
            project.init();

            Target target = new Target();
            target.setName(TARGET_NAME);

            Java task = new Java();
            task.setJar(outJar);
            task.setFork(true);
            task.setFailonerror(true);
            task.setProject(project);

            target.addTask(task);

            project.addTarget(target);
            project.setDefault(TARGET_NAME);

            // debugging purposes
            task.setOutput(out);

            target.execute();
            project.executeTarget(project.getDefaultTarget());
        } catch (IllegalFlowException e) {
            System.out.println("IllegalFlowException!! : " + e);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e + "\n\n" + e.getClass().getClass().toString());
            e.printStackTrace();

            // just to get it working.
            // =======================
            try {
                Scanner scanner = new Scanner( out );
                String text = scanner.useDelimiter("\\A").next();
                assertThat(text, containsString("IllegalFlowException"));
                scanner.close(); // Put this call in a finally block
            } catch (Exception ex) {
                ex.printStackTrace();
                fail();
            }
            // ========================

        }

        assertTrue(out.delete());
        assertTrue(outFile.delete());
        assertTrue(outJar.delete());
        assertTrue(addFile.delete());

	}
	
}
