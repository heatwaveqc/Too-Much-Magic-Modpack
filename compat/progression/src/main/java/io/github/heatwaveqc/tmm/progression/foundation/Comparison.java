/*
 * Adapted from HaooooZhang/Epiphany, revision 1cbffb31ef4474fec73d66f1a888b4db27c4a71d.
 * content/condition/Comparison.java
 * Original license: MIT; see third_party_licenses/Epiphany.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tmm.progression.foundation;


/**
 * Comparison operators shared across multiple Condition types.
 */
public enum Comparison {
    GREATER_OR_EQUAL(">="),
    LESS_OR_EQUAL("<="),
    EQUAL("=="),
    GREATER(">"),
    LESS("<");


    private final String key;

    Comparison(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static Comparison fromKey(String key) {
        return switch (key) {
            case ">=" -> GREATER_OR_EQUAL;
            case "<=" -> LESS_OR_EQUAL;
            case "==" -> EQUAL;
            case ">" -> GREATER;
            case "<" -> LESS;
            default -> throw new IllegalArgumentException("Unknown comparison: " + key);
        };
    }

    public boolean test(long actual, long expected) {
        return switch (this) {
            case GREATER_OR_EQUAL -> actual >= expected;
            case LESS_OR_EQUAL -> actual <= expected;
            case EQUAL -> actual == expected;
            case GREATER -> actual > expected;
            case LESS -> actual < expected;
        };
    }

    public boolean test(int actual, int expected) {
        return test((double) actual, (double) expected);
    }

    public boolean test(double actual, double expected) {
        return switch (this) {
            case GREATER_OR_EQUAL -> actual >= expected;
            case LESS_OR_EQUAL -> actual <= expected;
            case EQUAL -> actual == expected;
            case GREATER -> actual > expected;
            case LESS -> actual < expected;
        };
    }
}
