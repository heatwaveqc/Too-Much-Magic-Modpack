package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.foundation.NativeAttackScope;
import io.github.heatwaveqc.tyr.progression.foundation.HunterAttackerPolicy;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.FakePlayer;
import java.util.UUID;
import java.util.function.Supplier;

/** Server-only reward attribution across loaded dimensions, NOT a replacement for the native DamageSource. */
public final class OwnedMagicDamage {
    public static final String MAHOU_BOUNDARY="mahou_drain_life", NEO_WELL="neo_well_of_suffering";
    private static final NativeAttackScope SCOPE=new NativeAttackScope();
    private OwnedMagicDamage(){}

    private static boolean online(ServerPlayer player,LivingEntity victim){
        return player!=null&&!(player instanceof FakePlayer)&&player!=victim
            &&!player.isCreative()&&!player.isSpectator()
            &&victim.level() instanceof ServerLevel level
            &&level.getServer().isSameThread()
            &&level.getServer().getPlayerList().getPlayer(player.getUUID())==player;
    }

    private static boolean live(ServerPlayer player,LivingEntity victim){
        return online(player,victim)&&player.isAlive();
    }

    /** Call only from a verified native producer using its own persisted owner UUID. */
    public static boolean hurt(String kind,UUID owner,LivingEntity victim,DamageSource source,Supplier<Boolean> original){
        ServerPlayer player=owner!=null&&victim.level() instanceof ServerLevel level
            &&level.getServer().isSameThread()?level.getServer().getPlayerList().getPlayer(owner):null;
        // Push even unresolved frames so nested operations cannot borrow an outer owner.
        return SCOPE.around(kind,live(player,victim)?player:null,victim,source,original);
    }

    /** AS compatibility was approved only for the Boundary. */
    public static ServerPlayer boundaryOwner(LivingEntity victim,DamageSource source){
        return owner(MAHOU_BOUNDARY,victim,source);
    }

    /** Native player identity first; ownerless damage requires an exact approved scope.
     * Never traverse a mob's owner, tame, summon, projectile or last-attacker chain.
     */
    public static ServerPlayer hunterOwner(LivingEntity victim,DamageSource source){
        Entity resolved=HunterAttackerPolicy.resolve(source.getEntity(),source.getDirectEntity(),
            entity->entity instanceof ServerPlayer player&&online(player,victim),
            ()->{
                ServerPlayer boundary=boundaryOwner(victim,source);
                return boundary!=null?boundary:owner(NEO_WELL,victim,source);
            });
        return resolved instanceof ServerPlayer player?player:null;
    }

    private static ServerPlayer owner(String kind,LivingEntity victim,DamageSource source){
        if(source.getDirectEntity() instanceof ServerPlayer||source.getEntity() instanceof ServerPlayer)return null;
        Object candidate=SCOPE.owner(kind,victim,source);
        return candidate instanceof ServerPlayer player&&live(player,victim)?player:null;
    }
}
