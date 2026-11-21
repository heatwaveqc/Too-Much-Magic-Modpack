package io.github.heatwaveqc.tyr.progression.runtime;

import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** Victim-only optional native ownership checks; never an attacker attribution chain. */
public final class HunterNativePreyOwnership {
    private static final String GOETY = "com.Polarice3.Goety.api.entities.IOwned";
    private static final String ETHEREAL = "dev.sterner.witchery_forbidden_magic.feature.necromancy.EtherealEntityAttachment";
    private HunterNativePreyOwnership() {}

    private static boolean pinned(String mod, String version) {
        return ModList.get().getModContainerById(mod)
            .filter(c -> c.getModInfo().getVersion().toString().startsWith(version)).isPresent();
    }

    public static boolean excluded(LivingEntity victim) {
        if (pinned("goety", "3.2.2")) {
            try {
                Class<?> owned = Class.forName(GOETY);
                if (owned.isInstance(victim)) {
                    Object owner = owned.getMethod("getTrueOwner").invoke(victim);
                    Object id = owned.getMethod("getOwnerId").invoke(victim);
                    if (owner instanceof Player || (id != null && !(owner instanceof LivingEntity))) return true;
                }
            } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
                // An ABI failure is not evidence of hostile unowned prey.
                return true;
            }
        }
        if (pinned("witchery_forbidden_magic", "0.5.37")) {
            try {
                Class<?> attachment = Class.forName(ETHEREAL);
                Object data = attachment.getMethod("getData", LivingEntity.class).invoke(null, victim);
                if (data != null && Boolean.TRUE.equals(data.getClass().getMethod("isEthereal").invoke(data))) {
                    Object id = data.getClass().getMethod("getOwnerUUID").invoke(data);
                    if (!(id instanceof UUID uuid) || !(victim.level() instanceof ServerLevel level)) return true;
                    var owner = level.getEntity(uuid);
                    if (!(owner instanceof LivingEntity) || owner instanceof Player) return true;
                }
            } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
                return true;
            }
        }
        return false;
    }
}
