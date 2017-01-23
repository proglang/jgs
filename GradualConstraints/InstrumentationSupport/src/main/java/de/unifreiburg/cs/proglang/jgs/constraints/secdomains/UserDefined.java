package de.unifreiburg.cs.proglang.jgs.constraints.secdomains;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by fennell on 23.01.17.
 */
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
}
