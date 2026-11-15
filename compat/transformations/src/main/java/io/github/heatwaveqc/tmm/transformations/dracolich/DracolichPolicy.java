package io.github.heatwaveqc.tmm.transformations.dracolich;

/**
 * Target morphology: one real Dragon Survival form + exactly one real lich
 * provider. Two liches with a dragon remain a conflict, not an Archlich
 * or a silently stacked third form. This is classification, not acquisition.
 */
public final class DracolichPolicy {
    public enum Source { NONE, GOETY_ARCA, FORBIDDEN_MAGIC_PHYLACTERY, CONFLICT }

    private DracolichPolicy() { }

    public static Source resolve(boolean dragon, boolean goetyLich,
                                 boolean forbiddenMagicLich) {
        if (!dragon) return Source.NONE;
        if (goetyLich && forbiddenMagicLich) return Source.CONFLICT;
        if (goetyLich) return Source.GOETY_ARCA;
        if (forbiddenMagicLich) return Source.FORBIDDEN_MAGIC_PHYLACTERY;
        return Source.NONE;
    }

    public static boolean isDracolich(Source source) {
        return source == Source.GOETY_ARCA
                || source == Source.FORBIDDEN_MAGIC_PHYLACTERY;
    }
}
