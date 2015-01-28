package utils;

import static resource.Configuration.DEF_CLASS_NAME;
import static resource.Configuration.DEF_PATH_JAVA;
import static resource.Messages.getMsg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import security.ILevelDefinition;
import exception.DefinitionNotFoundException;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * 
 */
public class DefinitionClassHandler {

    /**
     * 
     * @return
     */
    public static ILevelDefinition getDefinitionClass(String definitionClassPath) {
        try {
            URL url = new File(definitionClassPath).toURI().toURL();
            URL[] urls = { url };
            URLClassLoader loader = new URLClassLoader(urls);
            ILevelDefinition impl =
                (ILevelDefinition) loader.loadClass(DEF_PATH_JAVA)
                                         .newInstance();
            loader.close();
            return impl;
        } catch (IOException | NullPointerException | ClassNotFoundException
                | InstantiationException | IllegalAccessException e) {
            throw new DefinitionNotFoundException(getMsg("exception.utils.error_loading_class",
                                                         DEF_CLASS_NAME),
                                                  e);
        }
    }

}
