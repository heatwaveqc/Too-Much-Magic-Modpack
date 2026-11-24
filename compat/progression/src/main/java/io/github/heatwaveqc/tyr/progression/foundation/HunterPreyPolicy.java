package io.github.heatwaveqc.tyr.progression.foundation;

/** Shared Hunter prey admission; caller supplies authoritative entity/tag/ownership facts. */
public final class HunterPreyPolicy {
    private HunterPreyPolicy() {}

    public static boolean eligible(boolean included, boolean excluded,
                                   boolean invalidHunterOrVictim, boolean tame,
                                   boolean playerOrUnknownOwner) {
        return included && !excluded && !invalidHunterOrVictim && !tame && !playerOrUnknownOwner;
    }
}
