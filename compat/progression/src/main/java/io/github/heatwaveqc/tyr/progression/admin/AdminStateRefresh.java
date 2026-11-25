package io.github.heatwaveqc.tyr.progression.admin;

import io.github.heatwaveqc.tyr.progression.runtime.*;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
import net.minecraft.server.level.ServerPlayer;

/** Reconcile Progression-owned derived state only, without raw native state writes. */
public final class AdminStateRefresh {
    private AdminStateRefresh() {}
    public static void reconcile(ServerPlayer player){
        SkillActivation.reconcile(player);
        EffectRuntime.reconcile(player);
        MutationPowers.reconcile(player);
        PassivePowers.reconcile(player,true);
        VirtualElytra.reconcile(player,true);
        VisionRuntime.reconcile(player,true);
        ProgressionMenuNetwork.refresh(player);
    }
}
