package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.PassivePowerGrants;
import io.github.heatwaveqc.tyr.progression.core.PassivePowerLease;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.Map;
import io.github.heatwaveqc.tyr.progression.core.MutationPowerPolicy;
import net.minecraft.world.entity.LivingEntity;

/**
 * Client-side local-entity mirror with no reference to Minecraft.client classes.
 *
 * Stored in common code so the server-loaded VirtualElytra flight predicate
 * never needs to link client-only Minecraft/LocalPlayer bytecode.
 * Only PassivePowerNetwork's playToClient handler calls accept.
 */
public final class ClientPowerSnapshot {
    private ClientPowerSnapshot() {}
    private static WeakReference<LivingEntity> owner = new WeakReference<>(null);
    private static Set<String> active = Set.of();
    private static Map<String,Integer> mutations=Map.of();
    private static int receivedAt = -1;
    private static String dimension = "";

    public static void accept(LivingEntity player, String incomingDimension, String encoded,String mutationData) {
        if (player == null || !player.level().isClientSide()
                || !player.level().dimension().location().toString().equals(incomingDimension)) {
            clear(); return;
        }
        Set<String> parsed;
        try { parsed = PassivePowerGrants.decodeActive(encoded); mutations=MutationPowerPolicy.decode(mutationData); }
        catch (IllegalArgumentException invalid) { clear(); return; }
        owner = new WeakReference<>(player);
        dimension = incomingDimension;
        active = parsed;
        receivedAt = player.tickCount;
    }

    public static boolean active(LivingEntity player, String id) {return valid(player)&&active.contains(id);}
    public static int mutation(LivingEntity player,String id){return valid(player)?mutations.getOrDefault(id,-1):-1;}
    private static boolean valid(LivingEntity player){
        return player != null && player.level().isClientSide()
                && owner.get() == player
                && player.level().dimension().location().toString().equals(dimension)
                && player.isAlive() && !player.isSpectator()
                && (!(player instanceof net.minecraft.world.entity.player.Player actor) || !actor.isCreative())
                && PassivePowerLease.valid(receivedAt, player.tickCount);
    }

    public static void clear() {
        owner = new WeakReference<>(null);
        active = Set.of();
        mutations=Map.of();
        dimension = "";
        receivedAt = -1;
    }
}
