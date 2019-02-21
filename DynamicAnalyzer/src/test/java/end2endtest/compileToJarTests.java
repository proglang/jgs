package end2endtest;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class compileToJarTests {

    private String outputPath = "sootOutput/argsTest";
    String externalPath = "DynamicAnalyzer/testing_external/src/main/java";
    File extPath = new File( new File(System.getProperty("user.dir")), externalPath);


    /**
     * NSUPolicy1 is present only on external_path, not in src/main...
     * NSUPolicy is present only in src/main, not on external_path...
     */
    @Test
    public void pathTest() throws UnsupportedEncodingException {
        String testFile = "NSUPolicy";

        main.Main.main(new String[]{"-m", "testclasses." + testFile, "-o", outputPath});
        File outParent = new File(System.getProperty("user.dir"));
        File outFile = new File(outParent, outputPath + "/testclasses/" + testFile + ".class");
        File outJar = new File(outParent, outputPath + "/testclasses/" + testFile + ".jar");

        assertTrue(outFile.exists());
        assertTrue(outJar.exists());

        // delete for valid run next time
        assertTrue(outFile.delete());
        assertTrue(outJar.delete());
    }

    @Test
    public void pathTestJimple() throws UnsupportedEncodingException {
        String testFile = "NSUPolicy";

        main.Main.main(new String[]{"-m", "testclasses." + testFile, "-o", outputPath, "-j"});
        File outParent = new File(System.getProperty("user.dir"));
        File outFile = new File(outParent, outputPath + "/testclasses." + testFile + ".jimple");
        File outJar = new File(outParent, outputPath + "/testclasses/" + testFile + ".jar");

        assertTrue(outFile.exists());
        assertTrue(outJar.exists());

        // delete for valid run next time
        assertTrue(outFile.delete());
        assertTrue(outJar.delete());
    }

    /**
     * Note that the externalPath must be set individually on each computer!
     */
    @Test
    public void pathTestP() throws UnsupportedEncodingException {
        String testFile = "NSUPolicy1";        // SHOULD BE NSUPolicy1
        File inputFile = new File(extPath.getAbsolutePath(), "/testclasses/" + testFile + ".java");

        assertThat( "Input file cannot be found at " + inputFile.getAbsolutePath() +
                ".\n Please note that this test requires the external file path to be manually set on your computer. " +
                "See test.java.end2endtest.compilerToJarTests.", inputFile.exists(), is(Boolean.TRUE));

        main.Main.main(new String[]{"-m", "testclasses." + testFile, "-o", outputPath, "-p", extPath.getAbsolutePath()});
        File outParent = new File(System.getProperty("user.dir"));
        File outFile = new File(outParent, outputPath + "/testclasses/" + testFile + ".class");
        File outJar = new File(outParent, outputPath + "/testclasses/" + testFile + ".jar");

        assertTrue(outFile.exists());
        assertTrue(outJar.exists());

        // delete for valid run next time
        assertTrue(outFile.delete());
        assertTrue(outJar.delete());
    }

    /**
     * NSUPolicy1 is present only on external_path, not in src/main...
     * NSUPolicy is present only in src/main, not on external_path...
     */
    @Test
    public void pathTestPRelative() throws UnsupportedEncodingException {
        String testFile = "NSUPolicy1";
        File inputFile = new File(extPath.getAbsolutePath(), "/testclasses/" + testFile + ".java");

        assertThat( "Input file cannot be found at " + inputFile.getAbsolutePath() +
                ".\n Please note that this test requires the external file path to be manually set on your computer." +
                " See test.java.end2endtest.compilerToJarTests.", inputFile.exists(), is(Boolean.TRUE));

        main.Main.main(new String[]{"-m", "testclasses." + testFile, "-o", outputPath, "-p", extPath.getAbsolutePath()});
        File outParent = new File(System.getProperty("user.dir"));
        File outFile = new File(outParent, outputPath + "/testclasses/" + testFile + ".class");
        File outJar = new File(outParent, outputPath + "/testclasses/" + testFile + ".jar");

        assertTrue(outFile.exists());
        assertTrue(outJar.exists());

        // delete for valid run next time
        assertTrue(outFile.delete());
        assertTrue(outJar.delete());
    }


    @Test
    public void staticMethodsFail() {
        runAndTestJar("StaticMethodsFail", "testclasses.util.SimpleObject", true);
    }

    @Test
    public void externalFail1() {
        runAndTestJar("ExternalFail1", "testclasses.util.simpleClassForTests", true);
    }

    /**
     * Super awesome test: Takes a testfile, one additional file, and a boolean hasIllegalFLow.
     * Build Jar, runs it and tests for illegalFlowException.
     */
    private void runAndTestJar(String testFile, String additionalFile, boolean hasIllegalFlow)  {
        final String TARGET_NAME = "exec";

        String externalPath = "testing_external";

        try {
            main.Main.main(new String[]{"-m", "testclasses." + testFile, "-o", outputPath, "-p", externalPath, "-f", additionalFile});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        File outParent = new File(System.getProperty("user.dir"));
        File outFile = new File(outParent, outputPath + "/testclasses/" + testFile + ".class");
        File outJar = new File(outParent, outputPath + "/testclasses/" + testFile + ".jar");
        File addFile = new File(outParent, outputPath + "/" + additionalFile.replace(".", "/") + ".class");

        assertTrue("File not found: " + outFile.getAbsolutePath(), outFile.exists());
        assertTrue("File not found: " + outJar.getAbsolutePath(), outJar.exists());
        assertTrue("File not found: " + addFile.getAbsolutePath(), addFile.exists());

        // now run it and check wheter or not it produces an IFCError
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

            task.setOutput(out);

            target.execute();
            project.executeTarget(project.getDefaultTarget());

        } catch (Exception e) {
            try {
                Scanner scanner = new Scanner(out);
                String text = scanner.useDelimiter("\\A").next();
                if (hasIllegalFlow) {
                    assertThat(text, containsString("IFCError"));
                } else {
                    assertThat(text, not(containsString("IFCError")));
                }
                scanner.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                fail();
            }
        }

        assertTrue(out.delete());
        assertTrue(outFile.delete());
        assertTrue(outJar.delete());
        assertTrue(addFile.delete());

    }

}
