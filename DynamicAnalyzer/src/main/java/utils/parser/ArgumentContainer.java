package utils.parser;

import java.io.File;
import java.util.List;

/**
 * Created by Nicolas Müller on 12.01.17.
 * This is a structured container for the arguments passed in.
 * It's purpose is to facility handeling the appropriate arguments to both soot and ant.
 */
public class ArgumentContainer {

    static final String VALUE_NOT_SET = "!_!_VALUE_NOT_SET_!_!";

    private final String mainclass;
    private final List<String> addDirsToClasspath;
    private final boolean toJimple;
    private final String outputFolder;
    private final List<String> additionalFiles;

    ArgumentContainer(String mainclass, List<String> addDirsToClasspath, boolean toJimple, String outputFolder, List<String> additionalFiles) {
        this.mainclass = mainclass;
        this.toJimple = toJimple;
        this.outputFolder = outputFolder;
        this.additionalFiles = additionalFiles;
        this.addDirsToClasspath = addDirsToClasspath;
    }

    public String getMainclass() {
        assert !mainclass.equals(VALUE_NOT_SET);
        return mainclass;
    }

    public List<String> getAddDirsToClasspath() {
        return addDirsToClasspath;
    }

    public String getOutputFormat() {
        if (toJimple) {
            return "J";
        } else {
            return "c";
        }
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
}
