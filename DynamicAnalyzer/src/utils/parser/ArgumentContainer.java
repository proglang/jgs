package utils.parser;

import java.util.List;

/**
 * Created by Nicolas Müller on 12.01.17.
 */
public class ArgumentContainer {

    public static final String VALUE_NOT_PRESENT = "!_!_VALUE_NOT_PRESENT_!_!";

    private final String mainclass;
    private final String outputFormat;
    private final String outputFolder;
    private final String pathToMainclass;
    private final List<String> additionalFiles;


    public String getMainclass() {
        return mainclass;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public String getPathToMainclass() {
        return pathToMainclass;
    }

    public List<String> getAdditionalFiles() {
        return additionalFiles;
    }

    public ArgumentContainer(String mainclass, String outputFormat, String outputFolder, String pathToMainclass, List<String> additionalFiles) {

        this.mainclass = mainclass;
        this.outputFormat = outputFormat;
        this.outputFolder = outputFolder;
        this.pathToMainclass = pathToMainclass;
        this.additionalFiles = additionalFiles;
    }
}
