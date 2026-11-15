package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.EffectiveForm.*;

/**
 * Pure, fail-closed preflight for complete before/after provider snapshots.
 * Does not alter player data or certify an upstream interception hook.
 */
public final class TransitionPolicy {
    private TransitionPolicy() { }

    public static TransitionDecision assess(StateSnapshot before, StateSnapshot after, TransitionKind kind) {
        if (before == null || after == null || kind == null) {
            return TransitionDecision.deny(UNVERIFIED, UNVERIFIED, "Missing transition inputs");
        }
        EffectiveForm from = FormResolver.resolve(before);
        EffectiveForm to = FormResolver.resolve(after);
        if (from == UNVERIFIED || to == UNVERIFIED) {
            return TransitionDecision.deny(from, to, "Incomplete provider observations; deny unknown state");
        }
        if (from == CONFLICT) {
            return TransitionDecision.deny(from, to, "Existing conflict requires backed-up administrative review");
        }
        if (to == CONFLICT) {
            return TransitionDecision.deny(from, to, "Proposed state violates the approved combination matrix");
        }

        boolean added = false;
        boolean removed = false;
        for (ProviderState provider : ProviderState.values()) {
            boolean oldPresent = before.present(provider);
            boolean newPresent = after.present(provider);
            added |= !oldPresent && newPresent;
            removed |= oldPresent && !newPresent;
        }
        if (!added && !removed) {
            return kind == TransitionKind.NORMAL
                    ? TransitionDecision.allow(from, to, "No provider-state change (ordinary progression)")
                    : TransitionDecision.deny(from, to, "Special transition cannot be a no-op");
        }

        return switch (kind) {
            case NORMAL -> {
                if (removed) {
                    yield TransitionDecision.deny(from, to, "Ordinary acquisition cannot remove existing states");
                }
                boolean edge = switch (from) {
                    case HUMAN -> to == HUNTER || to == VAMPIRE || to == WEREWOLF || to == LICH || to == DRAGON;
                    case VAMPIRE -> to == VAMPIRE;
                    case WEREWOLF -> to == WEREWOLF;
                    case LICH -> to == LICH || to == ARCHLICH || to == DRACOLICH;
                    case DRAGON -> to == DRAGON || to == DRACOLICH;
                    case ARCHLICH -> to == ARCHLICH;
                    case DRACOLICH -> to == DRACOLICH;
                    case ABOMINATION -> to == ABOMINATION;
                    case HUNTER -> to == HUNTER;
                    default -> false;
                };
                yield edge
                        ? TransitionDecision.allow(from, to, "Permitted state progression; provider hooks still require verification")
                        : TransitionDecision.deny(from, to, "No approved ordinary progression edge");
            }
            case CONTROLLED_CURE -> {
                if (added) {
                    yield TransitionDecision.deny(from, to, "A cure cannot add another state");
                }
                yield TransitionDecision.allow(from, to,
                        "Valid post-cure classification; provider cleanup and reversibility require verification");
            }
            case VOLUNTARY_HUNTER_REPLACEMENT -> {
                if (from != HUNTER || !before.present(ProviderState.VAMPIRISM_HUNTER)
                        || after.present(ProviderState.VAMPIRISM_HUNTER)
                        || (to != VAMPIRE && to != WEREWOLF && to != LICH && to != DRAGON)) {
                    yield TransitionDecision.deny(from, to, "Hunter replacement must atomically leave hunter for one base supernatural state");
                }
                yield TransitionDecision.allow(from, to,
                        "Voluntary hunter replacement; clear the faction before applying the new state");
            }
            case ABOMINATION_RITUAL -> {
                if ((from != VAMPIRE && from != WEREWOLF) || to != ABOMINATION || removed) {
                    yield TransitionDecision.deny(from, to,
                            "Abomination ritual requires an existing vampire or werewolf, both parent states, and no removals");
                }
                yield TransitionDecision.allow(from, to,
                        "Explicit vampire/werewolf hybridization permitted; JAWR native hybrid behavior awaits verification");
            }
        };
    }
}
