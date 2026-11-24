package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.GoetyRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.GoetyRitualContext;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.GoetyStormReceipt;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RitualSettlement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** The delayed effect is native. No added weather change, timer, or spawn. */
@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.entities.util.StormEntity",remap=false)
public abstract class GoetyStormRitualMixin implements GoetyStormReceipt {
    @Unique private static final String RECIPE="goety:summon_storm";
    @Unique private static final String KEY="tyr_progression_goety_storm";
    @Unique private GoetyRitualProof tmm$op;
    @Unique private boolean tmm$changed;
    @Unique private Entity tmm$entity(){return (Entity)(Object)this;}
    @Override public void tmm$armStorm(GoetyRitualProof proof){
        if(tmm$op==null&&proof!=null&&proof.matches(RECIPE,proof.actor())&&
            tmm$entity().level() instanceof ServerLevel world&&world.getServer().isSameThread()&&world.getEntity(tmm$entity().getUUID())==tmm$entity()&&GoetyRitualContext.supported(RECIPE))tmm$op=proof;
    }
    @WrapMethod(method="tick()V",require=0,remap=false)
    private void tmm$tick(Operation<Void> original){
        tmm$changed=false;
        try{
            original.call();
            if(tmm$entity().isRemoved()){
                var op=tmm$op;tmm$op=null;
                if(tmm$changed&&op!=null&&MxpMod.active()&&tmm$entity().level() instanceof ServerLevel world&&world.getServer().isSameThread()){
                    var player=world.getServer().getPlayerList().getPlayer(op.actor());
                    RitualSettlement.settle(player,op.receipt(true,true));
                }
            }
        }finally{tmm$changed=false;}
    }
    @WrapOperation(method="tick()V",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;setWeatherParameters(IIZZ)V"),require=0,allow=1,remap=false)
    private void tmm$weather(ServerLevel world,int clear,int rain,boolean raining,boolean thunder,Operation<Void> original){
        boolean watch=tmm$op!=null&&world==tmm$entity().level()&&world.getServer().isSameThread()&&raining&&thunder&&GoetyRitualContext.supported(RECIPE);
        boolean beforeThunder=watch&&world.getLevelData().isThundering();
        original.call(world,clear,rain,raining,thunder);
        if(watch&&!beforeThunder&&world.getLevelData().isThundering()&&world.getLevelData().isRaining())tmm$changed=true;
    }
    @Inject(method="addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,CallbackInfo ci){
        if(tmm$op!=null&&tmm$op.matches(RECIPE,tmm$op.actor())){
            CompoundTag data=new CompoundTag();data.putUUID("actor",tmm$op.actor());data.putUUID("operation",tmm$op.operation());tag.put(KEY,data);
        }else tag.remove(KEY);
    }
    @Inject(method="readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,CallbackInfo ci){
        tmm$op=null;tmm$changed=false;
        CompoundTag data=tag.getCompound(KEY);
        if(GoetyRitualContext.supported(RECIPE)&&data.hasUUID("actor")&&data.hasUUID("operation")){
            var proof=new GoetyRitualProof(RECIPE,data.getUUID("actor"),data.getUUID("operation"));
            if(proof.matches(RECIPE,proof.actor()))tmm$op=proof;
        }
    }
}
