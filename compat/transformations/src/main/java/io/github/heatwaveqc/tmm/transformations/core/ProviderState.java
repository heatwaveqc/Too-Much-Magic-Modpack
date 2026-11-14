package io.github.heatwaveqc.tmm.transformations.core;

/**
 * Logical upstream player conditions, not guessed registry IDs or mod presence.
 * These names do NOT imply that a functioning reader has been installed.
 */
public enum ProviderState {
    VAMPIRISM_VAMPIRE,
    VAMPIRISM_HUNTER,
    WEREWOLVES_WEREWOLF,
    JAWR_VAMPIRE,
    JAWR_LYCANTHROPE,
    FORBIDDEN_MAGIC_LICH,
    GOETY_LICH,
    DRAGON_SURVIVAL_DRAGON
}
