package utils;

import java.io.PrintStream;
import java.util.Collection;

import static resource.Configuration.DEBUG;
import resource.Configuration;
import soot.G;

public class Debugger {

    private interface Style {

        String getCode();

    }

    public static class Header {

        private final String header;
        private final String subheader;

        public Header(String header, String subheader) {
            this.header = header;
            this.subheader = subheader;
        }

        public Header(String header) {
            this.header = header;
            this.subheader = "";
        }

    }

    protected enum BGColor implements Style {
        BLACK(0),
        RED(1),
        GREEN(2),
        YELLOW(3),
        BLUE(4),
        MAGENTA(5),
        CYAN(6),
        WHITE(7);

        private int code = 0;

        private BGColor(int code) {
            this.code = code;
        }

        public String getCode() {
            if (highIntensity()) {
                return String.valueOf(100 + code);
            }
            return String.valueOf(40 + code);
        }

    }

    protected enum TextStyle implements Style {
        RESET(0),
        BOLD(1),
        ITALIC(3),
        UNDERLINE(4),
        NEGATIVE(7),
        CONCEAL(8),
        CROSSEDOUT(9),
        DOUBLE(21);

        private int code = 0;

        private TextStyle(int code) {
            this.code = code;
        }

        public String getCode() {
            return String.valueOf(code);
        }

    }

    protected enum TextColor implements Style {
        BLACK(0),
        RED(1),
        GREEN(2),
        YELLOW(3),
        BLUE(4),
        MAGENTA(5),
        CYAN(6),
        WHITE(7);

        private int code = 0;

        private TextColor(int code) {
            this.code = code;
        }

        public String getCode() {
            if (highIntensity()) {
                return String.valueOf(90 + code);
            }
            return String.valueOf(30 + code);
        }

    }

    private static final String ESC = (char) 27 + "[";
    private static final PrintStream OUT = G.v().out;

    public static void show(Header header) {
        formatAndShow(header.header, header.subheader, new String[] {}, null);
    }

    public static void show(String txt) {
        formatAndShow("", "", new String[] { txt }, null);
    }

    public static void show(Header header, String txt) {
        formatAndShow(header.header,
                      header.subheader,
                      new String[] { txt },
                      null);
    }

    public static void show(Header header,
                            String txt,
                            RuntimeException exception) {
        formatAndShow(header.header,
                      header.subheader,
                      new String[] { txt },
                      exception);
    }

    public static void show(Collection<String> txts) {
        formatAndShow("", "", txts.toArray(new String[] {}), null);
    }

    public static void show(Header header, Collection<String> txts) {
        formatAndShow(header.header,
                      header.subheader,
                      txts.toArray(new String[] {}),
                      null);
    }

    public static void show(Header header,
                            Collection<String> txts,
                            RuntimeException exception) {
        formatAndShow(header.header,
                      header.subheader,
                      txts.toArray(new String[] {}),
                      exception);
    }

    public static void show(String[] txts) {
        formatAndShow("", "", txts, null);
    }

    public static void show(Header header, String[] txts) {
        formatAndShow(header.header, header.subheader, txts, null);
    }

    public static void show(Header header,
                            String[] txts,
                            RuntimeException exception) {
        formatAndShow(header.header, header.subheader, txts, exception);
    }

    public static void showDependingOnError(Header header,
                                            String[] txts,
                                            RuntimeException exception) {
        if (exception != null)
            formatAndShow(header.header, header.subheader, txts, exception);
    }

    private static void formatAndShow(String title,
                                      String position,
                                      String[] txts,
                                      RuntimeException exception) {
        if (DEBUG) {
            String header =
                "DEBUG"
                        + ((title.isEmpty()) ? ""
                                            : (" - " + reduceString(title, 8)));
            String subheader =
                (position.isEmpty()) ? ""
                                    : ("[ " + reduceString(position, 4) + " ]");
            OUT.println();
            OUT.println(generateStyle(new Style[] { TextColor.BLUE,
                                                   TextStyle.BOLD,
                                                   TextStyle.DOUBLE })
                        + header
                        + repeat(Configuration.DISPLAY_SIZE - header.length(),
                                 " ")
                        + generateStyle(new Style[] { TextStyle.RESET }));
            if (!subheader.isEmpty()) {
                OUT.println(generateStyle(new Style[] { TextColor.BLUE,
                                                       TextStyle.BOLD })
                            + repeat(Configuration.DISPLAY_SIZE
                                             - subheader.length(),
                                     " ")
                            + subheader
                            + generateStyle(new Style[] { TextStyle.RESET }));
            }
            for (int i = 0; i < txts.length; i++) {
                OUT.println(generateStyle(new Style[] { TextColor.BLUE })
                            + txts[i]
                            + generateStyle(new Style[] { TextStyle.RESET }));
            }
        }
        if (exception != null)
            throw exception;
    }

    private static boolean highIntensity() {
        boolean highIntensity = false;
        if (System.getenv("HIGH_INTENSITY") != null
            && System.getenv("HIGH_INTENSITY").equals("true")) {
            highIntensity = true;
        }
        return highIntensity;
    }

    private static String generateStyle(Style[] styles) {
        String result = "";
        for (Style style : styles) {
            if (!result.isEmpty())
                result += ";";
            result += style.getCode();
        }
        return ESC + result + "m";
    }

    private static String reduceString(String string, int additional) {
        int size =
            Math.max(3, (Math.round(Configuration.DISPLAY_SIZE / 12) * 11)
                        - additional);
        if (string.length() > size) {
            return string.substring(0, size - 3) + "...";
        }
        return string;
    }

    private static String repeat(int n, String sign) {
        return new String(new char[Math.max(0, n)]).replace("\0", sign);
    }

}
