package de.unifreiburg.cs.proglang.jgs.support;

import java.io.*;

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
        Console cons = System.console();
        String result;
        if (cons != null) {
            result = new String(cons.readPassword("Enter a secret: "));
        } else {
            System.err.println("No console found. Secrets you enter are shown in clear text.");
            try {
                result = new BufferedReader(new InputStreamReader(System.in))
                            .readLine();
                if (result == null) {
                    throw new IOException("Readline returned null");
                }
            } catch (IOException e) {
                throw new RuntimeException("IO error while trying to read secret", e);
            }
        }
        return result;
    }

    /**
     * Rudimentary method to print a secret to stdout. The secret is "crossed out". Just for demonstration purposes.
     */
    public static void printSecret(Object secret) {
        for (int i = 0; i < secret.toString().length(); i++)  {
            System.out.print("X");
        }
        System.out.println();
    }

    public static void printPublicDynamic(String msg) {
        System.out.println(msg);
    }

    /**
     * Reads a line of public data from stdin.
     */
    public static String read() {
        System.out.print("Enter public data: ");
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
