package io.github.heatwaveqc.traders.vengeance.runtime;

import io.github.heatwaveqc.traders.vengeance.state.TraderBlacklist;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

/** Never retain vendetta targeting after the owner pardons a player. */
public final class WantedPlayerTargetGoal extends NearestAttackableTargetGoal<Player> {
    private final Mob defender;
    private final ServerLevel world;

    public WantedPlayerTargetGoal(Mob defender, ServerLevel world) {
        super(defender, Player.class, 10, true, false,
            candidate -> candidate instanceof ServerPlayer player
                && !player.isCreative() && !player.isSpectator()
                && TraderBlacklist.get(world).contains(player.getUUID()));
        this.defender = defender;
        this.world = world;
    }

    @Override
    public boolean canContinueToUse() {
        return defender.getTarget() instanceof ServerPlayer player
            && !player.isCreative() && !player.isSpectator()
            && TraderBlacklist.get(world).contains(player.getUUID())
            && super.canContinueToUse();
    }
}
