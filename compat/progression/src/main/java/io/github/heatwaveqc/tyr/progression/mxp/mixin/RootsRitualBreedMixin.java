package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RootsRitualContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
@Pseudo
@Mixin(targets={"elucent.rootsclassic.ritual.rituals.RitualMassBreed"},remap=false)
public abstract class RootsRitualBreedMixin {
    @WrapOperation(method="doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;Ljava/util/List;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/animal/Animal;setInLove(Lnet/minecraft/world/entity/player/Player;)V"),require=0,remap=false)
    private void tmm$breed(Animal animal,Player player,Operation<Void> original){
        boolean watch=animal.isAlive()&&animal.getAge()==0&&!animal.isInLove()&&RootsRitualContext.watching(this,Kind.BREED,animal.level());
        original.call(animal,player);
        if(watch)RootsRitualContext.changed(this,Kind.BREED,animal.level(),animal.isInLove());
    }
}
