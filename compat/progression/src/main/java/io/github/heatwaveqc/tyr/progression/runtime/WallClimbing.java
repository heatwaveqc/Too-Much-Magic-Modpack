package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import java.util.Set;
import java.util.stream.Collectors;

/** Uses native climbable movement on both sides, via an existing permission projection. */
public final class WallClimbing {
    private WallClimbing(){}
    public static final String POWER="tyr_progression:wall_climbing";
    public static void register(){PassivePowers.register(POWER,WallClimbing::sources);}
    private static Set<String> sources(ServerPlayer p){
        if(!p.isAlive()||p.isSpectator()||p.isCreative()||NewbieMode.isNewbie(p)
            ||p instanceof net.neoforged.neoforge.common.util.FakePlayer)return Set.of();
        return EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("wall_climbing"))
            .map(EffectRuntime.Active::id).collect(Collectors.toUnmodifiableSet());
    }
    public static boolean canClimb(LivingEntity entity){
        if(!(entity instanceof Player p)||!p.isAlive()||p.isSpectator()||p.isCreative()
            ||p.isPassenger()||p.isFallFlying()||p.getAbilities().flying||p.isSwimming()
            ||p.isInWater()||p.isInLava()||!p.horizontalCollision)return false;
        boolean permission=p instanceof ServerPlayer server?PassivePowers.active(server,POWER):
            p.level().isClientSide()&&ClientPowerSnapshot.active(p,POWER);
        if(!permission)return false;
        // Collision shapes, not entities or non-solid decorative blocks.
        // Narrow the vertical span so floors and ceilings alone cannot grant climbing.
        var box=p.getBoundingBox();
        if(box.getYsize()<=0.2)return false;
        var probe=box.deflate(0,0.10,0).inflate(0.075,0,0.075);
        return p.level().getBlockCollisions(p,probe).iterator().hasNext();
    }
}
