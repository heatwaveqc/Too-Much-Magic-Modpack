package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.foundation.NativeAttackScope;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.FakePlayer;
import java.util.UUID;
import java.util.function.Supplier;

/** Server-only reward attribution across loaded dimensions, NOT a replacement for the native DamageSource. */
public final class OwnedMagicDamage {
    public static final String MAHOU_BOUNDARY="mahou_drain_life";
    private static final NativeAttackScope SCOPE=new NativeAttackScope();
    private OwnedMagicDamage(){}

    private static boolean live(ServerPlayer player,LivingEntity victim){
        return player!=null&&!(player instanceof FakePlayer)&&player!=victim&&player.isAlive()
            &&!player.isCreative()&&!player.isSpectator()
            &&victim.level() instanceof ServerLevel level
            &&level.getServer().isSameThread()
            &&level.getServer().getPlayerList().getPlayer(player.getUUID())==player;
    }

    /** Call only from a verified native producer using its own persisted owner UUID. */
    public static boolean hurt(String kind,UUID owner,LivingEntity victim,DamageSource source,Supplier<Boolean> original){
        ServerPlayer player=owner!=null&&victim.level() instanceof ServerLevel level
            &&level.getServer().isSameThread()?level.getServer().getPlayerList().getPlayer(owner):null;
        // Push even unresolved frames so nested operations cannot borrow an outer owner.
        return SCOPE.around(kind,live(player,victim)?player:null,victim,source,original);
    }

    /** Initial approved provider only. Native player credit always takes precedence. */
    public static ServerPlayer boundaryOwner(LivingEntity victim,DamageSource source){
        if(source.getDirectEntity() instanceof ServerPlayer||source.getEntity() instanceof ServerPlayer)return null;
        Object candidate=SCOPE.owner(MAHOU_BOUNDARY,victim,source);
        return candidate instanceof ServerPlayer player&&live(player,victim)?player:null;
    }
}
