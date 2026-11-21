package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.EnchantedCurseWitness;
import net.minecraft.server.level.ServerLevel;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.RemoveCurseRite",remap=false)
public abstract class EnchantedRemoveCurseMixin{
    @WrapOperation(method="onTick(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/favouriteless/enchanted/api/curses/CurseManager;applyCurse(Lnet/favouriteless/enchanted/api/curses/Curse$Type;Ljava/util/UUID;ILnet/minecraft/server/level/ServerLevel;)V"),require=0,remap=false)
    private void tmm$apply(@Coerce Object manager,@Coerce Object type,UUID target,int strength,ServerLevel level,Operation<Void> original){
        var before=EnchantedCurseWitness.eligible(this,type,target,level,true)?EnchantedCurseWitness.snapshot(manager,type,target,level):null;
        original.call(manager,type,target,strength,level);
        EnchantedCurseWitness.changed(this,manager,type,target,level,before,true);
    }
    @WrapOperation(method="onTick(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/favouriteless/enchanted/api/curses/CurseManager;removeCurse(Lnet/favouriteless/enchanted/api/curses/Curse$Type;Ljava/util/UUID;Lnet/minecraft/server/level/ServerLevel;)V"),require=0,remap=false)
    private void tmm$remove(@Coerce Object manager,@Coerce Object type,UUID target,ServerLevel level,Operation<Void> original){
        var before=EnchantedCurseWitness.eligible(this,type,target,level,true)?EnchantedCurseWitness.snapshot(manager,type,target,level):null;
        original.call(manager,type,target,level);
        EnchantedCurseWitness.changed(this,manager,type,target,level,before,true);
    }
}
