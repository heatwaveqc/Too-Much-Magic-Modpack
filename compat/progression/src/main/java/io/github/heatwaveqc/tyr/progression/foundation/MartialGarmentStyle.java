package io.github.heatwaveqc.tyr.progression.foundation;

/** One canonical roster, stable registry IDs and default cosmetic palette.
 * All styles deliberately have the same training mechanics. */
public enum MartialGarmentStyle {
    KARATE_GI("karate_gi", GarmentPalette.of(0, 0, 15)),
    TAEKWONDO_DOBOK("taekwondo_dobok", GarmentPalette.of(0, 15, 15)),
    SHAOLIN("shaolin", GarmentPalette.of(1, 7, 15)),
    KUNG_FU("kung_fu", GarmentPalette.of(15, 0, 14)),
    KIMONO_HAKAMA("kimono_hakama", GarmentPalette.of(11, 15, 0)),
    WUXIA("wuxia", GarmentPalette.of(0, 11, 4));

    private final String id;
    private final GarmentPalette defaults;
    MartialGarmentStyle(String id, GarmentPalette defaults) {
        this.id = id;
        this.defaults = defaults;
    }
    public String id() { return id; }
    public GarmentPalette defaults() { return defaults; }
    public String upperId() { return id + "_upper"; }
    public String lowerId() { return id + "_lower"; }
}
