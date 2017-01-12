package utils.parser;

import utils.exceptions.InternalAnalyzerException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nicolas Müller on 12.01.17.
 */
public class PathHelper {
    /**
     * Convert a given path to canonical path, regardless whether input is relative or absolute
     * @param s			relative or absolute path
     * @return			equivalent canonical path
     */
    public static String toAbsolutePath(String s) {
        File path = new File(s);

        if (path.isAbsolute()) {
            try {
                return path.getCanonicalPath();
            } catch (IOException e) {
                throw new InternalAnalyzerException(e.getMessage());
            }
        } else {
            File parent = new File(System.getProperty("user.dir"));
            File fullPath = new File(parent, s);
            try {
                return fullPath.getCanonicalPath();
            } catch (IOException e) {
                throw new InternalAnalyzerException(e.getMessage());
            }
        }

    }
}
