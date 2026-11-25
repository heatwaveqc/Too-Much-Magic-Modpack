package io.github.heatwaveqc.tyr.progression.compat.physiology;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Presence;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Snapshot;
import java.lang.reflect.Method;
import java.util.EnumMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Single read-only Progression source for the five optional native providers.
 * Never creates a shadow transformation state, modifies native attachments or
 * substitutes a scoreboard tag for real provider evidence.
 *
 * A missing optional mod means ABSENT; a present but unreadable/unpinned mod
 * means UNKNOWN. The latter must fail closed during form/path admission.
 */
public final class NativePhysiologyObservations {
    private NativePhysiologyObservations() {}

    private static final String JAWR = "dev.sterner.witchery.feature.affliction.";
    private static final String FORBIDDEN =
        "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichPlayerAttachment";
    private static final String DRAGON =
        "by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider";

    @FunctionalInterface
    private interface BooleanProbe {
        boolean present(ServerPlayer player) throws ReflectiveOperationException;
    }

    private static Presence pinned(String modId, String version, ServerPlayer player,
                                   BooleanProbe read) {
        var container = ModList.get().getModContainerById(modId);
        if (container.isEmpty()) return Presence.ABSENT;
        if (!container.get().getModInfo().getVersion().toString().startsWith(version))
            return Presence.UNKNOWN;
        try {
            return read.present(player) ? Presence.PRESENT : Presence.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException unavailable) {
            ProgressionMod.LOGGER.warn(
                "Native {} observation unavailable; refusing to infer ABSENT", modId, unavailable);
            return Presence.UNKNOWN;
        }
    }

    private static boolean attachmentFlag(String typeName, String flag, ServerPlayer player)
        throws ReflectiveOperationException {
        Class<?> attachment = Class.forName(typeName);
        Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
        if (data == null) throw new ReflectiveOperationException("Null native attachment: " + typeName);
        Object result = data.getClass().getMethod(flag).invoke(data);
        if (!(result instanceof Boolean state))
            throw new ReflectiveOperationException("Non-boolean native flag: " + flag);
        return state;
    }

    private static boolean goetyLich(ServerPlayer player) throws ReflectiveOperationException {
        Class<?> helper = Class.forName("com.Polarice3.Goety.utils.LichdomHelper");
        Object value = helper.getMethod("isLich", Player.class).invoke(null, player);
        if (!(value instanceof Boolean active))
            throw new ReflectiveOperationException("Invalid Goety lich state");
        return active;
    }

    private static void setDragon(EnumMap<Form, Presence> observations, ServerPlayer player) {
        var container = ModList.get().getModContainerById("dragonsurvival");
        if (container.isEmpty()) {
            setAllDragons(observations, Presence.ABSENT);
            return;
        }
        if (!container.get().getModInfo().getVersion().toString().startsWith("2.0.74")) {
            setAllDragons(observations, Presence.UNKNOWN);
            return;
        }
        try {
            Class<?> provider = Class.forName(DRAGON);
            Object data = provider.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) throw new ReflectiveOperationException("Null Dragon Survival state");
            Object present = data.getClass().getMethod("isDragon").invoke(data);
            if (!(present instanceof Boolean dragon))
                throw new ReflectiveOperationException("Dragon Survival isDragon not boolean");
            if (!dragon) {
                setAllDragons(observations, Presence.ABSENT);
                return;
            }
            Object nativeId = data.getClass().getMethod("speciesId").invoke(data);
            if (!(nativeId instanceof ResourceLocation id))
                throw new ReflectiveOperationException("Missing Dragon Survival species");
            Form species = switch (id.toString()) {
                case "dragonsurvival:cave_dragon" -> Form.CAVE_DRAGON;
                case "dragonsurvival:forest_dragon" -> Form.FOREST_DRAGON;
                case "dragonsurvival:sea_dragon" -> Form.SEA_DRAGON;
                default -> null;
            };
            if (species == null) {
                setAllDragons(observations, Presence.UNKNOWN);
                return;
            }
            setAllDragons(observations, Presence.ABSENT);
            observations.put(species, Presence.PRESENT);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            ProgressionMod.LOGGER.warn("Dragon Survival species observation unavailable", failure);
            setAllDragons(observations, Presence.UNKNOWN);
        }
    }

    private static void setAllDragons(EnumMap<Form, Presence> observations, Presence value) {
        observations.put(Form.CAVE_DRAGON, value);
        observations.put(Form.FOREST_DRAGON, value);
        observations.put(Form.SEA_DRAGON, value);
    }

    /** Server-thread only; unknown observations never turn into human form. */
    public static Snapshot observe(ServerPlayer player) {
        EnumMap<Form, Presence> found = new EnumMap<>(Form.class);
        if (player == null || player.getServer() == null
                || !player.getServer().isSameThread()) {
            for (Form form : Form.values()) found.put(form, Presence.UNKNOWN);
            return new Snapshot(found);
        }
        found.put(Form.JAWR_VAMPIRE,
            pinned("witchery", "0.5.39", player, p ->
                attachmentFlag(JAWR + "vampire.VampirePlayerAttachment", "isVampire", p)));
        found.put(Form.JAWR_WEREWOLF,
            pinned("witchery", "0.5.39", player, p ->
                attachmentFlag(JAWR + "werewolf.WerewolfPlayerAttachment", "isWerewolf", p)));
        found.put(Form.GOETY_LICH,
            pinned("goety", "3.2.3", player, NativePhysiologyObservations::goetyLich));
        found.put(Form.FORBIDDEN_MAGIC_LICH,
            pinned("witchery_forbidden_magic", "0.5.37", player, p ->
                attachmentFlag(FORBIDDEN, "isLich", p)));
        setDragon(found, player);
        return new Snapshot(found);
    }
}
