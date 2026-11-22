package io.github.heatwaveqc.tmm.friendship;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/** Immutable persistent identity and server-derived visual state for one bound flower. */
public record FriendshipFlower(
        UUID crafterId,
        String crafterDisplayName,
        String originalFlowerId,
        Form form,
        long mourningUntilTick,
        boolean permanentlyWilted) {

    public static final long MOURNING_TICKS = 5L * 60L * 20L;
    private static final Pattern REGISTRY_ID = Pattern.compile("[a-z0-9_.-]+:[a-z0-9/._-]+");

    public enum Form { SHORT, TALL }
    public enum Appearance { BLOOMING, BUDDING, WILTED }

    public FriendshipFlower {
        Objects.requireNonNull(crafterId, "crafterId");
        Objects.requireNonNull(form, "form");
        crafterDisplayName = Objects.requireNonNull(crafterDisplayName, "crafterDisplayName").strip();
        if (crafterDisplayName.isEmpty() || crafterDisplayName.length() > 64)
            throw new IllegalArgumentException("crafter display name must contain 1..64 characters");
        if (originalFlowerId == null || !REGISTRY_ID.matcher(originalFlowerId).matches())
            throw new IllegalArgumentException("invalid original flower registry ID: " + originalFlowerId);
        if (mourningUntilTick < 0) throw new IllegalArgumentException("mourning tick cannot be negative");
    }

    /** The authoritative crafting hook supplies the crafter; placer identity is never accepted here. */
    public static FriendshipFlower craftedBy(UUID crafterId, String displayName,
                                              String originalFlowerId, Form form) {
        return new FriendshipFlower(crafterId, displayName, originalFlowerId, form, 0, false);
    }

    public FriendshipFlower onCrafterDeath(long nowTick, boolean permanentDeath) {
        if (nowTick < 0) throw new IllegalArgumentException("server tick cannot be negative");
        if (permanentDeath)
            return permanentlyWilted ? this
                : new FriendshipFlower(crafterId, crafterDisplayName, originalFlowerId, form,
                    mourningUntilTick, true);
        long until = nowTick > Long.MAX_VALUE - MOURNING_TICKS
            ? Long.MAX_VALUE : nowTick + MOURNING_TICKS;
        if (!permanentlyWilted && until <= mourningUntilTick) return this;
        return new FriendshipFlower(crafterId, crafterDisplayName, originalFlowerId, form,
            Math.max(mourningUntilTick, until), permanentlyWilted);
    }

    public boolean mourning(long nowTick) {
        if (nowTick < 0) throw new IllegalArgumentException("server tick cannot be negative");
        return permanentlyWilted || nowTick < mourningUntilTick;
    }

    public Appearance appearance(boolean crafterOnline, long nowTick) {
        if (mourning(nowTick)) return Appearance.WILTED;
        return crafterOnline ? Appearance.BLOOMING : Appearance.BUDDING;
    }
}
