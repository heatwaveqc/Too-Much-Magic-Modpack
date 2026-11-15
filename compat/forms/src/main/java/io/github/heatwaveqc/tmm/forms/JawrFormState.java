package io.github.heatwaveqc.tmm.forms;

import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exact native JAWR attachment observer, no NBT guessing and NO write access.
 * Source identifiers audited against pinned JAWR 0.5.39 class signatures.
 * Unknown/unreadable state ALWAYS means no skill effects.
 */
public final class JawrFormState {
    private JawrFormState() {}
    private static final String BASE = "dev.sterner.witchery.feature.affliction.";
    private static final ConcurrentHashMap<String, Accessor> CACHE = new ConcurrentHashMap<>();
    private record Accessor(Method data, Method present) { }

    public static boolean present(Player player, String form) {
        if (!ModList.get().isLoaded("witchery")) return false;
        if (!"vampire".equals(form) && !"werewolf".equals(form)) return false;
        try {
            Accessor accessor = CACHE.computeIfAbsent(form, JawrFormState::resolve);
            Object data = accessor.data().invoke(null, player);
            if (data == null) return false;
            Object value = accessor.present().invoke(data);
            return value instanceof Boolean b && b;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            // Do not fake a form from a cached tag. Refuse effects and purchases.
            return false;
        }
    }

    private static Accessor resolve(String form) {
        try {
            String name = "vampire".equals(form) ? "vampire.VampirePlayerAttachment"
                                                  : "werewolf.WerewolfPlayerAttachment";
            Class<?> type = Class.forName(BASE + name);
            Method getData = type.getMethod("getData", Player.class);
            String predicate = "vampire".equals(form) ? "isVampire" : "isWerewolf";
            Method isPresent = getData.getReturnType().getMethod(predicate);
            return new Accessor(getData, isPresent);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("JAWR pinned attachment API mismatch: " + form, exception);
        }
    }
}
