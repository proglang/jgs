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
            File parent = PathHelper.getBaseDirOfProjectViaClassloader();
            File fullPath = new File(parent, s);
            try {
                return fullPath.getCanonicalPath();
            } catch (IOException e) {
                throw new InternalAnalyzerException(e.getMessage());
            }
        }

    }

    /**
     * Returns the base dir of the JGS Project. Uses main.Main.class.getClassLoader() to return
     * /DynamicAnalyzer/target/scala-11/{test-classes, classes}. Thus, moving up four levels yields the
     * desired result, which should be independent of the user.dir.
     * @return      Return File pointing to /path/of/JGS-Folder
     */
    public static File getBaseDirOfProjectViaClassloader() {
        ClassLoader loader = main.Main.class.getClassLoader();

        // folder working dir
        String folder_working_dir = loader.getResource("").getPath();
        return new File(folder_working_dir).getParentFile().getParentFile().getParentFile().getParentFile();    // move up four levels
    }
}
