package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.EffectiveForm.*;
import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;

/** Pure preflight. This does not grant, remove, or cure native transformations. */
public final class TransitionPolicy {
    private TransitionPolicy() { }

    public static TransitionDecision assess(StateSnapshot before, StateSnapshot after, TransitionKind kind) {
        if (before == null || after == null || kind == null)
            return TransitionDecision.deny(UNVERIFIED, UNVERIFIED, "Missing transition inputs");
        EffectiveForm from = FormResolver.resolve(before), to = FormResolver.resolve(after);
        if (from == UNVERIFIED || to == UNVERIFIED)
            return TransitionDecision.deny(from, to, "Incomplete provider observations");
        if (from == CONFLICT || to == CONFLICT)
            return TransitionDecision.deny(from, to, "Unapproved native form combination");
        boolean added = false, removed = false;
        for (ProviderState provider : ProviderState.values()) {
            added |= !before.present(provider) && after.present(provider);
            removed |= before.present(provider) && !after.present(provider);
        }
        if (!added && !removed) {
            return kind == TransitionKind.NORMAL
                ? TransitionDecision.allow(from, to, "No native form change")
                : TransitionDecision.deny(from, to, "Special transition cannot be a no-op");
        }
        return switch (kind) {
            case NORMAL -> {
                if (removed) yield TransitionDecision.deny(from, to, "Ordinary acquisition cannot remove native forms");
                boolean permitted = switch (from) {
                    case HUMAN -> to == VAMPIRE || to == WEREWOLF || to == LICH || to == DRAGON;
                    case VAMPIRE -> to == VAMPIRE;
                    case WEREWOLF -> to == WEREWOLF;
                    case LICH -> to == LICH || to == ARCHLICH || to == DRACOLICH;
                    case DRAGON -> to == DRAGON || to == DRACOLICH;
                    case ARCHLICH -> to == ARCHLICH;
                    case DRACOLICH -> to == DRACOLICH;
                    case ABOMINATION -> to == ABOMINATION;
                    default -> false;
                };
                yield permitted
                    ? TransitionDecision.allow(from, to, "Permitted preflight; upstream callbacks still require QA")
                    : TransitionDecision.deny(from, to, "No approved ordinary progression edge");
            }
            case CONTROLLED_CURE -> {
                if (added) yield TransitionDecision.deny(from, to, "Cure cannot add a native form");
                yield TransitionDecision.allow(from, to, "Classifier permits reduction; native cure not performed");
            }
            case ABOMINATION_RITUAL -> {
                if (from != ARCHLICH || to != ABOMINATION || removed
                        || !after.present(GOETY_LICH) || !after.present(FORBIDDEN_MAGIC_LICH)
                        || !after.present(JAWR_VAMPIRE) || !after.present(JAWR_LYCANTHROPE))
                    yield TransitionDecision.deny(from, to, "Abomination requires both retained lichdoms and both JAWR forms");
                yield TransitionDecision.allow(from, to, "Preflight only; no ritual/interception shipped");
            }
        };
    }
}
