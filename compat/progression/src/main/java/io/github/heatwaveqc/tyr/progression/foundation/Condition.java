/*
 * Adapted from HaooooZhang/Epiphany, revision 1cbffb31ef4474fec73d66f1a888b4db27c4a71d.
 * content/condition/logic/AndCondition.java
 * content/condition/logic/OrCondition.java
 * content/condition/logic/NotCondition.java
 * Original license: MIT; see third_party_licenses/Epiphany.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.List;
import java.util.Objects;

/** Composable typed predicate; missing provider data is not a failed ordinary predicate. */
@FunctionalInterface
public interface Condition<C> {
    enum Result { PASS, FAIL, UNAVAILABLE }
    Result evaluate(C context);

    /** Only an affirmative result permits an acquisition or active effect. */
    default boolean test(C context) { return evaluate(context) == Result.PASS; }

    static <C> Condition<C> and(List<Condition<C>> children) {
        var copy = List.copyOf(children);
        if (copy.isEmpty()) throw new IllegalArgumentException("Empty AND");
        return context -> {
            boolean unavailable = false;
            for (var child : copy) {
                Result r = Objects.requireNonNull(child.evaluate(context));
                if (r == Result.FAIL) return Result.FAIL;
                if (r == Result.UNAVAILABLE) unavailable = true;
            }
            return unavailable ? Result.UNAVAILABLE : Result.PASS;
        };
    }
    static <C> Condition<C> or(List<Condition<C>> children) {
        var copy = List.copyOf(children);
        if (copy.isEmpty()) throw new IllegalArgumentException("Empty OR");
        return context -> {
            boolean unavailable = false;
            for (var child : copy) {
                Result r = Objects.requireNonNull(child.evaluate(context));
                if (r == Result.PASS) return Result.PASS;
                if (r == Result.UNAVAILABLE) unavailable = true;
            }
            return unavailable ? Result.UNAVAILABLE : Result.FAIL;
        };
    }
    static <C> Condition<C> not(Condition<C> child) {
        Objects.requireNonNull(child);
        return context -> switch (child.evaluate(context)) {
            case PASS -> Result.FAIL;
            case FAIL -> Result.PASS;
            case UNAVAILABLE -> Result.UNAVAILABLE;
        };
    }
}
