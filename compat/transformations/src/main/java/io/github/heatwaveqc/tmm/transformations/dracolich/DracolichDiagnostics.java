package io.github.heatwaveqc.tmm.transformations.dracolich;

import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichPolicy.Source;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Read-only first-phase Dracolich integration. Reports actual upstream state,
 * not "intended" markers; never grants species/lichdom or intercepts death.
 */
public final class DracolichDiagnostics {
    private DracolichDiagnostics() { }

    public static String describe(ServerPlayer player) {
        if (!ModList.get().isLoaded("dragonsurvival")
                || !ModList.get().isLoaded("goety")
                || !ModList.get().isLoaded("witchery_forbidden_magic")) {
            return "Dracolich: requires Dragon Survival, Goety, and Forbidden Magic"
                    + " for complete diagnostics. No state changes made.";
        }
        String dragonVersion = version("dragonsurvival");
        String goetyVersion = version("goety");
        String forbiddenVersion = version("witchery_forbidden_magic");
        if (!dragonVersion.startsWith("2.0.74")
                || !goetyVersion.startsWith("3.2.2")
                || !forbiddenVersion.startsWith("0.5.37")) {
            return "Dracolich: unverified mod version; Dragon Survival " + dragonVersion
                    + ", Goety " + goetyVersion
                    + ", Forbidden Magic " + forbiddenVersion
                    + ". Native state probe refused; do not infer player state.";
        }
        try {
            Class<?> dragonProvider = Class.forName(
                    "by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider");
            boolean dragon = (Boolean) dragonProvider.getMethod("isDragon", Entity.class)
                    .invoke(null, player);
            String species = "(human)";
            if (dragon) {
                Object handler = dragonProvider.getMethod("getData", Player.class)
                        .invoke(null, player);
                species = String.valueOf(handler.getClass().getMethod("speciesId")
                        .invoke(handler));
            }

            Class<?> goetyHelper = Class.forName("com.Polarice3.Goety.utils.LichdomHelper");
            boolean goety = (Boolean) goetyHelper.getMethod("isLich", Player.class)
                    .invoke(null, player);

            Class<?> forbiddenAttachment = Class.forName(
                    "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichPlayerAttachment");
            Object forbiddenData = forbiddenAttachment.getMethod("getData", Player.class)
                    .invoke(null, player);
            if (forbiddenData == null) {
                return "Dracolich: Forbidden Magic native data not available; "
                        + "classification UNKNOWN (fail closed).";
            }
            boolean forbidden = (Boolean) forbiddenData.getClass()
                    .getMethod("isLich").invoke(forbiddenData);
            int forbiddenLevel = ((Number) forbiddenData.getClass()
                    .getMethod("getLevel").invoke(forbiddenData)).intValue();

            Source source = DracolichPolicy.resolve(dragon, goety, forbidden);
            int hunger = player.getFoodData().getFoodLevel();
            float saturation = player.getFoodData().getSaturationLevel();

            return "Dracolich source=" + source
                    + "; dragon=" + dragon + " species=" + species
                    + "; Goety lich=" + goety
                    + "; Forbidden Magic lich=" + forbidden + " level=" + forbiddenLevel
                    + "; native hunger=" + hunger + ", saturation=" + saturation
                    + "; resurrection owner=" + switch (source) {
                        case GOETY_ARCA -> "native Goety Arca";
                        case FORBIDDEN_MAGIC_PHYLACTERY -> "native JAWR phylactery";
                        case CONFLICT -> "CONFLICT: two liches with a dragon (do not resolve automatically)";
                        case NONE -> "no Dracolich resurrection owner";
                    } + "; Cave/Center visual eligible=" + DracolichAppearanceEvents.active(player)
                    + "; client model/texture loading and visibility require client acceptance.";
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            return "Dracolich: native API not readable ("
                    + e.getClass().getSimpleName() + "): " + e.getMessage()
                    + "; no native mutations attempted.";
        }
    }

    private static String version(String modId) {
        return ModList.get().getModContainerById(modId)
                .map(mod -> mod.getModInfo().getVersion().toString())
                .orElse("unknown");
    }
}
