package io.github.heatwaveqc.tmm.transformations.dracolich;

/** Pure, deliberately narrow cosmetic scope. Never changes native form data. */
public final class DracolichAppearancePolicy {
    private DracolichAppearancePolicy() { }

    public static boolean eligible(boolean dragon, boolean goety, boolean forbidden,
                                   boolean verified, String species, String body, String model) {
        return verified && dragon && (goety ^ forbidden) && supports(species, body, model);
    }

    public static boolean supports(String species, String body, String model) {
        return "dragonsurvival:cave_dragon".equals(species)
                && "dragonsurvival:center".equals(body)
                && "dragonsurvival:dragon_model".equals(model);
    }
}
