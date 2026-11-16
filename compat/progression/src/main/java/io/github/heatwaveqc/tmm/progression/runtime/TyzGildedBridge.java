package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.core.GildedFortuneMath;
import io.github.heatwaveqc.tmm.progression.core.GildedLootingMath;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Optional Tyz 6.4.2 rank bridge, modeled after the existing native Monk Tyz bridge.
 * Tyz owns separate Fortune and Looting purchases/SP; Gilded owns the actions. No Tyz compile dependency.
 * A missing Tyz mod or absent skill gives the native +3 baseline, not zero Fortune.
 */
public final class TyzGildedBridge {
    public static final String SKILL_ID = "tmm_gilded_fortune_mastery";
    public static final String LOOTING_SKILL_ID = "tmm_gilded_looting_mastery";
    private static Object manager;
    private static Method getSkillLevel;

    private TyzGildedBridge() {}
    private static boolean installed() {
        return ModList.get().isLoaded("tyzs_skills");
    }
    public static void register() {
        if (!installed()) return;
        try {
            Class<?> pre = Class.forName("com.tyzsskills.api.events.SkillActionEvent$PurchasePre");
            if (!Event.class.isAssignableFrom(pre) || !ICancellableEvent.class.isAssignableFrom(pre))
                throw new IllegalStateException("Tyz pre-purchase event ABI mismatch");
            subscribe(pre, TyzGildedBridge::purchase);
            ProgressionMod.LOGGER.info("Registered independent Tyz Gilded Fortune/Looting purchase gates");
        } catch (ReflectiveOperationException | LinkageError failure) {
            ProgressionMod.LOGGER.error("Tyz installed but Gilded enchantment purchase gates unavailable",failure);
        }
    }
    @SuppressWarnings({"unchecked","rawtypes"})
    private static void subscribe(Class<?> type, Consumer<Event> callback) {
        NeoForge.EVENT_BUS.addListener((Class) type, (Consumer) callback);
    }
    private static void purchase(Event event) {
        try {
            Object skill = event.getClass().getMethod("getSkill").invoke(event);
            Object id = skill.getClass().getMethod("getID").invoke(skill);
            if (!SKILL_ID.equals(String.valueOf(id)) && !LOOTING_SKILL_ID.equals(String.valueOf(id))) return;
            Object candidate = event.getClass().getMethod("getPlayer").invoke(event);
            if (!(candidate instanceof ServerPlayer player) || !GildedRules.joined(player))
                ((ICancellableEvent)event).setCanceled(true);
        } catch (ReflectiveOperationException | RuntimeException failure) {
            // Only refuse a purchase when we can identify that it is the Gilded skill.
            // The actual Fortune effect always requires verified native membership.
            ProgressionMod.LOGGER.warn("Gilded Tyz purchase check failed; ensure Tyz ABI matches",failure);
        }
    }
    /**
     * Server-owned level, not a KubeJS client claim. Admin-granted ranks stay dormant
     * for non-Gilded players because GildedFortuneMath also checks native enrollment.
     */
    public static int rawRank(ServerPlayer player) {
        return rawRankFor(player,SKILL_ID);
    }
    public static int rawLootingRank(ServerPlayer player) {
        return rawRankFor(player,LOOTING_SKILL_ID);
    }
    private static int rawRankFor(ServerPlayer player, String skillId) {
        if (!installed()) return 0;
        if (getSkillLevel == null) {
            try {
                Class<?> api = Class.forName("com.tyzsskills.api.TyzsSkillsAPI");
                manager = api.getMethod("skills").invoke(null);
                getSkillLevel = manager.getClass().getMethod("getSkillLevel",ServerPlayer.class,String.class);
            } catch (ReflectiveOperationException | RuntimeException error) {
                manager = null;
                getSkillLevel = null;
                return 0;
            }
        }
        try {
            Object rank = getSkillLevel.invoke(manager,player,skillId);
            return rank instanceof Number n ?
                Math.max(0,Math.min(skillId.equals(LOOTING_SKILL_ID) ? GildedLootingMath.TYZ_RANKS : GildedFortuneMath.TYZ_RANKS,n.intValue())) : 0;
        } catch (ReflectiveOperationException | RuntimeException error) {
            return 0;
        }
    }
}
