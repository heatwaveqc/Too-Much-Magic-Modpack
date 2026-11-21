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
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;
@Pseudo
@Mixin(targets="alexthw.eidolon_repraised.common.ritual.PurifyRitual",remap=false)
public abstract class EidolonVillagerPurifyMixin{
    @WrapOperation(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/monster/ZombieVillager;finishConversion(Lnet/minecraft/server/level/ServerLevel;)V"),require=0,remap=false)
    private void tmm$cure(ZombieVillager zombie,ServerLevel level,Operation<Void> original){
        var bounds=zombie.getBoundingBox().inflate(2.0);
        Set<UUID> prior=E6bSpawnScope.observing()&&!zombie.isRemoved()?level.getEntitiesOfClass(Villager.class,bounds).stream().map(Entity::getUUID).collect(Collectors.toSet()):null;
        original.call(zombie,level);
        if(prior!=null&&zombie.isRemoved()&&level.getEntitiesOfClass(Villager.class,bounds).stream().anyMatch(v->!v.isRemoved()&&!prior.contains(v.getUUID())))
            E6bSpawnScope.effect(this,level);
    }
}
