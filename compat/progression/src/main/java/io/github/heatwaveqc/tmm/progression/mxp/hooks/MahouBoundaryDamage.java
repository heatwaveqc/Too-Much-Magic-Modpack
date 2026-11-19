package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.runtime.OwnedMagicDamage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import java.util.UUID;
import java.util.function.Supplier;

/** One actual terminal hurt per verified native Boundary operation, not any arbitrary last attacker. */
public final class MahouBoundaryDamage {
    private static final ThreadLocal<Frame> ACTIVE=new ThreadLocal<>();
    private MahouBoundaryDamage(){}
    private static final class Frame {
        final UUID owner;
        final LivingEntity victim,caster;
        final boolean fallback;
        boolean used;
        Frame(UUID owner,LivingEntity victim,LivingEntity caster,boolean fallback){
            this.owner=owner;this.victim=victim;this.caster=caster;this.fallback=fallback;
        }
    }
    private static boolean pinned(){
        return ModList.get().getModContainerById("mahoutsukai").filter(mod->{
            String version=mod.getModInfo().getVersion().toString();
            return version.equals("1.36.31")||version.equals("v1.36.31")
                ||version.equals("1.21.1-v1.36.31")||version.equals("1.21.1-1.36.31");
        }).isPresent();
    }
    public static void attack(Object boundary,LivingEntity victim,LivingEntity caster,boolean fallback,Runnable original){
        UUID owner=null;
        if(pinned()&&boundary instanceof BlockEntity tile&&tile.getLevel()==victim.level()
            &&caster instanceof ServerPlayer player){
            Object stored=MxpReflect.call(boundary,"getCasterUUID");
            if(stored instanceof UUID uuid&&uuid.equals(player.getUUID()))owner=uuid;
        }
        Frame previous=ACTIVE.get(),frame=new Frame(owner,victim,caster,fallback);
        ACTIVE.set(frame);
        try{original.run();}
        finally{if(previous==null)ACTIVE.remove();else ACTIVE.set(previous);}
    }
    public static boolean hurt(LivingEntity victim,DamageSource source,LivingEntity caster,Supplier<Boolean> original){
        Frame frame=ACTIVE.get();
        if(frame==null||frame.used||frame.victim!=victim||frame.caster!=caster)return original.get();
        frame.used=true;
        if(frame.owner==null||!(frame.fallback?source.is(DamageTypes.GENERIC):source.is(DamageTypes.MAGIC)))
            return original.get();
        return OwnedMagicDamage.hurt(OwnedMagicDamage.MAHOU_BOUNDARY,frame.owner,victim,source,original);
    }
}
