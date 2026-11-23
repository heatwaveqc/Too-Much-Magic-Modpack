package io.github.heatwaveqc.tmm.friendship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/** Pure authorization/deduplication policy for standard splash-potion effects. */
public final class SplashTransmission {
    private static final Pattern ID = Pattern.compile("[a-z0-9_.-]+:[a-z0-9/._-]+");

    private SplashTransmission() {}

    public enum Classification { BENEFICIAL, HARMFUL, UNKNOWN }

    /** Effect values are returned unchanged; this policy never adds a duration cap. */
    public record Effect(String effectId, int amplifier, int durationTicks,
                         boolean instant, Classification classification, boolean standard) {
        public Effect {
            requireId(effectId, "effect");
            if (amplifier < 0 || durationTicks < 0)
                throw new IllegalArgumentException("effect amplifier/duration cannot be negative");
            Objects.requireNonNull(classification, "classification");
        }
    }

    public record Config(boolean enabled, boolean beneficialEnabled, boolean crossDimension,
                         Set<String> blockedPotions, Set<String> blockedEffects) {
        public Config {
            blockedPotions = validatedIds(blockedPotions, "potion blacklist");
            blockedEffects = validatedIds(blockedEffects, "effect blacklist");
        }
        public static Config defaults() {
            return new Config(true, true, true, Set.of(), Set.of());
        }
    }

    /** Current server-owned recipient state; absence means offline. */
    public record Recipient(UUID playerId, String dimensionId, boolean alive, boolean harmfulOptIn) {
        public Recipient {
            Objects.requireNonNull(playerId, "playerId");
            requireId(dimensionId, "dimension");
        }
    }

    public record Delivery(UUID playerId, Effect effect) {}

    public static List<Delivery> resolve(
            String potionId,
            String splashDimensionId,
            long nowTick,
            List<Effect> effects,
            List<FriendshipFlower> flowersInSplash,
            Map<UUID, Recipient> onlineRecipients,
            Config config,
            boolean serverPvpAllowsHarmful) {

        requireId(potionId, "potion");
        requireId(splashDimensionId, "splash dimension");
        if (nowTick < 0) throw new IllegalArgumentException("server tick cannot be negative");
        Objects.requireNonNull(effects, "effects");
        Objects.requireNonNull(flowersInSplash, "flowersInSplash");
        Objects.requireNonNull(onlineRecipients, "onlineRecipients");
        Objects.requireNonNull(config, "config");
        if (!config.enabled() || config.blockedPotions().contains(potionId)) return List.of();

        Map<UUID, FriendshipFlower> uniqueOwners = new LinkedHashMap<>();
        Set<UUID> mourningOwners = new HashSet<>();
        for (FriendshipFlower flower : flowersInSplash) {
            Objects.requireNonNull(flower, "flower");
            uniqueOwners.putIfAbsent(flower.crafterId(), flower);
            if (flower.mourning(nowTick)) mourningOwners.add(flower.crafterId());
        }

        List<Delivery> result = new ArrayList<>();
        Set<DeliveryKey> delivered = new HashSet<>();
        for (UUID owner : uniqueOwners.keySet()) {
            if (mourningOwners.contains(owner)) continue;
            Recipient recipient = onlineRecipients.get(owner);
            if (recipient == null || !owner.equals(recipient.playerId()) || !recipient.alive()) continue;
            if (!config.crossDimension() && !splashDimensionId.equals(recipient.dimensionId())) continue;
            for (Effect effect : effects) {
                if (!effect.standard() || config.blockedEffects().contains(effect.effectId())) continue;
                if (!allowed(effect.classification(), recipient, config, serverPvpAllowsHarmful)) continue;
                DeliveryKey key = new DeliveryKey(owner, effect.effectId());
                if (delivered.add(key)) result.add(new Delivery(owner, effect));
            }
        }
        return List.copyOf(result);
    }

    private static boolean allowed(Classification classification, Recipient recipient,
                                   Config config, boolean pvpAllowsHarmful) {
        if (classification == Classification.BENEFICIAL) return config.beneficialEnabled();
        return recipient.harmfulOptIn() && pvpAllowsHarmful;
    }

    private record DeliveryKey(UUID owner, String effectId) {}

    private static Set<String> validatedIds(Set<String> ids, String field) {
        Objects.requireNonNull(ids, field);
        Set<String> copy = new HashSet<>();
        for (String id : ids) {
            requireId(id, field);
            copy.add(id);
        }
        return Set.copyOf(copy);
    }

    private static void requireId(String id, String field) {
        if (id == null || !ID.matcher(id).matches())
            throw new IllegalArgumentException("invalid " + field + " ID: " + id);
    }
}
