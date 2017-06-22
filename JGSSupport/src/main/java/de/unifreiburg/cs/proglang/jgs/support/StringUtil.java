package de.unifreiburg.cs.proglang.jgs.support;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static String append(String s1, String s2) {
        return s1 + s2;
    }


    public static List<Boolean> bits(String s) {
        List<Boolean> result = new ArrayList<>();
        for (byte c : s.getBytes(StandardCharsets.UTF_8)) {
            for (int i = 0; i < 8; i++) {
                result.add(((c >> i) & 1) == 1);
            }
        }
        return result;
    }
}
