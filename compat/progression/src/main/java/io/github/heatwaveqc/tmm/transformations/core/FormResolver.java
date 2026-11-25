package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;

/** Read-only, complete-snapshot classifier; no automatic cures or species mutation. */
public final class FormResolver {
    private FormResolver() { }

    public static EffectiveForm resolve(StateSnapshot s) {
        if (s == null || !s.isComplete()) return EffectiveForm.UNVERIFIED;
        boolean vampire = s.present(JAWR_VAMPIRE);
        boolean werewolf = s.present(JAWR_LYCANTHROPE);
        boolean dragon = s.present(DRAGON_SURVIVAL_DRAGON);
        int liches = (s.present(FORBIDDEN_MAGIC_LICH) ? 1 : 0)
                   + (s.present(GOETY_LICH) ? 1 : 0);
        if (dragon) {
            if (vampire || werewolf || liches > 1) return EffectiveForm.CONFLICT;
            return liches == 1 ? EffectiveForm.DRACOLICH : EffectiveForm.DRAGON;
        }
        if (vampire && werewolf)
            return liches == 2 ? EffectiveForm.ABOMINATION : EffectiveForm.CONFLICT;
        if (vampire || werewolf)
            return liches == 0 ? (vampire ? EffectiveForm.VAMPIRE : EffectiveForm.WEREWOLF)
                               : EffectiveForm.CONFLICT;
        if (liches == 2) return EffectiveForm.ARCHLICH;
        return liches == 1 ? EffectiveForm.LICH : EffectiveForm.HUMAN;
    }
}
