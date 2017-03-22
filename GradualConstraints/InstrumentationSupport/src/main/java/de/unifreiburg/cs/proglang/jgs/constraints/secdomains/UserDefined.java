package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;

import java.util.*;

public class UserDefined implements SecDomain<String> {


    public final Set<String> levels;
    public final Set<Edge> lt;
    public final Map<Edge, String> lubMap;
    public final Map<Edge, String> glbMap;
    public final String topLevel;
    public final String bottomLevel;

    public UserDefined(Set<String> levels, Set<Edge> lt, Map<Edge, String>
            lubMap, Map<Edge, String> glbMap, String topLevel, String
            bottomLevel) {
        this.levels = levels;
        this.lt = lt;
        this.lubMap = lubMap;
        this.glbMap = glbMap;
        this.topLevel = topLevel;
        this.bottomLevel = bottomLevel;
    }

    @Override
    public String bottom() {
        return bottomLevel;
    }

    @Override
    public String top() {
        return topLevel;
    }

    @Override
    public String lub(String l1, String l2) {
        return lubMap.get(new Edge(l1, l2));
    }

    @Override
    public String glb(String l1, String l2) {
        return glbMap.get(new Edge(l1, l2));
    }

    @Override
    public boolean le(String l1, String l2) {
        return l1.equals(l2) || lt.contains(new Edge(l1, l2));
    }

    @Override
    public String readLevel(String s) {
        if (!levels.contains(s)) {
            throw new UnknownSecurityLevelException(s);
        }
        return s;
    }

    @Override
    public Iterator<String> enumerate() {
        return levels.iterator();
    }

    @Override
    public String toString() {
       return String.format("lt: %s, top: %s, bottom: %s", lt.toString(),
                     topLevel, bottomLevel);
    }

    public static class Edge {
        public final String left;
        public final String right;

        public Edge(String left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (left != null ? !left.equals(edge.left) : edge.left != null)
                return false;
            return right != null ? right.equals(edge.right) : edge.right
                                                              == null;

        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }
    }

    /*
    Some examples of security domains.
     */
    public static final UserDefined lowHigh() {
        Set<String> levels = new HashSet<>(Arrays.asList("LOW", "HIGH"));
        Set<Edge> lt = new HashSet<>(Arrays.asList(new UserDefined.Edge("LOW", "HIGH")));
        Map<Edge, String> lubMap = new HashMap<>();
        for (String l : levels) {
            lubMap.put(new Edge(l, l), l);
            lubMap.put(new Edge(l, "HIGH"), "HIGH");
            lubMap.put(new Edge("HIGH", l), "HIGH");
            lubMap.put(new Edge("LOW", l), l);
            lubMap.put(new Edge(l, "LOW"), l);
        }
        Map<Edge, String> glbMap = new HashMap<>();
        for (String l : levels) {
            glbMap.put(new Edge(l, l), l);
            glbMap.put(new Edge(l, "HIGH"), l);
            glbMap.put(new Edge("HIGH", l), l);
            glbMap.put(new Edge("LOW", l), "LOW");
            glbMap.put(new Edge(l, "LOW"), "LOW");
        }
        return new UserDefined(levels, lt, lubMap, glbMap, "HIGH", "LOW");
    }
    public static final UserDefined aliceBobCharlie() {
        Set<String> levels = new HashSet<>(Arrays.asList("BOT", "alice", "bob", "charlie", "TOP"));
        Set<Edge> lt = new HashSet<>(Arrays.asList(new UserDefined.Edge("BOT", "alice"),
                                                   new UserDefined.Edge("BOT", "bob"),
                                                   new UserDefined.Edge("BOT", "charlie"),
                                                   new UserDefined.Edge("alice", "TOP"),
                                                   new UserDefined.Edge("bob", "TOP"),
                                                   new UserDefined.Edge("charlie", "TOP")
                                                   ));
        Map<Edge, String> lubMap = new HashMap<>();
        for (String l : levels) {
            lubMap.put(new Edge(l, l), l);
            for (String l2 : levels) {
                if (!l.equals(l2) && !l2.equals("BOT")) {
                    lubMap.put(new Edge(l, l2), "TOP");
                    lubMap.put(new Edge(l2, l), "TOP");
                }
            }
            lubMap.put(new Edge(l, "TOP"), "TOP");
            lubMap.put(new Edge("TOP", l), "TOP");
            lubMap.put(new Edge("BOT", l), l);
            lubMap.put(new Edge(l, "BOT"), l);
        }
        Map<Edge, String> glbMap = new HashMap<>();
        for (String l : levels) {
            glbMap.put(new Edge(l, l), l);
            for (String l2 : levels) {
                if (!l.equals(l2) && !l2.equals("TOP")) {
                    lubMap.put(new Edge(l, l2), "BOT");
                    lubMap.put(new Edge(l2, l), "BOT");
                }
            }
            glbMap.put(new Edge(l, "TOP"), l);
            glbMap.put(new Edge("TOP", l), l);
            glbMap.put(new Edge("BOT", l), "BOT");
            glbMap.put(new Edge(l, "BOT"), "BOT");
        }
        return new UserDefined(levels, lt, lubMap, glbMap, "TOP", "BOT");
    }

}
