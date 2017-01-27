package utils.parser;

import java.io.File;
import java.nio.file.Path;
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

    public String getMainclassAbsolutePath() {
        assert !mainclass.equals(VALUE_NOT_SET);
        return PathHelper.toAbsolutePath(mainclass);
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

    // output Folder wäre Optional<String>
    public String getOutputFolderAbsolutePath() {
        if (outputFolder.equals(VALUE_NOT_SET)) {
            return System.getProperty("user.dir");
        } else {
            return PathHelper.toAbsolutePath(outputFolder);
        }
    }

    public List<String> getAdditionalFiles() {
        return additionalFiles;
    }
}
