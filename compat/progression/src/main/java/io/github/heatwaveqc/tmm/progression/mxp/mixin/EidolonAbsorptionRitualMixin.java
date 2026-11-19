package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.E6bSpawnScope;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
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
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.world.entity.Entity.RemovalReason;
@Pseudo
@Mixin(targets="alexthw.eidolon_repraised.common.ritual.AbsorptionRitual",remap=false)
public abstract class EidolonAbsorptionRitualMixin{
    @Unique private Map<Object,ItemStack> tmm$before;
    @Unique private boolean tmm$removed;
    @Inject(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At("HEAD"),require=0,remap=false)
    private void tmm$start(Level world,BlockPos pos,CallbackInfoReturnable<Object> cir){
        tmm$before=E6bSpawnScope.observing()&&world instanceof ServerLevel?new IdentityHashMap<>():null;tmm$removed=false;
    }
    @WrapOperation(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lalexthw/eidolon_repraised/api/ritual/IRitualItemFocus;provide()Lnet/minecraft/world/item/ItemStack;"),require=0,remap=false)
    private ItemStack tmm$provide(@Coerce Object focus,Operation<ItemStack> original){
        ItemStack result=original.call(focus);
        if(tmm$before!=null&&result!=null&&!result.isEmpty())tmm$before.putIfAbsent(focus,result.copy());
        return result;
    }
    @WrapOperation(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V"),require=0,remap=false)
    private void tmm$remove(LivingEntity entity,RemovalReason reason,Operation<Void> original){
        boolean actual=tmm$before!=null&&!(entity instanceof Player)&&!entity.isRemoved()&&reason==RemovalReason.KILLED;
        original.call(entity,reason);if(actual&&entity.isRemoved())tmm$removed=true;
    }
    @WrapOperation(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lalexthw/eidolon_repraised/api/ritual/IRitualItemFocus;replace(Lnet/minecraft/world/item/ItemStack;)V"),require=0,remap=false)
    private void tmm$replace(@Coerce Object focus,ItemStack output,Operation<Void> original,Level world,BlockPos pos){
        ItemStack before=tmm$before==null?null:tmm$before.get(focus);
        original.call(focus,output);
        if(tmm$removed&&before!=null&&output!=null&&!output.isEmpty()&&
            "eidolon_repraised:summoning_staff".equals(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(output.getItem()).toString())&&
            before.getItem()==output.getItem()&&!ItemStack.isSameItemSameComponents(before,output)&&
            MxpReflect.call(focus,"provide") instanceof ItemStack actual&&ItemStack.isSameItemSameComponents(actual,output)&&actual.getCount()==output.getCount())
            E6bSpawnScope.effect(this,world);
    }
    @Inject(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At("RETURN"),require=0,remap=false)
    private void tmm$end(Level world,BlockPos pos,CallbackInfoReturnable<Object> cir){tmm$before=null;tmm$removed=false;}
}
