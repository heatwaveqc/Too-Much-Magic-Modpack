package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;

/**
 * Pure classifier for the agreed TARGET combinations. Does not authorize or
 * enforce transitions, modify upstream state, or choose competing mechanics.
 */
public final class FormResolver {
    private FormResolver() { }

    public static EffectiveForm resolve(StateSnapshot s) {
        if (!s.isComplete()) {
            return EffectiveForm.UNVERIFIED;
        }

        boolean vampire = s.present(VAMPIRISM_VAMPIRE) || s.present(JAWR_VAMPIRE);
        boolean werewolf = s.present(WEREWOLVES_WEREWOLF) || s.present(JAWR_LYCANTHROPE);
        boolean hunter = s.present(VAMPIRISM_HUNTER);
        boolean dragon = s.present(DRAGON_SURVIVAL_DRAGON);
        int liches = (s.present(FORBIDDEN_MAGIC_LICH) ? 1 : 0)
                    + (s.present(GOETY_LICH) ? 1 : 0);

        if (hunter) {
            return vampire || werewolf || dragon || liches > 0
                    ? EffectiveForm.CONFLICT : EffectiveForm.HUNTER;
        }
        if (dragon) {
            if (vampire || werewolf || liches > 1) {
                return EffectiveForm.CONFLICT;
            }
            return liches == 1 ? EffectiveForm.DRACOLICH : EffectiveForm.DRAGON;
        }
        if (vampire && werewolf) {
            return liches == 2 ? EffectiveForm.ABOMINATION : EffectiveForm.CONFLICT;
        }
        if (vampire || werewolf) {
            if (liches > 0) {
                return EffectiveForm.CONFLICT;
            }
            return vampire ? EffectiveForm.VAMPIRE : EffectiveForm.WEREWOLF;
        }
        if (liches == 2) {
            return EffectiveForm.ARCHLICH;
        }
        return liches == 1 ? EffectiveForm.LICH : EffectiveForm.HUMAN;
    }
}
