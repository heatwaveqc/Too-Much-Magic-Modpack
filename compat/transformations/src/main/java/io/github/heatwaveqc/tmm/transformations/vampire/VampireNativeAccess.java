package io.github.heatwaveqc.tmm.transformations.vampire;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Exact 1.21.1 pinned-binary interfaces. No guesses about NBT or capability IDs.
 * Reflection prevents linking other mods when the bridge loads independently.
 */
public final class VampireNativeAccess {
    private VampireNativeAccess() { }

    private static Class<?> type(String name) throws ReflectiveOperationException {
        return Class.forName(name);
    }

    private static Object callStatic(String className, String name, Class<?>[] signature, Object... args)
            throws ReflectiveOperationException {
        return type(className).getMethod(name, signature).invoke(null, args);
    }

    public static Object vampirismHandler(ServerPlayer player) throws ReflectiveOperationException {
        return callStatic("de.teamlapen.vampirism.api.VampirismAPI",
                "factionPlayerHandler", new Class<?>[]{Player.class}, player);
    }

    public static Object vampirismVampireFaction() throws ReflectiveOperationException {
        return type("de.teamlapen.vampirism.api.VReference").getField("VAMPIRE_FACTION").get(null);
    }

    public static Object currentFaction(ServerPlayer player) throws ReflectiveOperationException {
        return vampirismHandler(player).getClass().getMethod("getCurrentFaction").invoke(vampirismHandler(player));
    }

    public static boolean isVampirismVampire(ServerPlayer player) throws ReflectiveOperationException {
        Object faction = currentFaction(player);
        if (faction == null) return false;
        Object id = faction.getClass().getMethod("getID").invoke(faction);
        return id instanceof ResourceLocation location && "vampirism:vampire".equals(location.toString());
    }

    private static boolean asBool(Object value) throws ReflectiveOperationException {
        if (value instanceof Boolean result) return result;
        throw new ReflectiveOperationException("Upstream did not return boolean");
    }

    public static boolean canJoinVampirism(ServerPlayer player) throws ReflectiveOperationException {
        Object handler = vampirismHandler(player);
        Object faction = vampirismVampireFaction();
        if (faction == null) return false;
        return asBool(handler.getClass().getMethod("canJoin",
                type("de.teamlapen.vampirism.api.entity.factions.IPlayableFaction"))
                .invoke(handler, faction));
    }

    public static boolean canLeaveVampirism(ServerPlayer player) throws ReflectiveOperationException {
        Object handler = vampirismHandler(player);
        return asBool(handler.getClass().getMethod("canLeaveFaction").invoke(handler));
    }

    public static boolean enterVampirism(ServerPlayer player, int level) throws ReflectiveOperationException {
        Object handler = vampirismHandler(player);
        Object faction = vampirismVampireFaction();
        if (faction == null) return false;
        return asBool(handler.getClass().getMethod("setFactionAndLevel",
                type("de.teamlapen.vampirism.api.entity.factions.IPlayableFaction"), int.class)
                .invoke(handler, faction, level));
    }

    public static int vampirismLevel(ServerPlayer player) throws ReflectiveOperationException {
        Object handler = vampirismHandler(player);
        return ((Number) handler.getClass().getMethod("getCurrentLevel").invoke(handler)).intValue();
    }

    public static boolean leaveVampirism(ServerPlayer player) throws ReflectiveOperationException {
        Object handler = vampirismHandler(player);
        return asBool(handler.getClass().getMethod("setFactionAndLevel",
                type("de.teamlapen.vampirism.api.entity.factions.IPlayableFaction"), int.class)
                .invoke(handler, null, 0));
    }

    public static int jawrLevel(ServerPlayer player) throws ReflectiveOperationException {
        Object data = jawrVampireData(player);
        return ((Number) data.getClass().getMethod("getLevel").invoke(data)).intValue();
    }

    public static Object jawrVampireData(ServerPlayer player) throws ReflectiveOperationException {
        return callStatic("dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment",
                "getData", new Class<?>[]{Player.class}, player);
    }

    public static void setJAWRLevel(ServerPlayer player, int level) throws ReflectiveOperationException {
        Class<?> cls = type("dev.sterner.witchery.feature.affliction.vampire.VampireLeveling");
        Object singleton = cls.getField("INSTANCE").get(null);
        cls.getMethod("setLevel", ServerPlayer.class, int.class).invoke(singleton, player, level);
    }

    /** Use JAWR's actual native initialization path; setLevel(1) alone misses max blood update. */
    public static void startJAWR(ServerPlayer player) throws ReflectiveOperationException {
        Class<?> cls = type("dev.sterner.witchery.feature.affliction.vampire.VampireLeveling");
        Object singleton = cls.getField("INSTANCE").get(null);
        cls.getMethod("increaseLevel", ServerPlayer.class).invoke(singleton, player);
    }

    public static Object vampirismVampire(ServerPlayer player) throws ReflectiveOperationException {
        return callStatic("de.teamlapen.vampirism.api.VampirismAPI",
                "vampirePlayer", new Class<?>[]{Player.class}, player);
    }

    public static int nativeMax(ServerPlayer player) throws ReflectiveOperationException {
        Object stats = vampirismVampire(player).getClass().getMethod("getBloodStats")
                .invoke(vampirismVampire(player));
        return ((Number) stats.getClass().getMethod("getMaxBlood").invoke(stats)).intValue();
    }

    public static int nativeCurrent(ServerPlayer player) throws ReflectiveOperationException {
        Object vp = vampirismVampire(player);
        return ((Number) vp.getClass().getMethod("getBloodLevel").invoke(vp)).intValue();
    }

    public static void gainNativeBlood(ServerPlayer player, int amount) throws ReflectiveOperationException {
        if (amount <= 0) return;
        Object vp = vampirismVampire(player);
        Class<?> context = type("de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext");
        Object noSource = type("de.teamlapen.vampirism.entity.vampire.DrinkBloodContext")
                .getMethod("none").invoke(null);
        vp.getClass().getMethod("drinkBlood", int.class, float.class, boolean.class, context)
                .invoke(vp, amount, 0.5F, false, noSource);
    }

    public static boolean spendNativeBlood(ServerPlayer player, int amount) throws ReflectiveOperationException {
        Object vp = vampirismVampire(player);
        return asBool(vp.getClass().getMethod("useBlood", int.class, boolean.class)
                .invoke(vp, amount, true));
    }

    public static Object jawrBloodData(ServerPlayer player) throws ReflectiveOperationException {
        return callStatic("dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment",
                "getData", new Class<?>[]{LivingEntity.class}, player);
    }

    public static int jawrMax(ServerPlayer player) throws ReflectiveOperationException {
        Object data = jawrBloodData(player);
        return ((Number) data.getClass().getMethod("getMaxBlood").invoke(data)).intValue();
    }

    public static int jawrCurrent(ServerPlayer player) throws ReflectiveOperationException {
        Object data = jawrBloodData(player);
        return ((Number) data.getClass().getMethod("getBloodPool").invoke(data)).intValue();
    }

    public static void setJAWRShadowBlood(ServerPlayer player, int value) throws ReflectiveOperationException {
        Object oldData = jawrBloodData(player);
        int max = ((Number) oldData.getClass().getMethod("getMaxBlood").invoke(oldData)).intValue();
        if (max <= 0) throw new ReflectiveOperationException("JAWR blood capacity is uninitialized");
        int clamped = Math.max(0, Math.min(max, value));
        Object newData = oldData.getClass().getMethod("copy", int.class, int.class)
                .invoke(oldData, max, clamped);
        type("dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment")
                .getMethod("setData", LivingEntity.class, oldData.getClass())
                .invoke(null, player, newData);
    }

    /** Exposed for exact-version diagnostic readiness; never infer success from class presence alone. */
    public static boolean methodsAvailable() {
        try {
            type("dev.sterner.witchery.feature.affliction.vampire.VampireLeveling")
                    .getMethod("setLevel", ServerPlayer.class, int.class);
            type("de.teamlapen.vampirism.api.VampirismAPI")
                    .getMethod("vampirePlayer", Player.class);
            return true;
        } catch (ReflectiveOperationException | LinkageError e) {
            return false;
        }
    }
}
