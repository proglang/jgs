package util.parser;

import java.io.File;

/**
 * Created by Nicolas Müller on 12.01.17.
 */
public class PathHelper {
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
        return new File(folder_working_dir).getParentFile().getParentFile().getParentFile();    // move up four levels
    }
}
