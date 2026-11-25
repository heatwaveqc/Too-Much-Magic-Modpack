package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.GoetyRitualContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.ritual.ConvertRitual",remap=false)
public abstract class GoetyRitualConvertMixin {
    @WrapOperation(method="finish(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/utils/MobUtil;convertTo(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EntityType;ZZLnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/entity/Entity;"),require=0,allow=1,remap=false)
    private Entity tmm$convert(Entity source,EntityType<?> target,boolean loot,boolean equipment,Player owner,Operation<Entity> original){
        Entity result=original.call(source,target,loot,equipment,owner);
        GoetyRitualContext.converted(this,source,target,result);
        return result;
    }
}
