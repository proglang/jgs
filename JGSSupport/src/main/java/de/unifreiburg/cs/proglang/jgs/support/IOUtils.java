package de.unifreiburg.cs.proglang.jgs.support;

import java.io.Console;

/**
 * Utilities for security-aware IO.
 */
// TODO: make these utilities a little more useful.
public class IOUtils {

    /**
     * Rudimentary method to read a secret line from the console. Only useful for
     * simple tests.
     *
     * @return The secret read from the console or null if there is no
     * console or an error occurred while reading.
     */
    public static String readSecret() {
        String result = null;
        Console cons = System.console();
        if (cons != null) {
            result = new String(cons.readPassword("Provide a secret: "));
        }
        return result;
    }

    /**
     * Rudimentary method to print a secret to stdout. The secret is "crossed out". Just for demonstration purposes.
     */
    public static void printSecret(String secret) {
        for (int i = 0; i < secret.length(); i++)  {
            System.out.print("X");
        }
        System.out.println();
    }
}
