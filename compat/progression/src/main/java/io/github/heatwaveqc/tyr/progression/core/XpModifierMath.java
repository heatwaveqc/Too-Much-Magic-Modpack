package io.github.heatwaveqc.tyr.progression.core;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Pure deterministic composition of eligible, already-authenticated modifiers. */
public final class XpModifierMath {
    private XpModifierMath() {}
    public static final double MAX_REWARD_MULTIPLIER = 1_000_000.0;
    public enum Operation { ADD, MULTIPLY, REPLACE }
    public enum Kind { REWARD, GROWTH }

    public record Modifier(String id, int priority, boolean stack, Operation operation,
                           double value, Double minimum, Double maximum) {
        public Modifier {
            Objects.requireNonNull(id);
            Objects.requireNonNull(operation);
            if (id.isBlank() || !Double.isFinite(value) || Math.abs(value) > MAX_REWARD_MULTIPLIER
                    || operation != Operation.ADD && value < 0
                    || minimum != null && (!Double.isFinite(minimum) || minimum < 0
                        || minimum > MAX_REWARD_MULTIPLIER)
                    || maximum != null && (!Double.isFinite(maximum) || maximum < 0
                        || maximum > MAX_REWARD_MULTIPLIER)
                    || minimum != null && maximum != null && minimum > maximum)
                throw new IllegalArgumentException("Invalid XP modifier " + id);
        }
    }

    public record Resolution(double value, List<String> applied, boolean conflict) {
        public Resolution { applied = List.copyOf(applied); }
    }

    /**
     * Highest priority chooses exclusive or explicitly stackable mode.
     * An exclusive highest-priority tie fails closed to the base.
     * If the winning mode is stackable, only explicitly stackable rules
     * participate, from low priority to high (ID breaks equal-priority ties).
     * No implicit cross-Path composition. Bounds apply after each operation.
     */
    public static Resolution resolve(Kind kind, double base, List<Modifier> candidates) {
        Objects.requireNonNull(kind);
        if (!Double.isFinite(base) || base < lower(kind) || base > upper(kind))
            throw new IllegalArgumentException("Invalid modifier base");
        if (candidates.isEmpty()) return new Resolution(base, List.of(), false);
        int highest = candidates.stream().mapToInt(Modifier::priority).max().orElseThrow();
        var top = candidates.stream().filter(m -> m.priority() == highest).toList();
        if (top.size() > 1 && top.stream().anyMatch(m -> !m.stack()))
            return new Resolution(base, List.of(), true);
        var selected = top.getFirst().stack()
                ? candidates.stream().filter(Modifier::stack)
                    .sorted(Comparator.comparingInt(Modifier::priority).thenComparing(Modifier::id)).toList()
                : List.of(top.getFirst());
        double value = base;
        for (Modifier modifier : selected) {
            value = switch (modifier.operation()) {
                case ADD -> value + modifier.value();
                case MULTIPLY -> value * modifier.value();
                case REPLACE -> modifier.value();
            };
            if (modifier.minimum() != null) value = Math.max(value, modifier.minimum());
            if (modifier.maximum() != null) value = Math.min(value, modifier.maximum());
            value = Math.max(lower(kind), Math.min(upper(kind), value));
        }
        return new Resolution(value, selected.stream().map(Modifier::id).toList(), false);
    }

    public static double lower(Kind kind) {
        return kind == Kind.GROWTH ? ExperienceEconomy.MIN_GROWTH : 0;
    }
    public static double upper(Kind kind) {
        return kind == Kind.GROWTH ? ExperienceEconomy.MAX_GROWTH : MAX_REWARD_MULTIPLIER;
    }
}
