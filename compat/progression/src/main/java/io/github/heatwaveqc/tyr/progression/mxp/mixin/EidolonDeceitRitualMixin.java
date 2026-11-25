package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.E6bSpawnScope;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
@Pseudo
@Mixin(targets="alexthw.eidolon_repraised.common.ritual.DeceitRitual",remap=false)
public abstract class EidolonDeceitRitualMixin{
    @WrapOperation(method="tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/ai/gossip/GossipContainer;decay()V"),require=0,remap=false)
    private void tmm$decay(GossipContainer gossip,Operation<Void> original,Level world,BlockPos pos){
        Tag before=null;
        if(E6bSpawnScope.observing()&&world instanceof ServerLevel)try{before=gossip.store(NbtOps.INSTANCE).copy();}catch(RuntimeException ignored){}
        original.call(gossip);
        if(before!=null)try{if(!before.equals(gossip.store(NbtOps.INSTANCE)))E6bSpawnScope.effect(this,world);}catch(RuntimeException ignored){}
    }
}
