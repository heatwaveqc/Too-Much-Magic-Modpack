package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;

/** Distinct item-registry IDs consumed by this player, saved independently from path XP. */
public record Discoveries(List<String> foods) {
    public static final Codec<Discoveries> CODEC =
        Codec.STRING.listOf().xmap(Discoveries::new, Discoveries::foods);

    public Discoveries { foods = List.copyOf(foods); }
    public static Discoveries empty() { return new Discoveries(List.of()); }
    public boolean contains(String itemId) { return foods.contains(itemId); }
    public Discoveries discover(String itemId) {
        if (contains(itemId)) return this;
        List<String> next = new ArrayList<>(foods);
        next.add(itemId);
        return new Discoveries(next);
    }
}
