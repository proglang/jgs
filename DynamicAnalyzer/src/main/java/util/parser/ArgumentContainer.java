package util.parser;

import java.io.File;
import java.net.URL;
import java.util.Deque;
import java.util.List;

/**
 * Created by Nicolas Müller on 12.01.17.
 * This is a structured container for the arguments passed in.
 * It's purpose is to facilitate handling the appropriate arguments to both soot and ant.
 */
public class ArgumentContainer {

    static final String VALUE_NOT_SET = "!_!_VALUE_NOT_SET_!_!";

    private final String mainclass;
    private final Deque<String> addDirsToClasspath;
    private final List<URL> secDomainClasspath;
    private final List<String> addClasses;
    private final boolean toJimple;
    private final String outputFolder;
    private final List<String> additionalFiles;
    private final boolean onlyDynamic;
    private boolean usePublicTyping;
    private final boolean verbose;
    private final boolean forceMonomorphicMethods;

    ArgumentContainer(String mainclass, Deque<String> addDirsToClasspath, List<URL> secDomainClasspath, List<String> addClasses,
                      boolean toJimple, String outputFolder, List<String> additionalFiles, boolean usePublicTyping, boolean verbose, boolean onlyDynamic, boolean forceMonomorphicMethods) {
        this.mainclass = mainclass;
        this.secDomainClasspath = secDomainClasspath;
        this.toJimple = toJimple;
        this.outputFolder = outputFolder;
        this.additionalFiles = additionalFiles;
        this.addDirsToClasspath = addDirsToClasspath;
        this.addClasses = addClasses;
        this.usePublicTyping = usePublicTyping;
        this.verbose = verbose;
        this.onlyDynamic = onlyDynamic;
        this.forceMonomorphicMethods = forceMonomorphicMethods;
    }

    public String getMainclass() {
        assert !mainclass.equals(VALUE_NOT_SET);
        return mainclass;
    }

    public Deque<String> getAddDirsToClasspath() {
        return addDirsToClasspath;
    }

    public List<String> getAddClassesToClasspath() {return addClasses; }

    public String getOutputFormat() {
        if (toJimple) {
            return "J";
        } else {
            return "c";
        }
    }

    public boolean usePublicTyping() {
        return usePublicTyping;
    }

    public String getOutputFolderAbsolutePath() {
        if (outputFolder.equals(VALUE_NOT_SET)) {
            return System.getProperty("user.dir");
        } else {
            File out = new File(outputFolder);
            if (out.isAbsolute()) {
                return outputFolder;
            } else {
                return new File(System.getProperty("user.dir"), outputFolder).getAbsolutePath();
            }
        }

    }

    public List<String> getAdditionalFiles() {
        return additionalFiles;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public List<URL> getSecDomainClasspath() {
        return secDomainClasspath;
    }

    public boolean isOnlyDynamic() {
        return onlyDynamic;
    }

    public boolean forceMonomorphicMethods() {
        return this.forceMonomorphicMethods;
    }
}
