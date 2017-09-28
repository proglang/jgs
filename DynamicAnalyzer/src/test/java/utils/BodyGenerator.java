package utils;

import soot.*;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This Helper Class takes a existing Class File and
 * collects all Method Bodies, that are appearing within that File
 * @author Karsten Fix, 22.08.17
 */
public class BodyGenerator extends BodyTransformer{

    /** Saves a list of Bodies */
    private List<Body> bodies = new ArrayList<>();

    public BodyGenerator(String className) {
        G.reset();

        // <editor-fold desc="Setting up Class Path, adding Threads Class Path to Soot">
        String classPath = Scene.v().getSootClassPath();

        List<String> cpath = new ArrayList<>();
        ClassLoader cxClassloader = Thread.currentThread().getContextClassLoader();
        if (cxClassloader instanceof URLClassLoader) {
            for (URL url : Arrays.asList(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs())) {
                cpath.add(url.getFile());
            }
        }

        Scene.v().setSootClassPath(String.join(":", cpath) + ":" + classPath);
        // </editor-fold>

        PackManager.v().getPack("jtp").add(new Transform("jtp.bg", this));
        soot.Main.main(new String[] {className, "-p", "jtp.bg", "enabled:true"});
    }

    /**
     * This method is called of the {@link Main#main(String[])}, if the
     * arguments are set right.
     * And it is called, because the {@link BodyGenerator#BodyGenerator(String)} does this.
     * @param body The Body, that is then collected.
     * @param s The String of the Phase Name (in this case: jtp.bg)
     * @param map Some additional Options, such as enabled: true, etc.
     */
    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        bodies.add(body);
    }

    /**
     * Gets a list of all Bodies, that have been collected.
     * @return A list of Jimple Bodies.
     */
    public List<Body> getBodies() {
        return bodies;
    }
}
