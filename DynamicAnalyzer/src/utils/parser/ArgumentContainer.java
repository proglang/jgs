package utils.parser;

import java.util.List;

/**
 * Created by Nicolas Müller on 12.01.17.
 */
public class ArgumentContainer {

    public static final String VALUE_NOT_PRESENT = "!_!_VALUE_NOT_PRESENT_!_!";

    private final String mainclass;
    private final String pathToMainclass;
    private final boolean toJimple;
    private final String outputFolder;
    private final List<String> additionalFiles;

    public ArgumentContainer(String mainclass, String pathToMainclass, boolean toJimple, String outputFolder, List<String> additionalFiles) {
        this.mainclass = mainclass;
        this.pathToMainclass = pathToMainclass;
        this.toJimple = toJimple;
        this.outputFolder = outputFolder;
        this.additionalFiles = additionalFiles;
    }

    public String getMainclass() {
        return mainclass;
    }

    public String getPathToMainclass() {
        return pathToMainclass;
    }

    public String getOutputFormat() {
        if (toJimple) {
            return "J";
        } else {
            return "c";
        }
    }

    public String getOutputFolder() {
        if (outputFolder.equals(VALUE_NOT_PRESENT)) {
            return System.getProperty("user.dir");
        } else {
            return outputFolder;
        }
    }

    public List<String> getAdditionalFiles() {
        return additionalFiles;
    }
}
