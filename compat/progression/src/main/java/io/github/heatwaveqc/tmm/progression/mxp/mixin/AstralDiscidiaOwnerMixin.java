package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.runtime.OwnedMagicDamage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/** Native Discidia XP attribution only; no damage mutation or duplicate XP formula. */
@Pseudo
@Mixin(targets="hellfirepvp.astralsorcery.common.perk.tree.perk.root.RootPerkDiscidia",remap=false)
public abstract class AstralDiscidiaOwnerMixin {
    @WrapOperation(method="onAttack(Lnet/neoforged/neoforge/event/entity/living/LivingIncomingDamageEvent;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;"),
        require=0,remap=false)
    private Entity tmm$owner(DamageSource source,Operation<Entity> original,LivingIncomingDamageEvent event){
        Entity nativeOwner=original.call(source);
        if(nativeOwner instanceof ServerPlayer||source.getDirectEntity() instanceof ServerPlayer
            ||event.getSource()!=source)return nativeOwner;
        boolean pinned=ModList.get().getModContainerById("astralsorcery")
            .filter(mod->mod.getModInfo().getVersion().toString().equals("2.0.3.59")).isPresent();
        if(!pinned)return nativeOwner;
        ServerPlayer owner=OwnedMagicDamage.boundaryOwner(event.getEntity(),source);
        return owner==null?nativeOwner:owner;
    }
}
